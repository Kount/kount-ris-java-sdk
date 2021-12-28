package com.kount.ris.transport;

import com.kount.ris.util.RisTransportException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * RIS http data transport class.
 * </p>
 * Works with JWT (JSON Web Token) authentication, following the RFC 7519
 * standard. The used key is set as connection header with name
 * 'X-Kount-Api-Key'.
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class HttpApiTransport extends Transport {

    public static final int DEFAULT_MAX_CONNECTIONS = 256;
    public static final int DEFAULT_CONNECTION_IDLE_TIMEOUT_MINUTES = 1;
    public static final int DEFAULT_CONNECTION_TIMEOUT_MS = 10000;
    public static final int DEFAULT_SOCKET_TIMEOUT_MS = 10000;
    public static final String CUSTOM_HEADER_MERCHANT_ID = "X-Kount-Merc-Id";
    public static final String CUSTOM_HEADER_API_KEY = "X-Kount-Api-Key";

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger(HttpApiTransport.class);

    private static final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    static {
        connManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS);
    }

    /**
     * Cache the api key (minimize file reads to once per instatiation).
     */
    protected String apiKey;

    private volatile CloseableHttpClient httpClient;

    /**
     * Connection Time To Live
     */
    private int connectionTimeToLive;

    /**
     * Default transport constructor.
     */
    public HttpApiTransport() {
        connectTimeout = DEFAULT_CONNECTION_TIMEOUT_MS;
        readTimeout = DEFAULT_SOCKET_TIMEOUT_MS;
        connectionTimeToLive = DEFAULT_CONNECTION_IDLE_TIMEOUT_MINUTES;
    }

    /**
     * Constructor that accepts a RIS url and an api key as input.
     *
     * @param url RIS server url.
     * @param key API key.
     */
    public HttpApiTransport(URL url, String key) {
        this();
        setRisServerUrl(url.toString());
        setApiKey(key);
    }

    /**
     * Constructor that accepts a RIS url and an api key as input.
     *
     * @param url                    RIS server url.
     * @param key                    API key.
     * @param maxConnections         connection Pool Threads.
     * @param maxConnectionsPerRoute connection Per Route.
     */
    public HttpApiTransport(URL url, String key, int maxConnections, int maxConnectionsPerRoute) {
        this();
        setRisServerUrl(url.toString());
        setApiKey(key);
        connManager.setMaxTotal(maxConnections);
        connManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
    }

    /**
     * Set API Key.
     *
     * @param key String Kount Api Key (public) to use for authentication with RIS
     *            server.
     */
    public void setApiKey(String key) {
        apiKey = key;
    }

    /**
     * Set Connection Time To Live.
     *
     * @param minutes integer specifying the connection time to live in minutes.
     */
    public void setConnectionTimeToLive(int minutes) {
        connectionTimeToLive = minutes;
    }

    private CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (this) {
                if (httpClient == null) {
                    httpClient = HttpClients.custom()
                            .useSystemProperties()
                            .setConnectionTimeToLive(connectionTimeToLive, TimeUnit.MINUTES)
                            .setDefaultSocketConfig(SocketConfig.custom()
                                    .setSoTimeout(this.readTimeout)
                                    .build())
                            .setDefaultRequestConfig(RequestConfig.custom()
                                    .setConnectTimeout(this.connectTimeout)
                                    .setSocketTimeout(this.readTimeout)
                                    .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                                    .build())
                            .setConnectionManager(connManager)
                            .build();
                }
            }
        }

        return httpClient;
    }

    /**
     * Send transaction data to RIS.
     *
     * @param params Map of data to send
     * @return Reader for character stream returned by RIS
     * @throws RisTransportException RIS transport exception
     */
    public Reader send(Map<String, String> params) throws RisTransportException {
        if (!params.containsKey("PTOK") || ("KHASH".equals(params.get("PENC")) && null == params.get("PTOK"))) {
            params.put("PENC", "");
        }

        final Reader reader;
        try {
            long startTime = System.currentTimeMillis();

            HttpPost httpPost = new HttpPost(this.risServerUrl);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader(CUSTOM_HEADER_API_KEY, this.apiKey);
            httpPost.addHeader(CUSTOM_HEADER_MERCHANT_ID, params.get("MERC"));
            httpPost.setEntity(new UrlEncodedFormEntity(convertToNameValuePair(params)));

            try (CloseableHttpResponse httpResponse = getHttpClient().execute(httpPost)) {
                //read all request data since we need to close the response object
                reader = new InputStreamReader(readAllIntput(httpResponse.getEntity()));

                if (logger.isDebugEnabled()) {
                    long elapsed = (System.currentTimeMillis() - startTime);

                    StringBuilder builder = new StringBuilder();
                    builder.append("MERC = ").append(params.get("MERC"));
                    builder.append(" SESS = ").append(params.get("SESS"));
                    builder.append(" elapsed = ").append(elapsed).append(" ms.");

                    logger.debug(builder.toString());
                }
            }
        } catch (Exception ioe) {
            logger.error("Error fetching RIS response", ioe);
            throw new RisTransportException("An error occurred while getting the RIS response", ioe);
        }

        return reader;
    }

    public ByteArrayInputStream readAllIntput(HttpEntity entity) throws IOException {
        try {
            InputStream is = entity.getContent();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return new ByteArrayInputStream(buffer.toByteArray());
        } finally {
            EntityUtils.consume(entity);
        }
    }
}
