package com.orangehrm.pages;

import org.openqa.selenium.WebDriver;

/**
 * Page object for the OrangeHRM Admin &gt; Job &gt; Job Titles screen.
 *
 * <p>Exposes Job Titles actions and state in terms of intent; interaction
 * mechanics (waits, locator resolution) are inherited from {@link BasePage}.
 * Methods report state but never assert — assertions belong to the step
 * definitions.
 */
public class JobTitlesPage extends BasePage {

    public JobTitlesPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Adds a job title with the given name: opens the add form, enters the
     * title, and saves.
     *
     * @param title the job title to create
     */
    public void addJobTitle(String title) {
        click("jobTitles.add");
        type("jobTitles.titleInput", title);
        click("jobTitles.save");
    }

    /**
     * Reports whether a job title with the given name appears in the list.
     *
     * <p>Resolves the dynamic {@code jobTitles.rowByTitle} template with the
     * supplied title and checks whether that row is displayed. Returns state
     * only; the caller decides whether its presence or absence is a pass.
     *
     * @param title the job title to look for
     * @return {@code true} if a matching row is displayed, {@code false} otherwise
     */
    public boolean isJobTitleListed(String title) {
        return isDisplayed("jobTitles.rowByTitle", title);
    }

    /**
     * Edits an existing job title: opens its row's edit form, replaces the title,
     * and saves.
     *
     * @param currentTitle the title of the row to edit
     * @param newTitle     the replacement title
     */
    public void editJobTitle(String currentTitle, String newTitle) {
        click("jobTitles.editIconByTitle", currentTitle);
        type("jobTitles.titleInput", newTitle);
        click("jobTitles.save");
    }

    /**
     * Deletes an existing job title: clicks its row's delete icon and confirms.
     *
     * @param title the title of the row to delete
     */
    public void deleteJobTitle(String title) {
        click("jobTitles.deleteIconByTitle", title);
        click("jobTitles.deleteConfirm");
    }
}
