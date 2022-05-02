package com.kount.ris;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kount.ris.util.AuthorizationStatus;
import com.kount.ris.util.BankcardReply;
import com.kount.ris.util.Khash;
import com.kount.ris.util.MerchantAcknowledgment;
import com.kount.ris.util.RisException;
import com.kount.ris.util.payment.BillMeLaterPayment;
import com.kount.ris.util.payment.CardPayment;
import com.kount.ris.util.payment.CheckPayment;
import com.kount.ris.util.payment.GiftCardPayment;
import com.kount.ris.util.payment.GooglePayment;
import com.kount.ris.util.payment.GreenDotMoneyPakPayment;
import com.kount.ris.util.payment.NoPayment;
import com.kount.ris.util.payment.Payment;
import com.kount.ris.util.payment.PaypalPayment;

/**
 * RIS Request superclass for Inquiry and Update.
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public abstract class Request {

	/**
	 * Map containing data that will be sent to RIS.
	 */
	protected Map<String, String> params = new HashMap<>();

	/**
	 * Logger.
	 */
	protected Logger logger = LogManager.getLogger(Request.class);

	/**
	 * Optional parameter for clients to close the transport when done.
	 */
	protected boolean closeOnFinish;

	/**
	 * Config object holds SDK configuration information
	 */
	public static final Config config = new Config();

	/**
	 * Class constructor.
	 * @throws RisException 
	 */
	public Request() {
		setVersion(config.VERS);
		setKhashPaymentEncoding(true);
		params.put("SDK", "JAVA");
	}

	/**
	 * To String.
	 * 
	 * @return String
	 */
	public String toString() {
		String s = getParams().toString();
		return s.replaceAll(",", "\n");
	}

	/**
	 * Set a parm for the request.
	 *
	 * @param key
	 *            The key for the parm
	 * @param value
	 *            The value for the parm
	 * @return this
	 */
	public Request setParm(String key, String value) {
		params.put(key, value);
		return this;
	}

	/**
	 * Set the version number.
	 * 
	 * @param version
	 *            The SDK version
	 * @return this
	 */
	public Request setVersion(String version) {
		params.put("VERS", version);
		return this;
	}

	/**
	 * Set the session id. Must be unique over a 30-day span
	 * 
	 * @param sessionId
	 *            Id of the current session
	 * @return this
	 */
	public Request setSessionId(String sessionId) {
		params.put("SESS", sessionId);
		return this;
	}

	/**
	 * Set the merchant ID assigned by Kount.
	 * 
	 * @param merchantId
	 *            Merchant ID
	 * @return this
	 */
	public Request setMerchantId(int merchantId) {
		params.put("MERC", Integer.valueOf(merchantId).toString());
		return this;
	}

	/**
	 * Set the Kount Central Customer ID.
	 * 
	 * @param id
	 *            KC Customer ID
	 * @return this
	 */
	public Request setKcCustomerId(String id) {
		params.put("CUSTOMER_ID", id);
		return this;
	}

	/**
	 * Set the order number.
	 * 
	 * @param orderNumber
	 *            Merchant unique order number
	 * @return this
	 */
	public Request setOrderNumber(String orderNumber) {
		params.put("ORDR", orderNumber);
		return this;
	}

	/**
	 * Set the merchant acknowledgment.
	 * </p>
	 * 	Merchants acknowledgement to ship/process the order. The MACK field must be set as 
	 * 'Y' if persona data is to be collected to strengthen the score.
	 * 
	 * @param merchantAcknowledgment
	 *            Merchant acknowledgment
	 * @return this
	 */
	public Request setMerchantAcknowledgment(MerchantAcknowledgment merchantAcknowledgment) {
		params.put("MACK", merchantAcknowledgment.toString());
		return this;
	}

	/**
	 * Set the Authorization Status.
	 * </p>
	 * Authorization Status returned to merchant from processor. Acceptable values for the 
	 * AUTH field are ’A’ for Authorized or ’D’ for Decline. In orders where AUTH=A will 
	 * aggregate towards order velocity of the persona while orders where AUTH=D will 
	 * decrement the velocity of the persona.
	 * 
	 * @param authorizationStatus
	 *            Auth status by issuer
	 * @return this
	 */
	public Request setAuthorizationStatus(AuthorizationStatus authorizationStatus) {
		params.put("AUTH", authorizationStatus.toString());
		return this;
	}

	/**
	 * Set the Bankcard AVS zip code reply.
	 * </p>
	 * Address Verification System Zip Code verification response returned to merchant from 
	 * processor. Acceptable values are ‘M’ for match, ’N’ for no match, or ‘X’ for unsupported 
	 * or unavailable.
	 * 
	 * @param avsZipReply
	 *            Bankcard AVS zip code reply
	 * @return this
	 */
	public Request setAvsZipReply(BankcardReply avsZipReply) {
		params.put("AVSZ", avsZipReply.toString());
		return this;
	}

	/**
	 * Set the Bankcard AVS street addres reply.
	 * </p>
	 * Address Verification System Street verification response returned to merchant from 
	 * processor. Acceptable values are ’M’ for match, ’N’ for no-match, or ’X’ for 
	 * unsupported or unavailable.
	 * 	
	 * @param avsAddressReply
	 *            Bankcard AVS street addres reply
	 * @return this
	 */
	public Request setAvsAddressReply(BankcardReply avsAddressReply) {
		params.put("AVST", avsAddressReply.toString());
		return this;
	}

	/**
	 * Set the Bankcard CVV/CVC/CVV2 reply.
	 * </p>
	 * Card Verification Value response returned to merchant from processor. Acceptable 
	 * values are ’M’ for match, ’N’ for no-match, or ’X’ unsupported or unavailable.
	 * 
	 * @param cvvReply
	 *            Bankcard CVV/CVC/CVV2 reply
	 * @return this
	 */
	public Request setCvvReply(BankcardReply cvvReply) {
		params.put("CVVR", cvvReply.toString());
		return this;
	}

	/**
	 * Set a payment.
	 * </p>
	 * Depending on the payment type, various request parameters are set: PTOK, PTYP, LAST4.
	 * </p>
	 * If payment token hashing is not possible, the PENC parameter is set to empty string.
	 * 
	 * @param p
	 *            Payment
	 * @return this
	 */
	public Request setPayment(Payment p) {
		String type = p.getPaymentType();
		String token = p.getPaymentToken();

		if (isSetKhashPaymentEncoding() && !(p instanceof NoPayment) && !p.isKhashed()) {
			if (p.getPaymentTokenLast4().equals("")) {
				p.calculateLast4();
			}
			try {
				if (p instanceof GiftCardPayment) {
					int merchantId = Integer.parseInt((String) params.get("MERC"));
					token = Khash.getInstance().hashGiftCard(merchantId, token);
				} else {
					token = Khash.getInstance().hashPaymentToken(token);
				}
				p.setKhashed(true);
			} catch (NoSuchAlgorithmException nsae) {
				logger.error("Unable to create payment token hash. Caught "
						+ "exception java.security.NoSuchAlgorithmException." + " KHASH payment encoding disabled");
				// Default to plain text payment tokens
				params.put("PENC", "");
			} catch (NumberFormatException nfe) {
				logger.error("Error converting Merchant ID to integer" + " value. Set a valid Merchant ID.", nfe);
				throw nfe;
			}
		}

		params.put("PTOK", token);
		params.put("PTYP", type);
		params.put("LAST4", p.getPaymentTokenLast4());

		return this;
	}
	
	/**
	 * Sets a card payment and masks the card number in the following way:
	 * </p>
	 * First 6 characters remain as they are, following characters up to the last 4 are 
	 * replaced with the 'X' character, last 4 characters remain as they are.
	 * </p>
	 * If the provided Payment parameter is not a card payment, standard encoding 
	 * will be applied.
	 * </p>
	 * This method sets the following RIS Request fields: PTOK, PTYP, LAST4, PENC.
	 * 
	 * @param p
	 * 			Payment -- card payment
	 * @return
	 * 			this
	 */
	public Request setPaymentMasked(Payment p) {
		String type = p.getPaymentType();
		String token = p.getPaymentToken();
		
		if (p instanceof CardPayment && !p.isKhashed()) {
			if (p.getPaymentTokenLast4().isEmpty()) {
				p.calculateLast4();
			}
		
			token = maskToken(token);
			
			params.put("PTOK", token);
			params.put("PTYP", type);
			params.put("LAST4", p.getPaymentTokenLast4());
			params.put("PENC", "MASK");
			
			return this;
		} else {
			logger.warn("setPaymentMasked: provided payment is not a CardPayment, applying khash instead of masking");
			return setPayment(p);
		} 
	}

	/**
	 * Encodes the provided payment token according to the MASK encoding scheme
	 * 
	 * @param token		the Payment token for this request
	 * @return
	 * 			MASK-encoded token
	 */
	private static String maskToken(String token) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(token.substring(0, 6));
		for (int i = 6; i < (token.length() - 4); i++) {
			builder.append('X');
		}
		
		builder.append(token.substring(token.length() - 4));
		
		return builder.toString();
	}

	/**
	 * Set a payment by payment type and payment token.
	 * </p>
	 * The payment type parameter provided is checked if it's one of the predefined payment types
	 * and Payment is created appropriately
	 * 
	 * @param ptyp
	 *            See SDK documentation for a list of accepted payment types
	 * @param ptok
	 *            The payment token
	 * @return this
	 */
	public Request setPayment(String ptyp, String ptok) {
		if ("BLML".equals(ptyp)) {
			setPayment(new BillMeLaterPayment(ptok));
		} else if ("CARD".equals(ptyp)) {
			setPayment(new CardPayment(ptok));
		} else if ("CHECK".equals(ptok)) {
			setPayment(new CheckPayment(ptok));
		} else if ("GIFT".equals(ptyp)) {
			setPayment(new GiftCardPayment(ptok));
		} else if ("GOOG".equals(ptyp)) {
			setPayment(new GooglePayment(ptok));
		} else if ("GDMP".equals(ptyp)) {
			setPayment(new GreenDotMoneyPakPayment(ptok));
		} else if ("NONE".equals(ptyp)) {
			setPayment(new NoPayment());
		} else if ("PYPL".equals(ptyp)) {
			setPayment(new PaypalPayment(ptok));
		} else {
			setPayment(new Payment(ptyp, ptok));
		}

		return this;
	}

	/**
	 * Fetch the map of data parameters to send to RIS.
	 * 
	 * @return Map of params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * Set KHASH payment encoding.
	 * 
	 * @return this
	 * @deprecated Version 5.0.0. Use method
	 *             com.kount.ris.Request.setKhashPaymentEncoding(boolean) :
	 *             Request
	 */
	public Request setKhashPaymentEncoding() {
		logger.info("The method " + "com.kount.ris.Request.setKhashPaymentEncoding() : Request is "
				+ "deprecated. Use " + "com.kount.ris.Request.setKhashPaymentEncoding(boolean) : " + "Request instead.");
		params.put("PENC", "KHASH");
		return this;
	}

	/**
	 * Set KHASH payment encoding.
	 * 
	 * @param enabled
	 *            TRUE when enabled.
	 * @return this
	 */
	public Request setKhashPaymentEncoding(boolean enabled) {
		if (enabled) {
			params.put("PENC", "KHASH");
		} else {
			params.put("PENC", "");
		}
		return this;
	}

	/**
	 * Check if KHASH payment encoding has been set.
	 * 
	 * @return boolean TRUE when set.
	 */
	protected boolean isSetKhashPaymentEncoding() {
		return params.containsKey("PENC") && "KHASH".equals(params.get("PENC"));
	}

	/**
	 * Set a flag for the request transport.
	 * 
	 * @param cof
	 *            Sets the closeOnFinish flag
	 * @return this
	 */
	public Request setCloseOnFinish(boolean cof) {
		closeOnFinish = cof;
		return this;
	}
	/**
   * Set the Long Bank Identification Number.
   * 
   * @param  lbin  
   * 			Long Bank Identification Number
   * @return this
   */
	public Request setLbin(String lbin) {
		params.put("LBIN", lbin);
		return this;
	}

}
