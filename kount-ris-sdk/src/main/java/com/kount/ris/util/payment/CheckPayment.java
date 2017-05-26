package com.kount.ris.util.payment;

/**
 * A class representing a check payment.
 * </p>
 * Sets the PTYP parameter to "CHEK".
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class CheckPayment extends Payment {

	/**
	 * Constructor for a check payment.
	 * 
	 * @param micr
	 *            The MICR (Magnetic Ink Character Recognition) line on the
	 *            check.
	 */
	public CheckPayment(String micr) {
		super("CHEK", micr);
	}

}
