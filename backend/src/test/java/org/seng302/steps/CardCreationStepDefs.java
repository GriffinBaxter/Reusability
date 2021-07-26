package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.model.Address;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.*;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.model.*;
import org.seng302.controller.UserResource;
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
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Card creation step definitions class
 */
public class CardCreationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc userMVC;

    @Autowired
    private MockMvc cardMVC;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

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

    private User user;
    private Address address;
    private MarketplaceCard card;
    private Keyword keyword;
    private Keyword anotherKeyword;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    private final String cardPayloadJsonFormat = "{\"creatorId\":\"%d\"," +
            "\"section\":\"%s\"," +
            "\"title\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"keywordIds\":%s}";

    private String cardPayloadJson;

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
        this.userMVC = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();
    }

    @Given("I am logged in.")
    public void iAmLoggedIn() throws Exception {
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

        keyword = new Keyword("First", LocalDateTime.now());
        keyword.setId(1);

        anotherKeyword = new Keyword("Second", LocalDateTime.now());
        anotherKeyword.setId(2);

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        response = userMVC.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @When("I create a card with the For Sale section selected.")
    public void iCreateACardWithTheForSaleSectionSelected() throws Exception {
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));

        cardPayloadJson = String.format(cardPayloadJsonFormat, card.getCreatorId(), card.getSection(), card.getTitle(),
                card.getDescription(), "[" + keyword.getId() + "]");

        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription())).willReturn(Optional.empty());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(card);
        response = cardMVC.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(cardPayloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I create a card with the Wanted section selected.")
    public void iCreateACardWithTheWantedSectionSelected() throws Exception {
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));

        cardPayloadJson = String.format(cardPayloadJsonFormat, card.getCreatorId(), card.getSection(), card.getTitle(),
                card.getDescription(), "[" + keyword.getId() + "]");

        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription())).willReturn(Optional.empty());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(card);
        response = cardMVC.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(cardPayloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
        System.out.println(response.getStatus());
    }

    @When("I create a card with the Exchange section selected.")
    public void iCreateACardWithTheExchangeSectionSelected() throws Exception {
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.EXCHANGE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));

        cardPayloadJson = String.format(cardPayloadJsonFormat, card.getCreatorId(), card.getSection(), card.getTitle(),
                card.getDescription(), "[" + keyword.getId() + "]");

        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription())).willReturn(Optional.empty());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(card);
        response = cardMVC.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(cardPayloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I try to create a card without a title.")
    public void iTryToCreateACardWithoutATitle() throws Exception {
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.EXCHANGE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));

        // Empty string for no title.
        cardPayloadJson = String.format(cardPayloadJsonFormat, card.getCreatorId(), card.getSection(), "",
                card.getDescription(), "[" + keyword.getId() + "]");

        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription())).willReturn(Optional.empty());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(card);
        response = cardMVC.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(cardPayloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I create a card with more than one keyword.")
    public void iCreateACardWithMoreThanOneKeyword() throws Exception {
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.EXCHANGE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));
        given(keywordRepository.findById(anotherKeyword.getId())).willReturn(Optional.ofNullable(anotherKeyword));

        cardPayloadJson = String.format(cardPayloadJsonFormat, card.getCreatorId(), card.getSection(), card.getTitle(),
                card.getDescription(), "[" + keyword.getId() + ", " + anotherKeyword.getId() + "]");

        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription())).willReturn(Optional.empty());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(card);
        response = cardMVC.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(cardPayloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("The card is successfully created.")
    public void theCardIsSuccessfullyCreated() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Then("The card is not created.")
    public void theCardIsNotCreated() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



}
