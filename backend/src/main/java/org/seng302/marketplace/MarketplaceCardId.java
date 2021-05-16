package org.seng302.marketplace;

import java.io.Serializable;

/**
 * Class for the composite primary key of Card
 */
public class MarketplaceCardId implements Serializable {

    private int id;

    private Integer creatorId;

    public MarketplaceCardId(){

    }

    /**
     * Constructor for MarketplaceCardId.
     *
     * @param id Marketplace card id.
     * @param creatorId ID of the user the card belongs to.
     */
    public MarketplaceCardId(int id, Integer creatorId) {
        this.id = id;
        this.creatorId = creatorId;
    }

}


