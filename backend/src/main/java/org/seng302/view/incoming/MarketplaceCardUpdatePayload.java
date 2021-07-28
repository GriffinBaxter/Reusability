package org.seng302.view.incoming;

import org.seng302.model.enums.Section;

import java.util.List;

public class MarketplaceCardUpdatePayload {
    private Integer creatorId;
    private String section;
    private String title;
    private String description;
    private List<Integer> keywordIds;

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
                ",\"keywordIds\":" + keywordIds  +
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

    public void setKeywordIds(List<Integer> keywordIds) {this.keywordIds = keywordIds; }

    public List<Integer> getKeywordIds() {
        return keywordIds;
    }

    public void setSection(String section) {this.section = section; }

    public Section getSection() {
        return toSectionENUM(section);
    }

    /**
     * Check to see if a string is a valid section, if so convert the string to a Section ENUM, if not, return null
     * @param string a string representing a section.
     * @return when string is valid section return Section ENUM, otherwise return null
     */
    private Section toSectionENUM(String string){
        if (string == null) { return null; }
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
