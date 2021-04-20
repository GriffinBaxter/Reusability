package org.seng302.user;

import org.seng302.address.Address;
import org.seng302.address.AddressPayload;
import org.seng302.address.AddressRepository;
import org.seng302.business.Business;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.seng302.user.Role.*;

/**
 * UserResource class
 */
@RestController
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;
    private List<Business> businesses;

    public UserResource(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * Verifies the session token, throws an error if it does not exist, and if it does, returns the User object.
     * @param sessionToken Session token
     * @return User object
     */
    public User getUserVerifySession(String sessionToken) {
        Optional<User> user = userRepository.findBySessionUUID(sessionToken);
        if (sessionToken == null || user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Access token is missing or invalid"
            );
        } else {
            return user.get();
        }
    }

    /**
     * Checks if the current user's role matches the role parameter.
     * This method is useful for user authentication/identification.
     * @param currentUser current user
     * @param role Role being matched
     * @return boolean Returns true if the current user's role matches the role parameter, otherwise false.
     */
    private boolean verifyRole(User currentUser, Role role) {
        if (currentUser.getRole().equals(role)) {
            return true;
        }
        return false;
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

                Cookie cookie = new Cookie("JSESSIONID", sessionUUID);
                cookie.setHttpOnly(true);
                response.addCookie(cookie);

                return new UserIdPayload(user.get().getId());
            }
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Failed login attempt, email or password incorrect"
        );
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

            // Check to see if address already exists.
            Optional<Address> storedAddress = addressRepository.findAddressByStreetNumberAndStreetNameAndCityAndRegionAndCountryAndPostcode(
                    streetNumber, streetName, city, region, country, postcode);

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
                        postcode
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

            Cookie cookie = new Cookie("JSESSIONID", createdUser.getSessionUUID());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.CREATED).body(new UserIdPayload(createdUser.getId()));

        } catch (Exception e) {
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
    public UserPayload retrieveUser(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id
    ) throws Exception {
        getUserVerifySession(sessionToken);

        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        Role role = null;

        if (verifyRole(sessionToken, Role.DEFAULTGLOBALAPPLICATIONADMIN)) {
            role = user.get().getRole();
        }

        List<Business> administrators = user.get().getBusinessesAdministeredObjects();
        for (Business administrator : administrators) {
            administrator.setAdministrators(new ArrayList<>());
        }

        Address address = user.get().getHomeAddress();
        AddressPayload addressPayload = new AddressPayload(
                address.getStreetNumber(),
                address.getStreetName(),
                address.getCity(),
                address.getRegion(),
                address.getCountry(),
                address.getPostcode()
        );
        return new UserPayload(
                user.get().getId(),
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getMiddleName(),
                user.get().getNickname(),
                user.get().getBio(),
                user.get().getEmail(),
                user.get().getDateOfBirth(),
                user.get().getPhoneNumber(),
                addressPayload,
                user.get().getCreated(),
                role,
                administrators
        );
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
    public ResponseEntity<List<UserPayload>> searchUsers(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam String searchQuery,
            @RequestParam String orderBy,
            @RequestParam String page
    ) throws Exception {
        // TODO Add logging

        getUserVerifySession(sessionToken);
        int pageNo;
        try {
            pageNo = Integer.parseInt(page);
        } catch (final NumberFormatException e) {
            // Invalid page input
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
        if (orderBy.equals("fullNameASC")) {

            sortBy = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

        } else if (orderBy.equals("fullNameDESC")) {

            sortBy = Sort.by(Sort.Order.desc("firstName").ignoreCase()).and(Sort.by(Sort.Order.desc("middleName").ignoreCase())).and(Sort.by(Sort.Order.desc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

        } else if (orderBy.equals("nicknameASC")) {

            sortBy = Sort.by(Sort.Order.asc("nickname").ignoreCase()).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

        } else if (orderBy.equals("nicknameDESC")) {

            sortBy = Sort.by(Sort.Order.desc("nickname").ignoreCase()).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

        } else if (orderBy.equals("emailASC")) {

            sortBy = Sort.by(Sort.Order.asc("email").ignoreCase());

        } else if (orderBy.equals("emailDESC")) {

            sortBy = Sort.by(Sort.Order.desc("email").ignoreCase());

        } else if (orderBy.equals("addressASC")) {

            sortBy = Sort.by(Sort.Order.asc("homeAddress").ignoreCase()).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

        } else if (orderBy.equals("addressDESC")) {

            sortBy = Sort.by(Sort.Order.desc("homeAddress").ignoreCase()).and(Sort.by(Sort.Order.asc("email").ignoreCase()));

        } else {
            // Invalid orderBy input
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

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(convertToPayload(pagedResult.getContent(), sessionToken));
    }

    /**
     * Converts a list of users to a list of userPayloads.
     * @param userList The given list of users
     * @param sessionToken The current session token to verify
     * @return A list of userPayloads.
     */
    public List<UserPayload> convertToPayload(List<User> userList, String sessionToken) throws Exception {
        List<UserPayload> payLoads = new ArrayList<>();
        for (User user : userList) {
            Role role = null;
            if (verifyRole(sessionToken, Role.DEFAULTGLOBALAPPLICATIONADMIN)) {
                role = user.getRole();
            }
            UserPayload newPayload = new UserPayload(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getMiddleName(),
                    user.getNickname(),
                    user.getBio(),
                    user.getEmail(),
                    user.getDateOfBirth(),
                    user.getPhoneNumber(),
                    user.getHomeAddress().toAddressPayload(),
                    user.getCreated(),
                    role,
                    user.getBusinessesAdministeredObjects()
            );
            payLoads.add(newPayload);
        }
        return payLoads;
    }

    /**
     * Checks if the current user's role matches the role parameter.
     * This method is useful for user authentication/identification.
     * @param sessionToken Session token
     * @param role Role being matched
     * @return boolean Returns true if the current user's role matches the role parameter, otherwise false.
     */
    private boolean verifyRole(String sessionToken, Role role) {
        Optional<User> userOptional = userRepository.findBySessionUUID(sessionToken);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get method for change the Role of a user from USER to GLOBALAPPLICATIONADMIN by Email address
     * @param id Email address (primary key)
     */
    @PutMapping("/users/{id}/makeAdmin")
    @ResponseStatus(value = HttpStatus.OK, reason = "Action completed successfully")
    public void setGAA(@PathVariable int id, HttpServletRequest request,
                       @CookieValue(value = "JSESSIONID", required = false) String sessionToken){
        User currentUser = getUserVerifySession(sessionToken);

        Optional<User> optionalSelectedUser = userRepository.findById(id);

        if (optionalSelectedUser.isEmpty()) {
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
            } else {
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
    public void revokeGAA(@PathVariable int id, HttpServletRequest request,
                          @CookieValue(value = "JSESSIONID", required = false) String sessionToken) {
        User currentUser = getUserVerifySession(sessionToken);

        Optional<User> optionalSelectedUser = userRepository.findById(id);

        if (optionalSelectedUser.isEmpty()){
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
            } else {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "The user does not have permission to perform the requested action"
                );
            }
        }
    }
}
