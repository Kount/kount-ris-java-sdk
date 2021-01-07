package com.kount.ris.khash;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.kount.ris.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kount.ris.util.RisException;
import com.kount.ris.util.Utilities;
import com.kount.ris.util.payment.CardPayment;

public class MaskEncodingTest {
	private static final Logger logger = LogManager.getLogger(MaskEncodingTest.class);
	
	private static KountRisClient client = null;
	
	private String sessionId = null;
	private Inquiry inq = null;
	private static  String RIS_ENDPOINT;
	private static  int MERCHANT_ID;
	private static String KOUNT_API_KEY;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream inputStream = BasicConnectivityTest.class.getClassLoader().getResourceAsStream("config.properties");
		Properties properties=new Properties();
		properties.load(inputStream);
		String apiKey =  properties.getProperty("Ris.API.Key");
		String merchantId = properties.getProperty("Ris.MerchantId");

		if (apiKey == null || merchantId == null || (apiKey.equals("")) || merchantId.equals("")) {
			logger.debug("Unable to read config");
			throw  new Exception("Unable to read config : Enter valid credential in config.properties file");
		}

		KOUNT_API_KEY = apiKey;
		RIS_ENDPOINT = properties.getProperty("Ris.Url");
		MERCHANT_ID = Integer.parseInt(merchantId);
		URL serverUrl = new URL(RIS_ENDPOINT);
		client = new KountRisClient(serverUrl, KOUNT_API_KEY);
	}
	
	@Before
	public void resetIdAndInquiry() {
		this.sessionId = Utilities.generateUniqueId();
		this.inq = Utilities.defaultInquiry(sessionId, MERCHANT_ID);
	}
	
	@Test
	public void testRisQUsingPaymentEncodingMaskValid() throws RisException {
		logger.debug("running testRisQUsingPaymentEncodingMaskValid");
		
		inq.setPaymentMasked(new CardPayment("370070538959797"));
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("AMEX", response.getBrand());
	}

	@Test
	public void testRisQUsingPaymentEncodingMaskError() throws RisException {
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
}
