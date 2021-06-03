/**
 * Summary. The file contains the definition of the AddressPayloadSecure class.
 *
 * Description. The file contains the definition of AddressPayloadSecure, which is used
 * to only return parts of the address.
 *
 * @link   team-400/src/main/java/org/seng302/address/AddressPayloadSecure
 * @file   This file contains the definition for the AddressPayloadSecure class.
 * @author team-400
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

/**
 * AddressPayloadSecure class
 */
public class AddressPayloadSecure {
    private String suburb;
    private String city;
    private String region;
    private String country;

    public AddressPayloadSecure(
            String suburb,
            String city,
            String region,
            String country
    ) throws Exception {
        this.suburb = suburb;
        this.city = city;
        this.region = region;
        this.country = country;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
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

    @Override
    public String toString() {
        return "Address(" +
                "suburb=" + suburb +
                "city=" + city +
                ", region=" + region +
                ", country=" + country +
                ')';
    }
}

