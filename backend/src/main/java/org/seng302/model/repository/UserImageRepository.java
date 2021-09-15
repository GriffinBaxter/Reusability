package org.seng302.model.repository;

import org.seng302.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {

    /**
     * Search for all primary images for given user.
     * @param userId user's id
     * @param isPrimary primary image
     * @return list of primary images for given user.
     */
    List<UserImage> findUserImagesByUserIdAndIsPrimary(Integer userId, Boolean isPrimary);

    /**
     * Search for an user image by image id and user id.
     * @param imageId the id of the image to search for.
     * @param userId the id of the user to search for.
     * @return an optional that may or may not contain a user image.
     */
    Optional<UserImage> findImageByIdAndUserId(Integer imageId, Integer userId);

    /**
     * Delete the image for a user.
     * @param id the id of the image to delete.
     * @param userId the id of the user who has the image.
     */
    void deleteByIdAndUserId(Integer id, Integer userId);

    /**
     * Find all images belonging to a user.
     * @param userId the id of user to search images by.
     * @return a list of user images (may be empty)
     */
    List<UserImage> findUserImageByUserId(Integer userId);

    /**
     * Find all primary images belonging to a user.
     * @param userId the id of user to search images by.
     * @param isPrimary a boolean value. if true search for primary images, if false search for non-primary images.
     * @return a list of primary user images (may be empty).
     */
    List<UserImage> findUserImageByUserIdAndIsPrimary(Integer userId, Boolean isPrimary);
}
