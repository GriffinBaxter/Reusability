package org.seng302.view.outgoing;

import org.seng302.model.ProductId;

/**
 * Sold Listing Payload
 *
 * Sold listing payload to send to the API (outgoing).
 */
public class SoldListingPayload {

    private Integer id;
    private UserPayloadSecure user;
    private ProductId productId;
    private Integer quantity;
    private Double price;
    private Integer totalBookmarks;
    private String listingDate;
    private String saleDate;

    public SoldListingPayload(Integer id,
                              UserPayloadSecure user,
                              ProductId productId,
                              Integer quantity,
                              Double price,
                              Integer totalBookmarks,
                              String listingDate,
                              String saleDate
    ) {
        this.id = id;
        this.user = user;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.totalBookmarks = totalBookmarks;
        this.listingDate = listingDate;
        this.saleDate = saleDate;
    }
}
