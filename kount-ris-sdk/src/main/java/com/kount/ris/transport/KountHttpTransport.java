package com.kount.ris.transport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kount.ris.util.RisTransportException;

/**
 * RIS http data transport class.
 * </p>
 * Works with certificate authentication.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2010 Keynetics Inc
 */
public class KountHttpTransport extends Transport {

	/**
	 * Logger.
	 */
	private static final Log logger = LogFactory.getLog(KountHttpTransport.class);

	/**
	 * SSL socket factory.
	 */
	protected SSLSocketFactory factory;

	/**
	 * Private key pass phrase.
	 */
	protected char[] privateKeyPassphrase;

	/**
	 * Cert file input stream.
	 */
	protected InputStream pkcs12In;

	/**
	 * Path to cert file.
	 */
	protected String pkcs12file;

	/**
	 * Default algorithm to use for ssl key manager.
	 */
	protected String algorithm = "SunX509";

	/**
	 * Default transport constructor.
	 */
	public KountHttpTransport() {
	}

	/**
	 * Constructor that accepts a private key pass phrase, a RIS url, and a
	 * PKCS12 file path as input.
	 * 
	 * @param phrase
	 *            Private key pass phrase
	 * @param url
	 *            Ris server url
	 * @param p12file
	 *            Private key file
	 */
	public KountHttpTransport(String phrase, String url, String p12file) {
		this.setPassPhrase(phrase);
		this.risServerUrl = url;
		this.pkcs12file = p12file;
	}

	/**
	 * Constructor that accepts a private key pass phrase, a RIS url, and a
	 * PKCS12 resource stream as input.
	 * 
	 * @param phrase
	 *            Private key pass phrase
	 * @param url
	 *            Ris server url
	 * @param p12in
	 *            Private key resource stream
	 */
	public KountHttpTransport(String phrase, String url, InputStream p12in) {
		this.setPassPhrase(phrase);
		this.risServerUrl = url;
		this.pkcs12In = p12in;
	}

	/**
	 * Set the private key pass phrase.
	 * 
	 * @param p
	 *            Pass phrase
	 */
	public void setPassPhrase(String p) {
		this.privateKeyPassphrase = p.toCharArray();
	}

	/**
	 * Set the standard algorithm to use for the javax.net.ssl.KeyManagerFactory
	 * See Java online documentation for a list of version J2SE 1.4 supported
	 * algorithms. If not specified the algorithm used is SunX509.
	 * 
	 * @param a
	 *            Name of standard algorithm.
	 */
	public void setAlgorithm(String a) {
		this.algorithm = a;
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

			urlConn.setSSLSocketFactory(this.getSSLSocketFactory());

			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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

	/**
	 * Get an SSL Socket factory.
	 * 
	 * @throws RisTransportException
	 *             RIS transport exception
	 * @return SSLSocketFactory
	 */
	protected SSLSocketFactory getSSLSocketFactory() throws RisTransportException {
		if (null != this.factory) {
			return this.factory;
		}

		KeyStore store;
		try {
			store = KeyStore.getInstance("PKCS12");
		} catch (KeyStoreException kse) {
			logger.error("Error creating keystore of type PKCS12", kse);
			throw new RisTransportException("Unable to create KeyStore of type PKCS12", kse);
		}

		try {
			InputStream pkIn = this.getPkcs12Data();
			store.load(pkIn, this.privateKeyPassphrase);
			pkIn.close();
		} catch (IOException ioe) {
			logger.error("Unable to read PKCS12 data", ioe);
			throw new RisTransportException("Unable to read PKCS12 data", ioe);
		} catch (CertificateException ce) {
			logger.error("Unable to read PKCS12 data" + ce);
			throw new RisTransportException("Unable to read PKCS12 data", ce);
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("Unable to read PKCS12 data", nsae);
			throw new RisTransportException("Unable to read PKCS12 data", nsae);
		}

		KeyManagerFactory keyFact;
		try {
			keyFact = KeyManagerFactory.getInstance(this.algorithm);
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("Unable to create a KeyManagerFactory of type: " + this.algorithm, nsae);
			throw new RisTransportException("Unable to create a KeyManagerFactory of type " + this.algorithm, nsae);
		}

		try {
			keyFact.init(store, this.privateKeyPassphrase);
		} catch (UnrecoverableKeyException uke) {
			logger.error("Private key passphrase is " + "incorrect for PKCS12 data", uke);
			throw new RisTransportException("Private key passphrase is incorrect for PKCS12 data", uke);
		} catch (KeyStoreException kse) {
			logger.error("Unable to initialize KeyManagerFactory", kse);
			throw new RisTransportException("Unable to initialize KeyManagerFactory", kse);
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("PKCS12 is not supported in the current environment", nsae);
			throw new RisTransportException("PKCS12 is not supported in the current environment", nsae);
		}

		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLSv1.2");
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("Unable to create SSLContext of type TLS", nsae);
			throw new RisTransportException("Unable to create SSLContext of type TLS", nsae);
		}

		try {
			ctx.init(keyFact.getKeyManagers(), null, null);
		} catch (KeyManagementException kme) {
			logger.error("Unable to initialize SSLContext", kme);
			throw new RisTransportException("Unable to initialize SSLContext", kme);
		}

		this.factory = ctx.getSocketFactory();
		return this.factory;
	}

	/**
	 * Fetch data contained in PKCS12 private key file.
	 * 
	 * @throws RisTransportException
	 *             RIS transport exception
	 * @return InputStream
	 */
	protected InputStream getPkcs12Data() throws RisTransportException {
		if (null == this.pkcs12In) {
			String fileName = this.pkcs12file;
			try {
				this.pkcs12In = new FileInputStream(fileName);
			} catch (FileNotFoundException fnfe) {
				logger.error("PKCS12 file specified as " + fileName + " could not be found", fnfe);
				throw new RisTransportException("PKCS12 file specified as " + fileName + " could not be found", fnfe);
			}
		}

		return this.pkcs12In;
	}
}
