package com.kount.ris;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.either;

import java.io.InputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kount.ris.util.AuthorizationStatus;
import com.kount.ris.util.BankcardReply;
import com.kount.ris.util.CartItem;
import com.kount.ris.util.InquiryMode;
import com.kount.ris.util.KcEvent;
import com.kount.ris.util.Khash;
import com.kount.ris.util.MerchantAcknowledgment;
import com.kount.ris.util.RisException;
import com.kount.ris.util.UpdateMode;
import com.kount.ris.util.Utilities;
import static org.junit.matchers.JUnitMatchers.*;


public class TestRisTestSuite {

	private static final Logger logger = LogManager.getLogger(TestRisTestSuite.class);
	
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
	
	@Before
	public void resetIdAndInquiry() {
		this.sessionId = Utilities.generateUniqueId();
		this.inq = Utilities.defaultInquiry(sessionId, MERCHANT_ID);
	}

	@Test
	public void testRisQOneItemRequiredFieldsOneRuleReview_1() throws RisException {
		logger.debug("running testRisQOneItemRequiredFieldsOneRuleReview_1");
		
		Response response = client.process(inq);
		logger.trace(response.toString());

		assertEquals("R", response.getAuto());
		assertEquals(0, response.getWarningCount());
		assertEquals(1, response.getRulesTriggered().size());
		assertEquals(sessionId, response.getSessionId());
		assertEquals(sessionId.substring(0, 10), response.getOrderNumber());
	}
	
	@Test
	public void testRisQMultiCartItemsTwoOptionalFieldsTwoRulesDecline_2() throws RisException {
		logger.debug("running testRisQMultiCartItemsTwoOptionalFieldsTwoRulesDecline_2");
		
		inq.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36");
		inq.setTotal(123456789);
		inq.setCart(Arrays.asList(
				new CartItem("cart item type 0", "cart item 0", "cart item 0 description", 10, 1000),
				new CartItem("cart item type 1", "cart item 1", "cart item 1 description", 11, 1001),
				new CartItem("cart item type 2", "cart item 2", "cart item 2 description", 12, 1002)
			));
		
		Response response = client.process(inq);
		logger.trace(response.toString());

		assertEquals("D", response.getAuto());
		assertEquals(0, response.getWarningCount());
		assertEquals(2, response.getRulesTriggered().size());
	}
	
	@Test
	public void testRisQWithUserDefinedFields_3() throws RisException {
		logger.debug("running testRisQWitUserDefinedFields_3");
		
		inq.setParm("UDF[ARBITRARY_ALPHANUM_UDF]", "alphanumeric trigger value");
		inq.setParm("UDF[ARBITRARY_NUMERIC_UDF]", "777");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		boolean alphaNumericTriggered = false;
		boolean numericTriggered = false;

			for (int i = 0; i < response.getRulesTriggered().size(); i++) {
				String rdx = response.getParm("RULE_DESCRIPTION_" + i);
				if (rdx.contains("ARBITRARY_ALPHANUM_UDF")) {
					alphaNumericTriggered = true;
					logger.debug("[alpha-numeric rule] triggered");
				} else if (rdx.contains("ARBITRARY_NUMERIC_UDF")) {
					numericTriggered = true;
					logger.debug("[numeric rule] triggered");
				}
		}
		assertTrue("One or both rules were not found in the response", (alphaNumericTriggered && numericTriggered));
	}
	
	@Test
	public void testRisQHardErrorExpected_4() throws RisException {
		logger.debug("running testRisQHardErrorExpected_4");
		
		// overwrite the PTOK value to induce an error in the RIS
		inq.setParm("PTOK", "BADPTOK");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("E", response.getMode());
		assertEquals("332", response.getErrorCode());
		assertEquals(1, response.getErrorCount());
		assertEquals("332 BAD_CARD Cause: [PTOK invalid format], Field: [PTOK], Value: [hidden]", response.getErrors().get(0));
	}
	
	@Test
	public void testRisQWarningApproved_5() throws RisException {
		logger.debug("running testRisQWarningApproved_5");
		
		inq.setParm("TOTL", "1000");
		inq.setParm("UDF[UDF_DOESNOTEXIST]", "throw a warning please!");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("A", response.getAuto());
		assertEquals(2, response.getWarningCount());
		
		boolean throwAWarningPlease = false;
		boolean notDefinedForMerchant = false;
		
		for (String warning : response.getWarnings()) {
			if (warning.equals("399 BAD_OPTN Field: [UDF], Value: [UDF_DOESNOTEXIST=>throw a warning please!]")) {
				throwAWarningPlease = true;
				logger.debug("[throw a warning please] found");
			} else if (warning.equals("399 BAD_OPTN Field: [UDF], Value: [The label [UDF_DOESNOTEXIST] is not defined for merchant ID [999666].]")) {
				notDefinedForMerchant = true;
				logger.debug("[not defined for merchant] found");
			}
		}
		
		assertTrue("One or both warnings were not found in response", (throwAWarningPlease && notDefinedForMerchant));
	}
	
	@Test
	public void testRisQHardSoftErrorsExpected_6() throws RisException {
		logger.debug("running testRisQHardSoftErrorsExpected_6");
		
		inq.setParm("PTOK", "BADPTOK");
		inq.setParm("UDF[UDF_DOESNOTEXIST]", "throw a warning please!");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("E", response.getMode());
		assertEquals("332", response.getErrorCode());
		assertEquals(1, response.getErrorCount());
		assertEquals("332 BAD_CARD Cause: [PTOK invalid format], Field: [PTOK], Value: [hidden]", response.getErrors().get(0));
		
		assertEquals(2, response.getWarningCount());
		
		boolean throwAWarningPlease = false;
		boolean notDefinedForMerchant = false;
		
		for (String warning : response.getWarnings()) {
			if (warning.equals("399 BAD_OPTN Field: [UDF], Value: [UDF_DOESNOTEXIST=>throw a warning please!]")) {
				throwAWarningPlease = true;
				logger.debug("[throw a warning please] found");
			} else if (warning.equals("399 BAD_OPTN Field: [UDF], Value: [The label [UDF_DOESNOTEXIST] is not defined for merchant ID [999666].]")) {
				notDefinedForMerchant = true;
				logger.debug("[not defined for merchant] found");
			}
		}
		
		assertTrue("One or both warnings were not found in response", (throwAWarningPlease && notDefinedForMerchant));
	}
	
	@Test
		public void testRisWTwoKCRulesReview_7() throws RisException {
		logger.debug("running testRisWTwoKCRulesReview_7");
		
		inq.setMode(InquiryMode.KC_FULL_INQUIRY_W);
		inq.setTotal(10001);
		inq.setKcCustomerId("KCentralCustomerOne");
		
		Response response = client.process(inq);
		logger.trace(response.toString());

		assertThat(response.getKcDecision(), either(containsString("R")).or(containsString("D")) );
		assertEquals(0, response.getWarningCount());
		assertEquals(0, response.getKcWarningCount());
		assertEquals(2, response.getKcEventCount());
		
		boolean billingToShipping = false;
		boolean orderTotal = false;
		
		for (KcEvent event : response.getKcEvents()) {
			if (event.getCode().equals("billingToShippingAddressReview") && (event.getDecision().equals("R") || event.getDecision().equals("D"))) {
				billingToShipping = true;
				logger.debug("[billing to shipping event] found");
			} else if (event.getCode().equals("orderTotalReview") && (event.getDecision().equals("R") || event.getDecision().equals("D"))) {
				orderTotal = true;
				logger.debug("[order total event] found");
			}
		}
		
		assertTrue("One or both events were not found in the response", (billingToShipping && orderTotal));
	}
	
//	@Test
//	public void testRisJOneKountCentralRuleDecline_8() throws RisException {
//		logger.debug("running testRisJOneKountCentralRuelDecline_8");
//		inq.setMode(InquiryMode.KC_QUICK_INQUIRY_J);
//		inq.setTotal(1000);
//		inq.setKcCustomerId("KCentralCustomerDeclineMe");
//
//		Response response = client.process(inq);
//		logger.trace(response.toString());
//
//		assertThat(response.getKcDecision(),either(containsString("R")).or(containsString("D")));
//		assertEquals("D", response.getKcDecision());
//		assertEquals(0, response.getKcWarningCount());
//		assertEquals(1, response.getKcEventCount());
//
//		assertEquals("D", response.getKcEvents().get(0).getDecision());
//		assertEquals("orderTotalDecline", response.getKcEvents().get(0).getCode());
//	}
	
	@Test
	public void testModeUAfterModeQ_9() throws RisException, NoSuchAlgorithmException {
		logger.debug("running testModeUAfterModeQ_9");
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		String transactionId = response.getTransactionId();
		String sessionId = response.getSessionId();
		String orderId = response.getOrderNumber();
		
		Update update = new Update();
		update.setMode(UpdateMode.NO_RESPONSE);
		update.setVersion("0700");
		update.setTransactionId(transactionId);
		update.setMerchantId(MERCHANT_ID);
		update.setSessionId(sessionId);
		update.setOrderNumber(orderId);
		// PTOK has to be khashed manually because of its explicit setting
		update.setParm("PTOK", Khash.getInstance().hashPaymentToken("5386460135176807"));
		update.setParm("LAST4", "6807");
		update.setMerchantAcknowledgment(MerchantAcknowledgment.YES);
		update.setAuthorizationStatus(AuthorizationStatus.APPROVED);
		update.setAvsZipReply(BankcardReply.MATCH);
		update.setAvsAddressReply(BankcardReply.MATCH);
		update.setCvvReply(BankcardReply.MATCH);
		
		Response updateResponse = client.process(update);
		logger.trace(updateResponse.toString());

		assertEquals("U", updateResponse.getMode());
		assertEquals(transactionId, updateResponse.getTransactionId());
		assertEquals(sessionId, updateResponse.getSessionId());
		
		assertNull(updateResponse.getAuto());
		assertNull(updateResponse.getScore());
		assertNull(updateResponse.getGeox());
	}
	
	@Test
	public void testModeXAfterModeQ_10() throws RisException, NoSuchAlgorithmException {
		logger.debug("running testModeXAfterModeQ_10");

		Response response = client.process(inq);
		logger.trace(response.toString());

		String transactionId = response.getTransactionId();
		String sessionId = response.getSessionId();
		String orderId = response.getOrderNumber();

		Update update = new Update();
		update.setMode(UpdateMode.WITH_RESPONSE);
		update.setVersion("0700");
		update.setMerchantId(MERCHANT_ID);
		update.setTransactionId(transactionId);
		update.setSessionId(sessionId);
		update.setOrderNumber(orderId);
		// PTOK has to be khashed manually because of its explicit setting
		update.setParm("PTOK", Khash.getInstance().hashPaymentToken("5386460135176807"));
		update.setParm("LAST4", "6807");
		update.setMerchantAcknowledgment(MerchantAcknowledgment.YES);
		update.setAuthorizationStatus(AuthorizationStatus.APPROVED);
		update.setAvsZipReply(BankcardReply.MATCH);
		update.setAvsAddressReply(BankcardReply.MATCH);
		update.setCvvReply(BankcardReply.MATCH);

		Response updateResponse = client.process(update);
		logger.trace(updateResponse.toString());

		assertEquals("X", updateResponse.getMode());
		assertEquals(transactionId, updateResponse.getTransactionId());
		assertEquals(sessionId, updateResponse.getSessionId());
		assertEquals(orderId, updateResponse.getOrderNumber());

		assertNotNull(updateResponse.getAuto());
		assertNotNull(updateResponse.getScore());
		assertNotNull(updateResponse.getGeox());
	}
	
	@Test
	public void testModeP_11() throws RisException {
		logger.debug("running testModeP_11");;
		
		inq.setMode(InquiryMode.PHONE_ORDER);
		inq.setAnid("2085551212");
		inq.setTotal(1000);
		
		Response response = client.process(inq);
		logger.trace(response.toString());
		
		assertEquals("P", response.getMode());
		assertEquals("A", response.getAuto());
	}
}
