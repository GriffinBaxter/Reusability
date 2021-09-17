package org.seng302.model.repository;

import org.seng302.model.BusinessImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface BusinessImageRepository extends JpaRepository<BusinessImage, Integer> {

    /**
     * Find all images belonging to a business.
     * @param businessId the id of business to search images by.
     * @return a list of business images (could be empty)
     */
    List<BusinessImage> findBusinessImageByBusinessId(Integer businessId);

    /**
     * Search for a business image by image id and business id.
     * @param imageId the id of the image to search for.
     * @param businessId the id of the business to search for.
     * @return an optional that may or may not contain a business image.
     */
    Optional<BusinessImage> findBusinessImageByIdAndBusinessId(Integer imageId, Integer businessId);

    /**
     * Delete the image for a business.
     * @param id the id of the image to delete.
     * @param businessId the id of the business that has the image.
     */
    void deleteByIdAndBusinessId(int id, int businessId);

    /**
     * Find all primary images belonging to a business.
     * @param businessId the id of business to search images by.
     * @param isPrimary a boolean value. if true search for primary images, if false search for non-primary images.
     * @return a list of primary business images (could be empty).
     */
    List<BusinessImage> findBusinessImageByBusinessIdAndIsPrimary(Integer businessId, Boolean isPrimary);
}
