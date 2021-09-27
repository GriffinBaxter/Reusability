package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Login step definitions class
 */
public class LoginStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @MockBean
    private ForgotPasswordRepository forgotPasswordRepository;

    private User user;
    private Address address;
    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    private String currentEmail;
    private String currentPassword;

    private LocalDateTime lockedTime;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository, forgotPasswordRepository)).build();
    }

    @Given("The user's details exist in the database, with email of {string} and password of {string}")
    public void theUserSDetailsExistInTheDatabaseWithEmailOfAndPasswordOf(String email, String password) throws Exception {
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

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    }

    @When("The user supplies an email {string} and password {string} which matches the details in the database")
    public void theUserSuppliesAnEmailAndPasswordWhichMatchesTheDetailsInTheDatabase(String email, String password) {
        User findUser = userRepository.findByEmail(email).get();

        assertThat(findUser.getEmail()).isEqualTo(email);
        assertThat(findUser.getPassword()).isEqualTo(user.encode(password));
    }

    @Then("They should be logged in")
    public void theyShouldBeLoggedIn() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(user.getSessionUUID());
        assertThat(response.getCookie("JSESSIONID").getMaxAge()).isEqualTo(28800);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Given("The user is not existing in the database, i.e.  the email of {string} does not exist")
    public void theUserIsNotExistingInTheDatabaseIETheEmailOfDoesNotExist(String email) throws Exception {
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
                "invalidPassword1!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(1);

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
    }

    @When("The user enters an email of {string} that is not registered")
    public void theUserEntersAnEmailOfThatIsNotRegistered(String email) {
        currentEmail = email;

        Optional<User> findUser = userRepository.findByEmail(currentEmail);

        assertThat(findUser).isEmpty();
    }

    @And("the password {string} is supplied")
    public void thePasswordIsSupplied(String password) {
        currentPassword = password;
        assertThat(currentPassword).isNotNull();
    }

    @Then("They should not be logged in and an error message stating the email or password is incorrect is displayed")
    public void theyShouldNotBeLoggedInAndAnErrorMessageStatingTheEmailOrPasswordIsIncorrectIsDisplayed() throws Exception {
        MockHttpServletResponse response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, currentEmail, currentPassword)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Then("An error message stating the email or password is incorrect is displayed")
    public void anErrorMessageStatingTheEmailOrPasswordIsIncorrectIsDisplayed() throws Exception {

        MockHttpServletResponse response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, currentEmail, currentPassword)))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }


    @Given("No email is entered in the login page")
    public void noEmailIsEnteredInTheLoginPage() {

        currentEmail = "";

    }

    @When("The user attempts to login with no email and the password {string}")
    public void theUserAttemptsToLoginWithNoEmailAndThePassword(String password) {

        currentPassword = password;

    }

    @Given("No password is entered in the login page and a registered email of {string} is provided")
    public void noPasswordIsEnteredInTheLoginPageAndARegisteredEmailOfIsProvided(String email) {

        currentEmail = email;

    }

    @When("The user attempts to login, and a registered email of {string} is provided")
    public void theUserAttemptsToLoginAndARegisteredEmailOfIsProvided(String email) {

        currentEmail = email;

    }

    @When("The user enters a registered email, {string} and an incorrect password is supplied, {string}")
    public void theUserEntersARegisteredEmailAndAnIncorrectPasswordIsSupplied(String email, String password) {

        currentEmail = email;
        currentPassword = password;

        Optional<User> findUser = userRepository.findByEmail(currentEmail);
        assertThat(findUser.get().getPassword()).isNotEqualTo(currentPassword);

    }

    @Given("The user has tried unsuccessfully to login {int} times")
    public void the_user_has_tried_unsuccessfully_to_login_times(Integer attempts) {
        user.setRemainingLoginAttempts(3 - attempts);
        user.setTimeWhenUnlocked(null);

        assertThat(user.isLocked()).isFalse();
        assertThat(user.getTimeWhenUnlocked()).isNull();
    }

    @When("They try to login with the incorrect password {string}")
    public void they_try_to_login_with_the_incorrect_password(String incorrectPassword) throws Exception {
        String expectedJson= "";

        MockHttpServletResponse response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), incorrectPassword)))
                .andReturn().getResponse();

        lockedTime = LocalDateTime.now();

        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        assertThat(response.getCookie("JSESSIONID")).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Then("Their account is locked for {int} hour")
    public void their_account_is_locked_for_hour(Integer hoursLockedFor) {
        LocalDateTime whenLocked = lockedTime.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime whenUnlocked = user.getTimeWhenUnlocked().truncatedTo(ChronoUnit.MINUTES);

        assertThat(user.isLocked()).isTrue();
        assertThat(whenUnlocked).isEqualTo(whenLocked.plusHours(1));
    }

    @Given("Their account is locked")
    public void their_account_is_locked() {
        user.setRemainingLoginAttempts(0);
        user.setTimeWhenUnlocked(LocalDateTime.now().minusMinutes(1));

    }

}
