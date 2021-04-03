package org.seng302.business;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProductPayload {

    private String productCode;
    private String name;
    private String description;
    private String manufacturer;
    private Double recommendedRetailPrice;
    private String created;

    public ProductPayload(
            String productCode,
            String name,
            String description,
            String manufacturer,
            Double recommendedRetailPrice,
            LocalDateTime created
    ) {
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.recommendedRetailPrice = recommendedRetailPrice;
        this.created = created.toString();
    }

    public String getProductCode() {
        return productCode;
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

    public String getCreated() {
        return created;
    }

}
