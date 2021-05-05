/**
 * Summary. This file contains the definition for the AddressValidation.
 *
 * Description. This file contains the defintion for the AddressValidation.
 *
 * @link   team-400/src/main/java/org/seng302/validation/AddressValidation
 * @file   This file contains the definition for AddressValidation.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.validation;

public class AddressValidation {

    // Values need for validation.
    private static final Integer STREET_NUMBER_MIN_LENGTH = 0;
    private static final Integer STREET_NUMBER_MAX_LENGTH = 255;

    private static final Integer STREET_NAME_MIN_LENGTH = 0;
    private static final Integer STREET_NAME_MAX_LENGTH = 255;

    private static final Integer CITY_MIN_LENGTH = 0;
    private static final Integer CITY_MAX_LENGTH = 255;

    private static final Integer REGION_MIN_LENGTH = 0;
    private static final Integer REGION_MAX_LENGTH = 255;

    private static final Integer COUNTRY_MIN_LENGTH = 1;
    private static final Integer COUNTRY_MAX_LENGTH = 255;

    private static final Integer POSTCODE_MIN_LENGTH = 0;
    private static final Integer POSTCODE_MAX_LENGTH = 255;

    /**
     * Checks to see whether street number is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param streetNumber The street number to be checked.
     * @return true when the street number is within its range of length constraints.
     */
    public static boolean isValidStreetNumber(String streetNumber) {
        return (streetNumber.length() >= STREET_NUMBER_MIN_LENGTH) && (streetNumber.length() <= STREET_NUMBER_MAX_LENGTH);
    }

    /**
     * Checks to see whether street name is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param streetName The street name to be checked.
     * @return true when the street name is within its range of length constraints.
     */
    public static boolean isValidStreetName(String streetName) {
        return (streetName.length() >= STREET_NAME_MIN_LENGTH) && (streetName.length() <= STREET_NAME_MAX_LENGTH);
    }

    /**
     * Checks to see whether city is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param city The city to be checked.
     * @return true when the city is within its range of length constraints.
     */
    public static boolean isValidCity(String city) {
        return (city.length() >= CITY_MIN_LENGTH) && (city.length() <= CITY_MAX_LENGTH);
    }

    /**
     * Checks to see whether region is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param region The region to be checked.
     * @return true when the region is within its range of length constraints.
     */
    public static boolean isValidRegion(String region) {
        return (region.length() >= REGION_MIN_LENGTH) && (region.length() <= REGION_MAX_LENGTH);
    }

    /**
     * Checks to see whether country is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param country The country to be checked.
     * @return true when the country is within its range of length constraints.
     */
    public static boolean isValidCountry(String country) {
        return (country.length() >= COUNTRY_MIN_LENGTH) && (country.length() <= COUNTRY_MAX_LENGTH);
    }

    /**
     * Checks to see whether postcode is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param postcode The postcode to be checked.
     * @return true when the postcode is within its range of length constraints.
     */
    public static boolean isValidPostcode(String postcode) {
        return (postcode.length() >= POSTCODE_MIN_LENGTH) && (postcode.length() <= POSTCODE_MAX_LENGTH);
    }


}
