package org.seng302.validation;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class KeywordValidationTests {

    // *********************************** NAME **************************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of keyword name
     * is less than the minimum length. Also a test for when no keyword is inputted.
     */
    @Test
    public void isInvalidKeywordNameWhenLengthLessThanMinLengthTest() {
        String name = ""; //minLength = 2
        assertEquals(false, KeywordValidation.isValidName(name));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of keyword name
     * is greater than the max length.
     */
    @Test
    public void isInvalidKeywordNameWhenLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String name = string.repeat(30); //maxLength = 20
        assertEquals(false, KeywordValidation.isValidName(name));
    }

    /**
     * Test to see whether true (i.e valid) is returned when keyword name
     * is of the right length and contains symbols.
     */
    @Test
    public void isValidKeywordNameWhenOfRightLengthAndContainsSymbolsTest() {
        String name = "Money Maker $$$'*!";
        assertEquals(true, KeywordValidation.isValidName(name));
    }

    /**
     * Test to see whether true (i.e valid) is returned when keyword name
     * has the same length as the min length.
     */
    @Test
    public void isValidKeywordNameWhenLengthEqualsMinLengthTest() {
        String name = "IT"; // minLength = 2
        assertEquals(true, KeywordValidation.isValidName(name));
    }

    /**
     * Test to see whether true (i.e valid) is returned when keyword name
     * has the same length as the max length.
     */
    @Test
    public void isValidKeywordNameWhenLengthEqualsMaxLengthTest() {
        String string = "Z";
        String name = string.repeat(20); //maxLength = 20
        assertEquals(true, KeywordValidation.isValidName(name));
    }

}
