package org.seng302.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository interface
 */
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Search for an user account by their email address.
     * @param emailAddress Email address
     * @return A user matching the email address
     */
    Optional<User> findByEmail(String emailAddress);

    /**
     * Search for an user account by its id.
     * @param id Integer id
     * @return A user matching the id
     */
    Optional<User> findById(Integer id);

    /**
     * Search for a user account by its session UUID.
     * @param sessionUUID Session UUID
     * @return A user matching the session UUID
     */
    Optional<User> findBySessionUUID(String sessionUUID);

    /**
     * Search for a user account by their first, middle and last name, ignoring case.
     * @param firstName First name
     * @param middleName Middle name
     * @param lastName Last name
     * @return A list of matching UserPayload objects
     */
    List<User> findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String middleName, String lastName
    );

    /**
     * Search for a user account by their first and last name, ignoring case.
     * @param firstName First name
     * @param lastName Last name
     * @return A list of matching UserPayload objects
     */
    List<User> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    /**
     * Search for a user account by their nickname, ignoring case.
     * @param nickname Nickname
     * @return A list of matching UserPayload objects
     */
    List<User> findByNicknameIgnoreCase(String nickname);

    /**
     * Search for a user account by their first name, ignoring case.
     * @param firstName First name
     * @return A list of matching UserPayload objects
     */
    List<User> findByFirstNameIgnoreCase(String firstName);

    /**
     * Search for a user account by their last name, ignoring case.
     * @param lastName Last name
     * @return A list of matching UserPayload objects
     */
    List<User> findByLastNameIgnoreCase(String lastName);

    /**
     * Search for a user account by their middle name, ignoring case.
     * @param middleName Middle name
     * @return A list of matching UserPayload objects
     */
    List<User> findByMiddleNameIgnoreCase(String middleName);

    /**
     * Search for an individual account by their role.
     * @param role Determines admin privileges.
     * @return A boolean whether a user with the role exists.
     */
    boolean existsByRole(Role role);

    /**
     * Search for users by their first, middle, last name or nick name, ignoring case.
     * Takes a pageable object for pagination and allows partial matches (not case sensitive).
     * @param searchValue The user's inputted search string
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching user results
     */
    @Query(value="select u from User u where upper(CONCAT(u.firstName, ' ', u.middleName, ' ', u.lastName)) LIKE CONCAT('%',upper(?1),'%') or upper(u.nickname) LIKE CONCAT('%',upper(?1),'%') or upper(CONCAT(u.firstName, ' ', u.lastName)) LIKE CONCAT('%',upper(?1),'%') or upper(CONCAT(u.firstName, ' ', u.middleName)) LIKE CONCAT('%',upper(?1),'%') or upper(CONCAT(u.middleName, ' ', u.lastName)) LIKE CONCAT('%',upper(?1),'%')")
    Page<User> findAllUsersByNames(String searchValue, Pageable pageable);
//
//
//    /**
//     * Search for all users in database.
//     * @param name
//     * @return
//     */
//    List<UserPayload> findAllByFirstNameContains(String name);
//

}
