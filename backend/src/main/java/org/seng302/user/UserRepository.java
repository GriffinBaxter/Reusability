package org.seng302.user;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
