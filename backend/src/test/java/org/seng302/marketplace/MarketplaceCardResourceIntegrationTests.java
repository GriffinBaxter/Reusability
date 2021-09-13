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
import org.seng302.model.repository.MarketCardNotificationRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @MockBean
    private KeywordRepository keywordRepository;

    private MockHttpServletResponse response;

    private final String cardPayloadJson = "{\"creatorId\":\"%d\"," +
            "\"section\":\"%s\"," +
            "\"title\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"keywordIds\":%s}";

    private final String editCardPayloadJson = "{" +
            "\"title\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"keywordIds\":%s}";

    private String payloadJson;

    private User user;
    private User anotherUser;
    private MarketplaceCard marketplaceCard;
    private MarketplaceCard anotherMarketplaceCard;
    private User gaa;
    private User dgaa;
    private Keyword keyword;

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
            "\"images\":[],"+
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
     *
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
                "Middle",
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

        dgaa = new User("Default",
                "Admin",
                "Application",
                "DGAA",
                "bio",
                "dgaa@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        dgaa.setId(4);
        dgaa.setSessionUUID(User.generateSessionUUID());

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
                LocalDateTime.of(LocalDate.of(2022, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        anotherMarketplaceCard.setId(2);

        keyword = new Keyword("Resource", LocalDateTime.now());
        keyword.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository, userRepository, keywordRepository, marketCardNotificationRepository))
                .build();
    }

    // -------------------------------------------- CREATE ONE NEW CARD ------------------------------------------------

    /**
     * Tests that a CREATED status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains a card with valid data.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canCreateCardWhenUserExistsAndDataValid() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[" + keyword.getId() + "]");

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
     * /cards API endpoint that contains a card that already exists for an existing creator ID.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantCreateCardWhenCreatorExistsButCardAlreadyExists() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(), marketplaceCard.getDescription()))
                .willReturn(Optional.ofNullable(marketplaceCard));
        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(), marketplaceCard.getDescription(),
                "[" + keyword.getId() + "]");

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
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription())).willReturn(Optional.empty());
        String string = "Title";
        String title = string.repeat(20); // max length of title is 70, so invalid (current length = 100)

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                title, marketplaceCard.getDescription(), "[" + keyword.getId() + "]");


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
     * Tests that a BAD_REQUEST status is received when sending a marketplace card creation payload to the
     * /cards API endpoint that contains an existing creator ID and valid data but a keyword doesn't exist.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantCreateCardWhenUserExistsAndDataIsValidButKeywordDoesntExist() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription())).willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[" + keyword.getId() + ", 5]");

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
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[" + keyword.getId() + "]");
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
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantCreateCardWhenUserExistsAndDataValidWithInvalidUUID() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                marketplaceCard.getCreatorId(), marketplaceCard.getSection(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription())).willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, marketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[" + keyword.getId() + "]");

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
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantCreateCardWhenCreatorIdIsForAnotherUserWithUserNotBeingAGAAOrDGAA() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(anotherUser));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                anotherMarketplaceCard.getCreatorId(), anotherMarketplaceCard.getSection(), anotherMarketplaceCard.getTitle(),
                anotherMarketplaceCard.getDescription())).willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, anotherMarketplaceCard.getCreatorId(), anotherMarketplaceCard.getSection(),
                anotherMarketplaceCard.getTitle(), anotherMarketplaceCard.getDescription(), "[" + keyword.getId() + "]");

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
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canCreateCardWhenCreatorIdIsForAnotherUserWithUserBeingGAA() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(anotherUser));
        given(keywordRepository.findById(keyword.getId())).willReturn(Optional.ofNullable(keyword));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorIdAndSectionAndTitleAndDescription(
                anotherMarketplaceCard.getCreatorId(), anotherMarketplaceCard.getSection(), anotherMarketplaceCard.getTitle(),
                anotherMarketplaceCard.getDescription())).willReturn(Optional.empty());

        payloadJson = String.format(cardPayloadJson, anotherMarketplaceCard.getCreatorId(), marketplaceCard.getSection(),
                marketplaceCard.getTitle(), marketplaceCard.getDescription(), "[" + keyword.getId() + "]");

        when(userRepository.findBySessionUUID(gaa.getSessionUUID())).thenReturn(Optional.ofNullable(gaa));
        when(marketplaceCardRepository.save(any(MarketplaceCard.class))).thenReturn(anotherMarketplaceCard);
        response = mvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", gaa.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    // ------------------------------------------- GET ONE (by CARD ID) ------------------------------------------------

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

    // ------------------------------------------- GET ALL (by SECTION) ------------------------------------------------

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

    /**
     * Tests that the user can extend the display period of a card with a valid cookie when
     * they are the creator of the card and the card exists, and that an OK response is received.
     */
    @Test
    void canExtendDisplayPeriodAsCreatorWhenCardExists() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(put(String.format("/cards/%d/extenddisplayperiod", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that the user can extend the display period of a card with a valid cookie when
     * they are not the creator of the card but are a DGAA and the card exists, and that an OK response is received.
     */
    @Test
    void canExtendDisplayPeriodAsDGAAWhenCardExists() throws Exception {
        // given
        given(userRepository.findBySessionUUID(dgaa.getSessionUUID())).willReturn(Optional.ofNullable(dgaa));

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(put(String.format("/cards/%d/extenddisplayperiod", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", dgaa.getSessionUUID()))).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that the user can extend the display period of a card with a valid cookie when
     * they are not the creator of the card but are a DGAA and the card exists, and that an OK response is received.
     */
    @Test
    void canExtendDisplayPeriodAsGAAWhenCardExists() throws Exception {
        // given
        given(userRepository.findBySessionUUID(gaa.getSessionUUID())).willReturn(Optional.ofNullable(gaa));

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(put(String.format("/cards/%d/extenddisplayperiod", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", gaa.getSessionUUID()))).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that the user cannot extend the display period of a card with an invalid cookie
     * and that an UNAUTHORIZED response is received.
     */
    @Test
    void cantExtendDisplayPeriodWithInvalidCookie() throws Exception {
        // when
        response = mvc.perform(put(String.format("/cards/%d/extenddisplayperiod", 1))
                .cookie(new Cookie("JSESSIONID", "0"))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that the user cannot extend the display period of a card with no cookie
     * and that an UNAUTHORIZED response is received.
     */
    @Test
    void cantExtendDisplayPeriodWithNoCookie() throws Exception {
        // when
        response = mvc.perform(put(String.format("/cards/%d/extenddisplayperiod", 1))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that the user cannot extend the display period of a card when they are neither
     * a global application admin or the creator of the card and that a FORBIDDEN response is received.
     */
    @Test
    void cantExtendDisplayPeriodWhenNotCreatorAndNotAdmin() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));
        response = mvc.perform(put(String.format("/cards/%d/extenddisplayperiod", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Tests that the user cannot extend the display period of a card when the card with the
     * provided ID does not exist and that a NOT_ACCEPTABLE response is received.
     */
    @Test
    void cantExtendDisplayPeriodWhenCardDoesNotExist() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));

        // when
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/cards/%d/extenddisplayperiod", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }


    // ---------------------------------------- DELETE ONE CARD (by CARD ID) -------------------------------------------

    /**
     * Test that the creator can delete his card. Return OK (200).
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canDeleteAExistCardWithCreatorCookie() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        // when
        response = mvc.perform(delete(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that a GAA can delete his card. Return OK (200).
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canDeleteAExistCardWithGAACookie() throws Exception {
        // given
        anotherUser.setRole(Role.GLOBALAPPLICATIONADMIN);
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        // when
        response = mvc.perform(delete(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that the DGAA can delete his card. Return OK (200).
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canDeleteAExistCardWithDGAACookie() throws Exception {
        // given
        anotherUser.setRole(Role.DEFAULTGLOBALAPPLICATIONADMIN);
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        // when
        response = mvc.perform(delete(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that card will not been delete when no user login (no session token). Return UNAUTHORIZED (401).
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantDeleteAExistCardWithNoCookie() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        // when
        response = mvc.perform(delete(String.format("/cards/%d", marketplaceCard.getId()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test that card will not been delete when current user (session token) is a USER and not the creator of this card.
     * Return FORBIDDEN (403).
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantDeleteAExistCardWithOtherUserCookie() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        // when
        response = mvc.perform(delete(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test that the creator can delete his card. Return OK (200).
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantDeleteAExistCardWithCreatorCookie() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        // when
        response = mvc.perform(delete(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that the creator can delete his card. Return OK (200).
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cantDeleteANotExistCardWithCreatorCookie() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.empty());

        // when
        response = mvc.perform(delete(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    // --------------------------------------- GET ACTIVE CARDS (by USER ID) ------------------------------------------

    /**
     * Tests that the active cards from a given user can be retrieved with an OK response.
     */
    @Test
    void canRetrieveUsersActiveCardsWhenUserExists() throws Exception {
        // given
        expectedJson = "[" + String.format(expectedCardJson, anotherMarketplaceCard.getId(), anotherUser.getId(),
                anotherUser.getFirstName(), anotherUser.getLastName(), anotherUser.getMiddleName(),
                anotherUser.getNickname(), anotherUser.getBio(), anotherUser.getEmail(), anotherUser.getCreated(),
                anotherUser.getRole(), anotherUser.getHomeAddress().toSecureString(),
                anotherMarketplaceCard.getSection().toString(), anotherMarketplaceCard.getCreated(),
                anotherMarketplaceCard.getDisplayPeriodEnd(), anotherMarketplaceCard.getTitle(),
                anotherMarketplaceCard.getDescription(), anotherMarketplaceCard.getKeywords()) + "]";

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorId(anotherUser.getId())).willReturn(
                List.of(anotherMarketplaceCard)
        );

        // when
        response = mvc.perform(get(String.format("/users/%d/cards", anotherUser.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that the active cards from a given user can be retrieved with an OK response and empty list returned due
     * to cards no longer being active.
     */
    @Test
    void canRetrieveUsersActiveCardsWhenUserExistsEmptyList() throws Exception {
        // given
        expectedJson = "[]";

        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(
                Optional.ofNullable(anotherUser)
        );
        given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorId(user.getId())).willReturn(
                List.of(marketplaceCard)
        );

        // when
        response = mvc.perform(get(String.format("/users/%d/cards", user.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that the active cards from a given user can't be retrieved if the user doesn't exist with a NOT_ACCEPTABLE
     * response.
     */
    @Test
    void cantRetrieveUsersActiveCardsWhenUserDoesntExist() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorId(anotherUser.getId())).willReturn(
                List.of(anotherMarketplaceCard)
        );

        // when
        when(userRepository.findById(0)).thenReturn(Optional.empty());
        response = mvc.perform(get(String.format("/users/%d/cards", 0))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests that the active cards from a given user can't be retrieved if the current user doesn't exist with an
     * UNAUTHORIZED response.
     */
    @Test
    void cantRetrieveUsersActiveCardsWhenCurrentUserDoesntExists() throws Exception {
        // given
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.ofNullable(anotherUser));
        given(marketplaceCardRepository.findMarketplaceCardByCreatorId(anotherUser.getId())).willReturn(
                List.of(anotherMarketplaceCard)
        );

        // when
        when(userRepository.findBySessionUUID("0")).thenReturn(Optional.empty());
        response = mvc.perform(get(String.format("/users/%d/cards", 0))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    // ----------------- PUT ---------------------

    /**
     * Test user can edit their own card
     * Return OK (200)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void canEditCardWithCreatorCookie() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        String newTitle = "(NEW) Title";

        payloadJson = String.format(editCardPayloadJson, newTitle, marketplaceCard.getDescription(),
                "[]");

        // when
        response = mvc.perform(put(String.format("/cards/%d", marketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(marketplaceCard.getTitle()).isEqualTo(newTitle);
    }

    /**
     * Test user can't enter bad data ~ Title too small
     * Return BAD_REQUEST (400)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void cantEditCardWithBadData_TitleTooSmall() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        payloadJson = String.format(editCardPayloadJson, "", marketplaceCard.getDescription(),
                "[]");

        // when
        response = mvc.perform(put(String.format("/cards/%d", marketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test user can't enter bad data ~ Title too long
     * Return BAD_REQUEST (400)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void cantEditCardWithBadData_TitleTooLong() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        payloadJson = String.format(editCardPayloadJson, "a".repeat(51), marketplaceCard.getDescription(),
                "[]");

        // when
        response = mvc.perform(put(String.format("/cards/%d", marketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test user can't enter bad data ~ Description too long
     * Return BAD_REQUEST (400)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void cantEditCardWithBadData_DescriptionTooLong() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        payloadJson = String.format(editCardPayloadJson, marketplaceCard.getTitle(), "d".repeat(301), "[]");

        // when
        response = mvc.perform(put(String.format("/cards/%d", marketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test user includes all required fields ~ Title
     * Return BAD_REQUEST (400)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void cantEditCardWithMissingData_Title() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        String editedPayloadJson = "{" +
                "\"description\":\"%s\"," +
                "\"keywords\":%s}";
        payloadJson = String.format(editedPayloadJson, marketplaceCard.getDescription(), "");

        // when
        response = mvc.perform(put(String.format("/cards/%d", marketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test user includes all required fields ~ Description
     * Return BAD_REQUEST (400)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void cantEditCardWithMissingData_Description() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        String editedPayloadJson = "{" +
                "\"title\":\"%s\"," +
                "\"keywords\":%s}";

        payloadJson = String.format(editedPayloadJson, marketplaceCard.getTitle(), "");

        // when
        response = mvc.perform(put(String.format("/cards/%d", marketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test that the user cannot edit another users card
     * Return FORBIDDEN (403)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void cantEditOtherUsersCard() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(anotherMarketplaceCard.getId())).willReturn(Optional.ofNullable(anotherMarketplaceCard));

        payloadJson = String.format(editCardPayloadJson, anotherMarketplaceCard.getTitle(), anotherMarketplaceCard.getDescription(),
                "[]");

        // when
        response = mvc.perform(put(String.format("/cards/%d", anotherMarketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test that the DGAA can edit other users cards
     * Return OK (200)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void DGAACanEditOtherUsersCard() throws Exception {
        // given
        given(userRepository.findBySessionUUID(dgaa.getSessionUUID())).willReturn(Optional.ofNullable(dgaa));
        given(marketplaceCardRepository.findById(anotherMarketplaceCard.getId())).willReturn(Optional.ofNullable(anotherMarketplaceCard));

        String updatedDescription = "New Description";

        payloadJson = String.format(editCardPayloadJson, anotherMarketplaceCard.getTitle(), updatedDescription, "[]");

        // when
        response = mvc.perform(put(String.format("/cards/%d", anotherMarketplaceCard.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dgaa.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(anotherMarketplaceCard.getDescription()).isEqualTo(updatedDescription);
    }

    /**
     * Test for when Payload is empty
     * Returns BAD_REQUEST (400)
     *
     * @throws Exception In case there is an error with PUT call
     */
    @Test
    void noPayloadIncludedInEdit() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        // when
        response = mvc.perform(put(String.format("/cards/%d", marketplaceCard.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
