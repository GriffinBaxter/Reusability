/**
 * Summary. This file contains the definition for the MarketplaceConversationMessagePayload.
 *
 * Description. This file contains the definition for the MarketplaceConversationMessagePayload.
 *
 * @link   team-400/src/main/java/org/seng302/view/incoming/MarketplaceConversationMessagePayload
 * @file   This file contains the definition for MarketplaceConversationMessagePayload.
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
    private LocalDateTime created;

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
