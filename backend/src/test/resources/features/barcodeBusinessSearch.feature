Feature: BS3 - Barcode Business Search

  Background:
    Given I am a logged in business administrator.

  Scenario: AC1 & AC2 - Search for inventory items with barcode
    Given I have a inventory item with a barcode "9300675024235"
    When I request to retrieve listings for my business with barcode "9300675024235"
    Then My inventory item is returned in a list by its self
