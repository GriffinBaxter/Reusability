package org.seng302.marketplace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.business.BusinessResource;
import org.seng302.main.Authorization;
import org.seng302.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Section sectionType = Section.valueOf(section.toUpperCase());
        if (sectionType == null) {
            logger.error("400 [BAD REQUEST] - {} is not a valid sectopn", section);
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
        int pageSize = 20;

        Sort sortBy = null;
        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) { // TODO location orderBy
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
