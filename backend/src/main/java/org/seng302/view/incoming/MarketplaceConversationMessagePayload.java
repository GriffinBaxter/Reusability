/**
 * Summary. This file contains the definition for the BusinessRegistrationPayload.
 *
 * Description. This file contains the definition for the BusinessRegistrationPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/BusinessRegistrationPayload
 * @file   This file contains the definition for BusinessRegistrationPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.incoming;

import java.time.LocalDateTime;

/**
 * Class for the marketplace conversation payload.
 */
public class MarketplaceConversationMessagePayload {

    private Integer senderId;
    private Integer receiverId;
    private Integer marketplaceCardId;
    private String content;
    private LocalDateTime created; //TODO: do we need this?

    public MarketplaceConversationMessagePayload(Integer senderId, Integer receiverId, Integer marketplaceCardId, String content, LocalDateTime created) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.marketplaceCardId = marketplaceCardId;
        this.content = content;
        this.created = created;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Integer getReceiverId() { return receiverId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }

    public Integer getMarketplaceCardId() { return marketplaceCardId; }
    public void setMarketplaceCardId(Integer cardId) { this.marketplaceCardId = cardId; }

}
