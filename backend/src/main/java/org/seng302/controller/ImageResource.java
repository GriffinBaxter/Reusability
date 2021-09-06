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
 * POST   "/images" endpoint used for adding image to a product of a businesses.
 * DELETE "/businesses/{businessId}/products/{productId}/images/{imageId}" endpoint for deleting an image for a product of a business.
 * PUT    "/images/{imageId}/makePrimary" endpoint for changing the primary image of a product.
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
    @Value("product-images")
    private FileStorageService fileStorageService;

    private static final Logger logger = LogManager.getLogger(ImageResource.class.getName());

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
            String errorMessage = String.format(
                    "Thumbnail unable to be created from image (name: %s)", image.getName()
            );
            logger.error(errorMessage);
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
            String errorMessage = String.format("File already existed. Canceling storage of image with filename %s, %s",
                    imageFileName, imageOwner);
            logger.error(errorMessage);
            fileStorageService.deleteFile(fileName);
            fileStorageService.deleteFile(thumbnailFilename);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "One or more of the images failed to be stored.");
        }
        // If this fails we need to ensure it was removed.
        catch (IOException e) {
            String errorMessage = String.format("Failed to store image with filename %s, %s", imageFileName, imageOwner);
            logger.error(errorMessage);
            fileStorageService.deleteFile(fileName);
            fileStorageService.deleteFile(thumbnailFilename);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "One or more of the images failed to be stored.");
        }

        // If we cannot get the file path, then we have to assume that it failed.
        String imageFilePath = fileStorageService.getPathString(fileName);
        String thumbnailFilePath = fileStorageService.getPathString(thumbnailFilename);
        if (imageFilePath == null || thumbnailFilePath == null) {
            fileStorageService.deleteFile(fileName);
            fileStorageService.deleteFile(thumbnailFilename);
            String errorMessage = String.format("Failed to locate image with filename %s, %s", imageFileName, imageOwner);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "One or more of the images failed to be stored");
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
        List<UserImage> allUserImages = new ArrayList<>();
        List<ProductImage> allProductImages = new ArrayList<>();
        switch (uncheckedImageType) {
            case "USER_IMAGE":
                // Verify userIs parameter
                getVerifiedUser(userId);

                if (userId != currentUser.getId() && !Authorization.isGAAorDGAA(currentUser)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User have no permission to do this.");
                }
                imageType = ImageType.USER_IMAGE;
                imageOwnerInfo = String.format("for product (id: %s)", userId);
                allUserImages = userImageRepository.findUserImagesByUserIdAndIsPrimary(userId, true);
                break;
            case "BUSINESS_IMAGE":
                imageType = ImageType.BUSINESS_IMAGE;
                imageOwnerInfo = String.format("for business (id: %s)", businessId);
                //TODO: allBusinessImages
                break;
            case "PRODUCT_IMAGE":
                // Verify businessIs parameter
                Business business = getVerifiedBusiness(businessId);

                // Verify access rights of the user to the business
                Authorization.verifyBusinessAdmin(currentUser, businessId);

                // Verify Product id
                verifyProductId(productId, business, currentUser);

                imageType = ImageType.PRODUCT_IMAGE;
                imageOwnerInfo = String.format("for business (id: %s), product (id: %s)", businessId, productId);
                allProductImages = productImageRepository.findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true);
                break;
            default:
                logger.error("Given image type {} invalid", uncheckedImageType);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image type");
        }

        // Process Image
        List<String> imageInfo = processImage(image, imageOwnerInfo);
        String imageFilePath = imageInfo.get(0);
        String thumbnailFilePath = imageInfo.get(1);

        Integer storedImageId;
        String storedImageFileName;
        // Store the image data in an object
        if (imageType.equals(ImageType.USER_IMAGE)) {
            boolean isFirstImage = allUserImages.isEmpty();
            UserImage storedUserImage = userImageRepository.saveAndFlush(
                    new UserImage(userId, imageFilePath, thumbnailFilePath, isFirstImage));
            storedImageId = storedUserImage.getId();
            storedImageFileName = storedUserImage.getFilename();
//        } else if (imageType.equals(ImageType.BUSINESS_IMAGE)) {
            //TODO:
        } else {
            boolean isFirstImage = allProductImages.isEmpty();
            ProductImage storedProductImage = productImageRepository.saveAndFlush(
                    new ProductImage(productId, businessId, imageFilePath, thumbnailFilePath, isFirstImage));
            storedImageId = storedProductImage.getId();
            storedImageFileName = storedProductImage.getFilename();
        }

        logger.info("Successfully uploaded and stored image under filename \"{}\", {}, by a user (id: {})",
                storedImageFileName, imageOwnerInfo, currentUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ImageCreatePayload(storedImageId));
    }

    @Transactional
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
        Business business = getVerifiedBusiness(businessId);

        // Verify access rights of the user to the business
        Authorization.verifyBusinessAdmin(user, businessId);

        // Verify Product id
        verifyProductId(productId, business, user);

        // Verify image id
        ProductImage productImage = verifyImageId(imageId, businessId, productId, user);

        // verify file exists & delete image
        boolean imageDeleted = fileStorageService.deleteFile(productImage.getFilename());
        boolean thumbnailDeleted = fileStorageService.deleteFile(productImage.getThumbnailFilename());
        if (!imageDeleted || !thumbnailDeleted) {
            String errorMessage = String.format("User (id: %d) attempted to delete a non-existent image with image id %d for business with id %d and product id %s.", user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }

        // Delete from database
        productImageRepository.deleteByIdAndBusinessIdAndProductId(imageId, businessId, productId);
        productImageRepository.flush();
        // Check if primary image and update primary image if it is
        updatePrimaryImage(businessId, productId);
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
            case "USER_IMAGE":
                // Verify userId parameter
                getVerifiedUser(userId);

                // Verify current user permission
                if (userId != currentUser.getId() && !Authorization.isGAAorDGAA(currentUser)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User have no permission to do this.");
                }

                // Verify image id
                Optional<UserImage> optionalUserImage = userImageRepository.findById(imageId);
                if (optionalUserImage.isEmpty()) {
                    logger.error("Given image (Id: {}) does not exist.", imageId);
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given image does not exist.");
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
            case "BUSINESS_IMAGE":
                //TODO: allBusinessImages
                break;
            case "PRODUCT_IMAGE":
                // Verify businessIs parameter
                Business business = getVerifiedBusiness(businessId);

                // Verify access rights of the user to the business
                Authorization.verifyBusinessAdmin(currentUser, businessId);

                // Verify Product id
                verifyProductId(productId, business, currentUser);

                // Verify image id
                Optional<ProductImage> optionalProductImage = productImageRepository.findById(imageId);

                if (optionalProductImage.isEmpty()) {
                    logger.error("Given image (Id: {}) does not exist.", imageId);
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given image does not exist.");
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
                logger.error("Given image type {} invalid", uncheckedImageType);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image type");
        }
    }

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
     * <p>
     * PRECONDITIONS:
     * POST CONDITIONS:
     *
     * @param userId
     * @return user
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
     * <p>
     * PRECONDITIONS:
     * POST CONDITIONS:
     *
     * @param businessId
     * @return business
     */
    private Business getVerifiedBusiness(Integer businessId) throws ResponseStatusException {
        Optional<Business> business = businessRepository.findBusinessById(businessId);
        if (business.isEmpty()) {
            logger.error("Given business (ID: {}) does not exist", businessId);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given business does not exist.");
        }
        return business.get();
    }

    private Image getVerifiedImage(Optional<Image> optionalImage, Integer imageId) {
        if (optionalImage.isEmpty()) {
            logger.error("Given image (Id: {}) does not exist.", imageId);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given image does not exist.");
        }
        return optionalImage.get();
    }

    /**
     * Verifies that the given product id exists.
     * Throws NOT_ACCEPTABLE error if product id does not exists.
     * <p>
     * PRECONDITIONS:
     * POST CONDITIONS:
     *
     * @param productId
     * @param business
     * @param user
     */
    private void verifyProductId(String productId, Business business, User user) throws ResponseStatusException {
        Optional<Product> product = productRepository.findProductByIdAndBusinessId(productId, business.getId());
        if (product.isEmpty()) {
            logger.error("Given product (ID: {}) does not exist in current business (ID: {})",
                    productId, business.getId());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Given Product does not exist in current business.");
        }
    }

    /**
     * Verifies that the given image id exists.
     * Throws NOT_ACCEPTABLE error if image id does not exists.
     * <p>
     * PRECONDITIONS:
     * POST CONDITIONS:
     *
     * @param imageId
     * @param businessId
     * @param productId
     */
    private ProductImage verifyImageId(Integer imageId,
                                       Integer businessId,
                                       String productId,
                                       User user)
            throws ResponseStatusException {
        Optional<ProductImage> image = productImageRepository.findProductImageByIdAndBusinessIdAndProductId(imageId, businessId, productId);

        if (image.isEmpty()) {
            String errorMessage =
                    String.format("User (id: %d) attempted to delete a non-existent image with image id %d for business with id %d and product id %s.",
                            user.getId(), imageId, businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested route does exist (so not a 404) " +
                    "but some part of the request is not acceptable, " +
                    "for example trying to access a resource by an ID that does not exist.");
        }
        return image.get();
    }

    /**
     * Updates the primary image and enforces the primary image constraint.
     * If there are other images for the given product id that are not the primary image, then next one of these becomes
     * the primary image. If there are no other images, then there is no primary image (i.e. default used in front end).
     * <p>
     * PRECONDITIONS:
     * POST CONDITIONS:
     *
     * @param businessId
     * @param productId
     */
    private void updatePrimaryImage(Integer businessId, String productId) {
        List<ProductImage> primaryProductImages = productImageRepository
                .findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true);
        if (primaryProductImages.isEmpty()) {
            List<ProductImage> productImages = productImageRepository.findProductImageByBusinessIdAndProductId(businessId, productId);
            if (!productImages.isEmpty()) {
                productImages.get(0).setIsPrimary(true);
                productImageRepository.save(productImages.get(0));
            }
        }
    }

}
