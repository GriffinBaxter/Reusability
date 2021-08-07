/**
 * Summary. This file contains the definition for the ListingResource.
 *
 * Description. This file contains the defintion for the ListingResource.
 *
 * @link   team-400/src/main/java/org/seng302/business/listing/ListingResource
 * @file   This file contains the definition for ListingResource.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.seng302.exceptions.IllegalListingArgumentException;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.repository.InventoryItemRepository;
import org.seng302.model.InventoryItem;
import org.seng302.model.Listing;
import org.seng302.utils.PaginationUtils;
import org.seng302.utils.SearchUtils;
import org.seng302.view.incoming.ListingCreationPayload;
import org.seng302.view.outgoing.ListingPayload;
import org.seng302.model.repository.ListingRepository;
import org.seng302.model.repository.ProductRepository;

import org.seng302.Authorization;

import org.seng302.model.repository.UserRepository;
import org.seng302.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * ListingResource class
 */
@RestController
public class ListingResource {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(ListingResource.class.getName());

    /**
     * Constructor used to insert mocked repositories for testing.
     *
     * @param listingRepository ListingRepository
     * @param inventoryItemRepository InventoryItemRepository
     * @param productRepository ProductRepository
     * @param businessRepository BusinessRepository
     * @param userRepository UserRepository
     */
    public ListingResource(ListingRepository listingRepository,
                           InventoryItemRepository inventoryItemRepository,
                           ProductRepository productRepository,
                           BusinessRepository businessRepository,
                           UserRepository userRepository)
    {
        this.listingRepository = listingRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get method for retrieving listings
     * @param sessionToken when a user is logged in they have a session token which can be used to identify them.
     * @param id business ID
     * @param orderBy ordering of results
     * @param page page number
     * @return Listings for business
     */
    @GetMapping("/businesses/{id}/listings")
    public ResponseEntity<List<ListingPayload>> retrieveListings(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                                 @PathVariable Integer id,
                                                                 @RequestParam(defaultValue = "closesASC") String orderBy,
                                                                 @RequestParam(defaultValue = "0") String page) {

        logger.debug("Product inventory retrieval request received with business ID {}, order by {}, page {}", id, orderBy, page);

        // Checks user logged in - 401
        Authorization.getUserVerifySession(sessionToken, userRepository);

        Authorization.verifyBusinessExists(id, businessRepository);

        // Checks Page Num valid - 400
        int pageNo = PaginationUtils.parsePageNumber(page);

        // Front-end displays 10 listings per page
        int pageSize = 5;

        Sort sortBy;

        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "quantityASC":
                sortBy = Sort.by(Sort.Order.asc("quantity").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "quantityDESC":
                sortBy = Sort.by(Sort.Order.desc("quantity").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "priceASC":
                sortBy = Sort.by(Sort.Order.asc("price").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "priceDESC":
                sortBy = Sort.by(Sort.Order.desc("price").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "closesASC":
                sortBy = Sort.by(Sort.Order.asc("closes").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "closesDESC":
                sortBy = Sort.by(Sort.Order.desc("closes").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "createdASC":
                sortBy = Sort.by(Sort.Order.asc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "createdDESC":
                sortBy = Sort.by(Sort.Order.desc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            default:    // Order By value not valid - 400
                logger.error("400 [BAD REQUEST] - {} is not a valid order by parameter", orderBy);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OrderBy Field invalid"
                );
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        Page<Listing> pagedResult = listingRepository.findListingsByBusinessId(id, paging);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Listing Retrieval Success - 200 [OK] -  Listings retrieved for business with ID {}", id);

        List<ListingPayload> listingPayloads = convertToPayload(pagedResult.getContent());

        logger.debug("Listings retrieved for business with ID {}: {}", id, listingPayloads);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(listingPayloads);
    }

    /**
     * Create a new Listing belonging to the business with the given business ID.
     *
     * @param sessionToken Session Token
     * @param id Business ID
     * @param listingPayload listing creation payload
     */
    @PostMapping("/businesses/{id}/listings")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Listing Created successfully")
    public void createListing(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                @PathVariable Integer id,
                @RequestBody ListingCreationPayload listingPayload) {
        logger.debug("Listing payload received: {}", listingPayload);
        // Checks if User is logged in 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Checks Business Exists 406
        Authorization.verifyBusinessExists(id, businessRepository);

        // Checks User is Admin 403
        Authorization.verifyBusinessAdmin(currentUser, id);

        // Checks InventoryItem exists and gets InventoryItem
        Optional<InventoryItem> inventoryItem = inventoryItemRepository.findInventoryItemById(Integer.parseInt(listingPayload.getInventoryItemId()));
        if (inventoryItem.isEmpty()) {
            logger.error("Listing Creation Failure - 400 [BAD REQUEST] - Inventory Item at ID {} Not Found", listingPayload.getInventoryItemId());
            throw new ResponseStatusException(
                     HttpStatus.BAD_REQUEST,
                    "Inventory Item Not Found");
        }

        Integer quantity = listingPayload.getQuantity();
        Double price = listingPayload.getPrice();
        String moreInfo = listingPayload.getMoreInfo();
        LocalDateTime closes = listingPayload.getCloses();
        LocalDateTime created = LocalDateTime.now();

        // Creates Listing
        try {
            Listing listing = new Listing(
                inventoryItem.get(),
                quantity,
                price,
                moreInfo,
                created,
                closes
            );
            listingRepository.save(listing);

            logger.info("Listing Creation Success - 201 [CREATED] - Listing created for business with ID {}", id);
        } catch (IllegalListingArgumentException e) {
            logger.error("Couldn't make listing {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Bad Request - Couldn't make listing"
            );
        }
    }

    @GetMapping("/listings")
    public ResponseEntity<List<ListingPayload>> searchListings(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam(defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "listingName") String searchType,
            @RequestParam(defaultValue = "productNameASC") String orderBy,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "") String businessType,
            @RequestParam(required = false) Double minimumPrice,
            @RequestParam(required = false) Double maximumPrice,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate
    ) {
        logger.debug(
                "Listing search request received with search query {}, business type {}, order by {}, page {}",
                searchQuery, businessType, orderBy, page
        );

        Authorization.getUserVerifySession(sessionToken, userRepository);

        int pageNo = PaginationUtils.parsePageNumber(page);

        // Front-end displays 10 listings per page
        int pageSize = 10;

        Sort sortBy;
        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "productNameASC":
                sortBy = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
                break;
            case "productNameDESC":
                sortBy = Sort.by(Sort.Order.desc("inventoryItemId.product.name").ignoreCase());
                break;
            case "countryASC":
                sortBy = Sort.by(Sort.Order.asc(
                        "inventoryItemId.product.business_id.address_id.country"
                ).ignoreCase());
                break;
            case "countryDESC":
                sortBy = Sort.by(Sort.Order.desc(
                        "inventoryItemId.product.business_id.address_id.country"
                ).ignoreCase());
                break;
            case "cityASC":
                sortBy = Sort.by(Sort.Order.asc("inventoryItemId.product.business_id.address_id.city").ignoreCase());
                break;
            case "cityDESC":
                sortBy = Sort.by(Sort.Order.desc("inventoryItemId.product.business_id.address_id.city").ignoreCase());
                break;
            case "expiryDateASC":
                sortBy = Sort.by(Sort.Order.asc("inventoryItemId.expires").ignoreCase());
                break;
            case "expiryDateDESC":
                sortBy = Sort.by(Sort.Order.desc("inventoryItemId.expires").ignoreCase());
                break;
            case "sellerNameASC":
                sortBy = Sort.by(Sort.Order.asc("inventoryItemId.product.business_id.name").ignoreCase());
                break;
            case "sellerNameDESC":
                sortBy = Sort.by(Sort.Order.desc("inventoryItemId.product.business_id.name").ignoreCase());
                break;
            case "priceASC":
                sortBy = Sort.by(Sort.Order.asc("price").ignoreCase());
                break;
            case "priceDESC":
                sortBy = Sort.by(Sort.Order.desc("price").ignoreCase());
                break;
            default:
                logger.error("400 [BAD REQUEST] - {} is not a valid order by parameter", orderBy);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OrderBy Field invalid"
                );
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);
        Page<Listing> pagedResult = parseAndExecuteQuery(
                searchQuery, paging, searchType, businessType, minimumPrice, maximumPrice, fromDate, toDate
        );

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info(
                "Search Success - 200 [OK] - Listings retrieved for search query {}, business type {}, order by {}, page {}",
                searchQuery, businessType, orderBy, pageNo
        );

        logger.debug("Listings Found: {}", pagedResult.toList());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(ListingPayload.toListingPayload(pagedResult.getContent()));
    }

    /**
     * Converts a list of Listings to a list of ListingPayloads.
     * @param listingList The given list of listings
     * @return A list of productPayloads.
     */
    public List<ListingPayload> convertToPayload(List<Listing> listingList) {
        List<ListingPayload> payloads = new ArrayList<>();
        for (Listing listing : listingList) {
            ListingPayload newPayload = new ListingPayload(
                    listing.getId(),
                    InventoryItemResource.convertToPayload(listing.getInventoryItem()),
                    listing.getQuantity(),
                    listing.getPrice(),
                    listing.getMoreInfo(),
                    listing.getCreated().toString(),
                    listing.getCloses().toString()
            );
            logger.debug("Listing payload created: {}", newPayload);
            payloads.add(newPayload);
        }
        return payloads;
    }

    private Page<Listing> parseAndExecuteQuery(
            String searchQuery, Pageable paging,
            String searchType,
            String businessType,
            Double minimumPrice, Double maximumPrice,
            LocalDateTime fromDate, LocalDateTime toDate
    ) {
        BusinessType convertedBusinessType = toBusinessType(businessType);
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        if (searchType.equals("businessName")) {
            return listingRepository.findAllListingsByBusinessName(
                    names, paging, convertedBusinessType, minimumPrice, maximumPrice, fromDate, toDate
            );
        } else if (searchType.equals("location")) {
            return listingRepository.findAllListingsByLocation(
                    names, paging, convertedBusinessType, minimumPrice, maximumPrice, fromDate, toDate
            );
        } else {
            return listingRepository.findAllListingsByProductName(
                    names, paging, convertedBusinessType, minimumPrice, maximumPrice, fromDate, toDate
            );
        }
    }

    /**
     * Converts a string representation of business type to a enum representation (BusinessType). If the string does
     * not represent a valid business type then null is returned.
     * @param type A string representing business type.
     * @return An enum representation of business type (null if string representation is not valid).
     *
     * Preconditions:  A string representation of a valid business type.
     * Postconditions: An enum representation of business type.
     */
    private BusinessType toBusinessType(String type) {
        BusinessType businessType = null;
        if (type.equalsIgnoreCase("ACCOMMODATION_AND_FOOD_SERVICES")) {
            businessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;
        } else if (type.equalsIgnoreCase("RETAIL_TRADE")) {
            businessType = BusinessType.RETAIL_TRADE;
        } else if (type.equalsIgnoreCase("CHARITABLE_ORGANISATION")) {
            businessType = BusinessType.CHARITABLE_ORGANISATION;
        } else if (type.equalsIgnoreCase("NON_PROFIT_ORGANISATION")) {
            businessType = BusinessType.NON_PROFIT_ORGANISATION;
        }
        return businessType;
    }
}
