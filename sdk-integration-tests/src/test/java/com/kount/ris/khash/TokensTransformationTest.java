package com.kount.ris.khash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

import com.kount.ris.Inquiry;
import com.kount.ris.Request;
import com.kount.ris.util.payment.CardPayment;
import com.kount.ris.util.payment.CheckPayment;

public class TokensTransformationTest {

	@Test
	public void testMaskingCorrectUsage() {
		Request request = new Inquiry();
		
		request.setPaymentMasked(new CardPayment("0007380568572514"));
		
		assertEquals("000738XXXXXX2514", request.getParams().get("PTOK"));
		assertEquals("MASK", request.getParams().get("PENC"));
		assertEquals("2514", request.getParams().get("LAST4"));
	}

	@Test
	public void testMaskingIncorrectParameter() {
		// this test verifies that masking is not applied to payment types different than card payments
		// khashing is used instead
		
		Request request = new Inquiry();
		request.setPaymentMasked(new CheckPayment("0007380568572514"));
		
		assertNotSame("000738XXXXXX2514", request.getParams().get("PTOK"));
		assertEquals("KHASH", request.getParams().get("PENC"));
		assertEquals("2514", request.getParams().get("LAST4"));
	}
}
