package org.seng302.business.product;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductUpdateService {

    /**
     * Provides a transactional way to update the product given the original product ID and business ID, product repository and the new updatedProduct data.
     * @param productId Product ID of the product you want to update.
     * @param businessId Business ID of the business the product belongs to.
     * @param productRepository An instance that points to the productRepository interface.
     * @param updatedProduct The Payload with all the attributes set to the new values
     */
    void updateProduct(String productId, Integer businessId, ProductRepository productRepository, ProductUpdatePayload updatedProduct);
}
