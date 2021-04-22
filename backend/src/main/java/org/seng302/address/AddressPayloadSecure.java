package org.seng302.address;

public class AddressPayloadSecure {
    private String city;
    private String region;
    private String country;

    public AddressPayloadSecure(
            String city,
            String region,
            String country
    ) throws Exception {
        this.city = city;
        this.region = region;
        this.country = country;
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
                "city=" + city +
                ", region=" + region +
                ", country=" + country +
                ')';
    }
}

