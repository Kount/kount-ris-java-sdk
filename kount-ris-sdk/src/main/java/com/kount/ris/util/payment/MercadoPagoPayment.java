package com.kount.ris.util.payment;

/**
 * A class representing an Mercado Pago payment.
 * </p>
 * Sets the PTYP parameter to "MERCADE_PAGO".
 * 
 */
public class MercadoPagoPayment extends Payment {

	/**
	 * Constructor for an Mercado Pago payment.
	 * 
	 * @param id
	 *            The payment ID
	 */
	public MercadoPagoPayment(String id) {
		super("MERCADE_PAGO", id); // MERCADE_PAGO -- this is not a typo in the SDK
	}
}
