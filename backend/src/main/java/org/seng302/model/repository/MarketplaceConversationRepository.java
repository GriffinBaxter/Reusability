/**
 * Summary. Contains the definition for the conversation repository interface.
 *
 * Description. Contains the definition for the conversation repository, which defines the functions
 * used to perform actions with the data.
 *
 * @link   team-400/src/main/java/org/seng302/model/repository
 * @file   This file defines the MarketplaceConversationRepository interface.
 * @author team-400
 * @since  31.08.2021
 */

package org.seng302.model.repository;

import org.seng302.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource
public interface MarketplaceConversationRepository extends JpaRepository<Conversation, Integer> {

    /**
     * Search to see if a conversation exists.
     * @param conversationId the id of the conversation to search for.
     * @return conversation object if exists
     */
    Optional<Conversation> findConversationById(
            Integer conversationId
    );
}
