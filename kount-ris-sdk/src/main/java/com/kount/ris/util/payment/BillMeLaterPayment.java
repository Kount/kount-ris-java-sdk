package com.kount.ris.util.payment;

/**
 * A class representing a bill me later payment.
 * </p>
 * Sets the PTYP parameter to "BLML".
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class BillMeLaterPayment extends Payment {

	/**
	 * Constructor for a bill me later payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public BillMeLaterPayment(String id) {
		super("BLML", id);
	}

}
