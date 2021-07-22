package org.seng302.model.repository;

import org.seng302.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * A custom interface that defines methods which can be used to retrieve users.
 */
public interface UserRepositoryCustom {

    /**
     * Search for users by user names.
     * @param names A list of user names.
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching user results.
     *
     * Preconditions:  A non-null list of names to search for users.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching user results.
     */
    Page<User> findAllUsersByNames(List<String> names, Pageable pageable);

}
