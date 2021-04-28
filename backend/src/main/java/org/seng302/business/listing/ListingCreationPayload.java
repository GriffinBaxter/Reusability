package org.seng302.business.listing;

import java.time.LocalDateTime;

public class ListingCreationPayload {
    private String inventoryItemId;
    private Integer quantity;
    private Double price;
    private String moreInfo;
    private LocalDateTime closes;

    public String getInventoryId() {
        return inventoryItemId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public Double getPrice() {
        return price;
    }
    public String getMoreInfo() {
        return moreInfo;
    }
    public LocalDateTime getCloses() {
        return closes;
    }
}