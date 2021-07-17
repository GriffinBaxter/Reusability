package org.seng302.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.Address;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
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
public class MarketCardNotificationRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarketCardNotificationRepository marketCardNotificationRepository;

    private User user;
    private User anotherUser;
    private MarketCardNotification marketCardNotification;

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

        marketCardNotification = new MarketCardNotification(user.getId(), 1, "Your card (books) will be expired in 12h", LocalDateTime.now());
        MarketCardNotification marketCardNotification1 = new MarketCardNotification(user.getId(), 2, "Your card (Beef) will be expired in 10h", LocalDateTime.now());
        MarketCardNotification marketCardNotification2 = new MarketCardNotification(user.getId(), 3, "Your card (PS5) will be expired in 2h", LocalDateTime.now());
        MarketCardNotification marketCardNotification3 = new MarketCardNotification(user.getId(), 4, "Your card (Chocolate) will be expired in 20h", LocalDateTime.now());
        MarketCardNotification marketCardNotification4 = new MarketCardNotification(anotherUser.getId(), 5, "Your card (Vega) will be expired in 5m", LocalDateTime.now());
        MarketCardNotification marketCardNotification5 = new MarketCardNotification(anotherUser.getId(), 6, "Your card (Chicken) will be expired in 1h", LocalDateTime.now());
        entityManager.persist(marketCardNotification);
        entityManager.persist(marketCardNotification1);
        entityManager.persist(marketCardNotification2);
        entityManager.persist(marketCardNotification3);
        entityManager.persist(marketCardNotification4);
        entityManager.persist(marketCardNotification5);
        entityManager.flush();
    }


    /**
     * Test FindByReceiverIdAndNotificationMessage()
     */
    @Test
    public void testFindByUserIdAndDescription() {
        // given
        String message = "Your card (books) will be expired in 12h";

        // when
        Optional<MarketCardNotification> optionalNotification = marketCardNotificationRepository.findByUserIdAndDescription(user.getId(), message);

        // then
        Assertions.assertTrue(optionalNotification.isPresent());
        Assertions.assertEquals(marketCardNotification.toString(), optionalNotification.get().toString());
    }

    /**
     * Test FindAllByReceiverId()
     */
    @Test
    public void testFindAllByUserId() {
        // when
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAllByUserId(user.getId());

        // then
        Assertions.assertEquals(4, marketCardNotifications.size());
    }
}
