package org.seng302.conversation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.MarketplaceConversationRepository;
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
 * MarketplaceConversationRepository test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class MarketplaceConversationRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarketplaceConversationRepository marketplaceConversationRepository;

    private User user1;
    private User user2;
    private User anotherUser;
    private MarketplaceCard marketplaceCard1;
    private MarketplaceCard marketplaceCard2;

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
    }

    /**
     * Tests that when a user is neither an instigator nor a receiver of any conversations, then an empty list is returned.
     */
    @Test
    void findAllByInstigatorIdOrReceiverId_ReturnsEmptyList() {
        Conversation conversation = new Conversation(user1, user2, marketplaceCard1);
        conversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
        entityManager.persist(conversation);
        entityManager.flush();

        // When
        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(anotherUser.getId(), false, anotherUser.getId(), false);

        // Then
        Assertions.assertTrue(conversationList.isEmpty());
    }

    /**
     * Tests that when one user is a receiver of a conversation, then one conversation
     * is returned when searching for conversations where they are the instigator or receiver.
     */
    @Test
    void findAllByInstigatorIdOrReceiverId_ReturnsOneWhereUserIsReceiver() {
        Conversation expectedConversation = new Conversation(user1, user2, marketplaceCard1);
        expectedConversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
        expectedConversation = entityManager.persist(expectedConversation);
        entityManager.flush();
        Conversation otherConversation = new Conversation(user1, anotherUser, marketplaceCard2);
        otherConversation.setCreated(LocalDateTime.of(2021, 6, 2, 0, 0));
        entityManager.persist(otherConversation);
        entityManager.flush();

        // When
        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(user2.getId(), false, user2.getId(), false);

        // Then
        Assertions.assertEquals(1, conversationList.size());
        Assertions.assertEquals(expectedConversation.getId(), conversationList.get(0).getId());
        Assertions.assertEquals(user1, conversationList.get(0).getInstigator());
        Assertions.assertEquals(user2, conversationList.get(0).getReceiver());
        Assertions.assertEquals(marketplaceCard1, conversationList.get(0).getMarketplaceCard());
    }

    /**
     * Tests that when one user is an instigator of a conversation, then one conversation
     * is returned when searching for conversations where they are the instigator or receiver.
     */
    @Test
    void findAllByInstigatorIdOrReceiverId_ReturnsOneWhereUserIsInstigator() {
        Conversation expectedConversation = new Conversation(user1, user2, marketplaceCard1);
        expectedConversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
        expectedConversation = entityManager.persist(expectedConversation);
        entityManager.flush();
        Conversation otherConversation = new Conversation(user2, anotherUser, marketplaceCard2);
        otherConversation.setCreated(LocalDateTime.of(2021, 6, 2, 0, 0));
        entityManager.persist(otherConversation);
        entityManager.flush();

        // When
        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(user1.getId(), false, user1.getId(), false);

        // Then
        Assertions.assertEquals(1, conversationList.size());
        Assertions.assertEquals(expectedConversation.getId(), conversationList.get(0).getId());
        Assertions.assertEquals(user1, conversationList.get(0).getInstigator());
        Assertions.assertEquals(user2, conversationList.get(0).getReceiver());
        Assertions.assertEquals(marketplaceCard1, conversationList.get(0).getMarketplaceCard());
    }

    /**
     * Tests that when one user is an instigator of one conversation and a receiver of another, then two conversations
     * are returned when searching for conversations where they are the instigator or receiver.
     * Also tests that the list received is ordered by created descending.
     */
    @Test
    void findAllByInstigatorIdOrReceiverId_ReturnsTwoWhereUserIsInstigatorForOneConversationAndReceiverForOneConversation_OrderedByCreatedDescending() {
        Conversation expectedConversation = new Conversation(user1, user2, marketplaceCard1);
        expectedConversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
        expectedConversation = entityManager.persist(expectedConversation);
        entityManager.flush();
        Conversation otherExpectedConversation = new Conversation(anotherUser, user1, marketplaceCard2);
        otherExpectedConversation.setCreated(LocalDateTime.of(2021, 6, 2, 0, 0));
        otherExpectedConversation = entityManager.persist(otherExpectedConversation);
        entityManager.flush();
        Conversation otherConversation = new Conversation(anotherUser, user2, marketplaceCard2);
        otherConversation.setCreated(LocalDateTime.of(2021, 6, 3, 0, 0));
        entityManager.persist(otherConversation);
        entityManager.flush();

        // When
        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(user1.getId(), false, user1.getId(), false);

        // Then
        Assertions.assertEquals(2, conversationList.size());
        // Query orders by created descending so otherExpectedConversation should appear first in the list as it was created second
        Assertions.assertTrue(otherExpectedConversation.getCreated().isAfter(expectedConversation.getCreated()));

        Assertions.assertEquals(otherExpectedConversation.getId(), conversationList.get(0).getId());
        Assertions.assertEquals(anotherUser, conversationList.get(0).getInstigator());
        Assertions.assertEquals(user1, conversationList.get(0).getReceiver());
        Assertions.assertEquals(marketplaceCard2, conversationList.get(0).getMarketplaceCard());

        Assertions.assertEquals(expectedConversation.getId(), conversationList.get(1).getId());
        Assertions.assertEquals(user1, conversationList.get(1).getInstigator());
        Assertions.assertEquals(user2, conversationList.get(1).getReceiver());
        Assertions.assertEquals(marketplaceCard1, conversationList.get(1).getMarketplaceCard());
    }

    /**
     * Tests that when one user is an instigator of one conversation, and one user is a receiver of another, then two conversations
     * are returned when searching for conversations where they are the instigator and receiver.
     * Also tests that the list received is ordered by created descending.
     */
    @Test
    void findAllByInstigatorIdOrReceiverId_ReturnsTwoWhereOneUserIsInstigatorForOneConversationAndOneUserIsReceiverOfOneConversation_OrderedByCreatedDescending() {
        Conversation expectedConversation = new Conversation(user1, anotherUser, marketplaceCard1);
        expectedConversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
        expectedConversation = entityManager.persist(expectedConversation);
        entityManager.flush();
        Conversation otherConversation = new Conversation(anotherUser, user1, marketplaceCard2);
        otherConversation.setCreated(LocalDateTime.of(2021, 6, 2, 0, 0));
        entityManager.persist(otherConversation);
        entityManager.flush();
        Conversation otherExpectedConversation = new Conversation(anotherUser, user2, marketplaceCard2);
        otherExpectedConversation.setCreated(LocalDateTime.of(2021, 6, 3, 0, 0));
        otherExpectedConversation = entityManager.persist(otherExpectedConversation);
        entityManager.flush();

        // When
        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(user1.getId(), false, user2.getId(), false);

        // Then
        Assertions.assertEquals(2, conversationList.size());
        // Query orders by created descending so otherExpectedConversation should appear first in the list as it was created second
        Assertions.assertTrue(otherExpectedConversation.getCreated().isAfter(expectedConversation.getCreated()));

        Assertions.assertEquals(otherExpectedConversation.getId(), conversationList.get(0).getId());
        Assertions.assertEquals(anotherUser, conversationList.get(0).getInstigator());
        Assertions.assertEquals(user2, conversationList.get(0).getReceiver());
        Assertions.assertEquals(marketplaceCard2, conversationList.get(0).getMarketplaceCard());

        Assertions.assertEquals(expectedConversation.getId(), conversationList.get(1).getId());
        Assertions.assertEquals(user1, conversationList.get(1).getInstigator());
        Assertions.assertEquals(anotherUser, conversationList.get(1).getReceiver());
        Assertions.assertEquals(marketplaceCard1, conversationList.get(1).getMarketplaceCard());
    }

    /**
     * Tests that when one user is an instigator of a conversation, and one user is a receiver, then one conversation
     * is returned when searching for conversations where they are the instigator and receiver.
     */
    @Test
    void findAllByInstigatorIdOrReceiverId_ReturnsOneWhereOneUserIsInstigatorAndOneUserIsReceiver() {
        Conversation expectedConversation = new Conversation(user1, user2, marketplaceCard1);
        expectedConversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
        expectedConversation = entityManager.persist(expectedConversation);
        entityManager.flush();
        Conversation otherConversation = new Conversation(anotherUser, user1, marketplaceCard2);
        otherConversation.setCreated(LocalDateTime.of(2021, 6, 2, 0, 0));
        entityManager.persist(otherConversation);
        entityManager.flush();

        // When
        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(user1.getId(), false, user2.getId(), false);

        // Then
        Assertions.assertEquals(1, conversationList.size());

        Assertions.assertEquals(expectedConversation.getId(), conversationList.get(0).getId());
        Assertions.assertEquals(user1, conversationList.get(0).getInstigator());
        Assertions.assertEquals(user2, conversationList.get(0).getReceiver());
        Assertions.assertEquals(marketplaceCard1, conversationList.get(0).getMarketplaceCard());
    }

    /**
     * Tests that when one user is an instigator of a conversation, and one user is a receiver, then an empty list is returned when
     * searching for conversations where the instigator is a receiver and receiver is an instigator.
     */
    @Test
    void findAllByInstigatorIdOrReceiverId_ReturnsEmptyListWhereOneUserIsInstigatorAndOneUserIsReceiver() {
        Conversation conversation = new Conversation(user1, user2, marketplaceCard1);
        conversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
        entityManager.persist(conversation);
        entityManager.flush();

        // When
        List<Conversation> conversationList = marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(user2.getId(), false, user1.getId(), false);

        // Then
        Assertions.assertTrue(conversationList.isEmpty());
    }

}
