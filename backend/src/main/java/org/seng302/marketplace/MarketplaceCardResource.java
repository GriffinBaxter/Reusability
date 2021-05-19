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
     * @param sessionToken Session token
     * @return
     */
    @PostMapping("/cards")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Card created successfully.")
    public ResponseEntity<MarketplaceCardIdPayload> createCard(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestBody MarketplaceCardCreationPayload cardPayload
    ) {
        logger.debug("Card payload received: {}", cardPayload);

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        try {
            MarketplaceCard card = new MarketplaceCard(
                    cardPayload.getCreatorId(),
                    currentUser,
                    cardPayload.getSection(),
                    LocalDateTime.now(),
                    cardPayload.getTitle(),
                    cardPayload.getDescription()
            );

            List<KeywordPayload> keywords = cardPayload.getKeywords();
            for (KeywordPayload keyword: keywords) {
                Optional<Keyword> existingKeyword = keywordRepository.findByName(keyword.getName());
                if (existingKeyword.isPresent()) { // If keyword exists then update existing keyword.
                    Keyword existingKeywordPresent = existingKeyword.get();
                    existingKeywordPresent.addCard(card);
                    keywordRepository.save(existingKeyword.get());
                } else { // If no keyword existing create a new one and save.
                    Keyword newKeyword = new Keyword(
                            keyword.getName(),
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


    }
}
