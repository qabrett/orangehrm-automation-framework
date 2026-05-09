
package com.orangehrm.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads a locator properties file at construction time and provides
 * {@link By} objects for Selenium element location.
 *
 * <p>Properties entries must follow the format:
 * <pre>
 *   key = locatorType~locatorValue
 * </pre>
 * Supported locator types: id, name, xpath, css, linkText,
 * partialLinkText, tagName, className.
 *
 * <p>If the properties file cannot be loaded, a {@link RuntimeException}
 * is thrown immediately — this is a fatal initialisation failure.
 */
public class ObjectMap {

    private static final Logger log = LogManager.getLogger(ObjectMap.class);
    private static final String SEPARATOR = "~";

    private final Properties properties;

    /**
     * Constructs an ObjectMap and loads the specified properties file.
     *
     * @param filePath path to the locator properties file
     * @throws RuntimeException if the file cannot be found or read
     */
    public ObjectMap(String filePath) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
            log.debug("Loaded locator properties file: {}", filePath);
        } catch (IOException e) {
            String message = "Failed to load locator properties file: " + filePath;
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Returns the Selenium {@link By} locator for the given property key.
     *
     * @param objectName the property key, e.g. {@code login.username}
     * @return the corresponding {@link By} object
     * @throws IllegalArgumentException if the key is not found in the properties
     *                                  file, or the locator type is not supported
     */
    public By getLocator(String objectName) {
        String rawLocator = properties.getProperty(objectName);

        if (rawLocator == null) {
            String message = "Locator key not found in properties file: " + objectName;
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        String[] parts = rawLocator.trim().split(SEPARATOR, 2);

        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            String message = "Malformed locator entry for key '" + objectName
                    + "'. Expected format: type~value. Got: " + rawLocator;
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        String locatorType = parts[0].trim();
        String locatorValue = parts[1].trim();

        log.debug("Resolving locator — key: '{}', type: '{}', value: '{}'",
                objectName, locatorType, locatorValue);

        switch (locatorType) {
            case "id":            return By.id(locatorValue);
            case "name":          return By.name(locatorValue);
            case "xpath":         return By.xpath(locatorValue);
            case "css":           return By.cssSelector(locatorValue);
            case "linkText":      return By.linkText(locatorValue);
            case "partialLinkText": return By.partialLinkText(locatorValue);
            case "tagName":       return By.tagName(locatorValue);
            case "className":     return By.className(locatorValue);
            default:
                String message = "Unsupported locator type '" + locatorType
                        + "' for key: " + objectName;
                log.error(message);
                throw new IllegalArgumentException(message);
        }
    }
}