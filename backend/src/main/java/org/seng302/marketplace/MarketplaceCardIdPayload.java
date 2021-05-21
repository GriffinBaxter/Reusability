package org.seng302.marketplace;

/**
 * Payload for the Card ID.
 * Returned when new card is created.
 */
public class MarketplaceCardIdPayload {
    private int cardId;

    public MarketplaceCardIdPayload(int cardId) {
        this.cardId = cardId;
    }

    public int getCardId() {
        return cardId;
    }

    public MarketplaceCardIdPayload() {
    }
}
