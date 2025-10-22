package com.kount.ris.transport;

import com.kount.ris.Response;
import com.kount.ris.util.RisResponseException;
import com.kount.ris.util.RisTransportException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;

/**
 * RIS http data transport class.
 * </p>
 * Works with certificate authentication.
 * 
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class KountHttpTransport extends Transport {

	/**
	 * Logger.
	 */
	private static final Logger logger = LogManager.getLogger(KountHttpTransport.class);

	/**
	 * SSL socket factory.
	 */
	private SSLSocketFactory factory;

	/**
	 * Private key pass phrase.
	 */
	private char[] privateKeyPassphrase;

	/**
	 * Cert file input stream.
	 */
	private InputStream pkcs12In;

	/**
	 * Path to cert file.
	 */
	private String pkcs12file;

	/**
	 * Default algorithm to use for ssl key manager.
	 */
	private String algorithm;

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
	public KountHttpTransport(String phrase, String url, String p12file) throws RisTransportException {
        this.privateKeyPassphrase = phrase.toCharArray();
		this.risServerUrl = url;
		this.pkcs12file = p12file;
		this.pkcs12In = null;
		this.algorithm = KeyManagerFactory.getDefaultAlgorithm();
		this.factory = createSSLSocketFactory();
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
	public KountHttpTransport(String phrase, String url, InputStream p12in) throws RisTransportException {
        this.privateKeyPassphrase = phrase.toCharArray();
		this.risServerUrl = url;
		this.pkcs12file = null;
		this.pkcs12In = p12in;
		this.algorithm = KeyManagerFactory.getDefaultAlgorithm();
		this.factory = createSSLSocketFactory();
	}

	/**
	 * Constructor that accepts a private key pass phrase, a RIS url, a
	 * PKCS12 file path, and an algorithm as input.
	 *
	 * @param phrase
	 *            Private key pass phrase
	 * @param url
	 *            Ris server url
	 * @param p12file
	 *            Private key file
	 * @param algorithm
	 *            Key manager algorithm
	 */
	public KountHttpTransport(String phrase, String url, String p12file, String algorithm) throws RisTransportException {
        this.privateKeyPassphrase = phrase.toCharArray();
		this.risServerUrl = url;
		this.pkcs12file = p12file;
		this.pkcs12In = null;
		this.algorithm = algorithm;
		this.factory = createSSLSocketFactory();
	}

	private SSLSocketFactory createSSLSocketFactory() throws RisTransportException {
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
            logger.error("Unable to read PKCS12 data: {}", String.valueOf(ce));
			throw new RisTransportException("Unable to read PKCS12 data", ce);
		} catch (NoSuchAlgorithmException e) {
            logger.error("No such algorithm found: {}", String.valueOf(e));
            throw new RuntimeException(e);
        }

        KeyManagerFactory keyFact;
		try {
			keyFact = KeyManagerFactory.getInstance(this.algorithm);
		} catch (NoSuchAlgorithmException nsae) {
            logger.error("Unable to create a KeyManagerFactory of type: {}", this.algorithm, nsae);
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
            ctx = SSLContext.getInstance("TLSv1.3");
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("Unable to create SSLContext of type TLS 1.3", nsae);
            try {
                ctx = SSLContext.getInstance("TLSv1.2");
            } catch (NoSuchAlgorithmException nsa) {
                logger.error("Unable to create SSLContext of type TLS 1.2", nsa);
                throw new RisTransportException("Unable to create SSLContext of type TLS 1.2", nsa);
            }
        }

		try {
			ctx.init(keyFact.getKeyManagers(), null, null);
		} catch (KeyManagementException kme) {
			logger.error("Unable to initialize SSLContext", kme);
			throw new RisTransportException("Unable to initialize SSLContext", kme);
		}

		return ctx.getSocketFactory();
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
                logger.error("PKCS12 file specified as {} could not be found", fileName, fnfe);
				throw new RisTransportException("PKCS12 file specified as " + fileName + " could not be found", fnfe);
			}
		}

		return this.pkcs12In;
	}

	public Response sendRequest(Map<String, String> params) throws RisTransportException {
		if (!params.containsKey("PTOK")
				|| (params.containsKey("PENC") && "KHASH".equals(params.get("PENC")) && null == params.get("PTOK"))) {
			params.put("PENC", "");
		}

		try {
			URL url = new URL(this.risServerUrl);

			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();

			urlConn.setSSLSocketFactory(this.factory);

			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.setConnectTimeout(this.connectTimeout);
			urlConn.setReadTimeout(this.readTimeout);

            Response responseObj;

			try (OutputStreamWriter out = new OutputStreamWriter(urlConn.getOutputStream(), StandardCharsets.UTF_8)) {
                writeParametersToOutput(out, params);
                out.flush();
            }
			try (Reader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()))) {
                responseObj = parse(reader);
            }
		
		return responseObj; 

		} catch (IOException | RisResponseException ioe) {
			logger.error("Error fetching RIS response", ioe);
			throw new RisTransportException("An error occurred while getting the RIS response", ioe);
		}
	}

	protected Response parse(Reader r) throws RisResponseException {
		logger.trace("parse()");
		return Response.parseResponse(r);
	}

}
