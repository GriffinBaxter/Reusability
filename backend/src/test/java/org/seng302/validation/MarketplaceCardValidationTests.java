package org.seng302.validation;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

class MarketplaceCardValidationTests {


    // *************************************************** TITLE *******************************************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of title
     * is less than the minimum length.
     */
    @Test
    void isValidTitle_GivenTitleLessThanMinLength_ExpectFalse() {
        String title = "a"; //minLength = 2
        assertEquals(false, MarketplaceCardValidation.isValidTitle(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when title
     * has the same length as the min length.
     */
    @Test
    void isValidTitle_GivenTitleIsEqualMinLength_ExpectTrue() {
        String title = "Ha"; // minLength = 2
        assertEquals(true, MarketplaceCardValidation.isValidTitle(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when title
     * is of the correct length.
     */
    @Test
    void isValidTitle_GivenTitleIsCorrectLength_ExpectTrue() {
        String title = "Hayley";
        assertEquals(true, MarketplaceCardValidation.isValidTitle(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when title
     * has the same length as the max length.
     */
    @Test
    void isValidTitle_GivenTitleIsEqualMaxLength_ExpectTrue() {
        String string = "H";
        String title = string.repeat(70); //maxLength = 70
        assertEquals(true, MarketplaceCardValidation.isValidTitle(title));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of title
     * is greater than the max length.
     */
    @Test
    void isValidTitle_GivenTitleGreaterThanMaxLength_ExpectFalse() {
        String string = "H";
        String title = string.repeat(71); //maxLength = 70
        assertEquals(false, MarketplaceCardValidation.isValidTitle(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when title
     * is of the right length but contains numbers.
     */
    @Test
    void isValidTitle_GivenTitleContainsNumbers_ExpectTrue() {
        String title = "Hayley's 99th Birthday";
        assertEquals(true, MarketplaceCardValidation.isValidTitle(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when title
     * is of the right length and contains any symbol.
     */
    @Test
    void isValidTitle_GivenTitleContainsValidSymbols_ExpectTrue() {
        String title = "Hayley !@#$%^&*()_-+=[]{}|?<>,.";
        assertEquals(true, MarketplaceCardValidation.isValidTitle(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when title
     * is of the right length and contains a space.
     */
    @Test
    void isValidTitle_GivenTitleContainsSpace_ExpectTrue() {
        String title = "Hayley Krippner";
        assertEquals(true, MarketplaceCardValidation.isValidTitle(title));
    }

    // *********************************************** DESCRIPTION *****************************************************

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has the same length as the min length.
     */
    @Test
    void isValidDescription_GivenDescriptionIsEqualMinLength_ExpectTrue() {
        String title = ""; // minLength = 0
        assertEquals(true, MarketplaceCardValidation.isValidDescription(title));
    }
    /**
     * Test to see whether true (i.e valid) is returned when description
     * is of the correct length.
     */
    @Test
    void isValidDescription_GivenDescriptionIsCorrectLength_ExpectTrue() {
        String string = "H";
        String title = string.repeat(400);
        assertEquals(true, MarketplaceCardValidation.isValidDescription(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has the same length as the max length.
     */
    @Test
    void isValidDescription_GivenDescriptionIsEqualMaxLength_ExpectTrue() {
        String string = "H";
        String title = string.repeat(500); //maxLength = 500
        assertEquals(true, MarketplaceCardValidation.isValidDescription(title));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of description
     * is greater than the max length.
     */
    @Test
    void isValidDescription_GivenDescriptionGreaterThanMaxLength_ExpectFalse() {
        String string = "H";
        String description = string.repeat(501); //maxLength = 500
        assertEquals(false, MarketplaceCardValidation.isValidDescription(description));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * is of the right length but contains numbers.
     */
    @Test
    void isValidDescription_GivenDescriptionContainsNumbers_ExpectTrue() {
        String title = "Hayley's 99th Birthday";
        assertEquals(true, MarketplaceCardValidation.isValidDescription(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * is of the right length but contains numbers.
     */
    @Test
    void isValidDescription_GivenTitleContainsNumbers_ExpectTrue() {
        String title = "11111111111111111111111111111111111111111111";
        assertEquals(true, MarketplaceCardValidation.isValidDescription(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * is of the right length and contains any symbol.
     */
    @Test
    void isValidDescription_GivenDescriptionContainsValidSymbols_ExpectTrue() {
        String title = "Hayley !@#$%^&*()_-+=[]{}|?<>,.";
        assertEquals(true, MarketplaceCardValidation.isValidDescription(title));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * is of the right length and contains a space.
     */
    @Test
    void isValidDescription_GivenDescriptionContainsSpace_ExpectTrue() {
        String title = "Hayley Krippner";
        assertEquals(true, MarketplaceCardValidation.isValidDescription(title));
    }

}
