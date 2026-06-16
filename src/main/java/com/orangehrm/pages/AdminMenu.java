package com.orangehrm.pages;

import org.openqa.selenium.WebDriver;

/**
 * Page component for the Admin module's top-bar navigation.
 *
 * <p>This sub-navigation belongs to the Admin module specifically (its tabs
 * differ from those of PIM, Leave, etc.), so it is modelled as a module-scoped
 * component rather than a global one. Interaction mechanics are inherited from
 * {@link BasePage}.
 */
public class AdminMenu extends BasePage {

    public AdminMenu(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the Job Titles screen via the Job dropdown.
     *
     * <p>Clicking the Job tab opens the dropdown; the subsequent waited click
     * resolves once the Job Titles link is present and clickable, so no
     * intermediate wait is required.
     */
    public void goToJobTitles() {
        click("adminMenu.job");
        click("adminMenu.jobTitles");
    }
}
