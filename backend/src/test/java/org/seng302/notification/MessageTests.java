package org.seng302.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.*;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Message test class
 */
class MessageTests {

    private User user;

    private Conversation conversation;

    @BeforeEach
    void setup() throws
            IllegalAddressArgumentException,
            IllegalUserArgumentException,
            IllegalMarketplaceCardArgumentException
    {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        user = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        User anotherUser = new User("Another",
                "User",
                "Middle",
                "AU",
                "bio",
                "anotheruser@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(2);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(1);
        
        conversation = new Conversation(anotherUser, user, marketplaceCard);
        conversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
    }

    /**
     * Tests that a message can be created with valid content.
     */
    @Test
    void TestValidMessageContent() throws IllegalMessageContentException {
        String content = "Hello!";
        Message message = new Message(conversation, user, content);
        assertEquals(content, message.getContent());
    }

    /**
     * Tests that a message can be created with valid content (minimum acceptable characters, 1).
     */
    @Test
    void TestValidMessageContentMinimum() throws IllegalMessageContentException {
        String content = "A";
        Message message = new Message(conversation, user, content);
        assertEquals(content, message.getContent());
    }

    /**
     * Tests that a message can be created with valid content (maximum acceptable characters, 300).
     */
    @Test
    void TestValidMessageContentMaximum() throws IllegalMessageContentException {
        String content = "A".repeat(300);
        Message message = new Message(conversation, user, content);
        assertEquals(content, message.getContent());
    }

    /**
     * Tests that a message cannot be created with invalid content (too short, 0 characters).
     */
    @Test
    void TestInvalidMessageContentTooShort() {
        String content = "";
        assertThrows(IllegalMessageContentException.class, () -> new Message(conversation, user, content));
    }

    /**
     * Tests that a message cannot be created with invalid content (too long, 301 characters).
     */
    @Test
    void TestInvalidMessageContentTooLong() {
        String content = "A".repeat(301);
        assertThrows(IllegalMessageContentException.class, () -> new Message(conversation, user, content));
    }
}
