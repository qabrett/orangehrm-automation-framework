package com.orangehrm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {

    private PropertiesLoader() { }   // utility class — never instantiated

    public static Properties load(String fileName) {
        Properties properties = new Properties();
        try (InputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load properties file: " + fileName, e);
        }
    }
}