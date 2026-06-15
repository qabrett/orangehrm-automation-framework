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
        String seconds = properties.getProperty("explicit.wait.seconds");
        return Duration.ofSeconds(Long.parseLong(seconds));
    }
}