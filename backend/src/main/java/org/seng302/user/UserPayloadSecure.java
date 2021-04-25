package org.seng302.user;

import org.seng302.address.AddressPayloadSecure;
import org.seng302.business.Business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a user payload but the address does not contain street number, street address, post code.
 * I.e. for viewing a profile other than your own - not exposing sensitive/detailed data.
 */
public class UserPayloadSecure extends UserPayloadParent {

    private AddressPayloadSecure homeAddress;

    public UserPayloadSecure(int id,
                             String firstName,
                             String lastName,
                             String middleName,
                             String nickname,
                             String bio,
                             String email,
                             AddressPayloadSecure homeAddress,
                             LocalDateTime created,
                             Role role,
                             List<Business> businessesAdministeredObject) throws Exception {
        super(id,
                firstName,
                lastName,
                middleName,
                nickname,
                bio,
                email,
                created,
                role,
                businessesAdministeredObject);

        this.homeAddress = homeAddress;
    }

    /**
     * Converts a list of users to a list of userPayloadsSecure.
     * @param userList The given list of users
     * @return A list of userPayloadsSecure.
     */
    public static List<UserPayloadSecure> convertToPayloadSecure(List<User> userList) throws Exception {
        List<UserPayloadSecure> payLoadsSecure = new ArrayList<>();
        for (User user : userList) {

            UserPayloadSecure newPayload = new UserPayloadSecure(user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getMiddleName(),
                    user.getNickname(),
                    user.getBio(),
                    user.getEmail(),
                    user.getHomeAddress().toAddressPayloadSecure(),
                    user.getCreated(),
                    user.getRole(),
                    user.getBusinessesAdministeredObjects());

            payLoadsSecure.add(newPayload);
        }
        return payLoadsSecure;
    }

    public AddressPayloadSecure getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(AddressPayloadSecure homeAddress) {
        this.homeAddress = homeAddress;
    }
}
