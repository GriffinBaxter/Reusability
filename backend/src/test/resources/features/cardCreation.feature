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

  Scenario: AC3 - The card has a title.
    When I try to create a card without a title.
    Then The card is not created.

  Scenario: AC5 - One or more keywords/phrases can be added.
    When I create a card with more than one keyword.
    Then The card is successfully created.

