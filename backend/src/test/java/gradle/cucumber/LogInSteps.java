package gradle.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeAll;
import org.seng302.address.Address;
import org.seng302.address.AddressRepository;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.seng302.user.UserResource;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class LogInSteps extends SpringIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

    private User user;
    private Address address;
    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    @Before
    public void setup() throws Exception {
//
//        address = new Address(
//                "3/24",
//                "Ilam Road",
//                "Christchurch",
//                "Canterbury",
//                "New Zealand",
//                "90210"
//        );
//        user = new User("Bob",
//                "Smith",
//                "Ben",
//                "Bobby",
//                "cool person",
//                "email@email.com",
//                LocalDate.of(2020, 2, 2).minusYears(13),
//                "0271316",
//                address,
//                "Password123!",
//                LocalDateTime.of(LocalDate.of(2021, 2, 2),
//                        LocalTime.of(0, 0)),
//                Role.GLOBALAPPLICATIONADMIN);
//        user.setId(1);

        // initializes the MockMVC object and tells it to use the userRepository
//        this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();

    }

    @Given("The user's details exist in the database, with email of {string} and password of {string}")
    public void theUserSDetailsExistInTheDatabaseWithEmailOfAndPasswordOf(String email, String password) throws Exception {

        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        User user = new User("Bob",
                "Smith",
                "Ben",
                "Bobby",
                "cool person",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.ofNullable(user));

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(true).isEqualTo(user.verifyPassword(password));

    }

    @When("The user supplies an email {string} and password {string} which matches the details in the database")
    public void theUserSuppliesAnEmailAndPasswordWhichMatchesTheDetailsInTheDatabase(String email, String password) {

        Optional<User> optionUser = Optional.ofNullable(user);

        when(optionUser.get().verifyPassword("Password123!")).thenReturn(true);

    }


    @And("attempts to log in")
    public void attemptsToLogIn() {

    }

    @Then("They should be logged in")
    public void theyShouldBeLoggedIn() throws Exception {

        MockHttpServletResponse response = mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(user.getSessionUUID());
        assertThat(response.getCookie("JSESSIONID").getMaxAge()).isEqualTo(3600);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Given("The user is not existing in the database, i.e.  the email of {string} does not exist")
    public void theUserIsNotExistingInTheDatabaseIETheEmailOfDoesNotExist(String email) {

    }

    @When("The user enters an email of {string} that is not registered")
    public void theUserEntersAnEmailOfThatIsNotRegistered(String email) {

    }

    @And("the password {string} is supplied")
    public void thePasswordIsSupplied(String password) {

    }

    @Then("They should not be logged in")
    public void theyShouldNotBeLoggedIn() {

    }

    @And("An error message stating the email or password is incorrect is displayed")
    public void anErrorMessageStatingTheEmailOrPasswordIsIncorrectIsDisplayed() {

    }

    @When("The user enters a registered email, {string}")
    public void theUserEntersARegisteredEmail(String email) {

    }

    @And("An incorrect password is supplied, {string}")
    public void anIncorrectPasswordIsSupplied(String password) {

    }

    @Given("No email is entered in the login page")
    public void noEmailIsEnteredInTheLoginPage() {

    }

    @When("The user attempts to login with no email and the password {string}")
    public void theUserAttemptsToLoginWithNoEmailAndThePassword(String password) {

    }

    @Given("No password is entered in the login page and a registered email of {string} is provided")
    public void noPasswordIsEnteredInTheLoginPageAndARegisteredEmailOfIsProvided(String email) {

    }

    @When("The user attempts to login, and a registered email of {string} is provided")
    public void theUserAttemptsToLoginAndARegisteredEmailOfIsProvided(String email) {
    }
}
