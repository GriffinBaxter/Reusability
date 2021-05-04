Feature: U1 - Log in

  Scenario: Valid login details.
    Given The user's details exist in the database, with email of "email@email.com" and password of "Password123!"
    When The user supplies an email "email@email.com" and password "Password123!" which matches the details in the database
    Then They should be logged in

  Scenario: User email does not exist.
    Given The user is not existing in the database, i.e.  the email of "email@email.com" does not exist
    When The user enters an email of "email@email.com" that is not registered
    And the password "Password123!" is supplied
    Then They should not be logged in and an error message stating the email or password is incorrect is displayed

  Scenario: Incorrect password entered
    Given The user's details exist in the database, with email of "email@email.com" and password of "Password123!"
    When The user enters a registered email, "email@email.com"
    And An incorrect password is supplied, "password"
    Then An error message stating the email or password is incorrect is displayed

  Scenario: No email entered
    Given No email is entered in the login page
    When The user attempts to login with no email and the password "Password123!"
    Then An error message stating the email or password is incorrect is displayed

  Scenario: No password entered
    Given No password is entered in the login page and a registered email of "email@email.com" is provided
    When The user attempts to login, and a registered email of "email@email.com" is provided
    Then An error message stating the email or password is incorrect is displayed


