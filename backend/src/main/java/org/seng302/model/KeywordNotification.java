package org.seng302.model;

import lombok.NoArgsConstructor;
import org.seng302.exceptions.IllegalKeywordNotificationArgumentException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for keyword notifications.
 * Administrators (GAA/DGAA) should receive keyword notifications when
 * a new keyword is created.
 */
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class KeywordNotification {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "description", length = 600)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "keyword_id", referencedColumnName = "id")
    private Keyword keyword;

    @OneToMany(mappedBy = "keywordNotification")
    private List<HasKeywordNotification> readKeywordNotifications = new ArrayList<>();

    // Values need for validation.
    private static final Integer DESCRIPTION_MIN_LENGTH = 2;
    private static final Integer DESCRIPTION_MAX_LENGTH = 600;

    /**
     * A constructor used to created a new KeywordNotification.
     * @param description a description for the notification. Has a min length of 2 and max length of 600.
     * @param created the date and time the keyword notification was created.
     * @param keyword the keyword the notification is for.
     * @throws IllegalKeywordNotificationArgumentException thrown if a required field is not supplied or is invalid.
     */
    public KeywordNotification(String description, LocalDateTime created, Keyword keyword) throws IllegalKeywordNotificationArgumentException {
        if (!(isValidDescription(description))) {
            throw new IllegalKeywordNotificationArgumentException("Invalid description");
        }
        if (created == null) {
            throw new IllegalKeywordNotificationArgumentException("Created date is required");
        }
        if (keyword == null) {
            throw new IllegalKeywordNotificationArgumentException("Keyword is required");
        }
        this.description = description;
        this.created = created;
        this.keyword = keyword;
    }

    /**
     * Get the id of the keyword notification.
     * @return the id of the keyword notification.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the keyword notification.
     * @param id the id to be set to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the description of the keyword notification.
     * @return the description of the keyword notification.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the keyword notification.
     * @param description the description to be set to.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the date and time the keyword notification was created.
     * @return the date and time the keyword notification was created.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Change the time the keyword notification was created.
     * In reality this method should never be called, as you
     * shouldn't need to change the time a keyword notification was created.
     * @param created the date and time the keyword notification was created.
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Get the keyword corresponding to the notification.
     * @return a keyword that the notification has been created for.
     */
    public Keyword getKeyword() {
        return keyword;
    }

    /**
     * Change the keyword for the corresponding notification.
     * @param keyword the new keyword.
     */
    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    /**
     * Get the list of HasReadKeywordNotifications, which can be used to determine if a user
     * has read a notification.
     * @return list of HaHasReadKeywordNotifications, which can be used to determine if a user
     * has read a notification.
     */
    public List<HasKeywordNotification> getReadKeywordNotifications() {
        return readKeywordNotifications;
    }

    /**
     * Set the list of HasReadKeywordNotifications, which can be used to determine if a user
     * has read a notification.
     * @param readKeywordNotifications a new list of HasReadKeywordNotifications, to be set to.
     */
    public void setReadKeywordNotifications(List<HasKeywordNotification> readKeywordNotifications) {
        this.readKeywordNotifications = readKeywordNotifications;
    }

    /*---------------------------------------------------Validation---------------------------------------------------*/

    /**
     * Checks to see whether a keyword notification's description is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    private boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) &&
                (description.length() <= DESCRIPTION_MAX_LENGTH);
    }
}
