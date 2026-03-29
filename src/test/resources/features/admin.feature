Feature: Admin User Management

  Background:
    Given the user is on the OrangeHRM login page
    When the user enters username "Admin" and password "admin123"
    And the user clicks the login button

  Scenario: Search a system user by username
    Given the admin navigates to the Admin page
    When the admin searches by username "Admin"
    Then the search results should contain a user with username "Admin"

  Scenario: Mandatory fields are enforced when adding a user
    Given the admin navigates to the Admin page
    When the admin clicks the Add button
    And the admin saves the user form without filling any fields
    Then required field error messages should be displayed
