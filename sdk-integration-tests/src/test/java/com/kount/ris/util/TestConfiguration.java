package com.kount.ris.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestConfiguration {
	
	private TestConfiguration() {
		// private static util methods only
	}
	
	private static Map<String, String> configProperities = new HashMap<>();
	
	static {
		
	 	Properties properties = new Properties();
		
	 	try (InputStream inputStream = TestConfiguration.class.getClassLoader().getResourceAsStream("config.properties") ) {
			properties.load(inputStream);
		} catch (IOException e) {
			// no-op.  This is fine if they place all these parameters in System properties instead.
			// Below we will confirm that we have all required variables.
		}
		
		Arrays.asList("Ris.API.Key", "Ris.MerchantId", "Ris.Url").forEach(k -> {
			String p = properties.getProperty(k);
			if ( p == null || p.trim().isEmpty() ) {
				p = System.getProperty(k);
			}
			if ( p == null || p.trim().isEmpty() ) {
				throw new IllegalStateException("Unable to find configuration property: " + k);
			}
			configProperities.put(k, p);
		});
	}
	
	public static String getRisAPIKey() {
		return configProperities.get("Ris.API.Key");
	}
	
	public static String getMerchantID() {
		return configProperities.get("Ris.MerchantId");	
	}
	
	public static String getRisURL() {
		return configProperities.get("Ris.Url");	
	}
	
}
