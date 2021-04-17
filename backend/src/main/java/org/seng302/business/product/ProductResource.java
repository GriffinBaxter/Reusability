package org.seng302.business.product;

import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ProductResource class
 */
@RestController
public class ProductResource {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Constructor used to insert mocked repositories for testing.
     *
     * @param productRepository ProductRepository
     * @param businessRepository BusinessRepository
     * @param userRepository UserRepository
     */
    public ProductResource(ProductRepository productRepository,
                           BusinessRepository businessRepository,
                           UserRepository userRepository) {
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
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
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        if (!Authorization.verifyBusinessExists(id, businessRepository)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        Optional<Business> currentBusiness = businessRepository.findBusinessById(id);

        if (currentUser.getRole() == Role.USER && !(currentBusiness.get().getAdministrators().contains(currentUser))) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Forbidden: Returned when a user tries to add a product to business they do not administer " +
                            "AND the user is not a global application admin"
            );
        }

        try {
            if (productRepository.findProductByIdAndBusinessId(productPayload.getId(), id).isPresent()) {
                throw new Exception("Invalid product id, already in use");
            } else {
                Product product = new Product(
                        productPayload.getId(),
                        businessRepository.findBusinessById(id).get(),
                        productPayload.getName(),
                        productPayload.getDescription(),
                        productPayload.getManufacturer(),
                        productPayload.getRecommendedRetailPrice(),
                        LocalDateTime.now()
                );

                productRepository.save(product);
            }
        } catch (Exception e) {
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
            @RequestParam String orderBy,
            @RequestParam String page
    ) {
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        if (!Authorization.verifyBusinessExists(id, businessRepository)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        if (currentUser.getRole() == Role.USER && !currentUser.getBusinessesAdministered().contains(id)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "The account performing the request is neither an administrator of the business, nor a global application admin."
            );
        }

        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (final NumberFormatException e) {
            // Invalid page input
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page parameter invalid"
            );
        }

        // Front-end displays 5 users per page
        int pageSize = 5;

        Sort sortBy = null;

        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        if (orderBy.equals("productIdASC")) {

            sortBy = Sort.by(Sort.Order.asc("productId").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));

        } else if (orderBy.equals("productIdDESC")) {

            sortBy = Sort.by(Sort.Order.desc("productId").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));

        } else if (orderBy.equals("nameASC")) {

            sortBy = Sort.by(Sort.Order.asc("name").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else if (orderBy.equals("nameDESC")) {

            sortBy = Sort.by(Sort.Order.desc("name").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else if (orderBy.equals("descriptionASC")) {

            sortBy = Sort.by(Sort.Order.asc("description").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else if (orderBy.equals("descriptionDESC")) {

            sortBy = Sort.by(Sort.Order.asc("description").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else if (orderBy.equals("manufacturerASC")) {

            sortBy = Sort.by(Sort.Order.desc("manufacturer").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else if (orderBy.equals("manufacturerDESC")) {

            sortBy = Sort.by(Sort.Order.desc("manufacturer").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else if (orderBy.equals("createdASC")) {

            sortBy = Sort.by(Sort.Order.desc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else if (orderBy.equals("createdDESC")) {

            sortBy = Sort.by(Sort.Order.desc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("productId").ignoreCase()));

        } else {
            // Invalid orderBy input
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

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(convertToPayload(pagedResult.getContent()));
    }

    /**
     * Converts a list of products to a list of productPayloads.
     * @param productList The given list of products
     * @return A list of productPayloads.
     */
    public List<ProductPayload> convertToPayload(List<Product> productList) {
        List<ProductPayload> payloads = new ArrayList<>();
        for (Product product : productList) {
            ProductPayload newPayload = new ProductPayload(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getManufacturer(),
                    product.getRecommendedRetailPrice(),
                    product.getCreated()
            );
            payloads.add(newPayload);
        }
        return payloads;
    }

}
