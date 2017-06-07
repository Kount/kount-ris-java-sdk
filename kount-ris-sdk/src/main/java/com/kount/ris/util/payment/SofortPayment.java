package com.kount.ris.util.payment;

/**
 * A class representing a Sofort payment.
 * </p>
 * Sets the PTYP parameter to "SOFORT".
 * 
 */
public class SofortPayment extends Payment {

	/**
	 * Constructor for a Sofort payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public SofortPayment(String id) {
		super("SOFORT", id);
	}
}
