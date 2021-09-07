package org.seng302.conversation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.exceptions.IllegalMessageContentException;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.MarketplaceConversationMessageRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

/**
 * MarketplaceConversationMessageRepository test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class MarketplaceConversationMessageRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarketplaceConversationMessageRepository marketplaceConversationMessageRepository;

    private User user1;
    private User user2;
    private User anotherUser;
    private MarketplaceCard marketplaceCard1;
    private MarketplaceCard marketplaceCard2;
    private Conversation conversation;
    private Message message;
    private String content;
    private Message message2;
    private String content2;
    private Message message3;
    private String content3;
    private Message message4;
    private String content4;

    /**
     * Sets up data for testing
     */
    @BeforeEach
    public void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        entityManager.persist(address);
        entityManager.flush();

        // Users
        user1 = new User(
                "Jeff",
                "Alex",
                "C",
                "Jeff",
                "bio",
                "Jeff@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        user1 = entityManager.persist(user1);
        entityManager.flush();

        user2 = new User(
                "Jeffrey",
                "Bezo",
                "",
                "Jeff",
                "Pave the way, put your back into it",
                "JeffreyB@example.com",
                LocalDate.of(1964, Month.JANUARY, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        user2 = entityManager.persist(user2);
        entityManager.flush();

        anotherUser = new User(
                "Abby",
                "Wyatt",
                "W",
                "Abby",
                "bio",
                "Abby@example.com",
                LocalDate.of(2020, Month.JANUARY, 1).minusYears(13),
                "1234567555",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        anotherUser = entityManager.persist(anotherUser);
        entityManager.flush();

        // Cards
        marketplaceCard1 = new MarketplaceCard(
                user1.getId(),
                user1,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard1 = entityManager.persist(marketplaceCard1);
        entityManager.flush();

        marketplaceCard2 = new MarketplaceCard(
                user1.getId(),
                user1,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2019, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Money",
                "I'm poor..."
        );
        marketplaceCard2 = entityManager.persist(marketplaceCard2);
        entityManager.flush();

        conversation = new Conversation(user1, user2, marketplaceCard1);
        entityManager.persist(conversation);
        entityManager.flush();
        content = "Here is a message";
        message = new Message(conversation, user1, content);
        entityManager.persist(message);
        entityManager.flush();
        content2 = "Here is another new message";
        message2 = new Message(conversation, user2, content2);
        entityManager.persist(message2);
        entityManager.flush();
        content3 = "Here is a third new message";
        message3 = new Message(conversation, user1, content3);
        entityManager.persist(message3);
        entityManager.flush();
        content4 = "Here is a fourth new message";
        message4 = new Message(conversation, user2, content4);
        entityManager.persist(message4);
        entityManager.flush();
    }

    /**
     * Tests that when a conversation doesn't exist, an empty list of messages is returned.
     */
    @Test
    void findAllByConversationId_OrderByCreatedDesc_ReturnsEmptyList() {
        Conversation conversation = new Conversation(user1, user2, marketplaceCard1);
        entityManager.persist(conversation);
        entityManager.flush();

        // When
        List<Message> messageList = marketplaceConversationMessageRepository.findAllByConversationId_OrderByCreatedDesc(90);

        // Then
        assertThat(messageList).isEmpty();
    }

    /**
     * Tests that when a conversation does exist, a list of messages in the conversation is returned.
     */
    @Test
    void findAllByConversationId_OrderByCreatedDesc_ReturnsMessageList() throws IllegalMessageContentException {

        // When
        List<Message> messageList = marketplaceConversationMessageRepository.findAllByConversationId_OrderByCreatedDesc(conversation.getId());

        // Then
        assertThat(messageList.get(0).getId()).isEqualTo(message4.getId());
        assertThat(messageList.get(0).getContent()).isEqualTo(content4);
        assertThat(messageList.get(0).getConversation()).isEqualTo(conversation);
        assertThat(messageList.get(0).getSender()).isEqualTo(user2);

        assertThat(messageList.get(1).getId()).isEqualTo(message3.getId());
        assertThat(messageList.get(1).getContent()).isEqualTo(content3);
        assertThat(messageList.get(1).getConversation()).isEqualTo(conversation);
        assertThat(messageList.get(1).getSender()).isEqualTo(user1);
    }

    /**
     * Tests that when a conversation does exist, a list of messages in the conversation is returned, ordered by created date descending (the newest first).
     */
    @Test
    void findAllByConversationId_OrderByCreatedDesc_ReturnsOrderedMessageList() throws IllegalMessageContentException {


        // When
        List<Message> messageList = marketplaceConversationMessageRepository.findAllByConversationId_OrderByCreatedDesc(conversation.getId());

        // Then
        assertThat(messageList.get(0).getId()).isEqualTo(message4.getId());
        assertThat(messageList.get(0).getContent()).isEqualTo(content4);
        assertThat(messageList.get(0).getConversation()).isEqualTo(conversation);
        assertThat(messageList.get(0).getSender()).isEqualTo(user2);

        assertThat(messageList.get(1).getId()).isEqualTo(message3.getId());
        assertThat(messageList.get(1).getContent()).isEqualTo(content3);
        assertThat(messageList.get(1).getConversation()).isEqualTo(conversation);
        assertThat(messageList.get(1).getSender()).isEqualTo(user1);

        assertThat(messageList.get(2).getId()).isEqualTo(message2.getId());
        assertThat(messageList.get(2).getContent()).isEqualTo(content2);
        assertThat(messageList.get(2).getConversation()).isEqualTo(conversation);
        assertThat(messageList.get(2).getSender()).isEqualTo(user2);

        assertThat(messageList.get(3).getId()).isEqualTo(message.getId());
        assertThat(messageList.get(3).getContent()).isEqualTo(content);
        assertThat(messageList.get(3).getConversation()).isEqualTo(conversation);
        assertThat(messageList.get(3).getSender()).isEqualTo(user1);
    }


}
