package com.orangehrm.utils;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

/**
 * Reads a locator repository (.properties file) and resolves logical object
 * names into Selenium {@link By} locators at runtime.
 *
 * <p>Each entry uses the format {@code key = strategy~value}, e.g.
 * {@code login.username = name~username}.
 */
public class ObjectMap {

    private static final Logger log = LogManager.getLogger(ObjectMap.class);
    private static final String SEPARATOR = "~";

    private final Properties properties;

    /**
     * Loads the locator repository from the given file.
     *
     * @param fileName path to the locator .properties file
     * @throws IllegalStateException if the file cannot be read
     */
    public ObjectMap(String fileName) {
        this.properties = PropertiesLoader.load(fileName);
        log.info("Loaded {} locators from {}", properties.size(), fileName);
    }

    /**
     * Resolves a logical object name into a Selenium {@link By} locator.
     *
     * @param objectName key in the repository, e.g. {@code login.username}
     * @return the {@link By} locator for that object
     * @throws IllegalArgumentException if the key is missing, malformed, or uses
     *                                  an unsupported strategy
     */
    public By getLocator(String objectName) {
        String locator = properties.getProperty(objectName);
        if (locator == null) {
            throw new IllegalArgumentException("No locator found for key: " + objectName);
        }

        String[] parts = locator.split(SEPARATOR, 2);
        if (parts.length != 2 || parts[1].isBlank()) {
            throw new IllegalArgumentException(
                    "Malformed locator for key '" + objectName + "': " + locator);
        }

        String strategy = parts[0].trim();
        String value = parts[1].trim();
        log.debug("Resolving {} -> {}:{}", objectName, strategy, value);

        switch (strategy) {
            case "id":              return By.id(value);
            case "name":            return By.name(value);
            case "xpath":           return By.xpath(value);
            case "css":             return By.cssSelector(value);
            case "linkText":        return By.linkText(value);
            case "partialLinkText": return By.partialLinkText(value);
            case "tagName":         return By.tagName(value);
            case "className":       return By.className(value);
            default:
                throw new IllegalArgumentException(
                        "Unsupported locator strategy '" + strategy + "' for key: " + objectName);
        }
    }
}