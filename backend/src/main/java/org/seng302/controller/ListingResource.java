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
import org.seng302.exceptions.IllegalSoldListingArgumentException;
import org.seng302.exceptions.IllegalSoldListingNotificationArgumentException;
import org.seng302.model.*;
import org.seng302.model.repository.*;
import org.seng302.exceptions.IllegalListingNotificationArgumentException;
import org.seng302.model.enums.BusinessType;
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

    @Autowired
    private SoldListingNotificationRepository soldListingNotificationRepository;

    @Autowired
    private BookmarkedListingMessageRepository bookmarkedListingMessageRepository;

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
     * @param soldListingNotificationRepository SoldListingNotificationRepository
     */
    public ListingResource(ListingRepository listingRepository,
                           InventoryItemRepository inventoryItemRepository,
                           ProductRepository productRepository,
                           BusinessRepository businessRepository,
                           UserRepository userRepository,
                           SoldListingRepository soldListingRepository,
                           ListingNotificationRepository listingNotificationRepository,
                           SoldListingNotificationRepository soldListingNotificationRepository,
                           BookmarkedListingMessageRepository bookmarkedListingMessageRepository) {
        this.listingRepository = listingRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.soldListingRepository = soldListingRepository;
        this.listingNotificationRepository = listingNotificationRepository;
        this.soldListingNotificationRepository = soldListingNotificationRepository;
        this.bookmarkedListingMessageRepository = bookmarkedListingMessageRepository;
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
     * @param businessTypes Business types to search by.
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
            @RequestParam(required = false) List<String> businessTypes,
            @RequestParam(required = false) Double minimumPrice,
            @RequestParam(required = false) Double maximumPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) throws Exception {
        logger.debug(
                "Listing search request received with search query {}, business type {}, order by {}, page {}",
                searchQuery, businessTypes, orderBy, page
        );

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        int pageNo = PaginationUtils.parsePageNumber(page);

        // Front-end displays 12 listings per page
        int pageSize = 12;

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
                searchQuery, paging, searchType, businessTypes, minimumPrice, maximumPrice, fromDate, toDate
        );

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info(
                "Search Success - 200 [OK] - Listings retrieved for search query {}, business type {}, order by {}, page {}",
                searchQuery, businessTypes, orderBy, pageNo
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
     * Get method for retrieving a list of sold Listings for a business
     * @param sessionToken Session Token
     * @param businessId ID of business
     * @param page Page number to retrieve
     * @return The requested page of Sold Listings
     */
    @GetMapping("/businesses/{businessId}/soldListings")
    public ResponseEntity<List<SoldListingPayload>> retrieveSoldListing(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer businessId,
            @RequestParam(defaultValue = "0") String page
    ) throws Exception {
        // Checks user logged in - 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        // Checks business at ID exists - 406
        Authorization.verifyBusinessExists(businessId, businessRepository);
        // Checks user is business admin - 403
        Authorization.verifyBusinessAdmin(currentUser, businessId);
        // Checks page number is valid - 400
        int pageNo = PaginationUtils.parsePageNumber(page);

        // Paging
        Sort sortBy = Sort.by(Sort.Order.desc("saleDate"));
        Pageable pageable = PageRequest.of(pageNo, 10, sortBy);

        Page<SoldListing> pagedResult = soldListingRepository.findAllByBusinessId(businessId, pageable);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Sold Listing Retrieval Success - 200 [OK] - Sold Listings retrieved for business with ID {}", businessId);

        List<SoldListingPayload> listingPayloads = convertToSoldPayload(pagedResult.getContent());

        logger.debug("Sold Listings retrieved for business with ID {}: {}", businessId, listingPayloads);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(listingPayloads);
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

        LocalDateTime created = LocalDateTime.now();

        boolean currentStatus;
        if (Boolean.TRUE.equals(listing.isBookmarked(currentUser))) {
            // if marked before
            listing.removeUserFromABookmark(currentUser);
            currentStatus = false;

            try {
                BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage(
                        String.format("Bookmark for product listing '%s' has been removed.", listing.getInventoryItem().getProduct().getName()),
                        created,
                        listing);
                bookmarkedListingMessage.addUser(currentUser);
                bookmarkedListingMessageRepository.save(bookmarkedListingMessage);

                logger.info("Bookmarked listing message created successfully: {}", bookmarkedListingMessage);
            } catch (Exception ex) {
                logger.error("Bookmarked listing message creation failure - {}", ex, ex);
            }

            logger.info("Listing {} has been removed from current user's (Id: {}) bookmarks.", nameOfProduct, currentUser.getId());
        } else {
            // if not marked before
            listing.addUserToANewBookmark(currentUser);
            currentStatus = true;

            try {
                BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage(
                        String.format("Product listing '%s' has been bookmarked.", listing.getInventoryItem().getProduct().getName()),
                        created,
                        listing);
                bookmarkedListingMessage.addUser(currentUser);
                bookmarkedListingMessageRepository.save(bookmarkedListingMessage);

                logger.info("Bookmarked listing message created successfully: {}", bookmarkedListingMessage);
            } catch (Exception ex) {
                logger.error("Bookmarked listing message creation failure - {}", ex, ex);
            }

            logger.info("Listing {} has been added to current user's (Id: {}) bookmarks.", nameOfProduct, currentUser.getId());
        }

        // Save status change
        listingRepository.save(listing);
        logger.debug("Status ({}) change saved!", currentStatus);

        return new BookmarkStatusPayload(currentStatus);
    }

    /**
     * GET endpoint to retrieve the listing bookmark messages for a given user.
     * A message is created when a listing is bookmarked or when a bookmark is removed.
     *
     * @param sessionToken The current user's session token
     * @return A list of BookmarkedListingMessages.
     */
    @GetMapping("/home/bookmarkMessages")
    public List<BookmarkedListingMessagePayload> getBookmarkMessages(@CookieValue(value = "JSESSIONID", required = false) String sessionToken) {
        // 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        logger.debug("User retrieved, ID: {}.", currentUser.getId());

        Integer currentUserId = currentUser.getId();

        List<BookmarkedListingMessage> bookmarkedListingMessages = currentUser.getBookmarkedListingMessages();

        logger.info("BookmarkedListingMessages Retrieval Success - " +
                "All bookmarkedListingMessages entities retrieved from HasBookmarkedListingMessage entities for user with ID {}: {}", currentUserId, bookmarkedListingMessages);

        return convertBookmarkedListingMessageListToPayload(bookmarkedListingMessages);
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

        try {
            SoldListing soldListing = new SoldListing(business, currentUser, listing.getCreated(),
                    listing.getInventoryItem().getProduct().getProductId(),
                    listing.getQuantity(), listing.getPrice(),
                    listing.getTotalBookmarks());
            soldListing = soldListingRepository.save(soldListing);
            logger.info("Sold Listing Creation Success - Sold listing created for business with ID {}", listing.getBusinessId());

            try {
                String soldListingMessage = String.format("A listing of yours, %s x%d, has been sold.", listing.getInventoryItem().getProduct().getName(), listing.getQuantity());
                SoldListingNotification soldListingNotification = new SoldListingNotification(business.getId(), soldListing, soldListingMessage);
                soldListingNotificationRepository.save(soldListingNotification);

                logger.info("Sold Listing Notification Creation Success - Sold listing notification created for business with ID {}", business.getId());
            } catch (IllegalSoldListingNotificationArgumentException e) {
                logger.error("Couldn't create sold listing notification - {}", e.getMessage());
            }
        } catch (IllegalSoldListingArgumentException e) {
            logger.error("Couldn't create sold listing - {}", e.getMessage());
        }
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

    @DeleteMapping("/home/bookmarkMessages/{id}")
    @ResponseStatus(value = HttpStatus.OK, reason = "Bookmark message successfully deleted")
    public void deleteBookmarkMessage(@CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable String id) {
        // 401
        User user = Authorization.getUserVerifySession(sessionToken, userRepository);

        // 406
        Optional<BookmarkedListingMessage> message = bookmarkedListingMessageRepository.findById(Integer.valueOf(id));
        if (message.isEmpty()) {
            logger.error("406 [NOT_ACCEPTABLE] - Bookmark message with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Bookmark message does not exist"
            );
        }

        // 403
        if (!message.get().getUsers().contains(user) && !Authorization.isGAAorDGAA(user)) {
            logger.error("403 [FORBIDDEN] - User is not authorized to delete bookmark message with ID {}", id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User cannot delete another users bookmark message"
            );
        }

        // 200
        try {
            bookmarkedListingMessageRepository.delete(message.get());
            logger.info("200 [SUCCESS] - Bookmark message at ID {} successfully deleted", id);
        } catch (Exception e) {
            logger.debug("500 [INTERNAL SERVER ERROR] - Could not delete bookmarkedListingMessage at ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not delete Bookmark Message"
            );
        }
    }


    /**
     * This method parses the search criteria and then calls the needed methods to execute the "query".
     *
     * @param searchQuery Criteria to search for listings.
     * @param paging Information used to paginate the retrieved listings.
     * @param searchType Search type.
     * @param businessTypes Criteria to search for listings using business type.
     * @param minimumPrice Minimum price.
     * @param maximumPrice Maximum price.
     * @param fromDate From date (closing).
     * @param toDate To date (closing).
     * @return Page<Listing> A page of listings matching the search criteria.
     */
    private Page<Listing> parseAndExecuteQuery(
            String searchQuery, Pageable paging,
            String searchType,
            List<String> businessTypes,
            Double minimumPrice, Double maximumPrice,
            LocalDateTime fromDate, LocalDateTime toDate
    ) {
        List<BusinessType> convertedBusinessTypes = new ArrayList<>();
        if (businessTypes != null) {
            for (String businessType : businessTypes) {
                convertedBusinessTypes.add(toBusinessType(businessType));
            }
        }

        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        switch (searchType) {
            case "listingName":
                return listingRepository.findAllListingsByProductName(
                        names, paging, convertedBusinessTypes.isEmpty() ? null : convertedBusinessTypes, minimumPrice, maximumPrice, fromDate, toDate
                );
            case "businessName":
                return listingRepository.findAllListingsByBusinessName(
                        names, paging, convertedBusinessTypes.isEmpty() ? null : convertedBusinessTypes, minimumPrice, maximumPrice, fromDate, toDate
                );
            case "location":
                return listingRepository.findAllListingsByLocation(
                        names, paging, convertedBusinessTypes.isEmpty() ? null : convertedBusinessTypes, minimumPrice, maximumPrice, fromDate, toDate
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
     * Takes a list of BookmarkedListingMessages and returns a list of BookmarkedListingMessagePayloads
     * @param bookmarkedListingMessages The given list of BookmarkedListingMessages.
     * @return A list of BookmarkedListingMessagePayloads.
     */
    public List<BookmarkedListingMessagePayload> convertBookmarkedListingMessageListToPayload(List<BookmarkedListingMessage> bookmarkedListingMessages) {
        List<BookmarkedListingMessagePayload> bookmarkedListingMessagePayloads = new ArrayList<>();

        for (BookmarkedListingMessage bookmarkedListingMessage : bookmarkedListingMessages) {
            bookmarkedListingMessagePayloads.add(bookmarkedListingMessage.toBookmarkedListingMessagePayload());
        }

        return bookmarkedListingMessagePayloads;
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

    /**
     * Converts a list of Sold Listings to a list of SoldListingPayloads.
     * @param soldListings The given list of sold listings
     * @return A list of Sold Listings in payload form.
     */
    public List<SoldListingPayload> convertToSoldPayload(List<SoldListing> soldListings) throws Exception {
        List<SoldListingPayload> payloads = new ArrayList<>();
        for (SoldListing soldListing : soldListings) {
            SoldListingPayload newPayload = soldListing.toSoldListingPayload();
            logger.debug("Sold Listing payload created: {}", newPayload);
            payloads.add(newPayload);
        }
        return payloads;
    }
}
