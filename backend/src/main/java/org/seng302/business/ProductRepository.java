package org.seng302.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * ProductRepository interface
 */
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, String> {

    /**
     * Finds any products with the given business ID.
     *
     * @param businessId A business ID.
     * @return A list of products with the given business ID.
     */
    List<ProductPayload> findProductsByBusinessId(Integer businessId);

}
