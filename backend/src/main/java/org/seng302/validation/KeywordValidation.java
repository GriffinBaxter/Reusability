package org.seng302.validation;

/**
 * This class is used for validating the data when creating a keyword.
 */
public class KeywordValidation {

    // Values need for validation.
    private static final Integer NAME_MIN_LENGTH = 2;
    private static final Integer NAME_MAX_LENGTH = 20;

    /**
     * Checks to see whether keyword name is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param name The keyword name to be checked.
     * @return true when the keyword name is valid
     */
    public static boolean isValidName(String name) {
        return (name.length() >= NAME_MIN_LENGTH) && (name.length() <= NAME_MAX_LENGTH);
    }

}
