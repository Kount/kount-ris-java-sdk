package com.kount.ris.util.payment;

/**
 * A class representing a Single Euro Payments Area payment.
 * </p>
 * Sets the PTYP parameter to "SEPA".
 * 
 */
public class SingleEuroPaymentsAreaPayment extends Payment {

	/**
	 * Constructor for a SEPA payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public SingleEuroPaymentsAreaPayment(String id) {
		super("SEPA", id);
	}
}
