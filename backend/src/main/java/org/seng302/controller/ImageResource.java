package org.seng302.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.model.Business;
import org.seng302.model.Image;
import org.seng302.model.Product;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.repository.ImageRepository;
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
import java.util.*;

@RestController
public class ImageResource {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    private FileStorageService fileStorageService = new FileStorageService("product-images");

    private static final Logger logger = LogManager.getLogger(ImageResource.class.getName());

    @PostMapping("/businesses/{businessId}/products/{productId}/images")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Image created successfully")
    public void createImage(
            @RequestParam("images") MultipartFile image,
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
            String errorMessage = String.format("User (id: %d) lacks permissions to modify business (id: %d) product images.", user.getId(), business.get().getId());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Returned when a user tries to add an image for a product that it is in a catalogue for a " +
                    "business they do not administer AND the user is not a global application admin");
        }

        // Verify Product id
        Optional<Product> product = productRepository.findProductByIdAndBusinessId(productId, business.get().getId());
        if (product.isEmpty()) {
            String errorMessage = String.format("User (id: %d) attempted to access a non-existant product with product id %s.", user.getId(), productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // Verify the file type
        String imageFileName = Objects.requireNonNull(image.getOriginalFilename());
        String imageType = getFileExtension(imageFileName);
        if (!imageType.equalsIgnoreCase("jpg") && !imageType.equalsIgnoreCase("jpeg") &&
                !imageType.equalsIgnoreCase("png") && !imageType.equalsIgnoreCase("gif")) {

            String debugMessage = String.format("Creating another image with unknown if it is actually an IMAGE (name: %s) !!!", image.getName());
            logger.debug(debugMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The file type of the image uploaded is not " +
                    "supported. Only JPG, JPEG, PNG and GIF are supported.");
        }

        // Store the images
        String fileName = null;
        try {
            UUID uuid = UUID.randomUUID();
            fileName = uuid.toString() + "." + getFileExtension(image.getOriginalFilename());
            if (!fileStorageService.storeFile(image, fileName)) {
                throw new IOException("Failed to store images");
            }
        }
        catch (IOException e) {
            fileStorageService.deleteFile(fileName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more of the images failed to be stored.");
        }


        String path = fileStorageService.getPathString(fileName);
        if (path != null) {
            List<Image> primaryImages = imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true);
            boolean isFirstImage = false;
            if (primaryImages.isEmpty()) {
                isFirstImage = true;
            }
            Image imageObj = new Image(productId, businessId, path, fileName, isFirstImage);
            imageRepository.saveAndFlush(imageObj);
        }

    }

    @DeleteMapping("/businesses/{businessId}/products/{productId}/images/{imageId}")
    @ResponseStatus(value = HttpStatus.OK, reason = "Image deleted successfully")
    public void deleteImage(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer businessId,
            @PathVariable String productId,
            @PathVariable Integer imageId
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
            String errorMessage = String.format("User (id: %d) lacks permissions to modify business (id: %d) product images.", user.getId(), business.get().getId());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Returned when a user tries to add an image for a product that it is in a catalogue for a " +
                    "business they do not administer AND the user is not a global application admin");
        }

        // Verify Product id
        Optional<Product> product = productRepository.findProductByIdAndBusinessId(productId, business.get().getId());
        if (product.isEmpty()) {
            String errorMessage = String.format("User (id: %d) attempted to access a non-existant product with product id %s.", user.getId(), productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // Verify image id
        Optional<Image> image = imageRepository.findImageByIdAndAndBussinesIdAndProductId(imageId, businessId, productId);
        if (image.isEmpty()) {
            String errorMessage = String.format("User (id: %d) attempted to delete a non-existant image with image id %d for business with id %d and product id %s.", user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // verify file exists & Delete image
        if (!fileStorageService.deleteFile(image.get().getPath()) ) {
            String errorMessage = String.format("User (id: %d) attempted to delete a non-existant image with image id %d for business with id %d and product id %s.", user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // Delete from database
        imageRepository.deleteByIdAndBussinesIdAndProductId(imageId, businessId, productId);
        imageRepository.flush();

        // Check if primary image
        List<Image> primaryImages = imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true);
        if (primaryImages.isEmpty()) {
            List<Image> images = imageRepository.findImageByBussinesIdAndProductId(businessId, productId);
            if (!images.isEmpty()) {
                images.get(0).setIsPrimary(true);
                imageRepository.save(images.get(0));
            }
        }
    }

    private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

}
