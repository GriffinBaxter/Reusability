package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.seng302.MainApplicationRunner;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    @Autowired
    @MockBean
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @Autowired
    private UserRepository unMockUserRepository;

    @Autowired
    private BusinessRepository unMockBusinessRepository;

    @Autowired
    private AddressRepository unMockAddressRepository;

    @Autowired
    private ProductRepository unMockProductRepository;

    @Autowired
    private MarketplaceCardRepository unMockMarketplaceCardRepository;

    @Autowired
    private MarketCardNotificationRepository unMockMarketCardNotificationRepository;

    private MockHttpServletResponse response;

    private MainApplicationRunner mainApplicationRunner;

    private User user;
    private User anotherUser;
    private MarketplaceCard card;
    private LocalDateTime initialDisplayPeriodEnd;
    private String notificationMessage;


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

    @Given("I am logged in as a user and have already created a card.")
    public void iAmLoggedInAsAUserAndHaveAlreadyCreatedACard() throws IllegalAddressArgumentException, IllegalUserArgumentException, IllegalMarketplaceCardArgumentException {
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
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

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

        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.of(anotherUser));
        Assertions.assertTrue(userRepository.findBySessionUUID(anotherUser.getSessionUUID()).isPresent());
    }

    @Given("I login as a DGAA.")
    public void i_login_as_a_dgaa() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

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

        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.of(anotherUser));
        Assertions.assertTrue(userRepository.findBySessionUUID(anotherUser.getSessionUUID()).isPresent());
    }

    @When("I extend the display period of the card.")
    public void iExtendTheDisplayPeriodOfTheCard() throws Exception {
        response = cardMVC.perform(get(String.format("/cards/%d", card.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("I receive a 200 response and the display period end has been extended by two weeks.")
    public void iReceiveA200ResponseAndTheDisplayPeriodEndHasBeenExtendedByOneWeek() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Optional<MarketplaceCard> marketplaceCardOptional = marketplaceCardRepository.findById(card.getId());
        Assertions.assertTrue(marketplaceCardOptional.isPresent());

        MarketplaceCard marketplaceCard = marketplaceCardOptional.get();
        Assertions.assertEquals(initialDisplayPeriodEnd.plusWeeks(2), marketplaceCard.getDisplayPeriodEnd());
    }

    @When("I delete a card.")
    public void i_delete_a_card() throws Exception {
        response = cardMVC.perform(delete(String.format("/cards/%d", card.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
        when(marketplaceCardRepository.findById(card.getId())).thenReturn(Optional.empty());
    }

    @Then("I receive a 200 response and the card has been deleted.")
    public void i_receive_a_response_and_the_card_has_been_deleted() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        Optional<MarketplaceCard> marketplaceCardOptional = marketplaceCardRepository.findById(card.getId());
        Assertions.assertTrue(marketplaceCardOptional.isEmpty());
    }

    @Given("A card has been in the maximum display period.")
    public void a_card_has_been_in_the_maximum_display_period() throws Exception {
        mainApplicationRunner = new MainApplicationRunner(
                unMockUserRepository,
                unMockBusinessRepository,
                unMockAddressRepository,
                unMockProductRepository,
                unMockMarketplaceCardRepository,
                unMockMarketCardNotificationRepository);

        notificationMessage = "Your card (Hayley's Birthday) has been expired 0h 0m 1s ago.";
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        unMockAddressRepository.save(address);

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
        unMockUserRepository.save(user);

        Assertions.assertTrue(unMockUserRepository.findBySessionUUID(user.getSessionUUID()).isPresent());

        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusWeeks(2),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        card.setId(1);
        unMockMarketplaceCardRepository.save(card);

        Assertions.assertTrue(unMockMarketplaceCardRepository.findById(card.getId()).isPresent());
        Assertions.assertTrue(unMockMarketplaceCardRepository.findById(card.getId()).get().getDisplayPeriodEnd()
                .isBefore(LocalDateTime.now()));
    }

    @Transactional
    @When("check function been called.")
    public void check_function_been_called() {
        mainApplicationRunner.checkNotifications();
    }

    @Then("I will receive a notification for the expired.")
    public void i_will_receive_a_notification_for_the_expired() {
        Assertions.assertTrue(unMockMarketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), card.getId()).isPresent());
        Assertions.assertEquals(notificationMessage, unMockMarketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), card.getId()).get().getDescription());
    }

    @Given("I have no action within 24h for an expired card")
    public void i_have_no_action_within_24h_for_an_expired_card() {
        notificationMessage = "Your card (Hayley's Birthday) has been deleted.";
        LocalDateTime originalTime = card.getDisplayPeriodEnd();
        card.setDisplayPeriodEnd(originalTime.minusHours(24));
        unMockMarketplaceCardRepository.save(card);
    }

    @Then("The card will been deleted")
    public void the_card_will_been_deleted() {
        Assertions.assertTrue(unMockMarketplaceCardRepository.findById(card.getId()).isEmpty());
    }

    @Then("I will receive a notification for the deletion.")
    public void i_will_receive_a_notification_for_the_deletion() {
        List<MarketCardNotification> marketCardNotifications = unMockMarketCardNotificationRepository.findAllByUserId(user.getId());
        List<String> descriptions = new ArrayList<>();
        for (MarketCardNotification marketCardNotification : marketCardNotifications) {
            descriptions.add(marketCardNotification.getDescription());
        }
        Assertions.assertTrue(descriptions.contains(notificationMessage));
    }
}
