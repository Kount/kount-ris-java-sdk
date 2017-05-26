package com.kount.ris.util;

/**
 * RIS exeption class.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class RisException extends Exception {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 7277950309476486466L;

	/**
	 * Ris exception.
	 */
	public RisException() {
		super();
	}

	/**
	 * Ris exception with a message.
	 * 
	 * @param message
	 *            Exception message
	 */
	public RisException(String message) {
		super(message);
	}

	/**
	 * Ris exception with a message and cause.
	 * 
	 * @param message
	 *            Exception message
	 * @param cause
	 *            Exception cause
	 */
	public RisException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Ris exception with a message and cause.
	 * 
	 * @param cause
	 *            Exception cause
	 */
	public RisException(Throwable cause) {
		super(cause);
	}

}
