package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.model.*;
import org.seng302.model.enums.ImageType;
import org.seng302.model.repository.*;
import org.seng302.services.FileStorageService;
import org.seng302.view.outgoing.ImageCreatePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
import java.util.List;

import static org.seng302.Validation.verifyImageExtension;

/**
 * Controller class for images. This class includes:
 * POST   "/images" endpoint used for adding image to a user profile or a business profile or a product of a businesses.
 * DELETE "images/{imageId}" endpoint for deleting a product image of a business, user image, or business image.
 * PUT    "/images/{imageId}/makePrimary" endpoint for changing the primary image of a user or a business or a product.
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
    private ProductImageRepository productImageRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    @Autowired
    private BusinessImageRepository businessImageRepository;

    @Autowired
    @Value("product-images")
    private FileStorageService fileStorageService;

    private static final Logger logger = LogManager.getLogger(ImageResource.class.getName());

    // constants for switch cases.
    private static final String USER_IMAGE_STRING = "USER_IMAGE";
    private static final String BUSINESS_IMAGE_STRING = "BUSINESS_IMAGE";
    private static final String PRODUCT_IMAGE_STRING = "PRODUCT_IMAGE";

    // constants for error messages.
    private static final String LOGGER_INVALID_IMAGE_TYPE = "Given image type {} invalid";
    private static final String INVALID_IMAGE_TYPE = "Invalid image type";
    private static final String HTTP_NOT_ACCEPTABLE_MESSAGE = "The requested route does exist (so not a 404) but some part of the request is not acceptable, for example trying to access a resource by an ID that does not exist.";

    public ImageResource() {
    }

    public ImageResource(BusinessRepository businessRepository, UserRepository userRepository,
                         ProductRepository productRepository, ProductImageRepository productImageRepository,
                         UserImageRepository userImageRepository, FileStorageService fileStorageService) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.userImageRepository = userImageRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Take a image file to process imageFilePath and  thumbnailFilePath
     *
     * @param image      image file
     * @param imageOwner image owner info
     * @return a list contain imageFilePath and thumbnailFilePath
     */
    public List<String> processImage(MultipartFile image, String imageOwner) {
        String imageName = image.getName();

        // Verify the file type
        String imageFileName = image.getOriginalFilename();

        // Check file has the original name attached
        if (imageFileName == null) {
            logger.error("Image uploaded was missing original file name.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image was missing a file name.");
        }

        // Checking file extension
        String fileExtension = getFileExtension(imageFileName);
        if (verifyImageExtension(fileExtension, imageName)) {
            logger.error("Creating another image with unknown if it is actually an IMAGE (name: {}) !!!",
                    image.getName());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The file type of the image uploaded is not " +
                    "supported. Only JPG, JPEG, PNG and GIF are supported.");
        }

        // Creating thumbnail image
        InputStream thumbnailInputStream;
        try {
            thumbnailInputStream = fileStorageService.generateThumbnail(image, fileExtension);
        } catch (IOException e) {
            logger.error("Thumbnail unable to be created from image (name: {})", image.getName());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Thumbnail unable to be created from image.");
        }

        // Store the images in the file system
        String fileName = null;
        String thumbnailFilename = null;
        try {
            UUID uuid = UUID.randomUUID();
            fileName = uuid + "." + fileExtension;
            thumbnailFilename = "thumbnail" + fileName;

            if (!fileStorageService.storeFile(image.getInputStream(), fileName)
                    || !fileStorageService.storeFile(thumbnailInputStream, thumbnailFilename)) {
                throw new IOException("Failed to store images");
            }
        }
        // If the file already exists then we need to just something went wrong
        catch (FileAlreadyExistsException e) {
            logger.error("File already existed. Canceling storage of image with filename {}, {}",
                    imageFileName, imageOwner);
            fileStorageService.deleteFile(fileName);
            fileStorageService.deleteFile(thumbnailFilename);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "One or more of the images failed to be stored.");
        }
        // If this fails we need to ensure it was removed.
        catch (IOException e) {
            logger.error("Failed to store image with filename {}, {}", imageFileName, imageOwner);
            fileStorageService.deleteFile(fileName);
            fileStorageService.deleteFile(thumbnailFilename);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "One or more of the images failed to be stored.");
        }

        // If we cannot get the file path, then we have to assume that it failed.
        String imageFilePath = fileStorageService.getPathString(fileName);
        String thumbnailFilePath = fileStorageService.getPathString(thumbnailFilename);
        if (imageFilePath == null || thumbnailFilePath == null) {
            fileStorageService.deleteFile(fileName);
            fileStorageService.deleteFile(thumbnailFilename);
            logger.error("Failed to locate image with filename {}, {}", imageFileName, imageOwner);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "One or more of the images failed to be stored");
        }

        return List.of(imageFilePath, thumbnailFilePath);
    }

    /**
     * Upload an image for given user/business/product.
     *
     * @param image              image file
     * @param sessionToken       current user session token
     * @param uncheckedImageType image type (user/business/product) (owner type)
     * @param userId             selected user id
     * @param businessId         selected business id
     * @param productId          selected product id
     * @return image create payload
     */
    @PostMapping("/images")
    public ResponseEntity<ImageCreatePayload> createImage(@RequestParam("images") MultipartFile image,
                                                          @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                          @RequestParam String uncheckedImageType,
                                                          @RequestParam(required = false) Integer userId,
                                                          @RequestParam(required = false) Integer businessId,
                                                          @RequestParam(required = false) String productId) {
        // Verify token access
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        ImageType imageType;
        String imageOwnerInfo;
        List<UserImage> primaryUserImages = new ArrayList<>();
        List<BusinessImage> primaryBusinessImages = new ArrayList<>();
        List<ProductImage> primaryProductImages = new ArrayList<>();
        switch (uncheckedImageType) {
            case USER_IMAGE_STRING:
                // Verify userId parameter
                getVerifiedUser(userId);

                if (userId != currentUser.getId() && !Authorization.isGAAorDGAA(currentUser)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User have no permission to do this.");
                }
                imageType = ImageType.USER_IMAGE;
                imageOwnerInfo = String.format("for product (id: %s)", userId);
                primaryUserImages = userImageRepository.findUserImagesByUserIdAndIsPrimary(userId, true);
                break;
            case BUSINESS_IMAGE_STRING:
                // Verify businessId parameter
                getVerifiedBusiness(businessId);

                // Verify access rights of the user to the business
                Authorization.verifyBusinessAdmin(currentUser, businessId);

                imageType = ImageType.BUSINESS_IMAGE;
                imageOwnerInfo = String.format("for business (id: %s)", businessId);
                primaryBusinessImages = businessImageRepository.findBusinessImageByBusinessIdAndIsPrimary(
                        businessId, true
                );
                break;
            case PRODUCT_IMAGE_STRING:
                // Verify businessId parameter
                Business business = getVerifiedBusiness(businessId);

                // Verify access rights of the user to the business
                Authorization.verifyBusinessAdmin(currentUser, businessId);

                // Verify Product id
                verifyProductId(productId, business);

                imageType = ImageType.PRODUCT_IMAGE;
                imageOwnerInfo = String.format("for business (id: %s), product (id: %s)", businessId, productId);
                primaryProductImages = productImageRepository.findProductImageByBusinessIdAndProductIdAndIsPrimary(
                        businessId, productId, true
                );
                break;
            default:
                logger.error(LOGGER_INVALID_IMAGE_TYPE, uncheckedImageType);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_IMAGE_TYPE);
        }

        // Process Image
        List<String> imageInfo = processImage(image, imageOwnerInfo);
        String imageFilePath = imageInfo.get(0);
        String thumbnailFilePath = imageInfo.get(1);
        Image storedImage;

        // Store the image data in an object
        if (imageType.equals(ImageType.USER_IMAGE)) {
            boolean isFirstImage = primaryUserImages.isEmpty();
            storedImage = userImageRepository.saveAndFlush(
                    new UserImage(userId, imageFilePath, thumbnailFilePath, isFirstImage));
        } else if (imageType.equals(ImageType.BUSINESS_IMAGE)) {
            boolean isFirstImage = primaryBusinessImages.isEmpty();
            storedImage = businessImageRepository.saveAndFlush(
                    new BusinessImage(businessId, imageFilePath, thumbnailFilePath, isFirstImage));
        } else {
            boolean isFirstImage = primaryProductImages.isEmpty();
            storedImage = productImageRepository.saveAndFlush(
                    new ProductImage(productId, businessId, imageFilePath, thumbnailFilePath, isFirstImage));
        }

        Integer storedImageId = storedImage.getId();
        String storedImageFileName = storedImage.getFilename();

        logger.info("Successfully uploaded and stored image under filename \"{}\", {}, by a user (id: {})",
                storedImageFileName, imageOwnerInfo, currentUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ImageCreatePayload(storedImageId));
    }

    /**
     * This method checks to see whether a user is logged in before calling the necessary helper functions
     * needed to delete an image.
     *
     * @param sessionToken a token used to identify a logged in user.
     * @param uncheckedImageType a string representation of the image type to be checked.
     * @param userId the id of user which the image belongs to. This is only required when deleting a user image.
     * @param businessId the id of business which the product (and image) belong to. This is only required when deleting
     *                   a business or product image.
     * @param productId the id of the product which the image belongs to. This is only required when deleting a product
     *                  image.
     * @param imageId the id of the image to be deleted.
     */
    @Transactional
    @DeleteMapping("/images/{imageId}")
    @ResponseStatus(value = HttpStatus.OK, reason = "Image deleted successfully")
    public void deleteImage(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam String uncheckedImageType,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer businessId,
            @RequestParam(required = false) String productId,
            @PathVariable Integer imageId
    ) {
        // Verify token access
        User user = Authorization.getUserVerifySession(sessionToken, userRepository);

        switch (uncheckedImageType) {
            case PRODUCT_IMAGE_STRING:
                handleProductImageDeletion(businessId, user, productId, imageId);
                break;
            case USER_IMAGE_STRING:
                handleUserImageDeletion(user, userId, imageId);
                break;
            case BUSINESS_IMAGE_STRING:
                //TODO: Business Image Deletion
                break;
            default:
                logger.error(LOGGER_INVALID_IMAGE_TYPE, uncheckedImageType);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_IMAGE_TYPE);
        }
    }

    /**
     * make given image be primary image for given owner
     *
     * @param sessionToken       current user session token
     * @param imageId            selected
     * @param uncheckedImageType image type (user/business/product) (owner type)
     * @param userId             selected user id
     * @param businessId         selected business id
     * @param productId          selected product id
     */
    @PutMapping("/images/{imageId}/makePrimary")
    @ResponseStatus(value = HttpStatus.OK, reason = "Primary image successfully updated")
    public void makePrimaryImage(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                 @PathVariable Integer imageId,
                                 @RequestParam String uncheckedImageType,
                                 @RequestParam(required = false) Integer userId,
                                 @RequestParam(required = false) Integer businessId,
                                 @RequestParam(required = false) String productId) {

        // Verify token access
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        switch (uncheckedImageType) {
            case USER_IMAGE_STRING:
                // Verify userId parameter
                getVerifiedUser(userId);

                // Verify current user permission
                if (userId != currentUser.getId() && !Authorization.isGAAorDGAA(currentUser)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "User does not have permission to update this image.");
                }

                // Verify image id
                Optional<UserImage> optionalUserImage = userImageRepository.findById(imageId);
                if (optionalUserImage.isEmpty()) {
                    logger.error("Given user image (Id: {}) does not exist.", imageId);
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given user image does not exist.");
                }
                UserImage newPrimaryUserImage = optionalUserImage.get();
                logger.info("User image (ID: {}) retrieved", imageId);

                // Set new primary image
                List<UserImage> allUserImages = userImageRepository
                        .findUserImagesByUserIdAndIsPrimary(userId, true);
                for (UserImage userImage : allUserImages) {
                    userImage.setIsPrimary(false);
                    userImageRepository.saveAndFlush(userImage);
                }

                // Set desired image to primary image
                newPrimaryUserImage.setIsPrimary(true);

                // Save the image in the repository
                userImageRepository.saveAndFlush(newPrimaryUserImage);
                break;
            case BUSINESS_IMAGE_STRING:
                //TODO: allBusinessImages
                break;
            case PRODUCT_IMAGE_STRING:
                // Verify businessId parameter
                Business business = getVerifiedBusiness(businessId);

                // Verify access rights of the user to the business
                Authorization.verifyBusinessAdmin(currentUser, businessId);

                // Verify Product id
                verifyProductId(productId, business);

                // Verify image id
                Optional<ProductImage> optionalProductImage = productImageRepository.findById(imageId);

                if (optionalProductImage.isEmpty()) {
                    logger.error("Given product image (Id: {}) does not exist.", imageId);
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given product image does not exist.");
                }

                ProductImage newPrimaryProductImage = optionalProductImage.get();
                logger.info("Product image (ID: {}) retrieved", imageId);

                // Set existing primary image to non-primary
                List<ProductImage> allProductImages = productImageRepository
                        .findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true);
                for (ProductImage productImage : allProductImages) {
                    productImage.setIsPrimary(false);
                    productImageRepository.saveAndFlush(productImage);
                }

                // Set desired image to primary image
                newPrimaryProductImage.setIsPrimary(true);

                // Save the image in the repository
                productImageRepository.saveAndFlush(newPrimaryProductImage);
                break;
            default:
                logger.error(LOGGER_INVALID_IMAGE_TYPE, uncheckedImageType);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_IMAGE_TYPE);
        }
    }

    // ---------------------------------------------- Helper Methods --------------------------------------------------

    /**
     * Gets the file extension of the given file name. This is based on the content after the last punctuation mark.
     *
     * @param fileName, the file name
     * @return extension of file name if present, otherwise the empty string
     */
    private String getFileExtension(String fileName) {
        try {
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                return fileName.substring(fileName.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    /**
     * Verifies that the given user id exists and returns the user if it exists.
     * Throws a NOT_ACCEPTABLE error is the user id does not exist.
     *
     * @param userId the id of the user to check if it exists.
     * @return user a user with an id matching userId.
     * @throws ResponseStatusException an HTTP error with code and error message.
     */
    private User getVerifiedUser(Integer userId) throws ResponseStatusException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            logger.error("Given user (ID: {}) does not exist.", userId);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given user does not exist.");
        }
        return user.get();
    }

    /**
     * Verifies that the given business id exists and returns the associated business if it exists.
     * Throws a NOT_ACCEPTABLE error is the business id does not exist.
     *
     * @param businessId the id of the business to check if it exists.
     * @return business a business which has an id matching businessId
     * @throws ResponseStatusException an HTTP error with code and error message.
     */
    private Business getVerifiedBusiness(Integer businessId) throws ResponseStatusException {
        Optional<Business> business = businessRepository.findBusinessById(businessId);
        if (business.isEmpty()) {
            logger.error("Given business (ID: {}) does not exist", businessId);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given business does not exist.");
        }
        return business.get();
    }

    /**
     * Verifies that the given product id exists.
     * Throws NOT_ACCEPTABLE error if product id does not exists.
     *
     * @param productId the id of the product to check if it exists.
     * @param business a business which has the product.
     * @throws ResponseStatusException an HTTP error with code and error message.
     */
    private void verifyProductId(String productId, Business business) throws ResponseStatusException {
        Optional<Product> product = productRepository.findProductByIdAndBusinessId(productId, business.getId());
        if (product.isEmpty()) {
            logger.error("Given product (ID: {}) does not exist in current business (ID: {})",
                    productId, business.getId());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given Product does not exist in current business.");
        }
    }

    /**
     * Verifies that the given product image id exists.
     * Throws NOT_ACCEPTABLE error if product image id does not exists.
     *
     * @param imageId the id of the image.
     * @param businessId the id of the business which has the product.
     * @param productId the id of the product which has the images,
     * @throws ResponseStatusException an HTTP error with code and error message.
     */
    private ProductImage verifyProductImageId(Integer imageId, Integer businessId, String productId, User user) throws ResponseStatusException {
        Optional<ProductImage> image = productImageRepository.findProductImageByIdAndBusinessIdAndProductId(imageId, businessId, productId);
        if (image.isEmpty()) {
            String errorMessage =
                    String.format("User (id: %d) attempted to delete a non-existent image with image id %d for business with id %d and product id %s.",
                            user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HTTP_NOT_ACCEPTABLE_MESSAGE);
        }
        return image.get();
    }

    /**
     * Updates the primary image of a product and enforces the primary image constraint.
     * If there are other images for the given product id that are not the primary image, then next one of these becomes
     * the primary image. If there are no other images, then there is no primary image (i.e. default used in front end).
     *
     * @param businessId the id of business which has the product.
     * @param productId the id of product which has the images.
     */
    private void updatePrimaryProductImage(Integer businessId, String productId) {
        List<ProductImage> primaryProductImages = productImageRepository
                .findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true);
        if (primaryProductImages.isEmpty()) {
            List<ProductImage> productImages = productImageRepository
                    .findProductImageByBusinessIdAndProductId(businessId, productId);
            if (!productImages.isEmpty()) {
                productImages.get(0).setIsPrimary(true);
                productImageRepository.save(productImages.get(0));
            }
        }
    }

    /**
     * This method performs verification before deleting a product image.
     * In order for an image to be deleted:
     *                                    the supplied businessId must be for an existing business
     *                                    the logged in user must be a business admin or application admin
     *                                    the productId must be for an existing product
     *                                    the imageId must be for an existing image
     * If all this criteria is met then the image is deleted. If the primary image is deleted then another
     * image will be set as the primary image (if another image exists).
     * @param businessId the id of business which has the product.
     * @param user the currently logged in user.
     * @param productId the id of the product which has images.
     * @param imageId the id of the image for a product.
     */
    private void handleProductImageDeletion(Integer businessId, User user, String productId, Integer imageId) {
        // Verify business parameter
        Business business = getVerifiedBusiness(businessId);

        // Verify access rights of the user to the business
        Authorization.verifyBusinessAdmin(user, businessId);

        // Verify Product id
        verifyProductId(productId, business);

        // Verify image id
        ProductImage productImage = verifyProductImageId(imageId, businessId, productId, user);

        // verify file exists & delete image
        boolean imageDeleted = fileStorageService.deleteFile(productImage.getFilename());
        boolean thumbnailDeleted = fileStorageService.deleteFile(productImage.getThumbnailFilename());
        if (!imageDeleted || !thumbnailDeleted) {
            String errorMessage = String.format("User (id: %d) attempted to delete a non-existent image with image id %d for business with id %d and product id %s.", user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HTTP_NOT_ACCEPTABLE_MESSAGE);
        }

        // Delete from database
        productImageRepository.deleteByIdAndBusinessIdAndProductId(imageId, businessId, productId);
        productImageRepository.flush();
        // Check if primary image and update primary image if it is
        updatePrimaryProductImage(businessId, productId);
    }

    /**
     * This method performs verification before deleting a user image.
     * In order for an image to be deleted:
     *                                    the userId must be for an existing user
     *                                    the currentUser trying to delete the user must be the "owner" or an application admin
     *                                    the imageId must be for an existing image.
     * If all this criteria is met then the image is deleted. If the primary image is deleted then another
     * image will be set as the primary image (if another image exists).
     * @param currentUser the currently logged in user.
     * @param userId the id of the user who's image is to be deleted.
     * @param imageId the id of the image for a user.
     */
    private void handleUserImageDeletion(User currentUser, Integer userId, Integer imageId) {
        // Verify userId parameter
        getVerifiedUser(userId);

        // 403 - Forbidden
        if (userId != currentUser.getId() && !Authorization.isGAAorDGAA(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have permission to delete image.");
        }
        // If user is able to delete image then continue

        // Verify image id
        UserImage userImage = verifyUserImageId(imageId, userId, currentUser);

        // verify file exists & delete image
        boolean imageDeleted = fileStorageService.deleteFile(userImage.getFilename());
        boolean thumbnailDeleted = fileStorageService.deleteFile(userImage.getThumbnailFilename());
        if (!imageDeleted || !thumbnailDeleted) {
            String errorMessage = String.format("User (id: %d) attempted to delete a non-existent image with image id %d for user with id %d.", currentUser.getId(), imageId, userId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HTTP_NOT_ACCEPTABLE_MESSAGE);
        }

        // Delete from database
        userImageRepository.deleteByIdAndUserId(imageId, userId);
        userImageRepository.flush();
        // Check if primary image and update primary image if it is
        updatePrimaryUserImage(userId);
    }

    /**
     * Verifies that the given user image id exists.
     * Throws NOT_ACCEPTABLE error if user image id does not exists.
     *
     * @param userId the id of the user which has images.
     * @param imageId the id of the image.
     * @throws ResponseStatusException an HTTP error with code and error message.
     */
    private UserImage verifyUserImageId(Integer imageId, Integer userId, User currentUser) throws ResponseStatusException  {
        Optional<UserImage> image = userImageRepository.findImageByIdAndUserId(imageId, userId);

        if (image.isEmpty()) {
            String errorMessage =
                    String.format("User (id: %d) attempted to delete a non-existent image with image id %d for user with id %d.",
                            currentUser.getId(), imageId, userId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, HTTP_NOT_ACCEPTABLE_MESSAGE);
        }
        return image.get();
    }

    /**
     * Updates the primary image of a user and enforces the primary image constraint.
     * If there are other images for the given user id that are not the primary image, then next one of these becomes
     * the primary image. If there are no other images, then there is no primary image (i.e. default used in front end).
     *
     * @param userId the id of the user who has the image.
     */
    private void updatePrimaryUserImage(Integer userId) {
        List<UserImage> primaryUserImages = userImageRepository.findUserImageByUserIdAndIsPrimary(userId, true);
        if (primaryUserImages.isEmpty()) {
            List<UserImage> userImages = userImageRepository.findUserImageByUserId(userId);
            if (!userImages.isEmpty()) {
                userImages.get(0).setIsPrimary(true);
                userImageRepository.save(userImages.get(0));
            }
        }
    }

}
