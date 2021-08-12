/**
 * Summary. This file contains the definition for the ProductResource.
 *
 * Description. This file contains the definition for the ProductResource.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductResource
 * @file   This file contains the definition for ProductResource.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Validation;
import org.seng302.exceptions.IllegalProductArgumentException;
import org.seng302.model.*;
import org.seng302.Authorization;
import org.seng302.model.User;
import org.seng302.model.repository.*;
import org.seng302.utils.PaginationUtils;
import org.seng302.view.incoming.ProductCreationPayload;
import org.seng302.view.outgoing.ProductPayload;
import org.seng302.view.incoming.ProductUpdatePayload;
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
 * ProductResource class. This class includes:
 * POST "/businesses{id}/products" endpoint used to create new products for a business.
 * GET "/businesses{id}/products" endpoint used to retrieve the products for a business, including pagination.
 * PUT "/businesses/{businessId}/products/{productId}" endpoint used to update the details of product for a business.
 * GET "businesses/{id}/productAll" endpoint used to retrieve the products for a business, excluding pagination.
 */
@RestController
public class ProductResource {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductUpdateService productUpdateService;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    private static final Logger logger = LogManager.getLogger(ProductResource.class.getName());

    /**
     * Constructor used to insert mocked repositories for testing.
     *
     * @param productRepository ProductRepository
     * @param businessRepository BusinessRepository
     * @param userRepository UserRepository
     */
    public ProductResource(ProductRepository productRepository,
                           BusinessRepository businessRepository,
                           UserRepository userRepository,
                           ProductUpdateService productUpdateService) {
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.productUpdateService = productUpdateService;
    }

    /**
     * Create a new product belonging to the business with the given business ID.
     *
     * @param sessionToken Session token
     * @param id Business ID
     */
    @PostMapping("/businesses/{id}/products")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Product created successfully")
    public void createProduct(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id,
            @RequestBody ProductCreationPayload productPayload
    ) {
        logger.debug("Product payload received: {}", productPayload);

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Authorization.verifyBusinessExists(id, businessRepository);

        Authorization.verifyBusinessAdmin(currentUser, id);

        try {
            if (productRepository.findProductByIdAndBusinessId(productPayload.getId(), id).isPresent()) {
                logger.error("Product Creation Failure - 400 [BAD REQUEST] - Product with ID {} already exists for business with ID {}", productPayload.getId(), id);
                throw new IllegalProductArgumentException("Invalid product ID, already in use");
            } else if (productPayload.getBarcode().length() != 0 && productRepository.findProductByBarcodeAndBusinessId(productPayload.getBarcode(), id).isPresent()) {
                logger.error("Product Creation Failure - 400 [BAD REQUEST] - Product with barcode {} already exists for business with ID {}", productPayload.getBarcode(), id);
                throw new IllegalProductArgumentException("Invalid barcode, already in use");
            } else {
                Product product = new Product(
                        productPayload.getId(),
                        businessRepository.findBusinessById(id).get(),
                        productPayload.getName(),
                        productPayload.getDescription(),
                        productPayload.getManufacturer(),
                        productPayload.getRecommendedRetailPrice(),
                        productPayload.getBarcode()
                );

                productRepository.save(product);

                logger.info("Product Creation Success - 201 [CREATED] - Product created for business {} with ID {}", id, productPayload.getId());
                logger.debug("Product created for business {} with ID {}: {}", id, productPayload.getId(), product);
            }
        } catch (IllegalProductArgumentException e) {
            logger.error("Product Creation Failure - 400 [BAD REQUEST] - Bad data");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    /**
     * Retrieve products given a business ID. This is a GET call to the given endpoint.
     *
     * @param sessionToken Session token
     * @param id Business ID
     * @param orderBy Column to order the results by
     * @param page Page number to return results from
     * @return A list of ProductPayload objects representing the products belonging to the given business
     */
    @GetMapping("/businesses/{id}/products")
    public ResponseEntity<List<ProductPayload>> retrieveProducts(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer id,
            @RequestParam(defaultValue = "productIdASC") String orderBy,
            @RequestParam(defaultValue = "0") String page
    ) throws Exception {
        logger.debug("Product retrieval request received with business ID {}, order by {}, page {}", id, orderBy, page);

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Authorization.verifyBusinessExists(id, businessRepository);

        Authorization.verifyBusinessAdmin(currentUser, id);

        int pageNo = PaginationUtils.parsePageNumber(page);

        // Front-end displays 5 users per page
        int pageSize = 5;

        Sort sortBy;

        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "productIdASC":
                sortBy = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
                break;
            case "productIdDESC":
                sortBy = Sort.by(Sort.Order.desc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
                break;
            case "nameASC":
                sortBy = Sort.by(Sort.Order.asc("name").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "nameDESC":
                sortBy = Sort.by(Sort.Order.desc("name").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "recommendedRetailPriceASC":
                sortBy = Sort.by(Sort.Order.asc("recommendedRetailPrice").ignoreCase().nullsLast()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "recommendedRetailPriceDESC":
                sortBy = Sort.by(Sort.Order.desc("recommendedRetailPrice").ignoreCase().nullsFirst()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "manufacturerASC":
                sortBy = Sort.by(Sort.Order.asc("manufacturer").ignoreCase().nullsLast()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "manufacturerDESC":
                sortBy = Sort.by(Sort.Order.desc("manufacturer").ignoreCase().nullsFirst()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "createdASC":
                sortBy = Sort.by(Sort.Order.asc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            case "createdDESC":
                sortBy = Sort.by(Sort.Order.desc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
                break;
            default:
                logger.error("400 [BAD REQUEST] - {} is not a valid order by parameter", orderBy);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OrderBy Field invalid"
                );
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        Page<Product> pagedResult = productRepository.findProductsByBusinessId(id, paging);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Product Retrieval Success - 200 [OK] -  Products retrieved for business with ID {}, order by {}, page {}", id, orderBy, pageNo);

        List<ProductPayload> productPayloads = convertToPayload(pagedResult.getContent());

        logger.debug("Products retrieved for business with ID {}: {}", id, productPayloads);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(productPayloads);
    }

    /**
     * Converts a list of products to a list of productPayloads.
     * @param productList The given list of products
     * @return A list of productPayloads.
     */
    public List<ProductPayload> convertToPayload(List<Product> productList) throws Exception {
        List<ProductPayload> payloads = new ArrayList<>();
        for (Product product : productList) {
            ProductPayload newPayload = product.convertToPayload();
            logger.debug("Product payload created: {}", newPayload);
            payloads.add(newPayload);
        }
        return payloads;
    }

    /**
     * A PUT request used to update a product given a business ID, product ID and the new updated attributes.
     * @param updatedProduct The product payload containing the new attributes to be changed.
     * @param businessId The ID of the business of which it's product will be updated.
     * @param productId The ID of the product to be updated.
     * @param sessionToken The token used to identify the user.
     */
    @PutMapping("/businesses/{businessId}/products/{productId}")
    @ResponseStatus(code = HttpStatus.OK, reason = "Product updated successfully")
    public void modifyCatalogueItem(@RequestBody(required = false) ProductUpdatePayload updatedProduct,
                                    @PathVariable Integer businessId,
                                    @PathVariable String productId,
                                    @CookieValue(value = "JSESSIONID", required = false) String sessionToken) {

        logger.debug("Product update payload received: {}", updatedProduct);

        // Get the user object associated with this session token, and ensure the session token is valid.
        User requestingUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        logger.debug("Found requesting user via session token: {}", sessionToken);


        // Check the businessId given is associated with a real business.
        Authorization.verifyBusinessExists(businessId, businessRepository);


        // Verify that the business has this product with the given productId.
        Optional<Product> product = productRepository.findProductByIdAndBusinessId(productId, businessId);

        if (product.isEmpty()) {
            logger.error("Product Modify Failure - 400 [BAD REQUEST] - Product with ID {} does not exist", productId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The product id supplied is invalid.");
        }

        logger.debug("Product with ID {} from business with ID {} was found: {}", productId, businessId, product.get());

        // Verify the user has permission to update that product.
        Authorization.verifyBusinessAdmin(requestingUser, businessId);

        // Verify there is a payload. Otherwise we are wasting processing time.
        if (updatedProduct == null) {
            logger.error("Product Modify Failure - 400 [BAD REQUEST] - Payload is empty.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload is missing and must be provided.");
        }

        logger.debug("Update payload is not empty. Contains: {}", updatedProduct);

        // Used to see if we can avoid using a transaction & more quickly update the product.
        boolean idIsUpdated = false;

        // If the payload includes a new description check if it is valid. Otherwise use the previously defined value.
        if (updatedProduct.getId() != null) {
            logger.debug("updatedProduct contains a new ID: {}", updatedProduct.getId());
            // No point in checking this if it is already the same value.
            if (!productId.equals(updatedProduct.getId())) {
                logger.debug("New product ID: {} differs then the origin product id: {}", updatedProduct.getId(), product.get().getProductId());

                // Verify that inventory items are not present with the same product ID
                Optional<InventoryItem> inventoryItemsWithProductId = inventoryItemRepository.findInventoryItemByProductId(product.get().getId());
                if (inventoryItemsWithProductId.isPresent()) {
                    logger.error("Product Modify Failure - 400 [BAD REQUEST] - Product ID {} cannot be modified if it already exists as an inventory item.", product.get().getProductId());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID cannot be modified with existing inventory item");
                }

                // Verify the new id is unique are valid
                if (!Validation.isValidProductId(updatedProduct.getId()) || productRepository.findProductByIdAndBusinessId(updatedProduct.getId(), businessId).isPresent()) {
                    logger.error("Product Modify Failure - 400 [BAD REQUEST] - New product ID {} either already exists OR is invalid.", updatedProduct.getId());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new product id already exists OR product id is invalid.");
                }
                // If this line is reached it means that the id is different and is being updated. Therefore we have to use the transaction route.
                idIsUpdated = true;
            }
        } else {
            logger.debug("updatedProduct does not contain a new ID: {}", updatedProduct.getId());
            updatedProduct.setId(product.get().getProductId());
        }

        // Verify the name is included!
        if (updatedProduct.getName() == null) {
            logger.error("Product Modify Failure - 400 [BAD REQUEST] - New product does not contain a name as required.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new product must have a name.");
        }

        // If the payload doesn't include a new description use the previously defined value.
        if (updatedProduct.getDescription() == null) {
            logger.debug("Product update does not contain new description.");
            updatedProduct.setDescription(product.get().getDescription());
        }

        // If the payload doesn't include a new manufacturer use the previously defined value.
        if (updatedProduct.getManufacturer() == null) {
            logger.debug("Product update does not contain new manufacturer.");
            updatedProduct.setManufacturer(product.get().getManufacturer());
        }

        // If the payload doesn't include a new recommendedRetailPrice use the previously defined value.
        if (updatedProduct.getRecommendedRetailPrice() == null) {
            logger.debug("Product update does not contain a new recommended retail price.");
            updatedProduct.setRecommendedRetailPrice(product.get().getRecommendedRetailPrice());
        }

        // If the payload doesn't include a barcode use the previously defined value.
        if (updatedProduct.getBarcode() != null) {
            logger.debug("updatedProduct contains a new barcode: {}", updatedProduct.getBarcode());
            // No point in checking this if it is already the same value.
            if (product.get().getBarcode() == null || !product.get().getBarcode().equals(updatedProduct.getBarcode())) {
                logger.debug("New barcode: {} differs then the origin barcode: {}", updatedProduct.getBarcode(), product.get().getBarcode());
                // Verify that product doesn't exist with same barcode
                if (updatedProduct.getBarcode().length() != 0 && productRepository.findProductByBarcodeAndBusinessId(updatedProduct.getBarcode(), businessId).isPresent()) {
                    logger.error("Product Modify Failure - 400 [BAD REQUEST] - Barcode {} cannot be modified if it already exists.", product.get().getBarcode());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new barcode already exists within this business.");
                }
            }
        } else {
            logger.debug("Product update does not contain a new barcode.");
            updatedProduct.setBarcode(product.get().getBarcode());
        }

        // Check if we can prevent the transaction, as it is more risky and could take longer.
        if (idIsUpdated) {
            // Start a transaction to update the product
            productUpdateService.updateProduct(productId, businessId, productRepository, updatedProduct);
            logger.debug("Transactional product update approach was performed.");
        } else {
            // Update the attributes
            try {
                product.get().setName(updatedProduct.getName());
                product.get().setDescription(updatedProduct.getDescription());
                product.get().setManufacturer(updatedProduct.getManufacturer());
                product.get().setRecommendedRetailPrice(updatedProduct.getRecommendedRetailPrice());
                product.get().setBarcode(updatedProduct.getBarcode());
                productRepository.saveAndFlush(product.get());
                logger.debug("Non transactional product update approach was performed.");
            } catch (IllegalProductArgumentException e) {
                logger.error("Product Modify Failure - 400 [BAD REQUEST] - {}", e.getMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        logger.info("Product Modify Success - 200 [OK] - Product with ID {} for business with ID {} has been updated.", productId, businessId);
        logger.debug("Product update for business with ID {} with product ID {}: {}", businessId, productId, product);
    }

    /**
     * Get method for retrieving all products at once (no pagination). To be used with CreateInventoryItem modal.
     * @param sessionToken The current user's session token
     * @param id The current business ID (from the URL path)
     * @return A list of all products for the given business.
     */
    @GetMapping("/businesses/{id}/productAll")
    public ResponseEntity<List<ProductPayload>> retrieveAllProducts(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer id) throws Exception {
        logger.debug("Product catalogue retrieval request (all items) received with business ID {}", id);

        // Checks user logged in - 401
        Authorization.getUserVerifySession(sessionToken, userRepository);

        // Checks business at ID exists - 406
        Authorization.verifyBusinessExists(id, businessRepository);

        List<Product> products = productRepository.findAllByBusinessId(id);

        logger.info("Product Retrieval Success - 200 [OK] -  " +
                "All products retrieved for business with ID {}: {}", id, products);

        List<ProductPayload> productPayloads = convertToPayload(products);

        logger.debug("All product payloads created for business with ID {}: {}",
                id,
                products);

        return ResponseEntity.ok()
                .body(productPayloads);
    }


}
