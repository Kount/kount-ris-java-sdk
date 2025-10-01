package com.kount.ris.util;

/**
 * Authorization status type.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public final class AuthorizationStatus extends Enum {

	/**
	 * Constructor for an authorization status object.
	 * 
	 * @param value
	 *            Value
	 */
	private AuthorizationStatus(String value) {
		super(value);
	}

	/**
	 * "A". Transaction was approved.
	 */
	public static final AuthorizationStatus APPROVED = new AuthorizationStatus("A");

	/**
	 * "D". Transaction was declined.
	 */
	public static final AuthorizationStatus DECLINED = new AuthorizationStatus("D");
}
