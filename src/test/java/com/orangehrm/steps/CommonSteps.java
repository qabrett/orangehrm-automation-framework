package com.orangehrm.steps;

import org.openqa.selenium.WebDriver;

import com.orangehrm.config.CredentialsProvider;
import com.orangehrm.pages.LoginPage;

import io.cucumber.java.en.Given;

/**
 * Step definitions for preconditions shared across multiple features.
 *
 * <p>The {@link ScenarioContext} is injected by PicoContainer, giving these
 * steps the same driver created by the hooks. Step text is matched globally by
 * Cucumber, so these steps are reusable from any feature's Background.
 */
public class CommonSteps {

    private final WebDriver driver;
    private final CredentialsProvider credentials = new CredentialsProvider();

    public CommonSteps(ScenarioContext context) {
        this.driver = context.getDriverFactory().getDriver();
    }

    @Given("I am logged in")
    public void i_am_logged_in() {
        new LoginPage(driver).login(credentials.getUsername(), credentials.getPassword());
    }
}
