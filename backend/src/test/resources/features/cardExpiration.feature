Feature: UCM4 - Card Expiry

  Background:
    Given I am logged in as a user and have already created a card.

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

  Scenario: AC3 - I can extend the card for a further period.
    When I extend the display period of the card.
    Then I receive a 200 response and the display period end has been extended by two weeks.