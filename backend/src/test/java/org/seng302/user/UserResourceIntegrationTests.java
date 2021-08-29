package org.seng302.user;

import org.junit.jupiter.api.*;
import org.seng302.Validation;
import org.seng302.model.Address;
import org.seng302.model.repository.AddressRepository;
import org.seng302.controller.UserResource;
import org.seng302.Main;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * UserResource test class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class UserResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
                                        "\"password\": \"%s\"}";

    private final String expectedUserJson = "{\"id\":%d," +
                                    "\"firstName\":\"%s\"," +
                                    "\"lastName\":\"%s\"," +
                                    "\"middleName\":\"%s\"," +
                                    "\"nickname\":\"%s\"," +
                                    "\"bio\":\"%s\"," +
                                    "\"email\":\"%s\"," +
                                    "\"created\":\"%s" + "\"," +
                                    "\"role\":%s," +
                                    "\"businessesAdministered\":%s," +
                                    "\"dateOfBirth\":\"%s\"," +
                                    "\"phoneNumber\":\"%s\"," +
                                    "\"homeAddress\":%s"+
                                "}";

    private final String expectedSecureUserJson = "{\"id\":%d," +
            "\"firstName\":\"%s\"," +
            "\"lastName\":\"%s\"," +
            "\"middleName\":\"%s\"," +
            "\"nickname\":\"%s\"," +
            "\"bio\":\"%s\"," +
            "\"email\":\"%s\"," +
            "\"created\":\"%s" + "\"," +
            "\"role\":%s," +
            "\"businessesAdministered\":%s," +
            "\"homeAddress\":%s"+
            "}";

    private final String expectedUserIdJson = "{\"userId\":%s}";

    private final String modifiedUserPayload = "{\n" +
            "\"firstName\": \"%S\",\n" +
            "\"lastName\": \"%S\",\n" +
            "\"middleName\": \"%S\",\n" +
            "\"nickname\": \"%S\",\n" +
            "\"bio\": \"%S\",\n" +
            "\"email\": \"%S\",\n" +
            "\"dateOfBirth\": \"%S\",\n" +
            "\"phoneNumber\": \"%S\",\n" +
            "\"homeAddress\": {\n" +
            "\"streetNumber\": \"%s\",\n" +
            "\"streetName\": \"%S\",\n" +
            "\"suburb\": \"%S\",\n" +
            "\"city\": \"%S\",\n" +
            "\"region\": \"%S\",\n" +
            "\"country\": \"%s\",\n" +
            "\"postcode\": \"%s\"\n" +
            "},\n" +
            "\"password\": \"%s\"\n" +
            "}";

    private MockHttpServletResponse response;

    private String expectedJson;

    private User dGAA;

    private User user;

    private User anotherUser;

    private Address address;

    private User searchUser1;
    private User searchUser2;
    private User searchUser3;
    private User searchUser4;
    private User searchUser5;
    private Address address1;
    private Address address2;
    private Address address3;
    private Address address4;
    private Address address5;

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
        dGAA = new User(
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
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        dGAA.setId(1);
        dGAA.setSessionUUID(User.generateSessionUUID());

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
                        Role.GLOBALAPPLICATIONADMIN);
        user.setId(2);
        user.setSessionUUID(User.generateSessionUUID());
        anotherUser = new User ("first",
                                "last",
                                "middle",
                                "nick",
                                "bio",
                                "example@example.com",
                                LocalDate.of(2021, 1, 1).minusYears(13),
                                "123456789",
                                address,
                                "Password123!",
                                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                            LocalTime.of(0, 0)),
                                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
       // this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();

        //test users for searching for user by name

        address1 = new Address("129",
                "Mastic Trail",
                "Frank Sound",
                "Cayman Islands",
                "Caribbean",
                "North America",
                "Pirate Cove");

        searchUser1= new User(
                "Alex",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "test@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2000, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        searchUser1.setId(4);

        address2 = new Address("80416", "", "", "Jon Loop", "Shaanxi", "China", "Barryville");

        searchUser2 = new User(
                "Chad",
                "Taylor",
                "S",
                "Cha",
                "Biography123",
                "chad.taylor@example.com",
                LocalDate.of(2008, 2, 2),
                "0271316678",
                address2,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2000, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        searchUser2.setId(5);

        address3 = new Address("9205", "", "Monique Vista", "Bururi", "Bigomogomo", "Africa", "Buri");

        searchUser3 = new User(
                "Naomi",
                "Wilson",
                "I",
                "Gm",
                "Biography",
                "naomi.wilson@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address3,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        searchUser3.setId(6);

        address4 = new Address("240", "", "", "Bernhard Run", "Southland", "New Zealand", "Ilam");

        searchUser4 = new User(
                "Seth",
                "Murphy",
                "Tea",
                "S",
                "Biography",
                "seth.murphy@example.com",
                LocalDate.of(2008, 2, 2),
                "027188316",
                address4,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        searchUser4.setId(7);

        address5 = new Address("186", "Simpsons Road", "", "Ashburton", "Canterbury", "New Zealand", "Ilam");

        searchUser5 = new User(
                "Minttu",
                "Wainio",
                "A",
                "Min",
                "Biography",
                "minttu.wainio@example.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address5,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        searchUser5.setId(8);

        // initializes the MockMVC object and tells it to use the userRepository
        this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();

    }

    /**
     * Tests that an OK status is received when sending a login payload to the /login API endpoint
     * that contains an email that belongs to an existing user as well as the correct password
     */
    @Test
    void canLoginWhenUserExistsAndPasswordCorrect() throws Exception {
        // when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "Testpassword123!")))
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(user.getSessionUUID());
        assertThat(response.getCookie("JSESSIONID").getMaxAge()).isEqualTo(28800);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a login payload to the /login API endpoint
     * that contains an email that does not belong to an existing user
     */
    @Test
    void cantLoginWhenUserDoesntExist() throws Exception {
        // given
        expectedJson = "";

        // when
        response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, "dsjhsd@email.com", "testpassword")))
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a login payload to the /login API endpoint
     * that contains an email that belongs to an existing user but an incorrect password
     */
    @Test
    void cantLoginWhenUserExistsButPasswordIncorrect() throws Exception {
        // given
        expectedJson = "";

        // when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "asbhash")))
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that an OK status is received when making a POST to the /logout API endpoint
     * with an existing JSESSIONID cookie
     */
    @Test
    void canLogoutWhenCookieExists() throws Exception {
        // when
        response = mvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(user.getSessionUUID());
        assertThat(response.getCookie("JSESSIONID").getMaxAge()).isZero();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that an OK status is received when making a POST to the /logout API endpoint
     * with no existing JSESSIONID cookie
     */
    @Test
    void canLogoutWhenCookieDoesNotExist() throws Exception {
        // when
        response = mvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that a CREATED status is received when sending a registration payload to the /users API endpoint
     * that contains an email that doesn't belong to an existing user and contains valid data
     */
    @Test
    void canRegisterWhenUserDoesntExistAndDataValid() throws Exception {
        // given
        User newUser = new User("Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                          "bob@email.com", LocalDate.of(2000, user.getId(), dGAA.getId()), "01234567",
                     address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        newUser.setId(4);
        newUser.setSessionUUID(User.generateSessionUUID());
        given(userRepository.findByEmail(newUser.getEmail())).willReturn(java.util.Optional.empty());

        String registerJson = "{\"firstName\": \"Bob\", " +
                "\"lastName\": \"Boberson\", " +
                "\"middleName\": \"Robert\", " +
                "\"nickname\": \"Bobert\", " +
                "\"bio\": \"Bobsbio\", " +
                "\"email\": \"bob@email.com\", " +
                "\"dateOfBirth\": \"2000-02-01\", " +
                "\"phoneNumber\": \"01234567\", " +
                "\"homeAddress\": {" +
                        "\"streetNumber\": \"3/24\"," +
                        "\"streetName\": \"Ilam Road\"," +
                        "\"city\": \"Christchurch\"," +
                        "\"region\": \"Canterbury\"," +
                        "\"country\": \"New Zealand\"," +
                        "\"postcode\": \"90210\"," +
                        "\"suburb\": \"Ilam\"" +
                " }, " +
                "\"password\": \"Testpassword123!\"}";

        // when
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, 4));
        assertThat(response.getCookie("JSESSIONID").getValue()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a CONFLICT status is received when sending a registration payload to the /users API endpoint
     * that contains an email that belongs to an existing user but contains valid data
     */
    @Test
    void cantRegisterWhenUserAlreadyExistsButDataValid() throws Exception {
        // given
        String registerJson = "{\"firstName\": \"testfirstname\", " +
                "\"lastName\": \"testlastname\", " +
                "\"middleName\": \"testmiddlename\", " +
                "\"nickname\": \"testnickname\", " +
                "\"bio\": \"testbio\", " +
                "\"email\": \"%s\", " +
                "\"dateOfBirth\": \"2000-02-01\", " +
                "\"phoneNumber\": \"01234567\", " +
                "\"homeAddress\": {" +
                        "\"streetNumber\": \"3/24\"," +
                        "\"streetName\": \"Ilam Road\"," +
                        "\"city\": \"Christchurch\"," +
                        "\"region\": \"Canterbury\"," +
                        "\"country\": \"New Zealand\"," +
                        "\"postcode\": \"90210\"," +
                        "\"suburb\": \"Ilam\"" +
                " }, " +
                "\"password\": \"testpassword\"}";
        expectedJson = "";

        // when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(String.format(registerJson, user.getEmail())))
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a registration payload to the /users API endpoint
     * that contains an email that doesn't belong to an existing user but contains invalid data
     */
    @Test
    void cantRegisterWhenUserDoesntExistButDataInvalid() throws Exception {
        // given
        String registerJson = "{\"firstName\": \"\", " +
                "\"lastName\": \"testlastname\", " +
                "\"middleName\": \"testmiddlename\", " +
                "\"nickname\": \"testnickname\", " +
                "\"bio\": \"testbio\", " +
                "\"email\": \"test@email.com\", " +
                "\"dateOfBirth\": \"2000-02-01\", " +
                "\"phoneNumber\": \"01234567\", " +
                "\"homeAddress\": {" +
                        "\"streetNumber\": \"3/24\"," +
                        "\"streetName\": \"Ilam Road\"," +
                        "\"city\": \"Christchurch\"," +
                        "\"region\": \"Canterbury\"," +
                        "\"country\": \"New Zealand\"," +
                        "\"postcode\": \"90210\"," +
                        "\"suburb\": \"Ilam\"" +
                " }, " +
                "\"password\": \"testpassword\"}";
        expectedJson = "";

        // when
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a CONFLICT status is received when sending a registration payload to the /users API endpoint
     * that contains an email that belongs to an existing user and contains invalid data
     */
    @Test
    void cantRegisterWhenUserDoesExistAndDataInvalid() throws Exception {
        // given
        String registerJson = "{\"firstName\": \"\", " +
                "\"lastName\": \"testlastname\", " +
                "\"middleName\": \"testmiddlename\", " +
                "\"nickname\": \"testnickname\", " +
                "\"bio\": \"testbio\", " +
                "\"email\": \"%s\", " +
                "\"dateOfBirth\": \"2000-02-01\", " +
                "\"phoneNumber\": \"01234567\", " +
                "\"homeAddress\": {" +
                        "\"streetNumber\": \"3/24\"," +
                        "\"streetName\": \"Ilam Road\"," +
                        "\"city\": \"Christchurch\"," +
                        "\"region\": \"Canterbury\"," +
                        "\"country\": \"New Zealand\"," +
                        "\"postcode\": \"90210\"," +
                        "\"suburb\": \"Ilam\"" +
                " }, " +
                "\"password\": \"testpassword\"}";
        expectedJson = "";

        // when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(String.format(registerJson, user.getEmail())))
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Tests that an OK status and a user is received when the user ID in the /users/{id} API endpoint exists
     * Test specifically for when the cookie contains an ID belonging to a DGAA
     */
    @Test
    void canRetrieveUserWhenUserExistsWithDgaaCookie() throws Exception {
        // given
        expectedJson = String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(), "\"" + user.getRole() + "\"", "[null]",  user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(
                get(String.format("/users/%d", user.getId())).cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a user is received when the user ID in the /users/{id} API endpoint exists
     * Test specifically for when the cookie contains an ID belonging to a GAA
     */
    @Test
    void canRetrieveUserWhenUserExistsWithGaaCookie() throws Exception {
        // given
        expectedJson = String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(),
                ("\"" + user.getRole() + "\""), "[null]", user.getDateOfBirth(), user.getPhoneNumber(),  user.getHomeAddress());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(
                get(String.format("/users/%d", user.getId())).cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a user is received when the user ID in the /users/{id} API endpoint exists
     * Test specifically for when the cookie contains an ID belonging to a GAA
     */
    @Test
    void canRetrieveUserWhenUserExistsWithUserCookie() throws Exception {
        String expectedUserJson = "{\"id\":%d," +
                "\"firstName\":\"%s\"," +
                "\"lastName\":\"%s\"," +
                "\"middleName\":\"%s\"," +
                "\"nickname\":\"%s\"," +
                "\"bio\":\"%s\"," +
                "\"email\":\"%s\"," +
                "\"created\":\"%s" + "\"," +
                "\"role\":%s," +
                "\"businessesAdministered\":%s," +
                "\"homeAddress\":%s"+
                "}";


        // given
        expectedJson = String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(), null, "[null]", user.getHomeAddress().toSecureString());

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(
                get(String.format("/users/%d", user.getId())).cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an UNAUTHORIZED status is received when the user ID in the /users/{id} API endpoint exists
     * but the cookie contains a non-existing ID
     */
    @Test
    void cantRetrieveUserWhenUserExistsWithNonExistingIdCookie() throws Exception {
        // given
        String nonExistingSessionUUID = User.generateSessionUUID();
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(nonExistingSessionUUID)).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(
                get(String.format("/users/%d", user.getId())).cookie(new Cookie("JSESSIONID", nonExistingSessionUUID)))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an UNAUTHORIZED status is received when the user ID in the /users/{id} API endpoint exists
     * but there is no cookie
     */
    @Test
    void cantRetrieveUserWhenUserExistsWithNoCookie() throws Exception {
        // given
        expectedJson = "";

        // when
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(
                get(String.format("/users/%d", user.getId()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when the user ID in the /users/{id} API endpoint does not exist
     */
    @Test
    void cantRetrieveUserWhenUserDoesntExist() throws Exception {
        // given
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(0)).thenReturn(Optional.empty());
        response = mvc.perform(
                get(String.format("/users/%d", 0)).cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }


    /* ------------------------------------- (New) Tests for Searching for User by Name ----------------------------- */

    /**
     * Tests that an OK status is received when searching for a user using the /users/search API endpoint and that
     * the JSON response is equal to the user searched for. The user is searched for using the following orders of the
     * names: first, last, middle, first middle last, first last.
     * Test specifically for when the user searching for a user is a DGAA.
     */
    @Test
    void canSearchUsersWhenUserExistsWithDgaaCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "[" + String.format(expectedSecureUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(), "\"" +
                user.getRole() + "\"", "[null]",  user.getHomeAddress().toSecureString()) + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        Page<User> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(0)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(1)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(2)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(3)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(4)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(5)), paging)).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                            .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an OK status is received when searching for a user using the /users/search API endpoint and that
     * the JSON response is equal to the user searched for. The user is searched for using the following orders of the
     * names: first, last, middle, first middle last, first last.
     * Test specifically for when the order by and page params provided are valid.
     */
    @Test
    void canSearchUsersWhenUserExistsWithValidOrderByAndPageParams() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "[" + String.format(expectedSecureUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(), "\"" +
                        user.getRole() + "\"", "[null]",  user.getHomeAddress().toSecureString()) + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        Page<User> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(0)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(1)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(2)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(3)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(4)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(5)), paging)).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                            .param("orderBy", "fullNameASC")
                            .param("page", "0")
                            .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an OK status is received when searching for a user using the /users/search API endpoint and that
     * the JSON response is equal to the user searched for. The user is searched for using the following orders of the
     * names: first, last, middle, first middle last, first last.
     * Test specifically for when the user searching for a user is a GAA.
     */
    @Test
    void canSearchUsersWhenUserExistsWithGaaCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "[" + String.format(expectedSecureUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(),
                null, "[null]",  user.getHomeAddress().toSecureString()) + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        Page<User> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(0)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(1)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(2)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(3)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(4)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(5)), paging)).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                            .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an OK status is received when searching for a user using the /users/search API endpoint and that
     * the JSON response is equal to the user searched for. The user is searched for using the following orders of the
     * names: first, last, middle, first middle last, first last.
     * Test specifically for when the user searching for a user is a USER.
     */
    @Test
    void canSearchUsersWhenUserExistsWithUserCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "[" + String.format(expectedSecureUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(),
                null, "[null]",  user.getHomeAddress().toSecureString()) + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        Page<User> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(0)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(1)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(2)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(3)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(4)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(5)), paging)).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                            .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an OK status but an empty response is received when searching for a user that does not exist using
     * the /users/search API endpoint. The user (that does not exist by the names searched for) is searched for using
     * the following orders of the names: first, last, middle, first middle last, first last.
     */
    @Test
    void emptySearchUsersWhenUserDoesntExist() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "notFirst",
                "notLast",
                "notMiddle",
                "notNick",
                "notFirst notMiddle notLast",
                "notFirst notLast"
        );
        expectedJson = "[]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of();
        Page<User> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(0)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(1)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(2)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(3)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(4)), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList(searchQueryList.get(5)), paging)).thenReturn(pagedResponse);
        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                    .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that a BAD_REQUEST status is received when searching for a user using the /users/search API endpoint
     * when the order by param is invalid.
     * Test specifically for when the order by param provided is invalid.
     */
    @Test
    void cantSearchUsersWithInvalidOrderByParam() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                            .param("orderBy", "a")
                            .param("page", "0")
                            .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that a BAD_REQUEST status is received when searching for a user using the /users/search API endpoint
     * when the page param is invalid.
     * Test specifically for when the page param provided is invalid.
     */
    @Test
    void cantSearchUsersWithInvalidPageParam() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                            .param("orderBy", "fullNameASC")
                            .param("page", "a")
                            .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an UNAUTHORIZED status is received when searching for a user using the /users/search API endpoint
     * when the cookie contains a non-existing ID
     */
    @Test
    void cantSearchUsersWithNonExistingIdCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        when(userRepository.findBySessionUUID("0")).thenReturn(Optional.empty());

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", "0"))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an UNAUTHORIZED status is received when searching for a user using the /users/search API endpoint
     * when there is no cookie
     */
    @Test
    void cantSearchUsersWithNoCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an OK status is received when searching for a user using the /users/search API endpoint and that
     * the JSON response is equal to the users searched for. This test tests specifically for more complex search queries
     * i.e those that contain AND and OR operators.
     */
    @Test
    void canSearchUsersWithComplexQueriesWhenUsersExists() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST OR ALEX",
                "testfirst or alex",
                "TESTFIRST AND TESTLAST OR alex",
                "TESTFIRST OR TESTLAST OR Alex",
                "TESTFIRST and TESTLAST or ALEX"
        );

        expectedJson = "[" +
                String.format(expectedSecureUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                        user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getCreated(),
                        null, "[null]",  user.getHomeAddress().toSecureString()) + "," +
                String.format(expectedSecureUserJson, searchUser1.getId(), searchUser1.getFirstName(),
                        searchUser1.getLastName(), searchUser1.getMiddleName(), searchUser1.getNickname(),
                        searchUser1.getBio(), searchUser1.getEmail(), searchUser1.getCreated(),
                        null, "[null]",  searchUser1.getHomeAddress().toSecureString()) +
                "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user, searchUser1);
        Page<User> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("firstName").ignoreCase()).and(Sort.by(Sort.Order.asc("middleName").ignoreCase())).and(Sort.by(Sort.Order.asc("lastName").ignoreCase())).and(Sort.by(Sort.Order.asc("email").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findAllUsersByNames(Arrays.asList("TESTFIRST", "ALEX"), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList("testfirst", "alex"), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList("TESTFIRST TESTLAST", "alex"), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList("TESTFIRST", "TESTLAST", "Alex"), paging)).thenReturn(pagedResponse);
        when(userRepository.findAllUsersByNames(Arrays.asList("TESTFIRST TESTLAST", "ALEX"), paging)).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery)
                            .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /* ------------------------------------- Make/Revoke Admin Tests ------------------------------------- */

    /**
     * Test that an OK(200) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a DGAA cookie.
     * @throws Exception thrown by MockMvc
     */
    @Test
    void canChangeUserToGaaWithDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", anotherUser.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(userRepository.findById(anotherUser.getId()).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);

        // cleanup
        anotherUser.setRole(Role.USER);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a GAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeUserToGaaWithGaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", user.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", anotherUser.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(anotherUser.getId()).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a USER cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeUserToGaaWithUserCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", anotherUser.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", anotherUser.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(anotherUser.getId()).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an UNAUTHORIZED(401) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a non-existing ID in the cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeUserToGaaWithNonExistingIdCookie() throws Exception {
        // given
        String nonExistingSessionUUID = User.generateSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", nonExistingSessionUUID);

        // when
        when(userRepository.findBySessionUUID(nonExistingSessionUUID)).thenReturn(Optional.empty());
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", anotherUser.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(userRepository.findById(anotherUser.getId()).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an UNAUTHORIZED(401) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with no cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeUserToGaaWithNoCookie() throws Exception {
        // when
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", anotherUser.getId()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(userRepository.findById(anotherUser.getId()).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an NOT_ACCEPTABLE(406) status is received when sending a non-existing id to /users/{id}/makeAdmin
     * API endpoint with a DGAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeUserToGaaWithNonExistingUserAndDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(0)).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", 0))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a GAA id to /users/{id}/makeAdmin API endpoint
     * with a DGAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeGaaToGaaWithDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", user.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(user.getId()).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a DGAA id to /users/{id}/makeAdmin API endpoint
     * with a DGAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeDgaaToGaaWithDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(dGAA.getId())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(put(String.format("/users/%d/makeAdmin", dGAA.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(dGAA.getId()).get().getRole()).isEqualTo(Role.DEFAULTGLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an OK(200) status is received when sending a GAA id to /users/{id}/revokeAdmin API endpoint
     * with a DGAA cookie.
     * @throws Exception thrown by MockMvc
     */
    @Test
    void canChangeGaaToUserWithDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", user.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(userRepository.findById(user.getId()).get().getRole()).isEqualTo(Role.USER);

        // cleanup
        user.setRole(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a GAA id to /users/{id}/revokeAdmin API endpoint
     * with a GAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeGaaToUserWithGaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", user.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", user.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(user.getId()).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a GAA id to /users/{id}/revokeAdmin API endpoint
     * with a USER cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeGaaToUserWithUserCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", anotherUser.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", user.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(user.getId()).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an UNAUTHORIZED(401) status is received when sending a GAA id to /users/{id}/revokeAdmin API endpoint
     * with a non-existing ID in the cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeGaaToUserWithNonExistingIdCookie() throws Exception {
        // given
        String nonExistingSessionUUID = User.generateSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", nonExistingSessionUUID);

        // when
        when(userRepository.findBySessionUUID(nonExistingSessionUUID)).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", user.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(userRepository.findById(user.getId()).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an UNAUTHORIZED(401) status is received when sending a GAA id to /users/{id}/revokeAdmin API endpoint
     * with no cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeGaaToUserWithNoCookie() throws Exception {
        // when
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", user.getId()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(userRepository.findById(user.getId()).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an NOT_ACCEPTABLE(406) status is received when sending a non-existing id to /users/{id}/revokeAdmin
     * API endpoint with a DGAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeGaaToUserWithNonExistingGaaAndDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(0)).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", 0))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a USER id to /users/{id}/revokeAdmin API endpoint
     * with a DGAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeUserToUserWithDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", anotherUser.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(anotherUser.getId()).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a DGAA id to /users/{id}/revokeAdmin API endpoint
     * with a DGAA cookie
     * @throws Exception thrown by MockMvc
     */
    @Test
    void cantChangeDgaaToUserWithDgaaCookie() throws Exception {
        // given
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(dGAA.getId())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", dGAA.getId()))
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(dGAA.getId()).get().getRole()).isEqualTo(Role.DEFAULTGLOBALAPPLICATIONADMIN);
    }

/* ------------------------------------------ Modified User Profile Tests ------------------------------------------- */

    /**
     * Test An OK stats return when user modified his profile, and profile been successfully modified.
     * @throws Exception User Create Error
     */
    @Test
    void testUserCanChangeProfileForHisSelf() throws Exception {
        // given
        String newDateOfBirth = LocalDate.of(1990, 9, 10).toString();
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", newDateOfBirth, "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");
        Cookie cookie = new Cookie("JSESSIONID", selectedUser.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(selectedUser.getSessionUUID())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedUser.getDateOfBirth()).isEqualTo(newDateOfBirth);
    }

    /**
     * Test An OK stats return when DGAA modified other user's profile, and profile been successfully modified.
     * @throws Exception User Create Error
     */
    @Test
    void testDGAACanChangeProfileForOtherUser() throws Exception {
        // given
        String newDateOfBirth = LocalDate.of(1990, 9, 10).toString();
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", newDateOfBirth, "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");
        Cookie cookie = new Cookie("JSESSIONID", dGAA.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedUser.getDateOfBirth()).isEqualTo(newDateOfBirth);
    }

    /**
     * Test An OK stats return when GAA modified other user's profile, and profile been successfully modified.
     * @throws Exception User Create Error
     */
    @Test
    void testGAACanChangeProfileForOtherUser() throws Exception {
        // given
        String newDateOfBirth = LocalDate.of(1990, 9, 10).toString();
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", newDateOfBirth, "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");
        Cookie cookie = new Cookie("JSESSIONID", user.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedUser.getDateOfBirth()).isEqualTo(newDateOfBirth);
    }

    /**
     * Test A FORBIDDEN stats return when user try to modify other user's profile.
     * @throws Exception User Create Error
     */
    @Test
    void testUserCanNotChangeProfileForOtherUser() throws Exception {
        // given
        String newDateOfBirth = LocalDate.of(1990, 9, 10).toString();
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", newDateOfBirth, "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");
        Cookie cookie = new Cookie("JSESSIONID", anotherUser.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(selectedUser.getDateOfBirth()).isEqualTo(LocalDate.of(2000, 5, 10));
    }

    /**
     * Test A NOT_ACCEPTABLE stats return when user try to modify not exist user's profile.
     * @throws Exception User Create Error
     */
    @Test
    void testUserCanNotChangeProfileForNotExistUser() throws Exception {
        // given
        String newDateOfBirth = LocalDate.of(1990, 9, 10).toString();
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", newDateOfBirth, "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");
        Cookie cookie = new Cookie("JSESSIONID", user.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.empty());
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Test A UNAUTHORIZED stats return when user is not login.
     * @throws Exception User Create Error
     */
    @Test
    void testNotLoginUserCanNotChangeProfile() throws Exception {
        // given
        String newDateOfBirth = LocalDate.of(1990, 9, 10).toString();
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", newDateOfBirth, "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(registerJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test A BAD_REQUEST stats return when user is try to modified profile by invalid info, and error message will
     * show what is invalid.
     * @throws Exception User Create Error
     */
    @Test
    void testValidationStillWorkForModifiedUserProfile() throws Exception {
        // given
        String newName = "";
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());
        Cookie cookie = new Cookie("JSESSIONID", selectedUser.getSessionUUID());

        String registerJson = String.format(modifiedUserPayload, newName, "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", LocalDate.of(1990, 9, 10).toString(), "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");

        // when
        when(userRepository.findBySessionUUID(selectedUser.getSessionUUID())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                        .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                        .cookie(cookie))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid first name");
    }

    /**
     * Test A BAD_REQUEST stats return when user is try to modified profile by invalid address, and error message will
     * show what is invalid.
     * @throws Exception User Create Error
     */
    @Test
    void testValidationStillWorkForModifiedAddress() throws Exception {
        // given
        String newCountry = "";
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());
        Cookie cookie = new Cookie("JSESSIONID", selectedUser.getSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", LocalDate.of(1990, 9, 10).toString(), "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", newCountry, "90210", "Testpassword123!");

        // when
        when(userRepository.findBySessionUUID(selectedUser.getSessionUUID())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findByEmail("bob@email.com")).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                        .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                        .cookie(cookie))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid country");
    }

    /**
     * Test A BAD_REQUEST stats return when user is try to modified profile by invalid address, and error message will
     * show what is invalid.
     * @throws Exception User Create Error
     */
    @Test
    void testUserCanUserEmailAddressAlreadyExist() throws Exception {
        // given
        String newDateOfBirth = LocalDate.of(1990, 9, 10).toString();
        User selectedUser = new User("Bob", "Boberson", "Robert", "Bobert",
                "Bobsbio", "bob@email.com", LocalDate.of(2000, 5, 10),
                "01234567", address, "Testpassword123!", LocalDateTime.now(), Role.USER);
        selectedUser.setId(4);
        selectedUser.setSessionUUID(User.generateSessionUUID());

        String registerJson = String.format(modifiedUserPayload, "Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                "bob@email.com", newDateOfBirth, "01234567", "3/24",
                "Ilam Road", "Ilam", "Christchurch", "Canterbury", "New Zealand", "90210", "Testpassword123!");
        Cookie cookie = new Cookie("JSESSIONID", selectedUser.getSessionUUID());

        // when
        when(userRepository.findBySessionUUID(selectedUser.getSessionUUID())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findById(selectedUser.getId())).thenReturn(Optional.of(selectedUser));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(put(String.format("/user/%d/profile", selectedUser.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)
                .cookie(cookie)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(selectedUser.getDateOfBirth()).isEqualTo(newDateOfBirth);
    }
}
