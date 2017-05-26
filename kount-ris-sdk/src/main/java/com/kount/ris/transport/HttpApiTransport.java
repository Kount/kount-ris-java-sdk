package com.kount.ris.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kount.ris.util.RisTransportException;

/**
 * RIS http data transport class.
 * </p>
 * Works with JWT (JSON Web Token) authentication, following the RFC 7519 standard.
 * The used key is set as connection header with name 'X-Kount-Api-Key'.
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class HttpApiTransport extends Transport {

	/**
	 * Logger.
	 */
	private static final Log logger = LogFactory.getLog(HttpApiTransport.class);

	/**
	 * SSL socket factory.
	 */
	protected SSLSocketFactory factory = null;

	/**
	 * Cache the api key (minimize file reads to once per instatiation).
	 */
	protected String apiKey;

	/**
	 * Default transport constructor.
	 */
	public HttpApiTransport() {
		// Do nothing
	}

	/**
	 * Constructor that accepts a RIS url and an api key as input.
	 * 
	 * @param url
	 *            RIS server url.
	 * @param key
	 *            API key.
	 */
	public HttpApiTransport(URL url, String key) {
		setRisServerUrl(url.toString());
		setApiKey(key);
	}

	/**
	 * Set API Key.
	 * 
	 * @param key
	 *            String Kount Api Key (public) to use for authentication with
	 *            RIS server.
	 */
	public void setApiKey(String key) {
		apiKey = key;
	}

	/**
	 * Send transaction data to RIS.
	 * 
	 * @throws RisTransportException
	 *             RIS transport exception
	 * @param params
	 *            Map of data to send
	 * @return Reader for character stream returned by RIS
	 */
	public Reader send(Map<String, String> params) throws RisTransportException {
		if (!params.containsKey("PTOK")
				|| (params.containsKey("PENC") && "KHASH".equals(params.get("PENC")) && null == params.get("PTOK"))) {
			params.put("PENC", "");
		}

		Reader reader = null;
		try {
			URL url = new URL(this.risServerUrl);
			
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			
			urlConn.setSSLSocketFactory(this.getSslSocketFactory());
			
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.setRequestProperty("X-Kount-Api-Key", URLEncoder.encode(this.apiKey, "UTF-8"));
			urlConn.setConnectTimeout(this.connectTimeout);
			urlConn.setReadTimeout(this.readTimeout);

			OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream(), "UTF-8");
			writeParametersToOutput(out, params);
			out.flush();
			out.close();

			reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		} catch (IOException ioe) {
			logger.error("Error fetching RIS response", ioe);
			throw new RisTransportException("An error ocurred while getting the RIS response", ioe);
		}
		return reader;
	}

	private SSLSocketFactory getSslSocketFactory() throws RisTransportException {
		if (this.factory != null) {
			return this.factory;
		}
		
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLSv1.2");
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("Unable to create SSLContext of type TLSv1.2", nsae);
			throw new RisTransportException("Unable to create SSLContext of type TLSv1.2", nsae);
		}

		try {
			ctx.init(null, null, null);
		} catch (KeyManagementException kme) {
			logger.error("Unable to initialize SSLContext", kme);
			throw new RisTransportException("Unable to initialize SSLContext", kme);
		}

		this.factory = ctx.getSocketFactory();
		return this.factory;
	}
}
