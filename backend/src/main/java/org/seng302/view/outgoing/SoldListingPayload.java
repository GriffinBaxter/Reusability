package org.seng302.view.outgoing;

import org.seng302.model.Product;
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

    public Integer getId() {
        return id;
    }

    public UserPayloadSecure getUser() {
        return user;
    }

    public ProductId getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getTotalBookmarks() {
        return totalBookmarks;
    }

    public String getListingDate() {
        return listingDate;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(UserPayloadSecure user) {
        this.user = user;
    }

    public void setProductId(ProductId productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTotalBookmarks(Integer totalBookmarks) {
        this.totalBookmarks = totalBookmarks;
    }

    public void setListingDate(String listingDate) {
        this.listingDate = listingDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
}
