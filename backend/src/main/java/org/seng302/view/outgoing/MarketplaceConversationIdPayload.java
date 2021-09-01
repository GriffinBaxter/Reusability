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
    // TODO Why is this a business ID?
    private int businessId;

    public MarketplaceConversationIdPayload(int businessId) {
        this.businessId = businessId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public MarketplaceConversationIdPayload() {
    }
}

