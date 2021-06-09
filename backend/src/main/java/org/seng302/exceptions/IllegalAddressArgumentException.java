package org.seng302.exceptions;

/**
 * A custom exception for Addresses. This exception is thrown whenever trying to create a new Address entity
 * with data that is invalid (e.g. no country is supplied when it is a required field.)
 */
public class IllegalAddressArgumentException extends Exception {

    /**
     * A constructor for IllegalAddressArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid country")
     */
    public IllegalAddressArgumentException(String errorMessage) {
        super(errorMessage);
    }

}