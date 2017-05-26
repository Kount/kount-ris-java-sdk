package com.kount.ris.util.payment;

/**
 * No payment type. A class representing no payment.
 * </p>
 * Sets the PTYP parameter to "NONE".
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class NoPayment extends Payment {

	/**
	 * Constructor for a NoPayment object.
	 */
	public NoPayment() {
		super("NONE", null);
	}

}
