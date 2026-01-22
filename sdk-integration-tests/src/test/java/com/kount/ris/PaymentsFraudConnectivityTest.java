package com.kount.ris;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kount.ris.transport.BearerAuthResponse;
import com.kount.ris.util.*;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import javax.naming.ConfigurationException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.kount.ris.transport.HttpApiTransport.PF_AUTH_HEADER;
import static com.kount.ris.transport.HttpApiTransport.readAllInput;
import static com.kount.ris.transport.Transport.convertToNameValuePair;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PaymentsFraudConnectivityTest {
    private static final Logger logger = LogManager.getLogger(PaymentsFraudConnectivityTest.class);

    private final KountRisClient client;
    private final KountRisClient utf8Client;
    private final long merchantId;

    public PaymentsFraudConnectivityTest() throws MalformedURLException, ConfigurationException {
        String paymentsFraudApiKey = TestConfiguration.getPfApiKey();
        String paymentsFraudClientId = TestConfiguration.getPfClientId();
        String paymentsFraudApiEndpoint = TestConfiguration.getPfApiEndpoint();
        String paymentsFraudAuthEndpoint  = TestConfiguration.getPfAuthEndpoint();

        merchantId = Long.parseLong(TestConfiguration.getMerchantID());
        URL serverUrl = new URL(TestConfiguration.getRisURL());
        client = new KountRisClient(
                serverUrl,
                TestConfiguration.getRisAPIKey(),
                true,
                paymentsFraudApiKey,
                paymentsFraudClientId,
                paymentsFraudApiEndpoint,
                paymentsFraudAuthEndpoint
        );
        utf8Client = new KountRisClient(
                serverUrl,
                TestConfiguration.getRisAPIKey(),
                false,
                paymentsFraudApiKey,
                paymentsFraudClientId,
                paymentsFraudApiEndpoint,
                paymentsFraudAuthEndpoint,
                true
        );
    }

    @Test
    public void testHappyPath() throws RisException, UnsupportedEncodingException {
        logger.debug("running test happy path for payments fraud");

        Inquiry inq = getInquiry(merchantId);

        Response response = client.process(inq);
        logger.trace(response.toString());
        assertNotEquals("E", response.getAuto());
        assertNotEquals("", response.getAuto());
    }

    @Test
    void testForceCharset() throws UnsupportedEncodingException, RisException {
        logger.debug("running test forcing charset for payments fraud");

        Inquiry inq = getInquiry(merchantId);
        Address address = new Address();
        address.setAddress1("12345 € … • ™ ¢ £ § ¶ • ª º – ≠");
        address.setCity("Boise");
        address.setCountry("US");
        address.setPostalCode("83704");
        address.setState("ID");

        inq.setBillingAddress(address);

        Response response = utf8Client.process(inq);
        logger.trace(response.toString());
        assertNotEquals("E", response.getAuto());
        assertNotEquals("", response.getAuto());
    }

    @Test
    void testCustomBearerToken() throws RisException, UnsupportedEncodingException, MalformedURLException, ConfigurationException {
        logger.debug("running test for custom bearer token");
        URL serverUrl = new URL(TestConfiguration.getRisURL());

        KountRisClient customClient = new KountRisClient(
                serverUrl,
                TestConfiguration.getRisAPIKey(),
                true,
                TestConfiguration.getPfApiKey(),
                TestConfiguration.getPfClientId(),
                TestConfiguration.getPfApiEndpoint(),
                TestConfiguration.getPfAuthEndpoint()
        );

        BearerAuthResponse bearer;
        HttpPost httpPost = new HttpPost(TestConfiguration.getPfAuthEndpoint());
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader(PF_AUTH_HEADER, "Basic " + TestConfiguration.getPfApiKey());
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
                    bearer = authResponse;
                    bearer.getExpiresAt();

                } else {
                    String message = "Error fetching auth token: received " + httpResponse.getCode() + " " + httpResponse.getReasonPhrase();
                    logger.error(message);
                    throw new RisTransportException("An error occurred while reading the auth token response: " + message);
                }
            }
        } catch (Exception ioe) {
            throw new RisTransportException("An error occurred while getting the auth token", ioe);
        }

        BearerAuthResponse initialToken = customClient.getCustomBearerResponse();

        customClient.setCustomBearerResponse(bearer);

        if (customClient.getCustomBearerResponse() == null) {
            throw new RisTransportException("token should be initialized");
        }

        BearerAuthResponse setToken = customClient.getCustomBearerResponse();

        if (initialToken.getExpiresAt() == setToken.getExpiresAt() || initialToken.getAccessToken().equals(setToken.getAccessToken())) {
            throw new RisTransportException("token should not be the same as the initial value");
        }

        Inquiry inq = getInquiry(merchantId);

        Response response = customClient.process(inq);
        logger.trace(response.toString());
        assertNotEquals("E", response.getAuto());
        assertNotEquals("", response.getAuto());
    }

    private static Inquiry getInquiry(long merchantId) throws UnsupportedEncodingException {
        Inquiry inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), 0);
        inq.setMerchantId(merchantId);
        inq.setEmail("predictive@kount.com");

        return inq;
    }

}
