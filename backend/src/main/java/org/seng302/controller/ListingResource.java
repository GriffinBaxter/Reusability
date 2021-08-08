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
import org.seng302.exceptions.IllegalListingNotificationArgumentException;
import org.seng302.model.*;
import org.seng302.model.repository.*;
import org.seng302.utils.PaginationUtils;
import org.seng302.view.incoming.ListingCreationPayload;
import org.seng302.view.outgoing.*;

import org.seng302.Authorization;

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

    @Autowired
    private SoldListingRepository soldListingRepository;

    @Autowired
    private ListingNotificationRepository listingNotificationRepository;

    private static final Logger logger = LogManager.getLogger(ListingResource.class.getName());

    /**
     * Constructor used to insert mocked repositories for testing.
     *
     * @param listingRepository ListingRepository
     * @param inventoryItemRepository InventoryItemRepository
     * @param productRepository ProductRepository
     * @param businessRepository BusinessRepository
     * @param userRepository UserRepository
     * @param soldListingRepository SoldListingRepository
     * @param listingNotificationRepository ListingNotificationRepository
     */
    public ListingResource(ListingRepository listingRepository,
                           InventoryItemRepository inventoryItemRepository,
                           ProductRepository productRepository,
                           BusinessRepository businessRepository,
                           UserRepository userRepository,
                           SoldListingRepository soldListingRepository,
                           ListingNotificationRepository listingNotificationRepository) {
        this.listingRepository = listingRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.soldListingRepository = soldListingRepository;
        this.listingNotificationRepository = listingNotificationRepository;
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
            logger.error("406 [NOT ACCEPTABLE] - Select listing ({}) not exist", id);
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

    /**
     * PUT endpoint to purchase a given listing.
     * Creates a SoldListing to store the sale history for the business, creates a ListingNotification for the purchaser to remind them about the purchase,
     * creates a ListingNotification for the users who had the listing bookmarked to inform them of its removal, and deletes the Listing.
     *
     * @param sessionToken user's session token
     * @param id           given listing id
     */
    @PutMapping("/listings/{id}/buy")
    @ResponseStatus(value = HttpStatus.OK, reason = "Listing bought successfully")
    public void buyListing(@CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable String id) {
        // 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        logger.debug("User retrieved, ID: {}.", currentUser.getId());

        // 406
        Optional<Listing> optionalListing = listingRepository.findById(Integer.valueOf(id));
        if (optionalListing.isEmpty()) {
            logger.error("406 [NOT ACCEPTABLE] - Select listing ({}) not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Select listing not exist"
            );
        }

        Listing listing = optionalListing.get();
        String nameOfProduct = listing.getInventoryItem().getProduct().getName();
        logger.debug("Listing {} retrieved, ID: {}.", nameOfProduct, listing.getId());

        if (currentUser.getBusinessesAdministered().contains(listing.getBusinessId())) {
            logger.error("403 [FORBIDDEN] - Cannot purchase your own listing");
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Cannot purchase your own listing"
            );
        }

        Optional<Business> optionalBusiness = businessRepository.findBusinessById(listing.getBusinessId());
        if (optionalBusiness.isEmpty()) {
            logger.error("406 [NOT ACCEPTABLE] - Business with ID {} for listing with ID {} does not exist", listing.getBusinessId(), id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Business for listing does not exist"
            );
        }
        Business business = optionalBusiness.get();

        SoldListing soldListing = new SoldListing(business, currentUser, listing.getCreated(),
                                                    new ProductId(listing.getInventoryItem().getProduct().getProductId(),
                                                    listing.getBusinessId()), listing.getQuantity(), listing.getPrice(),
                                                    listing.getTotalBookmarks());
        soldListingRepository.save(soldListing);
        logger.info("Sold Listing Creation Success - Sold listing created for business with ID {}", listing.getBusinessId());

        try {
            String purchaserMessage = String.format("You have purchased %s x%d for $%.2f. Your purchase can be picked up from %s.",
                                                    listing.getInventoryItem().getProduct().getName(), listing.getQuantity(),
                                                    listing.getPrice(), business.getAddress().toOneLineString());
            ListingNotification purchaserListingNotification = new ListingNotification(purchaserMessage);
            purchaserListingNotification.addUser(currentUser);
            listingNotificationRepository.save(purchaserListingNotification);

            logger.info("Listing Notification Creation Success - Listing purchase notification created for purchaser");
        } catch (IllegalListingNotificationArgumentException e) {
            logger.error("Couldn't create listing purchase notification - {}", e.getMessage());
        }

        try {
            String bookmarkMessage = String.format("A listing you bookmarked, %s x%d from %s, has been removed.",
                                                    listing.getInventoryItem().getProduct().getName(), listing.getQuantity(),
                                                    business.getName());
            ListingNotification bookmarkListingNotification = new ListingNotification(bookmarkMessage);
            for (User user : listing.getBookmarkedListings()) {
                bookmarkListingNotification.addUser(user);
            }
            bookmarkListingNotification.removeUser(currentUser);
            listingNotificationRepository.save(bookmarkListingNotification);

            logger.info("Listing Notification Creation Success - Listing removal notification created");
        } catch (IllegalListingNotificationArgumentException e) {
            logger.error("Couldn't create listing removal notification - {}", e.getMessage());
        }

    }
}

