package org.seng302.user;

import org.seng302.address.AddressPayloadSecure;
import org.seng302.business.Business;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                             LocalDate dateOfBirth,
                             String phoneNumber,
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
                dateOfBirth,
                phoneNumber,
                created,
                role,
                businessesAdministeredObject);

        this.homeAddress = homeAddress;
    }

    public AddressPayloadSecure getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(AddressPayloadSecure homeAddress) {
        this.homeAddress = homeAddress;
    }
}
