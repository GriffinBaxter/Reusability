package org.seng302.business.product;

/**
 * ProductCreationPayload class
 */
public class ProductCreationPayload {

    private String id;
    private String name;
    private String description;
    private String manufacturer;
    private Double recommendedRetailPrice;

    public String getId() {
        return id;
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
}
