package com.kount.ris.util.payment;

/**
 * A class representing a credit card payment.
 * </p>
 * Sets the PTYP parameter to "CARD".
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class CardPayment extends Payment {

	/**
	 * Constructor for a credit card payment.
	 * 
	 * @param number
	 *            The card number
	 */
	public CardPayment(String number) {
		super("CARD", number);
	}

}
