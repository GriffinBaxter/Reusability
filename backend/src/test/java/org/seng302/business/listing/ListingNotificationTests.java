package org.seng302.business.listing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.*;
import org.seng302.model.*;
import org.seng302.model.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ListingNotification test class.
 * This class tests the validation for ListingNotification.
 */
class ListingNotificationTests {

    private User user;
    private ListingNotification listingNotification;

    @BeforeEach
    void before() throws IllegalUserArgumentException, IllegalAddressArgumentException, IllegalListingNotificationArgumentException {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        listingNotification = new ListingNotification("Listing description");
    }

    /**
     * Test to see whether ListingNotification is successfully created when all data is valid
     */
    @Test
    void isListingNotificationCreatedWhenDataIsValidTest() throws IllegalListingNotificationArgumentException {
        ListingNotification newListingNotification = new ListingNotification("Description");
        LocalDateTime created = LocalDateTime.now();

        Assertions.assertEquals("Description", newListingNotification.getDescription());
        Assertions.assertEquals(created.getDayOfYear(), newListingNotification.getCreated().getDayOfYear());
        Assertions.assertEquals(created.getHour(), newListingNotification.getCreated().getHour());
        Assertions.assertEquals(created.getMinute(), newListingNotification.getCreated().getMinute());
    }

    /**
     * Test to see whether ListingNotification is successfully created when the description length is equal to the minimum length
     */
    @Test
    void isListingNotificationCreatedWhenDescriptionIsMinimumLengthTest() throws IllegalListingNotificationArgumentException {
        ListingNotification newListingNotification = new ListingNotification("Descriptio");
        LocalDateTime created = LocalDateTime.now();

        Assertions.assertEquals("Descriptio", newListingNotification.getDescription());
        Assertions.assertEquals(created.getDayOfYear(), newListingNotification.getCreated().getDayOfYear());
        Assertions.assertEquals(created.getHour(), newListingNotification.getCreated().getHour());
        Assertions.assertEquals(created.getMinute(), newListingNotification.getCreated().getMinute());
    }

    /**
     * Test to see whether ListingNotification is not successfully created when the description length is less than the minimum length
     */
    @Test
    void isListingNotificationNotCreatedWhenDescriptionIsLessThanMinimumLengthTest() {
        try {
            ListingNotification newListingNotification = new ListingNotification("Descripti");
        } catch (IllegalListingNotificationArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether ListingNotification is successfully created when the description length is equal to the maximum length
     */
    @Test
    void isListingNotificationCreatedWhenDescriptionIsMaximumLengthTest() throws IllegalListingNotificationArgumentException {
        ListingNotification newListingNotification = new ListingNotification("D".repeat(600));
        LocalDateTime created = LocalDateTime.now();

        Assertions.assertEquals("D".repeat(600), newListingNotification.getDescription());
        Assertions.assertEquals(created.getDayOfYear(), newListingNotification.getCreated().getDayOfYear());
        Assertions.assertEquals(created.getHour(), newListingNotification.getCreated().getHour());
        Assertions.assertEquals(created.getMinute(), newListingNotification.getCreated().getMinute());
    }

    /**
     * Test to see whether ListingNotification is not successfully created when the description length is greater than the maximum length
     */
    @Test
    void isListingNotificationNotCreatedWhenDescriptionIsGreaterThanMaximumLengthTest() {
        try {
            ListingNotification newListingNotification = new ListingNotification("D".repeat(601));
        } catch (IllegalListingNotificationArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether a User can be added to a ListingNotification list of users
     */
    @Test
    void canAddUserToListingNotificationTest() {
        listingNotification.addUser(user);

        Assertions.assertTrue(listingNotification.getUsers().contains(user));
    }

    /**
     * Test to see whether a User can be removed from a ListingNotification list of users
     */
    @Test
    void canRemoveUserFromListingNotificationTest() {
        listingNotification.addUser(user);
        Assertions.assertTrue(listingNotification.getUsers().contains(user));

        listingNotification.removeUser(user);
        Assertions.assertFalse(listingNotification.getUsers().contains(user));
    }

    /**
     * Test to see whether a ListingNotification can be added to a User list of listing notifications
     */
    @Test
    void canAddListingNotificationToUserTest() {
        user.addListingNotification(listingNotification);

        Assertions.assertTrue(user.getListingNotifications().contains(listingNotification));
        Assertions.assertTrue(listingNotification.getUsers().contains(user));
    }

    /**
     * Test to see whether a ListingNotification can be removed from a User list of listing notifications
     */
    @Test
    void canRemoveListingNotificationFromUserTest() {
        user.addListingNotification(listingNotification);
        Assertions.assertTrue(user.getListingNotifications().contains(listingNotification));
        Assertions.assertTrue(listingNotification.getUsers().contains(user));

        user.removeListingNotification(listingNotification);
        Assertions.assertFalse(user.getListingNotifications().contains(listingNotification));
        Assertions.assertFalse(listingNotification.getUsers().contains(user));
    }
}
