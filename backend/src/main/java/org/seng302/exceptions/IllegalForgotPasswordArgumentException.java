package org.seng302.exceptions;

/**
 * A custom exception for ForgotPassword. This exception is thrown whenever trying to create a new ForgotPassword
 * entity with data that is invalid (e.g. userId is negative)
 */
public class IllegalForgotPasswordArgumentException extends Exception {

    /**
     * A constructor for IllegalForgotPasswordArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid userId")
     */
    public IllegalForgotPasswordArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
