package com.kount.ris.util;

/**
 * Update mode types.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public final class UpdateMode extends Enum {

	/**
	 * Update mode constructor.
	 * 
	 * @param value
	 *            Value
	 */
	private UpdateMode(String value) {
		super(value);
	}

	/**
	 * "U". Get no RIS response with the update.
	 */
	public static final UpdateMode NO_RESPONSE = new UpdateMode("U");

	/**
	 * "X". Get a RIS response with the update.
	 */
	public static final UpdateMode WITH_RESPONSE = new UpdateMode("X");

}
