package org.seng302.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * A class which represents a composite key for the HasBookmarkedListingMessage relationship.
 */
@Embeddable
public class HasBookmarkedListingMessageId implements Serializable {

    @Column(name = "id", nullable = false)
    private int userId;

    @Column(name = "bookmarked_listing_message_id", nullable = false)
    private int bookmarkedListingMessageId;

    public HasBookmarkedListingMessageId() {
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
     * Get the id of the bookmarked listing message.
     * @return id of the bookmarked listing message.
     */
    public int getBookmarkedListingMessageId() {
        return bookmarkedListingMessageId;
    }

    /**
     * Set the bookmarked listing message id to the id of another bookmarked listing message.
     * @param bookmarkedListingMessageId and id representing an existing bookmarked listing message.
     */
    public void setBookmarkedListingMessageId(int bookmarkedListingMessageId) {
        this.bookmarkedListingMessageId = bookmarkedListingMessageId;
    }
}
