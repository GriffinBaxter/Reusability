package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.address.Address;
import org.seng302.address.AddressRepository;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
public class BusinessResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private String payloadJson;

    private MockHttpServletResponse response;

    private Integer id;

    private String expectedJson;

    private User user;

    private Business business;

    private Address address;

    @BeforeAll
    public void setup() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        addressRepository.save(address);
        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        userRepository.save(user);
        businessRepository.save(business);
    }

    /**
     * test when business has been create, current user has been add to business's administrators
     * @throws Exception
     */
    @Test
    public void setAdministratorComplete() throws Exception {
        payloadJson = "{" +
                        "\"name\": \"Lumbridge General Stores\"," +
                        "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                        "\"address\": {" +
                                "\"streetNumber\": \"3/24\"," +
                                "\"streetName\": \"Ilam Road\"," +
                                "\"city\": \"Christchurch\"," +
                                "\"region\": \"Canterbury\"," +
                                "\"country\": \"New Zealand\"," +
                                "\"postcode\": \"90210\"" +
                                "}," +
                        "\"businessType\": \"Accommodation and Food Services\"" +
                        "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Business antherBusiness = businessRepository.findBusinessesByName("Lumbridge General Stores").get(0);
        assertThat(antherBusiness.getAdministrators().get(0).equals(user));
    }


    /**
     * Tests that an CREATED(201) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType and a create cookie belongs to an user.
     */
    @Test
    public void canCreateWhenDataValidAndCookieExists() throws Exception {
        payloadJson = "{" +
                "\"name\": \"New Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name(empty), description, address, businessType.
     */
    @Test
    public void canNotCreateWhenNameEmpty() throws Exception {
        payloadJson = "{" +
                "\"name\": \"\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Illegal business name");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name(only space), description, address, businessType.
     */
    @Test
    public void canNotCreateWhenNameOnlySpace() throws Exception {
        payloadJson = "{" +
                "\"name\": \"   \"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Illegal business name");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name(length = 101), description, address, businessType.
     */
    @Test
    public void canNotCreateWhenNameLengthLagerThen100() throws Exception {
        String aName = "a";
        for (int i = 0; i < 10; i++){
            aName += "aaaaaaaaaa"; // (a * 10)
        } //length = 101

        payloadJson = "{" +
                "\"name\": \"" + aName + "\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Illegal business name");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description(length = 601), address, businessType.
     */
    @Test
    public void canNotCreateWhenDescriptionLengthLagerThen600() throws Exception {
        String aDescription = "a";
        for (int i = 0; i < 12; i++){
            aDescription += "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"; // (a * 50)
        } //length = 601

        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"" + aDescription + "\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Illegal description");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description, address(length = 256), businessType.
     */
    @Test
    public void canNotCreateWhenAddressLengthLagerThen255() throws Exception {
        String aString = "";
        for (int i = 0; i < 5; i++){
            aString += "aaaaaaaaaa"; // (a * 10)
        } //length = 50

        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"" + aString + "\"," +
                    "\"streetName\": \"" + aString + "\"," +
                    "\"city\": \"" + aString + "\"," +
                    "\"region\": \"" + aString + "\"," +
                    "\"country\": \"" + aString + "\"," +
                    "\"postcode\": \"9\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Illegal address");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description, address(country = ""), businessType.
     */
    @Test
    public void canNotCreateWhenAddressContainAnEmptyCountry() throws Exception {
        String aString = "";
        for (int i = 0; i < 5; i++){
            aString += "aaaaaaaaaa"; // (a * 10)
        } //length = 50

        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Illegal address");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description, address, businessType(not exist).
     */
    @Test
    public void canNotCreateWhenBusinessTypeIsNotExist() throws Exception {

        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"example\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Illegal business type");
    }

    /**
     * Tests that an UNAUTHORIZED(401) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType but a wrong cookie.
     */
    @Test
    public void canNotCreateWhenDataValidAndCookieNotExists() throws Exception {
        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        response = mvc.perform(post("/businesses").contentType(MediaType.APPLICATION_JSON)
                .content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that an CONFLICT(409) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType and a create cookie belongs to an user, but
     * twice.
     */
    @Test
    public void canNotCreateWhenDataAlreadyInRepo() throws Exception {
        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"3/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Tests that a OK(200) status is received when the user id in the /businesses/{id} API endpoint does exist
     */
    @Test
    public void canRetrieveBusinessWhenBusinessDoesExist() throws Exception {
        id = business.getId();
        expectedJson = "{" +
                "\"id\":" + id + "," +
                "\"administrators\":[null]," +
                "\"primaryAdministratorId\":" + business.getPrimaryAdministratorId() + "," +
                "\"name\":\"" + business.getName() + "\"," +
                "\"description\":\"" + business.getDescription() + "\"," +
                "\"address\":{" +
                    "\"streetNumber\":\"" + address.getStreetNumber() + "\"," +
                    "\"streetName\":\"" + address.getStreetName() + "\"," +
                    "\"city\":\"" + address.getCity() + "\"," +
                    "\"region\":\"" + address.getRegion() + "\"," +
                    "\"country\":\"" + address.getCountry() + "\"," +
                    "\"postcode\":\"" + address.getPostcode() + "\"" +
                    "}," +
                "\"businessType\":\"" + business.getBusinessType() + "\"," +
                "\"created\":\"" + business.getCreated() + "\"}";
        System.out.println(expectedJson);
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(1));
        response = mvc.perform(get(String.format("/businesses/%d", id)).cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().replace("\\n", "").replace("\\", ""))
                .isEqualTo(expectedJson);
    }

    /**
     * Tests that a UNAUTHORIZED(401) status is received when cookie wrong
     */
    @Test
    public void canNotRetrieveBusinessWhenCookieNotExist() throws Exception {
        id = business.getId();
        expectedJson = "";

        response = mvc.perform(get(String.format("/businesses/%d", id))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT_ACCEPTABLE(406) status is received when the user id in the /businesses/{id} API endpoint does exist
     */
    @Test
    public void canNotRetrieveBusinessWhenBusinessDoesNotExist() throws Exception {
        id = 0;
        expectedJson = "";

        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(1));
        response = mvc.perform(get(String.format("/businesses/%d", id)).cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

}