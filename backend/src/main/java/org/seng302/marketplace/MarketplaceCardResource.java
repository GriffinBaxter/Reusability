package org.seng302.marketplace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.address.AddressRepository;
import org.seng302.main.Authorization;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.seng302.address.AddressRepository;
import org.seng302.business.Business;
import org.seng302.business.BusinessResource;
import org.seng302.main.Authorization;
import org.seng302.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.seng302.main.Authorization.verifyRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MarketplaceCard Resource class.
 * This class contains the endpoints for cards.
 * The POST /cards endpoint is used to create cards.
 * The GET /cards endpoint is used to retrieve all cards that are stored.
 * The GET /cards/id endpoint is used to retrieve the details for a single card.
 */
@RestController
public class MarketplaceCardResource {

    @Autowired
    private MarketplaceCardRepository cardRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    private static final Logger logger = LogManager.getLogger(MarketplaceCardResource.class.getName());

    /**
     * A constructor for MarketplaceCardResource which is used for mocking purposes.
     * @param cardRepository - Stores cards.
     * @param userRepository - Stores user.
     * @param addressRepository - Stores addresses.
     * @param keywordRepository - Stores keywords.
     */
    public MarketplaceCardResource(
             MarketplaceCardRepository cardRepository, UserRepository userRepository,
             AddressRepository addressRepository, KeywordRepository keywordRepository
    ) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.keywordRepository = keywordRepository;
    }

    /**
     * Create a new card.
     * The response status and reason is returned for the corresponding scenario.
     * @param sessionToken Session token
     * @return ResponseEntity<MarketplaceCardIdPayload> this payload contains the id of a successfully created card.
     */
    @PostMapping("/cards")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Card created successfully.")
    public ResponseEntity<MarketplaceCardIdPayload> createCard(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestBody MarketplaceCardCreationPayload cardPayload
    ) {
        logger.debug("Card payload received: {}", cardPayload);

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // If user is GAA, DGAA or the user is also the creator than a card can be created.
        // Otherwise the user is forbidden from creating the card.
        if (Authorization.isGAAorDGAA(currentUser) || (currentUser.getId() == cardPayload.getCreatorId())) {
            // Check to see if card already exists.
            Optional<MarketplaceCard> storedCard = cardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                    cardPayload.getCreatorId(), cardPayload.getSection(), cardPayload.getTitle(), cardPayload.getDescription());

            // If card does not exist create a new one.
            // This is done to prevent duplicate cards.
            if (storedCard.isEmpty()) {
                try {
                    // Retrieve the user who matches the creator id.
                    Optional<User> creator = userRepository.findById(cardPayload.getCreatorId());
                    // If creator exists create card, otherwise return a 404 not found.
                    if (creator.isPresent()) {
                        MarketplaceCard card = new MarketplaceCard(
                                cardPayload.getCreatorId(),
                                creator.get(),
                                cardPayload.getSection(),
                                LocalDateTime.now(),
                                cardPayload.getTitle(),
                                cardPayload.getDescription()
                        );

                        // Loop through keywords and update card and keywords accordingly.
                        List<String> keywords = cardPayload.getKeywords();
                        for (String keyword: keywords) {
                            Optional<Keyword> existingKeyword = keywordRepository.findByName(keyword);
                            if (existingKeyword.isPresent()) { // If keyword exists then update existing keyword.
                                Keyword existingKeywordPresent = existingKeyword.get();
                                existingKeywordPresent.addCard(card);
                                keywordRepository.save(existingKeywordPresent);
                            } else { // If no keyword existing create a new one and save.
                                Keyword newKeyword = new Keyword(
                                        keyword,
                                        LocalDateTime.now(),
                                        card
                                );
                                keywordRepository.save(newKeyword);
                            }
                        }
                        MarketplaceCard createdCard = cardRepository.save(card);
                        logger.info("Successful Card Creation - {}", createdCard.toString());
                        return ResponseEntity.status(HttpStatus.CREATED).body(new MarketplaceCardIdPayload(createdCard.getId()));
                    } else {
                        logger.error("User with ID: {} not found", cardPayload.getCreatorId());
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("User with ID: {} does not exist.", cardPayload.getCreatorId())
                        );
                    }
                } catch (Exception e) {
                    logger.error("Card Creation Failure - {}", e.getMessage());
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            e.getMessage()
                    );
                }
            } else {
                logger.error("Card already exists.");
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Card already exists."
                );
            }
        } else {
            logger.error("User with ID: {} does no have permission to create this card.", currentUser.getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User does not have the permission to create this card."
            );
        }
    }

    /**
     * GET method for retrieving a specific marketplace card.
     * @param id Integer Id (primary key)
     * @return Marketplace card object if it exists
     */
    @GetMapping("/cards/{id}")
    public MarketplaceCardPayload retrieveMarketplaceCard(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id
    ) throws Exception {
        Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<MarketplaceCard> optionalMarketplaceCard = marketplaceCardRepository.findById(id);

        if (optionalMarketplaceCard.isEmpty()) {
            logger.error("Marketplace Card Retrieval Failure - 406 [NOT ACCEPTABLE] - Marketplace card with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        logger.info("Marketplace Card Retrieval Success - 200 [OK] -  Marketplace card retrieved with ID {}", id);
        logger.debug("Marketplace card retrieved with ID {}: {}", id, optionalMarketplaceCard.get().toString());

        MarketplaceCard marketplaceCard = optionalMarketplaceCard.get();

        return marketplaceCard.toMarketplaceCardPayload();
    }
}
