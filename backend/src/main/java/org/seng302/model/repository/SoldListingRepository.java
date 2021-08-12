package org.seng302.model.repository;

import org.seng302.model.SoldListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/**
 * Provides an interface to the database, and
 * functions to handle the SoldListing entities.
 */
@RepositoryRestResource
public interface SoldListingRepository extends JpaRepository<SoldListing, Integer> {
}
