package com.kount.ris.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestConfiguration {
	
	private TestConfiguration() {
		// private static util methods only
	}
	
	private static final Map<String, String> configProperties = new HashMap<>();
	
	static {
		
	 	Properties properties = new Properties();
		
//	 	try (InputStream inputStream = TestConfiguration.class.getClassLoader().getResourceAsStream("config.properties") ) {
//			properties.load(inputStream);
//		} catch (IOException e) {
//			// no-op.  This is fine if they place all these parameters in System properties instead.
//			// Below we will confirm that we have all required variables.
//		}

		Arrays.asList(
                "Ris.API.Key",
                "Ris.MerchantId",
                "Ris.Url",
                "Payments.Fraud.Api.Key",
                "Payments.Fraud.Client.Id",
                "Payments.Fraud.Api.Endpoint",
                "Payments.Fraud.Auth.Endpoint",
                "Migration.Mode.Enabled"
        ).forEach(k -> {
			String p = properties.getProperty(k);
			if ( p == null || p.trim().isEmpty() ) {
				p = System.getProperty(k);
			}
			if ( p == null || p.trim().isEmpty() ) {
				throw new IllegalStateException("Unable to find configuration property: " + k);
			}
			configProperties.put(k, p);
		});
	}
	
	public static String getRisAPIKey() {
		return configProperties.get("Ris.API.Key");
	}
	
	public static String getMerchantID() {
		return configProperties.get("Ris.MerchantId");
	}
	
	public static String getRisURL() {
		return configProperties.get("Ris.Url");
	}

    public static String getPfApiKey() {
		return configProperties.get("Payments.Fraud.Api.Key");
	}

    public static String getPfClientId() {
		return configProperties.get("Payments.Fraud.Client.Id");
	}

    public static String getPfApiEndpoint() {
		return configProperties.get("Payments.Fraud.Api.Endpoint");
	}

    public static String getPfAuthEndpoint() {
		return configProperties.get("Payments.Fraud.Auth.Endpoint");
	}

    public static String getMigrationModeEnabled() {
		return configProperties.get("Migration.Mode.Enabled");
	}

}
