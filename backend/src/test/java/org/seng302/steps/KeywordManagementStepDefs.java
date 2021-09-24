package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.KeywordResource;
import org.seng302.controller.UserResource;
import org.seng302.model.Address;
import org.seng302.model.KeywordNotification;
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

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class KeywordManagementStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc userMVC;

    @Autowired
    private MockMvc keywordMVC;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @MockBean
    private KeywordRepository keywordRepository;

    @Autowired
    @MockBean
    private KeywordNotificationRepository keywordNotificationRepository;

    @Autowired
    @MockBean
    private ForgotPasswordRepository forgotPasswordRepository;

    private MockHttpServletResponse response;

    private User user;
    private Address address;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    private final String keywordPayloadJsonFormat = "{\"name\":\"%s\"}";

    private String keywordPayloadJson;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        keywordRepository = mock(KeywordRepository.class);

        this.keywordMVC = MockMvcBuilders.standaloneSetup(new KeywordResource(keywordRepository, userRepository, keywordNotificationRepository)).build();
        this.userMVC = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository, forgotPasswordRepository)).build();
    }

    @Given("I am already logged in.")
    public void iAmAlreadyLoggedIn() throws Exception {
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
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(1);

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        response = userMVC.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @When("I try to create a keyword that is {int} characters long.")
    public void iTryToCreateAKeywordThatIs_CharactersLong(Integer lengthKeyword) throws Exception {
        keywordPayloadJson = String.format(keywordPayloadJsonFormat, "H".repeat(lengthKeyword));

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        when(keywordRepository.findByName("H".repeat(lengthKeyword))).thenReturn(Optional.empty());
        response = keywordMVC.perform(post("/keywords")
                .contentType(MediaType.APPLICATION_JSON).content(keywordPayloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("The keyword is not created.")
    public void theKeywordIsNotCreated() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
