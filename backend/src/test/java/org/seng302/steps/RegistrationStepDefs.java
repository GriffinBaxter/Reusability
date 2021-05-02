package org.seng302.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class RegistrationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    UserRepository userRepository;

    @Given("My email {string} doesnt exist in the database.")
    public void my_email_doesnt_exist_in_the_database(String email) {
        given(userRepository.findByEmail(email)).willReturn(java.util.Optional.empty());
        assertEquals(userRepository.findByEmail(email), java.util.Optional.empty());
    }

    @When("I register an account with email {string}.")
    public void i_register_an_account_with_email(String email) {
//        response = mvc.perform(post("/users")
//                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();
    }

    @Then("The account is created with email {string}.")
    public void the_account_is_created_with_email(String email) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("The email {string} already exists in the database.")
    public void the_email_already_exists_in_the_database(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I try to register with existing email {string}.")
    public void i_try_to_register_with_existing_email(String email) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("I receive a 409 response.")
    public void i_receive_a_409_response() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I try to register with invalid data and email {string}.")
    public void i_try_to_register_with_invalid_data_and_email(String email) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("I receive a 400 response.")
    public void i_receive_a_400_response() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
