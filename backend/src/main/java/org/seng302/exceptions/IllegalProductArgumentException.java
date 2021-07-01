package org.seng302.exceptions;

/**
 * A custom exception for Product. This exception is thrown whenever trying to create a new Product
 * entity with data that is invalid (e.g. business is null.)
 */
public class IllegalProductArgumentException extends Exception {

    /**
     * A constructor for IllegalProductArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid business")
     */
    public IllegalProductArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
