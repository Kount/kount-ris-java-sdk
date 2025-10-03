package com.kount.ris.util;

/**
 * Refund or chargeback status types.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public final class RefundChargebackStatus extends Enum {

	/**
	 * Constructor for a refund charge back status.
	 * 
	 * @param value
	 *            Value
	 */
	private RefundChargebackStatus(String value) {
		super(value);
	}

	/**
	 * "R". The transaction was a refund.
	 */
	public static final RefundChargebackStatus REFUND = new RefundChargebackStatus("R");

	/**
	 * "C". The transaction was a chargeback.
	 */
	public static final RefundChargebackStatus CHARGEBACK = new RefundChargebackStatus("C");
}
