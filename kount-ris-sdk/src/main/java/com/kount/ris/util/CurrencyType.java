package com.kount.ris.util;

/**
 * Currency types.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 * 
 * @deprecated Use three-character ISO-4217 currency code.
 */
public final class CurrencyType extends Enum {

	/**
	 * Constructor for a currency type object.
	 * 
	 * @param value
	 *            value
	 */
	private CurrencyType(String value) {
		super(value);
	}

	/**
	 * United States Dollars.
	 */
	public static final CurrencyType USD = new CurrencyType("USD");

	/**
	 * European currency unit.
	 */
	public static final CurrencyType EUR = new CurrencyType("EUR");

	/**
	 * Canadian Dollar.
	 */
	public static final CurrencyType CAD = new CurrencyType("CAD");

	/**
	 * Austrailian Dollar.
	 */
	public static final CurrencyType AUD = new CurrencyType("AUD");

	/**
	 * Japanese Yen.
	 */
	public static final CurrencyType JPY = new CurrencyType("JPY");

	/**
	 * Hong Kong Dollar.
	 */
	public static final CurrencyType HKD = new CurrencyType("HKD");

	/**
	 * New Zealand Dollar.
	 */
	public static final CurrencyType NZD = new CurrencyType("NZD");
}
