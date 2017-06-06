package com.kount.ris.util.payment;

/**
 * A class representing an Apple Pay payment.
 * </p>
 * Sets the PTYP parameter to "APAY".
 * 
 */
public class ApplePayPayment extends Payment {

	/**
	 * Constructor for an Apple Pay payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public ApplePayPayment(String id) {
		super("APAY", id);
	}
}
