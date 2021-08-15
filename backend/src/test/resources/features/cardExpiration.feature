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

  Scenario: AC3 - I can delete the card.
    When I delete a card.
    Then  I receive a 200 response and the card has been deleted.

  Scenario: AC3 - If one of my cards has been in the marketplace for the maximum display period then I will be notified.
    Given A card has been in the maximum display period.
    When check function been called.
    Then I will receive a notification for the expired.

  Scenario: AC4 - I take no action within 24 hours of the notification then the card will be deleted automatically and I will be notified that this has happened.
    Given A card has been in the maximum display period.
    And I have no action within 24h for an expired card
    When check function been called.
    Then The card will been deleted
    And I will receive a notification for the deletion.