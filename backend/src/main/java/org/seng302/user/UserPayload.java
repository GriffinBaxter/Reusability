package org.seng302.user;

import org.seng302.Address.Address;

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
    private Address homeAddress;
    private String created;
    private Role role;

    /**
     * translate a list of User to a list of UserPayload
     * @param users a list of User
     * @return a list of UserPayload
     */
    public static List<UserPayload> toUserPayload (List<User> users){
        List<UserPayload> userPayloads = new ArrayList<>();
        UserPayload userPayload;
        for (User user: users){
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
                    user.getHomeAddress(),
                    user.getCreated(),
                    user.getRole()
            );
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
            Address homeAddress,
            LocalDateTime created,
            Role role
    ) {
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

    public Address getHomeAddress() {
        return homeAddress;
    }

    public String getCreated() {
        return created;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
