package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.controller.UserResource;
import org.seng302.model.Address;
import org.seng302.model.Keyword;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
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
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class MarketCardExpiryStep {

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
    private User anotherUser;
    private Address address;
    private MarketplaceCard card;

    private Keyword keyword;
    private Keyword keyword2;
    private Keyword keyword3;


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

    @Given("I am logged in as a user.")
    public void i_am_logged_in_as_a_user_and_create_a_card() throws Exception {
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

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        response = userMVC.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(user.getSessionUUID());
        assertThat(response.getCookie("JSESSIONID").getMaxAge()).isEqualTo(3600);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    @Given("I create a card.")
    public void i_create_a_card() throws Exception {
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));

        keyword = new Keyword("Party", LocalDateTime.now());
        keyword2 = new Keyword("Celebrate", LocalDateTime.now());
        keyword3 = new Keyword("Happy", LocalDateTime.now());
        String keywordPayload = String.format("[%d, %d, %d]", keyword.getId(), keyword2.getId(), keyword3.getId());


        cardPayloadJson = String.format(cardPayloadJsonFormat, card.getCreatorId(), card.getSection(), card.getTitle(),
                card.getDescription(), keywordPayload);

        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription())).willReturn(Optional.empty());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(card);
        response = cardMVC.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(cardPayloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I try to delete this card.")
    public void i_try_to_delete_this_card() throws Exception {
        when(marketplaceCardRepository.findById(card.getId())).thenReturn(Optional.ofNullable(card));

        response = cardMVC.perform(delete(String.format("/cards/%d", card.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    @Then("The card is successfully deleted.")
    public void the_card_is_successfully_deleted() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Given("I login as a GAA.")
    public void i_login_as_a_gaa() throws Exception {
        anotherUser = new User("Another",
                "User",
                "",
                "AU",
                "bio",
                "anotheruser@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        anotherUser.setId(2);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findByEmail(anotherUser.getEmail())).willReturn(Optional.of(anotherUser));

        response = userMVC.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, anotherUser.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, anotherUser.getId()));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(anotherUser.getSessionUUID());
        assertThat(response.getCookie("JSESSIONID").getMaxAge()).isEqualTo(3600);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Given("I login as a DGAA.")
    public void i_login_as_a_dgaa() throws Exception {
        anotherUser = new User("Another",
                "User",
                "",
                "AU",
                "bio",
                "anotheruser@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        anotherUser.setId(2);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findByEmail(anotherUser.getEmail())).willReturn(Optional.of(anotherUser));

        response = userMVC.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, anotherUser.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, anotherUser.getId()));
        assertThat(response.getCookie("JSESSIONID").getValue()).isEqualTo(anotherUser.getSessionUUID());
        assertThat(response.getCookie("JSESSIONID").getMaxAge()).isEqualTo(3600);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
