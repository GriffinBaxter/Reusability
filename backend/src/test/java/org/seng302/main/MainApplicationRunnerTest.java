package org.seng302.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.MainApplicationRunner;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.model.Address;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class MainApplicationRunnerTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    private MarketCardNotificationRepository marketCardNotificationRepository;

    private MainApplicationRunner mainApplicationRunner;

    private User user;

    private MarketplaceCard marketplaceCard;

    private String givenMessage;
    private MarketCardNotification givenMarketCardNotification;

    private Optional<MarketCardNotification> optionalRetrievedMarketCardNotification;
    private List<MarketCardNotification> marketCardNotifications;

    /**
     * Sets up data for testing
     */
    @BeforeEach
    void beforeEach() throws Exception {
        mainApplicationRunner = new MainApplicationRunner(userRepository,
                businessRepository,
                addressRepository,
                productRepository,
                marketplaceCardRepository,
                marketCardNotificationRepository);

        // Address
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        entityManager.persist(address);
        entityManager.flush();

        // User
        user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316", address, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        entityManager.persist(user);
        entityManager.flush();
    }

    /**
     * Test that checkNotifications will not create notification for card which will expired in more then 24h.
     */
    @Test
    void testCheckNotificationsWillNotCreateNotificationForCardExpiredInMoreThen24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().plusDays(10).minusWeeks(2), // expired 10 day later
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        optionalRetrievedMarketCardNotification = marketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), marketplaceCard.getId());
        assertThat(optionalRetrievedMarketCardNotification).isEmpty();
    }

    /**
     * Test that checkNotifications will create notification for card which will expired in next 24h.
     */
    @Test
    void testCheckNotificationsWillCreateNotificationForCardExpiredInNext24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        givenMessage = "Your card (Hayley's Birthday) will be expired in 1h.";
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().plusHours(2).minusWeeks(2), // expired 1 hour later
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        optionalRetrievedMarketCardNotification = marketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), marketplaceCard.getId());
        assertThat(optionalRetrievedMarketCardNotification).isPresent();
        assertThat(optionalRetrievedMarketCardNotification.get().getDescription()).isEqualTo(givenMessage);
    }

    /**
     * Test that checkNotifications will update notification for card which will expired in next 24h.
     */
    @Test
    void testCheckNotificationsWillUpdateNotificationForCardExpiredInNext24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        givenMessage = "Your card (Hayley's Birthday) will be expired in 1h.";
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().plusHours(2).minusWeeks(2), // expired 1 hour later
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        givenMarketCardNotification = new MarketCardNotification(user.getId(),
                marketplaceCard,
                "",
                LocalDateTime.now());
        entityManager.persist(givenMarketCardNotification);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        optionalRetrievedMarketCardNotification = marketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), marketplaceCard.getId());
        assertThat(optionalRetrievedMarketCardNotification).isPresent();
        assertThat(optionalRetrievedMarketCardNotification.get().getDescription()).isEqualTo(givenMessage);
    }

    /**
     * Test that checkNotifications will create notification for card which already expired less then 24h.
     */
    @Test
    void testCheckNotificationsWillCreateNotificationForCardExpiredLessThen24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        givenMessage = "Your card (Hayley's Birthday) expired 5h ago and will soon be deleted.";
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusHours(5).minusWeeks(2), // expired 5 hour ago
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        optionalRetrievedMarketCardNotification = marketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), marketplaceCard.getId());
        assertThat(optionalRetrievedMarketCardNotification).isPresent();
        assertThat(optionalRetrievedMarketCardNotification.get().getDescription()).isEqualTo(givenMessage);
    }

    /**
     * Test that checkNotifications will update notification for card which already expired less then 24h.
     */
    @Test
    void testCheckNotificationsWillUpdateNotificationForCardExpiredLessThen24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        givenMessage = "Your card (Hayley's Birthday) expired 5h ago and will soon be deleted.";
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusHours(5).minusWeeks(2), // expired 5 hour ago
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        givenMarketCardNotification = new MarketCardNotification(user.getId(),
                marketplaceCard,
                "",
                LocalDateTime.now());
        entityManager.persist(givenMarketCardNotification);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        optionalRetrievedMarketCardNotification = marketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), marketplaceCard.getId());
        assertThat(optionalRetrievedMarketCardNotification).isPresent();
        assertThat(optionalRetrievedMarketCardNotification.get().getDescription()).isEqualTo(givenMessage);
    }

    /**
     * Test that checkNotifications will create notification for card which already expired more then 24h.
     */
    @Test
    void testCheckNotificationsWillCreateNotificationForCardExpiredMoreThen24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        givenMessage = "Your card (Hayley's Birthday) has been deleted.";
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusWeeks(3), // expired 1 week ago
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        marketCardNotifications = marketCardNotificationRepository.findAllByUserId(user.getId());
        List<String> allMessages = new ArrayList<>();
        for (MarketCardNotification marketCardNotification : marketCardNotifications) {
            allMessages.add(marketCardNotification.getDescription());
        }
        assertThat(allMessages).contains(givenMessage);
    }

    /**
     * Test that checkNotifications will update notification for card which already expired more then 24h.
     */
    @Test
    void testCheckNotificationsWillUpdateNotificationForCardExpiredMoreThen24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        givenMessage = "Your card (Hayley's Birthday) has been deleted.";
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusWeeks(3), // expired 1 week ago
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        givenMarketCardNotification = new MarketCardNotification(user.getId(),
                marketplaceCard,
                "",
                LocalDateTime.now().minusWeeks(3));
        entityManager.persist(givenMarketCardNotification);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        marketCardNotifications = marketCardNotificationRepository.findAllByUserId(user.getId());

        List<String> allMessages = new ArrayList<>();
        for (MarketCardNotification marketCardNotification : marketCardNotifications) {
            allMessages.add(marketCardNotification.getDescription());
        }
        assertThat(allMessages).contains(givenMessage);
    }

    /**
     * Test that checkNotifications will delete marketplace card which expired more then 24h.
     */
    @Test
    void testCheckNotificationsWillDeleteMarketCardWhichExpiredMoreThen24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusWeeks(3), // expired 1 week ago
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        assertThat(marketplaceCardRepository.findMarketplaceCardByCreatorId(user.getId()).contains(marketplaceCard)).isFalse();
    }

    /**
     * Test that checkNotifications will not delete marketplace card which expired less then 24h.
     */
    @Test
    void testCheckNotificationsWillNotDeleteMarketCardWhichExpiredLessThen24h() throws IllegalMarketplaceCardArgumentException {
        // Given
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusHours(5).minusWeeks(2), // expired 5 hour ago
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!");
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // When
        mainApplicationRunner.checkNotifications();

        // Then
        assertThat(marketplaceCardRepository.findMarketplaceCardByCreatorId(user.getId()).contains(marketplaceCard)).isTrue();
    }
}
