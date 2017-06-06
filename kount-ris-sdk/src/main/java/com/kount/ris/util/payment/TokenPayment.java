package com.kount.ris.util.payment;

/**
 * A class representing a Token payment.
 * </p>
 * Sets the PTYP parameter to "TOKEN".
 * 
 */
public class TokenPayment extends Payment {

	/**
	 * Constructor for a Token payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public TokenPayment(String id) {
		super("TOKEN", id);
	}
}
