package org.seng302.marketplace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@RestController
public class MarketplaceCardResource {

    @Autowired
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(BusinessResource.class.getName());

    public MarketplaceCardResource(MarketplaceCardRepository marketplaceCardRepository, UserRepository userRepository) {
        this.marketplaceCardRepository = marketplaceCardRepository;
        this.userRepository = userRepository;
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
