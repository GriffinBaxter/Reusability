package org.seng302.validation;

import org.seng302.business.Business;
import org.seng302.business.BusinessType;

import java.util.List;

public class BusinessValidation {

    // Values need for validation.
    private static final Integer NAME_MIN_LENGTH = 1;
    private static final Integer NAME_MAX_LENGTH = 100;

    private static final Integer DESCRIPTION_MIN_LENGTH = 0;
    private static final Integer DESCRIPTION_MAX_LENGTH = 600;

    /**
     * Checks to see whether business name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param businessName The business name to be checked.
     * @return true when the business name is valid
     */
    public static boolean isValidName(String businessName) {
        return (businessName.length() >= NAME_MIN_LENGTH) &&
                (businessName.length() <= NAME_MAX_LENGTH) &&
                (businessName.matches("^[a-zA-Z0-9 '#,.&()-]+$"));
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
                }
            }
        }
        return flag;
    }

}
