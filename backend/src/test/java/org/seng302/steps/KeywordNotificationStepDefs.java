package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.KeywordResource;
import org.seng302.controller.NotificationResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalKeywordArgumentException;
import org.seng302.exceptions.IllegalKeywordNotificationArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Keyword;
import org.seng302.model.KeywordNotification;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.seng302.view.outgoing.KeywordPayload;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class KeywordNotificationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc notificationMvc;

    @Autowired
    private MockMvc keywordMvc;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @Autowired
    @MockBean
    private KeywordNotificationRepository keywordNotificationRepository;

    @Autowired
    @MockBean
    private ListingNotificationRepository listingNotificationRepository;

    @Autowired
    @MockBean
    private KeywordRepository keywordRepository;

    private MockHttpServletResponse response;

    private Keyword keyword;
    private KeywordNotification keywordNotification;
    private User admin;

    private String expectedNotificationJson = "[{\"id\":%d," +
                                                "\"description\":\"%s\"," +
                                                "\"created\":\"%s\"," +
                                                "\"keyword\":%s}]";

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        marketCardNotificationRepository = mock(MarketCardNotificationRepository.class);
        keywordNotificationRepository = mock(KeywordNotificationRepository.class);
        listingNotificationRepository = mock(ListingNotificationRepository.class);
        keywordRepository = mock(KeywordRepository.class);

        this.notificationMvc = MockMvcBuilders.standaloneSetup(new NotificationResource(userRepository, marketCardNotificationRepository,
                keywordNotificationRepository, listingNotificationRepository)).build();
        this.keywordMvc = MockMvcBuilders.standaloneSetup(new KeywordResource(keywordRepository, userRepository, keywordNotificationRepository)).build();
    }

    @Given("A keyword has been created")
    public void aKeywordHasBeenCreated() throws IllegalKeywordArgumentException, IllegalKeywordNotificationArgumentException {
        keyword = new Keyword(
                "Beef",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0))
        );
        keyword.setId(1);

        keywordNotification = new KeywordNotification(
                "A new keyword, Beef, has been created",
                LocalDateTime.now(),
                keyword
        );
        keywordNotification.setId(1);

        given(keywordNotificationRepository.findAll()).willReturn(List.of(keywordNotification));
        assertThat(keywordNotificationRepository.findAll()).isEqualTo(List.of(keywordNotification));
    }

    @When("I am logged in as a system administrator")
    public void iAmLoggedInAsASystemAdministrator() throws IllegalAddressArgumentException, IllegalUserArgumentException {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        admin = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "admin@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        admin.setId(2);
        admin.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findBySessionUUID(admin.getSessionUUID())).willReturn(Optional.ofNullable(admin));
        assertTrue(userRepository.findBySessionUUID(admin.getSessionUUID()).isPresent());
    }

    @Then("I receive a notification that a keyword has been created")
    public void iReceiveANotificationThatAKeywordHasBeenCreated() throws Exception {
        response = notificationMvc.perform(get("/users/notifications")
                    .cookie(new Cookie("JSESSIONID", admin.getSessionUUID())))
                    .andReturn()
                    .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedNotificationJson, keywordNotification.getId(),
                                                                          keywordNotification.getDescription(),
                                                                          keywordNotification.getCreated(),
                                                                          (new KeywordPayload(keyword.getId(), keyword.getName(), keyword.getCreated())).toString()));
    }
}
