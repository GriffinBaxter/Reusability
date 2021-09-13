package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.model.Address;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.MarketplaceCardRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.*;

/**
 * Marketplace display step definitions class
 */
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

    @Autowired
    @MockBean
    private MarketCardNotificationRepository marketCardNotificationRepository;


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
    private MarketplaceCard marketplaceCard10;
    private MarketplaceCard marketplaceCard11;
    private MarketplaceCard marketplaceCard12;

    private String title1;
    private String title2;
    private String title3;
    private String location1;
    private String location2;
    private String location3;

    private User user;
    private User user2;
    private User user3;
    private User user4;
    private Address address;
    private Address address2;
    private Address address3;
    private Address address4;

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
            "\"images\":[]," +
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
        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository, userRepository, keywordRepository, marketCardNotificationRepository
        )).build();
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
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 8), LocalTime.of(0, 0)),
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

        List<MarketplaceCard> listForSale = List.of(marketplaceCard1, marketplaceCard2, marketplaceCard3);
        List<MarketplaceCard> listWanted = List.of(marketplaceCard4, marketplaceCard5, marketplaceCard6);
        List<MarketplaceCard> listExchange = List.of(marketplaceCard7, marketplaceCard8, marketplaceCard9);

        Page<MarketplaceCard> pagedResponse = new PageImpl<>(list);
        Page<MarketplaceCard> pagedResponseForSale = new PageImpl<>(listForSale);
        Page<MarketplaceCard> pagedResponseWanted = new PageImpl<>(listWanted);
        Page<MarketplaceCard> pagedResponseExchange = new PageImpl<>(listExchange);

        given(marketplaceCardRepository.findAllBySection(eq(Section.EXCHANGE), any(Pageable.class))).willReturn(pagedResponseExchange);
        given(marketplaceCardRepository.findAllBySection(eq(Section.WANTED), any(Pageable.class))).willReturn(pagedResponseWanted);
        given(marketplaceCardRepository.findAllBySection(eq(Section.FORSALE), any(Pageable.class))).willReturn(pagedResponseForSale);

        Pageable paging = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("created").ignoreCase()));

        assertThat(marketplaceCardRepository.findAllBySection(Section.EXCHANGE, paging)).isNotEmpty();
        assertThat(marketplaceCardRepository.findAllBySection(Section.WANTED, paging)).isNotEmpty();
        assertThat(marketplaceCardRepository.findAllBySection(Section.FORSALE, paging)).isNotEmpty();

    }

    @When("The user attempts to view the {string} section.")
    public void the_user_attempts_to_view_the_section(String sectionStr) throws Exception {

        // Remove space for valid enum conversion.
        if (sectionStr.equals("For Sale")) {
            sectionStr = "FORSALE";
        }

        response = mvc.perform(get("/cards")
                .param("section", sectionStr)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("Only the {string} section cards are retrieved in the correct order \\(recently created\\/renewed first).")
    public void only_the_section_cards_are_retrieved_in_the_correct_order_recently_created_renewed_first(String sectionStr) throws Exception {

        String forSaleCardJSON = "[" +
                                String.format(expectedCardJson, marketplaceCard1.getId(), user.getId(), user.getFirstName(),
                                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard1.getSection().toString(),
                                        marketplaceCard1.getCreated(), marketplaceCard1.getDisplayPeriodEnd(), marketplaceCard1.getTitle(),
                                        marketplaceCard1.getDescription(), marketplaceCard1.getKeywords()) +
                                "," +
                                String.format(expectedCardJson, marketplaceCard2.getId(), user.getId(), user.getFirstName(),
                                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard2.getSection().toString(),
                                        marketplaceCard2.getCreated(), marketplaceCard2.getDisplayPeriodEnd(), marketplaceCard2.getTitle(),
                                        marketplaceCard2.getDescription(), marketplaceCard2.getKeywords()) +
                                "," +
                                String.format(expectedCardJson, marketplaceCard3.getId(), user.getId(), user.getFirstName(),
                                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard3.getSection().toString(),
                                        marketplaceCard3.getCreated(), marketplaceCard3.getDisplayPeriodEnd(), marketplaceCard3.getTitle(),
                                        marketplaceCard3.getDescription(), marketplaceCard3.getKeywords()) +
                                "]";

        String wantCardJSON = "[" +
                String.format(expectedCardJson, marketplaceCard4.getId(), user.getId(), user.getFirstName(),
                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard4.getSection().toString(),
                        marketplaceCard4.getCreated(), marketplaceCard4.getDisplayPeriodEnd(), marketplaceCard4.getTitle(),
                        marketplaceCard4.getDescription(), marketplaceCard4.getKeywords()) +
                "," +
                String.format(expectedCardJson, marketplaceCard5.getId(), user.getId(), user.getFirstName(),
                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard5.getSection().toString(),
                        marketplaceCard5.getCreated(), marketplaceCard5.getDisplayPeriodEnd(), marketplaceCard5.getTitle(),
                        marketplaceCard5.getDescription(), marketplaceCard5.getKeywords()) +
                "," +
                String.format(expectedCardJson, marketplaceCard6.getId(), user.getId(), user.getFirstName(),
                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard6.getSection().toString(),
                        marketplaceCard6.getCreated(), marketplaceCard6.getDisplayPeriodEnd(), marketplaceCard6.getTitle(),
                        marketplaceCard6.getDescription(), marketplaceCard6.getKeywords()) +
                "]";

        String exchangeCardJSON = "[" +
                String.format(expectedCardJson, marketplaceCard7.getId(), user.getId(), user.getFirstName(),
                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard7.getSection().toString(),
                        marketplaceCard7.getCreated(), marketplaceCard7.getDisplayPeriodEnd(), marketplaceCard7.getTitle(),
                        marketplaceCard7.getDescription(), marketplaceCard7.getKeywords()) +
                "," +
                String.format(expectedCardJson, marketplaceCard8.getId(), user.getId(), user.getFirstName(),
                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard8.getSection().toString(),
                        marketplaceCard8.getCreated(), marketplaceCard8.getDisplayPeriodEnd(), marketplaceCard8.getTitle(),
                        marketplaceCard8.getDescription(), marketplaceCard8.getKeywords()) +
                "," +
                String.format(expectedCardJson, marketplaceCard9.getId(), user.getId(), user.getFirstName(),
                        user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                        user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard9.getSection().toString(),
                        marketplaceCard9.getCreated(), marketplaceCard9.getDisplayPeriodEnd(), marketplaceCard9.getTitle(),
                        marketplaceCard9.getDescription(), marketplaceCard9.getKeywords()) +
                "]";

        String comparisonString = null;
        switch (sectionStr) {
            case "For Sale":
                comparisonString = forSaleCardJSON;
                break;
            case "Wanted":
                comparisonString = wantCardJSON;
                break;
            case "Exchange":
                comparisonString = exchangeCardJSON;
                break;
        }

        assertThat(response.getContentAsString()).isEqualTo(comparisonString);

    }

    @Given("There are three cards with titles {string}, {string}, {string}.")
    public void there_are_three_cards_with_titles(String inputTitle1, String inputTitle2, String inputTitle3) throws Exception {
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

        marketplaceCard1 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                inputTitle1,
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard1.setId(1);

        marketplaceCard2 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 2), LocalTime.of(0, 0)),
                inputTitle2,
                "Card 2 description"
        );
        marketplaceCard2.setId(2);

        marketplaceCard3 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 3), LocalTime.of(0, 0)),
                inputTitle3,
                "Card 3 description"
        );
        marketplaceCard3.setId(3);

        title1 = inputTitle1;
        title2 = inputTitle2;
        title3 = inputTitle3;

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

    }

    @When("The user attempts to order the cards by title in {string} order.")
    public void the_user_attempts_to_order_the_cards_by_title_in_order(String sortBy) throws Exception {
        ArrayList<MarketplaceCard> list = new ArrayList<>();
        list.add(marketplaceCard1);
        list.add(marketplaceCard2);
        list.add(marketplaceCard3);

        Sort sort = null;
        if (sortBy.equals("ascending")) {
            sort = Sort.by(Sort.Order.asc("title").ignoreCase());
            list.sort(Comparator.comparing(MarketplaceCard::getTitle));
        } else {
            list.sort(Comparator.comparing(MarketplaceCard::getTitle));
            Collections.reverse(list);
            sort = Sort.by(Sort.Order.desc("title").ignoreCase());
        }

        Page<MarketplaceCard> pagedResponse = new PageImpl<>(list);
        given(marketplaceCardRepository.findAllBySection(any(Section.class), any(Pageable.class))).willReturn(pagedResponse);
        Pageable paging = PageRequest.of(0, 10, sort);
        assertThat(marketplaceCardRepository.findAllBySection(Section.EXCHANGE, paging)).isNotEmpty();

        response = mvc.perform(get("/cards")
                .param("section", "Wanted")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("The retrieved cards are ordered by title in {string} order.")
    public void the_retrieved_cards_are_ordered_by_title_in_order(String direction) throws Exception {

        // Titles from response
        String[] titleArray = response.getContentAsString().split("\"");
        ArrayList<String> responseTitles = new ArrayList<>();
        for (int i = 0; i < titleArray.length; i++) {
            if (titleArray[i].equals("title")) {
                responseTitles.add(titleArray[i+2]);
            }
        }

        // Titles from GIVEN, manually sorted.
        ArrayList<String> orderedTitles = new ArrayList<>();
        orderedTitles.add(title1);
        orderedTitles.add(title2);
        orderedTitles.add(title3);
        Collections.sort(orderedTitles);

        if (direction.equals("descending")) {
            Collections.reverse(orderedTitles);
        }

        assertThat(responseTitles).isEqualTo(orderedTitles);
    }

    @Given("There are three cards and the location of the creator associated with them are {string}, {string}, {string} {string}, {string}, {string}.")
    public void there_are_three_cards_and_the_location_of_the_creator_associated_with_them_are(String suburb1, String city1, String suburb2, String city2, String suburb3, String city3) throws Exception {

        address2 = new Address(
                "3/24",
                "Ilam Road",
                city1,
                "Canterbury",
                "New Zealand",
                "90210",
                suburb1
        );

        user2 = new User(
                "Johnny",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address2,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user2.setId(2);
        user2.setSessionUUID(User.generateSessionUUID());

        address3 = new Address(
                "3/24",
                "Some Road",
                city2,
                "Wellington",
                "New Zealand",
                "90210",
                suburb2
        );

        user3 = new User(
                "Johnson",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address3,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user3.setId(3);
        user3.setSessionUUID(User.generateSessionUUID());

        address4 = new Address(
                "3/24",
                "Some Road",
                city3,
                "Wellington",
                "New Zealand",
                "90210",
                suburb3
        );

        user4 = new User(
                "Jimmy",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address4,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user4.setId(4);
        user4.setSessionUUID(User.generateSessionUUID());


        // ********************************************************

        marketplaceCard10 = new MarketplaceCard(
                user2.getId(),
                user2,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "inputTitle1",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard10.setId(10);

        marketplaceCard11 = new MarketplaceCard(
                user3.getId(),
                user3,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 2), LocalTime.of(0, 0)),
                "inputTitle2",
                "Card 2 description"
        );
        marketplaceCard11.setId(11);

        marketplaceCard12 = new MarketplaceCard(
                user4.getId(),
                user4,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 3), LocalTime.of(0, 0)),
                "inputTitle3",
                "Card 3 description"
        );
        marketplaceCard12.setId(12);

        given(userRepository.findBySessionUUID(user2.getSessionUUID())).willReturn(Optional.ofNullable(user2));
        given(userRepository.findBySessionUUID(user3.getSessionUUID())).willReturn(Optional.ofNullable(user3));
        given(userRepository.findBySessionUUID(user4.getSessionUUID())).willReturn(Optional.ofNullable(user4));

        location1 = user2.getHomeAddress().getSuburb() + user2.getHomeAddress().getCity();
        location2 = user3.getHomeAddress().getSuburb() + user3.getHomeAddress().getCity();
        location3 = user4.getHomeAddress().getSuburb() + user4.getHomeAddress().getCity();

    }

    @When("The user attempts to order the cards by location in {string} order.")
    public void the_user_attempts_to_order_the_cards_by_location_in_order(String sortBy) throws Exception {

        ArrayList<MarketplaceCard> list = new ArrayList<>();
        list.add(marketplaceCard10);
        list.add(marketplaceCard11);
        list.add(marketplaceCard12);

        Sort sort = null;

        Collections.sort(list, (option1, option2) ->
        {
            try {
                return (option1.getCreator().getHomeAddress().getSuburb() + option1.getCreator().getHomeAddress().getCity())
                        .compareTo(option2.getCreator().getHomeAddress().getSuburb() + option2.getCreator().getHomeAddress().getCity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });

        if (sortBy.equals("ascending")) {
            sort = Sort.by(Sort.Order.asc("creator.homeAddress.suburb").ignoreCase()).and(Sort.by(Sort.Order.asc("creator.homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.desc("created").ignoreCase())));
        } else {
            Collections.reverse(list);
            sort = Sort.by(Sort.Order.desc("creator.homeAddress.suburb").ignoreCase()).and(Sort.by(Sort.Order.desc("creator.homeAddress.city").ignoreCase()).and(Sort.by(Sort.Order.desc("created").ignoreCase())));
        }

        Page<MarketplaceCard> pagedResponse = new PageImpl<>(list);
        given(marketplaceCardRepository.findAllBySection(any(Section.class), any(Pageable.class))).willReturn(pagedResponse);
        Pageable paging = PageRequest.of(0, 10, sort);
        assertThat(marketplaceCardRepository.findAllBySection(Section.EXCHANGE, paging)).isNotEmpty();

        response = mvc.perform(get("/cards")
                .param("section", "Wanted")
                .cookie(new Cookie("JSESSIONID", user2.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("The retrieved cards are ordered by location in {string} order.")
    public void the_retrieved_cards_are_ordered_by_location_in_order(String direction) throws UnsupportedEncodingException {

        // Locations from response
        String[] locationArray = response.getContentAsString().split("\"");
        ArrayList<String> responseLocations = new ArrayList<>();
        for (int i = 0; i < locationArray.length; i++) {
            if (locationArray[i].equals("suburb")) {
                int suburbIndex = i + 2;
                int cityIndex = i + 6;
                String suburbAndCity = locationArray[suburbIndex] + locationArray[cityIndex];
                responseLocations.add(suburbAndCity);
            }
        }

        // Locations from GIVEN, manually sorted.
        ArrayList<String> orderedLocations = new ArrayList<>();
        orderedLocations.add(location1);
        orderedLocations.add(location2);
        orderedLocations.add(location3);
        Collections.sort(orderedLocations);

        if (direction.equals("descending")) {
            Collections.reverse(orderedLocations);
        }

        assertThat(responseLocations).isEqualTo(orderedLocations);
    }

}
