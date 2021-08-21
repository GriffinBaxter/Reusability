package org.seng302.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.seng302.exceptions.IllegalListingNotificationArgumentException;
import org.seng302.model.enums.NotificationType;
import org.seng302.view.outgoing.ListingNotificationPayload;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for listing notifications.
 * Listing notifications can be associated with users.
 */
@Embeddable
@NoArgsConstructor
@Entity
public class ListingNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @JsonManagedReference
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "listing_notifications_users",
            joinColumns = {@JoinColumn(name = "listing_notification_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users = new ArrayList<>();

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "listingNotification")
    private List<HasListingNotification> readListingNotifications = new ArrayList<>();

    // Values need for validation.
    private static final Integer DESCRIPTION_MIN_LENGTH = 10;
    private static final Integer DESCRIPTION_MAX_LENGTH = 600;

    /**
     * Constructor for ListingNotification.
     * Only the description is required as the ID is generated, and created is generated upon construction.
     *
     * @param description the message of the notification
     */
    public ListingNotification(String description) throws IllegalListingNotificationArgumentException {
        if (!(isValidDescription(description))) {
            throw new IllegalListingNotificationArgumentException("Invalid description");
        }
        this.description = description;
        this.created = LocalDateTime.now();
    }

    /**
     * Add a user to the list of users for this listing notification.
     * Called when a new listing notification is created.
     *
     * @param user a user that is to be added to this listing notification.
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Remove a user from the list of users for this listing notification.
     *
     * @param user a user that is to be removed from this listing notification.
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
     * Get the id of the listing notification.
     * @return the id of the listing notification.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the list of users associated with the listing notification.
     * @return the list of users associated with the listing notification.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Get the description of the listing notification.
     * @return the description of the listing notification.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the LocalDateTime the listing notification was created.
     * @return LocalDateTime when the listing notification was created.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Get the list of HasReadListingNotifications, which can be used to determine if a user
     * has read a notification.
     * @return list of HasReadListingNotifications, which can be used to determine if a user
     * has read a notification.
     */
    public List<HasListingNotification> getReadListingNotifications() {
        return readListingNotifications;
    }

    /**
     * Set the id of the notification.
     * @param id a new id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the list of users associated with the notification.
     * @param users a new list of users.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Set the description of the notification.
     * @param description a new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the date the notification was created.
     * @param created a new LocalDateTime.
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Set the list of HasReadListingNotifications, which can be used to determine if a user
     * has read a notification.
     * @param readListingNotifications a new list of HasReadListingNotifications, to be set to.
     */
    public void setReadListingNotifications(List<HasListingNotification> readListingNotifications) {
        this.readListingNotifications = readListingNotifications;
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the ListingNotification.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id + "," +
                "\"description\":\"" + description + "\"," +
                "\"created\":\"" + created + "\"," +
                "\"users\":" + users + "}";
    }

    /**
     * Create a new payload that will be sent to the frontend in a GET request
     * @return a payload representing the listing notification
     */
    public ListingNotificationPayload toListingNotificationPayload() {
        return new ListingNotificationPayload(id, description, created);
    }

    /*---------------------------------------------------Validation---------------------------------------------------*/
    /**
     * Checks to see whether a listing notification's description is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    private boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) &&
                (description.length() <= DESCRIPTION_MAX_LENGTH);
    }

}
