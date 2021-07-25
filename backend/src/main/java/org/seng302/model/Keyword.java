package org.seng302.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.NoArgsConstructor;
import org.seng302.exceptions.IllegalKeywordArgumentException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for keywords used in the marketplace.
 * A marketplace can have many keywords.
 */
@Embeddable
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class Keyword {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @JsonBackReference
    @ManyToMany(mappedBy = "keywords", fetch = FetchType.LAZY)
    private List<MarketplaceCard> cards = new ArrayList<>();

    @OneToOne(mappedBy = "keyword")
    private KeywordNotification keywordNotification;

    // Values need for validation.
    private static final Integer NAME_MIN_LENGTH = 2;
    private static final Integer NAME_MAX_LENGTH = 20;

    /**
     * Marketplace Keyword constructor.
     * It validates the name of when constructed.
     * @throws IllegalKeywordArgumentException Validation exception thrown when a new keyword does not contain valid info.
     */
    public Keyword(String name, LocalDateTime created, MarketplaceCard card) throws IllegalKeywordArgumentException {
        if (!isValidName(name)) {
            throw new IllegalKeywordArgumentException("Invalid name");
        }
        this.name = name;
        this.created = created;
    }

    public Keyword(String name, LocalDateTime created) throws IllegalKeywordArgumentException {
        if (!isValidName(name)) {
            throw new IllegalKeywordArgumentException("Invalid name");
        }
        this.name = name;
        this.created = created;
    }

    /**
     * Retrieve the autoincrement id for keyword.
     * @return the id for keyword.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the keyword.
     * @param id the id to be changed to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieve the name of the keyword.
     * @return the name of the keyword.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the keyword
     * @param name the name to be changed to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the time the keyword was created.
     * @return created the time the keyword was created.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Set the time the keyword was created.
     * @param created the time to be changed to.
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Adds a new card to the list of cards that contain this keyword.
     * Also adds this keyword to the list of keywords for the corresponding card.
     * @param card A card which contains this keyword.
     */
    public void addCard(MarketplaceCard card) {
        this.cards.add(card);
        card.addKeyword(this);
    }

    /**
     * Removes a card from the list of cards that contain this keyword.
     * Also removes this keyword from the list of keywords for the corresponding card.
     * @param card A card that used to contain this keyword (keyword to be removed).
     */
    public void removeCard(MarketplaceCard card) {
        int cardId = card.getId();
        for (int i = 0; i < cards.size(); i++){
            if (cards.get(i).getId() == cardId){
                this.cards.remove(i);
            }
        }
        card.removeKeyword(this);
    }

    /**
     * Retrieve the notification for this keyword.
     * @return a notification for this keyword.
     */
    public KeywordNotification getKeywordNotification() {
        return keywordNotification;
    }

    /**
     * Set the notification for this keyword to a new one.
     * @param keywordNotification a notification for a keyword.
     */
    public void setKeywordNotification(KeywordNotification keywordNotification) {
        this.keywordNotification = keywordNotification;
    }

    /**
     * Retrieve the list of cards that contain this keyword.
     * @return cards a list of cards that contain this keyword.
     */
    public List<MarketplaceCard> getCards() {
        return cards;
    }

    /**
     * Checks to see whether keyword name is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param name The keyword name to be checked.
     * @return true when the keyword name is valid
     */
    private boolean isValidName(String name) {
        return (name.length() >= NAME_MIN_LENGTH) && (name.length() <= NAME_MAX_LENGTH);
    }

}
