package org.seng302.address;

public class AddressPayload {
    private String streetNumber;
    private String streetName;
    private String city;
    private String region;
    private String country;
    private String postcode;


    public AddressPayload(
            String streetNumber,
            String streetName,
            String city,
            String region,
            String country,
            String postcode
    ) throws Exception {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.region = region;
        this.country = country;
        this.postcode = postcode;
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

    @Override
    public String toString() {
        return "Address(" +
                "streetNumber=" + streetNumber +
                ", streetName=" + streetName +
                ", city=" + city +
                ", postcode=" + postcode +
                ", region=" + region +
                ", country=" + country +
                ')';
    }
}

