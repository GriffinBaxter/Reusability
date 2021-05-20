package org.seng302.marketplace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * KeywordRepository class.
 * This is used to store keywords.
 */
@RepositoryRestResource
public interface KeywordRepository extends JpaRepository<Keyword, Integer> {

    /**
     * Search to see if a keyword exists.
     * @param name the name of the keyword to be searched for.
     * @return a keyword that should be unique.
     */
    Optional<Keyword> findByName(String name);
}
