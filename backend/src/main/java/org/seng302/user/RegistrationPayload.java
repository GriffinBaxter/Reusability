package org.seng302.user;

import java.time.LocalDate;

public class RegistrationPayload {
    private String firstName;
    private String lastName;
    private String middleName;
    private String nickname;
    private String bio;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String homeAddress;
    private String password;

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

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getPassword() {
        return password;
    }
}
