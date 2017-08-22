package com.kount.ris.util;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.github.fzakaria.ascii85.Ascii85;

public class Base85EncodeDecodeTest {

	private static final String PLAIN_TEXT = "This is sample text for testing purposes.";
	private static final String ENCODED_TEXT = "<+oue+DGm>F(&p)Ch4`2AU&;>AoD]4FCfN8Bl7Q+E-62?Df]K2/c";
	
	@Test
	public void testEncode() throws UnsupportedEncodingException {
		String encoded = Ascii85.encode(PLAIN_TEXT.getBytes("UTF-8"));
		assertEquals("Encoded value is not the same as the expected", ENCODED_TEXT, encoded);
	}

	@Test
	public void testDecode() throws UnsupportedEncodingException {
		String decoded = new String(Ascii85.decode(ENCODED_TEXT), "UTF-8");
		assertEquals("Decoded value is not the same as the expected", PLAIN_TEXT, decoded);
	}
}
