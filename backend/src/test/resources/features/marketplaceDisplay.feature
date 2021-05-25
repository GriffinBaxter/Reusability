Feature: UCM3 - Marketplace Section Display

  Scenario: AC1 - I only see cards from the Wanted section when wanted is selected
    Given There are three cards in each of the Wanted, For Sale, and Exchange sections.
    When The user attempts to view the "Wanted" section.
    Then Only the "Wanted" section cards are retrieved in the correct order (recently created/renewed first).


  Scenario: AC1 - I only see cards from the Exchange section when wanted is selected
    Given There are three cards in each of the Wanted, For Sale, and Exchange sections.
    When The user attempts to view the "Exchange" section.
    Then Only the "Exchange" section cards are retrieved in the correct order (recently created/renewed first).


  Scenario: AC1 - I only see cards from the For Sale section when wanted is selected
    Given There are three cards in each of the Wanted, For Sale, and Exchange sections.
    When The user attempts to view the "For Sale" section.
    Then Only the "For Sale" section cards are retrieved in the correct order (recently created/renewed first).

  Scenario: AC2 & AC4 - I can view the full card content of a given card including the title, creator, section, end of display period, description, created date, and keywords.
    Given A card with ID 1 exists in the database.
    When The user attempts to retrieve the details for the card with ID 1.
    Then The card with ID 1 is retrieved.

  Scenario: AC3 - I can order cards in ascending order by title
    Given There are three cards with titles "Apple", "Pear", "Banana".
    When The user attempts to order the cards by title in "ascending" order.
    Then The retrieved cards are ordered by title in "ascending" order.

  Scenario: AC3 - I can order cards in descending order by title
    Given There are three cards with titles "Apple", "Pear", "Banana".
    When The user attempts to order the cards by title in "descending" order.
    Then The retrieved cards are ordered by title in "descending" order.

  Scenario: AC3 - I can order cards in ascending order by location
    Given There are three cards and the location of the creator associated with them are "Ilam", "Christchurch", "Trentham" "Upper Hutt", "Addington", "Christchurch".
    When The user attempts to order the cards by location in "ascending" order.
    Then The retrieved cards are ordered by location in "ascending" order.

  Scenario: AC3 - I can order cards in descending order by location
    Given There are three cards and the location of the creator associated with them are "Ilam", "Christchurch", "Trentham" "Upper Hutt", "Addington", "Christchurch".
    When The user attempts to order the cards by location in "descending" order.
    Then The retrieved cards are ordered by location in "descending" order.