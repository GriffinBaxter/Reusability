package org.seng302.model;

import org.seng302.view.outgoing.MarketCardNotificationPayload;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class MarketCardNotification {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "market_card_id", nullable = false)
    private Integer marketCardId;

    @ManyToOne(targetEntity = MarketplaceCard.class, fetch = FetchType.EAGER)  //EAGER to allow access to this attribute outside of a context of an open hibernate session (for loading initial data SQL script)
    @JoinColumn(name = "market_card", insertable = false, updatable = false)
    private MarketplaceCard marketplaceCard;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    /**
     * @param userId       id of the receiver
     * @param marketCardId id of the market card
     * @param description  description
     * @param created      time created
     */
    public MarketCardNotification(Integer userId,
                                  Integer marketCardId,
                                  MarketplaceCard marketplaceCard,
                                  String description,
                                  LocalDateTime created) {
        this.userId = userId;
        this.marketCardId = marketCardId;
        this.marketplaceCard = marketplaceCard;
        this.description = description;
        this.created = created;
    }

    public MarketCardNotification() {
    }

    public int getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getMarketCardId() {
        return marketCardId;
    }

    public MarketplaceCard getMarketplaceCard() {
        return marketplaceCard;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMarketCardId(Integer marketCardId) {
        this.marketCardId = marketCardId;
    }

    public void setMarketplaceCard(MarketplaceCard marketplaceCard) {
        this.marketplaceCard = marketplaceCard;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public MarketCardNotificationPayload toMarketCardNotificationPayload() throws Exception {
        return new MarketCardNotificationPayload(id,
                description,
                created,
                marketplaceCard.toMarketplaceCardPayload());
    }

}
