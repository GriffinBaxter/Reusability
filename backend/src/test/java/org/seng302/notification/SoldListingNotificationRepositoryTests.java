package org.seng302.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.SoldListingNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


/**
 * SoldListingNotificationRepository Test Class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class SoldListingNotificationRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SoldListingNotificationRepository soldListingNotificationRepository;

    private User user;
    private User customer;
    private Business business;
    private Business anotherBusiness;


    private SoldListing soldListing;
    private SoldListing soldListing2;
    private SoldListing anotherSoldListing;

    private SoldListingNotification soldListingNotification;
    private SoldListingNotification soldListingNotification2;
    private SoldListingNotification anotherSoldListingNotification;
    private List<SoldListingNotification> expectedNotifications = new ArrayList<>();

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
        user = entityManager.persist(user);
        entityManager.flush();

        customer = new User(
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
        customer = entityManager.persist(customer);
        entityManager.flush();

        business = new Business(
                user.getId(),
                "example name",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );
        business = entityManager.persist(business);
        entityManager.flush();

        anotherBusiness = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );
        anotherBusiness = entityManager.persist(anotherBusiness);
        entityManager.flush();

        // Sold Listings
        soldListing = new SoldListing(business,
                                    customer,
                                    LocalDateTime.now().minusDays(1),
                                    new ProductId("PROD", business.getId()),
                                    4,
                                    20.0,
                                    5);
        soldListing = entityManager.persist(soldListing);
        entityManager.flush();

        soldListing2 = new SoldListing(business,
                customer,
                LocalDateTime.now().minusDays(1),
                new ProductId("PRODUCT", business.getId()),
                4,
                20.0,
                5);
        soldListing2 = entityManager.persist(soldListing2);
        entityManager.flush();

        anotherSoldListing = new SoldListing(anotherBusiness,
                customer,
                LocalDateTime.now().minusDays(1),
                new ProductId("PRODUC", anotherBusiness.getId()),
                4,
                20.0,
                5);
        anotherSoldListing = entityManager.persist(anotherSoldListing);
        entityManager.flush();

        //Sold Listing Notifications
        soldListingNotification = new SoldListingNotification(business.getId(), soldListing, "Purchased listing");
        soldListingNotification2 = new SoldListingNotification(business.getId(), soldListing2, "Purchased listing 2");
        anotherSoldListingNotification = new SoldListingNotification(anotherBusiness.getId(), anotherSoldListing, "Another listing purchased");
    }

    /**
     * Test findAllByBusinessId() returns a list of SoldListingNotifications when some exist for that business
     */
    @Test
    void testFindAllByBusinessId_returnsListWhenNotificationsExist() {
        // given
        soldListingNotification = entityManager.persist(soldListingNotification);
        entityManager.flush();
        expectedNotifications.add(soldListingNotification);

        soldListingNotification2 = entityManager.persist(soldListingNotification2);
        entityManager.flush();
        expectedNotifications.add(soldListingNotification2);

        anotherSoldListingNotification = entityManager.persist(anotherSoldListingNotification);
        entityManager.flush();

        // when
        List<SoldListingNotification> soldListingNotifications = soldListingNotificationRepository.findAllByBusinessId(business.getId());

        // then
        Assertions.assertEquals(expectedNotifications.size(), soldListingNotifications.size());

        for (int i = 0; i < soldListingNotifications.size(); i++) {
            Assertions.assertEquals(expectedNotifications.get(i).getId(), soldListingNotifications.get(i).getId());
            Assertions.assertEquals(expectedNotifications.get(i).getDescription(), soldListingNotifications.get(i).getDescription());
            Assertions.assertEquals(expectedNotifications.get(i).getSoldListing(), soldListingNotifications.get(i).getSoldListing());
            Assertions.assertEquals(expectedNotifications.get(i).getCreated(), soldListingNotifications.get(i).getCreated());
        }
    }

    /**
     * Test findAllByBusinessId() returns an empty list when there are no SoldListingNotifications for that business
     */
    @Test
    void testFindAllByBusinessId_returnsEmptyListWhenNoNotificationsExist() {
        // given
        anotherSoldListingNotification = entityManager.persist(anotherSoldListingNotification);
        entityManager.flush();

        // when
        List<SoldListingNotification> soldListingNotifications = soldListingNotificationRepository.findAllByBusinessId(business.getId());

        // then
        Assertions.assertTrue(soldListingNotifications.isEmpty());
    }
}
