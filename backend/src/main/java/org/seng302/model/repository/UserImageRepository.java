package org.seng302.model.repository;

import org.seng302.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {

    /**
     * Search for all primary images for given user.
     * @param userId user's id
     * @param isPrimary primary image
     * @return list of primary images for given user.
     */
    List<UserImage> findUserImagesByUserIdAndIsPrimary(Integer userId, Boolean isPrimary);
}
