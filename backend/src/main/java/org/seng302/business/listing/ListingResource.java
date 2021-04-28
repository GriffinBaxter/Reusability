package org.seng302.business.listing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.inventoryItem.InventoryItemRepository;
import org.seng302.business.inventoryItem.InventoryItem;
import org.seng302.business.product.ProductRepository;
import org.seng302.business.product.ProductResource;

import org.seng302.main.Authorization;

import org.seng302.user.UserRepository;
import org.seng302.user.User;
import org.seng302.user.Role;

import org.seng302.validation.Validation;

import org.springframework.beans.factory.annotation.Autowired;
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
     * Create a new Listing belonging to the business with the given business ID.
     *
     * @param sessionToken Session Token
     * @param id Business ID
     */
    @PostMapping("/businesses/{id}/listings")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Listing Created successfully")
    public void createListing(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id,
            @RequestBody ListingCreationPayload listingPayload) {
        logger.debug("Listing payload received: {}", listingPayload);

        // Checks if User is logged in
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Checks Business Exists
        if (!Authorization.verifyBusinessExists(id, businessRepository)) {
            logger.error("Listing Creation Failure - 406 [NOT ACCEPTABLE] - Business with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }
        // Checks User is Admin
        Optional<Business> business = BusinessRepository.findBusinessById(id);
        if (currentUser.getRole() == Role.USER && !(currentBusiness.get().getAdministrators().contains(currentUser))) {
            logger.error("Listing Creation Failure - 403 [NOT AUTHORIZED] - User with ID {} is not admin of business with ID {}", currentUser.getId(), id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Forbidden: User is not an administrator of business" +
                        " AND the user is not a global application admin"
            );
        }

        // Checks InventoryItem exists and gets InventoryItem
        Optional<InventoryItem> inventoryItem = InventoryItemRepository.findInventoryItemById(Integer.parseInt(listingPayload.inventoryItemId));
        if (!inventoryItem) {
            logger.error("Listing Creation Failure - 400 [BAD REQUEST] - Inventory Item at ID {} Not Found", listingPayload.inventoryItemId);
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
                inventoryItem,
                quantity,
                price,
                moreInfo,
                created,
                closes
            );
            listingRepository.save(listing);

            logger.info("Listing Creation Success - 201 [CREATED] - Listing created for business with ID {}", id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Bad Request"
            );
        }
    }

}

