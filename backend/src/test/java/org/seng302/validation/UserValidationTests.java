package org.seng302.validation;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class UserValidationTests {

    // ******************************** FIRST NAME ***********************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of first name
     * is less than the minimum length.
     */
    @Test
    public void isValidStreetNumberTestEqualMinLength() {
        String streetNumber = ""; //minLength = 0
        assertEquals(true, AddressValidation.isValidStreetNumber(streetNumber));
    }

    // less
    // greater
    // between not valid symbol
    // between with symbol
    // between with space
    // equal to max
    // less than max
}
