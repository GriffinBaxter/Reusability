package org.seng302.exceptions;

/**
 * A custom exception for Messages. This exception is thrown whenever trying to create a new Message entity
 * with data that is invalid (e.g. message length is invalid.)
 */
public class IllegalMessageContentException extends Exception {

    /**
     * A constructor for IllegalMessageContentException.
     * @param errorMessage the error message to be included when exception is thrown
     * (e.g. "Invalid message length, must be between 1 and 300 characters")
     */
    public IllegalMessageContentException(String errorMessage) {
        super(errorMessage);
    }
    
}
