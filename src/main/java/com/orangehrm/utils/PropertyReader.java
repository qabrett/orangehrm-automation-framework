package com.orangehrm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class for reading properties from a file.
 */
public class PropertyReader {

    private static final Logger log = LogManager.getLogger(PropertyReader.class);
    private final Properties properties;

    /**
     * Constructs a new PropertyReader object and loads the properties from the
     * specified file.
     *
     * @param propertiesFilePath the name of the properties file to read
     */
    public PropertyReader(String propertiesFilePath) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
            properties.load(fis);
            log.debug("Loaded property file path: {}", propertiesFilePath);
        } catch (IOException e) {
            String message = "Failed to load property file path: " + propertiesFilePath;
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Retrieves the value of the specified property from the properties file.
     *
     * @param propertyName the key to look up in the properties file
     * @return the value of the specified property
     * @throws IllegalArgumentException if the property key is not found
     */
    public String getProperty(String propertyName) {
        String value = properties.getProperty(propertyName);

        if (value == null) {
            throw new IllegalArgumentException("Property key not found: " + propertyName);
        }
        return value;
    }

}

