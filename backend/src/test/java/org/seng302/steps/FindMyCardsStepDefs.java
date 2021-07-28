package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.MarketplaceCardRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class FindMyCardsStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc cardMVC;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    @MockBean
    private KeywordRepository keywordRepository;

    @Autowired
    @MockBean
    private MarketCardNotificationRepository marketCardNotificationRepository;

    private MockHttpServletResponse response;

    private User user1;
    private User user2;
    private MarketplaceCard card1;
    private MarketplaceCard card2;
    private LocalDateTime initialDisplayPeriodEnd;

    private final String expectedCardJson = "{" +
            "\"id\":%d," +
            "\"creator\":{" +
            "\"id\":%d," +
            "\"firstName\":\"%s\"," +
            "\"lastName\":\"%s\"," +
            "\"middleName\":\"%s\"," +
            "\"nickname\":\"%s\"," +
            "\"bio\":\"%s\"," +
            "\"email\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"role\":\"%s\"," +
            "\"businessesAdministered\":[null]," +
            "\"homeAddress\":%s" +
            "}," +
            "\"section\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"displayPeriodEnd\":\"%s\"," +
            "\"title\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"keywords\":%s" +
            "}";

    private String expectedJson;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        keywordRepository = mock(KeywordRepository.class);

        this.cardMVC = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository,
                userRepository,
                keywordRepository,
                marketCardNotificationRepository
        )).build();
    }

    @Given("I am logged in and have already created cards.")
    public void iAmLoggedInAndHaveAlreadyCreatedCards() throws
            IllegalAddressArgumentException,
            IllegalUserArgumentException,
            IllegalMarketplaceCardArgumentException
    {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user1 = new User("Bob",
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
        user1.setId(1);
        user1.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.of(user1));
        Assertions.assertTrue(userRepository.findBySessionUUID(user1.getSessionUUID()).isPresent());

        initialDisplayPeriodEnd = LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(0, 0));
        card1 = new MarketplaceCard(
                user1.getId(),
                user1,
                Section.FORSALE,
                initialDisplayPeriodEnd,
                "Card 1 Title",
                "This is the first card."
        );
        card1.setId(1);

        card2 = new MarketplaceCard(
                user1.getId(),
                user1,
                Section.EXCHANGE,
                initialDisplayPeriodEnd,
                "Card 2 Title",
                "This is the second card."
        );
        card2.setId(2);

        given(marketplaceCardRepository.findById(card1.getId())).willReturn(Optional.ofNullable(card1));
        given(marketplaceCardRepository.findById(card2.getId())).willReturn(Optional.ofNullable(card2));

        Assertions.assertTrue(marketplaceCardRepository.findById(card1.getId()).isPresent());
        Assertions.assertTrue(marketplaceCardRepository.findById(card2.getId()).isPresent());
    }

    @Given("I am logged in and another user has already created cards.")
    public void iAmLoggedInAndAnotherUserHasAlreadyCreatedCards() throws
            IllegalAddressArgumentException,
            IllegalUserArgumentException,
            IllegalMarketplaceCardArgumentException
    {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user1 = new User("Bob",
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
        user1.setId(1);
        user1.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.of(user1));
        Assertions.assertTrue(userRepository.findBySessionUUID(user1.getSessionUUID()).isPresent());

        user2 = new User("John",
                "Smith",
                "Ben",
                "Johnny",
                "cool person",
                "newemail@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user2.setId(2);
        user2.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findBySessionUUID(user2.getSessionUUID())).willReturn(Optional.of(user2));
        Assertions.assertTrue(userRepository.findBySessionUUID(user2.getSessionUUID()).isPresent());

        initialDisplayPeriodEnd = LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(0, 0));
        card1 = new MarketplaceCard(
                user2.getId(),
                user2,
                Section.FORSALE,
                initialDisplayPeriodEnd,
                "Card 1 Title",
                "This is the first card."
        );
        card1.setId(1);

        card2 = new MarketplaceCard(
                user2.getId(),
                user2,
                Section.EXCHANGE,
                initialDisplayPeriodEnd,
                "Card 2 Title",
                "This is the second card."
        );
        card2.setId(2);

        given(marketplaceCardRepository.findById(card1.getId())).willReturn(Optional.ofNullable(card1));
        given(marketplaceCardRepository.findById(card2.getId())).willReturn(Optional.ofNullable(card2));

        Assertions.assertTrue(marketplaceCardRepository.findById(card1.getId()).isPresent());
        Assertions.assertTrue(marketplaceCardRepository.findById(card2.getId()).isPresent());
    }

    @When("I request to find my own active cards.")
    public void iRequestToFindMyOwnActiveCards() throws Exception {
        given(userRepository.findById(user1.getId())).willReturn(Optional.ofNullable(user1));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorId(user1.getId())).willReturn(
                List.of(card1, card2)
        );

        response = cardMVC.perform(get(String.format("/users/%d/cards", user1.getId()))
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID()))).andReturn().getResponse();
    }

    @When("I request to find another user's active cards.")
    public void iRequestToFindAnotherUsersActiveCards() throws Exception {
        given(userRepository.findById(user1.getId())).willReturn(Optional.ofNullable(user1));
        given(userRepository.findById(user2.getId())).willReturn(Optional.ofNullable(user2));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorId(user2.getId())).willReturn(
                List.of(card1, card2)
        );

        response = cardMVC.perform(get(String.format("/users/%d/cards", user2.getId()))
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID()))).andReturn().getResponse();
    }

    @Then("All of my active cards are shown.")
    public void allOfMyActiveCardsAreShown() throws UnsupportedEncodingException {
        String card1Json = String.format(expectedCardJson, card1.getId(), user1.getId(), user1.getFirstName(),
                user1.getLastName(), user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(),
                user1.getCreated(), user1.getRole(), user1.getHomeAddress().toSecureString(),
                card1.getSection().toString(), card1.getCreated(), card1.getDisplayPeriodEnd(), card1.getTitle(),
                card1.getDescription(), card1.getKeywords());

        String card2Json = String.format(expectedCardJson, card2.getId(), user1.getId(), user1.getFirstName(),
                user1.getLastName(), user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(),
                user1.getCreated(), user1.getRole(), user1.getHomeAddress().toSecureString(),
                card2.getSection().toString(), card2.getCreated(), card2.getDisplayPeriodEnd(), card2.getTitle(),
                card2.getDescription(), card2.getKeywords());

        expectedJson = "[" + card1Json + "," + card2Json + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Then("All of the other user's active cards are shown.")
    public void allOfTheOtherUsersActiveCardsAreShown() throws UnsupportedEncodingException {
        String card1Json = String.format(expectedCardJson, card1.getId(), user2.getId(), user2.getFirstName(),
                user2.getLastName(), user2.getMiddleName(), user2.getNickname(), user2.getBio(), user2.getEmail(),
                user2.getCreated(), user2.getRole(), user2.getHomeAddress().toSecureString(),
                card1.getSection().toString(), card1.getCreated(), card1.getDisplayPeriodEnd(), card1.getTitle(),
                card1.getDescription(), card1.getKeywords());

        String card2Json = String.format(expectedCardJson, card2.getId(), user2.getId(), user2.getFirstName(),
                user2.getLastName(), user2.getMiddleName(), user2.getNickname(), user2.getBio(), user2.getEmail(),
                user2.getCreated(), user2.getRole(), user2.getHomeAddress().toSecureString(),
                card2.getSection().toString(), card2.getCreated(), card2.getDisplayPeriodEnd(), card2.getTitle(),
                card2.getDescription(), card2.getKeywords());

        expectedJson = "[" + card1Json + "," + card2Json + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Then("All of my active cards of multiple types are shown together.")
    public void allOfMyActiveCardsOfMultipleTypesAreShownTogether() throws UnsupportedEncodingException {
        String card1Json = String.format(expectedCardJson, card1.getId(), user1.getId(), user1.getFirstName(),
                user1.getLastName(), user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(),
                user1.getCreated(), user1.getRole(), user1.getHomeAddress().toSecureString(),
                card1.getSection().toString(), card1.getCreated(), card1.getDisplayPeriodEnd(), card1.getTitle(),
                card1.getDescription(), card1.getKeywords());

        String card2Json = String.format(expectedCardJson, card2.getId(), user1.getId(), user1.getFirstName(),
                user1.getLastName(), user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(),
                user1.getCreated(), user1.getRole(), user1.getHomeAddress().toSecureString(),
                card2.getSection().toString(), card2.getCreated(), card2.getDisplayPeriodEnd(), card2.getTitle(),
                card2.getDescription(), card2.getKeywords());

        expectedJson = "[" + card1Json + "," + card2Json + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);

        assertThat(card1.getSection()).isNotEqualTo(card2.getSection());
    }

}
