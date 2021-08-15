package org.seng302.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.Keyword;
import org.seng302.model.KeywordNotification;
import org.seng302.model.repository.KeywordNotificationRepository;
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

/**
 * KeywordNotificationRepository Test Class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class KeywordNotificationRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private KeywordNotificationRepository keywordNotificationRepository;

    Keyword keyword1;
    Keyword keyword2;
    KeywordNotification keywordNotification1;
    KeywordNotification keywordNotification2;
    KeywordNotification keywordNotification3;

    /**
     * Sets up data for testing
     */
    @BeforeEach
    public void setup() throws Exception {
        keyword1 = new Keyword(
                "Beef",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0))
        );
        keyword1 = entityManager.persist(keyword1);
        entityManager.flush();

        keyword2 = new Keyword(
                "Cheese",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0))
        );
        keyword2 = entityManager.persist(keyword2);
        entityManager.flush();

        keywordNotification1 = new KeywordNotification(
                "A new keyword, Beef, has been created",
                LocalDateTime.now(),
                keyword1
        );
        entityManager.persist(keywordNotification1);
        entityManager.flush();

        keywordNotification2 = new KeywordNotification(
                "A new keyword, Beef, has been created",
                LocalDateTime.now(),
                keyword1
        );
        entityManager.persist(keywordNotification2);
        entityManager.flush();

        keywordNotification3 = new KeywordNotification(
                "A new keyword, Cheese, has been created",
                LocalDateTime.now(),
                keyword2
        );
        entityManager.persist(keywordNotification3);
        entityManager.flush();
    }

    /**
     * Testing that all keyword notifications with a given keyword ID are deleted.
     */
    @Test
    void canDeleteAllKeywordNotificationsByKeywordId() {
        List<KeywordNotification> keywordNotifications = keywordNotificationRepository.findAll();
        Assertions.assertTrue(keywordNotifications.contains(keywordNotification1));
        Assertions.assertTrue(keywordNotifications.contains(keywordNotification2));

        // when
        keywordNotificationRepository.deleteAllByKeywordId(keyword1.getId());

        // then
        keywordNotifications = keywordNotificationRepository.findAll();
        Assertions.assertFalse(keywordNotifications.contains(keywordNotification1));
        Assertions.assertFalse(keywordNotifications.contains(keywordNotification2));
        Assertions.assertTrue(keywordNotifications.contains(keywordNotification3));
    }

}
