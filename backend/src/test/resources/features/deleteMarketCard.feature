Feature: UCM4 - Card expiry

  Background:
    Given I am logged in as a user.
    And I create a card.

  Scenario: AC1 - I can delete any card that I created but I cannot delete a card created by another user.
    When I try to delete this card.
    Then The card is successfully deleted.

  Scenario: AC2 - A system administrator (GAA) can delete any cards.
    Given I login as a GAA.
    When I try to delete this card.
    Then The card is successfully deleted.

  Scenario: AC2 - A system administrator (DGAA) can delete any cards.
    Given I login as a DGAA.
    When I try to delete this card.
    Then The card is successfully deleted.