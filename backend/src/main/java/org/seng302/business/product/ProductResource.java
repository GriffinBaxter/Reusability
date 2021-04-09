package org.seng302.business.product;

import org.seng302.business.BusinessRepository;
import org.seng302.main.Authorization;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

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

        if (currentUser.getRole() == Role.USER && !currentUser.getBusinessesAdministered().contains(id)) {
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
     * @return A list of ProductPayload objects representing the products belonging to the given business
     */
    @GetMapping("/businesses/{id}/products")
    public List<ProductPayload> retrieveProducts(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id
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

        return productRepository.findProductsByBusinessId(id);
    }
}
