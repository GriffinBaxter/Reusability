package org.seng302.marketplace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MarketplaceCardRepository extends JpaRepository<MarketplaceCard, Integer> {

    /**
     * Search to see if a card exists.
     * Useful for validation purposes when creating a card.
     * @param creatorId the id of the user who created the card.
     * @param section the section of the marketplace the card is to be placed in (For Sale etc.)
     * @param title the title of the card.
     * @param description the description of the card.
     * @return address object if exists
     */
    Optional<MarketplaceCard> findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
            Integer creatorId,
            Section section,
            String title,
            String description
    );
}
