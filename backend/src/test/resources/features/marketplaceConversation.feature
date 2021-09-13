Feature: UCM8 Contacting Other Marketplace Users

  Background:
    Given I am logged in as a user
    And The marketplace card with id 1 exists who belongs to the user with first name "Hayley" and last name "Krippner"

  Scenario: AC3: Sending a message results in a new item on the recipient’s feed on their home page.  The sender’s name is included together with the relevant card title.
    Given There exists a conversation with instigator "Jim James" with id 1, recipient "John Jones" with id 2, and marketplace card with title "Flying Fish".
    When The recipient with id 2 tries to retrieve their conversations.
    Then I receive a 200 response and a conversation with instigator "Jim James", recipient "John Jones", and marketplace card "Flying Fish" is returned in a list.

  Scenario: AC3 - Sending a message in a non-existing conversation results in a new conversation and a new item in the receiver’s version
    Given I have not contacted this user about this card with id 1 before
    When I send a message with the content of "Hi, can I buy your baked goods please?" about this marketplace card with id 1
    Then A conversation which contains this message is created

  Scenario: AC4: The recipient can expand the message to see the full message text.
    Given There exists a conversation with instigator "Jim James" with id 1, recipient "John Jones" with id 2, and marketplace card with title "Flying Fish".
    When The recipient with id 2 tries to retrieve the messages in their conversation.
    Then A 200 response is received containing an ordered list of full messages.

  Scenario: AC5 - Sending a message in an existing conversation results in a new item in the receiver’s version of this conversation
    And I have contacted this user about this card before in the conversation with id 1
    When I send a message with the content of "Hello, yes you can" about this marketplace card with id 1 in the existing conversation with id 1
    And I expect the message to be created in this conversation

  Scenario: AC5: The recipient can reply to the message. This leads to a response appearing on the original sender's feed.
    Given There exists a conversation with instigator "Jim James" with id 1, recipient "John Jones" with id 2, and marketplace card with title "Flying Fish".
    When The recipient replies to the conversation with the message "This is a reply"
    And The recipient with id 1 tries to retrieve the messages in their conversation.
    Then A 200 response is received containing the message "This is a reply"
