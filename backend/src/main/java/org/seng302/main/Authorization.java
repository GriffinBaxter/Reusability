package org.seng302.main;

import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
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

    /**
     * Verifies the session token, throws an error if it does not exist, and if it does, returns the User object.
     * @param sessionToken Session token
     * @return User object
     */
    public static User getUserVerifySession(String sessionToken, UserRepository userRepository) {
        Optional<User> user = userRepository.findBySessionUUID(sessionToken);
        if (sessionToken == null || user.isEmpty()) {
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

}
