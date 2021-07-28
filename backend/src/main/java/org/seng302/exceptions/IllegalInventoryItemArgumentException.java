package org.seng302.exceptions;

/**
 * A custom exception for Inventory Item. This exception is thrown whenever trying to create a new Inventory
 * Item entity with data that is invalid (e.g. quantity is negative.)
 */
public class IllegalInventoryItemArgumentException extends Exception {

    /**
     * A constructor for IllegalInventoryItemArgumentException.
     * @param errorMessage the error message to be included when exception is thrown
     *                     (e.g. "Invalid quantity, must have at least one item")
     */
    public IllegalInventoryItemArgumentException(String errorMessage) {
        super(errorMessage);
    }


}
