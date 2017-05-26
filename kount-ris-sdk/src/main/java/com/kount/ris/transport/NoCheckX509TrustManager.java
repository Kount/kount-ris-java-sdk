package com.kount.ris.transport;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * Implementation of X509TrustManager. Moved from AbstractTest.java
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id: NoCheckX509TrustManager.java $
 * @copyright 2010 Keynetics Inc
 */
public class NoCheckX509TrustManager implements X509TrustManager {
	/**
	 * Constructor.
	 */
	public NoCheckX509TrustManager() {
	}

	/**
	 * Check client trusted.
	 * 
	 * @param chain
	 *            X509Certificate chain
	 * @param authType
	 *            authorization type
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType) {
	}

	/**
	 * Check server trusted.
	 * 
	 * @param chain
	 *            X509Certificate chain
	 * @param authType
	 *            authorization type
	 */
	public void checkServerTrusted(X509Certificate[] chain, String authType) {
	}

	/**
	 * Get accepted issuers.
	 * 
	 * @return null
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
