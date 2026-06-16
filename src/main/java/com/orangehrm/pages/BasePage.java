package com.orangehrm.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;

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
 * <p>Interaction methods accept optional varargs that are substituted into
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
     * @param args optional runtime values for dynamic locator templates
     * @return the visible WebElement
     */
    protected WebElement waitForElement(String locatorKey, Object... args) {
        By locator = resolve(locatorKey, args);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the element to be clickable, then clicks it.
     *
     * @param locatorKey key in the locator repository
     * @param args optional runtime values for dynamic locator templates
     */
    protected void click(String locatorKey, Object... args) {
        By locator = resolve(locatorKey, args);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        log.debug("Clicked: {}", locatorKey);
    }

    /**
     * Clicks an element, retrying briefly if the click is intercepted or the
     * element goes stale — both transient symptoms of an in-progress CSS
     * animation (e.g. an opening dropdown). Only these specific transient
     * exceptions are retried; anything else propagates immediately, and the
     * retry window is bounded by the standard explicit wait, so a genuinely
     * missing element still fails loudly rather than being masked.
     *
     * @param locatorKey key in the locator repository
     * @param args       optional runtime values for dynamic locator templates
     */
    protected void clickWhenStable(String locatorKey, Object... args) {
        wait.until(driver -> {
            try {
                By locator = resolve(locatorKey, args);
                driver.findElement(locator).click();
                return true;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                return false;
            }
        });
        log.debug("Clicked (stable): {}", locatorKey);
    }

    /**
     * Waits for the element to be visible, clicks it to ensure focus, selects any
     * existing value, then types the supplied text over it. The click-to-focus and
     * select-all approach (rather than {@link WebElement#clear()}) is required
     * because OrangeHRM's React inputs only register keystrokes when focused and
     * ignore a programmatic clear — so the field reliably ends up containing
     * exactly the supplied text.
     */
    protected void type(String locatorKey, String text) {
        WebElement field = waitForElement(locatorKey);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(text);
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
     * @param args optional runtime values for dynamic locator templates
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