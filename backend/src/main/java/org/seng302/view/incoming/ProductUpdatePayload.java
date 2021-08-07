/**
 * Summary. This file contains the definition for the ProductUpdatePayload.
 *
 * Description. This file contains the defintion for the ProductUpdatePayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductUpdatePayload
 * @file   This file contains the definition for ProductUpdatePayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.incoming;

/**
 * The payload for when a user requests to update a product's details.
 */
public class ProductUpdatePayload {

    private String id;
    private String name;
    private String description;
    private String manufacturer;
    private Double recommendedRetailPrice;
    private String barcode;

    @Override
    public String toString() {
        return "ProductUpdatePayload{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", recommendedRetailPrice='" + recommendedRetailPrice + '\'' +
                ", barcode=" + barcode +
                '}';
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

    public String getManufacturer() { return manufacturer; }

    public Double getRecommendedRetailPrice() {
        return recommendedRetailPrice;
    }

    public String getBarcode() { return barcode; }

    public void setId(String id) {
        this.id = id;
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

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
