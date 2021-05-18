package org.seng302.marketplace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.address.Address;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
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
    private MarketplaceCardRepository marketplaceCardRepository;

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

    private String expectedJson;

    private MockHttpServletResponse response;

    private User user;

    private MarketplaceCard marketplaceCard;

    @BeforeEach
    public void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
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
        marketplaceCard.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository, userRepository)).build();
    }


    /**
     * Tests that an OK status and a marketplace card is received when the card ID in the /cards/{id} API endpoint exists.
     * Test specifically for when the cookie contains a valid UUID.
     */
    @Test
    public void canRetrieveCardWhenCardExistsWithValidCookie() throws Exception {
        // given
        expectedJson = String.format(expectedCardJson, marketplaceCard.getId(), user.getId(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard.getSection().toString(),
                marketplaceCard.getCreated(), marketplaceCard.getDisplayPeriodEnd(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription());
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
    public void cantRetrieveCardWhenCardExistsWithInvalidCookie() throws Exception {
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
    public void cantRetrieveCardWhenCardExistsWithNoCookie() throws Exception {
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
    public void cantRetrieveCardWhenCardDoesntExist() throws Exception {
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
    public void cantRetrieveCardWithInvalidId() throws Exception {
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
    public void canRetrieveCardsWithValidSectionAndCookie() throws Exception {
        // given
        expectedJson = "[" + String.format(expectedCardJson, marketplaceCard.getId(), user.getId(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard.getSection().toString(),
                marketplaceCard.getCreated(), marketplaceCard.getDisplayPeriodEnd(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription()) + "]";

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        Sort sort = Sort.by(Sort.Order.desc("created").ignoreCase());;
        Pageable page = PageRequest.of(0, 20, sort);

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
    public void canRetrieveCardsWithValidSectionAndCookieAndOrderByAndPage() throws Exception {
        // given
        expectedJson = "[" + String.format(expectedCardJson, marketplaceCard.getId(), user.getId(), user.getFirstName(),
                user.getLastName(), user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(),
                user.getCreated(), user.getRole(), user.getHomeAddress().toSecureString(), marketplaceCard.getSection().toString(),
                marketplaceCard.getCreated(), marketplaceCard.getDisplayPeriodEnd(), marketplaceCard.getTitle(),
                marketplaceCard.getDescription()) + "]";

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        Sort sort = Sort.by(Sort.Order.asc("created").ignoreCase());;
        Pageable page = PageRequest.of(0, 20, sort);

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
    public void cantRetrieveCardsWithInvalidJSESSIONID() throws Exception {
        // given
        String fakeSessionID = "xxx";
        given(userRepository.findBySessionUUID(fakeSessionID)).willReturn(Optional.ofNullable(null));

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
    public void cantRetrieveCardsWithNoJSESSIONID() throws Exception {
        // given
        given(userRepository.findBySessionUUID(null)).willReturn(Optional.ofNullable(null));

        // when
        response = mvc.perform(get("/cards").param("section", "FORSALE")).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that the user cannot retrieve cards with an invalid Page number parameter
     */
    @Test
    public void cantRetrieveCardsWithInvalidPage() throws Exception {
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
    public void cantRetrieveCardsWithInvalidOrderBy() throws Exception {
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
    public void cantRetrieveCardsWithInvalidSection() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));

        // when
        response = mvc.perform(get("/cards").param("section", "SECTION")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}