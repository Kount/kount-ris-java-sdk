package com.kount.ris.util.payment;

/**
 * A class representing a gift card payment.
 * </p>
 * Sets the PTYP parameter to "GIFT".
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax All Rights Reserved.
 */
public class GiftCardPayment extends Payment {

	/**
	 * Constructor for a gift card payment.
	 * 
	 * @param giftCardNumber
	 *            The gift card number
	 */
	public GiftCardPayment(String giftCardNumber) {
		super("GIFT", giftCardNumber);
	}

}
