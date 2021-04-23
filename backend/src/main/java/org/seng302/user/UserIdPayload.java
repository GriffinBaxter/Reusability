package org.seng302.user;

/**
 * Payload for the User ID.
 */
public class UserIdPayload {
    private int userId;

    public UserIdPayload(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public UserIdPayload() {
    }
}
