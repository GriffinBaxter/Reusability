package org.seng302.view.incoming;


import org.seng302.model.enums.Section;

import java.util.List;

/**
 * MarketplaceCardCreationPayload class
 * This payload is used in the POST request "/cards"
 */
public class MarketplaceCardCreationPayload {

    private Integer creatorId;
    private String section;
    private String title;
    private String description;
    private List<String> keywords;

    /**
     * Retrieve the id of the user who created the card.
     * @return the id of the user who created the card.
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * Retrieve the section of the card (For Sale etc.)
     * When this variable is passed from the frontend it is of type string.
     * Therefore it needs to be converted to a Section ENUM.
     * @return the section the card belongs to (as an ENUM)
     */
    public Section getSection() {
        return toSectionENUM(section);
    }

    /**
     * Check to see if a string is a valid section, if so convert the string to a Section ENUM, if not, return null
     * @param string a string representing a section.
     * @return when string is valid section return Section ENUM, otherwise return null
     */
    private Section toSectionENUM(String string){
        Section section = null;
        if (string.equalsIgnoreCase("FORSALE")){
            section = Section.FORSALE;
        } else if (string.equalsIgnoreCase("EXCHANGE")){
            section = Section.EXCHANGE;
        } else if (string.equalsIgnoreCase("WANTED")) {
            section = Section.WANTED;
        }
        return section;
    }

    /**
     * Retrieve the title of the created card.
     * @return the title of the card.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieve the description of the created card.
     * @return the description of the card.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieve the keywords belonging to the card.
     * @return a list of strings representing keywords belonging to card.
     */
    public List<String> getKeywords() {
        return keywords;
    }


    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the MarketplaceCardCreationPayload.
     */
    @Override
    public String toString() {
        return "{\"creatorId\":\"" + creatorId + "\"" +
                ",\"section\":\"" + section + "\"" +
                ",\"title\":\"" + title + "\"" +
                ",\"description\":\"" + description + "\"" +
                ",\"keywords\":" + keywords  +
                "}";
    }



}
