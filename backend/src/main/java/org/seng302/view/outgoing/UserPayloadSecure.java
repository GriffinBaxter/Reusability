/**
 * Summary. This file contains the definition for the UserPayloadSecure.
 *
 * Description. This file contains the defintion for the UserPayloadSecure.
 *
 * @link   team-400/src/main/java/org/seng302/user/UserPayloadSecure
 * @file   This file contains the definition for UserPayloadSecure.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

import org.seng302.model.Business;
import org.seng302.model.UserImage;
import org.seng302.model.enums.Role;
import org.seng302.model.User;

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
                             List<Business> businessesAdministeredObject,
                             List<UserImage> userImages) throws Exception {
        super(id,
                firstName,
                lastName,
                middleName,
                nickname,
                bio,
                email,
                created,
                role,
                businessesAdministeredObject,
                userImages);

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
                    user.getBusinessesAdministeredObjects(),
                    user.getUserImages()
                    );

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

    /**
     * Override the toString method for debugging and testing purposes.
     * @return a string representing the secure user payload.
     */
    @Override
    public String toString() {
        return "{\"id\":" + super.getId() +
                ",\"firstName\":\"" + super.getFirstName() + "\"" +
                ",\"lastName\":\"" + super.getLastName() + "\"" +
                ",\"middleName\":\"" + super.getMiddleName() + "\"" +
                ",\"nickname\":\"" + super.getNickname() + "\"" +
                ",\"bio\":\"" + super.getBio() + "\"" +
                ",\"email\":\"" + super.getEmail() + "\"" +
                ",\"created\":\"" + super.getCreated() + "\"" +
                ",\"role\":\"" + super.getRole() + "\"" +
                ",\"businessesAdministered\":" + super.getBusinessesAdministered().toString() +
                ",\"images\":" + super.getImages() +
                ",\"homeAddress\":" + homeAddress +
                "}";
    }
}
