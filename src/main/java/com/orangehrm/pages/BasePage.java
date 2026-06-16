package com.orangehrm.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.config.ConfigReader;
import com.orangehrm.utils.ObjectMap;

/**
 * Base class for all page objects. Holds the WebDriver and the shared locator
 * repository, and provides explicit-wait-based interaction methods that page
 * objects build on.
 *
 * <p>The {@link ObjectMap} is static and shared across all pages and threads:
 * locators are read-only after load, so concurrent reads are safe and the file
 * is parsed only once.
 *
 * <p>Read-path methods accept optional varargs that are substituted into
 * dynamic locator templates at resolution time (see {@link ObjectMap}).
 */
public abstract class BasePage {

    private static final Logger log = LogManager.getLogger(BasePage.class);

    /** Shared, read-only locator repository. Safe to share: no mutation after load. */
    private static final ObjectMap objectMap = new ObjectMap("locators.properties");

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        Duration timeout = new ConfigReader().getExplicitWait();
        this.wait = new WebDriverWait(driver, timeout);
    }

    /**
     * Waits for the element identified by the given locator key to be visible.
     *
     * @param locatorKey key in the locator repository, e.g. {@code login.username}
     * @param args       optional runtime values for dynamic locator templates
     * @return the visible WebElement
     */
    protected WebElement waitForElement(String locatorKey, Object... args) {
        By locator = resolve(locatorKey, args);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the element to be clickable, then clicks it.
     */
    protected void click(String locatorKey) {
        By locator = resolve(locatorKey);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        log.debug("Clicked: {}", locatorKey);
    }

    /**
     * Waits for the element to be visible, then types text into it.
     */
    protected void type(String locatorKey, String text) {
        waitForElement(locatorKey).sendKeys(text);
        log.debug("Typed into {}: {}", locatorKey, text);
    }

    /**
     * Waits for the element to be visible, then returns its text.
     */
    protected String getText(String locatorKey) {
        return waitForElement(locatorKey).getText();
    }

    /**
     * Checks whether the element is currently displayed. Returns false rather
     * than throwing if the element is absent.
     *
     * @param locatorKey key in the locator repository
     * @param args       optional runtime values for dynamic locator templates
     */
    protected boolean isDisplayed(String locatorKey, Object... args) {
        try {
            return waitForElement(locatorKey, args).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private By resolve(String locatorKey, Object... args) {
        try {
            return objectMap.getLocator(locatorKey, args);
        } catch (Exception e) {
            throw new IllegalStateException("Could not resolve locator: " + locatorKey, e);
        }
    }
}