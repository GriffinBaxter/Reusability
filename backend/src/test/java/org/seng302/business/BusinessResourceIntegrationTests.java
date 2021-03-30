package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Address.Address;
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
import java.util.Optional;

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
        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                address,
                "testpassword",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        business = new Business(
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
        payloadJson = "{\n" +
                        "\"name\": \"Lumbridge General Store\",\n" +
                        "\"description\": \"A one-stop shop for all your adventuring needs\",\n" +
                        "\"address\": {\n" +
                                "\"streetNumber\": \"3/24\",\n" +
                                "\"streetName\": \"Ilam Road\",\n" +
                                "\"city\": \"Christchurch\",\n" +
                                "\"region\": \"Canterbury\",\n" +
                                "\"country\": \"New Zealand\",\n" +
                                "\"postcode\": \"90210\"\n" +
                                "},\n" +
                        "\"businessType\": \"Accommodation and Food Services\"\n" +
                        "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        Optional<Business> antherBusiness= businessRepository.findBusinessByName("Lumbridge General Store");
        assertThat(antherBusiness.get().getAdministrators().get(0).equals(user));
    }


    /**
     * Tests that an CREATED(201) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType and a create cookie belongs to an user.
     */
    @Test
    public void canCreateWhenDataValidAndCookieExists() throws Exception {
        payloadJson = "{\n" +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"A one-stop shop for all your adventuring needs\",\n" +
                "\"address\": {\n" +
                    "\"streetNumber\": \"3/24\",\n" +
                    "\"streetName\": \"Ilam Road\",\n" +
                    "\"city\": \"Christchurch\",\n" +
                    "\"region\": \"Canterbury\",\n" +
                    "\"country\": \"New Zealand\",\n" +
                    "\"postcode\": \"90210\"\n" +
                    "},\n" +
                "\"businessType\": \"Accommodation and Food Services\"\n" +
                "}";
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(user.getId()));
        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that an UNAUTHORIZED status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType but a wrong cookie.
     */
    @Test
    public void canCreateWhenDataValidAndCookieNotExists() throws Exception {
        payloadJson = "{\n" +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"A one-stop shop for all your adventuring needs\",\n" +
                "\"address\": {\n" +
                    "\"streetNumber\": \"3/24\",\n" +
                    "\"streetName\": \"Ilam Road\",\n" +
                    "\"city\": \"Christchurch\",\n" +
                    "\"region\": \"Canterbury\",\n" +
                    "\"country\": \"New Zealand\",\n" +
                    "\"postcode\": \"90210\"\n" +
                "},\n" +
                "\"businessType\": \"Accommodation and Food Services\"\n" +
                "}";
        response = mvc.perform(post("/businesses").contentType(MediaType.APPLICATION_JSON)
                .content(payloadJson)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a OK(200) status is received when the user id in the /businesses/{id} API endpoint does exist
     */
    @Test
    public void canRetrieveBusinessWhenBusinessDoesExist() throws Exception {
        id = business.getId();
        expectedJson = "{" +
                "\"id\":" + id + "," +
                "\"administrators\":[]," +
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