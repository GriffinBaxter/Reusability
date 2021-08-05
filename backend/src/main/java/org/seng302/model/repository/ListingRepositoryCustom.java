package org.seng302.model.repository;

import org.seng302.model.Listing;
import org.seng302.model.enums.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ListingRepositoryCustom {

    /**
     * Finds all listings with matching product names
     * @param names list of product names
     * @param pageable Pageable
     * @param businessType type of business
     * @param minimumPrice minimum price for price range
     * @param maximumPrice maximum price for price range
     * @param fromDate from date for close date range
     * @param toDate to date for close date range
     * @return returns a Page of listings
     */
    Page<Listing> findAllListingsByProductName(
            List<String> names, Pageable pageable,
            BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate
    );

    /**
     * Finds all listings with matching locations
     * @param locations list of locations
     * @param pageable Pageable
     * @param businessType type of business
     * @param minimumPrice minimum price for price range
     * @param maximumPrice maximum price for price range
     * @param fromDate from date for close date range
     * @param toDate to date for close date range
     * @return returns a Page of listings
     */
    Page<Listing> findAllListingsByLocation(
            List<String> locations, Pageable pageable,
            BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate
    );

    /**
     * Finds all listings with matching business names
     * @param names list of business names
     * @param pageable Pageable
     * @param businessType type of business
     * @param minimumPrice minimum price for price range
     * @param maximumPrice maximum price for price range
     * @param fromDate from date for close date range
     * @param toDate to date for close date range
     * @return returns a Page of listings
     */
    Page<Listing> findAllListingsByBusinessName(
            List<String> names, Pageable pageable,
            BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate
    );
}
