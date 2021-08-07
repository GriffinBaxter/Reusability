/**
 * Summary. This file contains the definition for the ListingResource.
 * <p>
 * Description. This file contains the defintion for the ListingResource.
 *
 * @link team-400/src/main/java/org/seng302/business/listing/ListingResource
 * @file This file contains the definition for ListingResource.
 * @author team-400.
 * @since 5.5.2021
 */
package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.seng302.exceptions.IllegalListingArgumentException;
import org.seng302.model.Business;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.repository.InventoryItemRepository;
import org.seng302.model.InventoryItem;
import org.seng302.model.Listing;
import org.seng302.utils.PaginationUtils;
import org.seng302.view.incoming.ListingCreationPayload;
import org.seng302.view.incoming.UserIdPayload;
import org.seng302.view.outgoing.*;
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

import static org.seng302.Authorization.verifyRole;

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
                           UserRepository userRepository) {
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

        logger.debug("Business listings retrieval request received with business ID {}, order by {}, page {}", id, orderBy, page);

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

    /**
     * Get method for retrieving a specific listing.
     * @param businessId Integer Id of business
     * @param listingId Integer Id of listing
     * @return Listing payload if it exists
     */
    @GetMapping("/businesses/{businessId}/listings/{listingId}")
    public ListingPayload retrieveListing(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer businessId,
            @PathVariable Integer listingId
    ) {
        logger.debug("Business sale listing retrieval request received with business ID {}, listing ID {}", businessId, listingId);

        // Checks user logged in - 401
        Authorization.getUserVerifySession(sessionToken, userRepository);
        // Verify business exists
        Authorization.verifyBusinessExists(businessId, businessRepository);
        // Retrieve listing from database
        Optional<Listing> listing = listingRepository.findListingByBusinessIdAndId(businessId, listingId);

        if (listing.isEmpty()) {
            logger.error("Listing Retrieval Failure - 400 [BAD REQUEST] - Sale listing at ID {} Not Found", listingId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sale Listing Not Found");
        }

        Listing returnedListing = listing.get();

        logger.info("Listing Retrieval Success - 200 [OK] -  Listing retrieved with ID {} and business ID {}", listingId, businessId);

        ListingPayload listingPayload = new ListingPayload(
                returnedListing.getId(),
                InventoryItemResource.convertToPayload(returnedListing.getInventoryItem()),
                returnedListing.getQuantity(),
                returnedListing.getPrice(),
                returnedListing.getMoreInfo(),
                returnedListing.getCreated().toString(),
                returnedListing.getCloses().toString());

        logger.debug("Listing retrieved for business with ID {}: {}", businessId, listing);

        return listingPayload;
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

    /**
     * Add/Remove given user from/to bookmark of given listing.
     *
     * @param sessionToken user's session token
     * @param id           given listing id
     * @return status of bookmark
     */
    @PutMapping("/listings/{id}/bookmark")
    public BookmarkStatusPayload updateBookmarkStatus(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                      @PathVariable String id) {
        // 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        logger.debug("User retrieved, ID: {}.", currentUser.getId());

        // 406
        Optional<Listing> optionalListing = listingRepository.findById(Integer.valueOf(id));
        if (optionalListing.isEmpty()) {
            logger.error("406 [BAD REQUEST] - Select listing ({}) not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Select listing not exist"
            );
        }

        Listing listing = optionalListing.get();
        String nameOfProduct = listing.getInventoryItem().getProduct().getName();
        logger.debug("Listing {} retrieved, ID: {}.", nameOfProduct, listing.getId());

        boolean currentStatus;
        if (Boolean.TRUE.equals(listing.isBookmarked(currentUser))) {
            // if marked before
            listing.removeUserFromABookmark(currentUser);
            currentStatus = false;
            logger.info("Listing {} has been add to current user's (Id: {}) bookmark.", nameOfProduct, currentUser.getId());
        } else {
            // if not marked before
            listing.addUserToANewBookmark(currentUser);
            currentStatus = true;
            logger.info("Listing {} has been remove from current user's (Id: {}) bookmark.", nameOfProduct, currentUser.getId());
        }

        // Save status change
        listingRepository.save(listing);
        logger.debug("Status ({}) change saved!", currentStatus);

        return new BookmarkStatusPayload(currentStatus);
    }
}

