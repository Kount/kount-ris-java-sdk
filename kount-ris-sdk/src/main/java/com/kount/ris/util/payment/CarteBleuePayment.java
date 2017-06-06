package com.kount.ris.util.payment;

/**
 * A class representing an Carte Bleue payment.
 * </p>
 * Sets the PTYP parameter to "CARTE_BLEUE".
 * 
 */
public class CarteBleuePayment extends Payment {

	/**
	 * Constructor for an Carte Bleue payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public CarteBleuePayment(String id) {
		super("CARTE_BLEUE", id);
	}
}
