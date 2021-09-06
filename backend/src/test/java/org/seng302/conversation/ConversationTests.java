package org.seng302.conversation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * Test class for the Conversation entity.
 */
class ConversationTests {

    private Conversation conversation;

    @BeforeEach
    void setup() throws IllegalAddressArgumentException, IllegalUserArgumentException, IllegalMarketplaceCardArgumentException {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        User sender = new User(
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
                Role.USER
        );
        sender.setId(1);

        User receiver = new User("Another",
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
                Role.USER
        );
        receiver.setId(2);

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                receiver.getId(),
                receiver,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(1);

        conversation = new Conversation(
                sender,
                receiver,
                marketplaceCard
        );
        conversation.setId(1);
    }

    /* -------------------------------------- hasNoMembers Tests -----------------------------------------------------*/

    /**
     * This method tests to see whether the method hasNoMembers returns false when neither the instigator or receiver
     * have deleted the conversation.
     */
    @Test
    void TestHasNoMembersReturnsFalseWhenNeitherInstigatorOrReceiverHasDeletedConversation() {
        conversation.setDeletedByInstigator(false);
        conversation.setDeletedByReceiver(false);
        Assertions.assertFalse(conversation.hasNoMembers());
    }

    /**
     * This method tests to see whether the method hasNoMembers returns false when the instigator has deleted
     * the conversation but the receiver has not deleted the conversation.
     */
    @Test
    void TestHasNoMembersReturnsFalseWhenInstigatorHasDeletedConversationButReceiverHasNotDeletedConversation() {
        conversation.setDeletedByInstigator(true);
        conversation.setDeletedByReceiver(false);
        Assertions.assertFalse(conversation.hasNoMembers());
    }

    /**
     * This method tests to see whether the method hasNoMembers returns false when the instigator has not deleted
     * the conversation but the receiver has deleted the conversation.
     */
    @Test
    void TestHasNoMembersReturnsFalseWhenInstigatorHasNotDeletedConversationButReceiverHasDeletedConversation() {
        conversation.setDeletedByInstigator(false);
        conversation.setDeletedByReceiver(true);
        Assertions.assertFalse(conversation.hasNoMembers());
    }

    /**
     * This method tests to see whether the method hasNoMembers returns true when the instigator and receiver have
     * deleted the conversation.
     */
    @Test
    void TestHasNoMembersReturnsTrueWhenBothInstigatorAndReceiverHaveDeletedConversation() {
        conversation.setDeletedByInstigator(true);
        conversation.setDeletedByReceiver(true);
        Assertions.assertTrue(conversation.hasNoMembers());
    }

}
