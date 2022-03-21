package com.kount.ris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kount.ris.util.RisException;
import com.kount.ris.util.TestConfiguration;
import com.kount.ris.util.Utilities;

public class BasicConnectivityTest {

	private static final Logger logger = LogManager.getLogger(BasicConnectivityTest.class);
	
	private static KountRisClient client = null;
	private static int merchantId;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		merchantId = Integer.parseInt(TestConfiguration.getMerchantID());
		URL serverUrl = new URL(TestConfiguration.getRisURL());
		client = new KountRisClient(serverUrl, TestConfiguration.getRisAPIKey());
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

	@Test
	public void testPreviouslyWhiteListedExistWithRisCallVersion_0710() throws RisException {
		logger.debug("running get previously white listed test");		;
		Inquiry inq = getInquiry();
		inq.setVersion("0710");
		Response response = client.process(inq);
		logger.trace(response.toString());
		assertTrue( response.getPreviouslyWhiteListed()!= null);
	}

	@Test
	public void test3dSecureMerchantResponseExistWithRisCallVersion_0710() throws RisException {
		logger.debug("running get 3D secure merchant response test");		;
		Inquiry inq = getInquiry();
		inq.setVersion("0710");
		Response response = client.process(inq);
		logger.trace(response.toString());
		assertTrue( response.get3DSecureMerchantResponse() != null);
	}

	@Test
	public void testDefaultRisCallVersion() throws RisException {
		logger.debug("running default ris call version test");		;
		Inquiry inq = getInquiry();
		Response response = client.process(inq);
		logger.trace(response.toString());
		assertEquals("0710", response.getVersion());
	}

	@Test
	public void testRequstWithLbin() throws RisException {
		logger.debug("creating RIS request with LBIN parameter");
		Inquiry inq = getInquiry();
		inq.setLbin("12345123");
		System.out.println(inq.toString());
		Response response = client.process(inq);
		assertEquals(true, inq.getParams().containsKey("LBIN"));
		assertEquals("class com.kount.ris.Response",  response.getClass().toString());
	}

	@Test
	public void testRequestWithoutLbin() throws RisException {
		logger.debug("creating RIS request without LBIN parameter ");		
		Inquiry inq = getInquiry();
		assertEquals(false, inq.getParams().containsKey("LBIN"));
		Response response = client.process(inq);
	}

	private static Inquiry getInquiry() {
		Inquiry inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), 0);
		inq.setMerchantId(merchantId);
		inq.setEmail("predictive@kount.com");
		
		return inq;
	}

}
