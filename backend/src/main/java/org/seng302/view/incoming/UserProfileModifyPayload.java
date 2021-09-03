package org.seng302.view.incoming;

import org.seng302.view.outgoing.AddressPayload;

import java.time.LocalDate;

public class UserProfileModifyPayload {
    private String firstName;
    private String lastName;
    private String middleName;
    private String nickname;
    private String bio;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private AddressPayload homeAddress;
    private String currentPassword;
    private String newPassword;

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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AddressPayload getHomeAddress() {
        return homeAddress;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public String toString() {
        return "User(" +
                "firstName: " + firstName +
                "lastName: " + lastName +
                "middleName: " + middleName +
                "nickname: " + nickname +
                "bio: " + bio +
                "email: " + email +
                "dateOfBirth: " + dateOfBirth +
                "phoneNumber: " + phoneNumber +
                "homeAddress=" + homeAddress +
                "currentPassword: " + currentPassword +
                "newPassword: " + newPassword +
                ')';
    }
}
