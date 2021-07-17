package org.seng302.model.repository;

import org.seng302.model.MarketCardNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface MarketCardNotificationRepository extends JpaRepository<MarketCardNotification, String> {

    /**
     * Search for an notification by given id and message
     *
     * @param id          receiver id
     * @param description description
     * @return a notification matching given info
     */
    Optional<MarketCardNotification> findByUserIdAndDescription(Integer id, String description);

    /**
     * Search for a list of notification matching given user id
     *
     * @param id receiver id
     * @return a list of notification matching given user id
     */
    List<MarketCardNotification> findAllByUserId(Integer id);
}
