package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
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

public class MarketplaceDisplayStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    private MarketplaceCard marketplaceCard;
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
            "\"description\":\"%s\"" +
            "}";

    @Before
    public void createMockMvc() {
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        userRepository = mock(UserRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(marketplaceCardRepository, userRepository)).build();
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

        assertThat(marketplaceCardRepository.findById(id).isEmpty()).isFalse();
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
                marketplaceCard.getDescription()));
    }
}
