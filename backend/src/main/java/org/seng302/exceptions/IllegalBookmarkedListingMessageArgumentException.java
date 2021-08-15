package org.seng302.exceptions;

/**
 * A custom exception for BookmarkListingMessage. This exception is thrown whenever trying to create a new
 * BookmarkListingMessage entity with data that is invalid (e.g. the length of description is greater then the max length.)
 */
public class IllegalBookmarkedListingMessageArgumentException extends Exception {

    /**
     * A constructor for IllegalBookmarkListingMessageArgumentException.
     * @param errorMessage the error message to be included when exception is thrown (e.g. "Invalid description")
     */
    public IllegalBookmarkedListingMessageArgumentException(String errorMessage) {
        super(errorMessage);
    }

}

