Feature: U41 - Sales report

  Scenario: AC2 - I can select a period to be reported on. This might be a single year, month, week or day.
    Given I am logged in as an administrator of an existing business.
    When I select periods of sales reports for a single year, month, week and day.
    Then Sales reports are returned for the aforementioned periods.
