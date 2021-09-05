/**
 * Summary. This file contains the definition for the MarketplaceConversationIdPayload.
 *
 * Description. This file contains the definition for the MarketplaceConversationIdPayload.
 *
 * @link   team-400/src/main/java/org/seng302/view/outgoing/MarketplaceConversationIdPayload
 * @file   This file contains the definition for MarketplaceConversationIdPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

/**
 * Payload for the marketplace conversation ID.
 * Returned when new marketplace conversation is created.
 */
public class MarketplaceConversationIdPayload {
    private int conversationId;

    public MarketplaceConversationIdPayload(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public MarketplaceConversationIdPayload() {
    }
}

