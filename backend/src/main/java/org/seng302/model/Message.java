package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.exceptions.IllegalMessageContentException;

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

    /**
     * Constructor for Message
     * @param conversation Conversation
     * @param sender Sender User
     * @param content Content of Message
     */
    public Message(Conversation conversation, User sender, String content) throws IllegalMessageContentException {
        this.conversation = conversation;
        this.sender = sender;
        if (content.length() < 1 || content.length() > 300) {
            throw new IllegalMessageContentException("Invalid message length, must be between 1 and 300 characters");
        }
        this.content = content;
        this.created = LocalDateTime.now();
    }
}
