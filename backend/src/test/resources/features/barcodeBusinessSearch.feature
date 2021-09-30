Feature: BS3 - Barcode Business Search

  Background:
    Given I am a logged in business administrator.

  Scenario: AC1 & AC2 - Search for products with barcode
    Given I have a product with a barcode "9415767624207"
    When I request to retrieve products for my business with barcode "9415767624207"
    Then My product is returned in a list by its self

  Scenario: AC1 & AC2 - Search for inventory items with barcode
    Given I have a inventory item with a barcode "9300675024235"
    When I request to retrieve inventory item for my business with barcode "9300675024235"
    Then My inventory item is returned in a list by its self

  Scenario: AC1 & AC2 - Search for listings with barcode
    Given I have a listing with a barcode "9415767624207"
    When I request to retrieve listings for my business with barcode "9415767624207"
    Then My listing is returned in a list by its self