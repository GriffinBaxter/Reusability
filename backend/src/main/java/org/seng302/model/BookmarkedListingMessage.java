package org.seng302.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.seng302.exceptions.IllegalBookmarkedListingMessageArgumentException;
import org.seng302.view.outgoing.BookmarkedListingMessagePayload;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for bookmarked listing message.
 * User who bookmarks an individual full sale listing will receive an associated bookmark listing message.
 */
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class BookmarkedListingMessage {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "description", length = 600)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    // Listing
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listingId", nullable = false)
    private Listing listing;

    @OneToMany(mappedBy = "bookmarkedListingMessage")
    private List<HasBookmarkedListingMessage> readBookmarkedListingMessages = new ArrayList<>();

    @JsonManagedReference
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bookmarked_listing_message_users",
            joinColumns = {@JoinColumn(name = "bookmarked_listing_message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users = new ArrayList<>();

    // Values need for validation.
    private static final Integer DESCRIPTION_MIN_LENGTH = 2;
    private static final Integer DESCRIPTION_MAX_LENGTH = 600;

    /**
     * A constructor used to created a new BookmarkedListingMessage.
     * @param description a description for the message. Has a min length of 2 and max length of 600.
     * @param created the date and time the bookmarked listing message was created.
     * @param listing the individual full sale listing's id that the message is for.
     * @throws IllegalBookmarkedListingMessageArgumentException thrown if a required field is not supplied or is invalid.
     */
    public BookmarkedListingMessage(String description, LocalDateTime created, Listing listing) throws IllegalBookmarkedListingMessageArgumentException {
        if (!(isValidDescription(description))) {
            throw new IllegalBookmarkedListingMessageArgumentException("Invalid description");
        }
        if (created == null) {
            throw new IllegalBookmarkedListingMessageArgumentException("Created date is required");
        }
        if (listing == null) {
            throw new IllegalBookmarkedListingMessageArgumentException("Listing is required");
        }
        this.description = description;
        this.created = created;
        this.listing = listing;
    }

    /**
     * Get the id of the bookmarked listing message.
     * @return the id of the keyword notification.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the bookmarked listing message.
     * @param id the id to be set to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the description of the bookmarked listing message.
     * @return the description of the bookmarked listing message.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the bookmarked listing message.
     * @param description the description to be set to.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the date and time the bookmarked listing message was created.
     * @return the date and time the bookmarked listing message was created.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Change the time the bookmarked listing message was created.
     * In reality this method should never be called, as you
     * shouldn't need to change the time a bookmarked listing message was created.
     * @param created the date and time the bookmarked listing message was created.
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Get the listing corresponding to the bookmarked listing message.
     * @return listing that the listing that has been bookmarked..
     */
    public Listing getListing() {
        return listing;
    }

    /**
     * Change the listing for the corresponding bookmarked listing message.
     * @param listing the listing that has been bookmarked.
     */
    public void setListing(Listing listing) {
        this.listing = listing;
    }

    /**
     * Get the list of users associated with the bookmarked listing message.
     * @return the list of users associated with the bookmarked listing message.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Set the list of users associated with the bookmarked listing message.
     * @param users A list of users to set.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Add a user to the list of users for this bookmarked listing message.
     * Called when a new bookmarked listing message is created.
     *
     * @param user a user that is to be added to this bookmarked listing message.
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Remove a user from the list of users for this bookmarked listing message.
     *
     * @param user a user that is to be removed from this bookmarked listing message.
     */
    public void removeUser(User user) {
        int userId = user.getId();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                this.users.remove(i);
            }
        }
    }

    /**
     * Get the list of HasReadKeywordNotifications, which can be used to determine if a user
     * has read a notification.
     * @return list of HaHasReadKeywordNotifications, which can be used to determine if a user
     * has read a notification.
     */
    public List<HasBookmarkedListingMessage> getBookmarkedListingMessages() {
        return readBookmarkedListingMessages;
    }

    /**
     * Set the list of HasReadKeywordNotifications, which can be used to determine if a user
     * has read a notification.
     * @param readBookmarkedListingMessages a new list of HasReadKeywordNotifications, to be set to.
     */
    public void setReadBookmarkedListingMessages(List<HasBookmarkedListingMessage> readBookmarkedListingMessages) {
        this.readBookmarkedListingMessages = readBookmarkedListingMessages;
    }

    public BookmarkedListingMessagePayload toBookmarkedListingMessagePayload() {
        return new BookmarkedListingMessagePayload(id, description, created, listing.getCloses(), listing.getId(), listing.getBusinessId());
    }

    /*---------------------------------------------------Validation---------------------------------------------------*/

    /**
     * Checks to see whether a bookmarked listing message's description is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    private boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) &&
                (description.length() <= DESCRIPTION_MAX_LENGTH);
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the BookmarkedListingMessage.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id + "," +
                "\"description\":\"" + description + "\"," +
                "\"created\":\"" + created + "\",";
    }
}
