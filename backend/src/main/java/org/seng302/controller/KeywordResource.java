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
import org.seng302.model.Keyword;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.incoming.KeywordCreationPayload;
import org.seng302.view.outgoing.KeywordIdPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Keyword Resource class.
 * This class contains the endpoints for keywords.
 * The POST /keywords endpoint is used to create keywords.
 * The DELETE /keywords/{id} endpoint is used to delete keywords.
 */
@RestController
public class KeywordResource {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(KeywordResource.class.getName());

    public KeywordResource(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    /**
     * POST /keywords endpoint for creating new keywords
     * @param sessionToken JSESSIONID for verifying the user is logged in
     * @param keywordPayload Payload containing keyword name
     * @return Response Entity containing keyword id
     */
    @PostMapping("/keywords")
    public ResponseEntity<KeywordIdPayload> createKeyword(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                          @RequestBody KeywordCreationPayload keywordPayload) {
        logger.debug("Card payload received: {}", keywordPayload);

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
            return ResponseEntity.status(HttpStatus.CREATED).body(new KeywordIdPayload(newKeyword.getId()));
        } catch(Exception e) {
            logger.info("Keyword creation Failure - [BAD REQUEST] - Invalid keyword name {}", keyword);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
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

        keywordRepository.delete(keyword.get());
        logger.info("Keyword Deletion - 200 [OK] - Keyword at id {} successfully deleted", id);
    }
}
