package org.seng302.exceptions;

/**
 * A custom exception for KeywordNotifications. This exception is thrown whenever trying to create a new KeywordNotification
 * entity with data that is invalid (e.g. the length of description is greater then the max length.)
 */
public class IllegalKeywordNotificationArgumentException extends Exception {

    /**
     * A constructor for IllegalKeywordNotificationArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid description")
     */
    public IllegalKeywordNotificationArgumentException(String errorMessage) {
        super(errorMessage);
    }

}

