package org.seng302.model.repository;

import org.seng302.model.BusinessImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface BusinessImageRepository extends JpaRepository<BusinessImage, Integer> {
    
    List<BusinessImage> findBusinessImageByBusinessIdAndIsPrimary(Integer businessId, Boolean isPrimary);

}
