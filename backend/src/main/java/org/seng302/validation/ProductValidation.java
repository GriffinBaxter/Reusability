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

    /**
     * Checks to see whether product ID is valid based on its constraints.
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
     * Checks to see whether product name is valid based on its constraints.
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
     * Checks to see whether description is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    public static boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) && (description.length() <= DESCRIPTION_MAX_LENGTH);
    }
}
