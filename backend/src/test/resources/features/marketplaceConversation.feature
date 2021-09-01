Feature: UCM8 - Contacting Other Marketplace Users

  Scenario: AC3: Sending a message results in a new item on the recipient’s feed on their home page.  The sender’s name is included together with the relevant card title.
    Given There exists a conversation with instigator "Jim James" with id 1, recipient "John Jones" with id 2, and marketplace card with title "Flying Fish".
    When The recipient with id 2 tries to retrieve their conversations.
    Then I receive a 200 response and a conversation with instigator "Jim James", recipient "John Jones", and marketplace card "Flying Fish" is returned in a list.