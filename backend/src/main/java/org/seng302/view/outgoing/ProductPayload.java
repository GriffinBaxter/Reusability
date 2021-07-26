/**
 * Summary. This file contains the definition for the ProductPayload.
 *
 * Description. This file contains the defintion for the ProductPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductPayload
 * @file   This file contains the definition for ProductPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

import org.seng302.model.Image;
import org.seng302.model.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductPayload class
 */
public class ProductPayload {

    private String id;
    private String name;
    private String description;
    private String manufacturer;
    private Double recommendedRetailPrice;
    private String created;
    private List<ImagePayload> images;

    /**
     * Constructor for product payloads.
     *
     * @param id Product code
     * @param name Product name
     * @param description Product description
     * @param manufacturer Product manufacturer
     * @param recommendedRetailPrice The recommended retail price (RRP) of the product, a double
     * @param created The date and time the product was created
     */
    public ProductPayload(
            String id,
            String name,
            String description,
            String manufacturer,
            Double recommendedRetailPrice,
            LocalDateTime created,
            List<Image> images
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.recommendedRetailPrice = recommendedRetailPrice;
        this.created = created.toString();
        this.images = ImagePayload.convertToImagePayload(images);
    }

    /**
     * Converts a Product to a ProductPayload
     * @param product The product that will be converted
     * @return ProductPayload of the product
     */
    public static ProductPayload convertProductToProductPayload(Product product) {

        return new ProductPayload(product.getProductId(),
                                    product.getName(),
                                    product.getDescription(),
                                    product.getManufacturer(),
                                    product.getRecommendedRetailPrice(),
                                    product.getCreated(),
                                    product.getImages());
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

    public String getManufacturer() {
        return manufacturer;
    }

    public Double getRecommendedRetailPrice() {
        return recommendedRetailPrice;
    }

    public String getCreated() {
        return created;
    }

    public List<ImagePayload> getImages() {return images;}

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"name\":\"" + name + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"manufacturer\":\"" + manufacturer + "\"," +
                "\"recommendedRetailPrice\":" + recommendedRetailPrice + "," +
                "\"created\":\"" + created + "," +
                "\"images\":" + images + "," + "\"}";
    }

}
