/**
 * Summary. This file contains the definition for the ProductRepository.
 *
 * Description. This file contains the defintion for the ProductRepository.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductRepository
 * @file   This file contains the definition for ProductRepository.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model.repository;

import org.seng302.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * ProductRepository interface
 */
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, String>, ProductRepositoryCustom {

    /**
     * Finds any products with the given business ID.
     *
     * @param businessId A business ID.
     * @return A list of products with the given business ID.
     */
    Page<Product> findProductsByBusinessId(Integer businessId, Pageable paging);

    /**
     * Finds a product with the given product ID and business ID if one exists.
     *
     * @param id A product ID
     * @param businessId A business ID
     * @return A product with the given product ID and business ID.
     */
    Optional<Product> findProductByIdAndBusinessId(String id, Integer businessId);

    /**
     * Finds a product with the given barcode and business ID if one exists.
     *
     * @param barcode A barcode
     * @param businessId A business ID
     * @return A product with the given barcode and business ID.
     */
    Optional<Product> findProductByBarcodeAndBusinessId(String barcode, Integer businessId);

    /**
     * Deletes any products with a matching product ID and a business ID.
     * @param id The ID of the product
     * @param businessId The ID of the business
     */
    void deleteByIdAndBusinessId(String id, Integer businessId);

    /**
     * Finds all products with the given business ID (no pagination)
     * @param businessId A business ID.
     * @return A list of inventory items with the given business ID.
     */
    List<Product> findAllByBusinessId(Integer businessId);

}
