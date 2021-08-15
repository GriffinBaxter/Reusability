package org.seng302.model.repository;

import org.seng302.model.ListingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * ListingNotificationRepository interface.
 * This class can be used to retrieve listing notifications based on various criteria.
 */
@EnableJpaRepositories
public interface ListingNotificationRepository extends JpaRepository<ListingNotification, Integer> {

    /**
     * Search for a list of listing notifications matching given a user id.
     * Note: it is findAllByUsersId (plural), not findAllByUserId because listing notifications has
     *       a list of users.
     * @param id the id of the user to search for
     * @return a list of listings notifications of a user.
     */
    List<ListingNotification> findAllByUsersId(Integer id);

}
