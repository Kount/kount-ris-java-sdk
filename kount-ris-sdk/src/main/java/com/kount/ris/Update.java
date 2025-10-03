package com.kount.ris;

import com.kount.ris.util.RefundChargebackStatus;
import com.kount.ris.util.UpdateMode;

/**
 * RIS update class.
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class Update extends Request {

    /**
     * Class constructor Mode defaults to UpdateMode.WithResponse. Call setMode to change.
     */
    public Update() {
        super();
        this.params.put("MODE", UpdateMode.NO_RESPONSE.toString());
        this.params.remove("SDK");
    }

    /**
     * Set the mode.
     *
     * @param mode Mode of the request
     * @return this
     * @throws IllegalArgumentException when mode is null
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
     * @param transactionId String Transaction id
     * @return this
     */
    public Update setTransactionId(String transactionId) {
        this.params.put("TRAN", transactionId);
        return this;
    }

    /**
     * Set the Refund/Chargeback status: R = Refund C = Chargeback.
     *
     * @param refundChargebackStatus String Refund or chargeback status
     * @return this
     * @throws IllegalArgumentException when refundChargebackStatus is null
     */
    public Update setRefundChargebackStatus(RefundChargebackStatus refundChargebackStatus) {
        if (refundChargebackStatus == null) {
            throw new IllegalArgumentException("refundChargebackStatus can not be null");
        }
        this.params.put("RFCB", refundChargebackStatus.toString());
        return this;
    }

}
