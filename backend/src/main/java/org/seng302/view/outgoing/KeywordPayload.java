package org.seng302.view.outgoing;

import java.time.LocalDateTime;

/**
 * KeywordPayload class used when sending a response for a GET request.
 */
public class KeywordPayload {

    private Integer id;
    private String name;
    private LocalDateTime created;

    public KeywordPayload (Integer id, String name, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    /**
     * Retrieve the name of a keyword.
     * @return the name of the keyword.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the id of the keyword.
     * @return the id of the keyword.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Retrieve the date and time the keyword was created.
     * @return the date and time the keyword was created.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the KeywordPayload.
     */
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"name\":\"" + name + "," +
                "\"created\":\"" + created + "\"" +
                "}";
    }
}
