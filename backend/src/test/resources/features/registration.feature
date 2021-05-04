Feature: U1 - Register a new user

  Scenario: AC1 - Assuming I am not already logged in, the application gives me the ability to create a new account.
    Given My email "test@email.com" doesnt exist in the database.
    When I register an account with email "test@email.com".
    Then The account is created with email "test@email.com".

  Scenario: AC2 - If I try to register an account with an email address that is already registered, the system should not create the account but let me know.
    Given The email "test@email.com" already exists in the database.
    When I try to register with existing email "test@email.com".
    Then I receive a 409 response.

  Scenario: AC4 - If I try to register an account with invalid data.
    Given My email "test@email.com" doesnt exist in the database.
    When I try to register with invalid data and email "test@email.com".
    Then I receive a 400 response.
