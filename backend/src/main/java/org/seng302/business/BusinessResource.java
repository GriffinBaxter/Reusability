package org.seng302.business;

import org.seng302.address.Address;
import org.seng302.validation.Validation;
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
        Address address = businessRegistrationPayload.getAddress();

        //TODO: 400 not in api spec
        System.out.println(name);
        if (!Validation.isBusinessName(name)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal business name"
            );
        }

        if (!Validation.isDescription(description)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal description"
            );
        }
        if (!Validation.isAddress(address)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal address"
            );
        }
        if (businessType == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Illegal business type"
            );
        }

        if (Validation.isNewBusiness(businessRepository.findBusinessesByAddress(address.toString()), name, address)){
            Business business = new Business(
                    name,
                    description,
                    address,
                    businessType,
                    LocalDateTime.now(),
                    currentUser,
                    currentUser.getId()
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
                                            @PathVariable String id){
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

        return new BusinessPayload(
                business.get().getId(),
                administrators,
                business.get().getPrimaryAdministratorId(),
                business.get().getName(),
                business.get().getDescription(),
                business.get().getAddress(),
                business.get().getBusinessType(),
                business.get().getCreated()
                );
    }
}
