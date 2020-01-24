package com.kount.ris.util.payment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * RIS payment type object.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class Payment {

	/**
	 * Payment token.
	 */
	protected String paymentToken;

	/**
	 * Payment type.
	 */
	protected String paymentType;

	/**
	 * Last 4 characters of payment token.
	 */
	protected String last4 = "";

	/**
	 * Length of payment token last 4 string.
	 */
	protected static final int LAST4_LENGTH = 4;

	/**
	 * Indicates whether payment token is khashed.
	 */
	protected boolean khashed;

	/**
	 * Logger.
	 */
	protected Logger logger = LogManager.getLogger(Payment.class);

	/**
	 * Constructor for a payment that accepts the payment ID.
	 * 
	 * @param ptyp
	 *            Payment type
	 * @param ptok
	 *            Payment token
	 */
	public Payment(String ptyp, String ptok) {
		this.paymentType = ptyp;
		this.paymentToken = ptok;
		this.setKhashed(false);
	}

	/**
	 * Get the payment ID.
	 * 
	 * @return Payment ID
	 * @deprecated Version 5.0.0 - 2012. Use method
	 *             com.kount.ris.util.payment.Payment.getPaymentToken() :
	 *             String.
	 */
	public String getPaymentId() {
		this.logger.info("The method " + "com.kount.ris.util.payment.Payment.getPaymentId() : String is "
				+ "deprecated. Use " + "com.kount.ris.util.payment.Payment.getPaymentToken() : String " + "instead.");
		return this.paymentToken;
	}

	/**
	 * Get the payment token.
	 * 
	 * @return Payment token
	 */
	public String getPaymentToken() {
		return this.paymentToken;
	}

	/**
	 * Get payment type.
	 * 
	 * @return Payment type
	 */
	public String getPaymentType() {
		return this.paymentType;
	}

	/**
	 * Set the last 4 characters of the payment token.
	 * 
	 * @param lst4
	 *            Last 4 characters
	 */
	public void setPaymentTokenLast4(String lst4) {
		this.last4 = lst4;
	}

	/**
	 * Get last 4 characters of payment token.
	 * 
	 * @return Last 4 characters
	 */
	public String getPaymentTokenLast4() {
		return this.last4;
	}

	/**
	 * Calculate and set the payment token LAST4 value.
	 */
	public void calculateLast4() {
		if (null != this.paymentToken && this.paymentToken.length() >= Payment.LAST4_LENGTH) {
			this.setPaymentTokenLast4(this.paymentToken.substring(this.paymentToken.length() - Payment.LAST4_LENGTH));
		}
	}

	/**
	 * Used to set flag indicating whether payment token has been khashed. In
	 * most cases when this class is instantiated a raw (non-khashed) payment
	 * token is passed into the constructor. This method should be invoked with
	 * boolean value "true" immediately upon object instantiation if the Payment
	 * token is already Khashed. That way the SDK will not attempt to Khash the
	 * payment token a second time before sending it to RIS.
	 * 
	 * @param hashed
	 *            True if payment token is khashed.
	 */
	public void setKhashed(boolean hashed) {
		this.khashed = hashed;
	}

	/**
	 * Get flag indicating whether payment token has already been khashed.
	 * 
	 * @return Khashed flag
	 */
	public boolean isKhashed() {
		return this.khashed;
	}

}
