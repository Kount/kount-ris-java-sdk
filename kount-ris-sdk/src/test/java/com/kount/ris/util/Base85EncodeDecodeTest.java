package com.kount.ris.util;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.github.fzakaria.ascii85.Ascii85;

public class Base85EncodeDecodeTest {

	@Test
	public void testEncode() throws UnsupportedEncodingException {
		
		final String plain = "This is sample text for testing purposes.";
		final String expected = "<+oue+DGm>F(&p)Ch4`2AU&;>AoD]4FCfN8Bl7Q+E-62?Df]K2/c";
		
		String encoded = Ascii85.encode(plain.getBytes("UTF-8"));
		
		assertEquals("Encoded value is not the same as the expected", expected, encoded);
	}

	@Test
	public void testDecode() throws UnsupportedEncodingException {
		
		final String encoded = "<+oue+DGm>F(&p)Ch4`2AU&;>AoD]4FCfN8Bl7Q+E-62?Df]K2/c";
		final String expected = "This is sample text for testing purposes.";
		
		String decoded = new String(Ascii85.decode(encoded), "UTF-8");
		
		assertEquals("Decoded value is not the same as the expected", expected, decoded);
	}
}
