/**
 * Summary. This file contains the definition for the UserIdPayload.
 *
 * Description. This file contains the defintion for the UserIdPayload.
 *
 * @link   team-400/src/main/java/org/seng302/user/UserIdPayload
 * @file   This file contains the definition for UserIdPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.incoming;

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
