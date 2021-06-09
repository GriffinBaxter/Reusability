package org.seng302.address;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.model.Address;


class AddressTests {

    private Address address;

    @Test
    void testToStringJSON() throws Exception {
        String expected = "{" +
                "\"streetNumber\":\"3/24\"," +
                "\"streetName\":\"Ilam Road\"," +
                "\"city\":\"Christchurch\"," +
                "\"region\":\"Canterbury\"," +
                "\"country\":\"New Zealand\"," +
                "\"postcode\":\"90210\"," +
                "\"suburb\":\"Ilam\"" +
                "}";
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        Assertions.assertEquals(expected, address.toString());
    }

    @Test
    void testToAddressWithJsonFormat() throws Exception {
        String string = "{" +
                "\"streetNumber\":\"3/24\"," +
                "\"streetName\":\"Ilam Road\"," +
                "\"city\":\"Christchurch\"," +
                "\"region\":\"Canterbury\"," +
                "\"country\":\"New Zealand\"," +
                "\"postcode\":\"90210\"," +
                "\"suburb\":\"Ilam\"" +
                "}";
        Address address = Address.toAddress(string);

       Assertions.assertEquals("3/24", address.getStreetNumber());
       Assertions.assertEquals("Ilam Road", address.getStreetName());
       Assertions.assertEquals("Christchurch", address.getCity());
       Assertions.assertEquals("Canterbury", address.getRegion());
       Assertions.assertEquals("New Zealand", address.getCountry());
       Assertions.assertEquals("90210", address.getPostcode());
       Assertions.assertEquals("Ilam", address.getSuburb());
    }

    @Test
    void testToAddressWithoutJsonFormat() throws Exception {
        String string = "{" +
                "\"streetNumber\":\"3/24\"," +
                "\"streetName\":\"Ilam Road\"," +
                "\"city\":\"Christchurch\"," +
                "\"region\":\"Canterbury\"," +
                "\"country\":\"New Zealand\"," +
                "\"postcode\":\"90210\"," +
                "\"suburb\":\"Ilam\"" +
                "}";
        Address address = Address.toAddress(string);


       Assertions.assertEquals("3/24", address.getStreetNumber());
       Assertions.assertEquals("Ilam Road", address.getStreetName());
       Assertions.assertEquals("Christchurch", address.getCity());
       Assertions.assertEquals("Canterbury", address.getRegion());
       Assertions.assertEquals("New Zealand", address.getCountry());
       Assertions.assertEquals("90210", address.getPostcode());
       Assertions.assertEquals("Ilam", address.getSuburb());
    }

    /**
     * Test to see whether an address is successfully created when supplying the sample data address.
     */
    @Test
    void isAddressSuccessfullyCreatedWhenAddressDataContainsSampleDataTest() throws IllegalAddressArgumentException {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        Assertions.assertNotNull(address);
    }

    /**
     * Test to see whether an address is successfully created when only supplying the required fields (country).
     */
    @Test
    void isAddressSuccessfullyCreatedWhenOnlyRequiredFieldsSuppliedTest() throws IllegalAddressArgumentException {
        address = new Address(
                "",
                "",
                "",
                "",
                "New Zealand",
                "",
                ""
        );
        Assertions.assertNotNull(address);
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * the length of street number greater than the max length.
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenStreetNumberLengthGreaterThanMaxLengthTest() {
        String string = "2";
        String streetNumber = string.repeat(260); //maxLength = 255

        try {
            address = new Address(
                    streetNumber,
                    "Ilam Road",
                    "Christchurch",
                    "Canterbury",
                    "New Zealand",
                    "90210",
                    "Ilam"
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid street number", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * the length of street name greater than the max length.
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenStreetNameLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String streetName = string.repeat(260); //maxLength = 255

        try {
            address = new Address(
                    "3/24",
                    streetName,
                    "Christchurch",
                    "Canterbury",
                    "New Zealand",
                    "90210",
                    "Ilam"
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid street name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * the length of city greater than the max length.
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenCityLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String city = string.repeat(260); //maxLength = 255

        try {
            address = new Address(
                    "3/24",
                    "Ilam Road",
                    city,
                    "Canterbury",
                    "New Zealand",
                    "90210",
                    "Ilam"
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid city", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * the length of region greater than the max length.
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenRegionLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String region = string.repeat(260); //maxLength = 255

        try {
            address = new Address(
                    "3/24",
                    "Ilam Road",
                    "Christchurch",
                    region,
                    "New Zealand",
                    "90210",
                    "Ilam"
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid region", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * country as an empty field (check for min length).
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenCountryIsEmpty() {
        try {
            address = new Address(
                    "3/24",
                    "Ilam Road",
                    "Christchurch",
                    "Canterbury",
                    "",
                    "90210",
                    "Ilam"
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid country", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * the length of country greater than the max length.
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenCountryLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String country = string.repeat(260); //maxLength = 255

        try {
            address = new Address(
                    "3/24",
                    "Ilam Road",
                    "Christchurch",
                    "Canterbury",
                    country,
                    "90210",
                    "Ilam"
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid country", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * the length of postcode greater than the max length.
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenPostcodeLengthGreaterThanMaxLengthTest() {
        String string = "1";
        String postcode = string.repeat(260); //maxLength = 255

        try {
            address = new Address(
                    "3/24",
                    "Ilam Road",
                    "Christchurch",
                    "Canterbury",
                    "New Zealand",
                    postcode,
                    "Ilam"
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid postcode", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalAddressArgumentException is thrown when trying to create an address with
     * the length of suburb greater than the max length.
     */
    @Test
    void isIllegalAddressArgumentExceptionThrownWhenSuburbLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String suburb = string.repeat(260); //maxLength = 255

        try {
            address = new Address(
                    "3/24",
                    "Ilam Road",
                    "Christchurch",
                    "Canterbury",
                    "New Zealand",
                    "90210",
                    suburb
            );
        } catch (IllegalAddressArgumentException e) {
           Assertions.assertEquals("Invalid suburb", e.getMessage());
        }
    }

}
