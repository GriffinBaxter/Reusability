Feature: UCM6 - Keyword Management - Deletion

  Scenario: AC6 - A system admin can delete a keyword
    Given I am a system admin and a keyword at id 1 exists
    When I try to delete the keyword at id 1
    Then The keyword is deleted

  Scenario: AC6 - A user cannot delete a keyword
    Given I am a user and a keyword at id 2 exists
    When I try to delete the keyword at id 2
    Then The keyword is not deleted