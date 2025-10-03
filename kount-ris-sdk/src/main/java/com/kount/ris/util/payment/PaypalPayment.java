package com.kount.ris.util.payment;

/**
 * A class representing a Paypal payment.
 * </p>
 * Sets the PTYP parameter to "PYPL".
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class PaypalPayment extends Payment {

	/**
	 * Constructor for a paypal payment that accepts the paypal payment ID.
	 * 
	 * @param id
	 *            The Paypal payment ID
	 */
	public PaypalPayment(String id) {
		super("PYPL", id);
	}

}
