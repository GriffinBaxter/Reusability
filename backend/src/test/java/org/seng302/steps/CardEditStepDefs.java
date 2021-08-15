package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.model.Address;
import org.seng302.model.Keyword;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class CardEditStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

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

    private Address address;

    private User user;
    private User user2;
    private User gaa;

    private MarketplaceCard card;

    private Keyword keyword;
    private Keyword keyword2;

    private final String creatorCardEditPayloadJson = "{\"creatorId\":\"%d\"," +
            "\"section\":\"%s\"," +
            "\"title\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"keywordIds\":%s}";

    private final String cardEditPayloadJson = "{" +
            "\"section\":\"%s\"," +
            "\"title\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"keywordIds\":%s}";

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        keywordRepository = mock(KeywordRepository.class);

        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository,
                userRepository,
                keywordRepository,
                marketCardNotificationRepository
        )).build();
    }

    @Given("I am logged in as a user and another user exists")
    public void iAmLoggedInAsAUserAndAnotherUserExists() throws Exception {
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
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        user2 = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email2@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user2.setId(2);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
    }

    @Given("A card exists with id {int} with a different owner")
    public void cardExistsWithADifferentOwner(Integer id) throws Exception {
        card = new MarketplaceCard(
                user2.getId(),
                user2,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        card.setId(id);

        given(marketplaceCardRepository.findById(id)).willReturn(Optional.ofNullable(card));
    }

    @Given("A card with id {int} exists with me as the creator")
    public void aCardWithIdExistsWithMeAsTheCreator(Integer id) throws Exception {
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        card.setId(id);

        given(marketplaceCardRepository.findById(id)).willReturn(Optional.ofNullable(card));
    }

    @Given("I am a system admin and a card exists with a different owner at id {int}")
    public void iAmASystemAdminAndACardExistsWithADifferentOwnerAtId(int id) throws Exception {
        gaa = new User("Global",
                "Admin",
                "Application",
                "GAA",
                "stickler",
                "email3@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        gaa.setId(3);
        gaa.setSessionUUID(User.generateSessionUUID());

        card = new MarketplaceCard(
                user2.getId(),
                user2,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        card.setId(id);

        given(userRepository.findBySessionUUID(gaa.getSessionUUID())).willReturn(Optional.ofNullable(gaa));
        given(marketplaceCardRepository.findById(id)).willReturn(Optional.ofNullable(card));
    }

    @When("I try to edit a card with id {int}")
    public void iTryToEditACardWithId(Integer id) throws Exception {
        keyword = new Keyword("Party", LocalDateTime.of(2021,1,1,0,0));
        keyword.setId(1);
        keyword2 = new Keyword("Celebrate", LocalDateTime.of(2021,1,1,0,0));
        keyword2.setId(2);

        when(keywordRepository.findById(1)).thenReturn(Optional.ofNullable(keyword));
        when(keywordRepository.findById(2)).thenReturn(Optional.ofNullable(keyword2));

        String keywordsPayload = "[1,2]";
        String payload = String.format(cardEditPayloadJson, "Exchange" , "New Title", "New Description", keywordsPayload);
        response = mvc.perform(put(String.format("/cards/%d", id))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I try to change the creator for card at id {int}")
    public void iTryToChangeTheCreator(Integer id) throws Exception {
        String payload = String.format(creatorCardEditPayloadJson, 2, "FORSALE" , "Hayley's Birthday", "Come join Hayley and help her celebrate her birthday!", "[]");
        response = mvc.perform(put(String.format("/cards/%d", id))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I try to edit a card with id {int} as system admin")
    public void iTryToEditACardWithIdAsSystemAdmin(int id) throws Exception {
        keyword = new Keyword("Party", LocalDateTime.of(2021,1,1,0,0));
        keyword.setId(1);
        keyword2 = new Keyword("Celebrate", LocalDateTime.of(2021,1,1,0,0));
        keyword2.setId(2);

        when(keywordRepository.findById(1)).thenReturn(Optional.ofNullable(keyword));
        when(keywordRepository.findById(2)).thenReturn(Optional.ofNullable(keyword2));

        String keywordsPayload = "[1,2]";
        String payload = String.format(cardEditPayloadJson, "Exchange" , "New Title", "New Description", keywordsPayload);
        response = mvc.perform(put(String.format("/cards/%d", id))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(new Cookie("JSESSIONID", gaa.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("It doesn't let me edit")
    public void itDoesntLetMeEdit() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(card.getSection()).isNotEqualTo(Section.EXCHANGE);
        assertThat(card.getTitle()).isNotEqualTo("New Title");
        assertThat(card.getDescription()).isNotEqualTo("New Description");
        assertThat(card.getKeywords()).isEmpty();
    }

    @Then("The card is updated")
    public void theCardIsUpdated() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(card.getSection()).isEqualTo(Section.EXCHANGE);
        assertThat(card.getTitle()).isEqualTo("New Title");
        assertThat(card.getDescription()).isEqualTo("New Description");
        assertThat(card.getKeywords()).isNotEmpty();
    }

    @Then("It doesn't let me change the creator")
    public void itDoesntLetMeChangeTheCreator() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(card.getCreatorId()).isNotEqualTo(2);
    }
}
