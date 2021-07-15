package org.seng302.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.model.enums.Section;
import org.seng302.view.outgoing.KeywordPayload;
import org.seng302.view.outgoing.MarketplaceCardPayload;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for a marketplace card.
 * Marketplace cards can have many keywords.
 */
@Embeddable
@NoArgsConstructor  // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity             // declare this class as a JPA entity (that can be mapped to a SQL table)
public class MarketplaceCard {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER) //EAGER to allow access to this attribute outside of a context of an open hibernate session (for loading initial data SQL script)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private User creator;

    @Column(name = "creator_id")
    private Integer creatorId;

    @Enumerated(EnumType.STRING)
    private Section section;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "display_period_end", nullable = false)
    private LocalDateTime displayPeriodEnd;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @JsonManagedReference
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "card_keywords",
            joinColumns = { @JoinColumn(name = "card_id") },
            inverseJoinColumns = { @JoinColumn(name = "keyword_id") })
    private List<Keyword> keywords = new ArrayList<>();

    // Values needed for validation.
    private static final Integer TITLE_MIN_LENGTH = 2;
    private static final Integer TITLE_MAX_LENGTH = 70;

    private static final Integer DESCRIPTION_MIN_LENGTH = 0;
    private static final Integer DESCRIPTION_MAX_LENGTH = 500;

    /**
     * Marketplace card constructor
     * @param creatorId the id of the user who created this card.
     * @param creator the creator of this card.
     * @param section the section the card appears in e.g FORSALE, WANTED, EXCHANGE.
     * @param created the date and time the card was created.
     * @param title the title of the card.
     * @param description the description of the card.
     * @throws IllegalMarketplaceCardArgumentException Validation exception
     */
    public MarketplaceCard(
            int creatorId,
            User creator,
            Section section,
            LocalDateTime created,
            String title,
            String description
    ) throws IllegalMarketplaceCardArgumentException {
        if (!isValidTitle(title)) {
            throw new IllegalMarketplaceCardArgumentException("Invalid title");
        }
        if (!isValidDescription(description)) {
            throw new IllegalMarketplaceCardArgumentException("Invalid description");
        }

        this.creatorId = creatorId;
        this.creator = creator;
        this.section = section;
        this.created = created;
        this.displayPeriodEnd = created.plusWeeks(1);
        this.title = title;
        this.description = (description.equals("")) ? null : description;
    }

    public int getId() {
        return id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Get the User who created this card.
     * @return creator the user that created this card.
     */
    public User getCreator() { return creator; }

    public Section getSection() {
        return section;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getDisplayPeriodEnd() {
        return displayPeriodEnd;
    }

    public String getTitle() { return title; }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Change the user who created this card.
     * @param creator the user that created this card.
     */
    public void serCreator(User creator) { this.creator = creator; }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setDisplayPeriodEnd(LocalDateTime displayPeriodEnd) {
        this.displayPeriodEnd = displayPeriodEnd;
    }

    /**
     * Checks the title is valid and then sets it
     * @param title title to be set
     * @throws IllegalMarketplaceCardArgumentException if title is invalid
     */
    public void setTitle(String title) throws IllegalMarketplaceCardArgumentException {
        if (isValidTitle(title)) {
            this.title = title;
        } else {
            throw new IllegalMarketplaceCardArgumentException("Invalid Title");
        }
    }

    /**
     * Checks the description is valid and then sets it
     * @param description description to be set
     * @throws IllegalMarketplaceCardArgumentException if description is invalid
     */
    public void setDescription(String description) throws IllegalMarketplaceCardArgumentException {
        if (isValidDescription(description)) {
            this.description = description;
        } else {
            throw new IllegalMarketplaceCardArgumentException("Invalid Description");
        }
    }

    /**
     * Returns a list of keywords that the card contains.
     * @return keywords a list of keyword entities.
     */
    public List<Keyword> getKeywords() {
        return keywords;
    }

    /**
     * Add a keyword to the list of keywords for this card.
     * Called when a new keyword is created.
     * @param keyword a keyword that is to be added to this card.
     */
    public void addKeyword(Keyword keyword) {
        keywords.add(keyword);
    }

    /**
     * Remove a keyword from the list of keywords for this card.
     * @param keyword a keyword that is to be removed from this card.
     */
    public void removeKeyword(Keyword keyword) {
        int keywordId = keyword.getId();
        for (int i = 0; i < keywords.size(); i++){
            if (keywords.get(i).getId() == keywordId){
                this.keywords.remove(i);
            }
        }
    }

    /**
     * Removes all Keywords from the Marketplace Card
     */
    public void removeAllKeywords() {
        while (0 > keywords.size()) {
            this.keywords.remove(0);
        }
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the marketplace card.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"creatorId\":\"" + creatorId + "\"" +
                ",\"creator\":\"" + creator + "\"" +
                ",\"section\":\"" + section + "\"" +
                ",\"created\":\"" + created + "\"" +
                ",\"title\":\"" + title + "\"" +
                ",\"description\":\"" + description + "\"" +
                //TODO add keyword stuff
                "}";
    }


    /**
     * Convert a Marketplace object into a MarketplacePayload
     * @return a MarketplacePayload object
     */
    public MarketplaceCardPayload toMarketplaceCardPayload() throws Exception {
        return new MarketplaceCardPayload(
                id,
                creator.toUserPayloadSecure(),
                section,
                created,
                displayPeriodEnd,
                title,
                description,
                toKeywordPayloads(keywords)
        );
    }

    /**
     * Converts the keywords belonging to a card to Keyword Payloads (needed by GET requests).
     * @param keywords the list of keywords belonging to the card.
     * @return a list of keywords converted to keyword payloads.
     */
    public List<KeywordPayload> toKeywordPayloads(List<Keyword> keywords) {
        List<KeywordPayload> keywordPayloads = new ArrayList<>();
        for (Keyword keyword: keywords) {
            KeywordPayload keywordPayload = new KeywordPayload(keyword.getId(), keyword.getName(), keyword.getCreated());
            keywordPayloads.add(keywordPayload);
        }
        return keywordPayloads;
    }

    /**
     * Checks to see whether title is valid based on its constraints.
     * This method can be updated in the future if there are additional constraints.
     * @param title The title to be checked.
     * @return true when the title is valid
     */
    private boolean isValidTitle(String title) {
        return (title.length() >= TITLE_MIN_LENGTH) &&
                (title.length() <= TITLE_MAX_LENGTH);
    }

    /**
     * Checks to see whether description is valid based on its constraints.
     * This method can be updated in the future if there are additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    private boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) &&
                (description.length() <= DESCRIPTION_MAX_LENGTH);
    }

    /**
     * Extends the display period of the marketplace card by one week.
     */
    public void extendDisplayPeriod() {
        this.displayPeriodEnd = displayPeriodEnd.plusWeeks(1);
    }

}
