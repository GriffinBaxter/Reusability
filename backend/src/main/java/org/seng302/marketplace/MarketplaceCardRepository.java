package org.seng302.marketplace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MarketplaceCardRepository extends JpaRepository<MarketplaceCard, Integer> {

    /**
     * Search for a marketplace card by its id.
     * @param id Integer id
     * @return A marketplace card matching the id
     */
    Optional<MarketplaceCard> findById(Integer id);

    /**
     * Search to get marketplace cards based on Section
     * @param section
     * @param page
     * @return
     */
    Page<MarketplaceCard> findAllBySection(Section section, Pageable page);
}
