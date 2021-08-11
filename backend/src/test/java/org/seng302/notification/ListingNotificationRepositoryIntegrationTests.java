package org.seng302.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.ListingNotificationRepository;
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
 * ListingNotificationRepository test class.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class ListingNotificationRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ListingNotificationRepository listingNotificationRepository;

    private User user;
    private User anotherUser;
    private User anotherAnotherUser;
    private Business business;
    private ListingNotification listingNotification1;
    private ListingNotification listingNotification2;
    private ListingNotification listingNotification3;
    private Product product1;
    private Product product2;
    private InventoryItem inventoryItem1;
    private InventoryItem inventoryItem2;
    private InventoryItem inventoryItem3;
    private Listing listing1;
    private Listing listing2;
    private Listing listing3;

    /**
     * Sets up objects that are required for testing.
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

        anotherAnotherUser = new User(
                "Debbie",
                "Wilson",
                "Hugh",
                "Debs",
                "bio",
                "debs@example.com",
                LocalDate.of(2020, Month.JANUARY, 1).minusYears(13),
                "1234567555",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        entityManager.persist(anotherAnotherUser);
        entityManager.flush();

        business = new Business(
                user.getId(),
                "Business Name",
                "Description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );
        entityManager.persist(business);
        entityManager.flush();

        // Products
        product1 = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product1);
        entityManager.flush();

        product2 = new Product(
                "APPLE",
                business,
                "Apple",
                "A Description",
                "Manufacturer",
                21.00,
                ""
        );
        entityManager.persist(product2);
        entityManager.flush();

        // Inventory Items
        inventoryItem1 = new InventoryItem(product1,
                product1.getProductId(),
                30,
                1.99,
                59.70,
                LocalDate.of(2020, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 12, 25)
        );
        entityManager.persist(inventoryItem1);
        entityManager.flush();

        inventoryItem2 = new InventoryItem(product2,
                product2.getProductId(),
                30,
                1.99,
                59.70,
                LocalDate.of(2020, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 12, 25)
        );
        entityManager.persist(inventoryItem2);
        entityManager.flush();

        inventoryItem3 = new InventoryItem(product2,
                product2.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 12, 25)
        );
        entityManager.persist(inventoryItem3);
        entityManager.flush();

        // Listings
        listing1 = new Listing(inventoryItem1,
                5,
                12.0,
                "",
                LocalDateTime.of(LocalDate.of(2020, 8, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(0,0,0)));
        entityManager.persist(listing1);
        entityManager.flush();

        listing2 = new Listing(inventoryItem2,
                30,
                59.70,
                "",
                LocalDateTime.of(LocalDate.of(2020, 8, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(0,0,0)));
        entityManager.persist(listing2);
        entityManager.flush();

        listing3 = new Listing(inventoryItem3,
                2,
                13.0,
                "",
                LocalDateTime.of(LocalDate.of(2020, 8, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(0,0,0)));
        entityManager.persist(listing3);
        entityManager.flush();

        // Notifications
        listingNotification1 = new ListingNotification(
                String.format("Listing for (%s) has sold", listing1.getInventoryItem().getProduct().getName())
        );
        listingNotification1.addUser(user);
        entityManager.persist(listingNotification1);
        entityManager.flush();

        listingNotification2 = new ListingNotification(
                String.format("Listing for (%s) has sold", listing2.getInventoryItem().getProduct().getName())
        );
        listingNotification2.addUser(user);
        entityManager.persist(listingNotification2);
        entityManager.flush();

        listingNotification3 = new ListingNotification(
                String.format("Listing for (%s) has sold", listing3.getInventoryItem().getProduct().getName())
        );
        listingNotification3.addUser(user);
        listingNotification3.addUser(anotherUser);
        entityManager.persist(listingNotification3);
        entityManager.flush();
    }

    /**
     * Test that ListingNotificationRepository.finalAllByUsersId() returns listing notifications when user
     * exists and user has listing notifications.
     */
    @Test
    void testFindAllByUsersIdReturnsListingNotifications() {
        List<ListingNotification> listingNotifications = listingNotificationRepository.findAllByUsersId(user.getId());
        int expected_number_notifications = 3;

        Assertions.assertEquals(expected_number_notifications, listingNotifications.size());
    }

    /**
     * Test that ListingNotificationRepository.finalAllByUsersId() returns listing notifications when another user
     * exists and they have listing notifications.
     *
     */
    @Test
    void testFindAllByUsersIdReturnsListingNotificationsWhenAnotherUser() {
        List<ListingNotification> listingNotifications = listingNotificationRepository.findAllByUsersId(anotherUser.getId());
        int expected_number_notifications = 1;

        Assertions.assertEquals(expected_number_notifications, listingNotifications.size());
    }

    /**
     * Test that ListingNotificationRepository.finalAllByUsersId() returns an empty list when user
     * exists and user has no listing notifications.
     */
    @Test
    void testFindAllByUsersIdReturnsEmptyListingNotifications() {
        List<ListingNotification> listingNotifications = listingNotificationRepository.findAllByUsersId(anotherAnotherUser.getId());
        int expected_number_notifications = 0; // empty

        Assertions.assertEquals(expected_number_notifications, listingNotifications.size());
    }

    /**
     * Test that ListingNotificationRepository.finalAllByUsersId() returns an empty list when user doesn't exist.
     */
    @Test
    void testFindAllByUsersIdReturnsEmptyListingNotificationsWhenUserDoesntExist() {
        List<ListingNotification> listingNotifications = listingNotificationRepository.findAllByUsersId(0);
        int expected_number_notifications = 0; // empty

        Assertions.assertEquals(expected_number_notifications, listingNotifications.size());
    }

}
