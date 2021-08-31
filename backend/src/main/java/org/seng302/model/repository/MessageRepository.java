package org.seng302.model.repository;

import org.seng302.model.Conversation;
import org.seng302.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * MessageRepository interface.
 * This class can be used to perform database operations on the Message table.
 */
@RepositoryRestResource
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * This method is used to delete messages found in a conversation.
     * This method is called when a conversation is deleted.
     *
     * @param conversation the conversation which contains the messages.
     * @return the list of messages that are deleted.
     */
    List<Message> deleteByConversation(Conversation conversation);
}
