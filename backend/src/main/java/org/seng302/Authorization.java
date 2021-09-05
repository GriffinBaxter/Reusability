package org.seng302;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.model.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Utility class containing authorization methods.
 */
public class Authorization {

    Authorization() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LogManager.getLogger(Authorization.class.getName());

    /**
     * Verifies the session token, throws an error if it does not exist, and if it does, returns the User object.
     *
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
            logger.debug("User (Id: {}) received.", user.get().getId());
            return user.get();
        }
    }

    /**
     * Checks if the current user's role matches the role parameter.
     * This method is useful for user authentication/identification.
     *
     * @param currentUser current user
     * @param role        Role being matched
     * @return boolean Returns true if the current user's role matches the role parameter, otherwise false.
     */
    public static boolean verifyRole(User currentUser, Role role) {
        return currentUser.getRole().equals(role);
    }

    /**
     * Checks if a business exists with the given ID in the given business repository.
     *
     * @param businessId         An integer that may be the ID of a business.
     * @param businessRepository A business repository.
     */
    public static void verifyBusinessExists(Integer businessId, BusinessRepository businessRepository) {
        if (businessRepository.findBusinessById(businessId).isEmpty()) {
            logger.error("406 [NOT ACCEPTABLE] - Business with ID {} does not exist", businessId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        } else {
            logger.debug("Business found with ID {}", businessId);
        }
    }

    /**
     * Checks to see whether the current user is a GAA or DGAA.
     * This is important because GAA and DGAA users have additional
     * permissions.
     *
     * @param currentUser The user who is being checked to see if they are a GAA or DGAA.
     * @return boolean Returns true if a user is a GAA or DGAA.
     */
    public static boolean isGAAorDGAA(User currentUser) {
        return (currentUser.getRole() == Role.GLOBALAPPLICATIONADMIN) ||
                (currentUser.getRole() == Role.DEFAULTGLOBALAPPLICATIONADMIN);
    }

    /**
     * Checks to see whether the current user is an administrator of the given business.
     * Throws a 403 FORBIDDEN error if they are not.
     *
     * @param currentUser The current user.
     * @param businessId  The ID of the business you want to check if the user is administrator for.
     */
    public static void verifyBusinessAdmin(User currentUser, Integer businessId) {
        if (currentUser.getRole() == Role.USER && !currentUser.getBusinessesAdministered().contains(businessId)) {
            logger.error("403 [FORBIDDEN] - User with ID {} is not an admin of business with ID {}", currentUser.getId(), businessId);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "The account performing the request is neither an administrator of the business, nor a global application admin."
            );
        }
    }



}
