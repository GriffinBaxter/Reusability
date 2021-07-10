Feature: U23 Business for business

  Background:
    Given I am logged in.

  Scenario: AC2 - Business full name search.
    Given there exists a business with name "Countdown"
    When I enter the full name "Countdown" and search for businesses
    Then I receive the business with name "Countdown"

  Scenario: AC2 - Business partial name search.
    Given there exists a business with name "Countdown"
    When I enter the partial name "Count" and search for businesses
    Then I receive the business with name "Countdown"

  Scenario: AC2 - Business search using OR separating terms.
    Given there exists businesses with names "Countdown" and "New World"
    When I search for businesses using a search query containing "Countdown OR New World"
    Then I receive the businesses with names "Countdown" and "New World"

  Scenario: AC2 - Business search using AND separating terms.
    Given there exists a business with name "New World"
    When I search for businesses using a search query containing "New AND World"
    Then I receive the business with name "New World"

  Scenario: AC2- Business name exact search.
    Given there exists a business with name "Pak'nSave"
    When I search for businesses using a search query containing "\"Pak'nSave\""
    Then I receive the business with name "Pak'nSave"

  Scenario: AC4 - Business search by business type.
    Given there exists a business with business type "RETAIL_TRADE"
    When I search for businesses with business type "RETAIL_TRADE"
    Then I receive the business with business type "RETAIL_TRADE"

  Scenario: AC4 - Business search by business name and business type.
    Given there exists a business with name "New World" and business type "RETAIL_TRADE"
    When I search for businesses with name "New World" and business type "RETAIL_TRADE"
    Then I receive the business with name "New World" and business type "RETAIL_TRADE"