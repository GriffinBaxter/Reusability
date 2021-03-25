package org.seng302.user;

import org.junit.jupiter.api.*;
import org.seng302.main.Main;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private UserRepository userRepository;

    private String loginPayloadJson = "{\"email\": \"%s\", " +
                                        "\"password\": \"%s\"}";

    private String expectedUserJson = "{\"id\":%d," +
                                    "\"firstName\":\"%s\"," +
                                    "\"lastName\":\"%s\"," +
                                    "\"middleName\":\"%s\"," +
                                    "\"nickname\":\"%s\"," +
                                    "\"bio\":\"%s\"," +
                                    "\"email\":\"%s\"," +
                                    "\"dateOfBirth\":\"%s\"," +
                                    "\"phoneNumber\":\"%s\"," +
                                    "\"homeAddress\":\"%s\"," +
                                    "\"created\":\"%s" + ":00" + "\"," +
                                    "\"role\":%s}";

    private String expectedUserIdJson = "{\"userId\":%s}";

    private MockHttpServletResponse response;

    private Integer id;

    private String expectedJson;

    private User user;

    private User anotherUser;

    @BeforeAll
    public void setup() throws Exception {
        userRepository.deleteAll();
        user = new User("testfirst",
                        "testlast",
                        "testmiddle",
                        "testnick",
                        "testbiography",
                        "testemail@email.com",
                        LocalDate.of(2020, 2, 2),
                        "0271316",
                        "testaddress",
                        "testpassword",
                        LocalDateTime.of(LocalDate.of(2021, 2, 2),
                                            LocalTime.of(0, 0)),
                        Role.GLOBALAPPLICATIONADMIN);
        anotherUser = new User ("first",
                                "last",
                                "middle",
                                "nick",
                                "bio",
                                "example@example.com",
                                LocalDate.of(2021, Month.JANUARY, 1),
                                "123456789",
                                "1 Example Street",
                                "password",
                                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1),
                                            LocalTime.of(0, 0)),
                                Role.USER);
        userRepository.save(user);
        userRepository.save(anotherUser);
    }

    /**
     * Test that an OK(200) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a DGAA cookie.
     * @throws Exception
     */
    @Test
    public void canChangeUSERToGAAWithPermissionAndCreateAccessToken() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "1");

        response = mvc.perform(put(String.format("/users/%d/makeAdmin", 3))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(userRepository.findById(3).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);

        userRepository.saveAndFlush(anotherUser);
    }

    /**
     * Test that an UNAUTHORIZED(401) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a wrong cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeUSERToGAAWithoutAccessToken() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "0");

        response = mvc.perform(put(String.format("/users/%d/makeAdmin", 3))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(userRepository.findById(3).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a USER id to /users/{id}/makeAdmin API endpoint
     * with a Non-DGAA cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeUSERToGAAWithoutPermission() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(2));

        response = mvc.perform(put(String.format("/users/%d/makeAdmin", 3))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(3).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a GAA id to /users/{id}/makeAdmin API endpoint
     * with a DGAA cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeGAAToGAAWithPermissionAndCreateAccessToken() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "1");

        response = mvc.perform(put(String.format("/users/%d/makeAdmin", 2))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(2).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an NOT_ACCEPTABLE(406) status is received when sending a not exist id to /users/{id}/makeAdmin
     * API endpoint with a DGAA cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeUSERToGAAWithNotExistUser() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "1");

        response = mvc.perform(put(String.format("/users/%d/makeAdmin", 10))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Test that an OK(200) status is received when sending a USER id to /users/{id}/revokeAdmin API endpoint
     * with a DGAA cookie.
     * @throws Exception
     */
    @Test
    public void canChangeGAAToUSERWithPermissionAndCreateAccessToken() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "1");

        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", 2))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(userRepository.findById(2).get().getRole()).isEqualTo(Role.USER);

        userRepository.saveAndFlush(user);
    }

    /**
     * Test that an UNAUTHORIZED(401) status is received when sending a USER id to /users/{id}/revokeAdmin API endpoint
     * with a wrong cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeGAAToUSERWithoutAccessToken() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "0");

        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", 2))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(userRepository.findById(2).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a USER id to /users/{id}/revokeAdmin API endpoint
     * with a Non-DGAA cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeGAAToUSERWithoutPermission() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", String.valueOf(3));

        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", 2))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(2).get().getRole()).isEqualTo(Role.GLOBALAPPLICATIONADMIN);
    }

    /**
     * Test that an FORBIDDEN(403) status is received when sending a GAA id to /users/{id}/revokeAdmin API endpoint
     * with a DGAA cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeUSERToUSERWithPermissionAndCreateAccessToken() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "1");

        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", 3))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(userRepository.findById(3).get().getRole()).isEqualTo(Role.USER);
    }

    /**
     * Test that an NOT_ACCEPTABLE(406) status is received when sending a not exist id to /users/{id}/revokeAdmin
     * API endpoint with a DGAA cookie
     * @throws Exception
     */
    @Test
    public void canNotChangeGAAToUSERWithNotExistUser() throws Exception {
        Cookie cookie = new Cookie("JSESSIONID", "1");

        response = mvc.perform(put(String.format("/users/%d/revokeAdmin", 10))
                .cookie(cookie)).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests that an OK status is received when sending a login payload to the /login API endpoint
     * that contains an email that belongs to an existing user as well as the correct password
     */
    @Test
    public void canLoginWhenUserExistsAndPasswordCorrect() throws Exception {
        // when
        response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(String.format(loginPayloadJson, user.getEmail(), "testpassword"))).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, 2));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(String.valueOf(2));
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
                .contentType(MediaType.APPLICATION_JSON).content(String.format(loginPayloadJson, "dsjhsd@email.com", "testpassword"))).andReturn().getResponse();

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
        response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(String.format(loginPayloadJson, user.getEmail(), "asbhash"))).andReturn().getResponse();

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
        String registerJson = "{\"firstName\": \"Bob\", " +
                "\"lastName\": \"Boberson\", " +
                "\"middleName\": \"Robert\", " +
                "\"nickname\": \"Bobert\", " +
                "\"bio\": \"Bobsbio\", " +
                "\"email\": \"bob@email.com\", " +
                "\"dateOfBirth\": \"2000-02-01\", " +
                "\"phoneNumber\": \"01234567\", " +
                "\"homeAddress\": \"testaddress\", " +
                "\"password\": \"testpassword\"}";
        id = (userRepository.findAll()).size() + 1;

        // when
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, id));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(String.valueOf(id));
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
                "\"homeAddress\": \"testaddress\", " +
                "\"password\": \"testpassword\"}";
        expectedJson = "";

        // when
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(String.format(registerJson, user.getEmail()))).andReturn().getResponse();

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
                "\"homeAddress\": \"testaddress\", " +
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
                "\"homeAddress\": \"testaddress\", " +
                "\"password\": \"testpassword\"}";
        expectedJson = "";

        // when
        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(String.format(registerJson, user.getEmail()))).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isEqualTo(null);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Tests that a user is received when the user id in the /users/{id} API endpoint exists
     * Test specifically for when the user retrieving a user is a DGAA
     */
    @Test
    public void canRetrieveUserWhenUserExistsAsDgaa() throws Exception {
        // given
        id = 2;
        expectedJson = String.format(expectedUserJson, 2, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), "\"" + user.getRole() + "\"");

        // when
        response = mvc.perform(
                get(String.format("/users/%d", id)).cookie(new Cookie("JSESSIONID", "1"))
        ).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a user is received when the user id in the /users/{id} API endpoint exists
     * Test specifically for when the user retrieving a user is not a DGAA
     */
    @Test
    public void canRetrieveUserWhenUserExistsNotAsDgaa() throws Exception {
        // given
        id = 2;
        expectedJson = String.format(expectedUserJson, 2, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), null);

        // when
        response = mvc.perform(
                get(String.format("/users/%d", id)).cookie(new Cookie("JSESSIONID", "3"))
        ).andReturn().getResponse();

        // then
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when the user id in the /users/{id} API endpoint does not exist
     */
    @Test
    public void cantRetrieveUserWhenUserDoesntExist() throws Exception {
        // given
        expectedJson = "";
        id = (userRepository.findAll()).size() + 5;

        // when
        response = mvc.perform(
                get(String.format("/users/%d", id)).cookie(new Cookie("JSESSIONID", "1"))
        ).andReturn().getResponse();

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
        expectedJson = "[" + String.format(expectedUserJson, 2, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), "\"" + user.getRole() + "\"") + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", "1")
                    )
            ).andReturn().getResponse());
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
     * Test specifically for when the user searching for a user is not a DGAA.
     */
    @Test
    public void canSearchUsersWhenUserExistsNotAsDgaa() throws Exception {
        // given
        List<String> searchQueryList = List.of(
                "TESTFIRST",
                "TESTLAST",
                "TESTMIDDLE",
                "TESTNICK",
                "TESTFIRST TESTMIDDLE TESTLAST",
                "TESTFIRST TESTLAST"
        );
        expectedJson = "[" + String.format(expectedUserJson, 2, user.getFirstName(), user.getLastName(),
                    user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                    user.getPhoneNumber(), user.getHomeAddress(), user.getCreated(), null) + "]";
        ArrayList<MockHttpServletResponse> responseList = new ArrayList<>();

        // when
        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", "3")
                    )
            ).andReturn().getResponse());
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
        for (String searchQuery: searchQueryList) {
            responseList.add(mvc.perform(
                    get("/users/search").param("searchQuery", searchQuery).cookie(
                            new Cookie("JSESSIONID", "1")
                    )
            ).andReturn().getResponse());
        }

        // then
        for (MockHttpServletResponse response: responseList) {
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }
}
