package org.seng302.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Main;
import org.seng302.controller.MarketCardNotificationResource;
import org.seng302.model.Address;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/**
 * NotificationResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class NotificationResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MarketCardNotificationRepository marketCardNotificationRepository;

    private MockHttpServletResponse response;

    private User user;

    private String payloadJson;

    private MarketplaceCard marketplaceCard;

    private MarketCardNotification marketCardNotification;

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

        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(1);

        marketCardNotification = new MarketCardNotification(
                user.getId(),
                marketplaceCard.getId(),
                marketplaceCard,
                "",
                LocalDateTime.now()
        );

        this.mvc = MockMvcBuilders.standaloneSetup(
                new MarketCardNotificationResource(userRepository, marketCardNotificationRepository))
                .build();
    }

    // -------------------------------------------- CREATE ONE NEW CARD ------------------------------------------------

    /**
     * Test that a OK status is received when user try to retrieve notifications with login.
     *
     * @throws Exception thrown if there is an error when creating a card.
     */
    @Test
    void canRetrieveAllNotifications() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(marketCardNotificationRepository.findAllByUserId(user.getId())).willReturn(List.of(marketCardNotification));

        // When
        response = mvc.perform(get("/users/notifications")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that a UNAUTHORIZED status is received when user try to retrieve notification without login.
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
