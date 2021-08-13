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
import org.seng302.model.enums.BusinessType;
import org.seng302.model.repository.*;
import org.seng302.utils.PaginationUtils;
import org.seng302.utils.SearchUtils;
import org.seng302.view.incoming.ListingCreationPayload;
import org.seng302.view.outgoing.*;

import org.seng302.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
                                                                 @RequestParam(defaultValue = "0") String page) throws Exception {

        logger.debug("Business listings retrieval request received with business ID {}, order by {}, page {}", id, orderBy, page);

        // Checks user logged in - 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

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

        List<ListingPayload> listingPayloads = convertToPayloadList(pagedResult.getContent(), currentUser);

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
     * Search for listings with filtering and ordering.
     * Returns paginated and ordered results based on input query params.
     *
     * @param sessionToken Session token used to authenticate user (is user logged in?).
     * @param searchQuery Search query.
     * @param searchType Search type.
     * @param orderBy Column to order the results by.
     * @param page Page number to return results from.
     * @param businessType Business type to search by.
     * @param minimumPrice Minimum price.
     * @param maximumPrice Maximum price.
     * @param fromDate From date (closing).
     * @param toDate To date (closing).
     * @return A list of ListingPayload objects matching the search query
     */
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) throws Exception {
        logger.debug(
                "Listing search request received with search query {}, business type {}, order by {}, page {}",
                searchQuery, businessType, orderBy, page
        );

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        int pageNo = PaginationUtils.parsePageNumber(page);

        // Front-end displays 9 listings per page
        int pageSize = 9;

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
                        "inventoryItemId.product.business.address.country"
                ).ignoreCase());
                break;
            case "countryDESC":
                sortBy = Sort.by(Sort.Order.desc(
                        "inventoryItemId.product.business.address.country"
                ).ignoreCase());
                break;
            case "cityASC":
                sortBy = Sort.by(Sort.Order.asc("inventoryItemId.product.business.address.city").ignoreCase());
                break;
            case "cityDESC":
                sortBy = Sort.by(Sort.Order.desc("inventoryItemId.product.business.address.city").ignoreCase());
                break;
            case "expiryDateASC":
                sortBy = Sort.by(Sort.Order.asc("inventoryItemId.expires").ignoreCase());
                break;
            case "expiryDateDESC":
                sortBy = Sort.by(Sort.Order.desc("inventoryItemId.expires").ignoreCase());
                break;
            case "sellerNameASC":
                sortBy = Sort.by(Sort.Order.asc("inventoryItemId.product.business.name").ignoreCase());
                break;
            case "sellerNameDESC":
                sortBy = Sort.by(Sort.Order.desc("inventoryItemId.product.business.name").ignoreCase());
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

        logger.debug("Listings Found");
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(ListingPayload.toListingPayload(pagedResult.getContent(), currentUser));
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
    ) throws Exception {
        logger.debug("Business sale listing retrieval request received with business ID {}, listing ID {}", businessId, listingId);

        // Checks user logged in - 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
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

        logger.debug("Listing retrieved for business with ID {}: {}", businessId, listing);

        return convertToPayload(returnedListing, currentUser);
    }

    /**
     * Converts a list of Listings to a list of ListingPayloads.
     * @param listingList The given list of listings
     * @param user The User who requested the listings
     * @return A list of ListingPayloads.
     */
    public List<ListingPayload> convertToPayloadList(List<Listing> listingList, User user) throws Exception {
        List<ListingPayload> payloads = new ArrayList<>();
        for (Listing listing : listingList) {
            ListingPayload newPayload = convertToPayload(listing, user);
            logger.debug("Listing payload created: {}", newPayload);
            payloads.add(newPayload);
        }
        return payloads;
    }

    /**
     * Converts a Listing to a ListingPayload.
     * @param listing The given listing
     * @param user The User who requested the listing
     * @return A ListingPayload.
     */
    public ListingPayload convertToPayload(Listing listing, User user) throws Exception {
        ListingPayload newPayload = new ListingPayload(
                listing.getId(),
                listing.getInventoryItem().convertToPayload(),
                listing.getQuantity(),
                listing.getPrice(),
                listing.getMoreInfo(),
                listing.getCreated().toString(),
                listing.getCloses().toString(),
                listing.isBookmarked(user),
                listing.getTotalBookmarks()
        );
        logger.debug("Listing payload created: {}", newPayload);
        return newPayload;
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
            logger.info("Listing {} has been remove from current user's (Id: {}) bookmark.", nameOfProduct, currentUser.getId());
        } else {
            // if not marked before
            listing.addUserToANewBookmark(currentUser);
            currentStatus = true;
            logger.info("Listing {} has been add to current user's (Id: {}) bookmark.", nameOfProduct, currentUser.getId());
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
                    "Listing does not exist"
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
            logger.error("500 [INTERNAL SERVER ERROR] - Business with ID {} for listing with ID {} does not exist", listing.getBusinessId(), id);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Business for listing does not exist"
            );
        }
        Business business = optionalBusiness.get();

        Optional<InventoryItem> optionalInventoryItem = inventoryItemRepository.findInventoryItemById(listing.getInventoryItem().getId());
        if (optionalInventoryItem.isEmpty()) {
            logger.error("500 [INTERNAL SERVER ERROR] - Inventory item with ID {} for listing with ID {} does not exist", listing.getInventoryItem().getId(), id);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Inventory item for listing does not exist"
            );
        }

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

        listingRepository.delete(listing);
        logger.info("Listing Notification Deletion Success - Listing with ID {} has been deleted", id);

        logger.debug("Inventory item retrieved, ID: {}.", listing.getInventoryItem().getId());
        InventoryItem inventoryItem = optionalInventoryItem.get();
        inventoryItem.setQuantity(inventoryItem.getQuantity() - listing.getQuantity());

        inventoryItemRepository.save(inventoryItem);
    }

    /**
     * This method parses the search criteria and then calls the needed methods to execute the "query".
     *
     * @param searchQuery Criteria to search for listings.
     * @param paging Information used to paginate the retrieved listings.
     * @param searchType Search type.
     * @param businessType Criteria to search for listings using business type.
     * @param minimumPrice Minimum price.
     * @param maximumPrice Maximum price.
     * @param fromDate From date (closing).
     * @param toDate To date (closing).
     * @return Page<Listing> A page of listings matching the search criteria.
     */
    private Page<Listing> parseAndExecuteQuery(
            String searchQuery, Pageable paging,
            String searchType,
            String businessType,
            Double minimumPrice, Double maximumPrice,
            LocalDateTime fromDate, LocalDateTime toDate
    ) {
        BusinessType convertedBusinessType = toBusinessType(businessType);
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        switch (searchType) {
            case "listingName":
                return listingRepository.findAllListingsByProductName(
                        names, paging, convertedBusinessType, minimumPrice, maximumPrice, fromDate, toDate
                );
            case "businessName":
                return listingRepository.findAllListingsByBusinessName(
                        names, paging, convertedBusinessType, minimumPrice, maximumPrice, fromDate, toDate
                );
            case "location":
                return listingRepository.findAllListingsByLocation(
                        names, paging, convertedBusinessType, minimumPrice, maximumPrice, fromDate, toDate
                );
            default:
                logger.error("400 [BAD REQUEST] - {} is not a valid search type parameter", searchType);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "searchType Field invalid"
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
