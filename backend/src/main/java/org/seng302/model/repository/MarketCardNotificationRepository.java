package org.seng302.model.repository;

import org.seng302.model.MarketCardNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface MarketCardNotificationRepository extends JpaRepository<MarketCardNotification, Integer> {

    /**
     * Search for an notification by given id and relate market card id
     *
     * @param userId       receiver id
     * @param marketCardId market card id
     * @return a notification matching given info
     */
    Optional<MarketCardNotification> findByUserIdAndMarketCardId(Integer userId, Integer marketCardId);

    /**
     * Search for a list of notification matching given user id
     *
     * @param id receiver id
     * @return a list of notification matching given user id
     */
    List<MarketCardNotification> findAllByUserId(Integer id);

    /**
     * Delete all notification for given market card
     *
     * @param id market cardid
     */
    void deleteAllByMarketCardId(Integer id);
}
