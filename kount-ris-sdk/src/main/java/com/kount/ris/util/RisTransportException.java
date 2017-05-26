package com.kount.ris.util;

/**
 * Ris transport exception class.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class RisTransportException extends RisException {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 6989385172399274923L;

	/**
	 * Ris transport exception with a message.
	 * 
	 * @param message
	 *            Exception message
	 */
	public RisTransportException(String message) {
		super(message);
	}

	/**
	 * Ris transport exception constructor that accepts a message and a cause.
	 * 
	 * @param message
	 *            Exception message
	 * @param cause
	 *            Cause
	 */
	public RisTransportException(String message, Throwable cause) {
		super(message, cause);
	}
}
