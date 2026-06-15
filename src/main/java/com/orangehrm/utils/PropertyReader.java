package com.orangehrm.utils;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reads configuration values (URLs, credentials, browser, timeouts) from a
 * .properties file.
 */
public class PropertyReader {

    private static final Logger log = LogManager.getLogger(PropertyReader.class);

    private final Properties properties;

    /**
     * Loads configuration from the given file.
     *
     * @param propertiesFilePath path to the config .properties file
     * @throws IllegalStateException if the file cannot be read
     */
    public PropertyReader(String propertiesFilePath) {
        this.properties = PropertiesLoader.load(propertiesFilePath);
        log.info("Loaded {} config properties from {}", properties.size(), propertiesFilePath);
    }

    /**
     * Retrieves a configuration value by key.
     *
     * @param propertyName the property key, e.g. {@code base.url}
     * @return the value for that key
     * @throws IllegalArgumentException if the key is missing
     */
    public String getProperty(String propertyName) {
        String value = properties.getProperty(propertyName);
        if (value == null) {
            throw new IllegalArgumentException("No config property found for key: " + propertyName);
        }
        return value;
    }
}