package org.seng302.business.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 * Provides a transactional service to update the product.
 */
@Service
@Transactional
public class ProductUpdateServiceImplementation implements ProductUpdateService{


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

        // Update the attributes
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setRecommendedRetailPrice(updatedProduct.getRecommendedRetailPrice());

        // Detach the entity so we can update the ID attribute
        entityManager.detach(product);
        product.setProductId(updatedProduct.getId());

        // Adding the new copy to the database.
        entityManager.persist(product);

        // Delete the old copy
        productRepository.deleteByIdAndBusinessId(productId, businessId);
    }

}
