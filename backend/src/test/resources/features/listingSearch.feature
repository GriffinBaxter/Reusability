Feature: U29 Browse/Search Sale listings

  Background:
    Given I am logged in.

  Scenario: AC2 - All sales listings displayed with no filtering applied.
    Given there exists a listing with name "Beans"
    When I search for listings with no filters
    Then I receive the listing with name "Beans"

  Scenario: AC4 - Sale listings are ordered with product name descending ordering applied.
    Given there exists listings with names "Beans" and "Cookies"
    When I search for listings in product name descending order
    Then I receive the listings with names "Cookies" and "Beans" in that order

  Scenario: AC5 - Sale listings are filtered by business type with filter applied.
    Given there exists listings with names "Beans" and "Cookies"
    When I search for listings with businesses of type retail trade
    Then I receive the listing with name "Cookies"

  Scenario: AC6 - Sale listings are searched for by typing part of a product name.
    Given there exists a listing with name "Beans"
    When I search for product names with the query "B"
    Then I receive the listing with name "Beans"

  Scenario: AC7 - Sale listings are searched for by limiting the price range.
    Given there exists listings with names "Beans" and "Cookies"
    When I limit the price range to a minimum of $5.00 and maximum of $15.00
    Then I receive the listing with name "Beans"

  Scenario: AC8 - Sale listings are searched for by typing part of a seller (business) name.
    Given there exists listings with names "Beans" and "Cookies"
    When I search for products for business names with the query "Second"
    Then I receive the listing with name "Cookies"

  Scenario: AC9 - Sale listings are searched for by typing part of the address of a seller (business).
    Given there exists listings with names "Beans" and "Cookies"
    When I search for products for business addresses with the query "Ilam"
    Then I receive the listing with name "Beans"

  Scenario: AC10 - Sale listings are searched for by limiting the closing date range.
    Given there exists listings with names "Beans" and "Cookies"
    When I limit the closing date range to from "2021-01-01T00:00" to "2022-01-01T00:00"
    Then I receive the listing with name "Beans"

  Scenario: BS2 - Sale listings are searched for by barcode.
    Given there exists listings with names "Beans" and "Cookies"
    When I search using the barcode "9400547002634"
    Then I receive the listing with name "Beans"
