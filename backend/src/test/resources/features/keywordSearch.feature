Feature: UCM6 - Keyword management Search

  Scenario: AC2 - The Keywords may be selected from a system-wide list
    Given A list of keywords exist in the system
    When The user tries to get all keywords
    Then All keywords are returned to the user

  Scenario: AC3 - The user can search for keywords
    Given Keywords "search" and "searchTest" and "test" exist in the system
    When The user searches for "search"
    Then Keywords "search" and "searchTest" are returned to the user