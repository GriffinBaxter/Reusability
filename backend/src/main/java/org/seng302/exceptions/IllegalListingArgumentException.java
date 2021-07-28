package org.seng302.exceptions;

/**
 * A custom exception for Listing. This exception is thrown whenever trying to create a new Listing entity
 * with data that is invalid (e.g. inventory item is null.)
 */
public class IllegalListingArgumentException extends Exception{

    /**
     * A constructor for IllegalListingArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid inventory item")
     */
    public IllegalListingArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
