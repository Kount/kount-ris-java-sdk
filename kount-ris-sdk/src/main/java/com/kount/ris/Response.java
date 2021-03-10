package com.kount.ris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kount.ris.util.KcEvent;
import com.kount.ris.util.RisResponseException;

/**
 * RIS response data class.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class Response {

	/**
	 * Parameters to be sent to RIS.
	 */
	protected Map<String, String> params;

	/**
	 * Logger.
	 */
	protected static Logger logger = LogManager.getLogger(Response.class);

	/**
	 * Constructor for a response object that accepts a map of response data.
	 * 
	 * @param p
	 *            Response parameters.
	 */
	public Response(Map<String, String> p) {
		this.params = p;
	}

	/**
	 * Get the version number.
	 * 
	 * @return RIS version
	 */
	public String getVersion() {
		return this.params.get("VERS");
	}

	/**
	 * Get the parameter from the response.
	 *
	 * @param key
	 *            The key for the parameter
	 * @return The value for the parameter
	 */
	public String getParm(String key) {
		return this.params.get(key);
	}

	/**
	 * Get the mode.
	 * 
	 * @return Mode
	 */
	public String getMode() {
		return this.params.get("MODE");
	}

	/**
	 * Get the merchant id.
	 * 
	 * @return Merchant ID
	 */
	public String getMerchantId() {
		return this.params.get("MERC");
	}

	/**
	 * Get the session id.
	 * 
	 * @return Session ID
	 */
	public String getSessionId() {
		return this.params.get("SESS");
	}

	/**
	 * Get the transaction id.
	 * 
	 * @return Transaction ID
	 */
	public String getTransactionId() {
		return this.params.get("TRAN");
	}

	/**
	 * Get the merchant order number.
	 * 
	 * @return Order number
	 */
	public String getOrderNumber() {
		return this.params.get("ORDR");
	}

	/**
	 * Get a possible error code.
	 * 
	 * @return Error code
	 */
	public String getErrorCode() {
		return this.params.get("ERRO");
	}

	/**
	 * Get the RIS auto response (A/R/D).
	 * 
	 * @return Auto response
	 */
	public String getAuto() {
		return this.params.get("AUTO");
	}

	/**
	 * Get the RIS reason for the response.
	 * 
	 * @return Reason
	 * @deprecated Version 5.0.0 Use method
	 *             com.kount.ris.Response.getReasonCode() : String.
	 */
	public String getReason() {
		logger.info("The method " + "com.kount.ris.Response.getReason() : String is deprecated. "
				+ "Use com.kount.ris.Response.getReasonCode() : String instead.");
		return this.params.get("REAS");
	}

	/**
	 * Get the merchant defined decision reason code.
	 * 
	 * @return Reason code
	 */
	public String getReasonCode() {
		return this.params.get("REASON_CODE");
	}

	/**
	 * Get the Kount score.
	 * 
	 * @return Score
	 */
	public String getScore() {
		return this.params.get("SCOR");
	}
	
	/**
	 * Get the Kount Omniscore.
	 * 
	 * @return Omniscore
	 */
	public String getOmniscore() {
		return this.params.get("OMNISCORE");
	}

	/**
	 * Get the geox.
	 * 
	 * @return Geox
	 */
	public String getGeox() {
		return this.params.get("GEOX");
	}

	/**
	 * Get the credit card brand.
	 * 
	 * @return Brand
	 */
	public String getBrand() {
		return this.params.get("BRND");
	}

	/**
	 * Get the 6 hour velocity.
	 * 
	 * @return 6 hour velocity
	 */
	public String getVelo() {
		return this.params.get("VELO");
	}

	/**
	 * Get the 6 week velocity.
	 * 
	 * @return 6 week velocity
	 */
	public String getVmax() {
		return this.params.get("VMAX");
	}

	/**
	 * Get the network type.
	 * 
	 * @return Network type
	 */
	public String getNetwork() {
		return this.params.get("NETW");
	}

	/**
	 * Get the know your customer flag.
	 * 
	 * @return Know your customer flag
	 */
	public String getKnowYourCustomer() {
		return this.params.get("KYCF");
	}

	/**
	 * Get the region the remote device is located in.
	 * 
	 * @return Region
	 */
	public String getRegion() {
		return this.params.get("REGN");
	}

	/**
	 * Get the Kaptcha flag: enabled upon request and for when RIS has no
	 * record.
	 * 
	 * @return Kaptcha flag
	 */
	public String getKaptcha() {
		return this.params.get("KAPT");
	}

	/**
	 * Get the site ID.
	 * 
	 * @return site id
	 */
	public String getSite() {
		return this.params.get("SITE");
	}

	/**
	 * Get a string representing whether the remote device is using a proxy.
	 * 
	 * @return "Y" or "N"
	 */
	public String getProxy() {
		return this.params.get("PROXY");
	}

	/**
	 * Get the number of transactions associated with the email.
	 * 
	 * @return Number of emails
	 */
	public String getEmails() {
		return this.params.get("EMAILS");
	}

	/**
	 * Get the two character country code setting in the remote device's
	 * browser.
	 * 
	 * @return Country
	 */
	public String getHttpCountry() {
		return this.params.get("HTTP_COUNTRY");
	}

	/**
	 * Get a string representing the time zone of the customer as a 3 digit
	 * number.
	 * 
	 * @return Time zone
	 */
	public String getTimeZone() {
		return this.params.get("TIMEZONE");
	}

	/**
	 * Get the number of transactions associated with the credit card.
	 * 
	 * @return Number of cards
	 */
	public String getCards() {
		return this.params.get("CARDS");
	}

	/**
	 * Get a string representing whether the end device is a remotely controlled
	 * computer.
	 * 
	 * @return "Y" or "N"
	 */
	public String getPcRemote() {
		return this.params.get("PC_REMOTE");
	}

	/**
	 * Get the number of transactions associated with the particular device.
	 * 
	 * @return Number of devices
	 */
	public String getDevices() {
		return this.params.get("DEVICES");
	}

	/**
	 * Get a string representing the five layers (Operating System, SSL, HTTP,
	 * Flash, JavaScript) of the remote device.
	 * 
	 * @return Device layers
	 */
	public String getDeviceLayers() {
		return this.params.get("DEVICE_LAYERS");
	}

	/**
	 * Get the mobile device's wireless application protocol.
	 * 
	 * @return protocol
	 */
	public String getMobileForwarder() {
		return this.params.get("MOBILE_FORWARDER");
	}

	/**
	 * Get a string representing whether or not the remote device is voice
	 * controlled.
	 * 
	 * @return "Y" or "N"
	 */
	public String getVoiceDevice() {
		return this.params.get("VOICE_DEVICE");
	}

	/**
	 * Get local time of the remote device in the YYYY-MM-DD format.
	 * 
	 * @return Local time
	 */
	public String getLocalTime() {
		return this.params.get("LOCALTIME");
	}

	/**
	 * Get the mobile device type.
	 * 
	 * @return Mobile type
	 */
	public String getMobileType() {
		return this.params.get("MOBILE_TYPE");
	}

	/**
	 * Get the device finger print.
	 * 
	 * @return Finger print
	 */
	public String getFingerPrint() {
		return this.params.get("FINGERPRINT");
	}

	/**
	 * Get a string representing whether or not the remote device allows flash.
	 * 
	 * @return "Y" or "N"
	 */
	public String getFlash() {
		return this.params.get("FLASH");
	}

	/**
	 * Get the language setting on the remote device.
	 * 
	 * @return Language
	 */
	public String getLanguage() {
		return this.params.get("LANGUAGE");
	}

	/**
	 * Get the remote device's country of origin as a two character code.
	 * 
	 * @return Country
	 */
	public String getCountry() {
		return this.params.get("COUNTRY");
	}

	/**
	 * Get a string representing whether the remote device allows JavaScript.
	 * 
	 * @return "Y" or "N"
	 */
	public String getJavaScript() {
		return this.params.get("JAVASCRIPT");
	}

	/**
	 * Get a string representing whether the remote device allows cookies.
	 * 
	 * @return "Y" or "N"
	 */
	public String getCookies() {
		return this.params.get("COOKIES");
	}

	/**
	 * Get a string representing whether the remote device is a mobile device.
	 * 
	 * @return "Y" or "N"
	 */
	public String getMobileDevice() {
		return this.params.get("MOBILE_DEVICE");
	}

	/**
	 * Get pierced IP address.
	 * 
	 * @return Pierced IP address
	 */
	public String getPiercedIPAddress() {
		return this.params.get("PIP_IPAD");
	}

	/**
	 * Get latitude of pierced IP address.
	 * 
	 * @return Latitude of pierced IP address
	 */
	public String getPiercedIPAddressLatitude() {
		return this.params.get("PIP_LAT");
	}

	/**
	 * Get longitude of pierced IP address.
	 * 
	 * @return Longitude of pierced IP address
	 */
	public String getPiercedIPAddressLongitude() {
		return this.params.get("PIP_LON");
	}

	/**
	 * Get country of pierced IP address.
	 * 
	 * @return Country of pierced IP address
	 */
	public String getPiercedIPAddressCountry() {
		return this.params.get("PIP_COUNTRY");
	}

	/**
	 * Get region of pierced IP address.
	 * 
	 * @return Region of pierced IP address
	 */
	public String getPiercedIPAddressRegion() {
		return this.params.get("PIP_REGION");
	}

	/**
	 * Get city of pierced IP address.
	 * 
	 * @return City of pierced IP address
	 */
	public String getPiercedIPAddressCity() {
		return this.params.get("PIP_CITY");
	}

	/**
	 * Get organization of pierced IP address.
	 * 
	 * @return Organization of pierced IP address
	 */
	public String getPiercedIPAddressOrganization() {
		return this.params.get("PIP_ORG");
	}

	/**
	 * Get proxy IP address.
	 * 
	 * @return Proxy IP address
	 */
	public String getIPAddress() {
		return this.params.get("IP_IPAD");
	}

	/**
	 * Get latitude of proxy IP address.
	 * 
	 * @return Latitude of proxy IP address
	 */
	public String getIPAddressLatitude() {
		return this.params.get("IP_LAT");
	}

	/**
	 * Get longitude of proxy IP address.
	 * 
	 * @return Longitude of proxy IP address
	 */
	public String getIPAddressLongitude() {
		return this.params.get("IP_LON");
	}

	/**
	 * Get country of proxy IP address.
	 * 
	 * @return Country of proxy IP address
	 */
	public String getIPAddressCountry() {
		return this.params.get("IP_COUNTRY");
	}

	/**
	 * Get region of proxy IP address.
	 * 
	 * @return Region of proxy IP address
	 */
	public String getIPAddressRegion() {
		return this.params.get("IP_REGION");
	}

	/**
	 * Get city of proxy IP address.
	 * 
	 * @return City of proxy IP address
	 */
	public String getIPAddressCity() {
		return this.params.get("IP_CITY");
	}

	/**
	 * Get organization of proxy IP address.
	 * 
	 * @return Organization of proxy IP address
	 */
	public String getIPAddressOrganization() {
		return this.params.get("IP_ORG");
	}

	/**
	 * Get date device first seen.
	 * 
	 * @return Date device first seen
	 */
	public String getDateDeviceFirstSeen() {
		return this.params.get("DDFS");
	}

	/**
	 * Get user agent string.
	 * 
	 * @return User agent string
	 */
	public String getUserAgentString() {
		return this.params.get("UAS");
	}

	/**
	 * Get device screen resolution.
	 * 
	 * @return Device screen resolution (HxW - Height by Width)
	 */
	public String getDeviceScreenResolution() {
		return this.params.get("DSR");
	}

	/**
	 * Get operating system (derived from user agent string).
	 * 
	 * @return OS (operating system)
	 */
	public String getOS() {
		return this.params.get("OS");
	}

	/**
	 * Get browser (derived from user agent string).
	 * 
	 * @return Browser
	 */
	public String getBrowser() {
		return this.params.get("BROWSER");
	}

	/**
	 * Get the Kount Central Customer ID.
	 * 
	 * @return String KC Id
	 */
	public String getKcCustomerId() {
		return this.params.get("KC_CUSTOMER_ID");
	}

	/**
	 * Get the Kount Central Decision.
	 * 
	 * @return String The decision
	 */
	public String getKcDecision() {
		return this.params.get("KC_DECISION");
	}

	/**
	 * Get the number of KC warnings associated with the response.
	 * 
	 * @return Number of KC warnings
	 */
	public int getKcWarningCount() {
		try {
			String value = this.params.get("KC_WARNING_COUNT");
			return (value == null) ? 0 : Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			logger.error("RIS returned an KC_WARNING_COUNT field which could " + "not be parsed to a number", nfe);
		}
		return 0;
	}

	/**
	 * Get an ArrayList of the KC warnings returned by this Response.
	 * 
	 * @return KC warnings
	 */
	public List<String> getKcWarnings() {
		List<String> warnings = new ArrayList<>();
		int warningCount = this.getKcWarningCount();
		for (int i = 1; i <= warningCount; i++) {
			warnings.add(this.params.get("KC_WARNING_" + i));
		}
		return warnings;
	}

	/**
	 * Get the number of KC errors associated with the response.
	 * 
	 * @return Number of KC errors
	 */
	public int getKcErrorCount() {
		try {
			String value = this.params.get("KC_ERROR_COUNT");
			return (value == null) ? 0 : Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			logger.error("RIS returned an KC_ERROR_COUNT field which could " + "not be parsed to a number", nfe);
		}
		return 0;
	}

	/**
	 * Get an ArrayList of the KC errors returned by this Response.
	 * 
	 * @return KC errors
	 */
	public List<String> getKcErrors() {
		List<String> errors = new ArrayList<>();
		int errorCount = this.getKcErrorCount();
		for (int i = 1; i <= errorCount; i++) {
			errors.add(this.params.get("KC_ERROR_" + i));
		}
		return errors;
	}

	/**
	 * Get the number of KC events associated with the response.
	 * 
	 * @return Number of KC events
	 */
	public int getKcEventCount() {
		try {
			String value = this.params.get("KC_TRIGGERED_COUNT");
			return (value == null) ? 0 : Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			logger.error("RIS returned an KC_TRIGGERED_COUNT field which could " + "not be parsed to a number", nfe);
		}
		return 0;
	}

	/**
	 * Get an ArrayList of the KC events returned by this Response.
	 * 
	 * @return KC events
	 */
	public List<KcEvent> getKcEvents() {
		List<KcEvent> events = new ArrayList<>();
		int eventCount = this.getKcEventCount();
		for (int i = 1; i <= eventCount; i++) {
			KcEvent event = new KcEvent(this.params.get("KC_EVENT_" + i + "_DECISION"), this.params.get("KC_EVENT_" + i
					+ "_EXPRESSION"), this.params.get("KC_EVENT_" + i + "_CODE"));
			events.add(event);
		}
		return events;
	}

	/**
	 * Print all values in the object.
	 *
	 * @return The string representation of the response
	 */
	public String toString() {
		String s = params.toString();
		return s.replaceAll(",", "\n");
	}

	/**
	 * Get a Map of the rules triggered by this Response.
	 * 
	 * @return Rules
	 */
	public Map<String, String> getRulesTriggered() {
		Map<String, String> rules = new HashMap<>();
		int rulesTriggeredCount = this.getNumberRulesTriggered();
		String ruleId = null;
		for (int i = 0; i < rulesTriggeredCount; i++) {
			ruleId = this.params.get("RULE_ID_" + i);
			rules.put(ruleId, this.params.get("RULE_DESCRIPTION_" + i));
		}
		return rules;
	}

	/**
	 * Get the number of rules triggered associated with the response.
	 * 
	 * @return number of rules triggered
	 */
	public int getNumberRulesTriggered() {
		// A RIS response will always contain the field RULES_TRIGGERED which
		// will be set to zero if there are no rules triggered.
		try {
			return Integer.parseInt(this.params.get("RULES_TRIGGERED"));
		} catch (NumberFormatException nfe) {
			logger.error("RIS returned a RULES_TRIGGERED field " + "which could not be parsed to a number", nfe);
		}
		return 0;
	}

	/**
	 * Get an ArrayList of the warnings returned by this Response.
	 * 
	 * @return Warnings
	 */
	public List<String> getWarnings() {
		List<String> warnings = new ArrayList<>();
		int warningCount = this.getWarningCount();
		for (int i = 0; i < warningCount; i++) {
			warnings.add(this.params.get("WARNING_" + i));
		}
		return warnings;
	}

	/**
	 * Get the number of warnings associated with the response.
	 * 
	 * @return number of warnings
	 */
	public int getWarningCount() {
		// A RIS response will always contain the field WARNING_COUNT which
		// will be set to zero if there are no warnings.
		try {
			return Integer.parseInt(this.params.get("WARNING_COUNT"));
		} catch (NumberFormatException nfe) {
			logger.error("RIS returned a WARNING_COUNT field " + "which could not be parsed to a number", nfe);
		}
		return 0;
	}

	/**
	 * Get an ArrayList of errors associated with the response.
	 * 
	 * @return Errors
	 */
	public List<String> getErrors() {
		List<String> errors = new ArrayList<>();
		int errorCount = this.getErrorCount();
		for (int i = 0; i < errorCount; i++) {
			errors.add(this.params.get("ERROR_" + i));
		}
		return errors;
	}

	/**
	 * Get the number of errors associated with the response.
	 * 
	 * @return number of errors
	 */
	public int getErrorCount() {
		// A normal response will not contain any errors in which case the
		// RIS response field ERROR_COUNT will not be sent.
		try {
			String value = this.params.get("ERROR_COUNT");
			return (value == null) ? 0 : Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			logger.error("RIS returned an ERROR_COUNT field which could " + "not be parsed to a number", nfe);
		}
		return 0;
	}

	/**
	 * Parse the RIS repsonse and return a Response object.
	 * 
	 * @throws RisResponseException
	 *             When error encountered parsing response
	 * @param r
	 *            Reader for character stream returned by RIS
	 * @return Response
	 */
	public static Response parseResponse(Reader r) throws RisResponseException {
		BufferedReader reader = new BufferedReader(r);
		Map<String, String> responseFields = new HashMap<>();
		String line = null;
		try {
			while (null != (line = reader.readLine())) {
				// logger.debug(line);
				String[] field = line.split("=", 2);
				if (field.length > 1) {
					responseFields.put(field[0], field[1]);
				}
			}
		} catch (IOException ioe) {
			logger.error("Error parsing RIS response", ioe);
			throw new RisResponseException("Error parsing RIS response");
		}
		Response response = new Response(responseFields);
		return response;
	}

	/**
	 * Get LexisNexis Chargeback Defender attribute data associated with the RIS
	 * transaction. Please contact your Kount representative to enable support
	 * for this feature in your merchant account.
	 * 
	 * @return Map of attributes where the keys are the attribute names and the
	 *         values are the attribute values.
	 */
	public Map<String, String> getLexisNexisCbdAttributes() {
		return this.getPrefixedResponseDataMap("CBD_");
	}

	/**
	 * Get LexisNexis Instant ID attributes associated with the RIS transaction.
	 * Please contact your Kount representative to enable support for this
	 * feature in your merchant account.
	 * 
	 * @return Map of attributes where the keys are the attribute names and the
	 *         values are the attribute values.
	 */
	public Map<String, String> getLexisNexisInstandIdAttributes() {
		return this.getPrefixedResponseDataMap("INSTANTID_");
	}

	/**
	 * Get MasterCard Fraud Score associated with the RIS transaction. Please
	 * contact your Kount representative to enable support for this feature in
	 * your merchant account.
	 * 
	 * @return MasterCard Fraud Score
	 */
	public String getMasterCardFraudScore() {
		return this.params.get("MASTERCARD");
	}

	/**
	 * Get a map of the response data where the keys are the RIS response keys
	 * that begin with a specified prefix.
	 * 
	 * @param prefix
	 *            Key prefix.
	 * @return Map of key-value pairs for a specified RIS key prefix.
	 */
	protected Map<String, String> getPrefixedResponseDataMap(String prefix) {
		Map<String, String> data = new HashMap<>();
		for (String key : this.params.keySet()) {
			if (key.startsWith(prefix)) {
				data.put(key.substring(prefix.length()), this.params.get(key));
			}
		}
		return data;
	}

	/**
	 * Get a map of the rules counters triggered in the response.
	 * 
	 * @return Map Key: counter name, Value: counter value.
	 */
	public Map<String, String> getCountersTriggered() {
		Map<String, String> counters = new HashMap<>();
		int numCounters = this.getNumberCountersTriggered();
		for (int i = 0; i < numCounters; i++) {
			counters.put(this.params.get("COUNTER_NAME_" + i), this.params.get("COUNTER_VALUE_" + i));
		}
		return counters;
	}

	/**
	 * Get the number of rules counters triggered in the response.
	 * 
	 * @return int Number of counters triggered
	 */
	public int getNumberCountersTriggered() {
		try {
			return Integer.parseInt((String) this.params.get("COUNTERS_TRIGGERED"));
		} catch (NumberFormatException nfe) {
			logger.error("RIS returned a COUNTERS_TRIGGERED field " + "which could not be parsed to a number", nfe);
		}
		return 0;
	}

	/**
	 * Get the Previously WhiteListed.
	 *
	 * @return PREVIOUSLY_WHITELISTED
	 */
	public String getPreviouslyWhiteListed() {
		return this.params.get("PREVIOUSLY_WHITELISTED");
	}


	/**
	 * Get the 3D Secure Merchant Response.
	 *
	 * @return  3D_SECURE_MERCHANT_RESPONSE
	 */
	public String get3DSecureMerchantResponse() {
		return this.params.get("3D_SECURE_MERCHANT_RESPONSE");
	}

}
