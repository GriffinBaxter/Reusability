package org.seng302.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private List<HasReadKeywordNotification> readKeywordNotifications;

    //TODO
    public KeywordNotification(String description) {
        this.description = description;
    }

    /**
     * Get the id of the keyword notification.
     * @return the id of the keyword notification.
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    public List<HasReadKeywordNotification> getReadKeywordNotifications() {
        return readKeywordNotifications;
    }

    public void setReadKeywordNotifications(List<HasReadKeywordNotification> readKeywordNotifications) {
        this.readKeywordNotifications = readKeywordNotifications;
    }
}
