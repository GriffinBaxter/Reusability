package org.seng302.model.repository;

import org.seng302.model.Listing;
import org.seng302.model.enums.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ListingRepositoryCustom {

    Page<Listing> findAllListingsByProductName(
            List<String> names, Pageable pageable,
            BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate
    );

    Page<Listing> findAllListingsByLocation(
            List<String> names, Pageable pageable,
            BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate
    );
    
    Page<Listing> findAllListingsByBusinessName(
            List<String> names, Pageable pageable,
            BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate
    );
}
