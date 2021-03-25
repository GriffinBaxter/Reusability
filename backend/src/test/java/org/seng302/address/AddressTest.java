package org.seng302.address;

import org.junit.jupiter.api.Test;
import org.seng302.Address.Address;

import static org.junit.Assert.assertEquals;

public class AddressTest {

    @Test
    public void testToString(){
        String expected = "{\n" +
                "\"streetNumber\": \"3/24\",\n" +
                "\"streetName\": \"Ilam Road\",\n" +
                "\"city\": \"Christchurch\",\n" +
                "\"region\": \"Canterbury\",\n" +
                "\"country\": \"New Zealand\",\n" +
                "\"postcode\": \"90210\"\n" +
                "}";
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        assertEquals(expected, address.toString());
    }

    @Test
    public void testToAddressWithJsonFormat(){
        String string = "{\n" +
                "\"streetNumber\": \"3/24\",\n" +
                "\"streetName\": \"Ilam Road\",\n" +
                "\"city\": \"Christchurch\",\n" +
                "\"region\": \"Canterbury\",\n" +
                "\"country\": \"New Zealand\",\n" +
                "\"postcode\": \"90210\"\n" +
                "}";
        Address address = Address.toAddress(string);

        assertEquals("3/24", address.getStreetNumber());
        assertEquals("Ilam Road", address.getStreetName());
        assertEquals("Christchurch", address.getCity());
        assertEquals("Canterbury", address.getRegion());
        assertEquals("New Zealand", address.getCountry());
        assertEquals("90210", address.getPostcode());
    }

    @Test
    public void testToAddressWithoutJsonFormat(){
        String string = "{" +
                "\"streetNumber\": \"3/24\"," +
                "\"streetName\": \"Ilam Road\"," +
                "\"city\": \"Christchurch\"," +
                "\"region\": \"Canterbury\"," +
                "\"country\": \"New Zealand\"," +
                "\"postcode\": \"90210\"" +
                "}";
        Address address = Address.toAddress(string);

        assertEquals("3/24", address.getStreetNumber());
        assertEquals("Ilam Road", address.getStreetName());
        assertEquals("Christchurch", address.getCity());
        assertEquals("Canterbury", address.getRegion());
        assertEquals("New Zealand", address.getCountry());
        assertEquals("90210", address.getPostcode());
    }
}
