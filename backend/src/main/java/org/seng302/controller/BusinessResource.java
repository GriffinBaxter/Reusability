/**
 * Summary. This file contains the definition for the BusinessResource.
 *
 * Description. This file contains the defintion for the BusinessResource.
 *
 * @link   team-400/src/main/java/org/seng302/business/BusinessResource
 * @file   This file contains the definition for BusinessResource.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.controller;

import org.seng302.Authorization;
import org.seng302.utils.PaginationUtils;
import org.seng302.utils.SearchUtils;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalBusinessArgumentException;
import org.seng302.model.Address;
import org.seng302.Validation;
import org.seng302.view.incoming.BusinessModifyPayload;
import org.seng302.view.outgoing.AddressPayload;
import org.seng302.model.repository.AddressRepository;
import org.seng302.model.Business;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.view.incoming.UserIdPayload;
import org.seng302.model.User;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.outgoing.BusinessIdPayload;
import org.seng302.view.outgoing.BusinessPayload;
import org.seng302.view.incoming.BusinessRegistrationPayload;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for businesses. This class includes:
 * POST "/businesses" endpoint used for creating businesses.
 * GET "/businesses/{id}" endpoint used for retrieving a single business.
 * PUT "/businesses/{id}/makeAdministrator" endpoint for making a user an administrator of a business.
 * PUT "/businesses/{id}/revokeAdministrator" endpoint for removing a user as administrator of a business.
 * GET "/businesses/search" endpoint used to retrieve business accounts based on search criteria.
 */
@RestController
public class BusinessResource {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Address address;
    private List<Business> businesses;

    private static final Logger logger = LogManager.getLogger(BusinessResource.class.getName());

    public BusinessResource(
            BusinessRepository businessRepository, UserRepository userRepository, AddressRepository addressRepository
    ) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    /**
     * create a new business by info given by businessPayload
     * @param sessionToken value of cookie
     * @param businessRegistrationPayload contain new business info
     * @return business id payload
     */
    @PostMapping("/businesses")
    public ResponseEntity<BusinessIdPayload> createBusiness(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                            @RequestBody BusinessRegistrationPayload businessRegistrationPayload) {
        //access token invalid
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        String name = businessRegistrationPayload.getName();
        String description = businessRegistrationPayload.getDescription();
        BusinessType businessType = businessRegistrationPayload.getBusinessType();

        //403
        if (currentUser.getId() != businessRegistrationPayload.getPrimaryAdministratorId()){
            logger.error("User with Id: {} is not primary administrator.", currentUser.getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Invalid Primary Administrator Id"
            );        }


        if (businessType == null){
            logger.error("Invalid Business Type (is null)");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid business type"
            );
        }

        AddressPayload addressJSON = businessRegistrationPayload.getAddress();
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
            try {
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
            } catch (IllegalAddressArgumentException e) {
                logger.error("Invalid Business Address");
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid business address"
                );
            }

        }

        if (Validation.isNewBusiness(businesses, name)){
            try {
                Business business = new Business(
                        currentUser.getId(),
                        name.trim(),
                        description,
                        address,
                        businessType,
                        LocalDateTime.now(),
                        currentUser
                );
                business.addAdministrators(currentUser); //add user to administrators list
                Business createdBusiness = businessRepository.save(business);
                logger.info("Successful Business Registration - {}", createdBusiness);
                return ResponseEntity.status(HttpStatus.CREATED).body(new BusinessIdPayload(createdBusiness.getId()));
            } catch (IllegalBusinessArgumentException e) {
                logger.error("Business Registration Failure - {}", e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage()
                );
            }

        } else {
            logger.error("Name: {} and Address: {} already in use", name, address);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Name and Address already in use"
            );
        }
    }

    /**
     * get method for retrieving a specific business account.
     * @param sessionToken value of cookie
     * @param id user id
     * @return business object if it exists
     */
    @GetMapping("/businesses/{id}")
    public BusinessPayload retrieveBusiness(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                            @PathVariable String id) throws Exception {
        //access token invalid

        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<Business> optionalSelectBusiness = businessRepository.findBusinessById(Integer.valueOf(id));
        if (optionalSelectBusiness.isEmpty()){
            logger.error("The requested route does exist, but some part of the request is not acceptable");
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }
        Business selectBusiness = optionalSelectBusiness.get();

        List<User> administrators = selectBusiness.getAdministrators();
        for (User administrator : administrators){
            administrator.setBusinessesAdministeredObjects(new ArrayList<>());
        }

        address = selectBusiness.getAddress();
        AddressPayload addressPayload = new AddressPayload(
                address.getStreetNumber(),
                address.getStreetName(),
                address.getCity(),
                address.getRegion(),
                address.getCountry(),
                address.getPostcode(),
                address.getSuburb()
        );

        Integer primaryAdministratorId = null;
        if (selectBusiness.isAnAdministratorOfThisBusiness(currentUser) ||
                currentUser.getRole() == Role.DEFAULTGLOBALAPPLICATIONADMIN){
            primaryAdministratorId = selectBusiness.getPrimaryAdministratorId();
        }
        logger.info("Business Found - {}", selectBusiness);
        return new BusinessPayload(
                selectBusiness.getId(),
                administrators,
                primaryAdministratorId,
                selectBusiness.getName(),
                selectBusiness.getDescription(),
                addressPayload,
                selectBusiness.getBusinessType(),
                selectBusiness.getCreated()
                );
    }

    /**
     * check permission of make/remove business Administrator
     * @param sessionToken session token
     * @param optionalBusiness selected business
     * @param optionalUser selected user
     * @param isRemove is remove Administrator
     */
    private void checkBusinessPermission(String sessionToken,
                                         Optional<Business> optionalBusiness,
                                         Optional<User> optionalUser,
                                         boolean isRemove){
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        //406
        if (optionalBusiness.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Select business not exist"
            );
        }
        Business selectBusiness = optionalBusiness.get();

        //403
        if (currentUser.getRole() != Role.DEFAULTGLOBALAPPLICATIONADMIN &&
                !selectBusiness.isAnAdministratorOfThisBusiness(currentUser)){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Current user is not DGAA or an administrator of this business"
            );
        }

        //400
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Select user not exist"
            );
        }
        User selectUser = optionalUser.get();
        if (isRemove){
            if (!selectBusiness.isAnAdministratorOfThisBusiness(selectUser)){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Select user is not an administrator of this business"
                );
            }
            if (currentUser == selectUser){
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Administrator can not remove administrator self"
                );
            }
        } else {
            if (selectBusiness.isAnAdministratorOfThisBusiness(selectUser)){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Select user already is an administrator of this business"
                );
            }
        }
    }

    /**
     * make a non-administrator user(for selected business) become administrator
     * @param sessionToken session token
     * @param userIdPayload selected user id payload
     * @param id selected business id
     */
    @PutMapping("/businesses/{id}/makeAdministrator")
    @ResponseStatus(value = HttpStatus.OK, reason = "Individual added as an administrator successfully")
    public void makeAdministrator(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                  @RequestBody UserIdPayload userIdPayload, @PathVariable String id){

        Optional<Business> optionalBusiness = businessRepository.findBusinessById(Integer.valueOf(id));
        Optional<User> optionalUser = userRepository.findById(userIdPayload.getUserId());

        //Permission check
        checkBusinessPermission(sessionToken, optionalBusiness, optionalUser, false);

        //200
        optionalBusiness.get().addAdministrators(optionalUser.get());
        userRepository.flush();
        businessRepository.flush();
    }

    /**
     * remove a administrator user(for selected business) become non-administrator
     * @param sessionToken session token
     * @param userIdPayload selected user id payload
     * @param id selected business id
     */
    @PutMapping("/businesses/{id}/removeAdministrator")
    @ResponseStatus(value = HttpStatus.OK, reason = "Individual added as an administrator successfully")
    public void removeAdministrator(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                  @RequestBody UserIdPayload userIdPayload, @PathVariable String id){

        Optional<Business> optionalBusiness = businessRepository.findBusinessById(Integer.valueOf(id));
        Optional<User> optionalUser = userRepository.findById(userIdPayload.getUserId());

        //Permission check
        checkBusinessPermission(sessionToken, optionalBusiness, optionalUser, true);

        //200
        optionalBusiness.get().removeAdministrators(optionalUser.get());
        userRepository.flush();
        businessRepository.flush();
    }

    /**
     * Search for businesses by business name and/or business type.
     * Returns paginated and ordered results based on input query params.
     *
     * @param sessionToken Session token used to authenticate user (is user logged in?).
     * @param searchQuery Search query (the business name to search by).
     * @param businessType Business type to search by.
     * @param orderBy Column to order the results by.
     * @param page Page number to return results from.
     * @return A list of BusinessPayload objects matching the search query
     *
     * Preconditions:  sessionToken is of a valid user.
     *                 page can be parsed as a positive integer (i.e. it is a string representation of a positive integer)
     *                 orderBy is of one of the supported types ("nameASC", "nameDESC", "addressASC" or "addressDESC").
     * Postconditions: A response entity containing a list of businesses matching the search criteria and response code
     *                 are returned.
     */
    @GetMapping("/businesses/search")
    public ResponseEntity<List<BusinessPayload>> searchBusinesses(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @RequestParam(defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "") String businessType,
            @RequestParam(defaultValue = "nameASC") String orderBy,
            @RequestParam(defaultValue = "0") String page
    ) throws Exception {
        logger.debug("Business search request received with search query {}, business type {}, order by {}, page {}",
                searchQuery, businessType, orderBy, page);

        Authorization.getUserVerifySession(sessionToken, userRepository);

        int pageNo = PaginationUtils.parsePageNumber(page);

        // Front-end displays 5 businesses per page
        int pageSize = 5;

        Sort sortBy;
        // IgnoreCase is important to let lower case letters be the same as upper case in ordering.
        // Normally all upper case letters come before any lower case ones.
        switch (orderBy) {
            case "nameASC":
                sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
                break;
            case "nameDESC":
                sortBy = Sort.by(Sort.Order.desc("name").ignoreCase());
                break;
            case "addressASC":
                sortBy = Sort.by(Sort.Order.asc("address.city").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("address.region").ignoreCase()))
                        .and(Sort.by(Sort.Order.asc("address.country").ignoreCase()));
                break;
            case "addressDESC":
                sortBy = Sort.by(Sort.Order.desc("address.city").ignoreCase())
                        .and(Sort.by(Sort.Order.desc("address.region").ignoreCase()))
                        .and(Sort.by(Sort.Order.desc("address.country").ignoreCase()));
                break;
            case "businessTypeASC":
                sortBy = Sort.by(Sort.Order.asc("businessType").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("name").ignoreCase()));
                break;
            case "businessTypeDESC":
                sortBy = Sort.by(Sort.Order.desc("businessType").ignoreCase())
                        .and(Sort.by(Sort.Order.asc("name").ignoreCase()));
                break;
            default:
                logger.error("400 [BAD REQUEST] - {} is not a valid order by parameter", orderBy);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "OrderBy Field invalid"
                );
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);
        Page<Business> pagedResult = parseAndExecuteQuery(searchQuery, businessType, paging);

        int totalPages = pagedResult.getTotalPages();
        int totalRows = (int) pagedResult.getTotalElements();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Total-Pages", String.valueOf(totalPages));
        responseHeaders.add("Total-Rows", String.valueOf(totalRows));

        logger.info("Search Success - 200 [OK] -  Businesses retrieved for search query {}, business type {}, order by {}, page {}",
                searchQuery, businessType, orderBy, pageNo);

        logger.debug("Businesses Found: {}", pagedResult.toList());
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(BusinessPayload.toBusinessPayload(pagedResult.getContent()));
    }

    /**
     * This method parses the search criteria and then calls the needed methods to execute the "query".
     *
     * @param searchQuery criteria to search for businesses (business name).
     * @param businessType criteria to search for businesses using business type.
     * @param paging information used to paginate the retrieved businesses.
     * @return Page<Business> A page of businesses matching the search criteria.
     *
     * Preconditions:  A non-null string representing a name to be searched for (can be empty string)
     *                 A non-null string representing a business type to be searched for (can be empty string)
     * Postconditions: A page containing the results of the business search is returned.
     */
    private Page<Business> parseAndExecuteQuery(String searchQuery, String businessType, Pageable paging) {
        BusinessType convertedBusinessType = toBusinessType(businessType);
        if (searchQuery.equals("") && businessType.equals("")) return businessRepository.findAll(paging);
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        if (businessType.equals("")) return businessRepository.findAllBusinessesByNames(names, paging);
        if (searchQuery.equals("")) return businessRepository.findBusinessesByBusinessType(convertedBusinessType, paging);
        return businessRepository.findAllBusinessesByNamesAndType(names, convertedBusinessType, paging);
    }

    /**
     * Converts a string representation of business type to a enum representation (BusinessType). If the string does
     * not represent a valid business type then null is returned.
     * @param type A string representing business type.
     * @return An enum representation of business type (null if string representation is not valid).
     *
     * Preconditions:  A string representation of a valid business type.
     * Postconditions: An enum representation of business type.
     */
    private BusinessType toBusinessType(String type){
        BusinessType businessType = null;
        if (type.equalsIgnoreCase("ACCOMMODATION_AND_FOOD_SERVICES")){
            businessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;
        } else if (type.equalsIgnoreCase("RETAIL_TRADE")){
            businessType = BusinessType.RETAIL_TRADE;
        } else if (type.equalsIgnoreCase("CHARITABLE_ORGANISATION")){
            businessType = BusinessType.CHARITABLE_ORGANISATION;
        } else if (type.equalsIgnoreCase("NON_PROFIT_ORGANISATION")){
            businessType = BusinessType.NON_PROFIT_ORGANISATION;
        }
        return businessType;
    }

    /**
     * Modification of bussiness endpoint. Requires the user to have permission; business to exist and all the payload's
     * content to be still valid with the requirements. Then it will update the business.
     *
     * @param session User session token.
     * @param id Id of the business the user wants to update.
     * @param businessModifyPayload The changes made to the business.
     */
    @PutMapping("/businesses/{id}/profile")
    @ResponseStatus(value = HttpStatus.OK, reason = "Business updated successfully")
    public void modifyBusiness(@CookieValue(value = "JSESSIONID", required = false) String session,
                               @PathVariable Integer id, @RequestBody BusinessModifyPayload businessModifyPayload) {

        // Verify session token. fail --> 401 UAUTHORIZED
        User user = Authorization.getUserVerifySession(session, userRepository);

        // verify business exists. fail --> 406 NOT_ACCEPTABLE
        Optional<Business> business = businessRepository.findBusinessById(id);
        if (business.isEmpty()) {
            String errorMessage = String.format("Business id requests to be modified %d does not exist.", id);
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Selected Business does not exist");
        }

        // Verify user permissions. fail --> 403 FORBIDDEN
        if (!business.get().isAnAdministratorOfThisBusiness(user) && !Authorization.isGAAorDGAA(user)) {
            String errorMessage = String.format("User (id: %d) attempted to modify business (id: %d). But " +
                    "lacked permissions.", user.getId(), id);

            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Returned when a user tries to " +
                    "update the business info for a business they do not administer AND the user is not a global application admin");
        }

        // Verify payload content is still valid to the requirements of a business. fail --> 400 BAD REQUEST
        Business updatedBusiness = updatePrimaryAdminstrator(user, business.get(), businessModifyPayload);
        updateBusinessName(updatedBusiness, user, businessModifyPayload);
        updateBusinessDescription(updatedBusiness, user, businessModifyPayload.getDescription());
        updateBusinessAddress(updatedBusiness, user, businessModifyPayload.getAddress());
        updateBusinessType(updatedBusiness, user, businessModifyPayload.getBusinessType());

        // save and flush. fail --> 500 SERVER ERROR
        businessRepository.saveAndFlush(updatedBusiness);
    }

    /**
     * Update the business type of a given business.
     *
     * @param business The business to be updated.
     * @param user The user requesting the update.
     * @param businessType The new business type.
     * @throws ResponseStatusException Thrown when the new business type does not exist or invalid.
     */
    private void updateBusinessType(Business business, User user, BusinessType businessType) throws ResponseStatusException{
        if (businessType == null) {
            String errorMessage = String.format("User (id: %d) attempted to update business type for business (id: %d). But the type was invalid.", user.getId(), business.getId());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There was some error with the data supplied by the user, appropriate error message(s) should be shown to user");
        }
        String debugMessage = String.format("User (id: %d) updated business type for business (id: %d). %s --> %s.", user.getId(), business.getId(), business.getBusinessType().toString(), businessType);
        logger.debug(debugMessage);
        business.setBusinessType(businessType);
    }

    // TODO UPDATE the CURRENCY of the business. To suit the new address.
    /**
     * Updates the business address.
     *
     * @param business The business to be updated.
     * @param user The user requesting the update.
     * @param addressJSON The new address payload.
     * @throws ResponseStatusException Is thrown when the new address does not meet the requirements.
     */
    private void updateBusinessAddress(Business business, User user, AddressPayload addressJSON) throws ResponseStatusException{
        Optional<Address> existingAddress = addressRepository.findAddressByStreetNumberAndStreetNameAndCityAndRegionAndCountryAndPostcodeAndSuburb(addressJSON.getStreetNumber(),
                addressJSON.getStreetName(), addressJSON.getCity(), addressJSON.getRegion(), addressJSON.getCountry(), addressJSON.getPostcode(), addressJSON.getSuburb());

        // If the address is new, we need to first add it to the database.
        if (existingAddress.isEmpty()){
            try {
                Address newAddress = new Address(addressJSON.getStreetNumber(),
                        addressJSON.getStreetName(), addressJSON.getCity(), addressJSON.getRegion(), addressJSON.getCountry(), addressJSON.getPostcode(), addressJSON.getSuburb());
                addressRepository.save(newAddress);
                business.setAddress(newAddress);
            } catch (IllegalAddressArgumentException err) {
                String errorMessage = String.format("User (id: %d) attempted to update address for business (id: %d). But the address was invalid", user.getId(), business.getId());
                logger.error(errorMessage);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There was some error with the data supplied by the user, appropriate error message(s) should be shown to user");
            }
        } else {
            business.setAddress(existingAddress.get());
        }
        String debugMessage = String.format("User (id: %d) updated address for business (id: %d). %s --> %s.", user.getId(), business.getId(), business.getAddress().toString(), addressJSON);
        logger.debug(debugMessage);
    }

    /**
     * Updates the business description if provided.
     *
     * @param business The business that is to be updated.
     * @param user the requesting user
     * @param description The new description provided.
     */
    private void updateBusinessDescription(Business business, User user, String description) {
        if (description != null) {
            if (!business.isValidDescription(description)) {
                String errorMessage = String.format("User (id: %d) attempted to update description for business (id: %d). But the description was invalid", user.getId(), business.getId());
                logger.error(errorMessage);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There was some error with the data supplied by the user, appropriate error message(s) should be shown to user");
            }

            String debugMessage = String.format("User (id: %d) updated description for business (id: %d). %s --> %s.", user.getId(), business.getId(), business.getDescription(), description);
            logger.debug(debugMessage);
            business.setDescription(description);
        }
    }

    /**
     * Updates the business name of a given business.
     *
     * @param business The business to be updated.
     * @param user The user who requested the update.
     * @param payload The whole update payload.
     * @throws ResponseStatusException Thrown when the name placed if invalid or null.
     */
    private void updateBusinessName(Business business, User user, BusinessModifyPayload payload) throws ResponseStatusException {
        try {
            // Check that it is present
            if (payload.getName() == null) {
                throw new IllegalBusinessArgumentException("Cannot be null");
            }

            // Check that it is valid
            if (!business.isValidName(payload.getName())) {
                throw new IllegalBusinessArgumentException("invalid name provided.");
            }

            // Perform the modification
            business.setName(payload.getName());
            String debugMessage = String.format("User (id: %d) modfied business (id: %d) name. %s --> %s.",
                    user.getId(), business.getId(), business.getName(), payload.getName());
            logger.debug(debugMessage);

        } catch (IllegalBusinessArgumentException err) {
            // If an error is thrown the data was invalid.
            String errorMessage = String.format("User (id: %d) attempted to modify business (id: %d)'s name." +
                    " But it was invalid.", user.getId(), business.getId());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There was some error with the data " +
                    "supplied by the user, appropriate error message(s) should be shown to user");
        }
    }

    /**
     * Attempts to update the business primary adminstrator if there is a new value.
     *
     * @param user The user that requested update.
     * @param business The business that the update is performed on.
     * @param payload The payload of the update.
     * @throws ResponseStatusException Thrown when the user is not GAA nor DGAA and is not the primary admin and is trying to change
     * who the primary admin is.
     *
     * @return {Business} The newly updated business object.
     */
    private Business updatePrimaryAdminstrator(User user, Business business, BusinessModifyPayload payload)
            throws ResponseStatusException {
        boolean userIsPrimaryAdmin = business.getPrimaryAdministratorId().equals(user.getId());

        if (userIsPrimaryAdmin || Authorization.isGAAorDGAA(user)) {
            if (payload.getPrimaryAdministratorId() != null) {
                Optional<User> newPrimaryAdmin = userRepository.findById(payload.getPrimaryAdministratorId());

                // Ensure the new id is valid
                if (newPrimaryAdmin.isEmpty()) {
                    String errorMessage = String.format("User (id: %d) attempted to modify business (id: %d) primary adminsitrator. " +
                            "But lacked permissions.", user.getId(), business.getId());
                    logger.error(errorMessage);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Returned when a user tries to " +
                            "update the business info for a business they do not administer AND the user is not a global application admin");
                }

                // If the new primary admin is not part of the business then add him in.
                if (!business.isAnAdministratorOfThisBusiness(newPrimaryAdmin.get())) {
                    business.addAdministrators(newPrimaryAdmin.get());
                }


                String debugMessage = String.format("Updated business has a new primary adminstrator %d --> %d",
                        payload.getPrimaryAdministratorId(), business.getPrimaryAdministratorId());
                logger.debug(debugMessage);
                business.setPrimaryAdministratorId(payload.getPrimaryAdministratorId());
            }
        } else {
            // Only the primary admin can change the primary admin (or the GAA and DGAA can as well).
            String errorMessage = String.format("User (id: %d) attempted to modify business (id: %d) primary adminsitrator. " +
                    "But lacked permissions.", user.getId(), business.getId());
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Returned when a user tries to " +
                    "update the business info for a business they do not administer AND the user is not a global application admin");
        }

        return business;
    }

}
