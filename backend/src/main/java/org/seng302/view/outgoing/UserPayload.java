/**
 * Summary. This file contains the definition for the UserPayload.
 *
 * Description. This file contains the defintion for the UserPayload.
 *
 * @link   team-400/src/main/java/org/seng302/user/UserPayload
 * @file   This file contains the definition for UserPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

import org.seng302.model.Business;
import org.seng302.model.enums.Role;
import org.seng302.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class creates a user payload and the address is provided.
 */
public class UserPayload extends UserPayloadParent {


    private String dateOfBirth;
    private String phoneNumber;
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
                created,
                role,
                businessesAdministeredObject);

        this.homeAddress = homeAddress;
        this.dateOfBirth = dateOfBirth.toString();
        this.phoneNumber = phoneNumber;
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

    /**
     * Converts a list of users to a list of userPayloads with empty businesses (to stop infinite recursion)
     * @param userList The given list of users
     * @return A list of userPayloads.
     */
    public static List<UserPayload> convertToPayloadWithoutBusiness(List<User> userList) throws Exception {
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
                    Collections.emptyList());

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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + super.getId() + "," +
                "\"firstName\":\"" + super.getFirstName() + "\"," +
                "\"lastName\":\"" + super.getLastName() + "\"," +
                "\"middleName\":\"" + super.getMiddleName() + "\"," +
                "\"nickname\":\"" + super.getNickname() + "\"," +
                "\"bio\":\"" + super.getBio() + "\"," +
                "\"email\":\"" + super.getEmail() + "\"," +
                "\"created\":\"" + super.getCreated() + "\"," +
                "\"role\":\"" + super.getRole() + "\"," +
                "\"businessesAdministered\":[null]," +
                "\"dateOfBirth\":\"" + dateOfBirth + "\"," +
                "\"phoneNumber\":\"" + phoneNumber + "\"," +
                "\"homeAddress\":" + homeAddress + "}";
    }
}
