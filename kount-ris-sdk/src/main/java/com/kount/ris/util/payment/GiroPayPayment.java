package com.kount.ris.util.payment;

/**
 * A class representing a GiroPay payment.
 * </p>
 * Sets the PTYP parameter to "GIROPAY".
 * 
 */
public class GiroPayPayment extends Payment {

	/**
	 * Constructor for a GiroPay payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public GiroPayPayment(String id) {
		super("GIROPAY", id);
	}
}
