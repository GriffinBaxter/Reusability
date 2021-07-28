/**
 * Summary. Contains the definition for InventoryItemPayload.
 *
 * Description. The InventoryItemPayload is used for the the InvetoryResource request.
 *
 * @link   team-400/src/main/java/org/seng302/business/InventoryItemPayload
 * @file   This file contains the definition for InventoryItemPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

public class InventoryItemPayload {

    private Integer id;
    private ProductPayload product;
    private Integer quantity;
    private Double pricePerItem;
    private Double totalPrice;
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
                                Double pricePerItem,
                                Double totalPrice,
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

    public Double getPricePerItem() {
        return pricePerItem;
    }

    public Double getTotalPrice() {
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
