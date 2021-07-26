package org.seng302.model.repository;

import org.seng302.model.KeywordNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * KeywordNotificationRepository interface.
 * This class can be used to retrieve keyword notifications based on various criteria.
 */
@EnableJpaRepositories
public interface KeywordNotificationRepository extends JpaRepository<KeywordNotification, Integer> {

    /**
     * Delete all notifications for a given keyword.
     *
     * @param id The ID of the keyword you want to delete notifications for.
     */
    void deleteAllByKeywordId(Integer id);

}
