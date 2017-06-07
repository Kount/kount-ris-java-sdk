package com.kount.ris.khash;

import static org.junit.Assert.*;

import org.junit.Test;

import com.kount.ris.Inquiry;
import com.kount.ris.util.payment.CarteBleuePayment;
import com.kount.ris.util.payment.SkrillMoneybookersPayment;
import com.kount.ris.util.payment.TokenPayment;

public class PaymentTypesKhashingTest {

	@Test
	public void testTokenKhashing() {
		Inquiry inq = new Inquiry();
		
		inq.setPayment(new TokenPayment("6011476613608633"));
		
		assertEquals("601147IF86FKXJTM5K8Z", inq.getParams().get("PTOK"));
		assertEquals("TOKEN", inq.getParams().get("PTYP"));
		assertEquals("KHASH", inq.getParams().get("PENC"));
		
		inq.setPayment(new TokenPayment("1A2B3C6613608633"));
		
		assertEquals("1A2B3C6SYWXNDI5GN77V", inq.getParams().get("PTOK"));
		assertEquals("TOKEN", inq.getParams().get("PTYP"));
		assertEquals("KHASH", inq.getParams().get("PENC"));
	}

	@Test
	public void testCarteBleueKhashing() {
		Inquiry inq = new Inquiry();
		inq.setPayment(new CarteBleuePayment("AABBCC661360DDD"));
		
		assertEquals("AABBCCG297U47WC6J0BC", inq.getParams().get("PTOK"));
		assertEquals("CARTE_BLEUE", inq.getParams().get("PTYP"));
		assertEquals("KHASH", inq.getParams().get("PENC"));
	}
	
	@Test
	public void testSkrillKhashing() {
		Inquiry inq = new Inquiry();
		inq.setPayment(new SkrillMoneybookersPayment("XYZ123661360SKMB"));
		
		assertEquals("XYZ1230L2VYV3P815Q2I", inq.getParams().get("PTOK"));
		assertEquals("SKRILL", inq.getParams().get("PTYP"));
		assertEquals("KHASH", inq.getParams().get("PENC"));
	}
}
