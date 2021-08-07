/**
 * Summary. This file contains the definition for the Listing.
 *
 * Description. This file contains the defintion for the Listing.
 *
 * @link   team-400/src/main/java/org/seng302/business/listing/Listing
 * @file   This file contains the definition for Listing.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.exceptions.IllegalListingArgumentException;
import org.seng302.model.enums.BusinessType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for sale listings
 */
@NoArgsConstructor
@Entity
public class Listing {
    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    // InventoryItem
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventoryItemId", nullable = false)
    private InventoryItem inventoryItem;

    @Column(name = "productName")
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "businessType")
    private BusinessType businessType;

    @Column(name = "businessId", nullable = false)
    private Integer businessId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "sellerName", nullable = false)
    private String sellerName;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "moreInfo", length = 600)
    private String moreInfo;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "closes")
    private LocalDateTime closes;

    @JsonManagedReference
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_bookmarks",
            joinColumns = {@JoinColumn(name = "listings_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> bookmarkedListings = new ArrayList<>();

    @Column(name = "totalBookmarks")
    private Integer totalBookmarks;

    private static final Logger logger = LogManager.getLogger(Listing.class.getName());

    // Values need for validation.
    private static final Integer MORE_INFO_MIN_LENGTH = 0;
    private static final Integer MORE_INFO_MAX_LENGTH = 600;
    private static final Integer MIN_QUANTITY = 0;

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
                   Double price,
                   String moreInfo,
                   LocalDateTime created,
                   LocalDateTime closes
    ) throws IllegalListingArgumentException {
        if (inventoryItem == null) {
            logger.error("Listing Creation Error - Inventory item is null");
            throw new IllegalListingArgumentException("Invalid inventory item");
        }
        if (!isValidQuantity(quantity, inventoryItem)) {
            logger.error("Listing Creation Error - Quantity {} is not valid", quantity);
            throw new IllegalListingArgumentException("Invalid quantity");
        }
        if (!isValidMoreInfo(moreInfo)) {
            logger.error("Listing Creation Error - More Info {} is not valid", moreInfo);
            throw new IllegalListingArgumentException("Invalid more info");
        }
        if (created == null) {
            logger.error("Listing Creation Error - Created (Date-Time) is null");
            throw new IllegalListingArgumentException("Invalid creation date");
        }
        if (closes != null && closes.isBefore(LocalDateTime.now())) {
            logger.error("Listing Creation Error - Closes (Date-Time) is Invalid");
            throw new IllegalListingArgumentException("Invalid closing date.");
        }
        Product product = inventoryItem.getProduct();
        Business business = product.getBusiness();
        this.inventoryItem = inventoryItem;
        this.businessId = product.getBusinessId();
        this.quantity = quantity;
        // If price is not defined calculate it using price per item.
        this.price = (price == null) ? calculatePrice() : price;
        this.moreInfo = moreInfo;
        this.created = created;
        // If closing date is not defined, use expiry date of inventory item.
        this.closes = (closes == null) ? LocalDateTime.of(inventoryItem.getExpires(), LocalTime.of(0, 0)) : closes;
        // Read related business info
        this.productName = product.getName();
        this.businessType = business.getBusinessType();
        // Read related seller info
        this.sellerName = business.getName();
        this.country = business.getAddress().getCountry();
        this.city = business.getAddress().getCity();
        this.totalBookmarks = 0;
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
     * Set price
     * @param price the new price of the listing
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

    public String getProductName() {
        return productName;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Integer getTotalBookmarks() {
        totalBookmarks = bookmarkedListings.size();
        return totalBookmarks;
    }

    public List<User> getBookmarkedListings() {
        return bookmarkedListings;
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

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void addUserToANewBookmark(User user) {
        if (!this.bookmarkedListings.contains(user)) {
            this.bookmarkedListings.add(user);
        }
    }

    public void removeUserFromABookmark(User user) {
        for (int i = 0; i < this.bookmarkedListings.size(); i++){
            if(this.bookmarkedListings.get(i) == user){
                this.bookmarkedListings.remove(i);
                i--;
            }
        }
    }

    public Boolean isBookmarked(User user) {
        return this.bookmarkedListings.contains(user);
    }

    /**
     * calculate the price of this Listing.
     */
    public double calculatePrice() {
        double calculatedPrice;
        if (this.inventoryItem.getQuantity() == this.quantity) {
            calculatedPrice = this.inventoryItem.getTotalPrice();
        } else {
            calculatedPrice = this.inventoryItem.getPricePerItem() * this.quantity;
        }
        return calculatedPrice;
    }

    /* --------------------------------------------Validation--------------------------------------------*/

    /**
     * Checks to see whether more info is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param moreInfo The more info to be checked.
     * @return true when more info is valid
     */
    private boolean isValidMoreInfo(String moreInfo) {
        return (moreInfo.length() >= MORE_INFO_MIN_LENGTH) &&
                (moreInfo.length() <= MORE_INFO_MAX_LENGTH);
    }

    /**
     * Checks to see whether quantity is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param quantity The quantity to be checked.
     * @param inventoryItem An inventoryItem which is needed to validate the quantity of a listing
     * @return true when quantity is valid
     */
    private boolean isValidQuantity(int quantity, InventoryItem inventoryItem) {
        return (quantity > MIN_QUANTITY) &&
                ((quantity + inventoryItem.getInventoryItemQuantityListed()) <= inventoryItem.getQuantity());
    }
}
