package org.seng302.model.repository;

import org.seng302.model.ListingNotification;
import org.seng302.model.SoldListingNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * SoldListingNotificationRepository interface.
 * This class can be used to retrieve sold listing notifications based on various criteria.
 */
@EnableJpaRepositories
public interface SoldListingNotificationRepository extends JpaRepository<SoldListingNotification, Integer> {

    /**
     * Search for a list of sold listing notifications matching a given business ID.
     * @param id the business ID you want to find notifications containing
     * @return a list of sold listing notifications of a business.
     */
    List<SoldListingNotification> findAllByBusinessId(Integer id);
}
