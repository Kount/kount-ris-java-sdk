package com.kount.ris.util.payment;

/**
 * A class representing a POLi payment.
 * </p>
 * Sets the PTYP parameter to "POLI".
 * 
 */
public class PoliPayment extends Payment {

	/**
	 * Constructor for a POLi payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public PoliPayment(String id) {
		super("POLI", id);
	}
}
