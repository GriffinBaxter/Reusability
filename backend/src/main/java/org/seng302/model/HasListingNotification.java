package org.seng302.model;

import javax.persistence.*;

/**
 * An entity class which models the joint table between users and listing notifications.
 */
@Entity
public class HasListingNotification {

    @EmbeddedId
    private HasListingNotificationId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("listingNotificationId")
    @JoinColumn(name = "listing_notification_id")
    private ListingNotification listingNotification;

    @Column(name = "hasRead", nullable = false)
    private boolean hasRead;

    public HasListingNotification() {
        // no args constructor needed by JPA
    }

    /**
     * Get the id of the relationship between a user and listing notification.
     * @return id of the relationship between a user and listing notification.
     */
    public HasListingNotificationId getId() {
        return id;
    }

    /**
     * Set the id of the relationship between a user and listing notification.
     * @param id the id of a relationship between a user and listing notification.
     */
    public void setId(HasListingNotificationId id) {
        this.id = id;
    }

    /**
     * Get the user who is joined by this entity.
     * @return an existing user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Change the user who is joined by this entity.
     * @param user an existing user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the listing notification which is joined by this entity.
     * @return an existing listing notification.
     */
    public ListingNotification getListingNotification() {
        return listingNotification;
    }

    /**
     * Change the listing notification which is joined by this entity.
     * @param listingNotification an existing listing notification.
     */
    public void setListingNotification(ListingNotification listingNotification) {
        this.listingNotification = listingNotification;
    }

    /**
     * Get a boolean value which represents whether a user has read a listing notification.
     * @return boolean value representing whether a user has read a listing notification.
     */
    public boolean getHasRead() {
        return hasRead;
    }

    /**
     * Change a boolean value which represents whether a user has read a listing notification.
     * @param hasRead boolean value representing whether a user has read a listing notification.
     */
    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
