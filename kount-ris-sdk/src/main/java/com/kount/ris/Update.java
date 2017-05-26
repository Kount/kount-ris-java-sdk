package com.kount.ris;

import com.kount.ris.util.RefundChargebackStatus;
import com.kount.ris.util.UpdateMode;

/**
 * RIS update class.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class Update extends Request {

	/**
	 * Class constructor Mode defaults to UpdateMode.WithResponse. Call setMode
	 * to change.
	 */
	public Update() {
		super();
		setMode(UpdateMode.NO_RESPONSE);
		params.remove("SDK");
	}

	/**
	 * Set the mode.
	 * 
	 * @param mode
	 *            Mode of the request
	 * @throws IllegalArgumentException
	 *             when mode is null
	 * @return this
	 */
	public Update setMode(UpdateMode mode) {
		if (mode == null) {
			throw new IllegalArgumentException("Mode can not be null");
		}
		this.params.put("MODE", mode.toString());
		return this;
	}

	/**
	 * Set the transaction id.
	 * 
	 * @param transactionId
	 *            String Transaction id
	 * @return this
	 */
	public Update setTransactionId(String transactionId) {
		this.params.put("TRAN", transactionId);
		return this;
	}

	/**
	 * Set the Refund/Chargeback status: R = Refund C = Chargeback.
	 * 
	 * @param refundChargebackStatus
	 *            String Refund or chargeback status
	 * @throws IllegalArgumentException
	 *             when refundChargebackStatus is null
	 * @return this
	 */
	public Update setRefundChargebackStatus(RefundChargebackStatus refundChargebackStatus) {
		if (refundChargebackStatus == null) {
			throw new IllegalArgumentException("refundChargebackStatus can not be null");
		}
		this.params.put("RFCB", refundChargebackStatus.toString());
		return this;
	}

}
