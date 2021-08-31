Feature: UCM8 Contacting other marketplace users

  Scenario: AC6 - A user can delete a message.
    Given I have received a message from another user.
    When I try to delete the conversation containing the message.
    Then The message is successfully deleted.