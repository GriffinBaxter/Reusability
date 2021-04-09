package org.seng302.user;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.seng302.Address.Address;
import org.seng302.business.Business;
import org.seng302.Address.Validation;

/**
 * Class for individual accounts.
 */
@Embeddable
@Data // generate setters and getters for all fields (lombok pre-processor)
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class User {
    @Id // this field (attribute) is the table primary key
    @GeneratedValue // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "bio")
    private String bio;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "home_address", nullable = false)
    private String homeAddress;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonManagedReference
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_businesses",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "businesses_id") })
    private List<Business> businessesAdministeredObjects = new ArrayList<>();

    @Column(name = "session_uuid")
    private String sessionUUID;


    /**
     * User account constructor.
     * @param firstName First Name
     * @param lastName Last name
     * @param middleName Middle name (optional)
     * @param nickname Nickname (optional)
     * @param bio Bio (optional)
     * @param email Email address
     * @param dateOfBirth Date of birth
     * @param phoneNumber Phone number
     * @param homeAddress Home address
     * @param password User's password (not stored in plaintext
     * @param created Date individual signed up
     * @param role Role determines admin privileges.
     * @throws Exception Validation exception
     */
    public User(
            String firstName,
            String lastName,
            String middleName,
            String nickname,
            String bio,
            String email,
            LocalDate dateOfBirth,
            String phoneNumber,
            Address homeAddress,
            String password,
            LocalDateTime created,
            Role role
    ) throws Exception {
        if (!Validation.isEmail(email, false)) {
            throw new Exception("Invalid email address");
        }
        if (!Validation.isPassword(password)){
            throw new Exception("Invalid password");
        }
        if (!Validation.isAlpha(firstName, false)){
            throw new Exception("Invalid first name");
        }
        if (!Validation.isAlpha(lastName, false)){
            throw new Exception("Invalid last name");
        }
        if (!Validation.isAlphanumeric(nickname, true)) {
            throw new Exception("Invalid nickname");
        }
        if (!Validation.isPhoneNumber(phoneNumber, false)) {
            throw new Exception("Invalid phone number");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = (middleName.equals("")) ? null : middleName;
        this.nickname = (nickname.equals("")) ? null : nickname;
        this.bio = (bio.equals("")) ? null : bio;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = (phoneNumber.equals("")) ? null : phoneNumber;
        this.homeAddress = homeAddress.toString();
        this.password = encode(password);
        this.created = created;
        this.role = (role.toString().equals("")) ? Role.USER : role;
        this.sessionUUID = generateSessionUUID();
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getHomeAddress() {
        return Address.toAddress(homeAddress);
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Role getRole() {
        return role;
    }

    public String getSessionUUID() {
        return sessionUUID;
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

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setSessionUUID(String sessionUUID) {
        this.sessionUUID = sessionUUID;
    }

    /**
     * Calculates the time period between the current date and registration date and creates
     * the appropriate message containing the years and months.
     * @return String A message which details the years and months since the user's registration.
     */
    public String getTimeSinceRegistration() {
        String timeMessage;

        LocalDate currentDate = LocalDate.now();
        Period dateDifference = Period.between(LocalDate.from(this.created), currentDate);

        if (dateDifference.getYears() > 0) {
            timeMessage = dateDifference.getYears() + " Year(s) and " + dateDifference.getMonths() + " Month(s)\n";
        } else {
            timeMessage = dateDifference.getMonths() + " Month(s)\n";
        }

        return timeMessage;
    }

    /**
     * hash the password by base64 and Xor encryption
     * @param password password
     * @return hashed password
     */
    public String encode(String password) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * check whether the password is same with original
     * @param password password
     * @return true when two password same
     */
    public boolean verifyPassword(String password) {
        try {
            return (this.password.equals(encode(password)));
        }
        catch (UnsupportedEncodingException e) {
            return false;
        }

    }

    /**
     * Returns a list of Business objects administered by the user.
     * @return businessesAdministeredObjects a list of Business objects
     */
    public List<Business> getBusinessesAdministeredObjects() {
        return businessesAdministeredObjects;
    }

    /**
     * Loops through a list of businesses administered by the user and
     * gets the businesses ids' and adds them to a list of integers.
     * @return businessesAdministered A list of business ids' administered by the user.
     */
    public List<Integer> getBusinessesAdministered() {
        List<Integer> businessesAdministered = new ArrayList<>();
        for (Business business: businessesAdministeredObjects) {
            businessesAdministered.add(business.getId());
        }
        return businessesAdministered;
    }

    /**
     * Generate a randomised UUID used for a session token.
     * @return UUID
     */
    public static String generateSessionUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "User(" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", middleName=" + middleName +
                ", nickname=" + nickname +
                ", bio=" + bio +
                ", email=" + email +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNumber=" + phoneNumber +
                ", homeAddress=" + homeAddress +
                ", password=" + password +
                ", created=" + created +
                ", businessesAdministered=" + getBusinessesAdministered() +
                ')';
    }

}
