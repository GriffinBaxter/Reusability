package org.seng302.model.repository;

import org.seng302.model.HasBookmarkedListingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import java.util.List;

/**
 * HasBookmarkedListingMessageRepository interface.
 * This class can be used to retrieve listing bookmark messages based on various criteria.
 */
@EnableJpaRepositories
public interface HasBookmarkedListingMessageRepository extends JpaRepository<HasBookmarkedListingMessage, Integer> {

    /**
     * Search for all HasBookmarkedListingMessage based on the given user id.
     * Note: This is an intermediary table so the actual bookmark messages will
     * need to be retrieved after looking them up using this method.
     * @param id The ID of the user to search by
     * @return A list of HasBookmarkedListingMessage entities for the given user.
     */
    List<HasBookmarkedListingMessage> findAllByUserId(Integer id);

}
