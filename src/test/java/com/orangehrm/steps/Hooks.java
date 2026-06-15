package com.orangehrm.steps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.orangehrm.base.DriverFactory;
import com.orangehrm.config.ConfigReader;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber lifecycle hooks. Runs before and after every scenario to set up
 * and tear down the browser.
 *
 * <p>The {@link ScenarioContext} is injected by PicoContainer, giving these
 * hooks the same shared driver that the step classes use.
 */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    private final DriverFactory driverFactory;
    private final ConfigReader config = new ConfigReader();

    public Hooks(ScenarioContext context) {
        this.driverFactory = context.getDriverFactory();
    }

    @Before
    public void setUp(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
        driverFactory.createDriver();
        driverFactory.getDriver().get(config.getBaseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            log.warn("Scenario failed: {} - capturing screenshot", scenario.getName());
            WebDriver driver = driverFactory.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
        driverFactory.quitDriver();
        log.info("Finished scenario: {}", scenario.getName());
    }
}
