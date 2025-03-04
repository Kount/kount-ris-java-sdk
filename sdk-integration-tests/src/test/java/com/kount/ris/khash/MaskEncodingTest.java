package com.kount.ris.khash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kount.ris.Inquiry;
import com.kount.ris.KountRisClient;
import com.kount.ris.Response;
import com.kount.ris.util.RisException;
import com.kount.ris.util.TestConfiguration;
import com.kount.ris.util.Utilities;
import com.kount.ris.util.payment.CardPayment;

import javax.naming.ConfigurationException;

public class MaskEncodingTest {
	private static final Logger logger = LogManager.getLogger(MaskEncodingTest.class);
	
	private KountRisClient client = null;
	private long merchantId;

    private Inquiry inq = null;

	public MaskEncodingTest() throws MalformedURLException, ConfigurationException {
		merchantId = Long.parseLong(TestConfiguration.getMerchantID());
		URL serverUrl = new URL(TestConfiguration.getRisURL());
		client = new KountRisClient(serverUrl, TestConfiguration.getRisAPIKey());
	}

	@Test
	public void testRisQUsingPaymentEncodingMaskValid() throws RisException {
		if(isNotPointingToCommand()){
			return;
		}
		this.inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), merchantId);
		logger.debug("running testRisQUsingPaymentEncodingMaskValid");
		
		inq.setPaymentMasked(new CardPayment("370070538959797"));
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("AMEX", response.getBrand());
	}

	@Test
	public void testRisQUsingPaymentEncodingMaskError() throws RisException {
		if(isNotPointingToCommand()){
			return;
		}
		this.inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), merchantId);
		logger.debug("running RisQUsingPaymentEncodingMaskError");
		
		inq.setPaymentMasked(new CardPayment("370070538959797"));
		
		// overwrite the PTOK value set by previous method to induce error in RIS
		inq.setParm("PTOK", "370070538959797");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("E", response.getMode());
		assertEquals(0, response.getWarningCount());
		assertEquals(1, response.getErrorCount());
		
		assertEquals("340", response.getErrorCode());
		assertEquals("340 BAD_MASK Cause: [value [370070538959797] did not match regex "
				+ "/^\\d{6}X{5,9}\\d{1,4}$/], Field: [PTOK], Value: [370070538959797]", 
			response.getErrors().get(0));
	}

	private boolean isNotPointingToCommand() {
		String migrationMode = System.getProperty("migration.mode.enabled");
        return Boolean.parseBoolean(migrationMode);
	}
}
