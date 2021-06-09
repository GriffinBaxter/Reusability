/**
 * Summary. Contains the class definition for the address JPA entity object.
 *
 * Description. Contains the class for the address JPA entity. This is used to perform
 * actions associated with address information.
 *
 * @link   team-400/backend/src/main/java/org/seng302/address/Address
 * @file   Defines the Address class.
 * @author Team-400
 * @since  5.5.2021
 */
package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.view.outgoing.AddressPayload;
import org.seng302.view.outgoing.AddressPayloadSecure;

import javax.persistence.*;
import java.util.List;

/**
 * Class for the Address entity. Addresses are used by both Users and Businesses.
 */
@Embeddable
@Data // generate setters and getters for all fields (lombok pre-processor)
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class Address {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "suburb")
    private String suburb;

    @OneToMany(mappedBy = "homeAddress", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Business> businesses;

    // Values need for validation.
    private final Integer STREET_NUMBER_MIN_LENGTH = 0;
    private final Integer STREET_NUMBER_MAX_LENGTH = 255;

    private final Integer STREET_NAME_MIN_LENGTH = 0;
    private final Integer STREET_NAME_MAX_LENGTH = 255;

    private final Integer CITY_MIN_LENGTH = 0;
    private final Integer CITY_MAX_LENGTH = 255;

    private final Integer REGION_MIN_LENGTH = 0;
    private final Integer REGION_MAX_LENGTH = 255;

    private final Integer COUNTRY_MIN_LENGTH = 1;
    private final Integer COUNTRY_MAX_LENGTH = 255;

    private final Integer POSTCODE_MIN_LENGTH = 0;
    private final Integer POSTCODE_MAX_LENGTH = 255;

    private final Integer SUBURB_MIN_LENGTH = 0;
    private final Integer SUBURB_MAX_LENGTH = 255;

    /**
     * Address constructor.
     * @param streetNumber Street Number (optional)
     * @param streetName Street Name (optional)
     * @param city City (optional)
     * @param region Region (optional)
     * @param country Country (mandatory)
     * @param postcode Postcode (optional)
     * @param suburb Suburb (optional)
     * @throws IllegalArgumentException Thrown when a parameter is not valid.
     */
    public Address(String streetNumber,
                   String streetName,
                   String city,
                   String region,
                   String country,
                   String postcode,
                   String suburb) throws IllegalAddressArgumentException {
        if (!isValidStreetNumber(streetNumber)) {
            throw new IllegalAddressArgumentException("Invalid street number");
        }
        if (!isValidStreetName(streetName)) {
            throw new IllegalAddressArgumentException("Invalid street name");
        }
        if (!isValidCity(city)) {
            throw new IllegalAddressArgumentException("Invalid city");
        }
        if (!isValidRegion(region)) {
            throw new IllegalAddressArgumentException("Invalid region");
        }
        if (!isValidCountry(country)) {
            throw new IllegalAddressArgumentException("Invalid country");
        }
        if (!isValidPostcode(postcode)) {
            throw new IllegalAddressArgumentException("Invalid postcode");
        }
        if (!isValidSuburb(suburb)) {
            throw new IllegalAddressArgumentException("Invalid suburb");
        }

        this.streetNumber = (streetNumber.equals("")) ? null : streetNumber;
        this.streetName = (streetName.equals("")) ? null : streetName;
        this.city = (city.equals("")) ? null : city;
        this.region = (region.equals("")) ? null : region;
        this.country = country;
        this.postcode = (postcode.equals("")) ? null : postcode;
        this.suburb = (suburb.equals("")) ? null : suburb;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getPostcode() {
        return postcode;
    }

    /**
     * Returns the suburb
     * @return suburb, the suburb corresponding to this Address.
     */
    public String getSuburb() { return suburb; }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * Sets the suburb of this address to the incoming suburb.
     * @param suburb incoming suburb.
     */
    public void setSuburb (String suburb) {
        this.suburb = suburb;
    }

    /**
     * Change the users living at this address.
     * @param users the users living at this address.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Get a list of users located at this address.
     * @return a list of users located at this address.
     */
    public List<User> getUsers() {
        return this.users;
    }

    /**
     * The address entity stores a list of users living at the address.
     * This method can be used to add a user to this address.
     * @param user a user that is located at this address.
     */
    public void addUser(User user) {
        this.users.add(user);
        user.setHomeAddress(this);
    }

    /**
     * The address entity stores a list of users living at the address.
     * This method can be used to remove a user from this address.
     * @param user a user that use to be located at this address.
     */
    public void removeUser(User user) {
        this.users.remove(user);
        user.setHomeAddress(null);
    }

    /**
     * Get the id of this address.
     * This method can be useful if a search in the database by id is required.
     * @return the id of this address.
     */
    public int getId() {
        return id;
    }

    /**
     * Change the id of this address.
     * @param id the new id of the address.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get a list of businesses located at this address.
     * @return A list of businesses located at this address.
     */
    public List<Business> getBusinesses() {
        return businesses;
    }

    /**
     * Set the list of businesses located at this address to a new list of businesses.
     * @param businesses the new list of businesses located at this address.
     */
    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    /**
     * The address entity stores a list of businesses located at the address.
     * This method can be used to add a business to this address.
     * @param business a business that is located at this address.
     */
    public void addBusiness(Business business) {
        this.businesses.add(business);
        business.setAddress(this);
    }

    /**
     * The address entity stores a list of businesses located at the address.
     * This method can be used to remove a business from this address.
     * @param business a business that use to be located at this address.
     */
    public void removeBusiness(Business business) {
        this.businesses.remove(business);
        business.setAddress(null);
    }

    /**
     * make a string address to become an address object
     * @param string address in json form
     * @return an address object
     */
    public static Address toAddress(String string) throws Exception {
        String[] infos = string.replace("{", "").replace("}", "").
                replace("\"", "").replace("\n","").split(",");

        String streetNumber = "";
        String streetName = "";
        String city = "";
        String region = "";
        String country = "";
        String postcode = "";
        String suburb = "";

        for (int i = 0; i < 7; i++){
            String data = infos[i].split(":")[1];
            if (i == 0) streetNumber = data;
            else if (i == 1) streetName = data;
            else if (i == 2) city = data;
            else if (i == 3) region = data;
            else if (i == 4) country = data;
            else if (i == 5) postcode = data;
            else if (i == 6) suburb = data;
        }

        return new Address(streetNumber, streetName, city, region, country, postcode, suburb);
    }

    /**
     * Convert an Address object into an AddressPayload
     * @return an AddressPayload object
     */
    public AddressPayload toAddressPayload() throws Exception {
        return new AddressPayload(
                this.streetNumber,
                this.streetName,
                this.city,
                this.region,
                this.country,
                this.postcode,
                this.suburb
        );
    }

    /**
     * Convert an Address object into an AddressPayloadSecure i.e. no street number, street name or post code
     * @return an AddressPayloadSecure object
     */
    public AddressPayloadSecure toAddressPayloadSecure() throws Exception {
        return new AddressPayloadSecure(
                this.suburb,
                this.city,
                this.region,
                this.country
        );
    }

    /**
     * Make an address object to JSON string form.
     * @return a string contain address info in JSON form
     */
    @Override
    public String toString() {
        return "{" +
                "\"streetNumber\":\"" + streetNumber + "\"," +
                "\"streetName\":\""   + streetName   + "\"," +
                "\"city\":\""         + city         + "\"," +
                "\"region\":\""       + region       + "\"," +
                "\"country\":\""      + country      + "\"," +
                "\"postcode\":\""     + postcode     + "\"," +
                "\"suburb\":\""       + suburb       + "\""  +
                "}";
    }

    /**
     * Make an address object to secure JSON string form.
     * @return a string containing only some address info in JSON form (if you are not that user)
     */

    public String toSecureString() {
        return "{" +
                "\"suburb\":\""        + suburb         + "\"," +
                "\"city\":\""        + city         + "\"," +
                "\"region\":\""       + region       + "\"," +
                "\"country\":\""      + country      + "\"" +
                "}";
    }

    /**
     * Return the object represented in a single line readable string form
     * @return a string containing address info in string form
     */
    public String toOneLineString() {
        return streetNumber + ", "
                + streetName + ", "
                + city + ", "
                + region + ", "
                + country + ", "
                + postcode + ", "
                + suburb;
    }

    /* --------------------------------Validation-------------------------------- */

    /**
     * Checks to see whether street number is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param streetNumber The street number to be checked.
     * @return true when the street number is within its range of length constraints.
     */
    private boolean isValidStreetNumber(String streetNumber) {
        return (streetNumber.length() >= STREET_NUMBER_MIN_LENGTH) && (streetNumber.length() <= STREET_NUMBER_MAX_LENGTH);
    }

    /**
     * Checks to see whether street name is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param streetName The street name to be checked.
     * @return true when the street name is within its range of length constraints.
     */
    private boolean isValidStreetName(String streetName) {
        return (streetName.length() >= STREET_NAME_MIN_LENGTH) && (streetName.length() <= STREET_NAME_MAX_LENGTH);
    }

    /**
     * Checks to see whether city is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param city The city to be checked.
     * @return true when the city is within its range of length constraints.
     */
    private boolean isValidCity(String city) {
        return (city.length() >= CITY_MIN_LENGTH) && (city.length() <= CITY_MAX_LENGTH);
    }

    /**
     * Checks to see whether region is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param region The region to be checked.
     * @return true when the region is within its range of length constraints.
     */
    private boolean isValidRegion(String region) {
        return (region.length() >= REGION_MIN_LENGTH) && (region.length() <= REGION_MAX_LENGTH);
    }

    /**
     * Checks to see whether country is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param country The country to be checked.
     * @return true when the country is within its range of length constraints.
     */
    private boolean isValidCountry(String country) {
        return (country.length() >= COUNTRY_MIN_LENGTH) && (country.length() <= COUNTRY_MAX_LENGTH);
    }

    /**
     * Checks to see whether postcode is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param postcode The postcode to be checked.
     * @return true when the postcode is within its range of length constraints.
     */
    private boolean isValidPostcode(String postcode) {
        return (postcode.length() >= POSTCODE_MIN_LENGTH) && (postcode.length() <= POSTCODE_MAX_LENGTH);
    }

    /**
     * Checks to see whether suburb is valid based on its length.
     * This method can be updated in the future if there is additional constraints.
     * @param suburb The suburb to be checked.
     * @return true when the suburb is within its range of length constraints.
     */
    private boolean isValidSuburb(String suburb) {
        return (suburb.length() >= SUBURB_MIN_LENGTH) && (suburb.length() <= SUBURB_MAX_LENGTH);
    }


}
