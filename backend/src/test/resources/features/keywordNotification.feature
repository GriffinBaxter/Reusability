Feature: UCM6 - Keyword Management - Admin Notification

  Scenario: AC5 - To prevent abuse, system administrators (U4) are notified, via a news feed on their home page or other suitable mechanism, when a new keyword is added.
    Given A keyword has been created
    When I am logged in as a system administrator
    Then I receive a notification that a keyword has been created