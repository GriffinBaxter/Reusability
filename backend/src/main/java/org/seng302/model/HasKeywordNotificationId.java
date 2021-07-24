package org.seng302.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * A class which represents a composite key for the HasKeywordNotification relationship.
 */
@Embeddable
public class HasKeywordNotificationId implements Serializable {

    @Column(name = "id", nullable = false)
    private int userId;

    @Column(name = "keyword_notification_id", nullable = false)
    private int keywordNotificationId;

    public HasKeywordNotificationId() {
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
     * Get the id of the keyword notification.
     * @return id of the keyword notification.
     */
    public int getKeywordNotificationId() {
        return keywordNotificationId;
    }

    /**
     * Set the keyword notification id to the id of another keyword notification.
     * @param keywordNotificationId and id representing an existing keyword notification.
     */
    public void setKeywordNotificationId(int keywordNotificationId) {
        this.keywordNotificationId = keywordNotificationId;
    }
}
