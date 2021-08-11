/**
 * Summary. Contains the definition for the InventoryItem class.
 *
 * Description. Contains the definition for the InventoryItem class, which contains
 * the JPA entity for InventoryItem.
 *
 * @link   team-400/src/main/java/org/seng302/business/inventoryItem/InventoryItem
 * @file   This file contains the InventoryItem JPA entity definition.
 * @author team-400
 * @since  5.5.2021
 */
package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.exceptions.IllegalInventoryItemArgumentException;
import org.seng302.view.outgoing.InventoryItemPayload;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for inventory items
 */
@Data
@NoArgsConstructor
@Entity
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "business_id", referencedColumnName = "business_id", insertable = false, updatable = false)
    })
    private Product product;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "business_id", nullable = false)
    private Integer businessId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price_per_item")
    private Double pricePerItem;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "manufactured")
    private LocalDate manufactured;

    @Column(name = "sell_by")
    private LocalDate sellBy;

    @Column(name = "best_before")
    private LocalDate bestBefore;

    @Column(name = "expires", nullable = false)
    private LocalDate expires;

    @OneToMany(mappedBy = "inventoryItem", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Listing> listings = new ArrayList<>();

    /**
     * Constructor for inventory items.
     *
     * @param product      The product that the inventory item is an instance of.
     * @param productId    The product ID of the product.
     * @param quantity     The number of items.
     * @param pricePerItem The price for each item.
     * @param totalPrice   The total price of all the items together.
     * @param manufactured The date this batch was manufactured on.
     * @param sellBy       The sell by date of this batch.
     * @param bestBefore   The best before date of this batch.
     * @param expires      The expiration date of this batch.
     */
    public InventoryItem(
            Product product,
            String productId,
            Integer quantity,
            Double pricePerItem,
            Double totalPrice,
            LocalDate manufactured,
            LocalDate sellBy,
            LocalDate bestBefore,
            LocalDate expires
    ) throws IllegalInventoryItemArgumentException {
        if (product == null) {
            throw new IllegalInventoryItemArgumentException("Invalid product");
        }
        if (productId == null || !productId.equals(product.getProductId())) {
            throw new IllegalInventoryItemArgumentException("Invalid product or product ID");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalInventoryItemArgumentException("Invalid quantity, must have at least one item");
        }
        if (pricePerItem != null && pricePerItem < 0) {
            throw new IllegalInventoryItemArgumentException("Invalid price per item, must not be negative");
        }
        if (totalPrice != null && totalPrice < 0) {
            throw new IllegalInventoryItemArgumentException("Invalid total price, must not be negative");
        }
        if (manufactured != null && manufactured.isAfter(LocalDate.now())) {
            throw new IllegalInventoryItemArgumentException("Invalid manufacture date");
        }
        if (expires == null || expires.isBefore(LocalDate.now())) {
            throw new IllegalInventoryItemArgumentException("Invalid expiration date, must have expiration date and cannot add expired item");
        }
        this.product = product;
        this.productId = product.getProductId();
        this.businessId = product.getBusinessId();
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = totalPrice;
        this.manufactured = manufactured;
        this.sellBy = sellBy;
        this.bestBefore = bestBefore;
        this.expires = expires;
    }

    // Getters

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getBusinessId() {
        return businessId;
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

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
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

    public void setListings(List<Listing> listings) {
        this.listings = listings;
    }

    /**
     * Adds a new listing for this inventory item.
     * This function will need to be called in the endpoint when creating a listing.
     * @param listing A listing which contains this inventory item.
     */
    public void addListing(Listing listing) {
        this.listings.add(listing);
    }

    /**
     * Removes a listing from the list of listings for this inventory item.
     * Also removes this inventory item for the corresponding listing.
     * @param listing A listing which is to be removed.
     */
    public void removeListing(Listing listing) {
        int listingId = listing.getId();
        for (int i = 0; i < listings.size(); i++){
            if (listings.get(i).getId() == listingId){
                this.listings.remove(i);
            }
        }
        listing.setInventoryItem(null);
    }

    /**
     * Loops through the current listings for this inventory item and calculates the total currently listed.
     * This quantity is needed to validate whether a listing should be created based on its quantity.
     * @return totalListed the total quantity of this inventory item listed for sale.
     */
    public int getInventoryItemQuantityListed() {
        int totalListed = 0;
        for (Listing listing: listings) {
            totalListed += listing.getQuantity();
        }
        return totalListed;
    }

    /**
     * Converts an inventory item into an InventoryItemPayload
     * @throws Exception An exception that may be thrown from converting a product to a payload form
     */
    public InventoryItemPayload convertToPayload() throws Exception {

        String manufacturedString;
        if (manufactured == null) {
            manufacturedString = "";
        } else {
            manufacturedString = manufactured.toString();
        }

        String bestBeforeString;
        if (bestBefore == null) {
            bestBeforeString = "";
        } else {
            bestBeforeString = bestBefore.toString();
        }

        String sellByString;
        if (sellBy == null) {
            sellByString = "";
        } else {
            sellByString = sellBy.toString();
        }

        return new InventoryItemPayload(id, product.convertToPayload(),
                quantity, pricePerItem, totalPrice, manufacturedString,
                sellByString, bestBeforeString, expires.toString());
    }
}
