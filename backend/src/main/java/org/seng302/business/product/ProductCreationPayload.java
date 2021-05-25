/**
 * Summary. This file contains the definition for the ProductCreationPayload.
 *
 * Description. This file contains the defintion for the ProductCreationPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductCreationPayload
 * @file   This file contains the definition for ProductCreationPayload.
 * @author team-400.
 * @since  5.5.2021
 */
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
