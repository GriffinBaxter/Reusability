package org.seng302.business;

import org.seng302.main.Authorization;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class BusinessResource {

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * create a new business by info given by businessPayload
     * @param sessionToken value of cookie
     * @param businessPayload contain new business info
     * @throws Exception Access token is missing or invalid
     */
    @PostMapping("/businesses")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Business account created successfully")
    public void createBusiness(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                               @RequestBody BusinessPayload businessPayload) throws Exception {
        //access token invalid
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Business business = new Business(
                businessPayload.getName(),
                businessPayload.getDescription(),
                businessPayload.getAddress(),
                businessPayload.getBusinessType(),
                LocalDateTime.now()
                );
        business.addAdministrators(currentUser); //add user to administrators list
        businessRepository.saveAndFlush(business);
    }

    /**
     * get method for retrieving a specific business account.
     * @param sessionToken value of cookie
     * @param id user id
     * @return business object if it exists
     */
    @GetMapping("/businesses/{id}")
    public Business retrieveBusiness(@CookieValue(value = "JSESSIONID", required = false) String sessionToken,
                                     @PathVariable String id){
        //access token invalid
        Authorization.getUserVerifySession(sessionToken, userRepository);

        Optional<Business> business = businessRepository.findBusinessById(Integer.valueOf(id));

        if (business.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The requested route does exist (so not a 404) but some part of the request is not acceptable, " +
                            "for example trying to access a resource by an ID that does not exist."
            );
        }
        return business.get();
    }
}
