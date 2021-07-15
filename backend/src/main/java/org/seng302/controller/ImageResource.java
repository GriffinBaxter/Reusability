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
import org.seng302.view.outgoing.ImageCreatePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.*;

/**
 * Controller class for images. This class includes:
 * POST "/businesses/{businessId}/products/{productId}/images" endpoint used for adding image to a product of a businesses.
 * DELTETE "/businesses/{businessId}/products/{productId}/images/{imageId}" endpoint for deleting an image for a product of a business.
 */
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

    public ImageResource(BusinessRepository businessRepository, UserRepository userRepository,
                         ProductRepository productRepository, ImageRepository imageRepository) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/businesses/{businessId}/products/{productId}/images")
    public ResponseEntity<ImageCreatePayload> createImage(
            @RequestParam("images") MultipartFile image,
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer businessId,
            @PathVariable String productId
    ) {

        // Verify token access
        User user = Authorization.getUserVerifySession(sessionToken, userRepository);

        // Verify business parameter
        Optional<Business> business = getVerifiedBusiness(businessId);

        // Verify access rights of the user to the business
        verifyAdministrationRights(user, business);

        // Verify Product id
        verifyProductId(productId, business, user);

        // Verify the file type
        String imageFileName = image.getOriginalFilename();
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
            fileName = uuid + "." + getFileExtension(image.getOriginalFilename());
            if (!fileStorageService.storeFile(image, fileName)) {
                throw new IOException("Failed to store images");
            }
        }
        catch (Exception e) {
            String errorMessage = String.format("Failed to store image with filename %s, for user (id: %d), for business (id: %d), for product (id: %s)", imageFileName, user.getId(), businessId, productId);
            logger.error(errorMessage);
            fileStorageService.deleteFile(fileName);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "One or more of the images failed to be stored.");
        }

        String path = fileStorageService.getPathString(fileName);
        if (path != null) {
            List<Image> primaryImages = imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true);
            boolean isFirstImage = false;
            if (primaryImages.isEmpty()) {
                isFirstImage = true;
            }
            Image imageObj = new Image(productId, businessId, path, path, isFirstImage);
            imageObj = imageRepository.saveAndFlush(imageObj);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ImageCreatePayload(imageObj.getId()));
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "One or more of the images failed to be stored.");

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
        Optional<Business> business = getVerifiedBusiness(businessId);

        // Verify access rights of the user to the business
        verifyAdministrationRights(user, business);

        // Verify Product id
        verifyProductId(productId, business, user);

        // Verify image id
        Image image = verifyImageId(imageId, businessId, productId, user);

        // verify file exists & delete image
        if (!fileStorageService.deleteFile(image.getFilename()) ) {
            String errorMessage = String.format("User (id: %d) attempted to delete a non-existent image with image id %d for business with id %d and product id %s.", user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // Delete from database
        imageRepository.deleteByIdAndBussinesIdAndProductId(imageId, businessId, productId);
        imageRepository.flush();

        // Check if primary image and update primary image if it is
        updatePrimaryImage(businessId, productId, true);
    }

    /**
     * Gets the file extension of the given file name. This is based on the content after the last punctuation mark.
     * @param fileName, the file name
     * @return extension of file name if present, otherwise the empty string
     */
    private String getFileExtension(String fileName) {
        try {
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            }
        } catch ( IndexOutOfBoundsException e) {
            return "";
        }
        return "";
    }

    /**
     * Verifies that the given business id exists and returns the associated business if it exists.
     * Throws a NOT_ACCEPTABLE error is the business id does not exist.
     *
     * PRECONDITIONS:
     * POST CONDITIONS:
     * @param businessId
     * @return business
     */
    private Optional<Business> getVerifiedBusiness(Integer businessId) throws ResponseStatusException {
        Optional<Business> business = businessRepository.findBusinessById(businessId);
        if (business.isEmpty()) {
            logger.error("The requested route does exist, but some part of the request is not acceptable.");
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }
        return business;
    }

    /**
     * Verifies that the given user has the administration rights to access and potentially modify the associated business content.
     * Throws FORBIDDEN error message is the user has incorrect administration rights.
     *
     * PRECONDITIONS:
     * POST CONDITIONS:
     * @param user, the user who wants to access the information of the given business
     * @param business, the business that is desired to be accessed
     */
    private void verifyAdministrationRights(User user, Optional<Business> business) throws ResponseStatusException {
        if (user.getRole().equals(Role.USER) && !user.getBusinessesAdministered().contains(business.get().getId())) {
            String errorMessage = String.format("User (id: %d) lacks permissions to modify business (id: %d) product images.", user.getId(), business.get().getId());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Returned when a user tries to add an image for a product that it is in a catalogue for a " +
                    "business they do not administer AND the user is not a global application admin");
        }
    }

    /**
     * Verifies that the given product id exists.
     * Throws NOT_ACCEPTABLE error if product id does not exists.
     *
     * PRECONDITIONS:
     * POST CONDITIONS:
     * @param productId
     * @param business
     * @param user
     */
    private void verifyProductId(String productId, Optional<Business> business, User user) throws ResponseStatusException {
        Optional<Product> product = productRepository.findProductByIdAndBusinessId(productId, business.get().getId());
        if (product.isEmpty()) {
            String errorMessage = String.format("User (id: %d) attempted to access a non-existent product with product id %s.", user.getId(), productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }
    }

    /**
     * Verifies that the given image id exists.
     * Throws NOT_ACCEPTABLE error if image id does not exists.
     *
     * PRECONDITIONS:
     * POST CONDITIONS:
     * @param imageId
     * @param businessId
     * @param productId
     */
    private Image verifyImageId(Integer imageId, Integer businessId, String productId, User user) throws ResponseStatusException {
        Optional<Image> image = imageRepository.findImageByIdAndAndBussinesIdAndProductId(imageId, businessId, productId);
        if (image.isEmpty()) {
            String errorMessage = String.format("User (id: %d) attempted to delete a non-existent image with image id %d for business with id %d and product id %s.", user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }
        return image.get();
    }

    /**
     * Updates the primary image and enforces the primary image constraint.
     * If there are other images for the given product id that are not the primary image, then next one of these becomes
     * the primary image. If there are no other images, then there is no primary image (i.e. default used in front end).
     *
     * PRECONDITIONS:
     * POST CONDITIONS:
     * @param businessId
     * @param productId
     * @param isPrimary
     */
    private void updatePrimaryImage(Integer businessId, String productId, boolean isPrimary) {
        List<Image> primaryImages = imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true);
        if (primaryImages.isEmpty()) {
            List<Image> images = imageRepository.findImageByBussinesIdAndProductId(businessId, productId);
            if (!images.isEmpty()) {
                images.get(0).setIsPrimary(true);
                imageRepository.save(images.get(0));
            }
        }
    }

}
