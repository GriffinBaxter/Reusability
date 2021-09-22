package org.seng302.exceptions;

/**
 * A custom exception for ListingResource. This exception is thrown whenever trying to delete a listing and creating a notification
 * but failing.
 */
public class FailedToDeleteListingException extends Exception{

    /**
     * A constructor for FailedToDeleteListingException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid description")
     */
    public FailedToDeleteListingException(String errorMessage) {
        super(errorMessage);
    }
}
