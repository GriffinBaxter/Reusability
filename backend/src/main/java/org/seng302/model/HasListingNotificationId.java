package org.seng302.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * A class which represents a composite key for the HasListingNotification relationship.
 */
@Embeddable
public class HasListingNotificationId implements Serializable {

    @Column(name = "id", nullable = false)
    private int userId;

    @Column(name = "listing_notification_id", nullable = false)
    private int listingNotificationId;

    public HasListingNotificationId() {
        // no args constructor
    }

    /**
     * Get the id of the user.
     * @return id of the user.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the user id to the id of another user.
     * @param userId an id representing an existing user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Get the id of the listing notification.
     * @return id of the listing notification.
     */
    public int getListingNotificationId() {
        return listingNotificationId;
    }

    /**
     * Set the listing notification id to the id of another listing notification.
     * @param listingNotificationId an id representing an existing listing notification.
     */
    public void setListingNotificationId(int listingNotificationId) {
        this.listingNotificationId = listingNotificationId;
    }

}
