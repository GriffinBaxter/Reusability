package org.seng302.business.product;

import java.time.LocalDateTime;

/**
 * ProductPayload class
 */
public class ProductPayload {

    private String id;
    private String name;
    private String description;
    private Double recommendedRetailPrice;
    private String created;

    /**
     * Constructor for product payloads.
     *
     * @param id Product code
     * @param name Product name
     * @param description Product description
     * @param recommendedRetailPrice The recommended retail price (RRP) of the product, a double
     * @param created The date and time the product was created
     */
    public ProductPayload(
            String id,
            String name,
            String description,
            Double recommendedRetailPrice,
            LocalDateTime created
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recommendedRetailPrice = recommendedRetailPrice;
        this.created = created.toString();
    }

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

    public String getCreated() {
        return created;
    }

}
