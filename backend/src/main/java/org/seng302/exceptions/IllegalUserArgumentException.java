package org.seng302.exceptions;

/**
 * A custom exception for User. This exception is thrown whenever trying to create a new User
 * entity with data that is invalid (e.g. first name is empty.)
 */
public class IllegalUserArgumentException extends Exception {

    /**
     * A constructor for IllegalUserArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid first name")
     */
    public IllegalUserArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
