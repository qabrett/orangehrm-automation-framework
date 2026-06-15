package com.orangehrm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {

    private PropertiesLoader() { }   // utility class — never instantiated

    /**
     * Loads a properties file from a filesystem path.
     * Used for config files that live outside the classpath (e.g. config/demo.properties).
     */
    public static Properties load(String fileName) {
        Properties properties = new Properties();
        try (InputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load properties file: " + fileName, e);
        }
    }

    /**
     * Loads a properties file from the classpath.
     * Used for resources bundled in src/main/resources (e.g. locators.properties).
     */
    public static Properties loadFromClasspath(String resourceName) {
        Properties properties = new Properties();
        try (InputStream is = PropertiesLoader.class.getClassLoader()
                .getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IllegalStateException("Classpath resource not found: " + resourceName);
            }
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load classpath resource: " + resourceName, e);
        }
    }
}