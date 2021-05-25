package org.seng302.validation;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

class AddressValidationTests {

    // ******************************* STREET NUMBER *********************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of street number
     * is equal to the minimum length.
     */
    @Test
    void isValidStreetNumberTestEqualMinLength() {
        String streetNumber = ""; //minLength = 0
        assertEquals(true, AddressValidation.isValidStreetNumber(streetNumber));
    }

    /**
     * Test to see whether false (i.e not valid) is returned when the length of street number
     * is greater than the max length.
     */
    @Test
    void isValidStreetNumberTestGreaterMaxLength() {
        String string = "2";
        String streetNumber = string.repeat(260); //maxLength = 255
        assertEquals(false, AddressValidation.isValidStreetNumber(streetNumber));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of street number
     * is equal to the max length.
     */
    @Test
    void isValidStreetNumberTestEqualMaxLength() {
        String string = "2";
        String streetNumber = string.repeat(255); //maxLength = 255
        assertEquals(true, AddressValidation.isValidStreetNumber(streetNumber));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of street number
     * is in range of expected lengths.
     */
    @Test
    void isValidStreetNumberTestBetweenLengths() {
        String streetNumber = "3/24";
        assertEquals(true, AddressValidation.isValidStreetNumber(streetNumber));
    }

    // ******************************* STREET NAME ***********************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of street name
     * is equal to the minimum length.
     */
    @Test
    void isValidStreetNameTestEqualMinLength() {
        String streetName = ""; //minLength = 0
        assertEquals(true, AddressValidation.isValidStreetName(streetName));
    }

    /**
     * Test to see whether false (i.e not valid) is returned when the length of street name
     * is greater than the max length.
     */
    @Test
    void isValidStreetNameTestGreaterMaxLength() {
        String string = "2";
        String streetName = string.repeat(260); //maxLength = 255
        assertEquals(false, AddressValidation.isValidStreetName(streetName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of street name
     * is equal to the max length.
     */
    @Test
    void isValidStreetNameTestEqualMaxLength() {
        String string = "2";
        String streetName = string.repeat(255); //maxLength = 255
        assertEquals(true, AddressValidation.isValidStreetName(streetName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of street name
     * is in range of expected lengths.
     */
    @Test
    void isValidStreetNameTestBetweenLengths() {
        String streetName = "Ilam Road";
        assertEquals(true, AddressValidation.isValidStreetName(streetName));
    }

    // ********************************** CITY ***************************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of city
     * is equal to the minimum length.
     */
    @Test
    void isValidCityTestEqualMinLength() {
        String city = ""; //minLength = 0
        assertEquals(true, AddressValidation.isValidCity(city));
    }

    /**
     * Test to see whether false (i.e not valid) is returned when the length of city
     * is greater than the max length.
     */
    @Test
    void isValidCityTestGreaterMaxLength() {
        String string = "A";
        String city = string.repeat(260); //maxLength = 255
        assertEquals(false, AddressValidation.isValidCity(city));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of city
     * is equal to the max length.
     */
    @Test
    void isValidCityTestEqualMaxLength() {
        String string = "A";
        String city = string.repeat(255); //maxLength = 255
        assertEquals(true, AddressValidation.isValidCity(city));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of city
     * is in range of expected lengths.
     */
    @Test
    void isValidCityTestBetweenLengths() {
        String city = "New York";
        assertEquals(true, AddressValidation.isValidCity(city));
    }

    // ********************************* REGION **************************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of region
     * is equal to the minimum length.
     */
    @Test
    void isValidRegionTestEqualMinLength() {
        String region = ""; //minLength = 0
        assertEquals(true, AddressValidation.isValidRegion(region));
    }

    /**
     * Test to see whether false (i.e not valid) is returned when the length of region
     * is greater than the max length.
     */
    @Test
    void isValidRegionTestGreaterMaxLength() {
        String string = "A";
        String region = string.repeat(260); //maxLength = 255
        assertEquals(false, AddressValidation.isValidRegion(region));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of region
     * is equal to the max length.
     */
    @Test
    void isValidRegionTestEqualMaxLength() {
        String string = "A";
        String region = string.repeat(255); //maxLength = 255
        assertEquals(true, AddressValidation.isValidRegion(region));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of region
     * is in range of expected lengths.
     */
    @Test
    void isValidRegionTestBetweenLengths() {
        String region = "Otago";
        assertEquals(true, AddressValidation.isValidRegion(region));
    }

    // ********************************* COUNTRY *************************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of country
     * is equal to the minimum length.
     */
    @Test
    void isValidCountryTestEqualMinLength() {
        String country = "N"; //minLength = 1
        assertEquals(true, AddressValidation.isValidCountry(country));
    }

    /**
     * Test to see whether false (i.e not valid) is returned when the length of country
     * is greater than the max length.
     */
    @Test
    void isValidCountryTestGreaterMaxLength() {
        String string = "N";
        String country = string.repeat(260); //maxLength = 255
        assertEquals(false, AddressValidation.isValidCountry(country));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of country
     * is equal to the max length.
     */
    @Test
    void isValidCountryTestEqualMaxLength() {
        String string = "N";
        String country = string.repeat(255); //maxLength = 255
        assertEquals(true, AddressValidation.isValidCountry(country));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of country
     * is in range of expected lengths.
     */
    @Test
    void isValidCountryTestBetweenLengths() {
        String country = "Russia";
        assertEquals(true, AddressValidation.isValidCountry(country));
    }

    /**
     * Test to see whether invalid (i.e valid) is returned when the length of country
     * is less than the min length.
     */
    @Test
    void isValidCountryTestLessThanMinLength() {
        String country = ""; //minLength = 1
        assertEquals(false, AddressValidation.isValidCountry(country));
    }

    // ******************************** POSTCODE *************************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of postcode
     * is equal to the minimum length.
     */
    @Test
    void isValidPostcodeTestEqualMinLength() {
        String postcode = ""; //minLength = 0
        assertEquals(true, AddressValidation.isValidPostcode(postcode));
    }

    /**
     * Test to see whether false (i.e not valid) is returned when the length of postcode
     * is greater than the max length.
     */
    @Test
    void isValidPostcodeTestGreaterMaxLength() {
        String string = "1";
        String postcode = string.repeat(260); //maxLength = 255
        assertEquals(false, AddressValidation.isValidPostcode(postcode));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of postcode
     * is equal to the max length.
     */
    @Test
    void isValidPostcodeTestEqualMaxLength() {
        String string = "1";
        String postcode = string.repeat(255); //maxLength = 255
        assertEquals(true, AddressValidation.isValidPostcode(postcode));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of costcode
     * is in range of expected lengths.
     */
    @Test
    void isValidPostcodeTestBetweenLengths() {
        String postcode = "8041";
        assertEquals(true, AddressValidation.isValidPostcode(postcode));
    }

    // ********************************* SUBURB **************************************

    /**
     * Test to see whether true (i.e valid) is returned when the length of suburb
     * is equal to the minimum length.
     */
    @Test
    void isValidSuburbTestLengthEqualMinLength() {
        String suburb = ""; //minLength = 0
        assertEquals(true, AddressValidation.isValidSuburb(suburb));
    }

    /**
     * Test to see whether false (i.e not valid) is returned when the length of suburb
     * is greater than the max length.
     */
    @Test
    void isValidSuburbTestLengthGreaterThanMaxLength() {
        String string = "A";
        String suburb = string.repeat(260); //maxLength = 255
        assertEquals(false, AddressValidation.isValidSuburb(suburb));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of suburb
     * is equal to the max length.
     */
    @Test
    void isValidSuburbTestLengthEqualMaxLength() {
        String string = "A";
        String suburb = string.repeat(255); //maxLength = 255
        assertEquals(true, AddressValidation.isValidSuburb(suburb));
    }

    /**
     * Test to see whether true (i.e valid) is returned when the length of suburb
     * is in range of expected lengths.
     */
    @Test
    void isValidSuburbTestLengthBetweenLengths() {
        String suburb = "Ilam";
        assertEquals(true, AddressValidation.isValidSuburb(suburb));
    }

}
