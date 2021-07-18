package org.seng302.model.repository;

import org.seng302.model.MarketplaceCard;
import org.seng302.model.enums.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * MarketplaceCardRepository class.
 * This is used to store marketplace cards.
 */
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

    /**
     * search to get marketplace cards by creator Id
     * @param id creator id
     * @return A list of marketplace card
     */
    //TODO: Test
    List<MarketplaceCard> findAllByCreatorId(Integer id);

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

    /**
     * Search for all cards created by an existing user with a given user ID.
     * @param creatorId The ID of the creator (user) of the card.
     * @return A list of all cards created by the user (possibly empty).
     */
    List<MarketplaceCard> findMarketplaceCardByCreatorId(
            Integer creatorId
    );
}
