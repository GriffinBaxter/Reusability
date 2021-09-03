/**
 * Summary. This file contains the definition for the ListingRepository.
 *
 * Description. This file contains the defintion for the ListingRepository.
 *
 * @link   team-400/src/main/java/org/seng302/business/listing/ListingRepository
 * @file   This file contains the definition for ListingRepository.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model.repository;

import org.seng302.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * ListingRepository interface
 */
@RepositoryRestResource
public interface ListingRepository extends JpaRepository<Listing, Integer>, ListingRepositoryCustom {

    /**
     * Finds any listings with the given business ID.
     *
     * @param businessId A business ID.
     * @return A list of products with the given business ID.
     */
     Page<Listing> findListingsByBusinessId(Integer businessId, Pageable paging);


    /**
     * Finds a single listing with the given business and listing IDs.
     * @param businessId A business ID.
     * @param listingId A listing ID.
     * @return A listing with the given ID if it exists.
     */
     Optional<Listing> findListingByBusinessIdAndId(Integer businessId, Integer listingId);

    /**
     * Finds all listings for a business with a certain product barcode.
     * @param businessId A business ID.
     * @param barcode Product barcode number.
     * @param paging Pageable Object for sorting & paging.
     * @return A page of listings.
     */
     Page<Listing> findListingsByBusinessIdAndInventoryItem_Product_Barcode(Integer businessId, String barcode, Pageable paging);
}
