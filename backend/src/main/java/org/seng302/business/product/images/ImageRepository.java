package org.seng302.business.product.images;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ImageRepository extends JpaRepository<Image, Integer> {

    /**
     * Finds a image with a given image id.
     * @param id - A image id.
     * @return Returns a image entity or null.
     */
    Optional<Image> findImageById(Integer id);


    /**
     * Gets all the image entities
     * @return Returns a list of Images in the image table.
     */
    @Query(value = "select i from Image i")
    List<Image> getAll();

}
