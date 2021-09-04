Feature: U41 - Sales report

  Scenario: AC2 - I can select a period to be reported on. This might be a single year, month, week or day.
    Given I am logged in as an administrator of an existing business.
    When I select periods of sales reports for a single year, month, week and day.
    Then Sales reports are returned for the aforementioned periods.

  Scenario: AC3 - I can also specify a custom period by selecting when it starts and ends.
    Given I am logged in as an administrator of an existing business.
    When I select a custom period for the sales report to start at "2021-06-11T00:00" and end at "2021-08-25T00:00".
    Then A sales report is returned for the custom period.

  Scenario: AC4 - I can select the granularity of the report.
    Given I am logged in as an administrator of an existing business.
    When I select a granularity for the sales report.
    Then A sales report is returned with the granularity.

  Scenario: AC5 - I can also select finer granularity (e.g. monthly).
    Given I am logged in as an administrator of an existing business.
    When I select a granularity of "Monthly" for the sales report.
    Then A sales report is returned with the "Monthly" granularity.
