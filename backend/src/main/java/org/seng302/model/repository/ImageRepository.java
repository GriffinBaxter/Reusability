package org.seng302.model.repository;

import org.seng302.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface ImageRepository extends JpaRepository<Image, String> {

    List<Image> findImageByBussinesIdAndProductId(Integer businessId, String productId);

}
