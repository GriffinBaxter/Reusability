/**
 * Summary. This file contains the definition for the UserLoginPayload.
 *
 * Description. This file contains the defintion for the UserLoginPayload.
 *
 * @link   team-400/src/main/java/org/seng302/user/UserLoginPayload
 * @file   This file contains the definition for UserLoginPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.incoming;

public class UserLoginPayload {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
