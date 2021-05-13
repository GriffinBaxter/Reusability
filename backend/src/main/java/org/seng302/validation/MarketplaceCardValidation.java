package org.seng302.validation;

public class MarketplaceCardValidation {

    // Values need for validation.
    private static final Integer TITLE_MIN_LENGTH = 2;
    private static final Integer TITLE_MAX_LENGTH = 70;

    private static final Integer DESCRIPTION_MIN_LENGTH = 0;
    private static final Integer DESCRIPTION_MAX_LENGTH = 700;

    /**
     * Checks to see whether title is valid based on its constraints.
     * This method can be updated in the future if there are additional constraints.
     * @param title The title to be checked.
     * @return true when the title is valid
     */
    public static boolean isValidTitle(String title) {
        return (title.length() >= TITLE_MIN_LENGTH) &&
                (title.length() <= TITLE_MAX_LENGTH);
    }

    /**
     * Checks to see whether description is valid based on its constraints.
     * This method can be updated in the future if there are additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    public static boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) &&
                (description.length() <= DESCRIPTION_MAX_LENGTH);
    }

}
