package org.seng302.marketplace;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.address.Address;
import org.seng302.address.AddressRepository;
import org.seng302.business.product.Product;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * MarketplaceCardResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class MarketplaceCardResourceIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MarketplaceCardRepository cardRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private KeywordRepository keywordRepository;


    private MockHttpServletResponse response;

    private final String cardPayloadJson = "{\"creatorId\":\"%d\"," +
            "\"section\":\"%s\"," +
            "\"title\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"keywords\":%s}";

    private String payloadJson;

    private User user;
    private MarketplaceCard card;

    /**
     * Before all create a user that will be used in all tests when creating cards.
     * @throws Exception thrown if there is an error when creating an address or user.
     */
    @BeforeAll
    public void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );

        user = new User("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );

        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                cardRepository, userRepository, addressRepository, keywordRepository))
                .build();
    }

    /**
     * Tests that a CREATED status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains a card with valid data.
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    public void canCreateCardWhenUserExistsAndDataValid() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));

        payloadJson = String.format(cardPayloadJson, card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription(),
                "[\"Vege\", \"Green\", \"Fresh\"]");

        given(cardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription()))
                .willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(cardRepository.save(any(MarketplaceCard.class))).thenReturn(card);
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains a card that already exists for an
     * existing creator ID.
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    public void cantCreateCardWhenCreatorExistsButCardAlreadyExists() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(cardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription()))
                .willReturn(Optional.ofNullable(card));
        payloadJson = String.format(cardPayloadJson, card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription(),
                "[\"Vege\", \"Green\", \"Fresh\"]");

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains invalid data and an existing creator ID.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    public void cantCreateCardWhenUserExistsButDataIsInvalid() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(cardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription()))
                .willReturn(Optional.empty());
        String title = "Title";
        title.repeat(20);
        payloadJson = String.format(cardPayloadJson, card.getCreatorId(), card.getSection(), title, card.getDescription(),
                "[\"Vege\", \"Green\", \"Fresh\"]");

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that an UNAUTHORIZED status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains valid data and an existing creator ID but with
     * no cookie.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    public void cantCreateCardWhenUserExistsAndDataValidWithNoCookie() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));

        payloadJson = String.format(cardPayloadJson, card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription(),
                "[\"Vege\", \"Green\", \"Fresh\"]");
        given(cardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription()))
                .willReturn(Optional.empty());
        // when
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that an UNAUTHORIZED status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains valid data and an existing creator ID but with
     * an invalid UUID.
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    public void cantCreateCardWhenUserExistsAndDataValidWithInvalidUUID() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(cardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription()))
                .willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, card.getCreatorId(), card.getSection(), card.getTitle(), card.getDescription(),
                "[\"Vege\", \"Green\", \"Fresh\"]");

        // when
        // The UUID is invalid because a user can't be found with the supplied UUID
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.empty());
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
