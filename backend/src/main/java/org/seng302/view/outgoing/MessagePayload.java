package org.seng302.view.outgoing;

import java.time.LocalDateTime;

/**
 * MessagePayload class, used to send message data for a single message to the front-end.
 * Usually combined in an array for a conversation.
 */
public class MessagePayload {

    private Integer senderId;
    private Integer marketplaceCardId;
    private String content;
    private String created;

    public MessagePayload(Integer senderId, Integer marketplaceCardId, String content, LocalDateTime created) {
        this.senderId = senderId;
        this.marketplaceCardId = marketplaceCardId;
        this.content = content;
        this.created = created.toString();
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getMarketplaceCardId() {
        return marketplaceCardId;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }

    /**
     * Override the toString method for debugging and testing purposes.
     * @return a string representing the marketplace card payload.
     */
    @Override
    public String toString() {
        return "{\"senderId\":" + senderId +
                ",\"marketplaceCardId\":" + marketplaceCardId  +
                ",\"content\":\"" + content + "\"" +
                ",\"created\":\"" + created + "\"" +
                "}";
    }

}
