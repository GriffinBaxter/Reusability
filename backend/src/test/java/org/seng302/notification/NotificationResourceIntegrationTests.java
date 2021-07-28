package org.seng302.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Main;
import org.seng302.controller.NotificationResource;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.KeywordNotificationRepository;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.outgoing.KeywordPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
 * NotificationResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class NotificationResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @MockBean
    private KeywordNotificationRepository keywordNotificationRepository;

    private MockHttpServletResponse response;

    private User user;

    private User admin;

    private final String userNotificationPayloadJson = "[{\"id\":%d," +
                                                "\"description\":\"%s\"," +
                                                "\"created\":\"%s\"," +
                                                "\"marketplaceCardPayload\":%s}]";

    private final String adminNotificationPayloadJson = "[{\"id\":%d," +
                                                    "\"description\":\"%s\"," +
                                                    "\"created\":\"%s\"," +
                                                    "\"marketplaceCardPayload\":%s}," +
                                                    "{\"id\":%d," +
                                                    "\"description\":\"%s\"," +
                                                    "\"created\":\"%s\"," +
                                                    "\"keyword\":%s}]";

    private String payloadJson;

    private MarketplaceCard marketplaceCard;

    private MarketplaceCard anotherMarketplaceCard;

    private Keyword keyword;

    private MarketCardNotification marketCardNotification;

    private MarketCardNotification anotherMarketplaceCardNotification;

    private KeywordNotification keywordNotification;

    @BeforeEach
    public void setup() throws Exception {
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

        admin = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "admin@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        admin.setId(2);
        admin.setSessionUUID(User.generateSessionUUID());

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
                admin.getId(),
                admin,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Admin Card",
                "Come join Hayley and help her celebrate her birthday!"
        );
        anotherMarketplaceCard.setId(2);

        keyword = new Keyword(
                "Beef",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0))
        );
        keyword.setId(1);

        marketCardNotification = new MarketCardNotification(
                user.getId(),
                marketplaceCard,
                "",
                LocalDateTime.now()
        );
        marketCardNotification.setId(1);

        anotherMarketplaceCardNotification = new MarketCardNotification(
                admin.getId(),
                anotherMarketplaceCard,
                "",
                LocalDateTime.now().minusHours(1)
        );
        anotherMarketplaceCard.setId(2);

        keywordNotification = new KeywordNotification(
                "A new keyword, Beef, has been created",
                LocalDateTime.now(),
                keyword
        );
        keywordNotification.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(
                new NotificationResource(userRepository, marketCardNotificationRepository, keywordNotificationRepository))
                .build();
    }


    /**
     * Tests that an OK status and a list containing marketplace notifications is received when a user tries to retrieve notifications when logged in.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveAllNotificationsAsUser() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        payloadJson = String.format(userNotificationPayloadJson, marketCardNotification.getId(),
                                    marketCardNotification.getDescription(), marketCardNotification.getCreated(),
                                    marketplaceCard.toMarketplaceCardPayload().toString());

        // When
        when(marketCardNotificationRepository.findAllByUserId(user.getId())).thenReturn(List.of(marketCardNotification));
        response = mvc.perform(get("/users/notifications")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }

    /**
     * Tests that an OK status and a list containing marketplace and keyword notifications is received when an admin tries to retrieve notifications when logged in.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveAllNotificationsAsAdmin() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(admin.getSessionUUID())).willReturn(Optional.ofNullable(admin));
        payloadJson = String.format(adminNotificationPayloadJson, anotherMarketplaceCardNotification.getId(),
                                    anotherMarketplaceCardNotification.getDescription(), anotherMarketplaceCardNotification.getCreated(),
                                    anotherMarketplaceCard.toMarketplaceCardPayload().toString(),
                                    keywordNotification.getId(), keywordNotification.getDescription(), keywordNotification.getCreated(),
                                    (new KeywordPayload(keyword.getId(),keyword.getName(),keyword.getCreated())).toString());

        // When
        when(marketCardNotificationRepository.findAllByUserId(admin.getId())).thenReturn(List.of(anotherMarketplaceCardNotification));
        when(keywordNotificationRepository.findAll()).thenReturn(List.of(keywordNotification));
        response = mvc.perform(get("/users/notifications")
                .cookie(new Cookie("JSESSIONID", admin.getSessionUUID())))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }

    /**
     * Tests that an OK status and an empty list is received when a user tries to retrieve notifications when logged in and there's no notifications.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveEmptyNotificationsAsUser() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        payloadJson = "[]";

        // When
        //when(marketCardNotificationRepository.findAllByUserId(user.getId())).thenReturn(List.of(marketCardNotification));
        response = mvc.perform(get("/users/notifications")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }

    /**
     * Tests that an OK status and an empty list is received when an admin tries to retrieve notifications when logged in and there's no notifications.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveEmptyNotificationsAsAdmin() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(admin.getSessionUUID())).willReturn(Optional.ofNullable(admin));
        payloadJson = "[]";

        // When
        //when(marketCardNotificationRepository.findAllByUserId(admin.getId())).thenReturn(List.of(anotherMarketplaceCardNotification));
        //when(keywordNotificationRepository.findAll()).thenReturn(List.of());
        response = mvc.perform(get("/users/notifications")
                .cookie(new Cookie("JSESSIONID", admin.getSessionUUID())))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }

    /**
     * Tests that a UNAUTHORIZED status is received when user try to retrieve notification without login.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canNotRetrieveAllNotificationsWithoutLogin() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketCardNotificationRepository.findAllByUserId(user.getId())).willReturn(List.of(marketCardNotification));

        // When
        response = mvc.perform(get("/users/notifications"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
