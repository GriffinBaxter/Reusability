/**
 * Summary. This file contains the definition for the InventoryItemResource.
 *
 * Description. This file contains the defintion for the InvetoryItemResource.
 *
 * @link   team-400/src/main/java/org/seng302/business/inventoryItem/InventoryItemResource
 * @file   This file contains the definition for InventoryItemResource.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.exceptions.IllegalInventoryItemArgumentException;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.InventoryItem;
import org.seng302.utils.PaginationUtils;
import org.seng302.view.outgoing.InventoryItemPayload;
import org.seng302.model.repository.InventoryItemRepository;
import org.seng302.view.incoming.InventoryRegistrationPayload;
import org.seng302.model.Listing;
import org.seng302.model.Product;
import org.seng302.model.repository.ProductRepository;
import org.seng302.Authorization;
import org.seng302.model.User;
import org.seng302.model.repository.UserRepository;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * InventoryItemResource class. This class includes:
 * GET "/businesses/{id}/inventory" endpoint used to retrieve the inventory of a business, including pagination.
 * GET "/businesses/{id}/inventoryAll" endpoint used to retrieve all the inventory of a business, excluding pagination.
 * POST "/businesses/{id}/inventory" endpoint used to create a new inventory item for a business.
 * PUT "/businesses/{id}/inventory/{inventoryItemId}" endpoint used to update an existing inventory item.
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
     * @param id           Business ID
     * @param orderBy      Column to order the results by
     * @param page         Page number to return results from
     * @param pageSize     Number of elements to return per page
     * @return A list of InventoryPayload objects representing the inventory items belonging to the given business.
     */
    @GetMapping("/businesses/{id}/inventory")
    public ResponseEntity<List<InventoryItemPayload>> retrieveInventoryPage(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer id,
            @RequestParam(defaultValue = "productIdASC") String orderBy,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "5") String pageSize,
            @RequestParam(required = false) String barcode
    ) throws Exception {

        logger.debug("Product inventory retrieval request received with business ID {}, order by {}, page {}, page size {}",
                id, orderBy, page, pageSize);

        //401: Access token is missing or invalid
        // user is retrieved if access token is provided and valid
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Authorization.verifyBusinessExists(id, businessRepository);

        Authorization.verifyBusinessAdmin(currentUser, id);

        //200: Inventory retrieved successfully. This could be an empty array.

        int pageNo = PaginationUtils.parsePageNumber(page);
        int pageSizeNo = PaginationUtils.parsePageSizeNumber(pageSize);

        Sort sortBy;

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

        Pageable paging = PageRequest.of(pageNo, pageSizeNo, sortBy);

        Page<InventoryItem> pagedResult;

        if (barcode != null && !barcode.equals("")) {
            pagedResult = inventoryItemRepository.findInventoryItemsByBarcodeAndBusinessId(barcode, id, paging);
        } else {
            pagedResult = inventoryItemRepository.findInventoryItemsByBusinessId(id, paging);
        }

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Product Inventory Retrieval Success - 200 [OK] - " +
                "Product inventory retrieved for business with ID {}", id);

        List<InventoryItemPayload> inventoryItemPayloads = convertToPayloadList(pagedResult.getContent());

        logger.info("The size of the product inventory payload is {}", inventoryItemPayloads.size());

        logger.debug("Products retrieved for business with ID {}: {}", id, inventoryItemPayloads);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(inventoryItemPayloads);
    }

    /**
     * Get method for retrieving all inventory items at once (no pagination). To be used with CreateListing modal.
     * @param sessionToken The current user's session token
     * @param id The current business ID (from the URL path)
     * @return A list of all inventory items for the given business
     */
    @GetMapping("/businesses/{id}/inventoryAll")
    public ResponseEntity<List<InventoryItemPayload>> retrieveAllInventoryItems(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer id) throws Exception {
        logger.debug("Product inventory retrieval request (all items) received with business ID {}", id);

        // Checks user logged in - 401
        Authorization.getUserVerifySession(sessionToken, userRepository);

        // Checks business at ID exists - 406
        Authorization.verifyBusinessExists(id, businessRepository);

        List<InventoryItem> inventoryItems = inventoryItemRepository.findAllByBusinessId(id);

        logger.info("Inventory Retrieval Success - 200 [OK] -  " +
                "All inventory items retrieved for business with ID {}", id);

        List<InventoryItemPayload> inventoryItemPayloads = convertToPayloadList(inventoryItems);

        logger.debug("All inventory items retrieved for business with ID {}: {}",
                id,
                inventoryItemPayloads);

        return ResponseEntity.ok()
                .body(inventoryItemPayloads);
    }

    /**
     * Converts a list of product inventory items to a list of inventoryPayloads.
     *
     * @param inventoryList The given list of product inventory items
     * @return A list of inventoryPayloads.
     */
    public List<InventoryItemPayload> convertToPayloadList(List<InventoryItem> inventoryList) throws Exception {
        List<InventoryItemPayload> payloads = new ArrayList<>();
        for (InventoryItem inventoryItem : inventoryList) {
            InventoryItemPayload newPayload = inventoryItem.convertToPayload();
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
    @PostMapping("/businesses/{id}/inventory")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Inventory item created successfully")
    public void addAnInventoryItem(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                   @RequestBody InventoryRegistrationPayload inventoryRegistrationPayload,
                                   @PathVariable Integer id) {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        //406
        Authorization.verifyBusinessExists(id, businessRepository);

        //403
        Authorization.verifyBusinessAdmin(currentUser, id);

        //400
        String productId = inventoryRegistrationPayload.getProductId();
        Optional<Product> optionalSelectProduct = productRepository.findProductByIdAndBusinessId(productId, id);
        if (optionalSelectProduct.isEmpty()) {
            logger.error("Inventory Item Creation Failure - 400 [BAD REQUEST] - " +
                    "Inventory Item with ID {} does not exist", productId);
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
            logger.info("Inventory Item Create Success - 200 [OK] - " +
                    "An inventory item has been create for business with ID {}", id);
        } catch (IllegalInventoryItemArgumentException e) {
            logger.error("Inventory Item Creation Failure - {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    /**
     * A PUT request used to update a inventory item by given a business ID, inventory Item ID and the new updated
     * attributes.
     *
     * @param inventoryRegistrationPayload The inventory item payload containing the new attributes to be changed.
     * @param businessId The ID of the business of which it's product will be updated.
     * @param inventoryItemId The ID of the inventory item to be updated.
     * @param sessionToken The token used to identify the user.
     */
    @PutMapping("/businesses/{businessId}/inventory/{inventoryItemId}")
    @ResponseStatus(code = HttpStatus.OK, reason = "Inventory item updated successfully")
    public void modifyInventoryItem(
            @RequestBody(required = false) InventoryRegistrationPayload inventoryRegistrationPayload,
            @PathVariable Integer businessId,
            @PathVariable Integer inventoryItemId,
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken) {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        //406
        Authorization.verifyBusinessExists(businessId, businessRepository);

        //403
        Authorization.verifyBusinessAdmin(currentUser, businessId);

        //400
        Optional<InventoryItem> optionalSelectInventoryItem = inventoryItemRepository
                .findInventoryItemById(inventoryItemId);
        if (optionalSelectInventoryItem.isEmpty()) {
            logger.error("Inventory Item Updating Failure - 400 [BAD REQUEST] - " +
                    "Inventory Item with ID {} not exist", inventoryItemId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "selected inventory item does not exist"
            );
        }
        InventoryItem selectInventoryItem = optionalSelectInventoryItem.get();
        logger.debug("One inventory item has been find.");

        String errorMessage = null;
        String productId = inventoryRegistrationPayload.getProductId();
        Integer quantity = inventoryRegistrationPayload.getQuantity();
        Double pricePerItem = inventoryRegistrationPayload.getPricePerItem();
        Double totalPrice = inventoryRegistrationPayload.getTotalPrice();
        LocalDate manufactured = inventoryRegistrationPayload.getManufactured();
        LocalDate expires = inventoryRegistrationPayload.getExpires();

        Optional<Product> optionalSelectProduct = productRepository.findProductByIdAndBusinessId(productId, businessId);
        if (optionalSelectProduct.isEmpty()) {
            logger.error("Inventory Item Creation Failure - 400 [BAD REQUEST] - " +
                    "Product with ID {} not exist", productId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid product ID"
            );
        }
        Product selectProduct = optionalSelectProduct.get();

        // Calculates the total quantity of the inventory item being changed from all listings (if any).
        int totalQuantityFromListings = 0;
        for (Listing listing: selectInventoryItem.getListings()) {
            totalQuantityFromListings += listing.getQuantity();
        }
        logger.debug("Total quantity from listings: {}.", totalQuantityFromListings);

        if (!productId.equals(selectProduct.getProductId())) {
            errorMessage = "Invalid product ID";
        }
        // The new quantity cannot be lower than the total amount currently in listings for the given inventory item.
        else if (quantity == null || quantity <= 0) {
            errorMessage = "Invalid quantity, must have at least one item";
        } else if (quantity < totalQuantityFromListings) {
            errorMessage = "Invalid quantity, must be more than the total quantity in your current listings";
        } else if (pricePerItem != null && pricePerItem < 0) {
            errorMessage = "Invalid price per item, must not be negative";
        } else if (totalPrice != null && totalPrice < 0) {
            errorMessage = "Invalid total price, must not be negative";
        } else if (manufactured != null && manufactured.isAfter(LocalDate.now())) {
            errorMessage = "Invalid manufacture date";
        } else if (expires == null || expires.isBefore(LocalDate.now())) {
            errorMessage = "Invalid expiration date, must have expiration date and cannot add expired item";
        }

        //400
        if (errorMessage != null){
            logger.error("Inventory Item Updating Failure - 400 [BAD REQUEST] - {}", errorMessage);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    errorMessage
            );
        }

        selectInventoryItem.setProductId(productId);
        logger.debug("Inventory item's productId has been change to {}.", productId);
        selectInventoryItem.setProduct(selectProduct);
        selectInventoryItem.setQuantity(quantity);
        logger.debug("Inventory item's quantity has been change to {}.", quantity);
        selectInventoryItem.setPricePerItem(pricePerItem);
        logger.debug("Inventory item's pricePerItem has been change to {}.", pricePerItem);
        selectInventoryItem.setTotalPrice(totalPrice);
        logger.debug("Inventory item's totalPrice has been change to {}.", totalPrice);
        selectInventoryItem.setManufactured(manufactured);
        logger.debug("Inventory item's manufactured has been change to {}.", manufactured);
        selectInventoryItem.setSellBy(inventoryRegistrationPayload.getSellBy());
        logger.debug("Inventory item's sellBy has been change to {}.",
                inventoryRegistrationPayload.getSellBy());
        selectInventoryItem.setBestBefore(inventoryRegistrationPayload.getBestBefore());
        logger.debug("Inventory item's bestBefore has been change to {}.",
                inventoryRegistrationPayload.getBestBefore());
        selectInventoryItem.setExpires(expires);
        logger.debug("Inventory item's expires has been change to {}.", expires);


        inventoryItemRepository.saveAndFlush(selectInventoryItem);
        logger.info("Inventory Item Updating Success - 200 [OK] - " +
                "Inventory item (Id: {}) has been update for business with ID {}", inventoryItemId, businessId);

    }

}
