Feature: UCM3 - Marketplace Section Display

  Scenario: AC4 - You can view the full card content of a given card.
    Given A card with ID 1 exists in the database.
    When The user attempts to retrieve the details for the card with ID 1.
    Then The card with ID 1 is retrieved.
    