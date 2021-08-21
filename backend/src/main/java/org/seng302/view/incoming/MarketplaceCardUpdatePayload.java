package org.seng302.view.incoming;

import org.seng302.model.enums.Section;

import java.util.List;

public class MarketplaceCardUpdatePayload {
    private String title;
    private String description;
    private List<Integer> keywordIds;

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the MarketplaceCardCreationPayload.
     */
    @Override
    public String toString() {
        return "{\"title\":\"" + title + "\"" +
                ",\"description\":\"" + description + "\"" +
                ",\"keywordIds\":" + keywordIds  +
                "}";
    }

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

}
