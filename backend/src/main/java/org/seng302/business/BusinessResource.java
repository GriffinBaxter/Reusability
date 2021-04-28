package org.seng302.business;

import org.seng302.main.Authorization;
import org.seng302.address.Address;
import org.seng302.address.AddressPayload;
import org.seng302.address.AddressRepository;
import org.seng302.user.Role;
import org.seng302.user.UserIdPayload;
import org.seng302.validation.BusinessValidation;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.seng302.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * @throws Exception Access token is missing or invalid
     */
    @PostMapping("/businesses")
    public ResponseEntity<BusinessIdPayload> createBusiness(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                                        @RequestBody BusinessRegistrationPayload businessRegistrationPayload) throws Exception {
        //access token invalid
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        String name = businessRegistrationPayload.getName();
        String description = businessRegistrationPayload.getDescription();
        BusinessType businessType = businessRegistrationPayload.getBusinessType();

        //403
        if (currentUser.getId() != businessRegistrationPayload.getPrimaryAdministratorId()){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Invalid Primary Administrator Id"
            );        }


        if (!BusinessValidation.isValidName(name.trim())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid business name"
            );
        }

        if (!BusinessValidation.isValidDescription(description)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid description"
            );
        }

        if (businessType == null){
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
            try {
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
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid business address"
                );
            }

        }

        if (BusinessValidation.isNewBusiness(businesses, name)){
            try {
                Business business = new Business(
                        currentUser.getId(),
                        name,
                        description,
                        address,
                        businessType,
                        LocalDateTime.now(),
                        currentUser
                );
                business.addAdministrators(currentUser); //add user to administrators list
                Business createdBusiness = businessRepository.saveAndFlush(business);
                System.out.println(createdBusiness);
                System.out.println(createdBusiness.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(new BusinessIdPayload(createdBusiness.getId()));
            } catch (Exception e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid business"
                );
            }

        } else { //TODO: 409 not in api spec
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
                address.getPostcode()
        );

        Integer primaryAdministratorId = null;
        if (selectBusiness.isAnAdministratorOfThisBusiness(currentUser) ||
                currentUser.getRole() == Role.DEFAULTGLOBALAPPLICATIONADMIN){
            primaryAdministratorId = selectBusiness.getPrimaryAdministratorId();
        }
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
        System.out.println(optionalBusiness.get().getAdministrators());
        System.out.println(optionalUser.get());
        optionalBusiness.get().removeAdministrators(optionalUser.get());
        userRepository.flush();
        businessRepository.flush();
    }
}