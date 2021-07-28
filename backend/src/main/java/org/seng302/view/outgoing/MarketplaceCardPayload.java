package org.seng302.view.outgoing;

import org.seng302.model.enums.Section;

import java.time.LocalDateTime;
import java.util.List;

public class MarketplaceCardPayload {

    private Integer id;
    private UserPayloadSecure creator;
    private String section;
    private String created;
    private String displayPeriodEnd;
    private String title;
    private String description;
    private List<KeywordPayload> keywords;

    public MarketplaceCardPayload(Integer id,
                                  UserPayloadSecure creator,
                                  Section section,
                                  LocalDateTime created,
                                  LocalDateTime displayPeriodEnd,
                                  String title,
                                  String description,
                                  List<KeywordPayload> keywords) {
        this.id = id;
        this.creator = creator;
        this.section = section.toString();
        this.created = created.toString();
        this.displayPeriodEnd = displayPeriodEnd.toString();
        this.title = title;
        this.description = description;
        this.keywords = keywords;
    }

    public Integer getId() {
        return id;
    }

    public UserPayloadSecure getCreator() {
        return creator;
    }

    public String getSection() {
        return section;
    }

    public String getCreated() {
        return created;
    }

    public String getDisplayPeriodEnd() {
        return displayPeriodEnd;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<KeywordPayload> getKeywords() { return keywords; }

    /**
     * Override the toString method for debugging and testing purposes.
     * @return a string representing the marketplace card payload.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"creator\":" + creator.toString() +
                ",\"section\":\"" + section + "\"" +
                ",\"created\":\"" + created + "\"" +
                ",\"displayPeriodEnd\":\"" + displayPeriodEnd + "\"" +
                ",\"title\":\"" + title + "\"" +
                ",\"description\":\"" + description + "\"" +
                ",\"keywords\":" + keywords +
                "}";
    }
}
