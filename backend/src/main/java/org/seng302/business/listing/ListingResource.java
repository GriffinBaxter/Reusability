package org.seng302.business.listing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.inventoryItem.InventoryItemRepository;
import org.seng302.business.inventoryItem.InventoryItem;
import org.seng302.business.inventoryItem.InventoryItemResource;
import org.seng302.business.product.Product;
import org.seng302.business.product.ProductPayload;
import org.seng302.business.product.ProductRepository;
import org.seng302.business.product.ProductResource;

import org.seng302.main.Authorization;

import org.seng302.user.UserRepository;
import org.seng302.user.User;
import org.seng302.user.Role;

import org.seng302.validation.Validation;

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

    private static final Logger logger = LogManager.getLogger(ProductResource.class.getName());

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
     * @param sessionToken
     * @param id business ID
     * @param orderBy ordering of results
     * @param page page number
     * @return Listings for business
     */
    @GetMapping("/businesses/{id}/listings")
    public ResponseEntity<List<ListingPayload>> retrieveListings(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                                 @PathVariable Integer id,
                                                                 @RequestParam(defaultValue = "idASC") String orderBy,
                                                                 @RequestParam(defaultValue = "0") String page) {

        logger.debug("Product inventory retrieval request received with business ID {}, order by {}, page {}", id, orderBy, page);

        // Checks user logged in - 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        if (!Authorization.verifyBusinessExists(id, businessRepository)) {
            logger.error("Product Catalogue Retrieval Failure - 406 [NOT ACCEPTABLE] - Business with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        // Checks Page Num valid - 400
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

        // Front-end displays 10 listings per page
        int pageSize = 10;

        Sort sortBy = null;

        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "idASC":
                sortBy = Sort.by(Sort.Order.asc("id").ignoreCase());
                break;
            case "idDESC":
                sortBy = Sort.by(Sort.Order.desc("id").ignoreCase());
                break;
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
        System.out.println("TEST");
        // Checks if User is logged in 401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Checks Business Exists 406
        if (!Authorization.verifyBusinessExists(id, businessRepository)) {
            logger.error("Listing Creation Failure - 406 [NOT ACCEPTABLE] - Business with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }
        // Checks User is Admin 403
        Optional<Business> business = businessRepository.findBusinessById(id);
        if (currentUser.getRole() == Role.USER && !(business.get().getAdministrators().contains(currentUser))) {
            logger.error("Listing Creation Failure - 403 [NOT AUTHORIZED] - User with ID {} is not admin of business with ID {}", currentUser.getId(), id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Forbidden: User is not an administrator of business" +
                        " AND the user is not a global application admin"
            );
        }
        System.out.println(listingPayload.getInventoryItemId());
        // Checks InventoryItem exists and gets InventoryItem
        Optional<InventoryItem> inventoryItem = inventoryItemRepository.findInventoryItemByProductId(listingPayload.getInventoryItemId());
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
        } catch (Exception e) {
            logger.error("Couldn't make listing {}", e.getMessage());
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Bad Request - Couldn't make listing"
            );
        }
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
                    listing.getCreated(),
                    listing.getCloses()
            );
            logger.debug("Listing payload created: {}", newPayload);
            payloads.add(newPayload);
        }
        return payloads;
    }

}

