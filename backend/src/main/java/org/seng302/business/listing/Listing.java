package org.seng302.business.listing;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.address.Address;
import org.seng302.business.inventoryItem.InventoryItem;
import org.seng302.business.product.Product;
import org.seng302.validation.ListingValidation;


import javax.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventoryItem_id", nullable = false)
    private InventoryItem inventoryItem;

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

    private static final Logger logger = LogManager.getLogger(Listing.class.getName());

    /**
     * Create a new listing.
     * @param quantity the quantity of an item being listed.
     * @param price the price of the listing
     * @param moreInfo the business may wish to include extra info about the listing e.g. willing to consider near offers.
     * @param created the date the listing was created.
     * @param closes the date the listing closes, defaults to expiry date.
     */
    public Listing(InventoryItem inventoryItem,
                   int quantity,
                   double price,
                   String moreInfo,
                   LocalDateTime created,
                   LocalDateTime closes
    ) throws Exception {
        if (inventoryItem == null) {
            logger.error("Listing Creation Error - Inventory item is null");
            throw new Exception("Invalid inventory item");
        }
        if (!ListingValidation.isValidQuantity(quantity, inventoryItem.getQuantity())) {
            logger.error("Listing Creation Error - Quantity {} is not valid", quantity);
            throw new Exception("Invalid quantity");
        }
        if (!ListingValidation.isValidMoreInfo(moreInfo)) {
            logger.error("Listing Creation Error - More Info {} is not valid", moreInfo);
            throw new Exception("Invalid more info");
        }
        if (created == null) {
            logger.error("Listing Creation Error - Created (Date-Time) is null");
            throw new Exception("Invalid creation date");
        }
        this.inventoryItem = inventoryItem;
        this.quantity = quantity;
        this.price = price;
        this.moreInfo = moreInfo;
        this.created = created;
        this.closes = closes;
    }

    /**
     * get id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * set id
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get inventory item
     * @return inventory item
     */
    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    /**
     * set inventory item
     * @param inventoryItem inventory item
     */
    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    /**
     * get quantity
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * set quantity
     * @param quantity quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * get price
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * set price
     * @return price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * get more info
     * @return more info
     */
    public String getMoreInfo() {
        return moreInfo;
    }

    /**
     * set More Info
     * @param moreInfo more Info
     */
    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    /**
     * get Created
     * @return Created
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * set Created
     * @param created created
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * get closes
     * @return closes
     */
    public LocalDateTime getCloses() {
        return closes;
    }

    /**
     * set closes
     * @param closes closes
     */
    public void setCloses(LocalDateTime closes) {
        this.closes = closes;
    }
}
