/**
 * Summary. This file contains the definition for ForgotPassword.
 *
 * Description. This file contains the definition for ForgotPassword.
 *
 * @link   team-400/src/main/java/org/seng302/forgotPassword/ForgotPassword
 * @file   This file contains the definition for ForgotPassword.
 * @author team-400.
 * @since  22.9.2021
 */

package org.seng302.model;

import lombok.NoArgsConstructor;
import org.seng302.exceptions.IllegalForgotPasswordArgumentException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class for ForgotPassword entity
 */
@Embeddable
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class ForgotPassword {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry")
    private LocalDateTime expiry;

    /**
     * Constructor method for the ForgotPassword Class.
     * @param userId Id of associated user
     * @param expiry Date/Time token will expire
     */
    public ForgotPassword(Integer userId, LocalDateTime expiry) throws IllegalForgotPasswordArgumentException {
        if (!validUserId(userId)) {
            throw new IllegalForgotPasswordArgumentException("Invalid userId");
        }
        if (!validExpiry(expiry)) {
            throw new IllegalForgotPasswordArgumentException("Invalid expiry");
        }

        this.userId = userId;
        this.token = generateForgotPasswordToken();
        this.expiry = expiry;
    }

    /**
     * Gets the id of ForgotPassword Entity
     * @return id
     */
    public int getId() { return this.id; }

    /**
     * Gets the id of the associated user
     * @return userId
     */
    public int getUserId() { return this.userId; }

    /**
     * Gets the generated forgot password token
     * @return token
     */
    public String getToken() { return this.token; }

    /**
     * Gets the expiry Date/Time object
     * @return expiry
     */
    public LocalDateTime getExpiry() { return this.expiry; }

    /**
     * Checks if the requested forgotten password token is still valid.
     * @return T/F if token is valid
     */
    public boolean isValidToken() {
        return (this.expiry.isAfter(LocalDateTime.now()));
    }

    /**
     * Checks the userId is a valid positive integer
     * @param userId Id of user
     * @return T/F if userId is valid
     */
    private boolean validUserId(Integer userId) {
        return (userId != null && userId > 0);
    }

    /**
     * Checks the expiry date is in the future
     * @param expiry date of expiry
     * @return T/F if expiry is valid
     */
    private boolean validExpiry(LocalDateTime expiry) {
        return (expiry != null && expiry.isAfter(LocalDateTime.now()));
    }

    /**
     * Generate a randomised UUID used for a Forgot Password token.
     * @return UUID
     */
    private String generateForgotPasswordToken() {
        return UUID.randomUUID().toString();
    }
}
