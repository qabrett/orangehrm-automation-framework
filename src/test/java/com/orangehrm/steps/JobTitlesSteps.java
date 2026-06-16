package com.orangehrm.steps;

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
 * steps the same driver created by the hooks. The generated job title is held
 * as a field because only this step class needs it across its own steps.
 */
public class JobTitlesSteps {

    private final WebDriver driver;
    private final TestDataGenerator data = new TestDataGenerator();

    private String jobTitle;

    public JobTitlesSteps(ScenarioContext context) {
        this.driver = context.getDriverFactory().getDriver();
    }

    @Given("I am on the Job Titles page")
    public void i_am_on_the_job_titles_page() {
        new SidebarMenu(driver).goToAdmin();
        new AdminMenu(driver).goToJobTitles();
    }

    @When("I add a new job title")
    public void i_add_a_new_job_title() {
        jobTitle = data.uniqueJobTitle();
        new JobTitlesPage(driver).addJobTitle(jobTitle);
    }

    @Then("the job title should appear in the list")
    public void the_job_title_should_appear_in_the_list() {
        boolean listed = new JobTitlesPage(driver).isJobTitleListed(jobTitle);
        Assert.assertTrue(listed,
                "Expected job title '" + jobTitle + "' to appear in the list, but it was not found.");
    }
}