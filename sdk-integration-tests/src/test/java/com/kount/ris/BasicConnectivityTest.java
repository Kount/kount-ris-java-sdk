package com.kount.ris;

import com.kount.ris.util.RisException;
import com.kount.ris.util.TestConfiguration;
import com.kount.ris.util.Utilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class BasicConnectivityTest {

	private static final Logger logger = LogManager.getLogger(BasicConnectivityTest.class);
	private KountRisClient client;
	private long merchantId;

	public BasicConnectivityTest() throws Exception {
		logger.debug("running before all setup");
		merchantId = Long.parseLong(TestConfiguration.getMerchantID());
		URL serverUrl = new URL(TestConfiguration.getRisURL());
		client = new KountRisClient(serverUrl, TestConfiguration.getRisAPIKey());
	}

	@Test
	public void testExpectedScore() throws RisException, UnsupportedEncodingException {
		logger.debug("running get expected score test");
		
		Inquiry inq = getInquiry(merchantId);
		inq.setParm("UDF[~K!_SCOR]", "42");
		Response response = client.process(inq);
		logger.trace(response.toString());
		assertEquals("42", response.getScore());
	}

	@Test
	public void testExpectedDecision() throws RisException, UnsupportedEncodingException {
		logger.debug("running get expected decision test");
		
		Inquiry inq = getInquiry(merchantId);
		inq.setParm("UDF[~K!_AUTO]", "R");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("R", response.getAuto());
	
	}

	@Test
	public void testTotalAndCashWithLongType() throws RisException, UnsupportedEncodingException {
		logger.debug("running total and cash with long type test");
		long total = 11111111111111L;
		long cash = 111111111111111L;
		Inquiry inq = getInquiry(merchantId);
		inq.setTotal(total);
		inq.setCash(cash);
		Response response = client.process(inq);
		logger.trace(response.toString());

		assertEquals(0, response.getErrorCount());
	}

	@Test
	public void testPreviouslyWhiteListedExistWithRisCallVersion_0710() throws RisException, UnsupportedEncodingException {
		logger.debug("running get previously white listed test");
		Inquiry inq = getInquiry(merchantId);
		inq.setVersion("0710");
		Response response = client.process(inq);
		logger.trace(response.toString());
        assertNotNull(response.getPreviouslyWhiteListed());
	}

	@Test
	public void test3dSecureMerchantResponseExistWithRisCallVersion_0710() throws RisException, UnsupportedEncodingException {
		logger.debug("running get 3D secure merchant response test");
		Inquiry inq = getInquiry(merchantId);
		inq.setVersion("0710");
		Response response = client.process(inq);
		logger.trace(response.toString());
        assertNotNull(response.get3DSecureMerchantResponse());
	}

	@Test
	public void testDefaultRisCallVersion() throws RisException, UnsupportedEncodingException {
		logger.debug("running default ris call version test");
		Inquiry inq = getInquiry(merchantId);
		Response response = client.process(inq);
		logger.trace(response.toString());
		Config config = new Config();
		assertEquals(config.VERS, response.getVersion());
	}

	@Test
	public void testRequstWithLbin() throws RisException, UnsupportedEncodingException {
		logger.debug("creating RIS request with LBIN parameter");
		Inquiry inq = getInquiry(merchantId);
		inq.setLbin("12345123");
		Response response = client.process(inq);
        assertTrue(inq.getParams().containsKey("LBIN"));
		assertEquals(0, response.getErrorCount());
	}

	@Test
	public void testRequestWithoutLbin() throws RisException, UnsupportedEncodingException {
		logger.debug("creating RIS request without LBIN parameter ");		
		Inquiry inq = getInquiry(merchantId);
        assertFalse(inq.getParams().containsKey("LBIN"));
		Response response = client.process(inq);
		assertEquals(0, response.getErrorCount());
	}

	private static Inquiry getInquiry(long merchantId) throws UnsupportedEncodingException {
		Inquiry inq = Utilities.defaultInquiry(Utilities.generateUniqueId(), 0);
		inq.setMerchantId(merchantId);
		inq.setEmail("predictive@kount.com");
		
		return inq;
	}
}
