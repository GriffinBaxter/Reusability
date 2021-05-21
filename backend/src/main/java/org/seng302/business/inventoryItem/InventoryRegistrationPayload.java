/**
 * Summary. This file contains the definition for the InventoryItemRegistrationPayload.
 *
 * Description. This file contains the defintion for the InventoryItemRegistrationPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/inventoryItem/InventoryItemRegistrationPayload
 * @file   This file contains the definition for InventoryItemRegistrationPayload.
 * @author team-400.
 * @since  5.5.2021
 */
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
        return pricePerItem;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getManufactured() {
        return manufactured;
    }

    public LocalDate getSellBy() {
        return sellBy;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    public LocalDate getExpires() {
        return expires;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPricePerItem(Double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setManufactured(LocalDate manufactured) {
        this.manufactured = manufactured;
    }

    public void setSellBy(LocalDate sellBy) {
        this.sellBy = sellBy;
    }

    public void setBestBefore(LocalDate bestBefore) {
        this.bestBefore = bestBefore;
    }

    public void setExpires(LocalDate expires) {
        this.expires = expires;
    }
}

