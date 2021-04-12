package org.seng302.business;

import org.seng302.address.Address;
import org.seng302.address.AddressPayload;
import org.seng302.address.AddressRepository;
import org.seng302.validation.BusinessValidation;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Verifies the session token, throws an error if it does not exist, and if it does, returns the User object.
     * @param sessionToken Session token
     * @return User object
     */
    private User getUserVerifySession(String sessionToken) {
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
     * create a new business by info given by businessPayload
     * @param sessionToken value of cookie
     * @param businessRegistrationPayload contain new business info
     * @throws Exception Access token is missing or invalid
     */
    @PostMapping("/businesses")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Business account created successfully")
    public void createBusiness(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                               @RequestBody BusinessRegistrationPayload businessRegistrationPayload) throws Exception {
        //access token invalid
        User currentUser = getUserVerifySession(sessionToken);

        String name = businessRegistrationPayload.getName();
        String description = businessRegistrationPayload.getDescription();
        BusinessType businessType = businessRegistrationPayload.getBusinessType();

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

        if (BusinessValidation.isNewBusiness(businesses, name)){
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
            businessRepository.saveAndFlush(business);
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
        getUserVerifySession(sessionToken);

        Optional<Business> business = businessRepository.findBusinessById(Integer.valueOf(id));

        if (business.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }

        List<User> administrators = business.get().getAdministrators();
        for (User administrator : administrators){
            administrator.setBusinessesAdministeredObjects(new ArrayList<>());
        }

        address = business.get().getAddress();
        AddressPayload addressPayload = new AddressPayload(
                address.getStreetNumber(),
                address.getStreetName(),
                address.getCity(),
                address.getRegion(),
                address.getCountry(),
                address.getPostcode()
        );
        return new BusinessPayload(
                business.get().getId(),
                administrators,
                business.get().getPrimaryAdministratorId(),
                business.get().getName(),
                business.get().getDescription(),
                addressPayload,
                business.get().getBusinessType(),
                business.get().getCreated()
                );
    }
}
