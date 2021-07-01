package org.seng302.exceptions;

/**
 * A custom exception for Keyword. This exception is thrown whenever trying to create a new Keyword entity
 * with data that is invalid (e.g. keyword length is less than the minimum length.)
 */
public class IllegalKeywordArgumentException extends Exception {

    /**
     * A constructor for IllegalKeywordArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid name.")
     */
    public IllegalKeywordArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
