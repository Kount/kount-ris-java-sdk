package com.kount.ris.util.payment;

/**
 * A class representing an ELV payment.
 * </p>
 * Sets the PTYP parameter to "ELV".
 * 
 */
public class ElvPayment extends Payment {

	/**
	 * Constructor for an ELV payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public ElvPayment(String id) {
		super("ELV", id);
	}
}
