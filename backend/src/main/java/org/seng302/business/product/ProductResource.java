package org.seng302.business.product;

import jdk.jfr.ContentType;
import org.seng302.business.BusinessRepository;
import org.seng302.business.product.images.Image;
import org.seng302.business.product.images.ImageRepository;
import org.seng302.business.product.images.ImageUploadPayload;
import org.seng302.business.product.images.StorageService;
import org.seng302.main.Authorization;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ProductResource class
 */
@RestController
public class ProductResource {

    public final boolean DEBUG_MODE = true;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StorageService storageService;

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

    // TODO AGREE on maximum image size.
    /**
     *
     * @param images
     * @param sessionToken
     * @param businessId
     * @param productId
     */
    @PostMapping("/businesses/{businessId}/products/{productId}/images")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Image successfully created")
    public void uploadImage(
            @RequestParam("images") MultipartFile[] images,
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer businessId,
            @PathVariable String productId
    ) {

        // Disables safety checks for the data before creation of the files
        if (!DEBUG_MODE) {

            // Get the current user to verify his session token
            User requestingUser = Authorization.getUserVerifySession(sessionToken, userRepository);

            // Verify the business and product exists within the database.
            if (!Authorization.verifyProductExists(productId, businessId, productRepository)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE,
                        "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                                "for example trying to access a resource by an ID that does not exist."
                );
            }

            // Verify if the user has permissions to upload images to this product
            if (requestingUser.getRole() == Role.USER && !requestingUser.getBusinessesAdministered().contains(businessId)) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "The account performing the request is neither an administrator of the business, nor a global application admin."
                );
            }

            // Verify it is the correct file type.
            Arrays.stream(images).forEach(
                    file -> {
                        // TODO Check file type
                    }
            );
        }

        // Store the file & Create the thumbnail for the primary image
        AtomicBoolean needToCreateThumbnail = new AtomicBoolean(true);
        ArrayList<String> fileNamesAdded = new ArrayList<String>();
        Arrays.stream(images).forEach(
                image -> {

                    // Create thumbnail for primary image.
                    // TODO Not specified yet. So for now will create an exact copy labeled with "*-thumbnail.*"
                    if (needToCreateThumbnail.get()) {

                        // Generate image

                        // Store the image locally

                        // Add path to database

                        needToCreateThumbnail.set(false);
                    }

                    // Store the image locally
//                    try {
//                        storageService.storeFile(image, "test");
//                        fileNamesAdded.add("test");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


                    imageRepository.saveAndFlush(new Image("some path", "filename", "exten"));

                    // Add path to database
                    System.out.println(imageRepository.getAll());
                    // TODO Perform a transation to the DB creating a file path for each image in the Image table
                    // TODO This means that if any of the images fails we can perform a rollback, and also delete the images from the file system.
                    // TODO The creation of images should be atomic.
                }
        );

    }

}
