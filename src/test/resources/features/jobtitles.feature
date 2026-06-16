Feature: Job Titles
  As an HR administrator
  I want to manage job titles
  So that they are available when defining employee roles

  Background:
    Given I am logged in

  Scenario: Add a new job title
    Given I am on the Job Titles page
    When I add a new job title
    Then the job title should appear in the list

  Scenario: Edit an existing job title
    Given I am on the Job Titles page
    And I have added a job title
    When I change the job title
    Then the new job title should appear in the list
    And the original job title should no longer appear

  Scenario: Delete a job title
    Given I am on the Job Titles page
    And I have added a job title
    When I delete the job title
    Then the job title should no longer appear in the list