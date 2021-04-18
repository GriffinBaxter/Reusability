package org.seng302.user;

import org.seng302.address.Address;
import org.seng302.address.AddressPayload;
import org.seng302.business.Business;
import org.seng302.business.BusinessPayload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Payload for the User (notably excluding the password field, for JSON responses).
 */
public class UserPayload {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String nickname;
    private String bio;
    private String email;
    private String dateOfBirth;
    private String phoneNumber;
    private AddressPayload homeAddress;
    private String created;
    private Role role;
    private List<BusinessPayload> businessesAdministered;

    /**
     * translate a list of User to a list of UserPayload
     * @param users a list of User
     * @return a list of UserPayload
     */
    public static List<UserPayload> toUserPayload (List<User> users) throws Exception {
        List<UserPayload> userPayloads = new ArrayList<>();
        UserPayload userPayload;
        for (User user: users){
            Address address = user.getHomeAddress();
            AddressPayload addressPayload = new AddressPayload(
                    address.getStreetNumber(),
                    address.getStreetName(),
                    address.getCity(),
                    address.getRegion(),
                    address.getCountry(),
                    address.getPostcode()
            );
            userPayload = new UserPayload(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getMiddleName(),
                    user.getNickname(),
                    user.getBio(),
                    user.getEmail(),
                    user.getDateOfBirth(),
                    user.getPhoneNumber(),
                    addressPayload,
                    user.getCreated(),
                    user.getRole(),
                    user.getBusinessesAdministeredObjects());
            userPayloads.add(userPayload);
        }
        return userPayloads;
    }

    public UserPayload(
            int id,
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
            List<Business> businessesAdministeredObject
    ) throws Exception {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.nickname = nickname;
        this.bio = bio;
        this.email = email;
        this.dateOfBirth = dateOfBirth.toString();
        this.phoneNumber = phoneNumber;
        this.homeAddress = homeAddress;
        this.created = created.toString();
        this.role = role;
        this.businessesAdministered = BusinessPayload.toBusinessPayload(businessesAdministeredObject);
//      TODO This might get changed in the future due to the recursive nature of the API seems wrong.
        if (this.businessesAdministered.isEmpty()){
            this.businessesAdministered.add(null);
        }
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AddressPayload getHomeAddress() {
        return homeAddress;
    }

    public String getCreated() {
        return created;
    }

    public Role getRole() {
        return role;
    }

    public List<BusinessPayload> getBusinessesAdministered() {
        return businessesAdministered;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHomeAddress(AddressPayload homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setBusinessesAdministered(List<BusinessPayload> businessesAdministered) {
        this.businessesAdministered = businessesAdministered;
    }
}
