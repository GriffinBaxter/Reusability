Feature: UCM8 Contacting Other Marketplace Users

  Background:
    Given I am logged in as a user
    And The marketplace card with id 1 exists who belongs to the user with first name "Hayley" and last name "Krippner"

  Scenario: AC3 - Sending a message in a non-existing conversation results in a new conversation and a new item in the receiver’s version
      Given I have not contacted this user about this card with id 1 before
      When I send a message with the content of "Hi, can I buy your baked goods please?" about this marketplace card with id 1
      Then A conversation which contains this message is created

  Scenario: AC5 - Sending a message in an existing conversation results in a new item in the receiver’s version of this conversation
    And I have contacted this user about this card before in the conversation with id 1
    When I send a message with the content of "Hello, yes you can" about this marketplace card with id 1 in the existing conversation with id 1
    And I expect the message to be created in this conversation