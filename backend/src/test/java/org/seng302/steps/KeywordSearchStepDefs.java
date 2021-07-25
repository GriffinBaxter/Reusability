package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.KeywordResource;
import org.seng302.model.Address;
import org.seng302.model.Keyword;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.KeywordNotificationRepository;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class KeywordSearchStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private KeywordRepository keywordRepository;

    @Autowired
    @MockBean
    private KeywordNotificationRepository keywordNotificationRepository;

    private User user;

    private MockHttpServletResponse response;

    // AC2
    private Keyword keyword;
    private Keyword keyword2;
    private Keyword keyword3;

    // AC3
    private Keyword keyword4;
    private Keyword keyword5;

    private String expectedJson = "{" +
            "\"id\":%d," +
            "\"name\":\"%s\"," +
            "\"created\":\"%s\"" +
            "}";

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        keywordRepository = mock(KeywordRepository.class);
        keywordNotificationRepository = mock(KeywordNotificationRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new KeywordResource(keywordRepository, userRepository, keywordNotificationRepository)).build();
    }

    @Given("A list of keywords exist in the system")
    public void listOfKeywordsExist() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        keyword = new Keyword("Event", LocalDateTime.now());
        keyword2 = new Keyword("Health", LocalDateTime.now());
        keyword3 = new Keyword("Music", LocalDateTime.now());

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
    }

    @When("The user tries to get all keywords")
    public void userTriesToGetAllKeywords() throws Exception {

        when(keywordRepository.findAllByNameIgnoreCaseContaining("")).thenReturn(List.of(keyword, keyword2, keyword3));

        response = mvc.perform(get("/keywords/search?searchQuery=")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("All keywords are returned to the user")
    public void allKeywordsReturned() throws Exception {
        String expected = "[" +
                String.format(expectedJson, keyword.getId(), keyword.getName(), keyword.getCreated()) + "," +
                String.format(expectedJson, keyword2.getId(), keyword2.getName(), keyword2.getCreated()) + "," +
                String.format(expectedJson, keyword3.getId(), keyword3.getName(), keyword3.getCreated()) +
                "]";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expected);
    }

    @Given("Keywords {string} and {string} and {string} exist in the system")
    public void keywordsExist(String keywordName1, String keywordName2, String keywordName3) throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(2);
        user.setSessionUUID(User.generateSessionUUID());

        keyword4 = new Keyword(keywordName1, LocalDateTime.now());
        keyword5 = new Keyword(keywordName2, LocalDateTime.now());

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
    }

    @When("The user searches for {string}")
    public void userSearchesForX(String search) throws Exception {

        when(keywordRepository.findAllByNameIgnoreCaseContaining(search)).thenReturn(List.of(keyword4, keyword5));

        response = mvc.perform(get(String.format("/keywords/search?searchQuery=%s", search))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("Keywords {string} and {string} are returned to the user")
    public void certainKeywordsReturned(String keywordName1, String keywordName2) throws Exception {
        String expected = "[" +
                String.format(expectedJson, keyword4.getId(), keywordName1, keyword4.getCreated()) +
                "," +
                String.format(expectedJson, keyword5.getId(), keywordName2, keyword5.getCreated()) +
                "]";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expected);
    }

}
