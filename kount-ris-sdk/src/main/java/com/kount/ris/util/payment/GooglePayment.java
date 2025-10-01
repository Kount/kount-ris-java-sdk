package com.kount.ris.util.payment;

/**
 * A google payment.
 * </p>
 * Sets the PTYP parameter to "GOOG".
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class GooglePayment extends Payment {

	/**
	 * Constructor for a google payment object.
	 * 
	 * @param id
	 *            Google payment ID
	 */
	public GooglePayment(String id) {
		super("GOOG", id);
	}

}
