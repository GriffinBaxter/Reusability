package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.model.Address;
import org.seng302.model.repository.AddressRepository;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.model.repository.ForgotPasswordRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.controller.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Registration step definitions class
 */
public class RegistrationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @MockBean
    private ForgotPasswordRepository forgotPasswordRepository;

    private MockHttpServletResponse response;
    private User user;
    private Address address;
    private final String expectedUserIdJson = "{\"userId\":%s}";

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository, forgotPasswordRepository)).build();
    }

    @Given("My email {string} doesnt exist in the database.")
    public void my_email_doesnt_exist_in_the_database(String email) {
        assertEquals(userRepository.findByEmail(email), java.util.Optional.empty());
    }

    @When("I register an account with email {string}.")
    public void i_register_an_account_with_email(String email) throws Exception {

        String password = "Password123!";

        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User("Bob",
                "Smith",
                "Ben",
                "Bobby",
                "cool person",
                email,
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                password,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

    @Then("The account is created with email {string}.")
    public void the_account_is_created_with_email(String email) throws Exception {

        String registerJson = "{\"firstName\": \"Bob\", " +
                "\"lastName\": \"Boberson\", " +
                "\"middleName\": \"Robert\", " +
                "\"nickname\": \"Bobert\", " +
                "\"bio\": \"Bobsbio\", " +
                "\"email\": \"" + email + "\", " +
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

        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, 1));
        assertThat(response.getCookie("JSESSIONID").getValue()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Given("The email {string} already exists in the database.")
    public void the_email_already_exists_in_the_database(String email) throws Exception {
        String password = "Password123!";

        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User("Bob",
                "Smith",
                "Ben",
                "Bobby",
                "cool person",
                email,
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                password,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        assertEquals(userRepository.findByEmail(user.getEmail()).get().getEmail(), email);
    }

    @When("I try to register with existing email {string}.")
    public void i_try_to_register_with_existing_email(String email) throws Exception {
        String password = "Password123!";

        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User("Bob",
                "Smith",
                "Ben",
                "Bobby",
                "cool person",
                email,
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                password,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(2);
        user.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        String registerJson = "{\"firstName\": \"Bob\", " +
                "\"lastName\": \"Boberson\", " +
                "\"middleName\": \"Robert\", " +
                "\"nickname\": \"Bobert\", " +
                "\"bio\": \"Bobsbio\", " +
                "\"email\": \"" + email + "\", " +
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

        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();
    }

    @Then("I receive a 409 response.")
    public void i_receive_a_409_response() throws Exception {

        assertThat(response.getContentAsString()).isEmpty();
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

    }

    @When("I try to register with invalid data and email {string}.")
    public void i_try_to_register_with_invalid_data_and_email(String email) throws Exception {

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        String registerJson = "{\"firstName\": \"Bo333b\", " +
                "\"lastName\": \"Boberson\", " +
                "\"middleName\": \"Robert\", " +
                "\"nickname\": \"Bobert\", " +
                "\"bio\": \"Bobsbio\", " +
                "\"email\": \"" + email + "\", " +
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

        response = mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();
    }

    @Then("I receive a 400 response.")
    public void i_receive_a_400_response() throws UnsupportedEncodingException {

        assertThat(response.getContentAsString()).isEmpty();
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }
}
