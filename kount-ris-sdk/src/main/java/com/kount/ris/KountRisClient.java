package com.kount.ris;

import com.kount.ris.transport.HttpApiTransport;
import com.kount.ris.transport.KountHttpTransport;
import com.kount.ris.transport.Transport;
import com.kount.ris.util.RisException;
import com.kount.ris.util.RisResponseException;
import com.kount.ris.util.RisTransportException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Controller class for the Kount RIS SDK.
 * </p>
 * This class is responsible for the correct communication between the RIS client and the RIS server.
 * It performs parameter validation, request transport, and response parsing.
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2025 Equifax
 */
public class KountRisClient {

	/**
	 * Logger.
	 */
	private static final Logger logger = LogManager.getLogger(KountRisClient.class);
	
	/**
	 * Transport to use for sending RIS request.
	 */
	protected Transport transport;

	/**
	 * StringBuilder to accumulate any error message found in the response being processed.
	 */
	protected StringBuilder errorMessage = null;

	/**
	 * API key for merchant-server authentication.
	 */
	protected String apiKey;

	/**
	 * Explicit default constructor.
	 */
	public KountRisClient() {
	}

	/**
	 * Constructor that accepts a pass phrase.
	 *
	 * @param phrase
	 *            Private key pass phrase
	 * @param url
	 *            RIS server url
	 * @param p12file
	 *            Path to PKCS12 private key file
	 */
	public KountRisClient(String phrase, String url, String p12file) throws RisTransportException {
        logger.debug("RIS endpoint URL [{}]", url);
		transport = new KountHttpTransport(phrase, url, p12file);
	}

	/**
	 * Constructor that accepts a pass phrase.
	 *
	 * @param phrase
	 *            Private key pass phrase
	 * @param url
	 *            RIS server url
	 * @param p12in
	 *            PKCS12 private key file input stream
	 */
	public KountRisClient(String phrase, String url, InputStream p12in) throws RisTransportException {
		transport = new KountHttpTransport(phrase, url, p12in);
	}

	/**
	 * Constructor for using API Keys instead of Certificates.
	 *
	 * @param url
	 *            Ris server URL
	 * @param apiKeyFile
	 *            API key file (absolute path)
	 * @throws RisTransportException
	 *             Exception if opening the api key file has a problem.
	 */
	public KountRisClient(URL url, File apiKeyFile) throws RisTransportException {
		getApiKey(apiKeyFile);
		transport = new HttpApiTransport(url, apiKey);
	}


    /**
     * For use with Migration mode and Payments Fraud integration.
     *
     * @param url Ris server URL
     * @param apiKeyFile API key file (absolute path)
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @throws RisTransportException RisTransportException
     *          Exception if opening the api key file has a problem.
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
     */
	public KountRisClient(URL url, File apiKeyFile, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint) throws RisTransportException, ConfigurationException {
		getApiKey(apiKeyFile);
		transport = new HttpApiTransport(url, apiKey, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, false);
	}

    /**
     * For use with Migration mode and Payments Fraud integration.
     *
     * @param url Ris server URL
     * @param apiKeyFile API key file (absolute path)
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @param forceUtf8 Force UTF-8 encoding this may cause encoding issues with legacy systems
     * @throws RisTransportException RisTransportException
     *          Exception if opening the api key file has a problem.
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
     */
    public KountRisClient(URL url, File apiKeyFile, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint, boolean forceUtf8) throws RisTransportException, ConfigurationException {
        getApiKey(apiKeyFile);
        transport = new HttpApiTransport(url, apiKey, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, forceUtf8);
    }

    /**
     *For use with Migration mode and Payments Fraud integration.
     *
     * @param url Ris server URL
     * @param apiKeyFile API key file (absolute path)
     * @param connectionPoolThreads Number of connection pool threads
     * @param connectionPerRoute Number of connections per route
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @throws RisTransportException RisTransportException
     *          Exception if opening the api key file has a problem.
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
     */
	public KountRisClient(URL url, File apiKeyFile, int connectionPoolThreads , int connectionPerRoute, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint) throws RisTransportException, ConfigurationException {
		getApiKey(apiKeyFile);
		transport = new HttpApiTransport(url, apiKey, connectionPoolThreads, connectionPerRoute, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, false);
	}

    /**
     *For use with Migration mode and Payments Fraud integration.
     *
     * @param url Ris server URL
     * @param apiKeyFile API key file (absolute path)
     * @param connectionPoolThreads Number of connection pool threads
     * @param connectionPerRoute Number of connections per route
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @param forceUtf8 Force UTF-8 encoding this may cause encoding issues with legacy systems
     * @throws RisTransportException RisTransportException
     *          Exception if opening the api key file has a problem.
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
     */
    public KountRisClient(URL url, File apiKeyFile, int connectionPoolThreads , int connectionPerRoute, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint, boolean forceUtf8) throws RisTransportException, ConfigurationException {
        getApiKey(apiKeyFile);
        transport = new HttpApiTransport(url, apiKey, connectionPoolThreads, connectionPerRoute, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, forceUtf8);
    }

	/**
	 * Constructor for using API Key instead of Cert.
	 *
	 * @param url
	 *            Ris server URL.
	 * @param key
	 *            API key (key data as a string).
	 */
	public KountRisClient(URL url, String key) {
		apiKey = key;
		transport = new HttpApiTransport(url, apiKey);
	}

	/**
     *For use with Migration mode and Payments Fraud integration.
     *
     * @param url Ris server URL
     * @param key API key
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
	 */
	public KountRisClient(URL url, String key, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint) throws ConfigurationException {
		apiKey = key;
		transport = new HttpApiTransport(url, apiKey, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, false);
	}

    /**
     *For use with Migration mode and Payments Fraud integration.
     *
     * @param url Ris server URL
     * @param key API key
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @param forceUtf8 Force UTF-8 encoding this may cause encoding issues with legacy systems
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
     */
    public KountRisClient(URL url, String key, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint, boolean forceUtf8) throws ConfigurationException {
        apiKey = key;
        transport = new HttpApiTransport(url, apiKey, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, forceUtf8);
    }

	/**
	 * Constructor for using API Key instead of Cert.
	 *
	 * @param url
	 *            Ris server URL.
	 * @param key
	 *            API key (key data as a string).
	 * @param connectionPoolThreads
	 *            API key (key data as an int).
	 * @param connectionPerRoute
	 *            API key (key data as an int).
	 */
	public KountRisClient(URL url, String key, int connectionPoolThreads , int connectionPerRoute ) {
		apiKey = key;
		transport = new HttpApiTransport(url, apiKey, connectionPoolThreads, connectionPerRoute );
	}

    /**
     * Constructor for using API Key instead of Cert.
     *
     * @param url Ris server URL.
     * @param key PI key (key data as a string).
     * @param connectionPoolThreads Number of connection pool threads
     * @param connectionPerRoute Number of connections per route
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
     */
    public KountRisClient(URL url, String key, int connectionPoolThreads , int connectionPerRoute, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint) throws ConfigurationException {
        apiKey = key;
        transport = new HttpApiTransport(url, apiKey, connectionPoolThreads, connectionPerRoute, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, false);
    }

    /**
     * Constructor for using API Key instead of Cert.
     *
     * @param url Ris server URL.
     * @param key PI key (key data as a string).
     * @param connectionPoolThreads Number of connection pool threads
     * @param connectionPerRoute Number of connections per route
     * @param migrationModeEnabled true if migration mode is enabled, false otherwise
     * @param paymentsFraudApiKey API key for Payments Fraud
     * @param paymentsFraudClientId Client ID for Payments Fraud
     * @param paymentsFraudApiEndpoint Payments Fraud API endpoint
     * @param paymentsFraudAuthEndpoint Payments Fraud Auth endpoint
     * @param forceUtf8 Force UTF-8 encoding this may cause encoding issues with legacy systems
     * @throws ConfigurationException ConfigurationException
     *          Exception if migration configuration is invalid.
     */
    public KountRisClient(URL url, String key, int connectionPoolThreads , int connectionPerRoute, boolean migrationModeEnabled, String paymentsFraudApiKey, String paymentsFraudClientId, String paymentsFraudApiEndpoint, String paymentsFraudAuthEndpoint, boolean forceUtf8) throws ConfigurationException {
        apiKey = key;
        transport = new HttpApiTransport(url, apiKey, connectionPoolThreads, connectionPerRoute, migrationModeEnabled, paymentsFraudApiKey, paymentsFraudClientId, paymentsFraudApiEndpoint, paymentsFraudAuthEndpoint, forceUtf8);
    }

	/**
	 * Set the transport object to use. If not specified the default transport
	 * object used is KountHttpTransport.
	 *
	 * @param t
	 *            Transport
	 */
	public void setTransport(Transport t) {
		transport = t;
	}

	/**
	 * Set api key to use.
	 * 
	 * @param key
	 *            Api key (value).
	 */
	public void setApiKey(String key) {
		apiKey = key;
	}

	/**
	 * Performs the actions of sending, and parsing a RIS request.
	 *
	 * @throws RisException
	 *             A subclass of RisException will be thrown which will be of
	 *             the type RisResponseException, RisTransportException.
	 * @param r
	 *            Request
	 * @return Response
	 */
	public Response process(Request r) throws RisException {
		logger.trace("process()");
		if (transport != null) {
			return transport.sendRequest(r.getParams());
		} else {
			throw new RisTransportException("No transport was specified, unable to send request.");
		}
	}

	/**
	 * Parse a collection of key-value strings into a Response object.
	 *
	 * @throws RisResponseException
	 *             RIS response exception
	 * @param r
	 *            Reader for character stream returned by RIS
	 * @return Response object
	 */
	protected Response parse(Reader r) throws RisResponseException {
		logger.trace("parse()");
		return Response.parseResponse(r);
	}

	/**
	 * Fetch data contained in api key file.
	 *
	 * @param apiKeyFile
	 *            API key file.
	 * @throws RisTransportException
	 *             RIS transport exception
	 */
	protected final void getApiKey(File apiKeyFile) throws RisTransportException {
		logger.trace("getApiKey()");
		if (apiKey == null && apiKeyFile != null) {
			try {
				byte[] keyBytes = Files.readAllBytes(Paths.get(apiKeyFile.toURI()));
				String key = new String(keyBytes, StandardCharsets.UTF_8);
                apiKey = key.trim();
			} catch (IOException e) {
                logger.error("API Key file ({}) could not be found:\n{}", apiKeyFile, e);
				throw new RisTransportException("API Key file (" + apiKeyFile + ") could not be found:\n" + e);
			}
		}
	}
}
