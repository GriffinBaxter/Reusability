package org.seng302.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.model.Business;
import org.seng302.model.Product;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.repository.ProductRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.services.FileStorageService;
import org.seng302.services.ImageScalingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ImageResource {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    private FileStorageService fileStorageService = new FileStorageService("product-images");

    private static final Logger logger = LogManager.getLogger(ImageResource.class.getName());

    @PostMapping("/businesses/{businessId}/products/{productId}/images")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Image created successfully")
    public void createImage(
            @RequestParam("images") MultipartFile[] images,
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer businessId,
            @PathVariable String productId
    ) {

        // Verify token access
        User user = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Verify business parameter
        Optional<Business> business = businessRepository.findBusinessById(businessId);
        if (business.isEmpty()) {
            logger.error("The requested route does exist, but some part of the request is not acceptable.");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // Verify access rights of the user to the business
        if (user.getRole().equals(Role.USER) && !user.getBusinessesAdministered().contains(business.get().getId())) {
            var errorMessage = String.format("User (id: %d) lacks permissions to modify business (id: %d) product images.", user.getId(), business.get().getId());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Returned when a user tries to add an image for a product that it is in a catalogue for a " +
                    "business they do not administer AND the user is not a global application admin");
        }

        // Verify Product id
        Optional<Product> product = productRepository.findProductByIdAndBusinessId(productId, business.get().getId());
        if (product.isEmpty()) {
            var errorMessage = String.format("User (id: %d) attempted to access a non-existant product with product id %s.", user.getId(), productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // Verify the file type TODO FIGURE OUT HOW TO VERIFY THE CONTENT TYPE NOT JUST THE EXTENTION...
        for (MultipartFile image : images) {
            var debugMessage = String.format("Creating another image with unknown if it is actually an IMAGE (name: %s) !!!", image.getName());
            logger.debug(debugMessage);
        }

        // Store the images
        ArrayList<String> processedImagesNames = new ArrayList<String>();
        try {
            for (int i = 0; i < images.length; i++) {
                String fileName = String.format(images[i].getOriginalFilename());
                if (!fileStorageService.storeFile(images[i], fileName)) {
                    throw new IOException("Failed to store images");
                }
                processedImagesNames.add(fileName);
            }
        } catch (IOException e) {
            for (String fileName : processedImagesNames) {
//                fileStorageService.deleteFile(fileName);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more of the images failed to be stored.");
            }
        }
    }

}
