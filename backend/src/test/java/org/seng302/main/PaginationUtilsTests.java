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
    void canParseValidPageSizeLessThan500() {
        String integerToParse = "499";

        int parsedInteger = parsePageSizeNumber(integerToParse);

        assertEquals(499, parsedInteger);
    }

    @Test
    void canParseValidPageSizeEqualTo500() {
        String integerToParse = "500";

        int parsedInteger = parsePageSizeNumber(integerToParse);

        assertEquals(500, parsedInteger);
    }

    @Test
    void canParseValidPageSizeGreaterThan500() {
        String integerToParse = "501";

        int parsedInteger = parsePageSizeNumber(integerToParse);

        assertEquals(500, parsedInteger);
    }

    @Test
    void cannotParseInvalidPageSize() {
        String integerToParse = "a";

        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> parsePageSizeNumber(integerToParse));

        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertEquals("Page size parameter invalid", e.getReason());
    }
}
