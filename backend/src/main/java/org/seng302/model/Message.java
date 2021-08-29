package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Message Entity (contains its associated Conversation)
 */
@Data
@NoArgsConstructor
@Entity
public class Message {

    /**
     * ID field for the Message entity
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private int id;

    /**
     * Conversation the message is in, lazily fetched
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    /**
     * User that sent the message, lazily fetched
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /**
     * Content of the message, with a max of 300 characters
     */
    @Column(name = "content", nullable = false, length = 300)
    private String content;

    /**
     * Date and time of the message creation
     */
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

}
