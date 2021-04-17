package org.seng302.business.product.images;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ImageRepository extends JpaRepository<Image, Integer> {

    /**
     * Finds a image with a given image id.
     * @param id - A image id.
     * @return Returns a image entity or null.
     */
    Optional<Image> findImageById(Integer id);

}
