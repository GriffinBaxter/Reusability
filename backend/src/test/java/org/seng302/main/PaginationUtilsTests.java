package org.seng302.main;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.seng302.utils.PaginationUtils.parsePageSizeNumber;

/**
 * Tests for PaginationUtils class
 * Contains tests for parsePageSizeNumber
 */
class PaginationUtilsTests {

    @Test
    void canParseValidPageSizeLessThan48() {
        String integerToParse = "47";

        int parsedInteger = parsePageSizeNumber(integerToParse);

        assertEquals(47, parsedInteger);
    }

    @Test
    void canParseValidPageSizeEqualTo48() {
        String integerToParse = "48";

        int parsedInteger = parsePageSizeNumber(integerToParse);

        assertEquals(48, parsedInteger);
    }

    @Test
    void canParseValidPageSizeGreaterThan48() {
        String integerToParse = "49";

        int parsedInteger = parsePageSizeNumber(integerToParse);

        assertEquals(48, parsedInteger);
    }

    @Test
    void cannotParseInvalidPageSize() {
        String integerToParse = "a";

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> parsePageSizeNumber(integerToParse));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertEquals("Page size parameter invalid", e.getReason());
    }
}
