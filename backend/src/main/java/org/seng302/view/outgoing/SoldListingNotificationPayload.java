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
     */
    public SoldListingNotificationPayload(Integer id,
                                      SoldListingPayload soldListing,
                                      String description,
                                      LocalDateTime created
    ) {
        this.id = id;
        this.soldListing = soldListing;
        this.description = description;
        this.created = created.toString();
        this.notificationType = NotificationType.SOLD_LISTING.toString();
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }

    public SoldListingPayload getSoldListing() {
        return soldListing;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setSoldListing(SoldListingPayload soldListing) {
        this.soldListing = soldListing;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
