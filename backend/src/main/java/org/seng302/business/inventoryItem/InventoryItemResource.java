package org.seng302.business.inventoryItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.product.Product;
import org.seng302.business.product.ProductPayload;
import org.seng302.business.product.ProductRepository;
import org.seng302.business.product.ProductResource;
import org.seng302.main.Authorization;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

/**
 * InventoryItemResource class
 */
@RestController
public class InventoryItemResource {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(InventoryItemResource.class.getName());

    /**
     * Constructor used to insert mocked repositories for testing.
     *
     * @param inventoryItemRepository InventoryItemRepository
     * @param productRepository       ProductRepository
     * @param businessRepository      BusinessRepository
     * @param userRepository          UserRepository
     */
    public InventoryItemResource(InventoryItemRepository inventoryItemRepository,
                                 ProductRepository productRepository,
                                 BusinessRepository businessRepository,
                                 UserRepository userRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/businesses/{id}/inventory/")
    public void retrieveAllInventory(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                     @PathVariable String id){
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        //403
        //TODO:use function(isAnAdministratorOfThisBusiness) in branch S302T400-153-two-business-admin-manage-API to check permission

        //406
        Business currentBusiness = businessRepository.findBusinessById(Integer.valueOf(id)).get();
        if (currentBusiness == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Business is not exist"
            );
        }

    }

    /**
     * Create a new Inventory Item belonging to the business with the given business ID.
     *
     * @param sessionToken                 session token
     * @param inventoryRegistrationPayload inventory registration payload
     * @param id                           business id
     */
    @PostMapping("/businesses/{id}/inventory/")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Inventory item created successfully")
    public void addAnInventory(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                               @RequestBody InventoryRegistrationPayload inventoryRegistrationPayload,
                               @PathVariable Integer id) {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        //406
        Optional<Business> optionalCurrentBusiness = businessRepository.findBusinessById(id);
        if (optionalCurrentBusiness.isEmpty()) {
            logger.error("Inventory Item Creation Failure - 406 [NOT ACCEPTABLE] - Business with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "select business not exist"
            );
        }
        Business currentBusiness = optionalCurrentBusiness.get();

        //403
        if (currentUser.getRole() == Role.USER &&
                !currentBusiness.isAnAdministratorOfThisBusiness(currentUser)) {
            logger.error("Inventory Item Creation Failure - 403 [FORBIDDEN] - User with ID {} is not an " +
                    "admin of business with ID {} or a GAA or DGAA", currentUser.getId(), id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "The account performing the request is neither an administrator of the business, nor a global " +
                            "application admin."
            );
        }

        //400
        String productId = inventoryRegistrationPayload.getProductId();
        Optional<Product> optionalSelectProduct = productRepository.findProductByIdAndBusinessId(productId, id);
        if (optionalSelectProduct.isEmpty()) {
            logger.error("Inventory Item Creation Failure - 406 [NOT ACCEPTABLE] - Inventory Item with ID {} does not exist", productId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "select product not exist"
            );
        }
        Product selectProduct = optionalSelectProduct.get();

        try {
            inventoryItemRepository.save(new InventoryItem(selectProduct,
                    productId,
                    inventoryRegistrationPayload.getQuantity(),
                    inventoryRegistrationPayload.getPricePerItem(),
                    inventoryRegistrationPayload.getTotalPrice(),
                    inventoryRegistrationPayload.getManufactured(),
                    inventoryRegistrationPayload.getSellBy(),
                    inventoryRegistrationPayload.getBestBefore(),
                    inventoryRegistrationPayload.getExpires()
            ));
        } catch (Exception e) {
            logger.error("Inventory Item Creation Failure - {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }


}

