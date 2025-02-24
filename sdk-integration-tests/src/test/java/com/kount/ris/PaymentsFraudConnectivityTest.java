package com.kount.ris;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.kount.ris.util.RisException;
import com.kount.ris.util.TestConfiguration;
import com.kount.ris.util.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;


import javax.naming.ConfigurationException;
import java.net.MalformedURLException;
import java.net.URL;

public class PaymentsFraudConnectivityTest {
    private static final Logger logger = LogManager.getLogger(PaymentsFraudConnectivityTest.class);

    private KountRisClient client = null;
    private long merchantId;

    public PaymentsFraudConnectivityTest() throws MalformedURLException, ConfigurationException {
        merchantId = Long.parseLong(TestConfiguration.getMerchantID());
        URL serverUrl = new URL(TestConfiguration.getRisURL());
        client = new KountRisClient(serverUrl, TestConfiguration.getRisAPIKey());
    }

    @Test
    public void testHappyPath() throws RisException {
        if(!isPointingToPaymentsFraud()){
            return;
        }
        logger.debug("running get expected score test");

        Inquiry inq = getInquiry(merchantId);

        Response response = client.process(inq);
        logger.trace(response.toString());
        assertNotEquals("E", response.getAuto());
        assertNotEquals("", response.getAuto());
    }

    private static Inquiry getInquiry(long merchantId) {
        Inquiry inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), 0);
        inq.setMerchantId(merchantId);
        inq.setEmail("predictive@kount.com");

        return inq;
    }

    private static boolean isPointingToPaymentsFraud() {
        String migrationMode = System.getProperty("migration.mode.enabled");
        return Boolean.parseBoolean(migrationMode);
    }

}
