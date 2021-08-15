package org.seng302.model;

import javax.persistence.*;

/**
 * An entity class which models the join table between users and bookmarked listing message.
 */
@Entity
public class HasBookmarkedListingMessage {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("bookmarkedListingMessageId")
    @JoinColumn(name = "bookmarked_listing_message_id")
    private BookmarkedListingMessage bookmarkedListingMessage;

    @Column(name = "hasRead", nullable = false)
    private boolean hasRead;

    public HasBookmarkedListingMessage() {
        // no args constructor needed by JPA
    }

    /**
     * Get the id of this entity.
     * @return the id of this entity.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of this entity.
     * @param id new id of this entity.
     */
    public void setId(int id) {
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
     * Get the bookmarked listing message which is joined by this entity.
     * @return an existing bookmarkedListingMessage.
     */
    public BookmarkedListingMessage getBookmarkedListingMessage() {
        return bookmarkedListingMessage;
    }

    /**
     * Change the bookmarked listing message which is joined by this entity.
     * @param bookmarkedListingMessage an existing bookmarked listing message.
     */
    public void setBookmarkedListingMessage(BookmarkedListingMessage bookmarkedListingMessage) {
        this.bookmarkedListingMessage = bookmarkedListingMessage;
    }

    /**
     * Get a boolean value which represents whether a user has read a bookmarked listing message.
     * @return boolean value representing whether a user has read a bookmarked listing message.
     */
    public boolean getHasRead() {
        return hasRead;
    }

    /**
     * Change a boolean value which represents whether a user has read a bookmarked listing message.
     * @param hasRead boolean value representing whether a user has read a bookmarked listing message.
     */
    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
