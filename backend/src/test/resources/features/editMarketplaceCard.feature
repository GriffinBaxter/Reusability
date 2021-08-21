Feature: UCM9 - Edit Card

  Background:
    Given I am logged in as a user and another user exists

  Scenario: AC1 - I can't edit someone else's card
    Given A card exists with id 1 with a different owner
    When I try to edit a card with id 1
    Then It doesn't let me edit

  Scenario: AC2 - I can update the card
    Given A card with id 2 exists with me as the creator
    When I try to edit a card with id 2
    Then The card is updated

  Scenario: AC4 - A system admin can edit my card
    Given I am a system admin and a card exists with a different owner at id 3
    When I try to edit a card with id 3 as system admin
    Then The card is updated