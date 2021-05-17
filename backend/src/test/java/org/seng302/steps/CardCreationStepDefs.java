package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.seng302.address.Address;
import org.seng302.address.AddressRepository;
import org.seng302.marketplace.MarketplaceCard;
import org.seng302.marketplace.MarketplaceCardRepository;
import org.seng302.marketplace.MarketplaceCardResource;
import org.seng302.marketplace.Section;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.seng302.user.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    private MockHttpServletResponse response;
    private User user;
    private Address address;
    private MarketplaceCard card;
    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);

        this.cardMVC = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository, userRepository, addressRepository)).build();
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
                "90210"
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
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
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
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
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
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }



}
