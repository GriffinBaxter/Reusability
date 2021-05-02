package org.seng302.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RegistrationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    UserRepository userRepository;

    @Given("My email {string} doesnt exist in the database.")
    public void my_email_doesnt_exist_in_the_database(String email) {
        given(userRepository.findByEmail(email)).willReturn(java.util.Optional.empty());
        assertEquals(userRepository.findByEmail(email), java.util.Optional.empty());
    }

    @When("I register an account.")
    public void i_register_an_account() {
//        response = mvc.perform(post("/users")
//                .contentType(MediaType.APPLICATION_JSON).content(registerJson)).andReturn().getResponse();
    }

    @Then("The account is created.")
    public void the_account_is_created() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}
