package org.seng302.exceptions;

public class IllegalSoldListingArgumentException extends Exception{

    /**
     * A constructor for IllegalListingArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid business")
     */
    public IllegalSoldListingArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
