Feature: Create a customer

  Background:tesy@sem.com
    Given I have opened the system
    When I set the browser to full screen

    And I wait few seconds

  Scenario: Test Scenario

    When I type "Admin" to the "Login_usernameField"
    Then I type "admin123" to the "Login_pswField"
    And I click on "Login_btn"

    And I wait few seconds

    When I click on "Leave"
    And I wait few seconds

