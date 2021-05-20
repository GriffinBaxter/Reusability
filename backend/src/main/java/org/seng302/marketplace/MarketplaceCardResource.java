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

    // TODO Helper functions
    // TODO GAA
    // TODO Cucumber tests
    // TODO Integration tests for 403/GAA
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

        // Check to see if card already exists.
        Optional<MarketplaceCard> storedCard = cardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                cardPayload.getCreatorId(), cardPayload.getSection(), cardPayload.getTitle(), cardPayload.getDescription());

        // If card does not exist create a new one.
        // This is done to prevent duplicate cards.
        if (storedCard.isEmpty()) {
            try {
                MarketplaceCard card = new MarketplaceCard(
                        cardPayload.getCreatorId(),
                        currentUser,
                        cardPayload.getSection(),
                        LocalDateTime.now(),
                        cardPayload.getTitle(),
                        cardPayload.getDescription()
                );

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
            } catch (Exception e) {
                logger.error("Card Creation Failure - {}", e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid card"
                );
            }
        } else {
            logger.error("Card already exists.");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Card already exists."
            );
        }
    }
}
