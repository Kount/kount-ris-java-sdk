package com.kount.ris.util;

/**
 * A class that represents a Kount Central event.
 * </p>
 * The event object contains three fields: decision, expression, and code.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id: KcEvent.java 10541 2010-07-02 18:09:10Z mmn $
 * @copyright 2010 Keynetics Inc
 */
public class KcEvent {
	/**
	 * The decision.
	 */
	private String eventDecision;
	/**
	 * Description of the event.
	 */
	private String eventExpression;
	/**
	 * The event code.
	 */
	private String eventCode;

	/**
	 * Constructor for an event object.
	 * 
	 * @param decision
	 *            the decision for the event
	 * @param expression
	 *            expression for the event
	 * @param code
	 *            the code for the event
	 */
	public KcEvent(String decision, String expression, String code) {
		this.eventDecision = decision;
		this.eventExpression = expression;
		this.eventCode = code;
	}

	/**
	 * @return the Kount Central event decision.
	 */
	public String getDecision() {
		return this.eventDecision;
	}

	/**
	 * @return the Kount Central event expression.
	 */
	public String getExpression() {
		return this.eventExpression;
	}

	/**
	 * @return the Kount Central event code.
	 */
	public String getCode() {
		return this.eventCode;
	}

	/**
	 * @return String representation of this event.
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("Decision: ").append(this.eventDecision).append("\n");
		str.append("Expression: ").append(this.eventExpression).append("\n");
		str.append("Code: ").append(this.eventCode);

		return str.toString();
	}

}
