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
     * @param sessionToken The current session token to verify
     * @param useSessionToken Whether a session token is being passed in or not. If true, the role of the user will be verified.
     * @return A list of userPayloads.
     */
    public List<UserPayload> convertToPayload(List<User> userList, String sessionToken, Boolean useSessionToken) throws Exception {
        List<UserPayload> payLoads = new ArrayList<>();
        for (User user : userList) {

            Role role = null;
            if (useSessionToken) {
                if (UserResource.verifyRole(sessionToken, Role.DEFAULTGLOBALAPPLICATIONADMIN)) {
                    role = user.getRole();
                }
            } else {
                role = user.getRole();
            }

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
                    role,
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