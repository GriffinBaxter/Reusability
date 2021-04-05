package org.seng302.business;

import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
     * Verifies the cookie(its self and the value its contain), throws an error if it does not exist, and if it does,
     * returns the User object.
     * @param request http servlet request
     * @return current select user object
     */
    private User getUserVerifySession(HttpServletRequest request) {
        String sessionToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null){ //Cookie not exist
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Access token is missing or invalid");
        }
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("JSESSIONID")){
                sessionToken = cookie.getValue();
            }
        }
        if (sessionToken == null){ //Cookie value not exist
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Access token is missing or invalid");
        }

        Optional<User> user = userRepository.findById(Integer.valueOf(sessionToken));
        if (sessionToken == null || user.isEmpty()) { //Cookie not contain an user
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Access token is missing or invalid"
            );
        } else {
            return user.get();
        }
    }

    /**
     * create a new business by info given by businessRegistrationPayload
     * @param businessRegistrationPayload contain new business info
     * @param response http servlet response
     * @throws Exception Access token is missing or invalid
     */
    @PostMapping("/businesses")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Business account created successfully")
    public void createBusiness(@RequestBody BusinessRegistrationPayload businessRegistrationPayload,
                               HttpServletRequest response) throws Exception {
        //access token invalid
        User currentUser = getUserVerifySession(response);

        Business business = new Business(
                businessRegistrationPayload.getName(),
                businessRegistrationPayload.getDescription(),
                businessRegistrationPayload.getAddress(),
                businessRegistrationPayload.getBusinessType(),
                LocalDateTime.now(),
                currentUser,
                currentUser.getId()
                );
        business.addAdministrators(currentUser); //add user to administrators list
        businessRepository.saveAndFlush(business);
    }

    /**
     * get method for retrieving a specific business account.
     * @param id user id
     * @param response http servlet request
     * @return business object if it exists
     */
    @GetMapping("/businesses/{id}")
    public BusinessPayload retrieveBusiness(@PathVariable String id, HttpServletRequest response){
        //access token invalid
        getUserVerifySession(response);

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
