Feature: U25 Lost Password

  Background:
    Given A user exists with the email "test@test.com"

  Scenario: AC5 - Change password
    Given A forgot password entity exist for user
    When I try to change the password
    Then The password successfully changes