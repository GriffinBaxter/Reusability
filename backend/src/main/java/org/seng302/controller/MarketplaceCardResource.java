package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.model.enums.Section;
import org.seng302.model.*;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.MarketplaceCardRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.incoming.MarketplaceCardCreationPayload;
import org.seng302.view.outgoing.MarketplaceCardIdPayload;
import org.seng302.view.outgoing.MarketplaceCardPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

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
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    private static final Logger logger = LogManager.getLogger(MarketplaceCardResource.class.getName());

    /**
     * A constructor for MarketplaceCardResource which is used for mocking purposes.
     * @param marketplaceCardRepository - Stores cards.
     * @param userRepository - Stores user.
     * @param keywordRepository - Stores keywords.
     */
    public MarketplaceCardResource(
             MarketplaceCardRepository marketplaceCardRepository, UserRepository userRepository,
             KeywordRepository keywordRepository
    ) {
        this.marketplaceCardRepository = marketplaceCardRepository;
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
    }

    /**
     * Create a new card.
     * The response status and reason is returned for the corresponding scenario.
     * @param sessionToken Session token
     * @return ResponseEntity<MarketplaceCardIdPayload> this payload contains the id of a successfully created card.
     */
    @PostMapping("/cards")
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
            Optional<MarketplaceCard> storedCard = marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
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
                                keywordRepository.save(existingKeywordPresent);
                                card.addKeyword(existingKeywordPresent);
                            } else { // If no keyword existing create a new one and save.
                                Keyword newKeyword = new Keyword(
                                        keyword,
                                        LocalDateTime.now(),
                                        card
                                );
                                keywordRepository.save(newKeyword);
                                card.addKeyword(newKeyword);
                            }
                        }
                        MarketplaceCard createdCard = marketplaceCardRepository.save(card);
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
     * Get response for retrieving a list of Marketplace Cards from a Section
     * @param sessionToken JSESSIONID
     * @param section Section of card
     * @param orderBy Ordering
     * @param page Page number
     * @return List of MarketplaceCardPayloads
     * @throws Exception
     */
    @GetMapping("/cards")
    public ResponseEntity<List<MarketplaceCardPayload>> retrieveMarketplaceCards(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam String section,
            @RequestParam(defaultValue = "createdDESC") String orderBy,
            @RequestParam(defaultValue = "0") String page
    ) throws Exception {
        logger.debug("Get card request received with section {}, order by {}, page {}", section, orderBy, page);

        // Checks user logged in 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Checks section is valid
        Section sectionType;
        try {
            sectionType = Section.valueOf(section.toUpperCase());
        } catch(Exception e) {
            logger.error("400 [BAD REQUEST] - {} is not a valid section", section);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid Section"
            );
        }

        // Checks page is number
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (final NumberFormatException e) {
            logger.error("400 [BAD REQUEST] - {} is not a valid page number", page);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page parameter invalid"
            );
        }

        // Front-end displays 20 cards per page
        int pageSize = 6; // NOTE if changed must also be changed in MarketplaceCardResourceIntegrationTests

        Sort sortBy = null;
        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "createdASC":
                sortBy = Sort.by(Sort.Order.asc("created").ignoreCase());
                break;
            case "createdDESC":
                sortBy = Sort.by(Sort.Order.desc("created").ignoreCase());
                break;
            case "titleASC":
                sortBy = Sort.by(Sort.Order.asc("title").ignoreCase());
                break;
            case "titleDESC":
                sortBy = Sort.by(Sort.Order.desc("title").ignoreCase());
                break;
            case "locationASC":
                sortBy = Sort.by(Sort.Order.asc("creator.homeAddress.suburb").ignoreCase()).and(Sort.by(Sort.Order.asc("creator.homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.desc("created").ignoreCase())));
                break;
            case "locationDESC":
                sortBy = Sort.by(Sort.Order.desc("creator.homeAddress.suburb").ignoreCase()).and(Sort.by(Sort.Order.desc("creator.homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.desc("created").ignoreCase())));
                break;
            default:
                logger.error("400 [BAD REQUEST] - {} is not a valid order by parameter", orderBy);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OrderBy Field invalid"
                );
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        Page<MarketplaceCard> pagedResult = marketplaceCardRepository.findAllBySection(sectionType, paging);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Get Marketplace Cards Success - 200 [OK] -  Cards retrieved for Section {}, order by {}, page {}", section, orderBy, pageNo);
        List<MarketplaceCard> cards = pagedResult.getContent();
        List<MarketplaceCardPayload> payload = new ArrayList<>();
        for (MarketplaceCard card: cards) {
            payload.add(card.toMarketplaceCardPayload());
        }

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(payload);
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
