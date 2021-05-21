/**
 * Summary. Contains the definition for a general address payload.
 *
 * Description. contains the difinition for a general Address payload that
 * contains all the information associated with an address.
 *
 * @link   team-400/backend/src/main/java/org/seng302/address/AddressPayload
 * @file   Defines the class for the AddressPayload
 * @author team-400
 * @since  5.5.2021
 */
package org.seng302.address;

/**
 * AddressPayload class
 */
public class AddressPayload {
    private String streetNumber;
    private String streetName;
    private String city;
    private String region;
    private String country;
    private String postcode;
    private String suburb;


    public AddressPayload(
            String streetNumber,
            String streetName,
            String city,
            String region,
            String country,
            String postcode,
            String suburb
    ) throws Exception {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.region = region;
        this.country = country;
        this.postcode = postcode;
        this.suburb = suburb;
    }

    public AddressPayload() {
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * Returns suburb
     * @return suburb of this Address payload
     */
    public String getSuburb() {
        return suburb;
    }

    /**
     * Set this suburb to incoming suburb
     * @param suburb incoming suburb.
     */
    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    /**
     * Overridden for debugging purposes.
     * @return string representing this Address payload.
     */
    @Override
    public String toString() {
        return "Address(" +
                "streetNumber=" + streetNumber +
                ", streetName=" + streetName +
                ", suburb=" + suburb +
                ", city=" + city +
                ", postcode=" + postcode +
                ", region=" + region +
                ", country=" + country +
                ')';
    }
}

