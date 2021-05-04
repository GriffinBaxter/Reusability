package org.seng302.business.listing;

import org.seng302.business.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * ListingRepository interface
 */
@RepositoryRestResource
public interface ListingRepository extends JpaRepository<Listing, Integer> {

    /**
     * Finds any listings with the given business ID.
     *
     * @param businessId A business ID.
     * @return A list of products with the given business ID.
     */
     Page<Listing> findListingsByBusinessId(Integer businessId, Pageable paging);
}
