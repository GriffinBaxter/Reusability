package org.seng302.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * Search for an address by its id
     * @param id id
     * @return business object if exists
     */
    Optional<Address> findAddressById(Integer id);

}
