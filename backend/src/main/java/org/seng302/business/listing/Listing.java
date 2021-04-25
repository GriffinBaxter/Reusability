package org.seng302.business.listing;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Class for sale listings
 */
@Data
@NoArgsConstructor
@Entity
public class Listing {
    @Id // this field (attribute) is the table primary key
    @GeneratedValue//(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    // InventoryItem

    @Column (name = "quantity", nullable = false)
    private int quantity;

    @Column (name = "price", nullable = false)
    private double price;

    @Column (name = "moreInfo")
    private String moreInfo;

    @Column (name = "created")
    private LocalDateTime created;

    @Column (name = "closes")
    private LocalDateTime closes;

    /**
     * Create a new listing.
     * @param quantity the quantity of an item being listed.
     * @param price the price of the listing
     * @param moreInfo the business may wish to include extra info about the listing e.g. willing to consider near offers.
     * @param created the date the listing was created.
     * @param closes the date the listing closes, defaults to expiry date.
     */
    public Listing(int quantity, double price, String moreInfo, LocalDateTime created, LocalDateTime closes) {
        this.quantity = quantity;
        this.price = price;
        this.moreInfo = moreInfo;
        this.created = created;
        this.closes = closes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getCloses() {
        return closes;
    }

    public void setCloses(LocalDateTime closes) {
        this.closes = closes;
    }
}
