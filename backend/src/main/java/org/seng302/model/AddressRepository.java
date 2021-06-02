/**
 * Summary. Contains the definition for the address repository interface.
 *
 * Description. Contains the definition for the address repository, which defines the functions
 * used to perform actions with the data.
 *
 * @link   team-400/src/main/java/org/seng302/address/AddresRepository
 * @file   This file defines the AddressRepository interface.
 * @author team-400
 * @since  5.5.2021
 */

package org.seng302.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * Search to see if an address exists.
     * Useful to validate if a business is not already created, and to determine
     * if multiple users live at the same address, so multiple copies
     * of the address are not created.
     * @param streetNumber the address street number.
     * @param streetName the address street name.
     * @param city the address city.
     * @param region the address region.
     * @param country the address country.
     * @param postcode the address postcode.
     * @return address object if exists
     */
    Optional<Address> findAddressByStreetNumberAndStreetNameAndCityAndRegionAndCountryAndPostcodeAndSuburb(
            String streetNumber,
            String streetName,
            String city,
            String region,
            String country,
            String postcode,
            String suburb
    );


}
