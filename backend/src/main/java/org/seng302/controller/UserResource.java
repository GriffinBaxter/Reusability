/**
 * Summary. This file contains the definition for the UserResource.
 *
 * Description. This file contains the defintion for the UserResource.
 *
 * @link   team-400/src/main/java/org/seng302/user/UserResource
 * @file   This file contains the definition for UserResource.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.controller;

import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.Authorization;
import org.seng302.view.incoming.UserIdPayload;
import org.seng302.view.incoming.UserLoginPayload;
import org.seng302.view.incoming.UserRegistrationPayload;
import org.seng302.view.outgoing.AddressPayload;
import org.seng302.model.repository.AddressRepository;
import org.seng302.model.Business;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.outgoing.UserPayload;
import org.seng302.view.outgoing.UserPayloadParent;
import org.seng302.view.outgoing.UserPayloadSecure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.seng302.Authorization.*;
import static org.seng302.model.enums.Role.*;

/**
 * UserResource class. This class includes:
 * POST "/login" endpoint used to allow a user to login.
 * POST "/logout" endpoint used to allow a user to logout.
 * POST "/users" endpoint used to create a new user account.
 * GET "/users/{id}" endpoint used to retrieve the details of a user account.
 * GET "/users/search" endpoint used to retrieve user accounts based on search criteria.
 * PUT "/users/{id}/makeAdmin" endpoint used to make a user account a GAA.
 * PUT "/users/{id}/revokeAdmin" endpoint used to revoke admin perms from user account (GAA -> normal user account)
 */
@RestController
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;

    private List<Business> businesses;

    private static final Logger logger = LogManager.getLogger(UserResource.class.getName());

    public UserResource(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Gets a unique session UUID, by generating until a session token is generated that does not already exist.
     * @return Unique session UUID
     */
    public String getUniqueSessionUUID() {
        String sessionUUID = User.generateSessionUUID();
        while (userRepository.findBySessionUUID(sessionUUID).isPresent()) {
            sessionUUID = User.generateSessionUUID();
        }
        return sessionUUID;
    }

    /**
     * Attempt to authenticate a user account with a username and password.
     * @param login Login payload
     * @param response HTTP Response
     */
    @PostMapping("/login")
    public UserIdPayload loginUser(@RequestBody UserLoginPayload login, HttpServletResponse response) {

        Optional<User> user = userRepository.findByEmail(login.getEmail());

        if (user.isPresent()) {
            if (user.get().verifyPassword(login.getPassword())) {
                String sessionUUID = getUniqueSessionUUID();

                user.get().setSessionUUID(sessionUUID);
                userRepository.save(user.get());

                ResponseCookie cookie = ResponseCookie.from("JSESSIONID", sessionUUID).maxAge(3600).sameSite("strict").httpOnly(true).build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                logger.info("Successful Login - User Id: {}", user.get().getId());
                return new UserIdPayload(user.get().getId());
            }
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Failed login attempt, email or password incorrect"
        );
    }

    /**
     * Attempt to authenticate a user account with a username and password.
     * @param sessionToken Login payload
     * @param response HTTP Response
     */
    @PostMapping("/logout")
    public void logoutUser(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                           HttpServletResponse response) {
        if (sessionToken != null) {

            ResponseCookie cookie = ResponseCookie.from("JSESSIONID", sessionToken).maxAge(0).sameSite("strict").httpOnly(true).build(); // maxAge 0 deletes the cookie
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        }
    }

    /**
     * Create a new user account.
     * @param registration Registration payload
     */
    @PostMapping("/users")
    public ResponseEntity<UserIdPayload> registerUser(
            @RequestBody UserRegistrationPayload registration, HttpServletResponse response
    ) {
        if (userRepository.findByEmail(registration.getEmail()).isPresent()) {
            logger.error("Registration Failure - Email already in use {}", registration.getEmail());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email address already in use"
            );
        }

        try {
            AddressPayload addressJSON = registration.getHomeAddress();
            String streetNumber = addressJSON.getStreetNumber();
            String streetName = addressJSON.getStreetName();
            String city = addressJSON.getCity();
            String region = addressJSON.getRegion();
            String country = addressJSON.getCountry();
            String postcode = addressJSON.getPostcode();
            String suburb = addressJSON.getSuburb();

            // Check to see if address already exists.
            Optional<Address> storedAddress = addressRepository.findAddressByStreetNumberAndStreetNameAndCityAndRegionAndCountryAndPostcodeAndSuburb(
                    streetNumber, streetName, city, region, country, postcode, suburb);

            // If address already exists it is retrieved.
            // The businesses already existing are also retrieved. These businesses will be
            // used to determine if a business hasn't already been created.
            if (storedAddress.isPresent()) {
                address = storedAddress.get();
                businesses = address.getBusinesses();
            } else {
                // Otherwise a new address is created and saved.
                address = new Address(
                        streetNumber,
                        streetName,
                        city,
                        region,
                        country,
                        postcode,
                        suburb
                );
                addressRepository.save(address);
                // No businesses will exist at new address.
                businesses = new ArrayList<>();
            }

            User newUser = new User(
                    registration.getFirstName(),
                    registration.getLastName(),
                    registration.getMiddleName(),
                    registration.getNickname(),
                    registration.getBio(),
                    registration.getEmail(),
                    registration.getDateOfBirth(),
                    registration.getPhoneNumber(),
                    address,
                    registration.getPassword(),
                    LocalDateTime.now(),
                    Role.USER);

            newUser.setSessionUUID(getUniqueSessionUUID());
            User createdUser = userRepository.save(newUser);

            ResponseCookie cookie = ResponseCookie.from("JSESSIONID", createdUser.getSessionUUID()).maxAge(3600).sameSite("strict").httpOnly(true).build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            logger.info("Successful Registration - User Id {}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserIdPayload(createdUser.getId()));

        } catch (IllegalUserArgumentException | IllegalAddressArgumentException e) {
            logger.error("Registration Failure - {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    /**
     * Get method for retrieving a specific user account.
     * @param id Integer Id (primary key)
     * @return User object if it exists
     */
    @GetMapping("/users/{id}")
    public UserPayloadParent retrieveUser(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id
    ) throws Exception {
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<User> optionalSelectUser = userRepository.findById(id);

        if (optionalSelectUser.isEmpty()) {
            logger.error("Requested route does exist, but some part of the request is not acceptable");
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        User selectUser = optionalSelectUser.get();

        //base info
        Role role = null;

        //stop payload loop
        List<Business> administrators = new ArrayList<>();
        administrators = selectUser.getBusinessesAdministeredObjects();
        for (Business administrator : administrators) {
            administrator.setAdministrators(new ArrayList<>());
        }

        logger.info("User Found - {}", selectUser);
        if (currentUser.getId() == id || verifyRole(currentUser, Role.DEFAULTGLOBALAPPLICATIONADMIN)){

            // If the current user is a DGAA, show the role of the user
            if (verifyRole(currentUser, Role.DEFAULTGLOBALAPPLICATIONADMIN)) {
                role = selectUser.getRole();
            } else if (currentUser.getId() == id){
                role = currentUser.getRole();
            }
            // If the current ID matches the retrieved user's ID or the current user is the DGAA, return a normal UserPayload with everything in it.
            return new UserPayload(
                    selectUser.getId(),
                    selectUser.getFirstName(),
                    selectUser.getLastName(),
                    selectUser.getMiddleName(),
                    selectUser.getNickname(),
                    selectUser.getBio(),
                    selectUser.getEmail(),
                    selectUser.getDateOfBirth(),
                    selectUser.getPhoneNumber(),
                    selectUser.getHomeAddress().toAddressPayload(),
                    selectUser.getCreated(),
                    role,
                    administrators
            );
        } else {
            // Otherwise return a UserPayloadSecure without the phone number, date of birth and a secure address with only the city, region, and country.
            return new UserPayloadSecure(
                    selectUser.getId(),
                    selectUser.getFirstName(),
                    selectUser.getLastName(),
                    selectUser.getMiddleName(),
                    selectUser.getNickname(),
                    selectUser.getBio(),
                    selectUser.getEmail(),
                    selectUser.getHomeAddress().toAddressPayloadSecure(),
                    selectUser.getCreated(),
                    role,
                    administrators
            );
        }


    }

    /**
     * Search for users by first name, middle name, last name, or nickname.
     * Returns paginated and ordered results based on input query params.
     * @param sessionToken Session token
     * @param searchQuery Search query
     * @param orderBy Column to order the results by
     * @param page Page number to return results from
     * @return A list of UserPayload objects matching the search query
     */
    @GetMapping("/users/search")
    public ResponseEntity<List<UserPayloadSecure>> searchUsers(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam String searchQuery,
            @RequestParam(defaultValue = "fullNameASC") String orderBy,
            @RequestParam(defaultValue = "0") String page
    ) throws Exception {
        logger.debug("User search request received with search query {}, order by {}, page {}", searchQuery, orderBy, page);

        //TODO check this
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (final NumberFormatException e) {
            logger.error("400 [BAD REQUEST] - {} is not a valid page number", page);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page parameter invalid"
            );
        }

        // Front-end displays 5 users per page
        int pageSize = 5;

        Sort sortBy = null;
        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "fullNameASC":

                sortBy = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

                break;
            case "fullNameDESC":

                sortBy = Sort.by(Sort.Order.desc("firstName").ignoreCase()).and(Sort.by(Sort.Order.desc("middleName").ignoreCase())).and(Sort.by(Sort.Order.desc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

                break;
            case "nicknameASC":

                sortBy = Sort.by(Sort.Order.asc("nickname").ignoreCase()).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

                break;
            case "nicknameDESC":

                sortBy = Sort.by(Sort.Order.desc("nickname").ignoreCase()).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

                break;
            case "emailASC":

                sortBy = Sort.by(Sort.Order.asc("email").ignoreCase());

                break;
            case "emailDESC":

                sortBy = Sort.by(Sort.Order.desc("email").ignoreCase());

                break;
            case "addressASC":

                sortBy = Sort.by(Sort.Order.asc("homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.asc("homeAddress.region").ignoreCase()).and(Sort.by(Sort.Order.asc("homeAddress.country").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase())));

                break;
            case "addressDESC":

                sortBy = Sort.by(Sort.Order.desc("homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.desc("homeAddress.region").ignoreCase()).and(Sort.by(Sort.Order.desc("homeAddress.country").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase())));

                break;
            default:
                logger.error("400 [BAD REQUEST] - {} is not a valid order by parameter", orderBy);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OrderBy Field invalid"
                );
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        Page<User> pagedResult = userRepository.findAllUsersByNames(searchQuery, paging);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Search Success - 200 [OK] -  Users retrieved for search query {}, order by {}, page {}", searchQuery, orderBy, pageNo);

        logger.debug("Users Found: {}", pagedResult.toList());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(convertToPayloadSecureAndRemoveRolesIfNotAuthenticated(pagedResult.getContent(), currentUser));
    }

    //TODO write unit tests

    /**
     * Converts a list of users to a list of user payloads and removes the users' roles if the current user is not
     * authorized to view roles.
     * @param userList A list of users to convert
     * @param user The current user
     * @return A list of user payloads
     * @throws Exception An exception
     */
    public List<UserPayloadSecure> convertToPayloadSecureAndRemoveRolesIfNotAuthenticated(List<User> userList, User user) throws Exception {
        List<UserPayloadSecure> userPayloadList = new ArrayList<>();
        userPayloadList = UserPayloadSecure.convertToPayloadSecure(userList);

        for (UserPayloadSecure userPayloadSecure: userPayloadList) {
            Role role = null;
            if (verifyRole(user, Role.DEFAULTGLOBALAPPLICATIONADMIN)) {
                role = userPayloadSecure.getRole();
            }
            userPayloadSecure.setRole(role);
        }

        return userPayloadList;
    }

    /**
     * Get method for change the Role of a user from USER to GLOBALAPPLICATIONADMIN by Email address
     * @param id Email address (primary key)
     */
    @PutMapping("/users/{id}/makeAdmin")
    @ResponseStatus(value = HttpStatus.OK, reason = "Action completed successfully")
    public void setGAA(@PathVariable int id, @CookieValue(value = "JSESSIONID", required = false) String sessionToken){
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<User> optionalSelectedUser = userRepository.findById(id);

        if (optionalSelectedUser.isEmpty()) {
            logger.error("Requested route does exist, but some part of the request is not acceptable");
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        } else {
            User selectedUser = optionalSelectedUser.get();
            if (selectedUser.getRole() == USER && currentUser.getRole() == DEFAULTGLOBALAPPLICATIONADMIN){
                selectedUser.setRole(GLOBALAPPLICATIONADMIN);
                userRepository.saveAndFlush(selectedUser);
                logger.info("User with Id: {} is now GAA.", selectedUser.getId());
            } else {
                logger.error("User does not have permission to perform action.");
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "The user does not have permission to perform the requested action"
                );
            }
        }
    }


    /**
     * Get method for change the Role of a user account from USE to GLOBALAPPLICATIONADMIN by Email address
     * @param id mail address (primary key)
     */
    @PutMapping("/users/{id}/revokeAdmin")
    @ResponseStatus(value = HttpStatus.OK, reason = "Account created successfully")
    public void revokeGAA(@PathVariable int id, @CookieValue(value = "JSESSIONID", required = false) String sessionToken) {
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<User> optionalSelectedUser = userRepository.findById(id);

        if (optionalSelectedUser.isEmpty()){
            logger.error("Requested route does exist, but some part of the request is not acceptable");
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        } else {
            User selectedUser = optionalSelectedUser.get();
            if (selectedUser.getRole() == GLOBALAPPLICATIONADMIN && currentUser.getRole() == DEFAULTGLOBALAPPLICATIONADMIN) {
                selectedUser.setRole(USER);
                userRepository.saveAndFlush(selectedUser);
                logger.info("User with Id: {} is now USER.", selectedUser.getId());
            } else {
                logger.error("User does not have permission to perform action.");
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "The user does not have permission to perform the requested action"
                );
            }
        }
    }
}
