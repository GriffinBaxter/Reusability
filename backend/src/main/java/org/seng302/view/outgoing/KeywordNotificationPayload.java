package org.seng302.view.outgoing;

import java.time.LocalDateTime;

/**
 * Payload for the KeywordNotification.
 */
public class KeywordNotificationPayload {

    private Integer id;
    private String description;
    private String created;
    private KeywordPayload keyword;

    /**
     * @param id                     notification id
     * @param description            description
     * @param created                time notification been created
     * @param keyword                payload representation of the keyword the notification is for
     */
    public KeywordNotificationPayload(Integer id,
                                     String description,
                                     LocalDateTime created,
                                     KeywordPayload keyword) {
        this.id = id;
        this.description = description;
        this.created = created.toString();
        this.keyword = keyword;
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

    public KeywordPayload getKeyword() {
        return keyword;
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

    public void setKeyword(KeywordPayload keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"description\":\"" + description + "\"" +
                ",\"created\":\"" + created + "\"" +
                ",\"keyword\":" + keyword.toString() +
                "}";
    }

}
