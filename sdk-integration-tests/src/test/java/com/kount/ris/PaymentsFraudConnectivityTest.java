package com.kount.ris;

import com.kount.ris.util.Address;
import com.kount.ris.util.RisException;
import com.kount.ris.util.TestConfiguration;
import com.kount.ris.util.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import javax.naming.ConfigurationException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

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

    @Test void testForceCharset() throws UnsupportedEncodingException, RisException {
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

    private static Inquiry getInquiry(long merchantId) throws UnsupportedEncodingException {
        Inquiry inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), 0);
        inq.setMerchantId(merchantId);
        inq.setEmail("predictive@kount.com");

        return inq;
    }

}
