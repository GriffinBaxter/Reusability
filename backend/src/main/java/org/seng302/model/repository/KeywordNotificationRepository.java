package org.seng302.model.repository;

import org.seng302.model.KeywordNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * KeywordNotificationRepository interface.
 * This class can be used to retrieve keyword notifications based on various criteria.
 */
@RepositoryRestResource
public interface KeywordNotificationRepository extends JpaRepository<KeywordNotification, Integer> {

}
