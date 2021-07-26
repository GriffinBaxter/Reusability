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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
import java.util.List;

/**
 * Controller class for images. This class includes:
 * POST "/businesses/{businessId}/products/{productId}/images" endpoint used for adding image to a product of a businesses.
 * DELETE "/businesses/{businessId}/products/{productId}/images/{imageId}" endpoint for deleting an image for a product of a business.
 * PUT "/businesses/{businessId}/products/{productId}/images/{imageId}/makeprimary" endpoint for changing the primary image of a product.
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

    @Autowired
    @Value("product-images")
    private FileStorageService fileStorageService;

    private static final Logger logger = LogManager.getLogger(ImageResource.class.getName());

    public ImageResource() {}

    public ImageResource(BusinessRepository businessRepository, UserRepository userRepository,
                         ProductRepository productRepository, ImageRepository imageRepository, FileStorageService fileStorageService) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.fileStorageService = fileStorageService;
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

        // Check file has the original name attached
        if (imageFileName == null) {
            logger.error("Image uploaded was missing original file name.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image was missing a file name.");
        }

        // Checking file extension
        String imageType = getFileExtension(imageFileName);
        if (!imageType.equalsIgnoreCase("jpg") && !imageType.equalsIgnoreCase("jpeg") &&
                !imageType.equalsIgnoreCase("png") && !imageType.equalsIgnoreCase("gif")) {

            String errorMessage = String.format("Creating another image with unknown if it is actually an IMAGE (name: %s) !!!", image.getName());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The file type of the image uploaded is not " +
                    "supported. Only JPG, JPEG, PNG and GIF are supported.");
        }

        String fileExtension = getFileExtension(imageFileName);
        
        // Creating thumbnail image
        InputStream thumbnailInputStream;
        try {
            BufferedImage thumbnail = ImageIO.read(image.getInputStream());
            
            // Crops image in the centre if not a square
            int newWidthHeight = Math.min(thumbnail.getWidth(), thumbnail.getHeight());
            int x = (thumbnail.getWidth() - newWidthHeight) / 2;
            int y = (thumbnail.getHeight() - newWidthHeight) / 2;
            BufferedImage cropped = thumbnail.getSubimage(x, y, newWidthHeight, newWidthHeight);

            java.awt.Image thumbnailData = cropped.getScaledInstance(250, 250, java.awt.Image.SCALE_SMOOTH);

            BufferedImage bufferedImage = new BufferedImage(
                    thumbnailData.getWidth(null),
                    thumbnailData.getHeight(null),
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.drawImage(thumbnailData, null, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, fileExtension, byteArrayOutputStream);
            thumbnailInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
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
            
            if (
                    !fileStorageService.storeFile(image.getInputStream(), fileName) ||
                    !fileStorageService.storeFile(thumbnailInputStream, thumbnailFilename)
            ) {
                throw new IOException("Failed to store images");
            }
        }
        // If the file already exists then we need to just something went wrong
        catch (FileAlreadyExistsException e) {
            String errorMessage = String.format("File already existed. Canceling storage of image with filename %s, for user (id: %d), for business (id: %d), for product (id: %s)", imageFileName, user.getId(), businessId, productId);
            logger.error(errorMessage);
            fileStorageService.deleteFile(fileName);
            fileStorageService.deleteFile(thumbnailFilename);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "One or more of the images failed to be stored.");
        }
        // If this fails we need to ensure it was removed.
        catch (IOException e) {
            String errorMessage = String.format("Failed to store image with filename %s, for user (id: %d), for business (id: %d), for product (id: %s)", imageFileName, user.getId(), businessId, productId);
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
            String errorMessage = String.format("Failed to locate image with filename %s, for user (id: %d), for business (id: %d), for product (id: %s)", imageFileName, user.getId(), businessId, productId);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "One or more of the images failed to be stored");
        }

        // Store the image data in an object
        List<Image> primaryImages = imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true);
        boolean isFirstImage = primaryImages.isEmpty();
        Image storedImage = imageRepository.saveAndFlush(
                new Image(productId, businessId, imageFilePath, thumbnailFilePath, isFirstImage)
        );

        String successMessage = String.format("Successfully uploaded and stored image under filename \"%s\", for product id: \"%s\", for business id: \"%d\", by a user (id: %d)", storedImage.getFilename(), productId, businessId, user.getId());
        logger.info(successMessage);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ImageCreatePayload(storedImage.getId()));

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
        Optional<Business> business = getVerifiedBusiness(businessId);

        // Verify access rights of the user to the business
        verifyAdministrationRights(user, business);

        // Verify Product id
        verifyProductId(productId, business, user);

        // Verify image id
        Image image = verifyImageId(imageId, businessId, productId, user);

        // verify file exists & delete image
        boolean imageDeleted = fileStorageService.deleteFile(image.getFilename());
        boolean thumbnailDeleted = fileStorageService.deleteFile(image.getThumbnailFilename());
        if (!imageDeleted || !thumbnailDeleted) {
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

    @PutMapping("/businesses/{businessId}/products/{productId}/images/{imageId}/makeprimary")
    @ResponseStatus(value = HttpStatus.OK, reason = "Primary image successfully updated")
    public void makePrimaryImage(
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

        // Set existing primary image to non-primary
        List<Image> primaryImages = imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true);
        for (Image primaryImage: primaryImages) {
            primaryImage.setIsPrimary(false);
        }
        
        // Set desired image to primary image
        image.setIsPrimary(true);
        
        // Save the image in the repository
        imageRepository.save(image);
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
        } catch ( Exception e) {
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
        Optional<Image> image = imageRepository.findImageByIdAndBussinesIdAndProductId(imageId, businessId, productId);

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
