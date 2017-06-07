package com.kount.ris.util.payment;

/**
 * A class representing a Neteller payment.
 * </p>
 * Sets the PTYP parameter to "NETELLER".
 * 
 */
public class NetellerPayment extends Payment {

	/**
	 * Constructor for a Neteller payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public NetellerPayment(String id) {
		super("NETELLER", id);
	}
}
