package com.kount.ris.util;

import com.github.fzakaria.ascii85.Ascii85;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base85EncodeDecodeTest {

	private static final String PLAIN_TEXT = "This is sample text for testing purposes.";
	private static final String ENCODED_TEXT = "<+oue+DGm>F(&p)Ch4`2AU&;>AoD]4FCfN8Bl7Q+E-62?Df]K2/c";
	
	@Test
	public void testEncode() throws UnsupportedEncodingException {
		String encoded = Ascii85.encode(PLAIN_TEXT.getBytes("UTF-8"));
		assertEquals(ENCODED_TEXT, encoded);
	}

	@Test
	public void testDecode() throws UnsupportedEncodingException {
		String decoded = new String(Ascii85.decode(ENCODED_TEXT), "UTF-8");
		assertEquals(PLAIN_TEXT, decoded);
	}
}
