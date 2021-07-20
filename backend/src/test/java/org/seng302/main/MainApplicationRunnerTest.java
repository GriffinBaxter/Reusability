package org.seng302.main;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.MainApplicationRunner;
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
import java.util.List;

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

    private int creatorId1;
    private int creatorId2;

    private MarketplaceCard marketplaceCard;
    private MarketplaceCard marketplaceCard2;
    private MarketplaceCard marketplaceCard3;
    private MarketplaceCard marketplaceCard4;
    private MarketplaceCard marketplaceCard5;

    private List<MarketplaceCard> marketplaceCards;

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

        Address address2 = new Address(
                "111",
                "Grant Road",
                "Invercargill",
                "Southland",
                "New Zealand",
                "9879",
                "Otatara"
        );
        entityManager.persist(address2);
        entityManager.flush();

        // User
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316", address, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        entityManager.persist(user);
        entityManager.flush();

        // User 2
        User user2 = new User("testfirstII", "testlastII", "testmiddleII", "testnickII",
                "testbiographyII", "testemail2@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "6415216", address2, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        entityManager.persist(user2);
        entityManager.flush();

        //Cards
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().minusWeeks(3), // expired 1 week ago
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        marketplaceCard2 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.now().plusMinutes(1).minusWeeks(2), // expired 1 minute later
                "Money",
                "I'm poor..."
        );
        entityManager.persist(marketplaceCard2);
        entityManager.flush();

        marketplaceCard3 = new MarketplaceCard(
                user2.getId(),
                user2,
                Section.FORSALE,
                LocalDateTime.now().plusHours(1).minusWeeks(2), // expired 1 hour later
                "Your dignity",
                ""
        );
        entityManager.persist(marketplaceCard3);
        entityManager.flush();

        marketplaceCard4 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.now().plusDays(1).minusWeeks(2), // expired 1 day later
                "Ambo",
                "Been shot pls help"
        );
        entityManager.persist(marketplaceCard4);
        entityManager.flush();

        marketplaceCard5 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now().plusDays(10).minusWeeks(2), // expired 10 day later
                "WordArt Title's",
                ""
        );
        entityManager.persist(marketplaceCard5);
        entityManager.flush();

        marketplaceCards = List.of(marketplaceCard, marketplaceCard2, marketplaceCard3, marketplaceCard4, marketplaceCard5);

        creatorId1 = user.getId();
        creatorId2 = user2.getId();
    }

    /**
     * Test that the checkNotifications will create notification for all cards will expired in next 24h or already expired.
     */
    @Test
    void testCheckNotificationsCreate() {
        // Given
        List<String> notificationMessages = List.of("Your card (Hayley's Birthday) has been deleted.",
                                                    "Your card (Money) will be expired in 0h 0m 59s.",
                                                    "Your card (Your dignity) will be expired in 0h 59m 59s.",
                                                    "Your card (Ambo) will be expired in 23h 59m 59s.");

        // Before
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAll();
        assertThat(marketCardNotifications.size()).isZero();

        // Run the function
        mainApplicationRunner.checkNotifications();

        // After
        marketCardNotifications = marketCardNotificationRepository.findAll();
        assertThat(notificationMessages.size()).isEqualTo(marketCardNotifications.size());
        for (int i = 0; i < 4; i++) {
            assertThat(marketCardNotifications.get(i).getDescription()).isEqualTo(notificationMessages.get(i));
        }
    }

    /**
     * Test that the checkNotifications will update notification for all cards will expired in next 24h or already expired.
     */
    @Test
    void testCheckNotificationsUpdate() {
        // Given
        List<String> notificationMessages = List.of("Your card (Hayley's Birthday) has been deleted.",
                "Your card (Money) will be expired in 0h 0m 59s.",
                "Your card (Your dignity) will be expired in 0h 59m 59s.",
                "Your card (Ambo) will be expired in 23h 59m 59s.");

        // Before
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAll();
        assertThat(marketCardNotifications.size()).isZero();

        // Create Notification
        MarketCardNotification marketCardNotification = new MarketCardNotification(creatorId1,
                marketplaceCard.getId(),
                marketplaceCard,
                "",
                LocalDateTime.now());
        MarketCardNotification marketCardNotification2 = new MarketCardNotification(creatorId1,
                marketplaceCard2.getId(),
                marketplaceCard2,
                "",
                LocalDateTime.now());
        MarketCardNotification marketCardNotification3 = new MarketCardNotification(creatorId2,
                marketplaceCard3.getId(),
                marketplaceCard3,
                "",
                LocalDateTime.now());
        MarketCardNotification marketCardNotification4 = new MarketCardNotification(creatorId1,
                marketplaceCard4.getId(),
                marketplaceCard4,
                "",
                LocalDateTime.now());
        entityManager.persist(marketCardNotification);
        entityManager.persist(marketCardNotification2);
        entityManager.persist(marketCardNotification3);
        entityManager.persist(marketCardNotification4);
        entityManager.flush();

        marketCardNotifications = marketCardNotificationRepository.findAll();
        for (int i = 0; i < 4; i++) {
            assertThat(marketCardNotifications.get(i).getDescription()).isEqualTo("");
        }

        // Run the function
        mainApplicationRunner.checkNotifications();

        // After
        marketCardNotifications = marketCardNotificationRepository.findAll();
        Assert.assertEquals(4, marketCardNotifications.size());
        for (int i = 0; i < 4; i++) {
            assertThat(marketCardNotifications.get(i).getDescription()).isEqualTo(notificationMessages.get(i));
        }
    }
}
