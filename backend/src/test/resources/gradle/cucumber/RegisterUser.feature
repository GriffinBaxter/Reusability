Feature: U1 - Register a new user

  Scenario: AC1 - Assuming I am not already logged in, the application gives me the ability to create a new account.
    Given My email "test@email.com" doesnt exist in the database.
    When I register an account.
    Then The account is created.

  Scenario: AC2 - If I try to register an account with an email address that is already registered, the system should not create the account but let me know.
    Given I am not logged in.
    When I register with an email that does exist.
    Then I receive a 409 response.

  Scenario: AC4 - If I try to register an account with invalid data.
    Given I am not logged in.
    When I register with invalid data.
    Then I receive a 400 response.
