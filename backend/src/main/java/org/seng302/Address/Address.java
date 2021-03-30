package org.seng302.Address;


import net.minidev.json.JSONObject;

import java.lang.reflect.Array;
import java.util.List;

public class Address {
    private String streetNumber;
    private String streetName;
    private String city;
    private String region;
    private String country;
    private String postcode;

    public Address(String streetNumber, String streetName, String city, String region, String country, String postcode){
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
     * make a string address to become an address object
     * @param string address in json form
     * @return an address object
     */
    public static Address toAddress(String string){
        String[] infos = string.replace("{", "").replace("}", "").
                replace("\"", "").replace("\n","").split(",");

        String streetNumber = "";
        String streetName = "";
        String city = "";
        String region = "";
        String country = "";
        String postcode = "";

        for (int i = 0; i < 6; i++){
            String data = infos[i].split(":")[1];
            if (i == 0) streetNumber = data;
            else if (i == 1) streetName = data;
            else if (i == 2) city = data;
            else if (i == 3) region = data;
            else if (i == 4) country = data;
            else if (i == 5) postcode = data;
        }

        return new Address(streetNumber, streetName, city, region, country, postcode);
    }

    /**
     * make an address object to json form
     * @return a string contain address info in json form
     */
    @Override
    public String toString() {
        return "{" +
                "\"streetNumber\":\"" + streetNumber + "\"," +
                "\"streetName\":\""   + streetName   + "\"," +
                "\"city\":\""        + city         + "\"," +
                "\"region\":\""       + region       + "\"," +
                "\"country\":\""      + country      + "\"," +
                "\"postcode\":\""     + postcode     + "\"" +
                "}";
    }
}
