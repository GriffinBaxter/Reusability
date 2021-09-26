Feature: U25 Lost Password

  Background:
    Given The user's details exist in the database, with email of "test@test.com" and password of "Password123!"

  Scenario: AC2 - A user is locked out of their account for 1 hour after three failed login attempts
    Given The user has tried unsuccessfully to login 2 times
    When They try to login with the incorrect password "incorrectPassword"
    Then Their account is locked for 1 hour

  Scenario: AC2 - A user account is unlocked after the 1 hour lock duration
    Given Their account is locked
    When The user supplies an email "test@test.com" and password "Password123!" which matches the details in the database
    Then They should be logged in

  Scenario: AC5 - Change password
    Given A user exists with the email "test@test.com"
    Given A forgot password entity exist for user
    When I try to change the password
    Then The password successfully changes