package org.seng302.address;

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
     * @param streetNumber
     * @param streetName
     * @param city
     * @param region
     * @param country
     * @param postcode
     * @return address object if exists
     */
    Optional<Address> findAddressByStreetNumberAndStreetNameAndCityAndRegionAndCountryAndPostcode(
            String streetNumber,
            String streetName,
            String city,
            String region,
            String country,
            String postcode
    );


}
