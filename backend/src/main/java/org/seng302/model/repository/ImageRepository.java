package org.seng302.model.repository;

import org.seng302.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface ImageRepository extends JpaRepository<Image, String> {

    List<Image> findImageByBussinesIdAndProductId(Integer businessId, String productId);

    Optional<Image> findImageByIdAndAndBussinesIdAndProductId(Integer imageId, Integer businessId, String productId);

    void deleteByIdAndBussinesIdAndProductId(int id, int bussinesId, String productId);

    List<Image> findImageByBussinesIdAndProductIdAndIsPrimary(Integer businessId, String productId, Boolean isPrimary);

}
