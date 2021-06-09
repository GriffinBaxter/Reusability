package org.seng302.exceptions;

/**
 * A custom exception for Businesses. This exception is thrown whenever trying to create a new Business entity
 * with data that is invalid (e.g. no business name is supplied when it is a required field.)
 */
public class IllegalBusinessArgumentException extends Exception {
    /**
     * A constructor for IllegalBusinessArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid business name.")
     */
    public IllegalBusinessArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
