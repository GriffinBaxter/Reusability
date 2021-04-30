package gradle.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.mockito.Mockito.when;


public class LogInSteps extends SpringIntegrationTest {
    @Given("The user's details exist in the database.")
    public void the_user_s_details_exist_in_the_database() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("The user supplies an email and password which matches the details in the database")
    public void theUserSuppliesAnEmailAndPasswordWhichMatchesTheDetailsInTheDatabase() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
    }

    @And("attempts to log in")
    public void attemptsToLogIn() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("They should be logged in")
    public void theyShouldBeLoggedIn() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("The user is not existing in the database.")
    public void theUserIsNotExistingInTheDatabase() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("The user enters an email that is not registered")
    public void theUserEntersAnEmailThatIsNotRegistered() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @And("any password is supplied")
    public void anyPasswordIsSupplied() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("They should not be logged in")
    public void theyShouldNotBeLoggedIn() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @And("An error message stating the email or password is incorrect is displayed")
    public void anErrorMessageStatingTheEmailOrPasswordIsIncorrectIsDisplayed() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("The user exists in the database")
    public void theUserExistsInTheDatabase() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("The user enters a registered email")
    public void theUserEntersARegisteredEmail() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @And("An incorrect password is supplied")
    public void anIncorrectPasswordIsSupplied() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("No email is entered in the login page")
    public void noEmailIsEnteredInTheLoginPage() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("The user attempts to login")
    public void theUserAttemptsToLogin() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("No password is entered in the login page")
    public void noPasswordIsEnteredInTheLoginPage() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
