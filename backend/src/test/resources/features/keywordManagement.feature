Feature: UCM6 - Keyword Management

  Scenario: UCM2 - AC5 - Keywords should have a maximum length.
    Given I am already logged in.
    When I try to create a keyword that is 25 characters long.
    Then The keyword is not created.