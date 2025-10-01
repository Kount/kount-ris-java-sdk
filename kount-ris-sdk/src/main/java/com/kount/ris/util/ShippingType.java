package com.kount.ris.util;

/**
 * Shipping types.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public final class ShippingType extends Enum {

	/**
	 * Shipping type constructor.
	 * 
	 * @param value
	 *            Value
	 */
	private ShippingType(String value) {
		super(value);
	}

	/**
	 * "SD". Same day shipping type.
	 */
	public static final ShippingType SAME_DAY = new ShippingType("SD");

	/**
	 * "ND". Next day shipping type.
	 */
	public static final ShippingType NEXT_DAY = new ShippingType("ND");

	/**
	 * "2D". Second day shipping type.
	 */
	public static final ShippingType SECOND_DAY = new ShippingType("2D");

	/**
	 * "ST". Standard shipping type.
	 */
	public static final ShippingType STANDARD = new ShippingType("ST");

	/**
	 * "DE". Delivery shipping type.
	 */
	public static final ShippingType DELIVERY = new ShippingType("DE");

	/**
	 * "PU". Pick-up shipping type.
	 */
	public static final ShippingType PICK_UP = new ShippingType("PU");
}
