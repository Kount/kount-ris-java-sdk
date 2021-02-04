package com.kount.ris;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.kount.ris.util.RisException;
import com.kount.ris.util.Utilities;

public class BasicConnectivityTest {

	private static final Logger logger = LogManager.getLogger(BasicConnectivityTest.class);
	
	private static KountRisClient client = null;
	
	private static final int MERCHANT_ID = 999667;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		URL keyLocation = BasicConnectivityTest.class.getClassLoader().getResource("999667.apikey");
		File keyFile = new File(keyLocation.getFile());

		logger.debug("API key file resolved to: " + keyFile.getAbsolutePath());
		
		URL serverUrl = new URL(TestConstants.RIS_ENDPOINT);
		client = new KountRisClient(serverUrl, keyFile);
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

	@Test
	public void testTotalAndCashWithLongType() throws RisException {
		logger.debug("running get expected score test");
		long total = 11111111111111L;
		long cash = 111111111111111L;
		Inquiry inq = getInquiry();
		inq.setTotal(total);
		inq.setCash(cash);
		Response response = client.process(inq);
		logger.trace(response.toString());

		assertEquals(0, response.getErrorCount());
	}
	
	private static Inquiry getInquiry() {
		Inquiry inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), 0);
		inq.setMerchantId(MERCHANT_ID);
		inq.setEmail("predictive@kount.com");
		
		return inq;
	}

}
