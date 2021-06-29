package org.seng302.marketplace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.model.Address;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.MarketplaceCardRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * MarketplaceCardResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class MarketplaceCardResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

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
    private User anotherUser;
    private MarketplaceCard marketplaceCard;
    private MarketplaceCard anotherMarketplaceCard;
    private User gaa;

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

    private String expectedJson;

    /**
     * Before each create a user that will be used in all tests when creating cards.
     * @throws Exception thrown if there is an error when creating an address or user.
     */
    @BeforeEach
    void setup() throws Exception {
        Address address = new Address(
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
                Role.USER);
        anotherUser.setId(2);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        gaa = new User("Global",
                "Admin",
                "Application",
                "GAA",
                "bio",
                "gaa@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        gaa.setId(3);
        gaa.setSessionUUID(User.generateSessionUUID());

        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(1);

        anotherMarketplaceCard = new MarketplaceCard(
                anotherUser.getId(),
                anotherUser,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        anotherMarketplaceCard.setId(2);

        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository, userRepository, keywordRepository))
                .build();
    }

    /**
     * Tests that a CREATED status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains a card with valid data.
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canCreateCardWhenUserExistsAndDataValid() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(), marketplaceCard.getDescription(),
                "[\"Vege\", \"Green\", \"Fresh\"]");

        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(), marketplaceCard.getDescription()))
                .willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(marketplaceCard);
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
    void cantCreateCardWhenCreatorExistsButCardAlreadyExists() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(), marketplaceCard.getDescription()))
                .willReturn(Optional.ofNullable(marketplaceCard));
        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(), marketplaceCard.getDescription(),
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
    void cantCreateCardWhenUserExistsButDataIsInvalid() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription())).willReturn(Optional.empty());
        String title = "Title";

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                title, marketplaceCard.getDescription(), "[\"Vege\", \"Green\", \"Fresh\"]");

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
    void cantCreateCardWhenUserExistsAndDataValidWithNoCookie() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[\"Vege\", \"Green\", \"Fresh\"]");
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription())).willReturn(Optional.empty());
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
    void cantCreateCardWhenUserExistsAndDataValidWithInvalidUUID() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription())).willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[\"Vege\", \"Green\", \"Fresh\"]");

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

    /**
     * Tests that a FORBIDDEN status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains valid data and an existing creator ID for another user but the current user
     * is not a GAA or DGAA.
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantCreateCardWhenCreatorIdIsForAnotherUserWithUserNotBeingAGAAOrDGAA() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                anotherMarketplaceCard.getCreatorId(), anotherMarketplaceCard.getSection(), anotherMarketplaceCard.getTitle(),
                anotherMarketplaceCard.getDescription())).willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, anotherMarketplaceCard.getCreatorId(), anotherMarketplaceCard.getSection(),
                anotherMarketplaceCard.getTitle(), anotherMarketplaceCard.getDescription(), "[\"Vege\", \"Green\", \"Fresh\"]");

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Tests that a CREATED status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains valid data and an existing creator ID for another user and the current user
     * is a GAA.
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canCreateCardWhenCreatorIdIsForAnotherUserWithUserBeingGAA() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                anotherMarketplaceCard.getCreatorId(), anotherMarketplaceCard.getSection(), anotherMarketplaceCard.getTitle(),
                anotherMarketplaceCard.getDescription())).willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, anotherMarketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[\"Vege\", \"Green\", \"Fresh\"]");

        when(userRepository.findBySessionUUID(gaa.getSessionUUID())).thenReturn(Optional.ofNullable(gaa));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(anotherMarketplaceCard);
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", gaa.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that an OK status and a marketplace card is received when the card ID in the /cards/{id} API endpoint exists.
     * Test specifically for when the cookie contains a valid UUID.
     */
    @Test
    void canRetrieveCardWhenCardExistsWithValidCookie() throws Exception {
        // given
        expectedJson = String.format(expectedCardJson, marketplaceCard.getId(), user.getId(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard.getSection().toString(),
                marketplaceCard.getCreated(), marketplaceCard.getDisplayPeriodEnd(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription(), marketplaceCard.getKeywords());
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(get(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an UNAUTHORIZED status is received when the marketplace card ID in the /cards/{id} API endpoint exists
     * but the cookie contains an invalid UUID
     */
    @Test
    void cantRetrieveCardWhenCardExistsWithInvalidCookie() throws Exception {
        // given
        String nonExistingSessionUUID = User.generateSessionUUID();
        given(userRepository.findBySessionUUID(nonExistingSessionUUID)).willReturn(Optional.empty());
        expectedJson = "";

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(get(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", nonExistingSessionUUID)))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an UNAUTHORIZED status is received when the marketplace card ID in the /cards/{id} API endpoint exists
     * but there is no cookie
     */
    @Test
    void cantRetrieveCardWhenCardExistsWithNoCookie() throws Exception {
        // given
        expectedJson = "";

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(get(String.format("/cards/%d", marketplaceCard.getId())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when the marketplace card ID in
     * the /cards/{id} API endpoint does not exist
     */
    @Test
    void cantRetrieveCardWhenCardDoesntExist() throws Exception {
        // given
        expectedJson = "";
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        when(marketplaceCardRepository.findById(0)).thenReturn(Optional.empty());
        response = mvc.perform(get(String.format("/cards/%d", 0))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a BAD_REQUEST status is received when the marketplace card ID provided to
     * the /cards/{id} API endpoint is invalid, i.e. is not an integer.
     */
    @Test
    void cantRetrieveCardWithInvalidId() throws Exception {
        // given
        expectedJson = "";
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        response = mvc.perform(get(String.format("/cards/%s", "a"))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    // ------------------- GET ALL (by SECTION) -------------------

    /**
     * Tests that an OK status and marketplace cards are received when the Section is valid.
     * Test specifically for when the cookie contains a valid UUID.
     */
    @Test
    void canRetrieveCardsWithValidSectionAndCookie() throws Exception {
        // given
        expectedJson = "[" + String.format(expectedCardJson, marketplaceCard.getId(), user.getId(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard.getSection().toString(),
                marketplaceCard.getCreated(), marketplaceCard.getDisplayPeriodEnd(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription(), marketplaceCard.getKeywords()) + "]";

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        Sort sort = Sort.by(Sort.Order.desc("created").ignoreCase());
        Pageable page = PageRequest.of(0, 6, sort);

        List<MarketplaceCard> list = List.of(marketplaceCard);
        Page<MarketplaceCard> pagedResponse = new PageImpl<>(list);

        // when
        when(marketplaceCardRepository.findAllBySection(Section.FORSALE, page)).thenReturn(pagedResponse);
        response = mvc.perform(get("/cards").param("section", "FORSALE")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and marketplace cards are received when the Section is valid.
     * Test specifically for when the cookie contains a valid UUID and orderBy and page are valid.
     */
    @Test
    void canRetrieveCardsWithValidSectionAndCookieAndOrderByAndPage() throws Exception {
        // given
        expectedJson = "[" + String.format(expectedCardJson, marketplaceCard.getId(), user.getId(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard.getSection().toString(),
                marketplaceCard.getCreated(), marketplaceCard.getDisplayPeriodEnd(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription(), marketplaceCard.getKeywords()) + "]";

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        Sort sort = Sort.by(Sort.Order.asc("created").ignoreCase());
        Pageable page = PageRequest.of(0, 6, sort);

        List<MarketplaceCard> list = List.of(marketplaceCard);
        Page<MarketplaceCard> pagedResponse = new PageImpl<>(list);

        // when
        when(marketplaceCardRepository.findAllBySection(Section.FORSALE, page)).thenReturn(pagedResponse);
        response = mvc.perform(get("/cards").param("section", "FORSALE")
                .param("orderBy", "createdASC").param("page", "0")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that the user cannot retrieve cards with an invalid JSESSIONID
     */
    @Test
    void cantRetrieveCardsWithInvalidJSESSIONID() throws Exception {
        // given
        String fakeSessionID = "xxx";
        given(userRepository.findBySessionUUID(fakeSessionID)).willReturn(Optional.empty());

        // when
        response = mvc.perform(get("/cards").param("section", "FORSALE")
                .cookie(new Cookie("JSESSIONID", fakeSessionID))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that the user cannot retrieve cards without a invalid JSESSIONID
     */
    @Test
    void cantRetrieveCardsWithNoJSESSIONID() throws Exception {
        // given
        given(userRepository.findBySessionUUID(null)).willReturn(Optional.empty());

        // when
        response = mvc.perform(get("/cards").param("section", "FORSALE")).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that the user cannot retrieve cards with an invalid Page number parameter
     */
    @Test
    void cantRetrieveCardsWithInvalidPage() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        response = mvc.perform(get("/cards").param("section", "FORSALE")
                .param("orderBy", "createdASC").param("page", "fd")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that the user cannot retrieve cards with an invalid OrderBy parameter
     */
    @Test
    void cantRetrieveCardsWithInvalidOrderBy() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        response = mvc.perform(get("/cards").param("section", "FORSALE")
                .param("orderBy", "qwerty")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that the user cannot retrieve cards with an invalid Section parameter
     */
    @Test
    void cantRetrieveCardsWithInvalidSection() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        response = mvc.perform(get("/cards").param("section", "SECTION")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
