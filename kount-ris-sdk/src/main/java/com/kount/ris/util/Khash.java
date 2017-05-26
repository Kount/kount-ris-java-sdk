package com.kount.ris.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.kount.ris.RisConfigurationConstants;

/**
 * Class for creating Kount RIS KHASH encoding payment tokens.
 *
 * @author Kount &lt;custserv@kount.com&gt;
 * @version $Id$
 * @copyright 2011 Kount Inc. All Rights Reserved.
 */
public final class Khash {

	private static final Logger logger = Logger.getLogger(Khash.class);
	
	private static final String ACCEPTABLE_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private String saltPhrase;
	
	private static Khash INSTANCE = null;
	
	/**
	 * Method to retrieve the singleton Khash instance. It is required that <code>kount.salt.phrase</code>
	 * system variable must be present in order for the instance to be initialized.
	 * 
	 * @return Khash object
	 * 
	 * @throws RuntimeException A {@link RuntimeException} is thrown if <code>kount.ris.salt</code>
	 * 			system variable has not been set.
	 */
	public static Khash getInstance() throws RuntimeException {
		if (INSTANCE == null) {
			INSTANCE = new Khash();
		}
		
		return INSTANCE;
	}
	
	/**
	 * The Khash class default constructor. Uses <code>System.getProperty</code> to initialize
	 * the SALT phrase used in hashing operations.
	 */
	private Khash() throws RuntimeException {
		saltPhrase = System.getProperty(RisConfigurationConstants.PROPERTY_RIS_SALT_PHRASE, null); 
		if (saltPhrase == null || saltPhrase.isEmpty()) {
			logger.error("No SALT phrase configured at 'kount.ris.salt' system variable");
			throw new RuntimeException("No SALT phrase configured");
		}
		
		try {
			String crypted = readCryptedSalt();
			String currentCrypted = sha256(saltPhrase);
			
			if (!crypted.equals(currentCrypted)) {
				logger.error("Configured SALT phrase is incorrect");
				throw new IllegalArgumentException("Configured SALT phrase is incorrect");
			}
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("Could not verify SALT phrase", nsae);
			throw new IllegalStateException("Could not verify SALT phrase");
		}
	}
	
	public static String sha256(String plain) throws NoSuchAlgorithmException {
		MessageDigest d = MessageDigest.getInstance("sha-256");
		byte[] digest = d.digest(plain.getBytes(StandardCharsets.UTF_8));

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(0xff & digest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		
		return hexString.toString();
	}

	private static String readCryptedSalt() {
		try (Scanner scanner = 
			new Scanner(Khash.class.getClassLoader()
				.getResourceAsStream("correct.salt.crypt"), "UTF-8")) {
			
			String line = scanner.nextLine();
			return line;
		}
	}

	/**
	 * Create a Kount hash of a provided payment token. Payment tokens that can
	 * be hashed via this method include: credit card numbers, Paypal payment
	 * IDs, Check numbers, Google Checkout IDs, Bill Me Later IDs, and Green Dot
	 * MoneyPak IDs. Throws a NoSuchAlgorithmException if SHA-1 isn't supported.
	 *
	 * @throws NoSuchAlgorithmException
	 *             When SHA-1 isn't supported
	 * @param token
	 *            Payment token to be hashed
	 * @return hashed string
	 */
	public String hashPaymentToken(String token) throws NoSuchAlgorithmException {
		final int binLength = 6;
		return (null == token) ? "" : token.substring(0, binLength) + this.hash(token);
	}

	/**
	 * Hash a gift card payment token using the Kount hashing algorithm. Throws
	 * a NoSuchAlgorithmException if SHA-1 isn't supported.
	 *
	 * @throws NoSuchAlgorithmException
	 *             When SHA-1 isn't supported.
	 * @param merchantId
	 *            Merchant ID
	 * @param token
	 *            Payment token to be hashed
	 * @return hashed string
	 */
	public String hashGiftCard(int merchantId, String token) throws NoSuchAlgorithmException {
		return merchantId + this.hash(token);
	}

	/**
	 * Hash a payment token using the Kount hash algorithm. Throws a
	 * NoSuchAlgorithmException if SHA-1 isn't supported.
	 *
	 * @throws NoSuchAlgorithmException
	 *             When SHA-1 isn't supported
	 * @param token
	 *            Payment token to be hashed
	 * @return hashed string
	 */
	protected String hash(String token) throws NoSuchAlgorithmException {
		final int loopMax = 28;
		final int hexChunk = 7;
		final int aLength = 36;

		StringBuilder hashed = new StringBuilder();
		String sha1 = Khash.sha1(token + "." + saltPhrase);
		for (int i = 0; i < loopMax; i += 2) {
			hashed.append(ACCEPTABLE_CHARACTERS.charAt(Integer.parseInt(sha1.substring(i, hexChunk + i), 16) % aLength));
		}
		return hashed.toString();
	}

	/**
	 * Perform SHA1 hash on a string.
	 *
	 * @throws NoSuchAlgorithmException
	 *             When SHA-1 isn't supported
	 * @param plainText
	 *            String to be hashed
	 * @return string
	 */
	protected static String sha1(String plainText) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(plainText.getBytes());
		byte[] b = md.digest();
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

}
