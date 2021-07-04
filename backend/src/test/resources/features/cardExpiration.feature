Feature: UCM4 - Card Expiry

  Background:
    Given I am logged in and have already created a card.

  Scenario: AC3 - I can extend the card for a further period.
    When I extend the display period of the card.
    Then I receive a 200 response and the display period end has been extended by one week.

