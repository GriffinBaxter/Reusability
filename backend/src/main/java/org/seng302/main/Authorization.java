package org.seng302.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.BusinessResource;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Utility class containing authorization methods.
 */
public class Authorization {

    private static final Logger logger = LogManager.getLogger(Authorization.class.getName());

    /**
     * Verifies the session token, throws an error if it does not exist, and if it does, returns the User object.
     * @param sessionToken Session token
     * @return User object
     */
    public static User getUserVerifySession(String sessionToken, UserRepository userRepository) {
        Optional<User> user = Optional.empty();
        if (sessionToken != null) {
            user = userRepository.findBySessionUUID(sessionToken);
        }
        if (user.isEmpty()) {
            logger.error("Invalid Session Token - {} - UNAUTHORIZED", sessionToken);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Access token is missing or invalid"
            );
        } else {
            return user.get();
        }
    }

    /**
     * Checks if the current user's role matches the role parameter.
     * This method is useful for user authentication/identification.
     * @param currentUser current user
     * @param role Role being matched
     * @return boolean Returns true if the current user's role matches the role parameter, otherwise false.
     */
    public static boolean verifyRole(User currentUser, Role role) {
        if (currentUser.getRole().equals(role)) {
            return true;
        }
        return false;

    }

    /**
     * Checks if a business exists with the given ID in the given business repository.
     *
     * @param businessId An integer that may be the ID of a business.
     * @param businessRepository A business repository.
     * @return boolean Returns true if a business exists with the given ID.
     */
    public static boolean verifyBusinessExists(Integer businessId, BusinessRepository businessRepository) {
        return businessRepository.findBusinessById(businessId).isPresent();
    }

    /**
     * Checks to see whether the current user is a GAA or DGAA.
     * This is important because GAA and DGAA users have additional
     * permissions.
     * @param currentUser The user who is being checked to see if they are a GAA or DGAA.
     * @return boolean Returns true if a user is a GAA or DGAA.
     */
    public static boolean isGAAorDGAA(User currentUser) {
        return (currentUser.getRole() == Role.GLOBALAPPLICATIONADMIN) ||
                (currentUser.getRole() == Role.DEFAULTGLOBALAPPLICATIONADMIN);
    }

}
