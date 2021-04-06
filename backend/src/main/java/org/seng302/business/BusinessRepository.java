package org.seng302.business;

import org.seng302.Address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface BusinessRepository extends JpaRepository<Business, String> {

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
     * Search for a list of business account by a address
     * @param address address
     * @return a list of business object
     */
    List<Business> findBusinessesByAddress(String address);
}

