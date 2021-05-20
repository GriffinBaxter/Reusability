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
package org.seng302.business.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.business.Business;
import org.seng302.image.Image;
import org.seng302.validation.ProductValidation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class for products
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
     * @throws Exception Validation exception.
     */
    public Product(String id,
                   Business business,
                   String name,
                   String description,
                   String manufacturer,
                   Double recommendedRetailPrice,
                   LocalDateTime created
    ) throws Exception {
        if (!ProductValidation.isValidProductId(id)) {
            logger.error("Product Creation Error - Product ID {} is not valid", id);
            throw new Exception("Invalid product ID");
        }
        if (business == null) {
            logger.error("Product Creation Error - Business is null");
            throw new Exception("Invalid business");
        }
        if (!ProductValidation.isValidName(name)) {
            logger.error("Product Creation Error - Name {} is not valid", name);
            throw new Exception("Invalid product name");
        }
        if (!ProductValidation.isValidDescription(description)) {
            logger.error("Product Creation Error - Description {} is not valid", description);
            throw new Exception("Invalid product description");
        }
        if (!ProductValidation.isValidManufacturer(manufacturer)) {
            logger.error("Product Creation Error - Manufacturer {} is not valid", manufacturer);
            throw new Exception("Invalid manufacturer");
        }
        if (created == null) {
            logger.error("Product Creation Error - Created (Date-Time) is null");
            throw new Exception("Invalid date");
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

}
