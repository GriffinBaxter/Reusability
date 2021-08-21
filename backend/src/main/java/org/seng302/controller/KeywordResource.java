/**
 * Summary. This file contains the definition for the KeywordResource.
 *
 * Description. This file contains the definition for the KeywordResource.
 *
 * @link   team-400/src/main/java/org/seng302/business/KeywordResource
 * @file   This file contains the definition for KeywordResource.
 * @author team-400.
 * @since  30.6.2021
 */

package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.exceptions.IllegalKeywordArgumentException;
import org.seng302.model.Keyword;
import org.seng302.model.KeywordNotification;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.repository.KeywordNotificationRepository;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.incoming.KeywordCreationPayload;
import org.seng302.view.outgoing.KeywordIdPayload;
import org.seng302.view.outgoing.KeywordPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Keyword Resource class.
 * This class contains the endpoints for keywords.
 * The POST /keywords endpoint is used to create keywords.
 * The DELETE /keywords endpoint is used to delete keywords.
 * The GET /keywords/search endpoint is used for searching for keywords by name
 */
@RestController
public class KeywordResource {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeywordNotificationRepository keywordNotificationRepository;

    private static final Logger logger = LogManager.getLogger(KeywordResource.class.getName());

    public KeywordResource(KeywordRepository keywordRepository, UserRepository userRepository, KeywordNotificationRepository keywordNotificationRepository) {
        this.keywordRepository = keywordRepository;
        this.userRepository = userRepository;
        this.keywordNotificationRepository = keywordNotificationRepository;
    }

    /**
     * POST /keywords endpoint for creating new keywords
     *
     * Preconditions: Valid JSESSIONID and keywordPayload are sent
     * Postconditions: A keyword is created
     *                 The keyword ID is sent back
     *
     * @param sessionToken JSESSIONID for verifying the user is logged in
     * @param keywordPayload Payload containing keyword name
     * @return Response Entity containing keyword id
     */
    @PostMapping("/keywords")
    public ResponseEntity<KeywordIdPayload> createKeyword(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                          @RequestBody KeywordCreationPayload keywordPayload) {
        logger.debug("Keyword payload received: {}", keywordPayload);

        Authorization.getUserVerifySession(sessionToken, userRepository);

        String keyword = keywordPayload.getName();

        // Checks if keyword already exists & returns ID if it does
        Optional<Keyword> foundKey = keywordRepository.findByName(keyword);
        if (foundKey.isPresent()) {
            logger.info("Keyword already exists - [OK]");
            return ResponseEntity.status(HttpStatus.OK).body(new KeywordIdPayload(foundKey.get().getId()));
        }

        LocalDateTime created = LocalDateTime.now();
        try {
            Keyword newKeyword = new Keyword(keyword, created);
            keywordRepository.save(newKeyword);
            logger.info("Keyword {} successfully created - [CREATED]", keyword);

            try {
                KeywordNotification keywordNotification = new KeywordNotification(String.format("A new keyword, %s, has been created.", newKeyword.getName()), created, newKeyword);
                keywordNotificationRepository.save(keywordNotification);

                logger.info("Keyword notification created successfully: {}", keywordNotification);
            } catch (Exception e) {
                logger.error("Keyword Notification Creation Failure - {}", e, e);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(new KeywordIdPayload(newKeyword.getId()));
        } catch (IllegalKeywordArgumentException e) {
            logger.info("Keyword Creation Failure - [BAD REQUEST] - Invalid keyword name {}", keyword);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        } catch (Exception e) {
            logger.info("Keyword Creation Failure - {}", e, e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    /**
     * GET /keywords/search endpoint for searching keywords
     *
     * Preconditions: Valid JSESSIONID
     * Postconditions: Returns a list of Keywords matching searchQuery (up to 6 matches)
     *
     * @param sessionToken JSESSIONID for verifying the user is logged in
     * @param searchQuery Query for searching keyword names
     * @return Payload containing a list of (possibly 0) keywords
     */
    @GetMapping("/keywords/search")
    public ResponseEntity<List<KeywordPayload>> searchKeywords(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam String searchQuery
    ) {
        // 401
        Authorization.getUserVerifySession(sessionToken, userRepository);

        List<Keyword> result = keywordRepository.findTop6ByNameIgnoreCaseContaining(searchQuery);

        logger.info("Search Success - 200 [OK] -  Keywords retrieved for search query {}", searchQuery);
        logger.debug("Keywords Found: {}", result);

        List<KeywordPayload> keywordPayload = convertToPayload(result);

        return ResponseEntity.ok()
                .body(keywordPayload);
    }

    /**
     * Function for converting a list of keywords into a payload
     * @param keywords List of keywords to convert
     * @return A list of Keyword Payloads
     */
    public List<KeywordPayload> convertToPayload(List<Keyword> keywords) {
        List<KeywordPayload> payload = new ArrayList<>();
        for (Keyword keyword : keywords) {
            KeywordPayload keywordPay = new KeywordPayload(keyword.getId(),keyword.getName(),keyword.getCreated());
            payload.add(keywordPay);
        }
        return payload;
    }

    /**
     * DELETE endpoint for deleting keywords (DGAA/GAA's only)
     *
     * Preconditions: Valid JSESSIONID and user is DGAA/GAA
     *                Id is an existing keyword id
     * Postconditions: Keyword is deleted
     *
     * @param id Keyword ID
     * @param sessionToken JSESSIONID for verifying the user is logged in
     */
    @DeleteMapping("/keywords/{id}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK, reason = "Keyword Successfully deleted")
    public void deleteKeyword( @PathVariable Integer id,
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken
    ) {
        logger.info("Request to delete keyword: {}", id);

        // Checks if user is logged in
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Checks user is GAA/DGAA
        if (!Authorization.isGAAorDGAA(currentUser)) {
            logger.error("Keyword Deletion Error - 403 [FORBIDDEN] - User doesn't have permissions to delete keywords");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid permissions to delete keywords");
        }

        logger.debug("Keyword Deletion Update - User has permissions to delete keywords");

        Optional<Keyword> keyword = keywordRepository.findById(id);

        if (keyword.isEmpty()) {
            logger.error("Keyword Deletion Error - 400 [BAD_REQUEST] - Keyword at ID {} not found", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keyword not found");
        }

        logger.debug("Keyword Deletion Update - Keyword found");

        List<MarketplaceCard> cards = keyword.get().getCards();
        for (MarketplaceCard card: cards) {
            card.removeKeyword(keyword.get());
        }

        logger.debug("Keyword Deletion Update - Keyword removed from cards");

        keywordNotificationRepository.deleteAllByKeywordId(id);
        logger.debug("Keyword Notification Deletion - Keyword notifications deleted for keyword with ID {}", id);
        keywordRepository.delete(keyword.get());
        logger.info("Keyword Deletion - 200 [OK] - Keyword at id {} successfully deleted", id);
    }
}
