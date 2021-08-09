package org.seng302.exceptions;

/**
 * A custom exception for ListingNotifications. This exception is thrown whenever trying to create a new ListingNotification
 * entity with data that is invalid (e.g. the length of description is greater than the max length.)
 */
public class IllegalListingNotificationArgumentException extends Exception {

    /**
     * A constructor for IllegalListingNotificationArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid description")
     */
    public IllegalListingNotificationArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
