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
package org.seng302.view.incoming;

/**
 * ProductCreationPayload class. This is used to get the data from the incoming JSON which stores the product data.
 */
public class ProductCreationPayload {

    private String id;
    private String name;
    private String description;
    private String manufacturer;
    private Double recommendedRetailPrice;
    private String barcode;

    /**
     * Get the id of the newly created product.
     * @return id of the newly created product.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the name of the newly created product.
     * @return name of the newly created product.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the newly created product.
     * @return description of the newly created product.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the manufacturer of the newly created product.
     * @return manufacturer of the newly created product.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Get the RRP of the newly created product.
     * @return RRP of the newly created product.
     */
    public Double getRecommendedRetailPrice() {
        return recommendedRetailPrice;
    }

    /**
     * Get the barcode of the newly created product.
     * @return barcode of the newly created product.
     */
    public String getBarcode() { return barcode; }
}
