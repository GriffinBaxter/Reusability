package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.MainApplicationRunner;
import org.seng302.exceptions.IllegalKeywordArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.*;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.MarketplaceCardRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.utils.PaginationUtils;
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
 * The GET /users/{id}/cards endpoint is used to retrieve all active cards from a given user by ID.
 * The PUT /cards/{id}/extenddisplayperiod endpoint is used to extend the display period of a card nearing expiry.
 * The PUT /cards/{id} endpoint is used to edit existing cards
 */
@RestController
public class MarketplaceCardResource {

    @Autowired
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private MarketCardNotificationRepository marketCardNotificationRepository;

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
            KeywordRepository keywordRepository, MarketCardNotificationRepository marketCardNotificationRepository
    ) {
        this.marketplaceCardRepository = marketplaceCardRepository;
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
        this.marketCardNotificationRepository = marketCardNotificationRepository;
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
                        List<Integer> keywordIds = cardPayload.getKeywordIds();
                        for (Integer keywordId : keywordIds) {
                            Optional<Keyword> existingKeyword = keywordRepository.findById(keywordId);
                            if (existingKeyword.isPresent()) { // If keyword exists then update existing keyword.
                                Keyword existingKeywordPresent = existingKeyword.get();
                                card.addKeyword(existingKeywordPresent);
                            } else {
                                throw new IllegalKeywordArgumentException(String.format("Keyword with ID %d not found", keywordId));
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
                    logger.error("Card Creation Failure [400]: {}", e.getMessage());
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
     * PUT endpoint for editing Marketplace Cards
     * The response status and reason is returned for the corresponding scenario.
     *
     * @param sessionToken Session token of user
     * @param updatedCardPayload Payload for the edited card
     * @param id id of the card to be edited
     *
     * Preconditions:  id is a positive integer that represents the id of an existing marketplace card.
     *                 updatedCardPayload is a non-null JSON representation of a marketplace card.
     * Postconditions: An updated marketplace card with the values of updatedCardPayload.
     *                 A 200 status code is returned.
     */
    @PutMapping("/cards/{id}")
    @ResponseStatus(code = HttpStatus.OK, reason = "Card updated successfully")
    public void editCard(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestBody(required = false) MarketplaceCardUpdatePayload updatedCardPayload,
            @PathVariable Integer id
    ) {
        logger.debug("Edit card payload received: {}", updatedCardPayload);

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Check to see if card exists.
        Optional<MarketplaceCard> storedCard = marketplaceCardRepository.findById(id);

        if (storedCard.isEmpty()) {
            logger.error("Card at ID: {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Card doesn't exist"
            );
        }

        // If user is GAA, DGAA or the user is also the creator then a card can be updated.
        // Otherwise the user is forbidden from updating the card.
        if (Authorization.isGAAorDGAA(currentUser) || currentUser.getId() == storedCard.get().getCreatorId()) {

            // Verify there is a payload. Otherwise we are wasting processing time.
            if (updatedCardPayload == null) {
                logger.error("Card Modify Failure - 400 [BAD REQUEST] - Payload is empty.");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload is missing and must be provided.");
            }

            // Checks keyword IDs exist
            List<Integer> keywordIds = updatedCardPayload.getKeywordIds();
            for (Integer keyword : keywordIds) {
                if (keywordRepository.findById(keyword).isEmpty()) {
                    logger.error("Keyword ID: {} not found", keyword);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keyword ID not found");
                }
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

            try {
                // Set changes
                storedCard.get().setTitle(updatedCardPayload.getTitle());
                storedCard.get().setDescription(updatedCardPayload.getDescription());
                storedCard.get().removeAllKeywords();
                for (Integer keyword : updatedCardPayload.getKeywordIds()) {
                    Optional<Keyword> optionalKeyword = keywordRepository.findById(keyword);
                    optionalKeyword.ifPresent(value -> storedCard.get().addKeyword(value));
                }
                marketplaceCardRepository.saveAndFlush(storedCard.get());

            } catch (IllegalMarketplaceCardArgumentException e) {
                logger.error("Card Update Failure - 400 [BAD_REQUEST] - {}", e.getMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }

            logger.info("Card Update Success - 200 [OK] - Successfully updated Card: {}", id);

        } else {
            logger.error("User with ID: {} does no have permission to update this card.", currentUser.getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User does not have the permission to update this card."
            );
        }
    }

    /**
     * Get response for retrieving a list of Marketplace Cards from a Section
     *
     * @param sessionToken JSESSIONID
     * @param section      Section of card
     * @param orderBy      Ordering
     * @param page         Page number
     * @param pageSize     Number of elements to return per page
     * @return List of MarketplaceCardPayloads
     * @throws Exception when card can't be converted to payload (DTO).
     */
    @GetMapping("/cards")
    public ResponseEntity<List<MarketplaceCardPayload>> retrieveMarketplaceCards(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam String section,
            @RequestParam(defaultValue = "createdDESC") String orderBy,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "6") String pageSize
    ) throws Exception {
        logger.debug("Get card request received with section {}, order by {}, page {}, page size {}", section, orderBy, page, pageSize);

        // Checks user logged in 401
        Authorization.getUserVerifySession(sessionToken, userRepository);

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
        int pageNo = PaginationUtils.parsePageNumber(page);
        int pageSizeNo = PaginationUtils.parsePageSizeNumber(pageSize);

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

        Pageable paging = PageRequest.of(pageNo, pageSizeNo, sortBy);

        Page<MarketplaceCard> pagedResult = marketplaceCardRepository.findAllBySection(sectionType, paging);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Get Marketplace Cards Success - 200 [OK] -  Cards retrieved for Section {}, order by {}, page {}, page size {}", section, orderBy, pageNo, pageSizeNo);
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
     * @param id           The ID of the card the user wishes to extend the display period of.
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

        // Delete all relate notifications
        Optional<MarketCardNotification> optionalMarketCardNotification = marketCardNotificationRepository.findByUserIdAndMarketCardId(currentUser.getId(), id);
        optionalMarketCardNotification.ifPresent(marketCardNotification -> marketCardNotificationRepository.delete(marketCardNotification));

        marketplaceCardRepository.save(marketplaceCard);
        logger.info("Marketplace Card Modification Success - 200 [OK] - Marketplace card with ID {} has had its display period extended to {}.", id, marketplaceCard.getDisplayPeriodEnd());
    }

    /**
     * GET method for retrieving all active cards that a given user has created.
     *
     * Contract:
     * Pre-conditions: Valid JSESSIONID cookie and user ID
     * Post-conditions: Returns active cards from given user
     *
     * @param sessionToken Session token of the currently logged in user.
     * @param id The ID of the user.
     * @return A list of all cards created by the user (possibly empty).
     */
    @GetMapping("/users/{id}/cards")
    public ResponseEntity<List<MarketplaceCardPayload>> retrieveUsersActiveCards(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer id
    ) throws Exception {
        Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<User> cardsUser = userRepository.findById(id);

        if (cardsUser.isEmpty()) {
            logger.error("406 [NOT ACCEPTABLE] - User with ID {} does not exist", id);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The given user does not exist.");
        } else {
            List<MarketplaceCard> cards = marketplaceCardRepository
                    .findMarketplaceCardByCreatorId(
                            id
                    );
            logger.info(
                    "Marketplace Retrieve Cards Success - 200 [OK] - User with ID {} has had its cards retrieved.",
                    id
            );

            List<MarketplaceCardPayload> payload = new ArrayList<>();
            for (MarketplaceCard card : cards) {
                LocalDateTime currentDateTime = LocalDateTime.now();

                if (card.getDisplayPeriodEnd().isAfter(currentDateTime)) {
                    payload.add(card.toMarketplaceCardPayload());
                }
            }

            return ResponseEntity.ok()
                    .body(payload);
        }
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

    /**
     * DELETE method for delete a specific marketplace card by given id.
     *
     * @param sessionToken session token for current user
     * @param id           id of the specific marketplace card
     */
    @DeleteMapping("/cards/{id}")
    @ResponseStatus(code = HttpStatus.OK, reason = "Card deleted successfully")
    public void deleteACard(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id
    ) {
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<MarketplaceCard> optionalMarketplaceCard = marketplaceCardRepository.findById(id);

        if (optionalMarketplaceCard.isEmpty()) {
            logger.error("Marketplace Card Delete Failure - 406 [NOT ACCEPTABLE] - Marketplace card with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        logger.debug("Retrieved marketplace card with ID {}: {}", id, optionalMarketplaceCard.get());

        MarketplaceCard marketplaceCard = optionalMarketplaceCard.get();

        if (currentUser.getRole() != Role.GLOBALAPPLICATIONADMIN
                && currentUser.getRole() != Role.DEFAULTGLOBALAPPLICATIONADMIN
                && currentUser != marketplaceCard.getCreator()) {
            logger.error("Marketplace Card Delete Failure - 403 [FORBIDDEN] - Current user have no permission" +
                    " to delete marketplace card with ID {}", id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Current user tries to delete a card that they are not the creator of AND the user is not a GAA."
            );
        }

        // delete selected card
        logger.debug("Marketplace card ({}) has been deleted.", marketplaceCard.getTitle());
        marketplaceCardRepository.delete(marketplaceCard);

        logger.info("Marketplace Card Delete Success - 200 [OK] -  Marketplace card with ID {} deleted", id);
        logger.debug("Delete marketplace card with ID {}: {}", id, marketplaceCard);

        // Create and save delete message for selected card
        MarketCardNotification deleteNotification = new MarketCardNotification(currentUser.getId(),
                null,
                String.format(MainApplicationRunner.DELETED_NOTIFICATION_MESSAGE, marketplaceCard.getTitle()),
                LocalDateTime.now());
        marketCardNotificationRepository.save(deleteNotification);

        logger.debug("Notification message ({}) has been saved.", deleteNotification.getDescription());
    }
}
