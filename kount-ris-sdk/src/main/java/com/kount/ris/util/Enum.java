package com.kount.ris.util;

/**
 * Abstract enum class.
 * </p>
 * Provides a single-parameter constructor and a <pre>toString()</pre> method.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
abstract class Enum {
	/**
	 * Value.
	 */
	private final String value;

	/**
	 * Constructor for enum object.
	 * 
	 * @param val
	 *            Value
	 */
	protected Enum(String val) {
		this.value = val;
	}

	/**
	 * String value of this enum type.
	 * 
	 * @return String value
	 */
	public String toString() {
		return this.value;
	}
}
