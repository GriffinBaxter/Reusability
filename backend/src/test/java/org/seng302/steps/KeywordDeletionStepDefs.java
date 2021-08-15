package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.KeywordResource;
import org.seng302.model.Address;
import org.seng302.model.Keyword;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.KeywordNotificationRepository;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.MarketplaceCardRepository;
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
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

public class KeywordDeletionStepDefs extends CucumberSpringConfiguration {

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
    private Keyword keyword;

    private MockHttpServletResponse response;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        keywordRepository = mock(KeywordRepository.class);
        keywordNotificationRepository = mock(KeywordNotificationRepository.class);

        this.mvc = MockMvcBuilders.standaloneSetup(new KeywordResource(keywordRepository, userRepository, keywordNotificationRepository)).build();
    }

    @Given("I am a system admin and a keyword at id {int} exists")
    public void iAmSystemAdminAndKeywordAtIdExists(Integer keywordId) throws Exception {
        Address address = new Address(
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
        user.setSessionUUID(User.generateSessionUUID());

        keyword = new Keyword("Cucumber", LocalDateTime.of(2021,1,1,0,0));
        keyword.setId(keywordId);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keywordId)).willReturn(Optional.ofNullable(keyword));

        assertThat(keywordRepository.findById(keywordId)).isNotEmpty();
    }

    @Given("I am a user and a keyword at id {int} exists")
    public void iAmAUserAndAKeywordAtIdExists(Integer keywordId) throws Exception {
        Address address = new Address(
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
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        keyword = new Keyword("Cucumber", LocalDateTime.of(2021,1,1,0,0));
        keyword.setId(keywordId);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keywordId)).willReturn(Optional.ofNullable(keyword));

        assertThat(keywordRepository.findById(keywordId)).isNotEmpty();
    }

    @When("I try to delete the keyword at id {int}")
    public void iTryToDeleteTheKeyword(Integer keywordId) throws Exception {
        response = mvc.perform(delete(String.format("/keywords/%d", keywordId))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("The keyword is deleted")
    public void theKeywordIsDeleted() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Then("The keyword is not deleted")
    public void theKeywordIsNotDeleted() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
