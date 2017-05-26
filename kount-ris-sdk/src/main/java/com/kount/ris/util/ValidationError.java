package com.kount.ris.util;

/**
 * A validation error.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 * @since 4.2.0
 */
public class ValidationError {

	/**
	 * The error type.
	 */
	private ValidationErrorType error;
	/**
	 * The field.
	 */
	private String field;
	/**
	 * The RIS mode.
	 */
	private String mode;
	/**
	 * Field value.
	 */
	private String value;
	/**
	 * Regular expression pattern.
	 */
	private String pattern;
	/**
	 * Maximum allowable length of a field.
	 */
	private int maxLength;

	/**
	 * Constructor for missing required field.
	 * 
	 * @param fld
	 *            The name of the bad field
	 * @param md
	 *            The RIS mode the field is associated with
	 */
	public ValidationError(String fld, String md) {
		this.error = ValidationErrorType.REQUIRED_ERR;
		this.field = fld;
		this.mode = md;
	}

	/**
	 * Constructor for regular expression error.
	 * 
	 * @param fld
	 *            The name of the bad field
	 * @param val
	 *            The value of the field
	 * @param patt
	 *            The regular expression violated.
	 */
	public ValidationError(String fld, String val, String patt) {
		this.error = ValidationErrorType.REGEX_ERR;
		this.field = fld;
		this.value = val;
		this.pattern = patt;
	}

	/**
	 * Constructor for maximum length error.
	 * 
	 * @param fld
	 *            The name of the bad field
	 * @param val
	 *            The value of the field
	 * @param length
	 *            The maximum allowable length
	 */
	public ValidationError(String fld, String val, int length) {
		this.error = ValidationErrorType.LENGTH_ERR;
		this.field = fld;
		this.value = val;
		this.maxLength = length;
	}

	/**
	 * Get the string representation of the error.
	 * 
	 * @return message
	 */
	public String toString() {
		if (this.error.equals(ValidationErrorType.LENGTH_ERR)) {
			return "Field [" + this.field + "] has length [" + this.value.length()
					+ "] which is longer than the maximum of [" + this.maxLength + "]";
		} else if (this.error.equals(ValidationErrorType.REGEX_ERR)) {
			return "Field [" + this.field + "] has value [" + this.value + "] which does not match the pattern ["
					+ this.pattern + "]";
		} else if (this.error.equals(ValidationErrorType.REQUIRED_ERR)) {
			return "Required field [" + this.field + "] missing for mode [" + this.mode + "]";
		}
		return null;
	}
}
