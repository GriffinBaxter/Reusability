package org.seng302.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String homeAddress;
    private String created;
    private Role role;

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
            String homeAddress,
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

    public String getHomeAddress() {
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
