package org.seng302.model.repository;

import org.seng302.model.SoldListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Provides an interface to the database, and
 * functions to handle the SoldListing entities.
 */
@RepositoryRestResource
public interface SoldListingRepository extends JpaRepository<SoldListing, Integer> {

    /**
     * Find all Sold Listings for a particular business
     * @param businessId ID of business to find
     * @param pageable Pageable
     * @return Page of SoldListings
     */
    Page<SoldListing> findAllByBusinessId(Integer businessId, Pageable pageable);

    /**
     * Find all Sold Listings for a particular business between given sale dates
     * @param businessId ID of business to find
     * @param fromDate The date the sale should be from
     * @param toDate The date the sale should be to
     * @return List of SoldListings
     */
    List<SoldListing> findAllByBusinessIdAndSaleDateBetween(
            Integer businessId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );
}
