package org.seng302.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.Address;
import org.seng302.model.Notification;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.NotificationRepository;
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
public class NotificationRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    private User user;
    private User anotherUser;
    private Notification notification;

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

        notification = new Notification(user.getId(), "Your card (books) will be expired", "in 12h");
        Notification notification1 = new Notification(user.getId(), "Your card (Beef) will be expired", "in 10h");
        Notification notification2 = new Notification(user.getId(), "Your card (PS5) will be expired", "in 2h");
        Notification notification3 = new Notification(user.getId(), "Your card (Chocolate) will be expired", "in 20h");
        Notification notification4 = new Notification(anotherUser.getId(), "Your card (Vega) will be expired", "in 5m");
        Notification notification5 = new Notification(anotherUser.getId(), "Your card (Chicken) will be expired", "in 1h");
        entityManager.persist(notification);
        entityManager.persist(notification1);
        entityManager.persist(notification2);
        entityManager.persist(notification3);
        entityManager.persist(notification4);
        entityManager.persist(notification5);
        entityManager.flush();
    }


    @Test
    public void testFindByReceiverIdAndNotificationMessage() {
        // given
        String message = "Your card (books) will be expired";

        // when
        Optional<Notification> optionalNotification = notificationRepository.findByReceiverIdAndNotificationMessage(user.getId(), message);

        // then
        Assertions.assertTrue(optionalNotification.isPresent());
        Assertions.assertEquals(notification.expiryFormTOString(), optionalNotification.get().expiryFormTOString());
    }

    @Test
    public void testFindAllByReceiverId() {
        // when
        List<Notification> notifications = notificationRepository.findAllByReceiverId(user.getId());

        // then
        Assertions.assertEquals(4, notifications.size());
    }
}
