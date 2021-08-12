package org.seng302.view.outgoing;

import org.seng302.model.enums.NotificationType;

import java.time.LocalDateTime;

/**
 * Payload for the SoldListingNotification.
 * This payload is sent to the frontend via GET requests.
 */
public class SoldListingNotificationPayload {

    private Integer id;
    private SoldListingPayload soldListing;
    private String description;
    private String created;
    private String notificationType;

    /**
     * Constructor for the SoldListingNotificationPayload
     *
     * @param id notification id
     * @param soldListing the payload representation of the sold listing associated with the notification
     * @param description description of notification
     * @param created the time the notification was created
     * @param notificationType the type of the notification
     */
    public SoldListingNotificationPayload(Integer id,
                                      SoldListingPayload soldListing,
                                      String description,
                                      LocalDateTime created,
                                      NotificationType notificationType
    ) {
        this.id = id;
        this.soldListing = soldListing;
        this.description = description;
        this.created = created.toString();
        this.notificationType = notificationType.toString();
    }
}
