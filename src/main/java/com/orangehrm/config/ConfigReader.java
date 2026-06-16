package com.orangehrm.config;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.orangehrm.utils.PropertyReader;

/**
 * Loads environment configuration for the active environment and exposes
 * it through typed accessors.
 *
 * <p>The environment is selected via the {@code env} system property
 * (e.g. {@code mvn test -Denv=local}) and defaults to {@code demo}.
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final String DEFAULT_ENV = "demo";

    private final PropertyReader properties;

    public ConfigReader() {
        String env = System.getProperty("env", DEFAULT_ENV);
        String configPath = "config/" + env + ".properties";
        log.info("Loading configuration for environment '{}' from {}", env, configPath);
        this.properties = new PropertyReader(configPath);
    }

    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public String getBrowser() {
        return properties.getProperty("browser");
    }

    public Duration getExplicitWait() {
        return Duration.ofSeconds(getRequiredInt("explicit.wait.seconds"));
    }

    public int getWindowWidth() {
        return getRequiredInt("browser.window.width");
    }

    public int getWindowHeight() {
        return getRequiredInt("browser.window.height");
    }

    /**
     * Reads a required integer property, failing fast with a clear message if it
     * is missing or not a valid number.
     *
     * @param key the property key
     * @return the property value parsed as an int
     * @throws IllegalStateException if the property is absent or non-numeric
     */
    private int getRequiredInt(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalStateException(
                    "Required config property '" + key + "' is missing");
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Config property '" + key + "' must be a number but was '" + value + "'", e);
        }
    }
}