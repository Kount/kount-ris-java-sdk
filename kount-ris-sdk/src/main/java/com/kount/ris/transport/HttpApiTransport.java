package com.kount.ris.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kount.ris.Response;
import com.kount.ris.util.RisResponseException;
import com.kount.ris.util.RisTransportException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.ConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;



/**
 * RIS http data transport class.
 * </p>
 * Works with JWT (JSON Web Token) authentication, following the RFC 7519
 * standard. The used key is set as connection header with name
 * 'X-Kount-Api-Key'.
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class HttpApiTransport extends Transport {
    public static final int DEFAULT_MAX_CONNECTIONS = 256;
    public static final int DEFAULT_CONNECTION_IDLE_TIMEOUT_MINUTES = 1;
    public static final int DEFAULT_CONNECTION_TIMEOUT_MS = 10000;
    public static final int DEFAULT_SOCKET_TIMEOUT_MS = 10000;
    public static final String CUSTOM_HEADER_MERCHANT_ID = "X-Kount-Merc-Id";
    public static final String CUSTOM_HEADER_API_KEY = "X-Kount-Api-Key";
    protected static final Lock connManagerWriteLock = new ReentrantLock();
    protected boolean migrationModeEnabled = false;
    protected String paymentsFraudApiEndpoint = "https://api.kount.com/commerce/ris";
    protected String paymentsFraudAuthEndpoint = "https://login.kount.com/oauth2/ausdppksgrbyM0abp357/v1/token";
    protected String paymentsFraudClientId = "";
    protected String paymentsFraudApiKey = "";
    protected BearerAuthResponse bearer = new BearerAuthResponse();
    protected static final ReentrantReadWriteLock bearerRWLock = new ReentrantReadWriteLock();
    protected static final Lock bearerReadLock = bearerRWLock.readLock();
    protected static final Lock bearerWriteLock = bearerRWLock.writeLock();
    public static final String PF_AUTH_HEADER = "Authorization";
    private boolean forceUtf8 = false;

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger(HttpApiTransport.class);

    private static final PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setDefaultSocketConfig(SocketConfig.custom()
                    .setSoTimeout(Timeout.ofMinutes(1))
                    .build())
            .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
            .setConnPoolPolicy(PoolReusePolicy.FIFO)
            .setDefaultConnectionConfig(ConnectionConfig.custom()
                    .setSocketTimeout(Timeout.ofMilliseconds(DEFAULT_SOCKET_TIMEOUT_MS))
                    .setConnectTimeout(Timeout.ofMilliseconds(DEFAULT_CONNECTION_TIMEOUT_MS))
                    .setTimeToLive(TimeValue.ofMinutes(DEFAULT_CONNECTION_IDLE_TIMEOUT_MINUTES))
                    .build())
            .build();

    static {
        connManagerWriteLock.lock();
        connManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS);
        connManagerWriteLock.unlock();
    }

    /**
     * Cache the api key (minimize file reads to once per instantiation).
     */
    protected String apiKey;

    private CloseableHttpClient httpClient;

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
        this.risServerUrl = url.toString();
        this.apiKey = key;
    }

    /**
     * Constructor that accepts a RIS url and an api key as input.
     *
     * @param url RIS server url.
     * @param key API key.
     */
    public HttpApiTransport(URL url, String key, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint, boolean forceUtf8) throws ConfigurationException {
        this(url, key);
        this.forceUtf8 = forceUtf8;
        configureMigrationMode(migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint);

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
        this.risServerUrl = url.toString();
        this.apiKey = key;

        connManagerWriteLock.lock();
        connManager.setMaxTotal(maxConnections);
        connManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connManagerWriteLock.unlock();
    }

    /**
     * Constructor that accepts a RIS url and an api key as input.
     *
     * @param url                    RIS server url.
     * @param key                    API key.
     * @param maxConnections         connection Pool Threads.
     * @param maxConnectionsPerRoute connection Per Route.
     */
    public HttpApiTransport(URL url, String key, int maxConnections, int maxConnectionsPerRoute, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint, boolean forceUtf8) throws ConfigurationException {
        this(url, key, maxConnections, maxConnectionsPerRoute);
        this.forceUtf8 = forceUtf8;
        configureMigrationMode(migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint);
    }

    /**
     * Getter
     *
     * @return bearer auth response
     */
    public BearerAuthResponse getBearerResponse() {
        return bearer;
    }

    /**
     * Getter
     *
     * @return is migration mode enabled
     */
    public boolean isMigrationModeEnabled() {
        return migrationModeEnabled;
    }

    /**
     * Using this function is strongly discouraged and will result in warning logs
     *
     * @param customBearer bearer to set
     */
    public void setCustomBearerResponse(BearerAuthResponse customBearer) {
        logger.warn("Setting custom bearer response. This is not recommended.");
        this.bearer = customBearer;
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
                httpClient = HttpClientBuilder
                        .create()
                        .setConnectionManager(connManager)
                        .build();
            }
        }

        return httpClient;
    }

    public static ByteArrayInputStream readAllInput(HttpEntity entity) throws IOException {
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

    public Response sendRequest(Map<String, String> params) throws RisTransportException {
        if (!params.containsKey("PTOK") || ("KHASH".equals(params.get("PENC")) && null == params.get("PTOK"))) {
            params.put("PENC", "");
        }
        try {
            if (migrationModeEnabled && bearer.expiresAt.isBefore(OffsetDateTime.now().plusSeconds(60))) {
                this.refreshAuthToken();
            }

            HttpPost httpPost;

            if (migrationModeEnabled) {
                httpPost = new HttpPost(paymentsFraudApiEndpoint);

                bearerReadLock.lock();
                try {
                    httpPost.addHeader(PF_AUTH_HEADER, bearer.tokenType + " " + bearer.accessToken);
                } finally {
                    bearerReadLock.unlock();
                }

                String clientId = this.paymentsFraudClientId;
                params.put("MERC", clientId); // override merc with client id
                httpPost.addHeader(CUSTOM_HEADER_MERCHANT_ID, clientId);
            } else {
                httpPost = new HttpPost(this.risServerUrl);

                httpPost.addHeader(CUSTOM_HEADER_API_KEY, this.apiKey);
                httpPost.addHeader(CUSTOM_HEADER_MERCHANT_ID, params.get("MERC"));
            }

            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            if (forceUtf8) {
                httpPost.setEntity(new UrlEncodedFormEntity(convertToNameValuePair(params), StandardCharsets.UTF_8));
            } else {
                httpPost.setEntity(new UrlEncodedFormEntity(convertToNameValuePair(params)));
            }

            try (CloseableHttpResponse httpResponse = getHttpClient().execute(httpPost);
                 Reader reader = new InputStreamReader(readAllInput(httpResponse.getEntity()))
            ) {
                return parse(reader);
            }

        } catch (Exception ioe) {
            logger.error("Error fetching RIS response", ioe);
            throw new RisTransportException("An error occurred while getting the RIS response", ioe);
        }
    }

    protected Response parse(Reader r) throws RisResponseException {
        logger.trace("parse()");
        return Response.parseResponse(r);
    }

    protected void configureMigrationMode(boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint) throws ConfigurationException {
        this.migrationModeEnabled = migrationModeEnabled;
        this.paymentsFraudApiKey = paymentsFraudApiKey;
        this.paymentsFraudClientId = paymentsFraudClientId;
        this.paymentsFraudApiEndpoint = paymentsFraudApiEndpoint;
        this.paymentsFraudAuthEndpoint = paymentsFraudAuthEndpoint;


        if (migrationModeEnabled) {
            if (this.paymentsFraudApiKey.isEmpty()) {
                throw new ConfigurationException("migration mode is set to enabled, but Payments Fraud API key is missing");
            }
            if (this.paymentsFraudClientId.isEmpty()) {
                throw new ConfigurationException("migration mode is set to enabled, but Payments Fraud Client ID is missing");
            }
            if (this.paymentsFraudApiEndpoint.isEmpty()) {
                throw new ConfigurationException("migration mode is set to enabled, but Payments Fraud API endpoint is missing");
            }
            if (this.paymentsFraudAuthEndpoint.isEmpty()) {
                throw new ConfigurationException("migration mode is set to enabled, but Payments Fraud Auth endpoint is missing");
            }
        }
    }

    protected void refreshAuthToken() throws RisTransportException {
        bearerWriteLock.lock();

        if (bearer.getExpiresAt().isAfter(OffsetDateTime.now().plusSeconds(60))) {
            // previous thread updated it already
            return;
        }

        try {
            HttpPost httpPost = new HttpPost(paymentsFraudAuthEndpoint);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader(PF_AUTH_HEADER, "Basic " + paymentsFraudApiKey);
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "client_credentials");
            params.put("scope", "k1_integration_api");

            httpPost.setEntity(new UrlEncodedFormEntity(convertToNameValuePair(params)));

            try (CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .build()) {

                try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
                     Reader reader = new InputStreamReader(readAllInput(httpResponse.getEntity()))
                ) {
                    if (httpResponse.getCode() < 400) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        BearerAuthResponse authResponse;
                        //convert json string to object
                        authResponse = objectMapper.readValue(reader, BearerAuthResponse.class);
                        bearerReadLock.lock();
                        bearer = authResponse;
                        bearerReadLock.unlock();

                    } else {
                        bearerWriteLock.unlock();
                        String message = "Error fetching auth token: received " + httpResponse.getCode() + " " + httpResponse.getReasonPhrase();
                        logger.error(message);
                        throw new RisTransportException("An error occurred while reading the auth token response: " + message);
                    }
                }
            } catch (Exception ioe) {
                bearerWriteLock.unlock();
                logger.error("Error fetching updating bearer auth token", ioe);
                throw new RisTransportException("An error occurred while getting the auth token", ioe);
            }
        } finally {
            bearerWriteLock.unlock();
        }
    }
}