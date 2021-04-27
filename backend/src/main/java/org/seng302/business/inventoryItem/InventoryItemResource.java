package org.seng302.business.inventoryItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.product.Product;
import org.seng302.business.product.ProductPayload;
import org.seng302.business.product.ProductRepository;
import org.seng302.main.Authorization;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
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

    /**
     * Retrieve a business's product inventory with the given business ID, 5 pages a time.
     * The page is ordered based on orderBy and the specified page given by page is returned.
     * This is a GET call to the given endpoint.
     *
     * @param sessionToken Session token
     * @param id Business ID
     * @param orderBy Column to order the results by
     * @param page Page number to return results from
     * @return A list of InventoryPayload objects representing the inventory items belonging to the given business.
     */
    @GetMapping("/businesses/{id}/inventory/")
    public ResponseEntity<List<InventoryItemPayload>> retrieveInventoryPage(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                                            @PathVariable Integer id,
                                                                            @RequestParam(defaultValue = "productIdASC") String orderBy,
                                                                            @RequestParam(defaultValue = "0") String page
    ){

        logger.debug("Product inventory retrieval request received with business ID {}, order by {}, page {}", id, orderBy, page);

        //401: Access token is missing or invalid
        // user is retrieved if access token is provided and valid
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        if (!Authorization.verifyBusinessExists(id, businessRepository)) {
            logger.error("Product Inventory Retrieval Failure - 406 [NOT ACCEPTABLE] - Business with ID {} does not exist", id);
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        if (currentUser.getRole() == Role.USER && !currentUser.getBusinessesAdministered().contains(id)) {
            logger.error("Product Inventory Retrieval Failure - 403 [FORBIDDEN] - User with ID {} is not an admin of business with ID {}", currentUser.getId(), id);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "The account performing the request is neither an administrator of the business, nor a global application admin."
            );
        }

        //200: Inventory retrieved successfully. This could be an empty array.

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

        // Front-end displays 5 product inventory items per page
        int pageSize = 5;

        Sort sortBy = null;

        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "productIdASC":
                sortBy = Sort.by(Sort.Order.asc("productId").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
                break;
            case "productIdDESC":
                sortBy = Sort.by(Sort.Order.desc("productId").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
                break;
            case "quantityASC":
                sortBy = Sort.by(Sort.Order.asc("quantity").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "quantityDESC":
                sortBy = Sort.by(Sort.Order.desc("quantity").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "pricePerItemASC":
                sortBy = Sort.by(Sort.Order.asc("pricePerItem").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "pricePerItemDESC":
                sortBy = Sort.by(Sort.Order.desc("pricePerItem").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "totalPriceASC":
                sortBy = Sort.by(Sort.Order.asc("totalPrice").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "totalPriceDESC":
                sortBy = Sort.by(Sort.Order.desc("totalPrice").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "manufacturedASC":
                sortBy = Sort.by(Sort.Order.asc("manufactured").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "manufacturedDESC":
                sortBy = Sort.by(Sort.Order.desc("manufactured").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "sellByASC":
                sortBy = Sort.by(Sort.Order.asc("sellBy").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "sellByDESC":
                sortBy = Sort.by(Sort.Order.desc("sellBy").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "bestBeforeASC":
                sortBy = Sort.by(Sort.Order.asc("bestBefore").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "bestBeforeDESC":
                sortBy = Sort.by(Sort.Order.desc("bestBefore").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "expiresASC":
                sortBy = Sort.by(Sort.Order.asc("expires").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            case "expiresDESC":
                sortBy = Sort.by(Sort.Order.desc("expires").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("productId")));
                break;
            default:
                logger.error("400 [BAD REQUEST] - {} is not a valid order by parameter", orderBy);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OrderBy Field invalid"
                );
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        Page<InventoryItem> pagedResult = inventoryItemRepository.findInventoryItemsByBusinessId(id, paging);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Product Inventory Retrieval Success - 200 [OK] - Product inventory retrieved for business with ID {}", id);

        List<InventoryItemPayload> inventoryItemPayloads = convertToPayload(pagedResult.getContent());

        logger.info("The size of the product inventory payload is {}", inventoryItemPayloads.size());

        logger.debug("Products retrieved for business with ID {}: {}", id, inventoryItemPayloads);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(inventoryItemPayloads);
    }

    /**
     * Converts a list of product inventory items to a list of inventoryPayloads.
     * @param inventoryList The given list of product inventory items
     * @return A list of inventoryPayloads.
     */
    public List<InventoryItemPayload> convertToPayload(List<InventoryItem> inventoryList) { //TODO: Test this function
        List<InventoryItemPayload> payloads = new ArrayList<>();
        InventoryItemPayload newPayload;
        for (InventoryItem inventoryItem : inventoryList) {
            newPayload = new InventoryItemPayload(
                    inventoryItem.getId(),
                    ProductPayload.convertProductToProductPayload(inventoryItem.getProduct()),
                    inventoryItem.getQuantity(),
                    inventoryItem.getPricePerItem(),
                    inventoryItem.getTotalPrice(),
                    inventoryItem.getManufactured().toString(),
                    inventoryItem.getSellBy().toString(),
                    inventoryItem.getBestBefore().toString(),
                    inventoryItem.getExpires().toString()
            );
            logger.debug("Product inventory payload created: {}", newPayload);
            payloads.add(newPayload);
        }
        return payloads;
    }

    /**
     * Create a new Inventory Item belonging to the business with the given business ID.
     * This is a POST call to the given endpoint.
     *
     * @param sessionToken                 session token
     * @param inventoryRegistrationPayload inventory registration payload
     * @param id                           business id
     */
    @PostMapping("/businesses/{id}/inventory/")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Inventory item created successfully")
    public void addAnInventoryItem(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
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
                    "selected business does not exist"
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
                    "selected product does not exist"
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

