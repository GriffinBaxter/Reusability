package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.view.outgoing.ConversationPayload;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Conversation Entity (for marketplace messages)
 */
@Data
@NoArgsConstructor
@Entity
public class Conversation {

    /**
     * ID field for the Conversation entity
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id", nullable = false)
    private int id;

    /**
     * User that started the conversation (from a card), lazily fetched
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instigator_id", nullable = false)
    private User instigator;

    /**
     * User that created the card, lazily fetched
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    /**
     * The marketplace card of the conversation
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marketplace_card_id", nullable = false)
    private MarketplaceCard marketplaceCard;

    /**
     * A boolean value used to keep track of whether the instigator has deleted/left the conversation.
     * true if they have, false if they have not.
     */
    @Column(name = "deleted_by_instigator")
    private boolean deletedByInstigator;

    /**
     * A boolean value used to keep track of whether the receiver has deleted/left the conversation.
     * true if they have, false if they have not.
     */
    @Column(name = "deleted_by_receiver")
    private boolean deletedByReceiver;

    /**
     * Date and time of the conversation creation
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "read_by_receiver")
    private boolean readByReceiver;

    @Column(name = "read_by_instigator")
    private boolean readByInstigator;

    /**
     * Constructor for Conversation
     * @param instigator Instigator User
     * @param receiver Receiver User
     * @param marketplaceCard Marketplace Card
     */
    public Conversation(User instigator, User receiver, MarketplaceCard marketplaceCard) {
        this.instigator = instigator;
        this.receiver = receiver;
        this.marketplaceCard = marketplaceCard;
        this.deletedByInstigator = false;
        this.deletedByReceiver = false;
        this.created = LocalDateTime.now();

        this.readByInstigator = false;
        this.readByReceiver = false;
    }

    /**
     * Converts the Conversation into its payload representation to be sent to the frontend.
     * @return ConversationPayload object representing the conversation.
     */
    public ConversationPayload toConversationPayload() {
        return new ConversationPayload(id, instigator, receiver, marketplaceCard, created, deletedByInstigator, deletedByReceiver, readByInstigator, readByReceiver);
    }

    /**
     * If the conversation has been deleted by both the instigator and receiver then it contains no members and should
     * be deleted.
     * @return boolean true if conversation is deleted by both instigator and receiver, otherwise false.
     */
    public boolean hasNoMembers() {
        return deletedByInstigator && deletedByReceiver;
    }
}
