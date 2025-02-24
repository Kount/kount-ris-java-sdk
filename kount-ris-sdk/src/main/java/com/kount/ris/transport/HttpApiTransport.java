package com.kount.ris.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kount.ris.Response;
import com.kount.ris.RisConfigurationConstants;
import com.kount.ris.util.RisResponseException;
import com.kount.ris.util.RisTransportException;
import org.apache.hc.client5.http.classic.HttpClient;
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
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
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
 * @copyright 2010 Keynetics Inc
 */
public class HttpApiTransport extends Transport {

    public static final int DEFAULT_MAX_CONNECTIONS = 256;
    public static final int DEFAULT_CONNECTION_IDLE_TIMEOUT_MINUTES = 1;
    public static final int DEFAULT_CONNECTION_TIMEOUT_MS = 10000;
    public static final int DEFAULT_SOCKET_TIMEOUT_MS = 10000;
    public static final String CUSTOM_HEADER_MERCHANT_ID = "X-Kount-Merc-Id";
    public static final String CUSTOM_HEADER_API_KEY = "X-Kount-Api-Key";
    public static final String PF_AUTH_HEADER = "Authorization";

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
        connManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
        connManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS);
    }

    /**
     * Cache the api key (minimize file reads to once per instatiation).
     */
    protected String apiKey;

    private CloseableHttpClient httpClient;

    private static boolean migrationModeEnabled = false;
    private static boolean initialized = false;
    private static String paymentsFraudApiEndpoint = "";
    private static String paymentsFraudAuthEndpoint = "";
    private static String paymentsFraudClientId = "";
    private static String paymentsFraudApiKey = "";
    private static BearerAuthResponse bearer = new BearerAuthResponse();
    private static final ReentrantReadWriteLock bearerRWLock = new ReentrantReadWriteLock();
    private static final Lock bearerReadLock = bearerRWLock.readLock();
    private static final Lock bearerWriteLock = bearerRWLock.writeLock();

    /**
     * Connection Time To Live
     */
    private int connectionTimeToLive;

    /**
     * Default transport constructor.
     */
    public HttpApiTransport() throws ConfigurationException {
        connectTimeout = DEFAULT_CONNECTION_TIMEOUT_MS;
        readTimeout = DEFAULT_SOCKET_TIMEOUT_MS;
        connectionTimeToLive = DEFAULT_CONNECTION_IDLE_TIMEOUT_MINUTES;
        checkMigrationMode();
    }

    /**
     * Constructor that accepts a RIS url and an api key as input.
     *
     * @param url RIS server url.
     * @param key API key.
     */
    public HttpApiTransport(URL url, String key) throws ConfigurationException {
        this();
        setRisServerUrl(url.toString());
        setApiKey(key);
        checkMigrationMode();
    }

    /**
     * Constructor that accepts a RIS url and an api key as input.
     *
     * @param url                    RIS server url.
     * @param key                    API key.
     * @param maxConnections         connection Pool Threads.
     * @param maxConnectionsPerRoute connection Per Route.
     */
    public HttpApiTransport(URL url, String key, int maxConnections, int maxConnectionsPerRoute) throws ConfigurationException {
        this();
        setRisServerUrl(url.toString());
        setApiKey(key);
        connManager.setMaxTotal(maxConnections);
        connManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        checkMigrationMode();
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

    private void checkMigrationMode() throws ConfigurationException {
        String migrationModeStr = System.getProperty(RisConfigurationConstants.PROPERTY_MIGRATION_MODE_ENABLED, "false");
        migrationModeEnabled = Boolean.parseBoolean(migrationModeStr);

        if (migrationModeEnabled && !initialized) {

            paymentsFraudApiEndpoint = System.getProperty(RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_API_ENDPOINT, "");
            if (Objects.equals(paymentsFraudApiEndpoint, "")){
                throw new ConfigurationException("Migration mode is enabled but '" + RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_API_ENDPOINT + "' is not configured");
            }

            paymentsFraudAuthEndpoint = System.getProperty(RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_AUTH_ENDPOINT, "");
            if (Objects.equals(paymentsFraudAuthEndpoint, "")){
                throw new ConfigurationException("Migration mode is enabled but '" + RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_AUTH_ENDPOINT + "' is not configured");
            }

            paymentsFraudClientId = System.getProperty(RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_CLIENT_ID, "");
            if (Objects.equals(paymentsFraudClientId, "")){
                throw new ConfigurationException("Migration mode is enabled but '" + RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_CLIENT_ID + "' is not configured");
            }

            paymentsFraudApiKey = System.getProperty(RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_API_KEY, "");
            if (Objects.equals(paymentsFraudApiKey, "")){
                throw new ConfigurationException("Migration mode is enabled but '" + RisConfigurationConstants.PROPERTY_PAYMENTS_FRAUD_API_KEY + "' is not configured");
            }

            initialized = true;
        }
    }

    private static void refreshAuthToken() throws RisTransportException {
        bearerWriteLock.lock();

        if (bearer.expiresAt.isAfter(OffsetDateTime.now().plusSeconds(60))) {
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

            HttpClient httpClient = HttpClientBuilder
                    .create()
                    .setConnectionManager(connManager)
                    .build();

            try (CloseableHttpResponse httpResponse = (CloseableHttpResponse) httpClient.execute(httpPost);
                 Reader reader = new InputStreamReader(readAllInput(httpResponse.getEntity()));
            ) {
                if (httpResponse.getCode() < 400) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    BearerAuthResponse authResponse = new BearerAuthResponse();
                    //convert json string to object
                    authResponse = objectMapper.readValue(reader, BearerAuthResponse.class);
                    if (
                            !authResponse.createdAt.equals(authResponse.expiresAt) &&
                                authResponse.expiresAt.isAfter(bearer.expiresAt)
                    ) {
                        bearerReadLock.lock();
                        bearer = authResponse;
                        bearerReadLock.unlock();
                    } else {
                        logger.warn("new auth token expires before existing one, keeping existing one");
                    }

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
        bearerWriteLock.unlock();
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

    public Response sendResponse(Map<String, String> params) throws RisTransportException {
        if (!params.containsKey("PTOK") || ("KHASH".equals(params.get("PENC")) && null == params.get("PTOK"))) {
            params.put("PENC", "");
        }     
        try {
            if (migrationModeEnabled && bearer.expiresAt.isBefore(OffsetDateTime.now().plusSeconds(60))) {
                refreshAuthToken();
            }
            long startTime = System.currentTimeMillis();

            HttpPost httpPost;

            if(migrationModeEnabled) {
                httpPost = new HttpPost(paymentsFraudApiEndpoint);

                bearerReadLock.lock();
                httpPost.addHeader(PF_AUTH_HEADER,  bearer.tokenType + " " + bearer.accessToken);
                bearerReadLock.unlock();

                params.put("MERC", paymentsFraudClientId); // override merc with client id
                httpPost.addHeader(CUSTOM_HEADER_MERCHANT_ID, paymentsFraudClientId);
            } else {
                httpPost = new HttpPost(this.risServerUrl);

                httpPost.addHeader(CUSTOM_HEADER_API_KEY, this.apiKey);
                httpPost.addHeader(CUSTOM_HEADER_MERCHANT_ID, params.get("MERC"));
            }

            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            httpPost.setEntity(new UrlEncodedFormEntity(convertToNameValuePair(params)));

            try (CloseableHttpResponse httpResponse = getHttpClient().execute(httpPost);
                 Reader reader = new InputStreamReader(readAllInput(httpResponse.getEntity()));
           )
              {
                Response responseObj = parse(reader);
              
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RisTransportException("Error closing reader", e);
                    }
                
                return responseObj;  
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
}
