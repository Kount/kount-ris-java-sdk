package com.kount.ris.transport;

import com.kount.ris.util.RisTransportException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.Map;

/**
 * RIS data transport interface.
 * </p>
 * Provides basic and utility methods for configuring HTTP connections for communication with RIS.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public abstract class Transport {

	/**
	 * Send transaction data to RIS.
	 * 
	 * @throws RisTransportException
	 *             RIS transport exception
	 * @param params
	 *            Map of data to send
	 * @return Reader for character stream returned by RIS
	 */
	public abstract Reader send(Map<String, String> params) throws RisTransportException;
	
	/**
	 * Ris server endpoint url.
	 */
	protected String risServerUrl;
	
	/**
	 * Ris connection establishment timeout, a timeout of zero is interpreted as
	 * an infinite timeout.
	 */
	protected int connectTimeout;

	/**
	 * Ris connection read timeout, a timeout of zero is interpreted as an
	 * infinite timeout.
	 */
	protected int readTimeout;

	/**
	 * Set the RIS server url.
	 * 
	 * @param url
	 *            Url
	 */
	public void setRisServerUrl(String url) {
		this.risServerUrl = url;
	}
	
	/**
	 * Set the transports connection timeout. See
	 * http://docs.oracle.com/javase/7
	 * /docs/api/java/net/URLConnection.html#setConnectTimeout(int)
	 * 
	 * @param timeout
	 *            Timeout in milliseconds
	 */
	public void setConnectTimeout(int timeout) {
		this.connectTimeout = timeout;
	}

	/**
	 * Set the transports connection read timeout.
	 * http://docs.oracle.com/javase/
	 * 7/docs/api/java/net/URLConnection.html#setReadTimeout(int)
	 * 
	 * @param timeout
	 *            Timeout in milliseconds
	 */
	public void setReadTimeout(int timeout) {
		this.readTimeout = timeout;
	}
	
	/**
	 * Serializes the given map of request parameters to the provided {@link OutputStream}.
	 * </p>
	 * All values are URL-encoded with UTF-8.
	 * 
	 * @param out
	 * @param params
	 * @throws IOException
	 */
	protected static void writeParametersToOutput(OutputStreamWriter out, Map<String, String> params) throws IOException {
		for (String key : params.keySet()) {
			String value = params.get(key);
			out.write(key);
			out.write('=');
			out.write(URLEncoder.encode(null == value ? "" : value, "UTF-8"));
			out.write('&');
		}
	}
}