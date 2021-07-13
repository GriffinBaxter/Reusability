package org.seng302.model.repository;

import org.seng302.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface NotificationRepository extends JpaRepository<Notification, String> {

    /**
     * Search for an notification by given id and message
     * @param id receiver id
     * @param notificationMessage notification message
     * @return a notification matching given info
     */
    //TODO: Test
    Optional<Notification> findByReceiverIdAndNotificationMessage(Integer id, String notificationMessage);

    /**
     * Search for a list of notification matching given user id
     * @param id receiver id
     * @return a list of notification matching given user id
     */
    //TODO: Test
    List<Notification> findAllByReceiverId(Integer id);
}
