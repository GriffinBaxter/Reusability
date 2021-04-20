package org.seng302.business.inventoryItem;

import org.seng302.business.product.ProductPayload;

import java.time.LocalDate;

public class InventoryRegistrationPayload {

    private String productId;
    private Integer quantity;
    private Double pricePerItem;
    private Double totalPrice;
    private LocalDate manufactured;
    private LocalDate sellBy;
    private LocalDate bestBefore;
    private LocalDate expires;

    //getter
    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPricePerItem() {
        return pricePerItem == null ? null : pricePerItem;
    }

    public Double getTotalPrice() {
        return totalPrice == null ? null : totalPrice;
    }

    public LocalDate getManufactured() {
        return manufactured == null ? null : manufactured;
    }

    public LocalDate getSellBy() {
        return sellBy == null ? null : sellBy;
    }

    public LocalDate getBestBefore() {
        return bestBefore == null ? null : bestBefore;
    }

    public LocalDate getExpires() {
        return expires;
    }
}

