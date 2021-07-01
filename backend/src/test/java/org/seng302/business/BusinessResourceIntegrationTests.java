package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.model.Address;
import org.seng302.model.repository.AddressRepository;
import org.seng302.controller.BusinessResource;
import org.seng302.Main;
import org.seng302.model.Business;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class BusinessResourceIntegrationTests {

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

    private User anotherUser;

    private Business business;

    private Address address;

    @BeforeAll
    void setup() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
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
        anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        this.mvc = MockMvcBuilders.standaloneSetup(
                new BusinessResource(businessRepository, userRepository, addressRepository)
        ).build();
    }

    /**
     * test when business has been create, current user has been add to business's administrators
     * @throws Exception
     */
    @Test
    void setAdministratorComplete() throws Exception {
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
                        "90210",
                        "Ilam"
                ),
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );
        newBusiness.setId(3);
        newBusiness.addAdministrators(user);

        payloadJson = "{" +
                        "\"primaryAdministratorId\": " + user.getId() + "," +
                        "\"name\": \"Lumbridge General Stores\"," +
                        "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                        "\"address\": {" +
                                "\"streetNumber\": \"2/24\"," +
                                "\"streetName\": \"Ilam Road\"," +
                                "\"city\": \"Christchurch\"," +
                                "\"region\": \"Canterbury\"," +
                                "\"country\": \"New Zealand\"," +
                                "\"postcode\": \"90210\"," +
                                "\"suburb\": \"Ilam\"" +
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
     * test when business has been create, current user has been add to business's administrators
     * @throws Exception
     */
    @Test
    void setPrimaryAdministratorComplete() throws Exception {
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
                        "90210",
                        "Ilam"
                ),
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );
        newBusiness.setId(3);
        newBusiness.addAdministrators(user);

        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"Lumbridge General Stores\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                "\"streetNumber\": \"2/24\"," +
                "\"streetName\": \"Ilam Road\"," +
                "\"city\": \"Christchurch\"," +
                "\"region\": \"Canterbury\"," +
                "\"country\": \"New Zealand\"," +
                "\"postcode\": \"90210\"," +
                "\"suburb\": \"Ilam\"" +
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
        assertThat(newBusiness.getPrimaryAdministratorId()).isEqualTo(user.getId());
    }

//--------------------------------------------------/businesses--------------------------------------------------

    /**
     * Tests that an CREATED(201) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType and a create cookie belongs to an user.
     */
    @Test
    void canCreateWhenDataValidAndCookieExists() throws Exception {
        // given
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"Lumbridge General Stores\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                "\"streetNumber\": \"16/24\"," +
                "\"streetName\": \"Ilam Road\"," +
                "\"city\": \"Christchurch\"," +
                "\"region\": \"Canterbury\"," +
                "\"country\": \"New Zealand\"," +
                "\"postcode\": \"90211\"," +
                "\"suburb\": \"Ilam\"" +
                "}," +
                "\"businessType\": \"Accommodation and Food Services\"" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(businessRepository.save(any(Business.class))).thenReturn(business);
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
    void canNotCreateWhenNameEmpty() throws Exception {
        // given
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"," +
                    "\"suburb\": \"Ilam\"" +
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
    void canNotCreateWhenNameOnlySpace() throws Exception {
        // given
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"   \"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"," +
                    "\"suburb\": \"Ilam\"" +
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
    void canNotCreateWhenNameLengthLargerThan100() throws Exception {
        // given
        String aName = "a".repeat(101);
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"" + aName + "\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"," +
                    "\"suburb\": \"Ilam\"" +
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
    void canNotCreateWhenDescriptionLengthLargerThan600() throws Exception {
        // given
        String aDescription = "a".repeat(601);
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"" + aDescription + "\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"," +
                    "\"suburb\": \"Ilam\"" +
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
        assertThat(response.getErrorMessage()).isEqualTo("Invalid business description");
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a payload to the /businesses API endpoint
     * that contains business name, description, address(length = 256), businessType.
     */
    @Test
    void canNotCreateWhenAddressLengthLargerThan255() throws Exception {
        // given
        String aString = "a".repeat(256);
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"" + aString + "\"," +
                    "\"streetName\": \"" + aString + "\"," +
                    "\"city\": \"" + aString + "\"," +
                    "\"region\": \"" + aString + "\"," +
                    "\"country\": \"" + aString + "\"," +
                    "\"postcode\": \"9\"," +
                    "\"suburb\": \"Ilam\"" +
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
    void canNotCreateWhenAddressContainAnEmptyCountry() throws Exception {
        // given
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"\"," +
                    "\"postcode\": \"90210\"," +
                    "\"suburb\": \"Ilam\"" +
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
    void canNotCreateWhenBusinessTypeIsNotExist() throws Exception {
        // given
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"Lumbridge General Store\",\n" +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"," +
                    "\"suburb\": \"Ilam\"" +
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
    void canNotCreateWhenDataValidAndCookieNotExists() throws Exception {
        // given
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + user.getId() + "," +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                    "\"streetNumber\": \"2/24\"," +
                    "\"streetName\": \"Ilam Road\"," +
                    "\"city\": \"Christchurch\"," +
                    "\"region\": \"Canterbury\"," +
                    "\"country\": \"New Zealand\"," +
                    "\"postcode\": \"90210\"," +
                    "\"suburb\": \"Ilam\"" +
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
     * Tests that an Forbidden(403) status is received when sending a create payload to the /businesses API endpoint
     * that contains business name, description, address, businessType but a wrong primaryAdministratorId.
     */
    @Test
    void canNotCreateWhenDataValidAndPrimaryAdministratorIdDifferent() throws Exception {
        // given
        payloadJson = "{" +
                "\"primaryAdministratorId\": " + (user.getId()+1) + "," +
                "\"name\": \"Lumbridge General Store\"," +
                "\"description\": \"A one-stop shop for all your adventuring needs\"," +
                "\"address\": {" +
                "\"streetNumber\": \"2/24\"," +
                "\"streetName\": \"Ilam Road\"," +
                "\"city\": \"Christchurch\"," +
                "\"region\": \"Canterbury\"," +
                "\"country\": \"New Zealand\"," +
                "\"postcode\": \"90210\"," +
                "\"suburb\": \"Ilam\"" +
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
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

//--------------------------------------------------/businesses/{id}--------------------------------------------------

    /**
     * Tests that a OK(200) status is received when the user id in the /businesses/{id} API endpoint does exist, and
     * primary administrator id will be display(As current user is administrator of this business).
     */
    @Test
    void administratorCanRetrieveBusinessWhenBusinessDoesExist() throws Exception {
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"id\":" + id + "," +
                "\"administrators\":" + "[{\"id\":1," +
                    "\"firstName\":\"testfirst\"," +
                    "\"lastName\":\"testlast\"," +
                    "\"middleName\":\"testmiddle\"," +
                    "\"nickname\":\"testnick\"," +
                    "\"bio\":\"testbiography\"," +
                    "\"email\":\"testemail@email.com\"," +
                    "\"created\":\"2021-02-02T00:00\"," +
                    "\"role\":\"USER\"," +
                    "\"businessesAdministered\":[" +
                    "null" +
                    "]," +
                    "\"dateOfBirth\":\"2007-02-02\"," +
                    "\"phoneNumber\":\"0271316\"," +
                    "\"homeAddress\":{" +
                        "\"streetNumber\":\"3/24\"," +
                        "\"streetName\":\"Ilam Road\"," +
                        "\"city\":\"Christchurch\"," +
                        "\"region\":\"Canterbury\"," +
                        "\"country\":\"New Zealand\"," +
                        "\"postcode\":\"90210\"," +
                        "\"suburb\":\"Ilam\"" +
                        "}" +
                "}]" + "," +
                "\"primaryAdministratorId\":" + business.getPrimaryAdministratorId() + "," +
                "\"name\":\"" + business.getName() + "\"," +
                "\"description\":\"" + business.getDescription() + "\"," +
                "\"address\":{" +
                    "\"streetNumber\":\"" + address.getStreetNumber() + "\"," +
                    "\"streetName\":\"" + address.getStreetName() + "\"," +
                    "\"city\":\"" + address.getCity() + "\"," +
                    "\"region\":\"" + address.getRegion() + "\"," +
                    "\"country\":\"" + address.getCountry() + "\"," +
                    "\"postcode\":\"" + address.getPostcode() + "\"," +
                    "\"suburb\":\"" + address.getSuburb() + "\"" +
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
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a OK(200) status is received when the user id in the /businesses/{id} API endpoint does exist, and
     * primary administrator id will be display(As current user is not administrator of this business and not a DGAA).
     */
    @Test
    void nonAdministratorCanRetrieveBusinessWhenBusinessDoesExist() throws Exception {
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"id\":" + id + "," +
                "\"administrators\":" + "[{\"id\":1," +
                "\"firstName\":\"testfirst\"," +
                "\"lastName\":\"testlast\"," +
                "\"middleName\":\"testmiddle\"," +
                "\"nickname\":\"testnick\"," +
                "\"bio\":\"testbiography\"," +
                "\"email\":\"testemail@email.com\"," +
                "\"created\":\"2021-02-02T00:00\"," +
                "\"role\":\"USER\"," +
                "\"businessesAdministered\":[" +
                "null" +
                "]," +
                "\"dateOfBirth\":\"2007-02-02\"," +
                "\"phoneNumber\":\"0271316\"," +
                "\"homeAddress\":{" +
                "\"streetNumber\":\"3/24\"," +
                "\"streetName\":\"Ilam Road\"," +
                "\"city\":\"Christchurch\"," +
                "\"region\":\"Canterbury\"," +
                "\"country\":\"New Zealand\"," +
                "\"postcode\":\"90210\"," +
                "\"suburb\":\"Ilam\"" +
                "}" +
                "}]" + "," +
                "\"primaryAdministratorId\":null," +
                "\"name\":\"" + business.getName() + "\"," +
                "\"description\":\"" + business.getDescription() + "\"," +
                "\"address\":{" +
                "\"streetNumber\":\"" + address.getStreetNumber() + "\"," +
                "\"streetName\":\"" + address.getStreetName() + "\"," +
                "\"city\":\"" + address.getCity() + "\"," +
                "\"region\":\"" + address.getRegion() + "\"," +
                "\"country\":\"" + address.getCountry() + "\"," +
                "\"postcode\":\"" + address.getPostcode() + "\"," +
                "\"suburb\":\"" + address.getSuburb() + "\"" +
                "}," +
                "\"businessType\":\"" + business.getBusinessType() + "\"," +
                "\"created\":\"" + business.getCreated() + "\"}";
        sessionToken = anotherUser.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        //when(business.getAdministrators()).thenReturn(List.of(user));
        response = mvc.perform(get(String.format("/businesses/%d", id)).cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a OK(200) status is received when the user id in the /businesses/{id} API endpoint does exist, and
     * primary administrator id will be display(As current user is DGAA).
     */
    @Test
    void DGAACanRetrieveBusinessWhenBusinessDoesExist() throws Exception {
        User user = new User("testfirst",
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
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        // given
        id = business.getId();
        expectedJson = "{" +
                "\"id\":" + id + "," +
                "\"administrators\":" + "[{\"id\":1," +
                "\"firstName\":\"testfirst\"," +
                "\"lastName\":\"testlast\"," +
                "\"middleName\":\"testmiddle\"," +
                "\"nickname\":\"testnick\"," +
                "\"bio\":\"testbiography\"," +
                "\"email\":\"testemail@email.com\"," +
                "\"created\":\"2021-02-02T00:00\"," +
                "\"role\":\"USER\"," +
                "\"businessesAdministered\":[" +
                "null" +
                "]," +
                "\"dateOfBirth\":\"2007-02-02\"," +
                "\"phoneNumber\":\"0271316\"," +
                "\"homeAddress\":{" +
                "\"streetNumber\":\"3/24\"," +
                "\"streetName\":\"Ilam Road\"," +
                "\"city\":\"Christchurch\"," +
                "\"region\":\"Canterbury\"," +
                "\"country\":\"New Zealand\"," +
                "\"postcode\":\"90210\"," +
                "\"suburb\":\"Ilam\"" +
                "}" +
                "}]" + "," +
                "\"primaryAdministratorId\":" + business.getPrimaryAdministratorId() + "," +
                "\"name\":\"" + business.getName() + "\"," +
                "\"description\":\"" + business.getDescription() + "\"," +
                "\"address\":{" +
                "\"streetNumber\":\"" + address.getStreetNumber() + "\"," +
                "\"streetName\":\"" + address.getStreetName() + "\"," +
                "\"city\":\"" + address.getCity() + "\"," +
                "\"region\":\"" + address.getRegion() + "\"," +
                "\"country\":\"" + address.getCountry() + "\"," +
                "\"postcode\":\"" + address.getPostcode() + "\"," +
                "\"suburb\":\"" + address.getSuburb() + "\"" +
                "}," +
                "\"businessType\":\"" + business.getBusinessType() + "\"," +
                "\"created\":\"" + business.getCreated() + "\"}";

        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        response = mvc.perform(get(String.format("/businesses/%d", id)).cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a UNAUTHORIZED(401) status is received when cookie wrong
     */
    @Test
    void canNotRetrieveBusinessWhenCookieNotExist() throws Exception {
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
    void canNotRetrieveBusinessWhenBusinessDoesNotExist() throws Exception {
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

//-----------------------------------------/businesses/{id}/makeAdministrator-----------------------------------------

    /**
     * Tests that an OK(200) status is received when sending a non-administrator(for this business) userId payload to
     * the /businesses/{id}/makeAdministrator API endpoint. And current session token is for an administrator of this
     * business.
     * @throws Exception
     */
    @Test
    void anBusinessAdministratorCanMakeUserBecomeAdministrator() throws Exception {
        // given
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);

        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add business to user object()
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/makeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        System.out.println(response.getErrorMessage());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that an OK(200) status is received when sending a non-administrator(for this business) userId payload to
     * the /businesses/{id}/makeAdministrator API endpoint. And current session token is for a DGAA.
     * @throws Exception
     */
    @Test
    void aDGAACanMakeUserBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        user.setRole(Role.DEFAULTGLOBALAPPLICATIONADMIN);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/makeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a not exist userId payload to the
     * /businesses/{id}/makeAdministrator API endpoint. And current session token is for an administrator of this
     * business.
     * @throws Exception
     */
    @Test
    void anBusinessAdministratorCanNotMakeANotExistUserBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":0" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add business to user object()
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/makeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a administrator(for this business) userId payload
     * to the /businesses/{id}/makeAdministrator API endpoint. And current session token is for an administrator of
     * this business.
     * @throws Exception
     */
    @Test
    void anBusinessAdministratorCanNotMakeOtherAdministratorBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add otherUser to administrator of business
        business.addAdministrators(anotherUser);

        //add business to user and otherUser
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);
        anotherUser.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/makeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that an UNAUTHORIZED(401) status is received when sending a non-administrator(for this business) userId
     * payload to the /businesses/{id}/makeAdministrator API endpoint. But session token is missing.
     * @throws Exception
     */
    @Test
    void whenSessionTokenMissing_MakingUserBecomeAdministratorNotWork() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();

        //add business to user object()
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/makeAdministrator", id))
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that an FORBIDDEN(403) status is received when sending a userId payload to the
     * /businesses/{id}/makeAdministrator API endpoint. But current session token is for an normal user.
     * @throws Exception
     */
    @Test
    void aNormalUserCanNotMakeUserBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = anotherUser.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //delete 'user' in 'business'
        business.setAdministrators(new ArrayList<>());

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(anotherUser));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/makeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(business.getAdministrators().size()).isZero();
    }

    /**
     * Tests that an NOT_ACCEPTABLE(406) status is received when sending a non-administrator(for this business) userId payload to
     * the /businesses/{id}/makeAdministrator API endpoint. And current session token is for an administrator of this
     * business. But given business not exist.
     * @throws Exception
     */
    @Test
    void CanNotMakeUserBecomeAdministratorWhenBusinessNotExist() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = 0;
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add business to user object()
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/makeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

//-----------------------------------------/businesses/{id}/removeAdministrator-----------------------------------------

    /**
     * Tests that an OK(200) status is received when sending a non-administrator(for this business) userId payload to
     * the /businesses/{id}/removeAdministrator API endpoint. And current session token is for an administrator of this
     * business.
     * @throws Exception
     */
    @Test
    void aBusinessAdministratorCanRemoveUserBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add business to user and anotherUser
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);
        anotherUser.setBusinessesAdministeredObjects(businessesAdministeredObjects);
        //add user and anotherUser to business
        business.addAdministrators(anotherUser);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that an OK(200) status is received when sending a non-administrator(for this business) userId payload to
     * the /businesses/{id}/removeAdministrator API endpoint. And current session token is for a DGAA.
     * @throws Exception
     */
    @Test
    void aDGAACanRemoveUserBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        user.setRole(Role.DEFAULTGLOBALAPPLICATIONADMIN);
        //add business to user and anotherUser
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        anotherUser.setBusinessesAdministeredObjects(businessesAdministeredObjects);
        //add user and anotherUser to business
        business.addAdministrators(anotherUser);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        System.out.println(response.getErrorMessage());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a not exist userId payload to the
     * /businesses/{id}/removeAdministrator API endpoint. And current session token is for an administrator of this
     * business.
     * @throws Exception
     */
    @Test
    void anBusinessAdministratorCanNotRemoveANotExistUserBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":0" +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add business to user object()
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that an BAD_REQUEST(400) status is received when sending a administrator(for this business) userId payload
     * to the /businesses/{id}/removeAdministrator API endpoint. And current session token is for an administrator of
     * this business.
     * @throws Exception
     */
    @Test
    void anBusinessAdministratorCanNotRemoveOtherAdministratorBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add business to user and otherUser
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);
        anotherUser.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that an UNAUTHORIZED(401) status is received when sending a non-administrator(for this business) userId
     * payload to the /businesses/{id}/removeAdministrator API endpoint. But session token is missing.
     * @throws Exception
     */
    @Test
    void whenSessionTokenMissing_RemovingUserBecomeAdministratorNotWork() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();

        //add business to user object()
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id))
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that an FORBIDDEN(403) status is received when sending a userId payload to the
     * /businesses/{id}/removeAdministrator API endpoint. But current session token is for an normal user.
     * @throws Exception
     */
    @Test
    void aNormalUserCanNotRemoveUserBecomeAdministrator() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = anotherUser.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //delete 'user' in 'business'
        business.setAdministrators(new ArrayList<>());

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(anotherUser));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(business.getAdministrators().size()).isZero();
    }

    /**
     * Tests that an FORBIDDEN(403) status is received when sending a userId payload to the
     * /businesses/{id}/removeAdministrator API endpoint. But select user is it's self.
     * @throws Exception
     */
    @Test
    void aAdministratorCanNotRemoveItsSelf() throws Exception {
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = business.getId();
        expectedJson = "{" +
                "\"userId\":" + user.getId() +
                "}";
        sessionToken = anotherUser.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //delete 'user' in 'business'

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(business.getAdministrators().size()).isEqualTo(1);
    }

    /**
     * Tests that an NOT_ACCEPTABLE(406) status is received when sending a non-administrator(for this business) userId payload to
     * the /businesses/{id}/removeAdministrator API endpoint. And current session token is for an administrator of this
     * business. But given business not exist.
     * @throws Exception
     */
    @Test
    void CanNotRemoveUserBecomeAdministratorWhenBusinessNotExist() throws Exception {
        User anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        User user = new User("testfirst",
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
        Business business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        // given
        id = 0;
        expectedJson = "{" +
                "\"userId\":" + anotherUser.getId() +
                "}";
        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        //add business to user object()
        List<Business> businessesAdministeredObjects = user.getBusinessesAdministeredObjects();
        businessesAdministeredObjects.add(business);
        user.setBusinessesAdministeredObjects(businessesAdministeredObjects);

        // when
        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));

        response = mvc.perform(put(String.format("/businesses/%d/removeAdministrator", id)).cookie(cookie)
                .content(expectedJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }
}
