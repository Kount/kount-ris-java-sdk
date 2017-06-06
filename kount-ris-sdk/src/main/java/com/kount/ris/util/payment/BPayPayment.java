package com.kount.ris.util.payment;

/**
 * A class representing a BPay payment.
 * </p>
 * Sets the PTYP parameter to "BPAY".
 * 
 */
public class BPayPayment extends Payment {

	/**
	 * Constructor for a BPay payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public BPayPayment(String id) {
		super("BPAY", id);
	}
}
