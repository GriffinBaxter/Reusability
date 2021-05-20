package org.seng302.marketplace;

/**
 * KeywordPayload class used when sending a response for a GET request.
 */
public class KeywordPayload {

    private String name;

    /**
     * Retrieve the name of a keyword.
     * @return the name of the keyword.
     */
    public String getName() {
        return name;
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the KeywordPayload.
     */
    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\"" + "}";
    }
}
