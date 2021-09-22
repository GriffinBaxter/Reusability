Feature: U40 Withdraw sales listing

  Scenario: AC1 - I can delete a listing as a business admin
    Given The "cool business" business has id 3 and products and inventory items
    And The business with 3 and I have a listing with id 3
    When As an admin I delete the listing with id 3
    Then An OK response is returned

  Scenario: AC4 - I can delete a listing as a GAA
    Given The "cool business" business has id 3 and products and inventory items
    And The business with 3 and I have a listing with id 3
    When As an GAA I delete the listing with id 3
    Then An OK response is returned

  Scenario: AC4 - I can delete a listing as a DGAA
    Given The "cool business" business has id 3 and products and inventory items
    And The business with 3 and I have a listing with id 3
    When As an DGAA I delete the listing with id 3
    Then An OK response is returned