package org.seng302.business.product;

/**
 * ProductCreationPayload class
 */
public class ProductCreationPayload {

    private String id;
    private String name;
    private String description;
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

    public Double getRecommendedRetailPrice() {
        return recommendedRetailPrice;
    }
}
