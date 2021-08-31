package org.seng302.model.repository;

import org.seng302.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * ConversationRepository interface.
 * This class can be used to retrieve marketplace conversations based on various criteria.
 */
@RepositoryRestResource
public interface ConversationRepository extends JpaRepository<Conversation, Integer>  {

    /**
     * Search for an conversation by its id.
     * @param id the id of the conversation to be found
     * @return an optional which may or may not contain a conversation entity.
     */
    Optional<Conversation> findById(Integer id);
}
