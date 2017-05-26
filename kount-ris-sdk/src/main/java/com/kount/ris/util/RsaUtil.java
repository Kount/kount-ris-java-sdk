package com.kount.ris.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;

/**
 * RSA encrypt a string with a public key.
 * 
 * @author Conrad Kennington, Brent Reed
 * @version $Id$
 * @copyright 2014 Kount Inc
 */
public final class RsaUtil {
	/**
	 * Cipher instance to be used.
	 */
	public static final String CIPHER_INSTANCE = "RSA";

	/**
	 * Cipher string we will be using.
	 */
	public static final String CIPHER_STRING = "RSA/ECB/PKCS1Padding";

	/**
	 * Singleton instance of this class.
	 */
	private static RsaUtil rsaUtil;

	/**
	 * Private Key to be used for decryption. Primarily for internal testing of
	 * the encryption process. Only encryption will actually be used via the
	 * SDK.
	 */
	private PrivateKey privateKey;

	/**
	 * Public key to be used for encryption.
	 */
	private PublicKey publicKey;

	/**
	 * Public key to be used for encrypting the payment token prior to transport
	 * to Kount.
	 */
	private final String rsaKey = "-----BEGIN PUBLIC KEY-----\n"
			+ "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAwyC+qwP7LEA/MFRp3d4p\n"
			+ "qNMEPavUIjMcKvUbfa9HaGmZuslPT9BY4cO18mrHTIoMOpazB0bN1ImYjagJitq9\n"
			+ "v0PLupIocFBEHVL8d+4G7anyiLQYMgztRv12VKbGXNmEZdu9VqptlXoOGvpMSVc0\n"
			+ "6zcwY4u9GaC+poa0AVX2iIzKsvzaNImHI4/QxJ7gm3QV1rEDcIeVT/GQc/YYVfwn\n"
			+ "hrGAazrao1KspBZQ5OEdeI7gMlVyiwiULhZDlePKY0nzmWZZkxBQ6m2OhFlaOHMR\n"
			+ "xbaKt14KN/DJ3U4F7+557j0cHYfxC79m7eTcEqgbXITHwVHMrqr/nd3Fdps+jp5G\n"
			+ "wqiRTDLpQXQEDSXdReZnjEyAGEbB4kPkvENyrfwNF7InGn8ilFUMCq1OTGqRwr7J\n"
			+ "TXhvefGPmtgFYLYsYhc2nauizjm7A6bvCSNB33PVTahAbPRw3ADw2H5tDbW9+VYt\n"
			+ "foMhagbzUyqHazNwbi3Zv7RuRZxXwpBtFZissdUJRvv5+n6LLz5HWQF5YpUELzLt\n"
			+ "aMJKwd8IoKXqMCaoLl0t2raNpMt+MH5o1TZjKH/LCI3DDNUTkVVKeIZQoylIGsxO\n"
			+ "+diE0Vn//pp137Ar6hWjhGQY8JjNX0KOIJxghl1/sd98Ec+VcdtW6dBQISI5aTTG\n"
			+ "sV+/iOvXHcHjxILT9riYCD0CAwEAAQ==\n" + "-----END PUBLIC KEY-----";

	/**
	 * Hidden constructor to ensure this class is used properly as a singleton.
	 */
	private RsaUtil() {
		// do nothing
	}

	/**
	 * Get a singleton instance of this class.
	 * 
	 * @return Singleton instance of RsaUtil
	 */
	public static synchronized RsaUtil getInstance() {
		if (rsaUtil == null) {
			rsaUtil = new RsaUtil();
		}
		return rsaUtil;
	}

	/**
	 * Encrypt using hardcoded public key 'rsaKey'.
	 * 
	 * @param payload
	 *            data to be encrypted.
	 * @return byte[] encrypted payload.
	 * @throws IOException
	 *             IO Exception
	 * @throws GeneralSecurityException
	 *             General Security Exception
	 */
	public byte[] encrypt(String payload) throws IOException, GeneralSecurityException {
		return encrypt(null, payload);
	}

	/**
	 * Encrypt payload using the provided public key identified by file name.
	 * 
	 * @param publicKeyFileName
	 *            Absolute path to the public key file to use.
	 * @param payload
	 *            data to be encrypted.
	 * @return byte[] encrypted payload.
	 * @throws IOException
	 *             IO Exception
	 * @throws GeneralSecurityException
	 *             General Security Exception
	 */
	public byte[] encrypt(String publicKeyFileName, String payload) throws IOException, GeneralSecurityException {
		publicKey = readPublicKeyFromFile(publicKeyFileName);
		Cipher c = Cipher.getInstance(CIPHER_STRING);
		c.init(Cipher.ENCRYPT_MODE, publicKey);
		return c.doFinal(payload.getBytes());
	}

	/**
	 * Decrypt payload using the provided private key identified by file name.
	 * 
	 * @param privateKeyFileName
	 *            Absolute path to the private key file to use.
	 * @param payload
	 *            Data to be decrypted.
	 * @return byte[] decrypted payload.
	 * @throws IOException
	 *             IO Exception
	 * @throws GeneralSecurityException
	 *             General Security Exception
	 */
	public byte[] decrypt(String privateKeyFileName, byte[] payload) throws IOException, GeneralSecurityException {
		if (privateKey == null) {
			privateKey = readPrivateKeyFromFile(privateKeyFileName);
		}

		Cipher c = Cipher.getInstance(CIPHER_STRING);
		c.init(Cipher.DECRYPT_MODE, privateKey);
		return c.doFinal(payload);
	}

	/**
	 * Get a public key (either hard coded key included, or provided via
	 * filename).
	 * 
	 * @param publicKeyFilename
	 *            Absolute path to public key file. If null, hard- coded public
	 *            key (rsaKey) will be used.
	 * @return PublicKey Object
	 * @throws IOException
	 *             IO Exception
	 * @throws GeneralSecurityException
	 *             General Security Exception
	 */
	private PublicKey readPublicKeyFromFile(String publicKeyFilename) throws IOException, GeneralSecurityException {
		if (publicKey == null) {
			byte[] fileData;

			if (publicKeyFilename == null) {
				fileData = scrubKey(rsaKey);
			} else {
				fileData = readFile(publicKeyFilename);
			}

			KeySpec keySpec = new X509EncodedKeySpec(fileData);
			KeyFactory fac = KeyFactory.getInstance(CIPHER_INSTANCE);
			publicKey = fac.generatePublic(keySpec);
		}
		return publicKey;
	}

	/**
	 * Read the private key from file identified. The private key (openssl) may
	 * need to be converted so Java can read it. this can be done using the
	 * following on the command line: openssl pkcs8 -topk8 -nocrypt -in <rsakey>
	 * -out <rsakey>_pcks8
	 * 
	 * @param privateKeyFilename
	 *            absolute path to private key file.
	 * @return PrivateKey object
	 * @throws IOException
	 *             IO Exception
	 * @throws GeneralSecurityException
	 *             General Security Exception
	 */
	private PrivateKey readPrivateKeyFromFile(String privateKeyFilename) throws IOException, GeneralSecurityException {
		if (privateKey == null) {
			byte[] fileData = readFile(privateKeyFilename);
			KeySpec keySpec = new PKCS8EncodedKeySpec(fileData);
			KeyFactory fac = KeyFactory.getInstance(CIPHER_INSTANCE);
			privateKey = fac.generatePrivate(keySpec);
		}
		return privateKey;
	}

	/**
	 * Get KeySpec from file.
	 * 
	 * @param filename
	 *            RSA Public key file.
	 * @return byte[] File data
	 * @throws IOException
	 *             IO Exception, failure to read/open file.
	 */
	private byte[] readFile(String filename) throws IOException {
		byte[] fileBytes = Files.readAllBytes(Paths.get(filename));
		String fileContents = new String(fileBytes, Charset.forName("UTF-8"));

		return scrubKey(fileContents.trim());
	}

	/**
	 * Scrub the key so that it conforms to the expected format.
	 * 
	 * @param key
	 *            String rsa key to scrub.
	 * @return byte[] scrubbed key as base 64 encoded byte array.
	 */
	private byte[] scrubKey(String key) {
		key = key.replaceAll("(-.+[BE].+-)", "");
		return DatatypeConverter.parseBase64Binary(key);
	}

} // end RsaUtil class
