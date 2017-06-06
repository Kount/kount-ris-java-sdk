package com.kount.ris.util.payment;

/**
 * A class representing a Skrill/Moneybookers payment.
 * </p>
 * Sets the PTYP parameter to "SKRILL".
 * 
 */
public class SkrillMoneybookersPayment extends Payment {

	/**
	 * Constructor for a Skrill/Moneybookers payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public SkrillMoneybookersPayment(String id) {
		super("SKRILL", id);
	}
}
