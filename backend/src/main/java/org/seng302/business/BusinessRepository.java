package org.seng302.business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource
public interface BusinessRepository extends JpaRepository<Business, String> {

    /**
     * Search for an business account by its id
     *
     * @param id id
     * @return business object if exists
     */
    Optional<Business> findBusinessById(Integer id);

    Optional<Business> findBusinessByName(String name);
}

