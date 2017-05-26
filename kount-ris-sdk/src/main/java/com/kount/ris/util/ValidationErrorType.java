package com.kount.ris.util;

/**
 * Validation error type.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 * @since 4.2.0
 */
public final class ValidationErrorType extends Enum {

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            Value of the error type.
	 */
	private ValidationErrorType(String value) {
		super(value);
	}

	/**
	 * A field regular expression error type.
	 */
	public static final ValidationErrorType REGEX_ERR = new ValidationErrorType("REGEX");
	/**
	 * A field length error type.
	 */
	public static final ValidationErrorType LENGTH_ERR = new ValidationErrorType("LENGTH");
	/**
	 * A required field error type.
	 */
	public static final ValidationErrorType REQUIRED_ERR = new ValidationErrorType("REQUIRED");

}
