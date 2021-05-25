Feature: UCM3 - Marketplace Section Display

  Scenario: AC1 - I only see cards from the Wanted section when wanted is selected
    Given There are three cards in each of the Wanted, For Sale, and Exchange sections.
    When The user attempts to view the "Wanted" section.
    Then Only the "Wanted" section cards are retrieved.
    And The most recently created (or renewed) items appear first.

  Scenario: AC1 - I only see cards from the Exchange section when wanted is selected
    Given There are three cards in each of the Wanted, For Sale, and Exchange sections.
    When The user attempts to view the "Exchange" section.
    Then Only the "Exchange" section cards are retrieved.
    And The most recently created (or renewed) items appear first.

  Scenario: AC1 - I only see cards from the For Sale section when wanted is selected
    Given There are three cards in each of the Wanted, For Sale, and Exchange sections.
    When The user attempts to view the "For Sale" section.
    Then Only the "For Sale" section cards are retrieved.
    And The most recently created (or renewed) items appear first.

  Scenario: AC4 - You can view the full card content of a given card.
    Given A card with ID 1 exists in the database.
    When The user attempts to retrieve the details for the card with ID 1.
    Then The card with ID 1 is retrieved.
