package org.seng302.business.inventoryItem;

import org.seng302.business.product.ProductPayload;

public class InventoryItemPayload {

    private Integer id;
    private ProductPayload product;
    private Integer quantity;
    private double pricePerItem;
    private double totalPrice;
    private String manufactured;
    private String sellBy;
    private String bestBefore;
    private String expires;

    /**
     * Constructor for inventory payloads.
     *
     * @param id           Inventory id
     * @param product      Product payload
     * @param quantity     Number of inventory
     * @param pricePerItem Price per inventory
     * @param totalPrice   Total price
     * @param manufactured Date of manufacture
     * @param sellBy       Date of sale
     * @param bestBefore   Date of best before
     * @param expires      Expiry date
     */
    public InventoryItemPayload(Integer id,
                                ProductPayload product,
                                Integer quantity,
                                double pricePerItem,
                                double totalPrice,
                                String manufactured,
                                String sellBy,
                                String bestBefore,
                                String expires) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = totalPrice;
        this.manufactured = manufactured;
        this.sellBy = sellBy;
        this.bestBefore = bestBefore;
        this.expires = expires;
    }

    public Integer getId() {
        return id;
    }

    public ProductPayload getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public double getPricePerItem() {
        return pricePerItem;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getManufactured() {
        return manufactured;
    }

    public String getSellBy() {
        return sellBy;
    }

    public String getBestBefore() {
        return bestBefore;
    }

    public String getExpires() {
        return expires;
    }
}
