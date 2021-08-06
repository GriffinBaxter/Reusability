Feature: U30 - Individual Full Sale Listing

  Background:
    Given I am logged in as a business administrator.

  Scenario: AC1 -  Full details of the individual sale listing are displayed.
    Given I have a listing with quantity 5, price 2.99, closing date "2030-10-10T00:00", and "Test123" in the more-info section.
    When I request to retrieve my listing.
    Then My listing is returned with all fields shown.
