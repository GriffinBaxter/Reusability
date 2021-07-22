package org.seng302.model;

import javax.persistence.*;

/**
 * An entity class which models the join table between users and keyword notifications.
 */
@Entity
public class HasReadKeywordNotification {

    @EmbeddedId
    private HasReadKeywordNotificationKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("keywordNotificationId")
    @JoinColumn(name = "keyword_notification_id")
    private KeywordNotification keywordNotification;

    @Column(name = "hasRead", nullable = false)
    private boolean hasRead;

    public HasReadKeywordNotification() {
        // no args constructor needed by JPA
    }

    /**
     * Get the id of the relationship between a user and keyword notification.
     * @return id of the relationship between a user and keyword notification.
     */
    public HasReadKeywordNotificationKey getId() {
        return id;
    }

    /**
     * Set the id of the relationship between a user and keyword notification.
     * @param id the id of a relationship between a user and keyword notification.
     */
    public void setId(HasReadKeywordNotificationKey id) {
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
     * Get the keyword notification which is joined by this entity.
     * @return an existing keywordNotification.
     */
    public KeywordNotification getKeywordNotification() {
        return keywordNotification;
    }

    /**
     * Change the keyword notification which is joined by this entity.
     * @param keywordNotification an existing keyword notification.
     */
    public void setKeywordNotification(KeywordNotification keywordNotification) {
        this.keywordNotification = keywordNotification;
    }

    /**
     * Get a boolean value which represents whether a user has read a keyword notification.
     * @return boolean value representing whether a user has read a keyword notification.
     */
    public boolean isHasRead() {
        return hasRead;
    }

    /**
     * Change a boolean value which represents whether a user has read a keyword notification.
     * @param hasRead boolean value representing whether a user has read a keyword notification.
     */
    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
