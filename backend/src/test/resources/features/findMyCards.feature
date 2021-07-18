Feature: UCM5 - Find my cards

  Scenario: AC1 - I can find all my own active cards.
    Given I am logged in and have already created cards.
    When I request to find my own active cards.
    Then All of my active cards are shown.

  Scenario: AC2 - I can find all the active cards created by a user.
    Given I am logged in and another user has already created cards.
    When I request to find another user's active cards.
    Then All of the other user's active cards are shown.

  Scenario: AC3 - All card types are displayed together.
    Given I am logged in and have already created cards.
    When I request to find my own active cards.
    Then All of my active cards of multiple types are shown together.
