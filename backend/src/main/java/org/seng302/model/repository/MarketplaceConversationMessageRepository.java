/**
 * Summary. Contains the definition for the conversation message repository interface.
 *
 * Description. Contains the definition for the conversation message repository, which defines the functions
 * used to perform actions with the data.
 *
 * @link   team-400/src/main/java/org/seng302/model/repository
 * @file   This file defines the MarketplaceConversationMessageRepository interface.
 * @author team-400
 * @since  31.08.2021
 */

package org.seng302.model.repository;

import org.seng302.model.Conversation;
import org.seng302.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * MessageRepository interface.
 * This class can be used to perform database operations on the Message table.
 */
@RepositoryRestResource
public interface MarketplaceConversationMessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Search to see if a message exists.
     * @param messageId the id of the message to search for.
     * @return conversation object if exists
     */
    Optional<Message> findMessageById(
            Integer messageId
    );

    /**
     * This method is used to delete messages found in a conversation.
     * This method is called when a conversation is deleted.
     *
     * @param conversation the conversation which contains the messages.
     * @return the list of messages that are deleted.
     */
    List<Message> deleteByConversation(Conversation conversation);


}
