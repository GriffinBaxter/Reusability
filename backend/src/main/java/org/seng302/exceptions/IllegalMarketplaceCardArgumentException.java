package org.seng302.exceptions;

/**
 * A custom exception for MarketplaceCard. This exception is thrown whenever trying to create a new MarketplaceCard
 * entity with data that is invalid (e.g. title is empty)
 */
public class IllegalMarketplaceCardArgumentException extends Exception {

    /**
     * A constructor for IllegalMarketplaceCardArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid title")
     */
    public IllegalMarketplaceCardArgumentException(String errorMessage) {
        super(errorMessage);
    }

}
