package org.seng302.address;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.validation.AddressValidation;
import org.seng302.validation.Validation;

import javax.persistence.*;

/**
 * Class for Addresses.
 */
@Embeddable
@Data // generate setters and getters for all fields (lombok pre-processor)
@NoArgsConstructor // generate a no-args constructor needed by JPA (lombok pre-processor)
@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class Address {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue // autoincrement the ID
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

    /**
     * Address constructor.
     * @param streetNumber Street Number (optional)
     * @param streetName Street Name (optional)
     * @param city City (optional)
     * @param region Region (optional)
     * @param country Country (mandatory)
     * @param postcode Postcode (optional)
     * @throws Exception Validation exception
     */
    public Address(String streetNumber, String streetName, String city, String region, String country, String postcode) throws Exception {
        if (!AddressValidation.isValidStreetNumber(streetNumber)) {
            throw new Exception("Invalid street number");
        }
        if (!AddressValidation.isValidStreetName(streetName)) {
            throw new Exception("Invalid street name");
        }
        if (!AddressValidation.isValidCity(city)) {
            throw new Exception("Invalid city");
        }
        if (!AddressValidation.isValidRegion(region)) {
            throw new Exception("Invalid region");
        }
        if (!AddressValidation.isValidCountry(country)) {
            throw new Exception("Invalid country");
        }
        if (!AddressValidation.isValidPostcode(postcode)) {
            throw new Exception("Invalid postcode");
        }

        this.streetNumber = (streetNumber.equals("")) ? null : streetNumber;
        this.streetName = (streetName.equals("")) ? null : streetName;
        this.city = (city.equals("")) ? null : city;
        this.region = (region.equals("")) ? null : region;
        this.country = country;
        this.postcode = (postcode.equals("")) ? null : postcode;
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
    public static Address toAddress(String string) throws Exception {
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
     * Make an address object to json form.
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
