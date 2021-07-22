package org.seng302.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

}
