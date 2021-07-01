/**
 * Summary. This file contains the definition for the Product.
 *
 * Description. This file contains the defintion for the Product.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/Product
 * @file   This file contains the definition for Product.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.exceptions.IllegalProductArgumentException;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class for products
 * A business can have many products.
 * Inventory items contain a set amount of products.
 */
@Data
@NoArgsConstructor
@Entity
@IdClass(ProductId.class)
public class Product {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(targetEntity = Business.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", insertable = false, updatable = false)
    private Business business;

    @Id
    @Column(name = "business_id")
    private Integer businessId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 600)
    private String description;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "recommended_retail_price")
    private Double recommendedRetailPrice;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    private static final Logger logger = LogManager.getLogger(Product.class.getName());

    // Values need for validation.
    private static final Integer PRODUCT_ID_MIN_LENGTH = 3;
    private static final Integer PRODUCT_ID_MAX_LENGTH = 15;

    private static final Integer NAME_MIN_LENGTH = 1;
    private static final Integer NAME_MAX_LENGTH = 100;

    private static final Integer DESCRIPTION_MIN_LENGTH = 0;
    private static final Integer DESCRIPTION_MAX_LENGTH = 600;

    private static final Integer MANUFACTURER_MIN_LENGTH = 0;
    private static final Integer MANUFACTURER_MAX_LENGTH = 100;

    private static final Double RECOMMENDED_RETAIL_PRICE_MINIMUM = 0.0;
    private static final Double RECOMMENDED_RETAIL_PRICE_MAXIMUM = Double.POSITIVE_INFINITY;

    /**
     * Constructor for products.
     *
     * @param id Unique 3-4 letter code for a product belonging to a given business.
     * @param business The business the product belongs to.
     * @param name The full name of the product.
     * @param description The description of the product (optional).
     * @param manufacturer The manufacturer of the product (optional).
     * @param recommendedRetailPrice The recommended retail price (RRP) of the product (optional).
     * @param created The date and time the product was created.
     * @throws IllegalProductArgumentException Validation exception.
     */
    public Product(String id,
                   Business business,
                   String name,
                   String description,
                   String manufacturer,
                   Double recommendedRetailPrice,
                   LocalDateTime created
    ) throws IllegalProductArgumentException {
        if (!isValidProductId(id)) {
            logger.error("Product Creation Error - Product ID {} is not valid", id);
            throw new IllegalProductArgumentException("Invalid product ID");
        }
        if (business == null) {
            logger.error("Product Creation Error - Business is null");
            throw new IllegalProductArgumentException("Invalid business");
        }
        if (!isValidName(name)) {
            logger.error("Product Creation Error - Name {} is not valid", name);
            throw new IllegalProductArgumentException("Invalid product name");
        }
        if (!isValidDescription(description)) {
            logger.error("Product Creation Error - Description {} is not valid", description);
            throw new IllegalProductArgumentException("Invalid product description");
        }
        if (!isValidManufacturer(manufacturer)) {
            logger.error("Product Creation Error - Manufacturer {} is not valid", manufacturer);
            throw new IllegalProductArgumentException("Invalid manufacturer");
        }
        if (created == null) {
            logger.error("Product Creation Error - Created (Date-Time) is null");
            throw new IllegalProductArgumentException("Invalid date");
        }
        this.id = id;
        this.business = business;
        this.businessId = business.getId();
        this.name = name;
        this.description = (description.equals("")) ? null : description;
        this.manufacturer = (manufacturer.equals("")) ? null : manufacturer;
        this.recommendedRetailPrice = recommendedRetailPrice;
        this.created = created;
    }

    // Getters

    public String getProductId() {
        return id;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Double getRecommendedRetailPrice() {
        return recommendedRetailPrice;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    // Setters

    public void setProductId(String id) {
        this.id = id;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setRecommendedRetailPrice(Double recommendedRetailPrice) {
        this.recommendedRetailPrice = recommendedRetailPrice;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /* --------------------------------------------------Validation-------------------------------------------------- */

    /**
     * Checks to see whether a product ID is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param productId The product ID to be checked.
     * @return true when the product ID is valid
     */
    private boolean isValidProductId(String productId) {
        return (productId.length() >= PRODUCT_ID_MIN_LENGTH) &&
                (productId.length() <= PRODUCT_ID_MAX_LENGTH) &&
                (productId.matches("^[A-Z0-9-]+$"));
    }

    /**
     * Checks to see whether a product name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param productName The product name to be checked.
     * @return true when the product name is valid
     */
    private boolean isValidName(String productName) {
        return (productName.length() >= NAME_MIN_LENGTH) &&
                (productName.length() <= NAME_MAX_LENGTH) &&
                (productName.matches("^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '#,.&()-]+$"));
    }

    /**
     * Checks to see whether a description is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param description The description to be checked.
     * @return true when the description is valid
     */
    private boolean isValidDescription(String description) {
        return (description.length() >= DESCRIPTION_MIN_LENGTH) && (description.length() <= DESCRIPTION_MAX_LENGTH);
    }

    /**
     * Checks to see whether a manufacturer name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param manufacturer The manufacturer name to be checked.
     * @return true when the manufacturer name is valid
     */
    private boolean isValidManufacturer(String manufacturer) {
        return (manufacturer.length() >= MANUFACTURER_MIN_LENGTH) &&
                (manufacturer.length() <= MANUFACTURER_MAX_LENGTH) &&
                (manufacturer.matches("^[a-zA-Z0-9À-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '#,.&()-]*$"));
    }

}
