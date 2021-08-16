package org.seng302.exceptions;

/**
 * A custom exception for SoldListingNotifications. This exception is thrown whenever trying to create a new SoldListingNotification
 * entity with data that is invalid (e.g. the length of description is greater than the max length.)
 */
public class IllegalSoldListingNotificationArgumentException extends Exception {

    /**
     * A constructor for IllegalSoldListingNotificationArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid description")
     */
    public IllegalSoldListingNotificationArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
