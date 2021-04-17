package org.seng302.business.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.business.Business;
import org.seng302.main.Validation;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "description")
    private String description;

    @Column(name = "recommended_retail_price")
    private Double recommendedRetailPrice;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;


    /**
     * Constructor for products.
     *
     * @param id Unique 3-4 letter code for a product belonging to a given business.
     * @param business The business the product belongs to.
     * @param name The full name of the product.
     * @param description The description of the product (optional).
     * @param recommendedRetailPrice The recommended retail price (RRP) of the product (optional).
     * @param created The date and time the product was created.
     * @throws Exception Validation exception.
     */
    public Product(String id,
                   Business business,
                   String name,
                   String description,
                   Double recommendedRetailPrice,
                   LocalDateTime created
    ) throws Exception {
        if (!Validation.isProductCode(id)){
            throw new Exception("Invalid product id");
        }
        if (business == null) {
            throw new Exception("Invalid business");
        }
        if (!Validation.isName(name)){
            throw new Exception("Invalid product name");
        }
        if (created == null) {
            throw new Exception("Invalid date");
        }
        this.id = id;
        this.business = business;
        this.businessId = business.getId();
        this.name = name;
        this.description = (description.equals("")) ? null : description;
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

    public void setRecommendedRetailPrice(Double recommendedRetailPrice) {
        this.recommendedRetailPrice = recommendedRetailPrice;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

}
