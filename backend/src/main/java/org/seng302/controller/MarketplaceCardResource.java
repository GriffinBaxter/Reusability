package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.exceptions.IllegalKeywordArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.model.enums.Section;
import org.seng302.model.*;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.MarketplaceCardRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.incoming.MarketplaceCardCreationPayload;
import org.seng302.view.incoming.MarketplaceCardUpdatePayload;
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
 * The PUT /cards/{id}/extenddisplayperiod endpoint is used to extend the display period of a card nearing expiry.
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
     *
     * @param marketplaceCardRepository - Stores cards.
     * @param userRepository            - Stores user.
     * @param keywordRepository         - Stores keywords.
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
     *
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
                        for (String keyword : keywords) {
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
                        logger.info("Successful Card Creation - {}", createdCard);
                        return ResponseEntity.status(HttpStatus.CREATED).body(new MarketplaceCardIdPayload(createdCard.getId()));
                    } else {
                        logger.error("User with ID: {} not found", cardPayload.getCreatorId());
                        throw new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("User with ID: %s does not exist.", cardPayload.getCreatorId())
                        );
                    }
                } catch (IllegalMarketplaceCardArgumentException | IllegalKeywordArgumentException e) {
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
     * Create a new card.
     * The response status and reason is returned for the corresponding scenario.
     *
     * @param sessionToken Session token
     * @return ResponseEntity<MarketplaceCardIdPayload> this payload contains the id of a successfully created card.
     */
    @PutMapping("/cards/{id}")
    public ResponseEntity<MarketplaceCardIdPayload> editCard(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestBody MarketplaceCardUpdatePayload updatedCardPayload,
            @PathVariable Integer id
    ) {
        logger.debug("Edit card payload received: {}", updatedCardPayload);

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Check to see if card exists.
        Optional<MarketplaceCard> storedCard = marketplaceCardRepository.findById(id);

        if (storedCard == null || storedCard.isEmpty()) {
            logger.error("Card at ID: {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Card doesn't exist"
            );

            // If user is GAA, DGAA or the user is also the creator then a card can be updated.
            // Otherwise the user is forbidden from creating the card.
            if (Authorization.isGAAorDGAA(currentUser) || currentUser.getId() == storedCard.get().getCreatorId()) {

                // Verify there is a payload. Otherwise we are wasting processing time.
                if (updatedCardPayload == null) {
                    logger.error("Card Modify Failure - 400 [BAD REQUEST] - Payload is empty.");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload is missing and must be provided.");
                }

                // Checks keyword IDs exist
                for (Integer keyword : updatedCardPayload.getKeywords()) {
                    if (keywordRepository.findById(keyword).isEmpty()) {
                        logger.error("Keyword ID: {} not found", keyword);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keyword ID not found");
                    }
                }

                // Checks ID was sent
                if (updatedCardPayload.getCreatorId() != null) {
                    // Checks if the ID's match
                    if (updatedCardPayload.getCreatorId() != storedCard.get().getCreatorId()) {
                        if (!Authorization.isGAAorDGAA(currentUser)) {
                            logger.error("User doesn't have permission to set creator to a different user");
                            throw new ResponseStatusException(
                                    HttpStatus.FORBIDDEN,
                                    "User doesn't have permission to set creator to a different user"
                            );
                        }
                    }
                } else {
                    // sets Id to stored Id
                    updatedCardPayload.setCreatorId(storedCard.get().getCreatorId());
                }

                // Checks if title was sent
                if (updatedCardPayload.getTitle() == null) {
                    logger.error("Card Update Failure - 400 [BAD_REQUEST] - Title was not included");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title was not included");
                }

                // Checks if description was sent
                if (updatedCardPayload.getDescription() == null) {
                    logger.error("Card Update Failure - 400 [BAD_REQUEST] - Description was not included");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description was not included");
                }

                // Checks if section was sent and is valid
                if (updatedCardPayload.getSection() == null) {
                    logger.error("Card Update Failure - 400 [BAD_REQUEST] - Section was not included or invalid");
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Section was not included or invalid");
                }

                // Set changes
                storedCard.get().setCreatorId(updatedCardPayload.getCreatorId());
                storedCard.get().setTitle(updatedCardPayload.getTitle());
                storedCard.get().setDescription(updatedCardPayload.getDescription());
                storedCard.get().setSection(updatedCardPayload.getSection());
                storedCard.get().setKeywords(updatedCardPayload.getKeywords());

            } else {
                logger.error("User with ID: {} does no have permission to create this card.", currentUser.getId());
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "User does not have the permission to create this card."
                );
            }
        }
    }

    /**
     * Get response for retrieving a list of Marketplace Cards from a Section
     *
     * @param sessionToken JSESSIONID
     * @param section      Section of card
     * @param orderBy      Ordering
     * @param page         Page number
     * @return List of MarketplaceCardPayloads
     * @throws Exception when card can't be converted to payload (DTO).
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
        } catch (Exception e) {
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

        Sort sortBy;
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
        for (MarketplaceCard card : cards) {
            payload.add(card.toMarketplaceCardPayload());
        }

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(payload);
    }

    /**
     * GET method for retrieving a specific marketplace card.
     *
     * @param id Integer Id (primary key)
     * @return Marketplace card object if it exists
     */
    @GetMapping("/cards/{id}")
    public MarketplaceCardPayload retrieveMarketplaceCard(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id
    ) throws Exception {
        Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<MarketplaceCard> optionalMarketplaceCard = marketplaceCardRepository.findById(id);

        MarketplaceCard marketplaceCard = checkCardExists(optionalMarketplaceCard);

        logger.info("Marketplace Card Retrieval Success - 200 [OK] -  Marketplace card retrieved with ID {}", id);
        logger.debug("Marketplace card retrieved with ID {}: {}", id, marketplaceCard);

        return marketplaceCard.toMarketplaceCardPayload();
    }

    /**
     * PUT method for extending the display period of a marketplace card with a given ID.
     *
     * @param sessionToken Session token of the currently logged in user.
     * @param id The ID of the card the user wishes to extend the display period of.
     */
    @PutMapping("/cards/{id}/extenddisplayperiod")
    public void extendDisplayPeriod(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id) {
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<MarketplaceCard> optionalMarketplaceCard = marketplaceCardRepository.findById(id);

        MarketplaceCard marketplaceCard = checkCardExists(optionalMarketplaceCard);

        if (!Authorization.isGAAorDGAA(currentUser) && marketplaceCard.getCreatorId() != currentUser.getId()) {
            logger.error("Marketplace Card Modification Error - 403 [FORBIDDEN] - User with ID {} neither a GAA nor the creator of card with ID {}", currentUser.getId(), marketplaceCard.getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "The account performing the request is neither a global application admin nor the creator of this card."
            );
        }

        marketplaceCard.extendDisplayPeriod();
        marketplaceCardRepository.save(marketplaceCard);
        logger.info("Marketplace Card Modification Success - 200 [OK] - Marketplace card with ID {} has had its display period extended to {}.", id, marketplaceCard.getDisplayPeriodEnd());
    }

    /**
     * Checks that an optional that may or may not contain a marketplace card is not empty.
     * If it is, then throws a response.
     * However, if a card exists, then the card is returned.
     *
     * @param marketplaceCardOptional Optional<MarketplaceCard> The optional that may or may not contain a marketplace card.
     * @return MarketplaceCard The card that was in the Optional if one exists.
     */
    private MarketplaceCard checkCardExists(Optional<MarketplaceCard> marketplaceCardOptional) {
        if (marketplaceCardOptional.isEmpty()) {
            logger.error("Marketplace Card Retrieval Failure - 406 [NOT ACCEPTABLE] - Marketplace card does not exist");
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }
        return marketplaceCardOptional.get();
    }

}