package com.kount.ris.util;

import com.kount.ris.Inquiry;
import com.kount.ris.util.payment.CardPayment;
import com.kount.ris.util.payment.Payment;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.UUID;

public final class Utilities {

	private Utilities() {}
	
	private static final Address BILLING_ADDRESS = 
			new Address("1234 North B2A1 Tree Lane South", null, "Albuquerque", "NM", "87101", "US");
	
	private static final Address SHIPPING_ADDRESS = 
			new Address("567 West S2A1 Court North", null, "Gnome", "AK", "99762", "US");
	
	public static Inquiry defaultInquiry(String sessionId, long merchantId) throws UnsupportedEncodingException {
		Inquiry inquiry = new Inquiry();
		// the value here gets khashed automatically by the setter method request.setPayment(p)
        Payment payment = new CardPayment("0007380568572514");

        String uniqueId = sessionId.substring(0, 20);
        String orderId = sessionId.substring(0, 10);

        CartItem cartItem0 = new CartItem("SPORTING_GOODS", "SG999999", "3000 CANDLEPOWER PLASMA FLASHLIGHT", 2, 68990);

        inquiry
                .setMode(InquiryMode.INITIAL_INQUIRY)
                .setUniqueCustomerId(uniqueId)
                .setName("SdkTestFirstName SdkTestLastName").setEmail("sdkTest@kountsdktestdomain.com")
                .setShippingName("SdkShipToFN SdkShipToLN")
                .setBillingAddress(BILLING_ADDRESS).setBillingPhoneNumber("555-867-5309")
                .setWebsite("DEFAULT")
                .setCurrency("USD").setTotal(123456).setCash(4444)
                .setShippingAddress(SHIPPING_ADDRESS)
                .setShippingEmail("sdkTestShipToEmail@kountsdktestdomain.com")
                .setShippingPhoneNumber("555-777-1212")
                .setIpAddress("131.206.45.21")
                .setCart(Collections.singletonList(cartItem0))
                .setMerchantId(merchantId)
                .setPayment(payment)
                .setSessionId(sessionId)
                .setOrderNumber(orderId)
                .setMerchantAcknowledgment(MerchantAcknowledgment.YES)
                .setAuthorizationStatus(AuthorizationStatus.APPROVED)
                .setAvsZipReply(BankcardReply.MATCH)
                .setAvsAddressReply(BankcardReply.MATCH)
                .setCvvReply(BankcardReply.MATCH);

        return inquiry;
    }



    public static String generateUniqueId() {
		return UUID.randomUUID().toString().substring(0, 32); // uuid returns 36-character string
	}
}
