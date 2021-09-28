package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.UserResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalForgotPasswordArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.ForgotPassword;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class ForgotPasswordStepDefs {

    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private ForgotPasswordRepository forgotPasswordRepository;

    private MockHttpServletResponse response;

    private User user;

    private ForgotPassword forgotPassword;

    private String originalPassword;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        forgotPasswordRepository = mock(ForgotPasswordRepository.class);

        this.mvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository, forgotPasswordRepository)).build();
    }

    @Given("A user exists with the email {string}")
    public void aUserExistsWithTheEmail(String email) throws IllegalAddressArgumentException, IllegalUserArgumentException {

        Address address = new Address("12", "Road Street",
                "City", "Region", "Country",
                "0000", "Suburb");

        user = new User("first","Last","","","",
                email, LocalDate.now().minusYears(20),"0219991111", address,
                "CurrentPassword123!", LocalDateTime.now(), Role.USER);
        user.setId(1);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    }


    @Given("A forgot password entity exist for user")
    public void aForgotPasswordEntityExistForUser() throws IllegalForgotPasswordArgumentException {
        forgotPassword = new ForgotPassword(user.getId());
        given(forgotPasswordRepository.findByToken(forgotPassword.getToken())).willReturn(Optional.of(forgotPassword));
    }

    @When("I try to change the password")
    public void iTryToChangeThePassword() throws Exception {
        originalPassword = user.getPassword();
        String passwordJson = "{\"password\":\"NewPassword123!\"}";

        response = mvc.perform(put("/users/forgotPassword")
                .param("token", forgotPassword.getToken())
                .contentType(MediaType.APPLICATION_JSON).content(passwordJson))
                .andReturn().getResponse();
    }

    @Then("The password successfully changes")
    public void thePasswordSuccessfullyChanges() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(user.getPassword()).isNotEqualTo(originalPassword);
    }
}
