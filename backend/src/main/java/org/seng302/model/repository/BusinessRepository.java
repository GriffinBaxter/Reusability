/**
 * Summary. This file contains the definition for the BusinessRepository.
 *
 * Description. This file contains the defintion for the BusinessRepository.
 *
 * @link   team-400/src/main/java/org/seng302/business/BusinessRepository
 * @file   This file contains the definition for BusinessRepository.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model.repository;

import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface BusinessRepository extends JpaRepository<Business, String>, BusinessRepositoryCustom {

    /**
     * Search for an business account by its id
     * @param id id
     * @return business object if exists
     */
    Optional<Business> findBusinessById(Integer id);

    /**
     * Search for a list of business account by a name
     * @param name name
     * @return a list of business object
     */
    List<Business> findBusinessesByName(String name);

    /**
     * Search for a list of business account by an address
     * @param address address
     * @return a list of business object
     */
    List<Business> findBusinessesByAddress(Address address);

    /**
     * Search for a page of business accounts by business type.
     * @param type the type of businesses to search for.
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching business results.
     *
     * Preconditions:  A non-null business type that has been converted from type String to type BusinessType.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching business results.
     */
    Page<Business> findBusinessesByBusinessType(BusinessType type, Pageable pageable);


}

