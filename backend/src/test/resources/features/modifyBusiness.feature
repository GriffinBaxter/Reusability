Feature: U11 - Modifying Businesses

  Scenario: AC1 - I can modify all attributes of a business as a primary adminsitrator
    Given I am logged in as a primary admin of a business
    When I try to modify all the attributes of a business
    Then The business is modified