/**
 * Summary. This file contains the definition for the Validation.
 *
 * Description. This file contains the defintion for the Validation.
 *
 * @link   team-400/src/main/java/org/seng302/validation/Validation
 * @file   This file contains the definition for Validation.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302;

import org.seng302.model.Business;

import java.util.List;

public class Validation {

    // Values need for validation.
    private static final Integer PRODUCT_ID_MIN_LENGTH = 3;
    private static final Integer PRODUCT_ID_MAX_LENGTH = 15;

    /**
     * Checks if a string is empty.
     * @param string String to be checked
     * @return True if the string is empty
     */
    public static boolean isEmpty(String string) {
        return string.equals("");
    }

    /**
     * Checks if a string is alpha and space.
     * @param string String to be checked
     * @return True if a given string consists of alpha characters and if it is not empty if it is not optional
     */
    public static boolean isName(String string) {
        boolean flag = true;
        String[] strings = string.split(" ");
        for (String s: strings){
            if (!s.matches("^[a-zA-Z]*$") || isEmpty(string)){
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * Checks to see whether a business already exists (i.e the business is in the business repository)
     * @param businesses A list of businesses
     * @param name Business name
     * @return true when name and address have not been used
     */
    public static boolean isNewBusiness(List<Business> businesses, String name) {
        boolean flag = true;
        if (!businesses.isEmpty()){
            for (Business business: businesses){
                if (business.getName().equals(name)){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

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



}
