package org.seng302.view.outgoing;

import java.time.LocalDateTime;

/**
 * Payload for the BookmarkedListingMessage.
 */
public class BookmarkedListingMessagePayload {

    private Integer id;
    private String description;
    private String created;

    /**
     * @param id                     message id
     * @param description            description
     * @param created                time message was created
     */
    public BookmarkedListingMessagePayload(Integer id,
                                           String description,
                                           LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created.toString();
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the BookmarkedListingMessagePayload.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"description\":\"" + description + "\"" +
                ",\"created\":\"" + created + "\"" +
                "}";
    }

}
