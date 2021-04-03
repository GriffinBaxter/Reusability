package org.seng302.business;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.main.Validation;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity
@IdClass(ProductId.class)
public class Product {

    @Id
    @Column(name = "product_code", nullable = false)
    private String productCode;


    @ManyToOne(targetEntity=Business.class, fetch = FetchType.LAZY)
    @JoinColumn(name="business_id", insertable = false, updatable = false)
    private Business business;

    @Id
    @Column(name = "business_id")
    private Integer businessId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "recommended_retail_price")
    private Double recommendedRetailPrice;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;


    /**
     * Constructor for ProductCatalogue objects (products).
     *
     * @param productCode Unique 3-4 letter code for a product belonging to a given business.
     * @param businessId The ID of the business the product belongs to.
     * @param name The full name of the product.
     * @param description The description of the product (optional).
     * @param manufacturer The manufacturer of the product (optional).
     * @param recommendedRetailPrice The recommended retail price (RRP) of the product (optional).
     * @param created The date and time the product was created.
     * @throws Exception Validation exception.
     */
    public Product(String productCode,
                   Integer businessId,
                   String name,
                   String description,
                   String manufacturer,
                   Double recommendedRetailPrice,
                   LocalDateTime created
    ) throws Exception{
        if (!Validation.isProductCode(productCode)){
            throw new Exception("Invalid product code");
        }
        if (businessId <= 0) {
            throw new Exception("Invalid business ID");
        }
        if (!Validation.isName(name)){
            throw new Exception("Invalid product name");
        }
        if (!manufacturer.equals("") && !Validation.isName(manufacturer)){
            throw new Exception("Invalid manufacturer");
        }
        if (created == null) {
            throw new Exception("Invalid date");
        }
        this.productCode = productCode;
        this.businessId = business.getId();
        this.name = name;
        this.description = (description.equals("")) ? null : description;
        this.manufacturer = (manufacturer.equals("")) ? null : manufacturer;
        this.recommendedRetailPrice = recommendedRetailPrice;
        this.created = created;
    }

    // Getters

    public String getProductCode() {
        return productCode;
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

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
