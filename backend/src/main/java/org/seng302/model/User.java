/**
 * Summary. This file contains the definition for the User.
 * <p>
 * Description. This file contains the defintion for the User.
 *
 * @link team-400/src/main/java/org/seng302/user/User
 * @file This file contains the definition for User.
 * @author team-400.
 * @since 5.5.2021
 */
package org.seng302.model;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.enums.Role;
import org.seng302.view.outgoing.UserPayloadSecure;

/**
 * Class for user accounts
 * Users can be administrators of business accounts.
 * Users can own marketplace cards.
 */
@Embeddable
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class User {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
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

    @Column(name = "bio", length = 600)
    private String bio;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    //EAGER to allow access to this attribute outside of a context of an open hibernate session (for loading initial data SQL script)
    @JoinColumn(name = "address_id", nullable = false)
    private Address homeAddress;

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
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "businesses_id")})
    private List<Business> businessesAdministeredObjects = new ArrayList<>();

    @Column(name = "session_uuid")
    private String sessionUUID;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<MarketplaceCard> cards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<HasKeywordNotification> readKeywordNotifications = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "bookmarkedListings", fetch = FetchType.LAZY)
    private List<Listing> bookmarkedListings = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<ListingNotification> listingNotifications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<HasListingNotification> readListingNotifications = new ArrayList<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<BookmarkedListingMessage> bookmarkedListingMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserImage> userImages;

    @Column(name = "remaining_login_attempts")
    private Integer remainingLoginAttempts = LOGIN_ATTEMPTS_LIMIT;

    @Column(name = "time_when_unlocked")
    private LocalDateTime timeWhenUnlocked;

    // Values need for validation.
    private static final Integer FIRST_NAME_MIN_LENGTH = 2;
    private static final Integer FIRST_NAME_MAX_LENGTH = 255;

    private static final Integer MIDDLE_NAME_MIN_LENGTH = 0;
    private static final Integer MIDDLE_NAME_MAX_LENGTH = 255;

    private static final Integer LAST_NAME_MIN_LENGTH = 2;
    private static final Integer LAST_NAME_MAX_LENGTH = 255;

    private static final Integer NICKNAME_MIN_LENGTH = 0;
    private static final Integer NICKNAME_MAX_LENGTH = 255;

    private static final Integer BIO_MIN_LENGTH = 0;
    private static final Integer BIO_MAX_LENGTH = 600;

    private static final Integer EMAIL_MIN_LENGTH = 5;
    private static final Integer EMAIL_MAX_LENGTH = 30;

    private static final Integer MIN_AGE = 13;

    private static final Integer PHONE_NUMBER_MIN_LENGTH = 0;
    private static final Integer PHONE_NUMBER_MAX_LENGTH = 15;

    private static final Integer PASSWORD_MIN_LENGTH = 8;
    private static final Integer PASSWORD_MAX_LENGTH = 30;

    private static final Integer LOGIN_ATTEMPTS_LIMIT = 3;

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
     * @param password User's password (not stored in plaintext)
     * @param created Date individual signed up
     * @param role Role determines admin privileges.
     * @throws IllegalUserArgumentException  Validation exception
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
    ) throws IllegalUserArgumentException {
        if (!isValidFirstName(firstName)) {
            throw new IllegalUserArgumentException("Invalid first name");
        }
        if (!isValidMiddleName(middleName)) {
            throw new IllegalUserArgumentException("Invalid middle name");
        }
        if (!isValidLastName(lastName)) {
            throw new IllegalUserArgumentException("Invalid last name");
        }
        if (!isValidNickname(nickname)) {
            throw new IllegalUserArgumentException("Invalid nickname");
        }
        if (!isValidBio(bio)) {
            throw new IllegalUserArgumentException("Invalid bio");
        }
        if (!isValidEmail(email)) {
            throw new IllegalUserArgumentException("Invalid email address");
        }
        if (!isValidDOB(dateOfBirth)) {
            throw new IllegalUserArgumentException("Invalid date of birth");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalUserArgumentException("Invalid phone number");
        }
        if (!isValidPassword(password)) {
            throw new IllegalUserArgumentException("Invalid password");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = (middleName.equals("")) ? null : middleName;
        this.nickname = (nickname.equals("")) ? null : nickname;
        this.bio = (bio.equals("")) ? null : bio;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = (phoneNumber.equals("")) ? null : phoneNumber;
        this.homeAddress = homeAddress;
        this.password = encode(password);
        this.created = created;
        this.role = (role.toString().equals("")) ? Role.USER : role;
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
        return homeAddress;
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

    public List<Listing> getBookmarkedListing() {
        return bookmarkedListings;
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

    public void setHomeAddress(Address homeAddress) {
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

    public List<HasKeywordNotification> getReadKeywordNotifications() {
        return readKeywordNotifications;
    }

    public void setReadKeywordNotifications(List<HasKeywordNotification> readKeywordNotifications) {
        this.readKeywordNotifications = readKeywordNotifications;
    }

    public void setBookmarkedListings(List<Listing> bookMarkedListings) {
        this.bookmarkedListings = bookMarkedListings;
    }

    public List<BookmarkedListingMessage> getBookmarkedListingMessages() {
        return bookmarkedListingMessages;
    }

    public void setBookmarkedListingMessages(List<BookmarkedListingMessage> bookmarkedListingMessages) {
        this.bookmarkedListingMessages = bookmarkedListingMessages;
    }

    public void setUserImages(List<UserImage> userImages) {
        this.userImages = userImages;
    }

    /**
     * validate and set firstName
     * @param firstName firstName
     * @throws IllegalUserArgumentException Invalid first name
     */
    public void updateFirstName(String firstName) throws IllegalUserArgumentException {
        if (!isValidFirstName(firstName)) {
            throw new IllegalUserArgumentException("Invalid first name");
        }
        setFirstName(firstName);
    }

    /**
     * validate and set lastName
     * @param lastName lastName
     * @throws IllegalUserArgumentException Invalid last name
     */
    public void updateLastName(String lastName) throws IllegalUserArgumentException {
        if (!isValidLastName(lastName)) {
            throw new IllegalUserArgumentException("Invalid last name");
        }
        setLastName(lastName);
    }

    /**
     * validate and set middleName
     * @param middleName middleName
     * @throws IllegalUserArgumentException Invalid middle name
     */
    public void updateMiddleName(String middleName) throws IllegalUserArgumentException {
        if (!isValidMiddleName(middleName)) {
            throw new IllegalUserArgumentException("Invalid middle name");
        }
        setMiddleName(middleName);
    }

    /**
     * validate and set nickname
     * @param nickname nickname
     * @throws IllegalUserArgumentException Invalid nickname
     */
    public void updateNickname(String nickname) throws IllegalUserArgumentException {
        if (!isValidNickname(nickname)) {
            throw new IllegalUserArgumentException("Invalid nickname");
        }
        setNickname(nickname);
    }

    /**
     * validate and set bio
     * @param bio bio
     * @throws IllegalUserArgumentException Invalid bio
     */
    public void updateBio(String bio) throws IllegalUserArgumentException {
        if (!isValidBio(bio)) {
            throw new IllegalUserArgumentException("Invalid bio");
        }
        setBio(bio);
    }

    /**
     * validate and set email
     * @param email email
     * @throws IllegalUserArgumentException Invalid email address
     */
    public void updateEmail(String email) throws IllegalUserArgumentException {
        if (!isValidEmail(email)) {
            throw new IllegalUserArgumentException("Invalid email address");
        }
        setEmail(email);
    }

    /**
     * validate and set dateOfBirth
     * @param dateOfBirth dateOfBirth
     * @throws IllegalUserArgumentException Invalid date of birth
     */
    public void updateDateOfBirth(LocalDate dateOfBirth) throws IllegalUserArgumentException {
        if (!isValidDOB(dateOfBirth)) {
            throw new IllegalUserArgumentException("Invalid date of birth");
        }
        setDateOfBirth(dateOfBirth);
    }

    /**
     * validate and set phoneNumber
     * @param phoneNumber phoneNumber
     * @throws IllegalUserArgumentException Invalid phone number
     */
    public void updatePhoneNumber(String phoneNumber) throws IllegalUserArgumentException {
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalUserArgumentException("Invalid phone number");
        }
        setPhoneNumber(phoneNumber);
    }

    /**
     * validate and set
     * @param password password
     * @throws IllegalUserArgumentException Invalid password
     */
    public void updatePassword(String password) throws IllegalUserArgumentException {
        if (!isValidPassword(password)) {
            throw new IllegalUserArgumentException("Invalid password");
        }
        setPassword(encode(password));
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
    public String encode(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * check whether the password is same with original
     * @param password password
     * @return true when two password same
     */
    public boolean verifyPassword(String password) {
        return (this.password.equals(encode(password)));
    }

    /**
     * Returns a list of Business objects administered by the user.
     * @return businessesAdministeredObjects a list of Business objects
     */
    public List<Business> getBusinessesAdministeredObjects() {
        return businessesAdministeredObjects;
    }

    /**
     * Returns a list of MarketplaceCard objects created by the user.
     * @return cards a list of MarketplaceCard objects.
     */
    public List<MarketplaceCard> getCards() {
        return cards;
    }

    /**
     * return a list of UserImage object related to the user.
     * @return user image
     */
    public List<UserImage> getUserImages() {
        return userImages;
    }

    /**
     * Removes the given business from the businessesAdministeredObjects
     * @param business a business to be removed.
     */
    public void removeABusinessesAdministeredObjects(Business business) {
        int businessId = business.getId();
        for (int i = 0; i < businessesAdministeredObjects.size(); i++) {
            if (businessesAdministeredObjects.get(i).getId() == businessId) {
                this.businessesAdministeredObjects.remove(i);
                i--;
            }
        }
    }

    /**
     * Removes the given card from the list of cards created by the user.
     * @param card the card to be removed
     */
    public void removeACardFromMarketplaceCards(MarketplaceCard card) {
        int cardId = card.getId();
        for (int i = 0; i < this.cards.size(); i++) {
            if (this.cards.get(i).getId() == cardId) {
                this.cards.remove(i);
                i--;
            }
        }
    }

    /**
     * Loops through a list of businesses administered by the user and
     * gets the businesses ids' and adds them to a list of integers.
     * @return businessesAdministered A list of business ids' administered by the user.
     */
    public List<Integer> getBusinessesAdministered() {
        List<Integer> businessesAdministered = new ArrayList<>();
        for (Business business : businessesAdministeredObjects) {
            businessesAdministered.add(business.getId());
        }
        return businessesAdministered;
    }

    /**
     * Add a listing to user bookmark if that listing not in user's bookmark
     * @param listing listing
     */
    public void addAListingToBookmark(Listing listing) {
        if (!this.bookmarkedListings.contains(listing)) {
            this.bookmarkedListings.add(listing);
        }
    }

    /**
     * Remove a listing from user bookmark if that listing not in user's bookmark
     * @param givenListing given listing
     */
    public void removeAListingFromBookmark(Listing givenListing) {
        for (int i = 0; i < bookmarkedListings.size(); i++) {
            if (bookmarkedListings.get(i) == givenListing) {
                bookmarkedListings.remove(i);
                i--;
            }
        }
    }

    /**
     * Add a new listing notification to the list of listing notifications that contain this user.
     * Also adds this user to the list of users for the corresponding listing notification.
     *
     * @param listingNotification a listing notification which contains this user.
     */
    public void addListingNotification(ListingNotification listingNotification) {
        listingNotifications.add(listingNotification);
        listingNotification.addUser(this);
    }

    /**
     * Remove a listing notification from the list of listing notifications that contain this user.
     * Also removes this user from the list of users for the corresponding listing notification.
     *
     * @param listingNotification a listing notification which contains this user.
     */
    public void removeListingNotification(ListingNotification listingNotification) {
        int listingNotificationId = listingNotification.getId();
        for (int i = 0; i < listingNotifications.size(); i++) {
            if (listingNotifications.get(i).getId() == listingNotificationId) {
                this.listingNotifications.remove(i);
            }
        }

        listingNotification.removeUser(this);
    }

    public List<ListingNotification> getListingNotifications() {
        return listingNotifications;
    }

    public void setListingNotifications(List<ListingNotification> listingNotifications) {
        this.listingNotifications = listingNotifications;
    }

    public List<HasListingNotification> getReadListingNotifications() {
        return readListingNotifications;
    }

    public void setReadListingNotifications(List<HasListingNotification> readListingNotifications) {
        this.readListingNotifications = readListingNotifications;
    }

    /**
     * Generate a randomised UUID used for a session token.
     * @return UUID
     */
    public static String generateSessionUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the User.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"firstName\":\"" + firstName + "\"" +
                ",\"lastName\":\"" + lastName + "\"" +
                ",\"middleName\":\"" + middleName + "\"" +
                ",\"nickname\":\"" + nickname + "\"" +
                ",\"bio\":\"" + bio + "\"" +
                ",\"email\":\"" + email + "\"" +
                ",\"dateOfBirth\":\"" + dateOfBirth + "\"" +
                ",\"phoneNumber\":\"" + phoneNumber + "\"" +
                ",\"homeAddress\":" + homeAddress +
                ",\"created\":\"" + created + "\"" +
                ",\"role\":\"" + role + "\"" +
                ",\"businessesAdministered\":[null]" +
                "}";
    }

    public void setBusinessesAdministeredObjects(List<Business> businessesAdministeredObjects) {
        this.businessesAdministeredObjects = businessesAdministeredObjects;
    }

    public UserPayloadSecure toUserPayloadSecure() throws Exception {
        List<Business> administrators = businessesAdministeredObjects;
        for (Business administrator : administrators) {
            administrator.setAdministrators(new ArrayList<>());
        }
        return new UserPayloadSecure(
                id,
                firstName,
                lastName,
                middleName,
                nickname,
                bio,
                email,
                homeAddress.toAddressPayloadSecure(),
                created,
                role,
                administrators,
                userImages
        );
    }

    /*---------------------------------------------------Validation---------------------------------------------------*/

    /**
     * Checks to see whether first name is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param firstName The first name to be checked.
     * @return true when the first name is valid
     */
    private boolean isValidFirstName(String firstName) {
        return (firstName.length() >= FIRST_NAME_MIN_LENGTH) &&
                (firstName.length() <= FIRST_NAME_MAX_LENGTH) &&
                (firstName.matches("^[a-zA-ZÀ-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '-]+$"));
    }

    /**
     * Checks to see whether middle name is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param middleName The middle name to be checked.
     * @return true when the middle name is valid
     */
    private boolean isValidMiddleName(String middleName) {
        return (middleName.length() >= MIDDLE_NAME_MIN_LENGTH) &&
                (middleName.length() <= MIDDLE_NAME_MAX_LENGTH) &&
                (middleName.matches("^[a-zA-ZÀ-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '-]*$"));
    }

    /**
     * Checks to see whether last name is valid based on its constraints
     * This method can be updated in the future if there is additional constraints.
     * @param lastName The last name to be checked.
     * @return true when the last name is valid.
     */
    private boolean isValidLastName(String lastName) {
        return (lastName.length() >= LAST_NAME_MIN_LENGTH) &&
                (lastName.length() <= LAST_NAME_MAX_LENGTH) &&
                (lastName.matches("^[a-zA-ZÀ-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '-]+$"));
    }

    /**
     * Checks to see whether nickname is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param nickname The nickname to be checked.
     * @return true when the nickname is valid.
     */
    private boolean isValidNickname(String nickname) {
        return (nickname.length() >= NICKNAME_MIN_LENGTH) &&
                (nickname.length() <= NICKNAME_MAX_LENGTH) &&
                (nickname.matches("^[a-zA-ZÀ-ÖØ-öø-įĴ-őŔ-žǍ-ǰǴ-ǵǸ-țȞ-ȟȤ-ȳɃɆ-ɏḀ-ẞƀ-ƓƗ-ƚƝ-ơƤ-ƥƫ-ưƲ-ƶẠ-ỿ '-]*$"));
    }

    /**
     * Checks to see whether bio is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param bio The bio to be checked.
     * @return true when the bio is valid
     */
    private boolean isValidBio(String bio) {
        return (bio.length() >= BIO_MIN_LENGTH) &&
                (bio.length() <= BIO_MAX_LENGTH);
    }

    /**
     * Checks to see whether email is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param email The email to be checked.
     * @return true when the email is valid.
     */
    private boolean isValidEmail(String email) {
        return (email.length() >= EMAIL_MIN_LENGTH) &&
                (email.length() <= EMAIL_MAX_LENGTH) &&
                (email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"));
    }

    /**
     * Checks to see whether date of birth is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param dateOfBirth The date of birth to be checked.
     * @return true when the date of birth is valid.
     */
    private boolean isValidDOB(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        Integer minAge = Period.between(dateOfBirth, currentDate).getYears();
        return minAge >= MIN_AGE;
    }

    /**
     * Checks to see whether phone number is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param phoneNumber The phone number to be checked.
     * @return true when the phone number is valid.
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        return (phoneNumber.length() >= PHONE_NUMBER_MIN_LENGTH) &&
                (phoneNumber.length() <= PHONE_NUMBER_MAX_LENGTH) &&
                (phoneNumber.matches("^[+0-9 ]*$"));
    }

    /**
     * Checks to see whether password is valid based on its constraints.
     * This method can be updated in the future if there is additional constraints.
     * @param password The password to be checked.
     * @return true when the phone number is valid.
     */
    private boolean isValidPassword(String password) {
        return (password.length() >= PASSWORD_MIN_LENGTH) &&
                (password.length() <= PASSWORD_MAX_LENGTH) &&
                (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,30}$"));
    }

    /**
     * Update number of remaining login attempts
     */
    public void useAttempt() {
        this.remainingLoginAttempts -= 1;
    }

    /**
     * Checks whether the user has exceed the allowed number of attempts.
     * @return true if the user has exceeded this limit, otherwise false.
     */
    public boolean hasLoginAttemptsRemaining() {
      return this.remainingLoginAttempts > 0;
    }

    /**
     * Sets timeWhenUnlocked.
     */
    public void setTimeWhenUnlocked(LocalDateTime time) {
        this.timeWhenUnlocked = time;
    }

    /**
     * Checks if the user's account can be unlocked.
     * @return true if the account can be unlocked, false otherwise.
     */
    public Boolean canUnlock() {
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.isAfter(this.timeWhenUnlocked);
    }

}
