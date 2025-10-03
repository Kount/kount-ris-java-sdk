package com.kount.ris.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Response exception codes.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class RisResponseException extends RisException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 2247083326375371440L;
	/**
	 * Error code.
	 */
	private String code;
	/**
	 * Error messages map.
	 */
	private static final Map<String, String> ERROR_MESSAGES = new HashMap<>();

	static {
		ERROR_MESSAGES.put("201", "Missing version");
		ERROR_MESSAGES.put("202", "Missing mode");
		ERROR_MESSAGES.put("203", "Missing merchant ID");
		ERROR_MESSAGES.put("204", "Missing session ID");
		ERROR_MESSAGES.put("205", "Missing transaction ID");

		ERROR_MESSAGES.put("211", "Missing currency type");
		ERROR_MESSAGES.put("212", "Missing total");

		ERROR_MESSAGES.put("221", "Missing email");
		ERROR_MESSAGES.put("222", "Missing anid");

		ERROR_MESSAGES.put("231", "Missing payment type");
		ERROR_MESSAGES.put("232", "Missing card number");
		ERROR_MESSAGES.put("233", "Missing check micro");
		ERROR_MESSAGES.put("234", "Missing PayPal ID");
		ERROR_MESSAGES.put("235", "Missing Payment Token");

		ERROR_MESSAGES.put("241", "Missing IP address");

		ERROR_MESSAGES.put("251", "Missing merchant acknowledgement");

		ERROR_MESSAGES.put("261", "Missing post body");

		ERROR_MESSAGES.put("301", "Bad version");
		ERROR_MESSAGES.put("302", "Bad mode");
		ERROR_MESSAGES.put("303", "Bad merchant ID");
		ERROR_MESSAGES.put("304", "Bad session ID");
		ERROR_MESSAGES.put("305", "Bad transaction ID");

		ERROR_MESSAGES.put("311", "Bad currency type");
		ERROR_MESSAGES.put("312", "Bad total");

		ERROR_MESSAGES.put("321", "Bad email");
		ERROR_MESSAGES.put("322", "Bad anid");

		ERROR_MESSAGES.put("331", "Bad payment type");
		ERROR_MESSAGES.put("332", "Bad card number");
		ERROR_MESSAGES.put("333", "Bad check micro");
		ERROR_MESSAGES.put("334", "Bad PayPal ID");
		ERROR_MESSAGES.put("335", "Bad Google ID");
		ERROR_MESSAGES.put("336", "Bad Bill Me Later ID");

		ERROR_MESSAGES.put("341", "Bad IP address");

		ERROR_MESSAGES.put("351", "Bad merchant acknowledgement");

		ERROR_MESSAGES.put("399", "Bad option");

		ERROR_MESSAGES.put("401", "Extra data");
		ERROR_MESSAGES.put("402", "Mismatched payment " + "type: you provided payment information in a field that "
				+ "did not match the payment type");
		ERROR_MESSAGES.put("403", "Unnecessary anid");
		ERROR_MESSAGES.put("404", "Unnecessary payment token");

		ERROR_MESSAGES.put("501", "Unauthorized request");
		ERROR_MESSAGES.put("502", "Unauthorized merchant");
		ERROR_MESSAGES.put("503", "Unauthorized IP address");
		ERROR_MESSAGES.put("504", "Unauthorized passphrase");

		ERROR_MESSAGES.put("601", "System error");

		ERROR_MESSAGES.put("701", "The transaction ID specified in the update " + "was not found.");
	}

	/**
	 * Ris response exception.
	 * 
	 * @param c
	 *            Ris exception code
	 */
	public RisResponseException(String c) {
		super(RisResponseException.getErrorMessage(c));
		this.code = c;
	}

	/**
	 * Get error message.
	 * 
	 * @param code
	 *            Error code
	 * @return Description of error code
	 */
	private static String getErrorMessage(String code) {
		if (ERROR_MESSAGES.get(code) != null) {
			return ERROR_MESSAGES.get(code);
		}
		return "Error code : " + code;
	}

	/**
	 * Get the exception code.
	 * 
	 * @return Code
	 */
	public String getCode() {
		return this.code;
	}
}
