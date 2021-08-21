Feature: U30 - Individual Full Sale Listing

  Background:
    Given I am logged in as a business administrator.

  Scenario: AC1 -  Full details of the individual sale listing are displayed.
    Given I have a listing with quantity 5, price 2.99, closing date "2030-10-10T00:00", and "Test123" in the more-info section.
    When I request to retrieve my listing.
    Then My listing is returned with all fields shown.

  Scenario: AC3 - I can “bookmark" a sale listing. When I “bookmark” the listing, a message is added to my home feed.
    Given I have a listing with quantity 5, price 2.99, closing date "2030-10-10T00:00", and "Test123" in the more-info section.
    And I have bookmarked the listing.
    When I request to retrieve my list of bookmarked listing messages.
    Then A bookmark message for the listing is returned.

  Scenario: AC6 - I can “un-bookmark” this listing. This will decrement the number of “bookmarks” the listing has (AC5) and add an appropriate message to my home feed.
    Given I have a listing with quantity 5, price 2.99, closing date "2030-10-10T00:00", and "Test123" in the more-info section.
    And I have un-bookmarked the listing.
    When I request to retrieve my list of bookmarked listing messages.
    Then An un-bookmark message for the listing is returned.
    And The number of bookmarks the listing has is decremented.
