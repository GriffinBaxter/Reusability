/**
 * Summary. This file contains the definition for the ListingValidation.
 *
 * Description. This file contains the defintion for the ListingValidation.
 *
 * @link   team-400/src/main/java/org/seng302/validation/ListingValidation
 * @file   This file contains the definition for ListingValidation.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.validation;

import org.seng302.business.inventoryItem.InventoryItem;

/**
 * Class methods to validate listing fields
 */
public class ListingValidation {

    // Values need for validation.
    private static final Integer MORE_INFO_MIN_LENGTH = 0;
    private static final Integer MORE_INFO_MAX_LENGTH = 600;

    private static final Integer MIN_QUANTITY = 0;

    /**
     * Checks to see whether more info is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param moreInfo The more info to be checked.
     * @return true when more info is valid
     */
    public static boolean isValidMoreInfo(String moreInfo) {
        return (moreInfo.length() >= MORE_INFO_MIN_LENGTH) &&
                (moreInfo.length() <= MORE_INFO_MAX_LENGTH);
    }

    /**
     * Checks to see whether quantity is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param quantity The quantity to be checked.
     * @param inventoryItem An inventoryItem which is needed to validate the quantity of a listing
     * @return true when quantity is valid
     */
    public static boolean isValidQuantity(int quantity, InventoryItem inventoryItem) {
        return (quantity > MIN_QUANTITY) &&
                ((quantity + inventoryItem.getInventoryItemQuantityListed()) <= inventoryItem.getQuantity());
    }
}
