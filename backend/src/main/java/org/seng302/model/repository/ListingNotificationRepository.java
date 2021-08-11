package org.seng302.model.repository;

import org.seng302.model.ListingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ListingNotificationRepository interface.
 * This class can be used to retrieve listing notifications based on various criteria.
 */
@EnableJpaRepositories
public interface ListingNotificationRepository extends JpaRepository<ListingNotification, Integer> {

}
