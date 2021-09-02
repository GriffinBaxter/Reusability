Feature: UCM8 Contacting Other Marketplace Users

  Scenario: AC3: Sending a message results in a new item on the recipient’s feed on their home page.  The sender’s name is included together with the relevant card title.
    Given There exists a conversation with instigator "Jim James" with id 1, recipient "John Jones" with id 2, and marketplace card with title "Flying Fish".
    When The recipient with id 2 tries to retrieve their conversations.
    Then I receive a 200 response and a conversation with instigator "Jim James", recipient "John Jones", and marketplace card "Flying Fish" is returned in a list.

  Scenario: AC4: The recipient can expand the message to see the full message text.
    Given There exists a conversation with instigator "Jim James" with id 1, recipient "John Jones" with id 2, and marketplace card with title "Flying Fish".
    When The recipient with id 2 tries to retrieve the messages in their conversation.
    Then A 200 response is received containing an ordered list of full messages.

  Scenario: AC5: The recipient can reply to the message. This leads to a response appearing on the original sender's feed.
    Given There exists a conversation with instigator "Jim James" with id 1, recipient "John Jones" with id 2, and marketplace card with title "Flying Fish".
    When The recipient replies to the conversation with the message "This is a reply"
    And The recipient with id 1 tries to retrieve the messages in their conversation.
    Then A 200 response is received containing the message "This is a reply"
