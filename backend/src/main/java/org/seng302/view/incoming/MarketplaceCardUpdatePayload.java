package org.seng302.view.incoming;

import org.seng302.model.enums.Section;

import java.util.List;

public class MarketplaceCardUpdatePayload {
    private Integer creatorId;
    private String section;
    private String title;
    private String description;
    private List<Integer> keywords;

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

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer id) {this.creatorId = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {this.title = title; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {this.description = description; }

    public List<Integer> getKeywords() {
        return keywords;
    }

    public Section getSection() {
        return toSectionENUM(section);
    }

    public void setSection(Section section) {
        if (section == Section.FORSALE) { this.section = "FORSALE"; }
        else if (section == Section.EXCHANGE) { this.section = "EXCHANGE"; }
        else if (section == Section.WANTED) { this.section = "WANTED"; }
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

}
