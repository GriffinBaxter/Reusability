package org.seng302.business.listing;

import java.time.LocalDateTime;

/*
 * ListingCreationPayload class
 */
public class ListingCreationPayload {
    private String inventoryItemId;
    private Integer quantity;
    private Double price;
    private String moreInfo;
    private LocalDateTime closes;

    public ListingCreationPayload() {
    }

    public ListingCreationPayload(String inventoryItemId, Integer quantity, Double price, String moreInfo, LocalDateTime closes) {
        this.inventoryItemId = inventoryItemId;
        this.quantity = quantity;
        this.price = price;
        this.moreInfo = moreInfo;
        this.closes = closes;
    }

    public String getInventoryItemId() {
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
