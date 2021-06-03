/**
 * Summary. This file contains the definition for the ProductUpdateServiceImplementation.
 *
 * Description. This file contains the defintion for the ProductUpdateServiceImplementation.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductUpdateServiceImplementation
 * @file   This file contains the definition for ProductUpdateServiceImplementation.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.model.repository.ProductRepository;
import org.seng302.model.repository.ProductUpdateService;
import org.seng302.view.incoming.ProductUpdatePayload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Provides a transactional service to update the product.
 */
@Service
@Transactional
public class ProductUpdateServiceImplementation implements ProductUpdateService {

    private static final Logger logger = LogManager.getLogger(ProductUpdateServiceImplementation.class.getName());

    @PersistenceContext
    private EntityManager entityManager; // Used to access the persistance level data.


    /**
     * Provides a transactional way to update the product given the original product ID and business ID, product repository and the new updatedProduct data.
     * @param productId Product ID of the product you want to update.
     * @param businessId Business ID of the business the product belongs to.
     * @param productRepository An instance that points to the productRepository interface.
     * @param updatedProduct The Payload with all the attributes set to the new validated values.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE) // Wraps the method in a transaction that automatically handles the errors and rolls back if necessary
    public void updateProduct(String productId, Integer businessId, ProductRepository productRepository, ProductUpdatePayload updatedProduct) {
        // Retrieving a copy of our entity product
        Product product = entityManager.find(Product.class, new ProductId(productId, businessId));

        if (product == null) {
            logger.error("Product does not exist.");
            return;
        }

        logger.debug("Found product from business (business ID: {}) with product ID {}: {}", businessId, productId, product);

        // Update the attributes
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setManufacturer(updatedProduct.getManufacturer());
        product.setRecommendedRetailPrice(updatedProduct.getRecommendedRetailPrice());
        logger.debug("Set new attributes for product.");

        // Detach the entity so we can update the ID attribute
        entityManager.detach(product);
        product.setProductId(updatedProduct.getId());
        logger.debug("Detached and set new product id.");

        // Adding the new copy to the database.
        entityManager.persist(product);
        logger.debug("Re-attached product");

        // Delete the old copy
        productRepository.deleteByIdAndBusinessId(productId, businessId);
        logger.debug("Old copy has been deleted.");
    }

}
