package com.kount.ris.util.payment;

/**
 * A class representing an Interac payment.
 * </p>
 * Sets the PTYP parameter to "INTERAC".
 * 
 */
public class InteracPayment extends Payment {

	/**
	 * Constructor for an Interac payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public InteracPayment(String id) {
		super("INTERAC", id);
	}
}
