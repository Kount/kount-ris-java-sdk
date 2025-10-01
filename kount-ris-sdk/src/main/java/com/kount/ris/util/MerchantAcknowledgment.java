package com.kount.ris.util;

/**
 * Merchant acknowledgment types.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public final class MerchantAcknowledgment extends Enum {

	/**
	 * Constructor for a merchant acknowledgment object.
	 * 
	 * @param value
	 *            Value
	 */
	private MerchantAcknowledgment(String value) {
		super(value);
	}

	/**
	 * "Y". The product expects to ship.
	 */
	public static final MerchantAcknowledgment YES = new MerchantAcknowledgment("Y");

	/**
	 * "N". The product does not expect to ship.
	 */
	public static final MerchantAcknowledgment NO = new MerchantAcknowledgment("N");
}
