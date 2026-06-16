package com.orangehrm.steps;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.orangehrm.config.CredentialsProvider;
import com.orangehrm.pages.LoginPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for the Login feature.
 *
 * <p>The {@link ScenarioContext} is injected by PicoContainer, giving these
 * steps the same driver created by the hooks.
 */
public class LoginSteps {

    private final WebDriver driver;
    private final CredentialsProvider credentials = new CredentialsProvider();
    private LoginPage loginPage;

    public LoginSteps(ScenarioContext context) {
        this.driver = context.getDriverFactory().getDriver();
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        loginPage = new LoginPage(driver);
    }

    @When("I log in with valid credentials")
    public void i_log_in_with_valid_credentials() {
        loginPage.login(credentials.getUsername(), credentials.getPassword());
    }

    @Then("I should see the dashboard")
    public void i_should_see_the_dashboard() {
        Assert.assertTrue(
                driver.getCurrentUrl().contains("dashboard"),
                "Expected to land on the dashboard after login, but URL was: "
                        + driver.getCurrentUrl());
    }
}