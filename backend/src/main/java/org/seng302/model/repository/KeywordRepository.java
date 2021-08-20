package org.seng302.model.repository;

import org.seng302.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
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

    /**
     * Search by partial keyword name ignoring case, including only up to 6 results
     * @param name Keyword name
     * @return List of keywords that fit the criteria
     */
    List<Keyword> findTop6ByNameIgnoreCaseContaining(String name);

    /**
     * Get Keyword from repository by ID
     * @param id id of the keyword to be searched for
     * @return a unique keyword
     */
    Optional<Keyword> findById(Integer id);
}
