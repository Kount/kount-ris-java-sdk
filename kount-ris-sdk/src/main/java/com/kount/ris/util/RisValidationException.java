package com.kount.ris.util;

import java.util.List;

/**
 * Ris validation exception class.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class RisValidationException extends RisException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -928981899464223585L;

	/**
	 * List of errors encountered.
	 */
	private List<ValidationError> errs;

	/**
	 * Ris validation exception with a message.
	 * 
	 * @param message
	 *            Exception message
	 * @param errors
	 *            List of com.kount.ris.util.ValidationError objects
	 *            representing the errors encountered
	 */
	public RisValidationException(String message, List<ValidationError> errors) {
		super(message);
		this.errs = errors;
	}

	/**
	 * Ris validation exception constructor that accepts a message and a cause.
	 * 
	 * @param message
	 *            Exception message
	 * @param cause
	 *            Cause
	 */
	public RisValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Get a list of the errors encountered.
	 * 
	 * @return errors
	 */
	public List<ValidationError> getErrors() {
		return this.errs;
	}
}
