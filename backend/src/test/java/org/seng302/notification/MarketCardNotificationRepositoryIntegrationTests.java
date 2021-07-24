package org.seng302.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.Address;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;


/**
 * NotificationRepository test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class MarketCardNotificationRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarketCardNotificationRepository marketCardNotificationRepository;

    private User user;
    private User anotherUser;
    private MarketCardNotification marketCardNotification;
    private MarketCardNotification marketCardNotification2;
    private MarketCardNotification marketCardNotification3;
    private MarketCardNotification marketCardNotification4;
    private MarketCardNotification marketCardNotification5;


    private MarketplaceCard marketplaceCard;
    private MarketplaceCard marketplaceCard2;
    private MarketplaceCard marketplaceCard3;
    private MarketplaceCard marketplaceCard4;
    private MarketplaceCard marketplaceCard5;

    /**
     * Sets up data for testing
     */
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
        entityManager.persist(address);
        entityManager.flush();

        // Users
        user = new User(
                "Jeff",
                "Alex",
                "C",
                "Jeff",
                "bio",
                "Jeff@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        entityManager.persist(user);
        entityManager.flush();

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
        entityManager.persist(anotherUser);
        entityManager.flush();

        // Cards
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        marketplaceCard2 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2019, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Money",
                "I'm poor..."
        );
        entityManager.persist(marketplaceCard2);
        entityManager.flush();

        marketplaceCard3 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 21), LocalTime.of(0, 0)),
                "Your dignity",
                ""
        );
        entityManager.persist(marketplaceCard3);
        entityManager.flush();

        marketplaceCard4 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2021, Month.MAY, 18), LocalTime.of(0, 0)),
                "Ambo",
                "Been shot pls help"
        );
        entityManager.persist(marketplaceCard4);
        entityManager.flush();

        marketplaceCard5 = new MarketplaceCard(
                anotherUser.getId(),
                anotherUser,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 21), LocalTime.of(0, 0)),
                "WordArt Title's",
                ""
        );
        entityManager.persist(marketplaceCard5);
        entityManager.flush();

        // Notifications
        marketCardNotification = new MarketCardNotification(user.getId(),
                marketplaceCard,
                String.format("Your card (%s) will be expired.", marketplaceCard.getTitle()),
                LocalDateTime.now());
        entityManager.persist(marketCardNotification);
        entityManager.flush();

        marketCardNotification2 = new MarketCardNotification(user.getId(),
                marketplaceCard2,
                String.format("Your card (%s) will be expired.", marketplaceCard2.getTitle()),
                LocalDateTime.now());
        entityManager.persist(marketCardNotification2);
        entityManager.flush();

        marketCardNotification3 = new MarketCardNotification(user.getId(),
                marketplaceCard3,
                String.format("Your card (%s) will be expired.", marketplaceCard3.getTitle()),
                LocalDateTime.now());
        entityManager.persist(marketCardNotification3);
        entityManager.flush();

        marketCardNotification4 = new MarketCardNotification(user.getId(),
                marketplaceCard4,
                String.format("Your card (%s) will be expired.", marketplaceCard4.getTitle()),
                LocalDateTime.now());
        entityManager.persist(marketCardNotification4);
        entityManager.flush();

        marketCardNotification5 = new MarketCardNotification(anotherUser.getId(),
                marketplaceCard5,
                String.format("Your card (%s) will be expired.", marketplaceCard5.getTitle()),
                LocalDateTime.now());
        entityManager.persist(marketCardNotification5);
        entityManager.flush();
    }


    /**
     * Test FindByUserIdAndMarketCardId()
     */
    @Test
    void testFindByUserIdAndMarketCardId() {
        // when
        Optional<MarketCardNotification> optionalNotification = marketCardNotificationRepository
                .findByUserIdAndMarketCardId(user.getId(), marketplaceCard.getId());

        // then
        Assertions.assertTrue(optionalNotification.isPresent());
        Assertions.assertEquals(marketCardNotification.toString(), optionalNotification.get().toString());
    }

    /**
     * Test FindAllByReceiverId()
     */
    @Test
    void testFindAllByUserId() {
        // when
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAllByUserId(user.getId());

        // then
        for (MarketCardNotification marketCardNotification : marketCardNotifications) {
            System.out.println(marketCardNotification.getDescription());
        }
        Assertions.assertEquals(4, marketCardNotifications.size());
    }

    /**
     * Test FindAllByReceiverId()
     */
    @Test
    void testDeleteAllByMarketCardId() {
        // when
        marketCardNotificationRepository.deleteAllByMarketCardId(marketplaceCard.getId());

        // then
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAll();
        Assertions.assertFalse(marketCardNotifications.contains(marketplaceCard));
    }
}
