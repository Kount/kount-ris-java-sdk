package com.kount.ris.util;

/**
 * Reply from the bank about a card request.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public final class BankcardReply extends Enum {

	/**
	 * Constructor for a bank card reply object.
	 * 
	 * @param value
	 *            Value
	 */
	private BankcardReply(String value) {
		super(value);
	}

	/**
	 * "M". There was a match.
	 */
	public static final BankcardReply MATCH = new BankcardReply("M");

	/**
	 * "N". There was not a match.
	 */
	public static final BankcardReply NO_MATCH = new BankcardReply("N");

	/**
	 * "X". No information was available.
	 */
	public static final BankcardReply UNAVAILABLE = new BankcardReply("X");
}
