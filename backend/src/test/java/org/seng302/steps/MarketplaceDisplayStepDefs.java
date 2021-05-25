package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.address.Address;
import org.seng302.marketplace.*;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class MarketplaceDisplayStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private KeywordRepository keywordRepository;


    private MarketplaceCard marketplaceCard;
    private MarketplaceCard marketplaceCard1;
    private MarketplaceCard marketplaceCard2;
    private MarketplaceCard marketplaceCard3;
    private MarketplaceCard marketplaceCard4;
    private MarketplaceCard marketplaceCard5;
    private MarketplaceCard marketplaceCard6;
    private MarketplaceCard marketplaceCard7;
    private MarketplaceCard marketplaceCard8;
    private MarketplaceCard marketplaceCard9;


    private User user;
    private Address address;

    private MockHttpServletResponse response;

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

    @Before
    public void createMockMvc() {
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        userRepository = mock(UserRepository.class);
        keywordRepository = mock(KeywordRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(marketplaceCardRepository, userRepository, keywordRepository)).build();
    }

    @Given("A card with ID {int} exists in the database.")
    public void aCardWithIDExistsInTheDatabase(Integer id) throws Exception {
        address = new Address(
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

        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(id);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(id)).willReturn(Optional.ofNullable(marketplaceCard));

        assertThat(marketplaceCardRepository.findById(id)).isPresent();
    }

    @When("The user attempts to retrieve the details for the card with ID {int}.")
    public void theUserAttemptsToRetrieveTheDetailsForTheCardWithID(Integer id) throws Exception {
        when(marketplaceCardRepository.findById(id)).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(get(String.format("/cards/%d", id))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("The card with ID {int} is retrieved.")
    public void theCardWithIDIsRetrieved(Integer id) throws Exception {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                String.format(expectedCardJson, id, user.getId(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard.getSection().toString(),
                marketplaceCard.getCreated(), marketplaceCard.getDisplayPeriodEnd(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription(), marketplaceCard.getKeywords()));
    }

    @Given("There are three cards in each of the Wanted, For Sale, and Exchange sections.")
    public void there_are_three_cards_in_each_of_the_wanted_for_sale_and_exchange_sections() throws Exception {

        address = new Address(
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

        // ************* For Sale Events *************

        marketplaceCard1 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard1.setId(1);

        marketplaceCard2 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 2), LocalTime.of(0, 0)),
                "Card 2",
                "Card 2 description"
        );
        marketplaceCard2.setId(2);

        marketplaceCard3 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 3), LocalTime.of(0, 0)),
                "Card 3",
                "Card 3 description"
        );
        marketplaceCard3.setId(3);

        // ************* Wanted Events *************

        marketplaceCard4 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 4), LocalTime.of(0, 0)),
                "Card 4",
                "Card 4 description"
        );
        marketplaceCard4.setId(4);

        marketplaceCard5 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 5), LocalTime.of(0, 0)),
                "Card 5",
                "Card 5 description"
        );
        marketplaceCard5.setId(5);

        marketplaceCard6 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 6), LocalTime.of(0, 0)),
                "Card 6",
                "Card 6 description"
        );
        marketplaceCard6.setId(6);

        // ************* Exchange Events *************

        marketplaceCard7 = new MarketplaceCard(
                user.getId(),
                user,
                Section.EXCHANGE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 7), LocalTime.of(0, 0)),
                "Card 7",
                "Card 7 description"
        );
        marketplaceCard7.setId(7);

        marketplaceCard8 = new MarketplaceCard(
                user.getId(),
                user,
                Section.EXCHANGE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 7), LocalTime.of(0, 0)),
                "Card 8",
                "Card 8 description"
        );
        marketplaceCard8.setId(8);

        marketplaceCard9 = new MarketplaceCard(
                user.getId(),
                user,
                Section.EXCHANGE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 9), LocalTime.of(0, 0)),
                "Card 9",
                "Card 9 description"
        );
        marketplaceCard9.setId(9);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        List<MarketplaceCard> list = List.of(marketplaceCard1, marketplaceCard2, marketplaceCard3,
                marketplaceCard4, marketplaceCard5, marketplaceCard6, marketplaceCard7,
                marketplaceCard8, marketplaceCard9);
        Page<MarketplaceCard> pagedResponse = new PageImpl<>(list);
        Pageable paging = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created").ignoreCase()));
        given(marketplaceCardRepository.findAllBySection(Section.EXCHANGE, paging)).willReturn(pagedResponse);
        given(marketplaceCardRepository.findAllBySection(Section.WANTED, paging)).willReturn(pagedResponse);
        given(marketplaceCardRepository.findAllBySection(Section.FORSALE, paging)).willReturn(pagedResponse);

        assertThat(marketplaceCardRepository.findAllBySection(Section.EXCHANGE, paging)).isNotEmpty();
        assertThat(marketplaceCardRepository.findAllBySection(Section.WANTED, paging)).isNotEmpty();
        assertThat(marketplaceCardRepository.findAllBySection(Section.FORSALE, paging)).isNotEmpty();

    }

    @When("The user attempts to view the {string} section.")
    public void the_user_attempts_to_view_the_section(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("Only the {string} section cards are retrieved.")
    public void only_the_section_cards_are_retrieved(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("The most recently created \\(or renewed) items appear first.")
    public void the_most_recently_created_or_renewed_items_appear_first() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
