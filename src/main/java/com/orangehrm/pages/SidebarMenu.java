package com.orangehrm.pages;

import org.openqa.selenium.WebDriver;

/**
 * Page component for the OrangeHRM left sidebar.
 *
 * <p>The sidebar is a persistent navigation region present on every screen, so
 * it is modelled as a shared component rather than a page object. It exposes
 * navigation to top-level modules in terms of intent; interaction mechanics
 * (waits, locator resolution) are inherited from {@link BasePage}.
 */
public class SidebarMenu extends BasePage {

    public SidebarMenu(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the Admin module via the sidebar.
     */
    public void goToAdmin() {
        click("sidebar.admin");
    }
}

