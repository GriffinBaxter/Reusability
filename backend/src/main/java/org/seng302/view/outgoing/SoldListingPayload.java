/**
 * Summary. This file contains the definition for the SoldListingPayload.
 *
 * Description. This file contains the definition for the SoldListingPayload.
 *
 * @link   team-400/src/main/java/org/seng302/view/outgoing/SoldListingPayload
 * @file   This file contains the definition for SoldListingPayload.
 * @author team-400.
 * @since  7.8.2021
 */
package org.seng302.view.outgoing;

import org.seng302.model.User;

/**
 * Payload for Sold Listings to send to the frontend
 */
public class SoldListingPayload {

    private Integer id;
    private String saleDate;
    private String listingDate;
    private String productId;
    private Integer quantity;
    private Double price;
    private Integer bookmarks;
    private UserPayloadSecure customer;

    /**
     * Creates a payload for SoldListings
     * @param id Identifier number for Sold Listing
     * @param saleDate Date of sale
     * @param listingDate Date of Listing
     * @param productId Products ID
     * @param quantity Number of item brought
     * @param price Price of item brought
     * @param bookmarks Number of people that bookmarked the listing
     * @param customer The UserPayloadSecure representation of the customer
     */
    public SoldListingPayload(Integer id, String saleDate, String listingDate, String productId, Integer quantity, Double price, Integer bookmarks, UserPayloadSecure customer) {
        this.id = id;
        this.saleDate = saleDate;
        this.listingDate = listingDate;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.bookmarks = bookmarks;
        this.customer = customer;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public String getListingDate() {
        return listingDate;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getBookmarks() {
        return bookmarks;
    }

    public UserPayloadSecure getCustomer() {
        return customer;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + "," +
                "\"saleDate\":\"" + saleDate + "\"," +
                "\"listingDate\":\"" + listingDate + "\"," +
                "\"productId\":\"" + productId + "\"," +
                "\"quantity\":" + quantity + "," +
                "\"price\":" + price + "," +
                "\"bookmarks\":" + bookmarks +
                ",\"customer\":" + customer + "}";
    }
}
