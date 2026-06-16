Feature: Login
  As a user of OrangeHRM
  I want to log in with valid credentials
  So that I can access the dashboard

  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I log in with valid credentials
    Then I should see the dashboard