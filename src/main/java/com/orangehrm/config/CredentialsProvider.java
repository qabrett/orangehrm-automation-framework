package com.orangehrm.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Supplies OrangeHRM login credentials.
 *
 * <p>Credentials are read from environment variables so they are never
 * committed to source control and can be injected by CI (e.g. Jenkins
 * credentials). If the variables are unset, this falls back to OrangeHRM's
 * PUBLIC demo credentials so the project runs out-of-the-box.
 */
public class CredentialsProvider {

    private static final Logger log = LogManager.getLogger(CredentialsProvider.class);

    // OrangeHRM's demo credentials are PUBLIC (shown on the login page).
    // In a real AUT these fallbacks would be removed and an unset variable
    // would fail fast — credentials would never be defaulted in code.
    private static final String DEFAULT_USERNAME = "Admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    public String getUsername() {
        return readOrDefault("ORANGEHRM_USERNAME", DEFAULT_USERNAME);
    }

    public String getPassword() {
        return readOrDefault("ORANGEHRM_PASSWORD", DEFAULT_PASSWORD);
    }

    private String readOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        if (value != null && !value.isBlank()) {
            return value;
        }
        log.warn("{} not set; using public demo credential fallback", envVar);
        return defaultValue;
    }
}
