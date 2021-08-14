package org.seng302.model.repository;

import org.seng302.model.BookmarkedListingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * BookmarkedListingMessageRepository interface.
 * This class can be used to retrieve BookmarkedListingMessages based on various criteria.
 */
@EnableJpaRepositories
public interface BookmarkedListingMessageRepository extends JpaRepository<BookmarkedListingMessage, Integer> {

//    /**
//     * Search for a list of bookmarked listings messages matching given a user id.
//     * Note: it is findAllByUsersId (plural), not findAllByUserId because the BookmarkedListingMessage entity has
//     *       a list of users.
//     * @param id The id of the user to search by
//     * @return A list of bookmarked listings messages for the given user.
//     */
//    List<BookmarkedListingMessage> findAllByUsersId(Integer id);

}
