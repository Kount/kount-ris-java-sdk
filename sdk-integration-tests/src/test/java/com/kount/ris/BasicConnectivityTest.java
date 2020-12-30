package com.kount.ris;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.kount.ris.util.RisException;
import com.kount.ris.util.Utilities;

public class BasicConnectivityTest {

	private static final Logger logger = LogManager.getLogger(BasicConnectivityTest.class);
	
	private static KountRisClient client = null;
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

		if (apiKey == null || merchantId == null) {
			logger.debug("Unable to read config");
			throw  new Exception("Unable to read config : Enter valid credential in config.properties file");
		}
		if ((apiKey.equals("")) ||merchantId.equals("")){
			logger.debug("Enter valid credential in config.properties file");
			throw  new Exception("Invalid Credentials : Enter valid credential in config.properties file");
		}

		KOUNT_API_KEY = apiKey;
		RIS_ENDPOINT = properties.getProperty("Ris.Url");
		MERCHANT_ID = Integer.parseInt(merchantId);

		URL serverUrl = new URL(RIS_ENDPOINT);
		client = new KountRisClient(serverUrl, KOUNT_API_KEY);
	}
	
	@Test
	public void testExpectedScore() throws RisException {
		logger.debug("running get expected score test");
		
		Inquiry inq = getInquiry();
		inq.setParm("UDF[~K!_SCOR]", "42");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("42", response.getScore());
	}
	
	@Test
	public void testExpectedDecision() throws RisException {
		logger.debug("running get expected decision test");
		
		Inquiry inq = getInquiry();
		inq.setParm("UDF[~K!_AUTO]", "R");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("R", response.getAuto());
	
	}
	
	private static Inquiry getInquiry() {
		Inquiry inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), 0);
		inq.setMerchantId(MERCHANT_ID);
		inq.setEmail("predictive@kount.com");
		
		return inq;
	}

}
