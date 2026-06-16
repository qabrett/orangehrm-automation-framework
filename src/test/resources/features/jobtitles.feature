Feature: Job Titles
  As an HR administrator
  I want to add job titles
  So that they are available when defining employee roles

  Background:
    Given I am logged in

  Scenario: Add a new job title
    Given I am on the Job Titles page
    When I add a new job title
    Then the job title should appear in the list