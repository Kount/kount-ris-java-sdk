package com.kount.ris.util;

/**
 * Inquiry mode class.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public final class InquiryMode extends Enum {

	/**
	 * Constructor for an inquiry mode object.
	 * 
	 * @param value
	 *            Value
	 */
	private InquiryMode(String value) {
		super(value);
	}

	/**
	 * "Q". Default inquiry mode, internet order type.
	 */
	public static final InquiryMode INITIAL_INQUIRY = new InquiryMode("Q");

	/**
	 * "P". Phone order type.
	 */
	public static final InquiryMode PHONE_ORDER = new InquiryMode("P");

	/**
	 * "W". Kount Central Mode W - Full Inquiry [W]ith thresholds.
	 */
	public static final InquiryMode KC_FULL_INQUIRY_W = new InquiryMode("W");

	/**
	 * "J". Kount Central Mode J - Fast Inquiry [J]ust thresholds.
	 */
	public static final InquiryMode KC_QUICK_INQUIRY_J = new InquiryMode("J");
}
