package org.seng302.validation;

/**
 * Class for methods to validate product fields
 */
public class ProductValidation {

    // Values need for validation.
    private static final Integer PRODUCT_ID_MIN_LENGTH = 3;
    private static final Integer PRODUCT_ID_MAX_LENGTH = 15;

    private static final Integer NAME_MIN_LENGTH = 1;
    private static final Integer NAME_MAX_LENGTH = 100;

    private static final Integer DESCRIPTION_MIN_LENGTH = 0;
    private static final Integer DESCRIPTION_MAX_LENGTH = 600;

    private static final Integer MANUFACTURER_MIN_LENGTH = 0;
    private static final Integer MANUFACTURER_MAX_LENGTH = 100;

    private static final Double RECOMMENDED_RETAIL_PRICE_MINIMUM = 0.0;
    private static final Double RECOMMENDED_RETAIL_PRICE_MAXIMUM = Double.POSITIVE_INFINITY;

    /**
     * Checks to see whether a product ID is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param productId The product ID to be checked.
     * @return true when the product ID is valid
     */
    public static boolean isValidProductId(String productId) {
        return (productId.length() >= PRODUCT_ID_MIN_LENGTH) &&
                (productId.length() <= PRODUCT_ID_MAX_LENGTH) &&
                (productId.matches("^[A-Z0-9-]+$"));
    }

    /**
     * Checks to see whether a product name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param productName The product name to be checked.
     * @return true when the product name is valid
     */
    public static boolean isValidName(String productName) {
        return (productName.length() >= NAME_MIN_LENGTH) &&
                (productName.length() <= NAME_MAX_LENGTH) &&
                (productName.matches("^[a-zA-Z0-9 '#,.&()-]+$"));
    }

    /**
     * Checks to see whether a description is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    public static boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) && (description.length() <= DESCRIPTION_MAX_LENGTH);
    }

    /**
     * Checks to see whether a manufacturer name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param manufacturer The manufacturer name to be checked.
     * @return true when the manufacturer name is valid
     */
    public static boolean isValidManufacturer(String manufacturer) {
        return (manufacturer.length() >= MANUFACTURER_MIN_LENGTH) &&
                (manufacturer.length() <= MANUFACTURER_MAX_LENGTH) &&
                (manufacturer.matches("^[a-zA-Z0-9 '#,.&()-]*$"));
    }

    /**
     * Checks to see whether a recommendedRetailPrice is valid based on its constratins.
     * This method can be updated in the future if there is additional constraints.
     * @param recommendedRetailPrice Recommended retail price to be checked.
     * @return true when the recommendedRetailPrice is valid. Otherwise false.
     */
    public static boolean isValidRecommendeRetailPrice(Double recommendedRetailPrice) {
        return RECOMMENDED_RETAIL_PRICE_MINIMUM <= recommendedRetailPrice && recommendedRetailPrice <= RECOMMENDED_RETAIL_PRICE_MAXIMUM;
    }
}
