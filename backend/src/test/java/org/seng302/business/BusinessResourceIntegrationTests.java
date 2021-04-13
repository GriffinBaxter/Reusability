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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
public class BusinessResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

    private String payloadJson;

    private MockHttpServletResponse response;

    private Integer id;

    private String sessionToken;

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
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());
        business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        this.mvc = MockMvcBuilders.standaloneSetup(
                new BusinessResource(businessRepository, userRepository, addressRepository)
        ).build();
    }

    /**
     * test when business has been create, current user has been add to business's administrators
     * @throws Exception
     */
    @Test
    public void setAdministratorComplete() throws Exception {
        // given
        Business newBusiness = new Business(
                user.getId(),
                "Lumbridge General Store",
                "A one-stop shop for all your adventuring needs",
                new Address(
                        "2/24",
                        "Ilam Road",
                        "Christchurch",
                        "Canterbury",
                        "New Zealand",
                        "90210"
                ),
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );
        newBusiness.setId(3);
        newBusiness.addAdministrators(user);

        payloadJson = "{" +
                        "\"name\": \"Lumbridge General Stores\"," +
                        "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                        "\"address\": {" +
                                "\"streetNumber\": \"2/24\"," +
                                "\"streetName\": \"Ilam Road\"," +
                                "\"city\": \"Christchurch\"," +
                                "\"region\": \"Canterbury\"," +
                                "\"country\": \"New Zealand\"," +
                                "\"postcode\": \"90210\"" +
                                "}," +
                        "\"businessType\": \"Accommodation and Food Services\"" +
                        "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(businessRepository.save(any(Business.class))).thenReturn(newBusiness);
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(newBusiness.getAdministrators().get(0)).isEqualTo(user);
    }

    /**
     * Tests that an CREATED(201) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType and a create cookie belongs to an user.
     */
    @Test
    public void canCreateWhenDataValidAndCookieExists() throws Exception {
        // given
        payloadJson = "{" +
                "\"name\": \"New Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name(empty), description, address, businessType.
     */
    @Test
    public void canNotCreateWhenNameEmpty() throws Exception {
        // given
        payloadJson = "{" +
                "\"name\": \"\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid business name");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name(only space), description, address, businessType.
     */
    @Test
    public void canNotCreateWhenNameOnlySpace() throws Exception {
        // given
        payloadJson = "{" +
                "\"name\": \"   \"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid business name");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name(length = 101), description, address, businessType.
     */
    @Test
    public void canNotCreateWhenNameLengthLargerThan100() throws Exception {
        // given
        String aName = "a".repeat(101);
        payloadJson = "{" +
                "\"name\": \"" + aName + "\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid business name");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description(length = 601), address, businessType.
     */
    @Test
    public void canNotCreateWhenDescriptionLengthLargerThan600() throws Exception {
        // given
        String aDescription = "a".repeat(601);
        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"" + aDescription + "\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid description");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description, address(length = 256), businessType.
     */
    @Test
    public void canNotCreateWhenAddressLengthLargerThan255() throws Exception {
        // given
        String aString = "a".repeat(256);
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
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid business address");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description, address(country = ""), businessType.
     */
    @Test
    public void canNotCreateWhenAddressContainAnEmptyCountry() throws Exception {
        // given
        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid business address");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description, address, businessType(not exist).
     */
    @Test
    public void canNotCreateWhenBusinessTypeIsNotExist() throws Exception {
        // given
        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"example\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/businesses").cookie(cookie).
                contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid business type");
    }

    /**
     * Tests that an UNAUTHORIZED(401) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType but a wrong cookie.
     */
    @Test
    public void canNotCreateWhenDataValidAndCookieNotExists() throws Exception {
        // given
        payloadJson = "{" +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.empty());
        response = mvc.perform(post("/businesses").contentType(MediaType.APPLICATION_JSON)
                .content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a OK(200) status is received when the user id in the /businesses/{id} API endpoint does exist
     */
    @Test
    public void canRetrieveBusinessWhenBusinessDoesExist() throws Exception {
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"id\":" + id + "," +
                "\"administrators\":" + business.getAdministrators() + "," +
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
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        //when(business.getAdministrators()).thenReturn(List.of(user));
        response = mvc.perform(get(String.format("/businesses/%d", id)).cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString().replace("\\n", "").replace("\\", ""))
                .isEqualTo(expectedJson);
    }

    /**
     * Tests that a UNAUTHORIZED(401) status is received when cookie wrong
     */
    @Test
    public void canNotRetrieveBusinessWhenCookieNotExist() throws Exception {
        // given
        String nonExistingSessionUUID = User.generateSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", nonExistingSessionUUID);
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(nonExistingSessionUUID)).thenReturn(Optional.empty());
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        response = mvc.perform(
                get(String.format("/businesses/%d", business.getId())).cookie(cookie)
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT_ACCEPTABLE(406) status is received when the user id in the /businesses/{id} API endpoint does exist
     */
    @Test
    public void canNotRetrieveBusinessWhenBusinessDoesNotExist() throws Exception {
        // given
        int nonExistentBusinessId = 0;
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(nonExistentBusinessId)).thenReturn(Optional.empty());
        response = mvc.perform(
                get(String.format("/businesses/%d", nonExistentBusinessId)).cookie(cookie)
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }
}
