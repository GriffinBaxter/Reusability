package org.seng302.user;

import org.seng302.address.AddressPayload;
import org.seng302.business.Business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a user payload and the address is provided.
 */
public class UserPayload extends UserPayloadParent {

    private AddressPayload homeAddress;

    public UserPayload(int id,
                       String firstName,
                       String lastName,
                       String middleName,
                       String nickname,
                       String bio,
                       String email,
                       LocalDate dateOfBirth,
                       String phoneNumber,
                       AddressPayload homeAddress,
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

    /**
     * Converts a list of users to a list of userPayloads.
     * @param userList The given list of users
     * @return A list of userPayloads.
     */
    public static List<UserPayload> convertToPayload(List<User> userList) throws Exception {
        List<UserPayload> payLoads = new ArrayList<>();
        for (User user : userList) {

            UserPayload newPayload = new UserPayload(user.getId(),
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
                    user.getRole(),
                    user.getBusinessesAdministeredObjects());

            payLoads.add(newPayload);
        }
        return payLoads;
    }

    public AddressPayload getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(AddressPayload homeAddress) {
        this.homeAddress = homeAddress;
    }
}