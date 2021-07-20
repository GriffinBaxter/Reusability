package org.seng302.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class contains methods which are needed for pagination across multiple Controller classes.
 */
public class PaginationUtils {

    private static final Logger logger = LogManager.getLogger(PaginationUtils.class.getName());

    private PaginationUtils() {
        // not called
    }

    /**
     * This method parses the page number (i.e converts it from a string to an integer).
     * If the string representation is not a valid page number (can't be converted to an integer), then a 400 BAD REQUEST
     * error is thrown.
     *
     * @param page A string representation of the current page number.
     * @return an integer representation of the page number.
     *
     * Preconditions:  A string representation of a page number.
     * Postconditions: An integer representation of a page number.
     */
    public static int parsePageNumber(String page) {
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (final NumberFormatException e) {
            logger.error("400 [BAD REQUEST] - {} is not a valid page number", page);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page parameter invalid"
            );
        }
        return pageNo;
    }
}
