package com.orangehrm.base;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.orangehrm.config.ConfigReader;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Creates and manages WebDriver instances on a per-thread basis.
 *
 * <p>The driver is held in a {@link ThreadLocal} so that parallel test
 * threads each operate on their own isolated browser session. The browser
 * type is read from the active environment configuration.
 */
public class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private final ConfigReader config = new ConfigReader();

    /**
     * Creates a WebDriver for the browser named in configuration and binds
     * it to the current thread.
     *
     * @throws IllegalArgumentException if the configured browser is unsupported
     */
    public void createDriver() {
        String browser = config.getBrowser().toLowerCase();
        log.info("Creating '{}' driver", browser);

        WebDriver webDriver;
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        webDriver.manage().window().maximize();
        driver.set(webDriver);
    }

    /**
     * Returns the WebDriver bound to the current thread.
     *
     * @return the current thread's WebDriver
     * @throws IllegalStateException if no driver has been created for this thread
     */
    public WebDriver getDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            throw new IllegalStateException(
                    "No WebDriver for this thread. Was createDriver() called?");
        }
        return webDriver;
    }

    /**
     * Quits the current thread's WebDriver and removes it from the ThreadLocal.
     */
    public void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
            log.info("Quit driver");
        }
    }
}
