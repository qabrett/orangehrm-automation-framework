package com.orangehrm.steps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.orangehrm.pages.AdminMenu;
import com.orangehrm.pages.JobTitlesPage;
import com.orangehrm.pages.SidebarMenu;
import com.orangehrm.utils.TestDataGenerator;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for the Job Titles feature.
 *
 * <p>The {@link ScenarioContext} is injected by PicoContainer, giving these
 * steps the same driver created by the hooks. Generated titles are held as
 * fields because only this step class needs them across its own steps.
 * Lifecycle logging records, at info level, the test data each scenario creates
 * and acts on.
 */
public class JobTitlesSteps {

    private static final Logger log = LogManager.getLogger(JobTitlesSteps.class);

    private final WebDriver driver;
    private final TestDataGenerator data = new TestDataGenerator();

    private String jobTitle;
    private String updatedTitle;

    public JobTitlesSteps(ScenarioContext context) {
        this.driver = context.getDriverFactory().getDriver();
    }

    @Given("I am on the Job Titles page")
    public void i_am_on_the_job_titles_page() {
        new SidebarMenu(driver).goToAdmin();
        new AdminMenu(driver).goToJobTitles();
    }

    @Given("I have added a job title")
    public void i_have_added_a_job_title() {
        jobTitle = data.uniqueJobTitle();
        log.info("Setup: adding job title '{}'", jobTitle);
        new JobTitlesPage(driver).addJobTitle(jobTitle);
    }

    @When("I add a new job title")
    public void i_add_a_new_job_title() {
        jobTitle = data.uniqueJobTitle();
        log.info("Adding job title '{}'", jobTitle);
        new JobTitlesPage(driver).addJobTitle(jobTitle);
    }

    @When("I change the job title")
    public void i_change_the_job_title() {
        updatedTitle = data.uniqueJobTitle();
        log.info("Editing job title '{}' to '{}'", jobTitle, updatedTitle);
        new JobTitlesPage(driver).editJobTitle(jobTitle, updatedTitle);
    }

    @When("I delete the job title")
    public void i_delete_the_job_title() {
        log.info("Deleting job title '{}'", jobTitle);
        new JobTitlesPage(driver).deleteJobTitle(jobTitle);
    }

    @Then("the job title should appear in the list")
    public void the_job_title_should_appear_in_the_list() {
        boolean listed = new JobTitlesPage(driver).isJobTitleListed(jobTitle);
        Assert.assertTrue(listed,
                "Expected job title '" + jobTitle + "' to appear in the list, but it was not found.");
    }

    @Then("the new job title should appear in the list")
    public void the_new_job_title_should_appear_in_the_list() {
        boolean listed = new JobTitlesPage(driver).isJobTitleListed(updatedTitle);
        Assert.assertTrue(listed,
                "Expected updated job title '" + updatedTitle + "' to appear in the list, but it was not found.");
    }

    @Then("the original job title should no longer appear")
    public void the_original_job_title_should_no_longer_appear() {
        boolean listed = new JobTitlesPage(driver).isJobTitleListed(jobTitle);
        Assert.assertFalse(listed,
                "Expected original job title '" + jobTitle + "' to be gone after edit, but it was still listed.");
    }

    @Then("the job title should no longer appear in the list")
    public void the_job_title_should_no_longer_appear_in_the_list() {
        boolean listed = new JobTitlesPage(driver).isJobTitleListed(jobTitle);
        Assert.assertFalse(listed,
                "Expected job title '" + jobTitle + "' to be gone after delete, but it was still listed.");
    }
}