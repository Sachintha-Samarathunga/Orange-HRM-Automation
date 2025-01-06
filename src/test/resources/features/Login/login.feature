Feature: Login to the system
  As a user
  I should be able to login to the system

  Background:tesy@sem.com
    Given I have opened the system
    When I set the browser to full screen

  @Login
  Scenario: Verify login with valid credentials 2

    When I type "admin" to the "Login_usernameField"
    Then I type "<password>" to the "Login_pswField"
    And I click on "Login_btn"

    And I wait few seconds


