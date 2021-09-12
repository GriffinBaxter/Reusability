package org.seng302.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Main;
import org.seng302.controller.NotificationResource;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.NotificationType;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private BusinessRepository businessRepository;

    @MockBean
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @MockBean
    private KeywordNotificationRepository keywordNotificationRepository;

    @MockBean
    private ListingNotificationRepository listingNotificationRepository;

    @MockBean
    private SoldListingNotificationRepository soldListingNotificationRepository;

    private MockHttpServletResponse response;

    private User user;

    private User anotherUser;

    private Business business;

    private User admin;

    private final String userNotificationPayloadJson = "[{\"id\":%d," +
                                                "\"description\":\"%s\"," +
                                                "\"created\":\"%s\"," +
                                                "\"marketplaceCardPayload\":%s," +
                                                "\"notificationType\":\"%s\"}]";

    private final String adminNotificationPayloadJson = "[{\"id\":%d," +
                                                    "\"description\":\"%s\"," +
                                                    "\"created\":\"%s\"," +
                                                    "\"marketplaceCardPayload\":%s," +
                                                    "\"notificationType\":\"%s\"}," +
                                                    "{\"id\":%d," +
                                                    "\"description\":\"%s\"," +
                                                    "\"created\":\"%s\"," +
                                                    "\"keyword\":%s," +
                                                    "\"notificationType\":\"%s\"}]";

    private final String listingNotificationPayloadJson = "[{\"id\":%d," +
            "\"description\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"notificationType\":\"LISTING\"}]";

    private final String soldListingNotificationPayloadJson = "[{\"id\":%d," +
            "\"soldListing\":%s," +
            "\"description\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"notificationType\":\"SOLD_LISTING\"}]";

    private String payloadJson;

    private MarketplaceCard marketplaceCard;

    private MarketplaceCard anotherMarketplaceCard;

    private Keyword keyword;

    private MarketCardNotification marketCardNotification;

    private MarketCardNotification anotherMarketplaceCardNotification;

    private KeywordNotification keywordNotification;

    private SoldListing soldListing;

    private ListingNotification listingNotification;

    private SoldListingNotification soldListingNotification;

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

        anotherUser = new User(
                "Abby",
                "Wyatt",
                "W",
                "Abby",
                "bio",
                "Abby@example.com",
                LocalDate.of(2020, Month.JANUARY, 1).minusYears(13),
                "1234567555",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());

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

        business = new Business(
                user.getId(),
                "example name",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user,
                "$",
                "NZD"
        );
        business.setId(1);
        user.setBusinessesAdministeredObjects(List.of(business));
        business.setAdministrators(List.of(user));

        soldListing = new SoldListing(business,
                anotherUser,
                LocalDateTime.now().minusDays(1),
                "PROD",
                4,
                20.0,
                5);

        soldListingNotification = new SoldListingNotification(business.getId(), soldListing, "Purchased listing");

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

        listingNotification = new ListingNotification(
                "Listing for Pizza has sold."
        );
        listingNotification.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(
                new NotificationResource(userRepository, businessRepository, marketCardNotificationRepository,
                        keywordNotificationRepository, listingNotificationRepository, soldListingNotificationRepository))
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
                                    marketplaceCard.toMarketplaceCardPayload().toString(), NotificationType.MARKETPLACE);

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
                                    anotherMarketplaceCard.toMarketplaceCardPayload().toString(), NotificationType.MARKETPLACE,
                                    keywordNotification.getId(), keywordNotification.getDescription(), keywordNotification.getCreated(),
                                    (new KeywordPayload(keyword.getId(),keyword.getName(),keyword.getCreated())), NotificationType.KEYWORD);

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
        List<MarketCardNotification> emptyMarketNotifications = new ArrayList<>();

        // When
        when(marketCardNotificationRepository.findAllByUserId(user.getId())).thenReturn(emptyMarketNotifications);
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
        List<MarketCardNotification> emptyMarketNotifications = new ArrayList<>();
        List<KeywordNotification> emptyKeywordNotifications = new ArrayList<>();

        // When
        when(marketCardNotificationRepository.findAllByUserId(admin.getId())).thenReturn(emptyMarketNotifications);
        when(keywordNotificationRepository.findAll()).thenReturn(emptyKeywordNotifications);
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

    /**
     * Tests that an OK status and a list containing a listing notification is received when a user tries to retrieve notifications when logged in.
     *
     * @throws Exception thrown if there is an error with MockMvc
     */
    @Test
    void canRetrieveListingNotificationsAsUser() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        payloadJson = String.format(listingNotificationPayloadJson, listingNotification.getId(),
                listingNotification.getDescription(), listingNotification.getCreated());

        // When
        when(listingNotificationRepository.findAllByUsersId(user.getId())).thenReturn(List.of(listingNotification));
        response = mvc.perform(get("/users/notifications")
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }


    /**
     * Tests that an OK status and a list containing sold listing notifications is received when a business admin user tries to retrieve business notifications when logged in.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveAllBusinessNotificationsAsBusinessAdmin() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        payloadJson = String.format(soldListingNotificationPayloadJson, soldListingNotification.getId(), soldListing,
                                    soldListingNotification.getDescription(), soldListingNotification.getCreated());

        // When
        when(soldListingNotificationRepository.findAllByBusinessId(business.getId())).thenReturn(List.of(soldListingNotification));
        response = mvc.perform(get(String.format("/businesses/%d/notifications", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                        .andReturn()
                        .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }

    /**
     * Tests that an OK status and a list containing sold listing notifications is received when an admin tries to retrieve business notifications when logged in.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveAllBusinessNotificationsAsAdmin() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(admin.getSessionUUID())).willReturn(Optional.ofNullable(admin));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        payloadJson = String.format(soldListingNotificationPayloadJson, soldListingNotification.getId(), soldListing,
                                    soldListingNotification.getDescription(), soldListingNotification.getCreated());

        // When
        when(soldListingNotificationRepository.findAllByBusinessId(business.getId())).thenReturn(List.of(soldListingNotification));
        response = mvc.perform(get(String.format("/businesses/%d/notifications", business.getId()))
                        .cookie(new Cookie("JSESSIONID", admin.getSessionUUID())))
                        .andReturn()
                        .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }

    /**
     * Tests that an OK status and an empty list is received when a business admin user tries to retrieve business notifications when logged in and there's no notification for that business.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveEmptyNotifications() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        payloadJson = "[]";

        // When
        response = mvc.perform(get(String.format("/businesses/%d/notifications", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                        .andReturn()
                        .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(payloadJson);
    }

    /**
     * Tests that a UNAUTHORIZED status is received when a user tries to retrieve business notifications without login.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cannotRetrieveAllBusinessNotificationsWithoutLogin() throws Exception {
        // When
        response = mvc.perform(get(String.format("/businesses/%d/notifications", business.getId())))
                        .andReturn()
                        .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when a user tries to retrieve business notifications when the business doesn't exist.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cannotRetrieveAllBusinessNotificationsWhenBusinessDoesNotExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(0)).willReturn(Optional.empty());

        // When
        response = mvc.perform(get(String.format("/businesses/%d/notifications",0))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                        .andReturn()
                        .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests that a FORBIDDEN status is received when a non-business-admin user tries to retrieve business notifications.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void cannotRetrieveAllBusinessNotificationsWhenNotBusinessAdmin() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        // When
        response = mvc.perform(get(String.format("/businesses/%d/notifications", business.getId()))
                        .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                        .andReturn()
                        .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

//    ----------------------------------------Notification Deletion----------------------------------------
    /**
     * Test that an UNAUTHORIZED status is received when a non-logged in user try to delete a notification.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteNotificationWhenUserNotLoggedIn() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.empty());

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", keywordNotification.getId()))
                        .param("type", "KEYWORD"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getErrorMessage()).isEqualTo("Access token is missing or invalid");
    }

    /**
     * Test that an NOT_ACCEPTABLE status is received when a logged-in user tries to delete a notification and the type
     * is incorrect.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteNotificationWhenLoggedInButTypeDoesNotExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(keywordNotificationRepository.findById(keywordNotification.getId()))
                .willReturn(Optional.ofNullable(keywordNotification));

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", keywordNotification.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                .param("type", "INCORRECT"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid notification type has been given.");
    }

//    ---------------------------------------- Deletion of Keyword Notifications ----------------------------------

    /**
     * Test that an FORBIDDEN status is received when a logged-in User try to delete a notification.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteKeywordNotificationWhenLoginAsAUser() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(keywordNotificationRepository.findById(keywordNotification.getId()))
                .willReturn(Optional.ofNullable(keywordNotification));

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", keywordNotification.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("type", "KEYWORD"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid permissions to delete notifications");
    }

    /**
     * Test that an NOT_ACCEPTABLE status is received when the notification not exist.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteKeywordNotificationWhenItNotExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(admin.getSessionUUID())).willReturn(Optional.ofNullable(admin));
        given(keywordNotificationRepository.findById(keywordNotification.getId()))
                .willReturn(Optional.empty());

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", keywordNotification.getId()))
                        .cookie(new Cookie("JSESSIONID", admin.getSessionUUID()))
                        .param("type", "KEYWORD"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("Keyword Notification not found");
    }

    /**
     * Test that an OK status is received when a logged-in admin try to delete a notification.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canDeleteKeywordNotificationWhenLoginAsAnAdmin() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(admin.getSessionUUID())).willReturn(Optional.ofNullable(admin));
        given(keywordNotificationRepository.findById(keywordNotification.getId()))
                .willReturn(Optional.ofNullable(keywordNotification));

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", keywordNotification.getId()))
                        .cookie(new Cookie("JSESSIONID", admin.getSessionUUID()))
                        .param("type", "KEYWORD"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getErrorMessage()).isEqualTo("Notifications Successfully deleted");
    }

//    ---------------------------------------- Deletion of Listings Notifications ----------------------------------

    /**
     * Test that an NOT_ACCEPTABLE status is received when the notification not exist.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteListingNotificationWhenItNotExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(listingNotificationRepository.findById(listingNotification.getId()))
                .willReturn(Optional.empty());

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", listingNotification.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("type", "LISTING"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("Listing Notification not found");
    }

    /**
     * Test that an NOT_ACCEPTABLE status is received when the User not in the subscribed list.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteListingNotificationWhenUserNotInTheList() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(listingNotificationRepository.findById(listingNotification.getId()))
                .willReturn(Optional.ofNullable(listingNotification));

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", listingNotification.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("type", "LISTING"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("User not subscribed to listing notification");
    }

    /**
     * Test that an OK status is received when the user is in the subscribed list.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canDeleteListingNotificationWhenUserInTheList() throws Exception {
        // Given
        listingNotification.setUsers(List.of(user));
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(listingNotificationRepository.findById(listingNotification.getId()))
                .willReturn(Optional.ofNullable(listingNotification));

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", listingNotification.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("type", "LISTING"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getErrorMessage()).isEqualTo("Notifications Successfully deleted");
    }

//    ---------------------------------------- Deletion of Sold Listings Notifications ----------------------------------

    /**
     * Test that an NOT_ACCEPTABLE status is received when the notification does not exist.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteSoldListingNotificationWhenItDoesNotExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(soldListingNotificationRepository.findById(soldListing.getId()))
                .willReturn(Optional.empty());

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", soldListingNotification.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                .param("type", "SOLD_LISTING"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("Sold Listing Notification not found");
    }

    /**
     * Test that an OK status is received when the user is in the subscribed list.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canDeleteSoldListingNotificationWhenUserInTheList() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(soldListingNotificationRepository.findById(soldListingNotification.getId()))
                .willReturn(Optional.ofNullable(soldListingNotification));

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", soldListingNotification.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                .param("type", "SOLD_LISTING"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getErrorMessage()).isEqualTo("Notifications Successfully deleted");
    }

//    ---------------------------------------- Deletion of Marketplace Card Notifications ------------------------------

    /**
     * Test that an NOT_ACCEPTABLE status is received when the notification does not exist.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canNotDeleteMarketplaceCardNotificationWhenItDoesNotExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketCardNotificationRepository.findById(marketCardNotification.getId()))
                .willReturn(Optional.empty());

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", marketCardNotification.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                .param("type", "MARKETPLACE"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("Marketplace Card Notification not found");
    }

    /**
     * Test that an OK status is received when the user is in the subscribed list.
     * @throws Exception thrown if there is an error when deleting a notification.
     */
    @Test
    void canDeleteMarketplaceCardNotificationWhenUserInTheList() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketCardNotificationRepository.findById(marketCardNotification.getId()))
                .willReturn(Optional.ofNullable(marketCardNotification));

        // When
        response = mvc.perform(delete(String.format("/users/notifications/%d", marketCardNotification.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                .param("type", "MARKETPLACE"))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getErrorMessage()).isEqualTo("Notifications Successfully deleted");
    }

}
