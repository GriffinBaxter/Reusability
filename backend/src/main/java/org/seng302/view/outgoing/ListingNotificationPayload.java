package org.seng302.view.outgoing;

import java.time.LocalDateTime;

/**
 * Payload for the ListingNotification.
 * This payload is sent to the frontend via GET requests.
 */
public class ListingNotificationPayload {

    private Integer id;
    private String description;
    private String created;

    /**
     * @param id notification id
     * @param description description of notification
     * @param created the time the notification was created
     */
    public ListingNotificationPayload(Integer id,
                                      String description,
                                      LocalDateTime created
    ) {
        this.id = id;
        this.description = description;
        this.created = created.toString();
    }

    /**
     * Get the id of the ListingNotificationPayload
     * @return an integer representing the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get the description of the ListingNotificationPayload
     * @return a string representing the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the date and time of when the ListingNotificationPayload was created
     * @return a string representing the date and time.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Change the id of the ListingNotificationPayload
     * @param id the new id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Change the description of the ListingNotificationPayload
     * @param description the new description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Change the created date and time of the ListingNotificationPayload
     * @param created the created date and time.
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the ListingNotificationPayload.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"description\":\"" + description + "\"" +
                ",\"created\":\"" + created + "\"" +
                "}";
    }

}
