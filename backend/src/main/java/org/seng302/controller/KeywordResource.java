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
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.incoming.KeywordCreationPayload;
import org.seng302.view.outgoing.KeywordIdPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Keyword Resource class.
 * This class contains the endpoints for keywords.
 * The POST /keywords endpoint is used to create keywords.
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
}
