Feature: UCM2 - Card Creation

  Background:
    Given I am logged in.

  Scenario: AC1 - I can create a card to be displayed in the For Sale section.
    When I create a card with the For Sale section selected.
    Then The card is successfully created.

  Scenario: AC1 - I can create a card to be displayed in the Wanted section.
    When I create a card with the Wanted section selected.
    Then The card is successfully created.

  Scenario: AC1 - I can create a card to be displayed in the Exchange section.
    When I create a card with the Exchange section selected.
    Then The card is successfully created.
