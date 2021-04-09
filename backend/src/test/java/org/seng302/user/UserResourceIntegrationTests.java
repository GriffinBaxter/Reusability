package org.seng302.user;

import org.junit.jupiter.api.*;
import org.seng302.Address.Address;
import org.seng302.main.Main;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class UserResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
                                        "\"password\": \"%s\"}";

    private final String expectedUserJson = "{\"id\":%d," +
                                    "\"firstName\":\"%s\"," +
                                    "\"lastName\":\"%s\"," +
                                    "\"middleName\":\"%s\"," +
                                    "\"nickname\":\"%s\"," +
                                    "\"bio\":\"%s\"," +
                                    "\"email\":\"%s\"," +
                                    "\"dateOfBirth\":\"%s\"," +
                                    "\"phoneNumber\":\"%s\"," +
                                    "\"homeAddress\":%s,"+
                                    "\"created\":\"%s" + "\"," +
                                    "\"role\":%s," +
                                    "\"businessesAdministered\":%s" +
            "}";

    private final String expectedUserIdJson = "{\"userId\":%s}";

    private MockHttpServletResponse response;

    private String expectedJson;

    private User dGAA;

    private User user;

    private User anotherUser;

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
        dGAA = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2),
                "0271316",
                address,
                "password",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        dGAA.setId(1);
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
                        Role.GLOBALAPPLICATIONADMIN);
        user.setId(2);
        anotherUser = new User ("first",
                                "last",
                                "middle",
                                "nick",
                                "bio",
                                "example@example.com",
                                LocalDate.of(2021, 1, 1),
                                "123456789",
                                address,
                                "password",
                                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                            LocalTime.of(0, 0)),
                                Role.USER);
        anotherUser.setId(3);
        this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository)).build();
    }

    /**
     * Tests that an OK status is received when sending a login payload to the /login API endpoint
     * that contains an email that belongs to an existing user as well as the correct password
     */
    @Test
    public void canLoginWhenUserExistsAndPasswordCorrect() throws Exception {
        // when
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "testpassword")))
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(user.getSessionUUID());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a login payload to the /login API endpoint
     * that contains an email that does not belong to an existing user
     */
    @Test
    public void cantLoginWhenUserDoesntExist() throws Exception {
        // given
        expectedJson = "";

        // when
        response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, "dsjhsd@email.com", "testpassword")))
                .andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isEqualTo(null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a login payload to the /login API endpoint
     * that contains an email that belongs to an existing user but an incorrect password
     */
    @Test
    public void cantLoginWhenUserExistsButPasswordIncorrect() throws Exception {
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
        assertThat(response.getCookie("JSESSIONID")).isEqualTo(null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a CREATED status is received when sending a registration payload to the /users API endpoint
     * that contains an email that doesn't belong to an existing user and contains valid data
     */
    @Test
    public void canRegisterWhenUserDoesntExistAndDataValid() throws Exception {
        // given
        User newUser = new User("Bob", "Boberson", "Robert", "Bobert", "Bobsbio",
                          "bob@email.com", LocalDate.of(2000, user.getId(), dGAA.getId()), "01234567",
                     address, "testpassword", LocalDateTime.now(), Role.USER);
        newUser.setId(4);
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
                        "\"postcode\": \"90210\"" +
                " }, " +
                "\"password\": \"testpassword\"}";

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
    public void cantRegisterWhenUserAlreadyExistsButDataValid() throws Exception {
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
                        "\"postcode\": \"90210\"" +
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
        assertThat(response.getCookie("JSESSIONID")).isEqualTo(null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a registration payload to the /users API endpoint
     * that contains an email that doesn't belong to an existing user but contains invalid data
     */
    @Test
    public void cantRegisterWhenUserDoesntExistButDataInvalid() throws Exception {
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
                        "\"postcode\": \"90210\"" +
                " }, " +
                "\"password\": \"testpassword\"}";
        expectedJson = "";

        // when
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isEqualTo(null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a CONFLICT status is received when sending a registration payload to the /users API endpoint
     * that contains an email that belongs to an existing user and contains invalid data
     */
    @Test
    public void cantRegisterWhenUserDoesExistAndDataInvalid() throws Exception {
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
                        "\"postcode\": \"90210\"" +
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
        assertThat(response.getCookie("JSESSIONID")).isEqualTo(null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Tests that an OK status and a user is received when the user ID in the /users/{id} API endpoint exists
     * Test specifically for when the cookie contains an ID belonging to a DGAA
     */
    @Test
    public void canRetrieveUserWhenUserExistsWithDgaaCookie() throws Exception {
        // given
        expectedJson = String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), "\"" + user.getRole() + "\"", "[null]");

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
    public void canRetrieveUserWhenUserExistsWithGaaCookie() throws Exception {
        // given
        expectedJson = String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), null, "[null]");

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
    public void canRetrieveUserWhenUserExistsWithUserCookie() throws Exception {
        // given
        expectedJson = String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), null, "[null]");

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
    public void cantRetrieveUserWhenUserExistsWithNonExistingIdCookie() throws Exception {
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
    public void cantRetrieveUserWhenUserExistsWithNoCookie() throws Exception {
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
    public void cantRetrieveUserWhenUserDoesntExist() throws Exception {
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

    /**
     * Tests that an OK status is received when searching for a user using the /users/search API endpoint and that
     * the JSON response is equal to the user searched for. The user is searched for using the following orders of the
     * names: first, last, middle, first middle last, first last.
     * Test specifically for when the user searching for a user is a DGAA.
     */
    @Test
    public void canSearchUsersWhenUserExistsAsDgaa() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        String[] firstNameMiddleNameLastName = searchQueryList.get(4).split(" ");
        String[] firstNameLastName = searchQueryList.get(5).split(" ");
        expectedJson = "[" + String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress().toString(), user.getCreated(), "\"" + user.getRole() + "\"",
                "[null]") + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        when(userRepository.findByFirstNameIgnoreCase(searchQueryList.get(0))).thenReturn(list);
        when(userRepository.findByLastNameIgnoreCase(searchQueryList.get(1))).thenReturn(list);
        when(userRepository.findByMiddleNameIgnoreCase(searchQueryList.get(2))).thenReturn(list);
        when(userRepository.findByNicknameIgnoreCase(searchQueryList.get(3))).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(firstNameMiddleNameLastName[0],
                                                firstNameMiddleNameLastName[1], firstNameMiddleNameLastName[2])).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstNameLastName[0], firstNameLastName[1])).thenReturn(list);
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse());
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
    public void canSearchUsersWhenUserExistsWithGaaCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        String[] firstNameMiddleNameLastName = searchQueryList.get(4).split(" ");
        String[] firstNameLastName = searchQueryList.get(5).split(" ");
        expectedJson = "[" + String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                    user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                    user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), null, "[null]") + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        when(userRepository.findByFirstNameIgnoreCase(searchQueryList.get(0))).thenReturn(list);
        when(userRepository.findByLastNameIgnoreCase(searchQueryList.get(1))).thenReturn(list);
        when(userRepository.findByMiddleNameIgnoreCase(searchQueryList.get(2))).thenReturn(list);
        when(userRepository.findByNicknameIgnoreCase(searchQueryList.get(3))).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(firstNameMiddleNameLastName[0],
                firstNameMiddleNameLastName[1], firstNameMiddleNameLastName[2])).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstNameLastName[0], firstNameLastName[1])).thenReturn(list);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse());
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
    public void canSearchUsersWhenUserExistsWithUserCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        String[] firstNameMiddleNameLastName = searchQueryList.get(4).split(" ");
        String[] firstNameLastName = searchQueryList.get(5).split(" ");
        expectedJson = "[" + String.format(expectedUserJson, user.getId(), user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), null, "[null]") + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        when(userRepository.findByFirstNameIgnoreCase(searchQueryList.get(0))).thenReturn(list);
        when(userRepository.findByLastNameIgnoreCase(searchQueryList.get(1))).thenReturn(list);
        when(userRepository.findByMiddleNameIgnoreCase(searchQueryList.get(2))).thenReturn(list);
        when(userRepository.findByNicknameIgnoreCase(searchQueryList.get(3))).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(firstNameMiddleNameLastName[0],
                firstNameMiddleNameLastName[1], firstNameMiddleNameLastName[2])).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstNameLastName[0], firstNameLastName[1])).thenReturn(list);
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse());
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
    public void emptySearchUsersWhenUserDoesntExist() throws Exception {
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
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", dGAA.getSessionUUID())
                    )).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    /**
     * Tests that an UNAUTHORIZED status is received when searching for a user using the /users/search API endpoint
     * when the cookie contains a non-existing ID
     */
    @Test
    public void cantSearchUsersWithNonExistingIdCookie() throws Exception {
        // given
        String nonExistingSessionUUID = User.generateSessionUUID();
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        String[] firstNameMiddleNameLastName = searchQueryList.get(4).split(" ");
        String[] firstNameLastName = searchQueryList.get(5).split(" ");
        expectedJson = "";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        when(userRepository.findByFirstNameIgnoreCase(searchQueryList.get(0))).thenReturn(list);
        when(userRepository.findByLastNameIgnoreCase(searchQueryList.get(1))).thenReturn(list);
        when(userRepository.findByMiddleNameIgnoreCase(searchQueryList.get(2))).thenReturn(list);
        when(userRepository.findByNicknameIgnoreCase(searchQueryList.get(3))).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(firstNameMiddleNameLastName[0],
                firstNameMiddleNameLastName[1], firstNameMiddleNameLastName[2])).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstNameLastName[0], firstNameLastName[1])).thenReturn(list);
        when(userRepository.findBySessionUUID(nonExistingSessionUUID)).thenReturn(Optional.empty());

        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", nonExistingSessionUUID))).andReturn().getResponse());
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
    public void cantSearchUsersWithNoCookie() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        String[] firstNameMiddleNameLastName = searchQueryList.get(4).split(" ");
        String[] firstNameLastName = searchQueryList.get(5).split(" ");
        expectedJson = "";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        List<User> list = List.of(user);
        when(userRepository.findByFirstNameIgnoreCase(searchQueryList.get(0))).thenReturn(list);
        when(userRepository.findByLastNameIgnoreCase(searchQueryList.get(1))).thenReturn(list);
        when(userRepository.findByMiddleNameIgnoreCase(searchQueryList.get(2))).thenReturn(list);
        when(userRepository.findByNicknameIgnoreCase(searchQueryList.get(3))).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(firstNameMiddleNameLastName[0],
                firstNameMiddleNameLastName[1], firstNameMiddleNameLastName[2])).thenReturn(list);
        when(userRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstNameLastName[0], firstNameLastName[1])).thenReturn(list);

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
     * Test that an OK(200) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a DGAA cookie.
     * @throws Exception
     */
    @Test
    public void canChangeUserToGaaWithDgaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeUserToGaaWithGaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeUserToGaaWithUserCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeUserToGaaWithNonExistingIdCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeUserToGaaWithNoCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeUserToGaaWithNonExistingUserAndDgaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeGaaToGaaWithDgaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeDgaaToGaaWithDgaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void canChangeGaaToUserWithDgaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeGaaToUserWithGaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeGaaToUserWithUserCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeGaaToUserWithNonExistingIdCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeGaaToUserWithNoCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeGaaToUserWithNonExistingGaaAndDgaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeUserToUserWithDgaaCookie() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void cantChangeDgaaToUserWithDgaaCookie() throws Exception {
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
}
