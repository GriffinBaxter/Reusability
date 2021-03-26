package org.seng302.user;

import org.seng302.Address.Address;

import java.time.LocalDate;

public class UserRegistrationPayload {
    private String firstName;
    private String lastName;
    private String middleName;
    private String nickname;
    private String bio;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String streetNumber;
    private String streetName;
    private String city;
    private String region;
    private String country;
    private String postcode;
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

    public Address getHomeAddress() {
        return new Address(streetNumber, streetName, city, region, country, postcode);
    }

    public String getPassword() {
        return password;
    }
}
