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
import org.seng302.model.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeywordResource {

    @Autowired
    private KeywordRepository keywordRepository;

    private static final Logger logger = LogManager.getLogger(KeywordResource.class.getName());

    public KeywordResource(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }
}
