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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class CardExpirationStepDefs extends CucumberSpringConfiguration {

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

    private MockHttpServletResponse response;

    private User user;
    private MarketplaceCard card;
    private LocalDateTime initialDisplayPeriodEnd;


    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        keywordRepository = mock(KeywordRepository.class);

        this.cardMVC = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository, userRepository, keywordRepository)).build();
    }

    @Given("I am logged in and have already created a card.")
    public void iAmLoggedInAndHaveAlreadyCreatedACard() throws IllegalAddressArgumentException, IllegalUserArgumentException, IllegalMarketplaceCardArgumentException {
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

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        Assertions.assertTrue(userRepository.findBySessionUUID(user.getSessionUUID()).isPresent());

        initialDisplayPeriodEnd = LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0));
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                initialDisplayPeriodEnd,
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        card.setId(1);

        given(marketplaceCardRepository.findById(card.getId())).willReturn(Optional.ofNullable(card));
        Assertions.assertTrue(marketplaceCardRepository.findById(card.getId()).isPresent());
    }

    @When("I extend the display period of the card.")
    public void iExtendTheDisplayPeriodOfTheCard() throws Exception {
        response = cardMVC.perform(get(String.format("/cards/%d", card.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("I receive a 200 response and the display period end has been extended by two weeks.")
    public void iReceiveA200ReponseAndTheDisplayPeriodEndHasBeenExtendedByOneWeek() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Optional<MarketplaceCard> marketplaceCardOptional = marketplaceCardRepository.findById(card.getId());
        Assertions.assertTrue(marketplaceCardOptional.isPresent());

        MarketplaceCard marketplaceCard = marketplaceCardOptional.get();
        Assertions.assertEquals(initialDisplayPeriodEnd.plusWeeks(2), marketplaceCard.getDisplayPeriodEnd());
    }

}
