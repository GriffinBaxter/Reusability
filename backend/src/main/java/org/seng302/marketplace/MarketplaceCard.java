package org.seng302.marketplace;

import lombok.NoArgsConstructor;
import org.seng302.business.Business;
import org.seng302.business.product.ProductId;
import org.seng302.user.User;
import org.seng302.validation.MarketplaceCardValidation;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class for a marketplace card
 */
@Embeddable
@NoArgsConstructor  // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity             // declare this class as a JPA entity (that can be mapped to a SQL table)
@IdClass(MarketplaceCardId.class)
public class MarketplaceCard {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    private User creator;

    @Id
    @Column(name = "creator_id")
    private Integer creatorId;

    @Enumerated(EnumType.STRING)
    private Section section;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    // TODO: add keywordIds;

    /**
     * Marketplace card constructor
     * @param creatorId
     * @param creator
     * @param section
     * @param created
     * @param title
     * @param description
     * @throws Exception Validation exception
     */
    public MarketplaceCard(
            int creatorId,
            User creator,
            Section section,
            LocalDateTime created,
            String title,
            String description
    ) throws Exception {
        if (!MarketplaceCardValidation.isValidTitle(title)) {
            throw new Exception("Invalid title");
        }
        if (!MarketplaceCardValidation.isValidDescription(description)) {
            throw new Exception("Invalid description");
        }

        this.creatorId = creatorId;
        this.creator = creator;
        this.section = section;
        this.created = created;
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

    public String getTitle() {
        return title;
    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    /**
//     * Convert a Marketplace object into a MarketplacePayload
//     * @return a MarketplacePayload object
//     */
//    public MarketplaceCardPayload toMarketplaceCardPayload() throws Exception {
//    };
//
//    /**
//     * Retrieves the cards a user has created.
//     * @return marketplaceCards a list of MarketplaceCards owned/created by a user
//     */
//    public getUserMarketplaceCards(Integer userId) {
//
//    }

}