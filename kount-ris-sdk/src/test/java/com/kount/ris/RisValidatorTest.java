package com.kount.ris;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import com.kount.ris.util.Utilities;
import com.kount.ris.util.ValidationError;

public class RisValidatorTest {

	private static RisValidator validator;

	@BeforeClass
	public static void initialize() {
		validator = new RisValidator();
	}
	
	@Test
	public void testPassingIpv6() throws Exception {
		Inquiry defaultInquiry = Utilities.defaultInquiry(UUID.randomUUID().toString().substring(0, 32), 900420);
				
		defaultInquiry.setIpAddress("2001:0:3238:DFE1:63::FEFB");
		
		List<ValidationError> result = validator.validate(defaultInquiry.params);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testFailingIpv6() throws Exception {
		Inquiry defaultInquiry = Utilities.defaultInquiry(UUID.randomUUID().toString().substring(0, 32), 900420);
		defaultInquiry.setIpAddress("2001:0:3238:mech:63::FEFB");
		
		List<ValidationError> result = validator.validate(defaultInquiry.params);
		assertEquals(1, result.size());
	}
	
	@Test
	public void testPassingIpv4Local() throws Exception {
		Inquiry defaultInquiry = Utilities.defaultInquiry(UUID.randomUUID().toString().substring(0, 32), 900420);
				
		defaultInquiry.setIpAddress("192.168.100.200");
		
		List<ValidationError> result = validator.validate(defaultInquiry.params);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testFailingIpv4Local() throws Exception {
		Inquiry defaultInquiry = Utilities.defaultInquiry(UUID.randomUUID().toString().substring(0, 32), 900420);
		defaultInquiry.setIpAddress("192.1.100.2048");
		
		List<ValidationError> result = validator.validate(defaultInquiry.params);
		assertEquals(1, result.size());
	}
	
	@Test
	public void testPassingIpv4() throws Exception {
		Inquiry defaultInquiry = Utilities.defaultInquiry(UUID.randomUUID().toString().substring(0, 32), 900420);
				
		defaultInquiry.setIpAddress("8.8.8.8");
		
		List<ValidationError> result = validator.validate(defaultInquiry.params);
		assertEquals(0, result.size());
	}
	
	@Test
	public void testFailingIpv4() throws Exception {
		Inquiry defaultInquiry = Utilities.defaultInquiry(UUID.randomUUID().toString().substring(0, 32), 900420);
		defaultInquiry.setIpAddress("8.8.8");
		
		List<ValidationError> result = validator.validate(defaultInquiry.params);
		assertEquals(1, result.size());
	}
}
