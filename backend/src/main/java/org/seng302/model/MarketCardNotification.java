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

    @ManyToOne(targetEntity = MarketplaceCard.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "market_card_id", insertable = false, updatable = false)
    private MarketplaceCard marketplaceCard;

    @Column(name = "market_card_id")
    private Integer marketCardId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    /**
     * @param userId      id of the receiver
     * @param description description
     * @param created     time created
     */
    public MarketCardNotification(Integer userId,
                                  MarketplaceCard marketplaceCard,
                                  String description,
                                  LocalDateTime created) {
        this.userId = userId;
        this.marketplaceCard = marketplaceCard;
        this.marketCardId = marketplaceCard != null ? marketplaceCard.getId() : null;
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

    /**
     * To convert MarketCardNotification to MarketCardNotificationPayload form.
     *
     * @return MarketCardNotification Payload
     */
    public MarketCardNotificationPayload toMarketCardNotificationPayload() throws Exception {
        return new MarketCardNotificationPayload(id,
                description,
                created,
                marketplaceCard.toMarketplaceCardPayload());
    }

    /**
     * Override the toString method for debugging purposes.
     *
     * @return a string representing the market card notification.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"userId\":\"" + userId + "\"" +
                ",\"marketCardId\":\"" + marketCardId + "\"" +
                ",\"description\":\"" + description + "\"" +
                ",\"created\":\"" + created + "\"" +
                "}";
    }
}
