package com.orangehrm.pages;

import org.openqa.selenium.WebDriver;

/**
 * Page object for the OrangeHRM login page.
 *
 * <p>Exposes login actions in terms of user intent; all interaction mechanics
 * (waits, locator resolution) are inherited from {@link BasePage}.
 */
public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Logs in with the given credentials.
     *
     * @param username the username to enter
     * @param password the password to enter
     */
    public void login(String username, String password) {
        type("login.username", username);
        type("login.password", password);
        click("login.loginBtn");
    }

    /**
     * Returns the error message shown after a failed login attempt.
     *
     * @return the visible error text (e.g. "Invalid credentials")
     */
    public String getErrorMessage() {
        return getText("login.invalidCredentials");
    }
}
