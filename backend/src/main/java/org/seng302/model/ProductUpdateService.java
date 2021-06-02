/**
 * Summary. This file contains the definition for the ProductUpdateService.
 *
 * Description. This file contains the defintion for the ProductUpdateService.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductUpdateService
 * @file   This file contains the definition for ProductUpdateService.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model;

import org.seng302.model.ProductRepository;
import org.seng302.view.ProductUpdatePayload;
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
