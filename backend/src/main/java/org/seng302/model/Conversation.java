package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Conversation Entity (contains messages)
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
     * Date and time of the conversation creation
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

}
