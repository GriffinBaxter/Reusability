package org.seng302.business.soldListing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.*;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.NotificationType;
import org.seng302.model.enums.Role;
import org.seng302.view.outgoing.ListingNotificationPayload;
import org.seng302.view.outgoing.SoldListingNotificationPayload;
import org.seng302.view.outgoing.SoldListingPayload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

class SoldListingNotificationTests {

    private User user;
    private User customer;
    private Business business;
    private SoldListing soldListing;
    private SoldListingNotification soldListingNotification;

    @BeforeEach
    void before() throws IllegalUserArgumentException, IllegalAddressArgumentException, IllegalListingNotificationArgumentException, IllegalSoldListingNotificationArgumentException, IllegalBusinessArgumentException, IllegalSoldListingArgumentException {
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

        business = new Business(
                user.getId(),
                "example name",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );

        soldListing = new SoldListing(business,
                customer,
                LocalDateTime.now().minusDays(1),
                "PROD",
                4,
                20.0,
                5);

        soldListingNotification = new SoldListingNotification(business.getId(), soldListing, "Purchased listing");
    }

    /**
     * Test to see whether SoldListingNotification is successfully created when all data is valid
     */
    @Test
    void isSoldListingNotificationCreatedWhenDataIsValidTest() throws IllegalSoldListingNotificationArgumentException {
        SoldListingNotification newSoldListingNotification = new SoldListingNotification(business.getId(), soldListing, "Purchased listing");
        LocalDateTime created = LocalDateTime.now();

        Assertions.assertEquals("Purchased listing", newSoldListingNotification.getDescription());
        Assertions.assertEquals(business.getId(), newSoldListingNotification.getBusinessId());
        Assertions.assertEquals(soldListing, newSoldListingNotification.getSoldListing());
        Assertions.assertEquals(soldListing.getId(), newSoldListingNotification.getSoldListingId());
        Assertions.assertEquals(created.getDayOfYear(), newSoldListingNotification.getCreated().getDayOfYear());
        Assertions.assertEquals(created.getHour(), newSoldListingNotification.getCreated().getHour());
        Assertions.assertEquals(created.getMinute(), newSoldListingNotification.getCreated().getMinute());
    }

    /**
     * Test to see whether SoldListingNotification is successfully created when the description length is equal to the minimum length
     */
    @Test
    void isSoldListingNotificationCreatedWhenDescriptionIsMinimumLengthTest() throws IllegalSoldListingNotificationArgumentException {
        SoldListingNotification newSoldListingNotification = new SoldListingNotification(business.getId(), soldListing, "Descriptio");
        LocalDateTime created = LocalDateTime.now();

        Assertions.assertEquals("Descriptio", newSoldListingNotification.getDescription());
        Assertions.assertEquals(business.getId(), newSoldListingNotification.getBusinessId());
        Assertions.assertEquals(soldListing, newSoldListingNotification.getSoldListing());
        Assertions.assertEquals(soldListing.getId(), newSoldListingNotification.getSoldListingId());
        Assertions.assertEquals(created.getDayOfYear(), newSoldListingNotification.getCreated().getDayOfYear());
        Assertions.assertEquals(created.getHour(), newSoldListingNotification.getCreated().getHour());
        Assertions.assertEquals(created.getMinute(), newSoldListingNotification.getCreated().getMinute());
    }

    /**
     * Test to see whether SoldListingNotification is not successfully created when the description length is less than the minimum length
     */
    @Test
    void isSoldListingNotificationNotCreatedWhenDescriptionIsLessThanMinimumLengthTest() {
        try {
            SoldListingNotification newSoldListingNotification = new SoldListingNotification(business.getId(), soldListing, "Descripti");
            Assertions.fail("IllegalSoldListingNotificationArgumentException was expected to be thrown.");
        } catch (IllegalSoldListingNotificationArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether SoldListingNotification is successfully created when the description length is equal to the maximum length
     */
    @Test
    void isSoldListingNotificationCreatedWhenDescriptionIsMaximumLengthTest() throws IllegalSoldListingNotificationArgumentException {
        SoldListingNotification newSoldListingNotification = new SoldListingNotification(business.getId(), soldListing, "D".repeat(600));
        LocalDateTime created = LocalDateTime.now();

        Assertions.assertEquals("D".repeat(600), newSoldListingNotification.getDescription());
        Assertions.assertEquals(business.getId(), newSoldListingNotification.getBusinessId());
        Assertions.assertEquals(soldListing, newSoldListingNotification.getSoldListing());
        Assertions.assertEquals(soldListing.getId(), newSoldListingNotification.getSoldListingId());
        Assertions.assertEquals(created.getDayOfYear(), newSoldListingNotification.getCreated().getDayOfYear());
        Assertions.assertEquals(created.getHour(), newSoldListingNotification.getCreated().getHour());
        Assertions.assertEquals(created.getMinute(), newSoldListingNotification.getCreated().getMinute());
    }

    /**
     * Test to see whether SoldListingNotification is not successfully created when the description length is greater than the maximum length
     */
    @Test
    void isSoldListingNotificationNotCreatedWhenDescriptionIsGreaterThanMaximumLengthTest() {
        try {
            SoldListingNotification newSoldListingNotification = new SoldListingNotification(business.getId(), soldListing, "D".repeat(601));
            Assertions.fail("IllegalSoldListingNotificationArgumentException was expected to be thrown.");
        } catch (IllegalSoldListingNotificationArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether SoldListingNotification is not successfully created when the business ID does not match the business ID in the sold listing
     */
    @Test
    void isSoldListingNotificationNotCreatedWhenBusinessIdDoesNotMatchTest() {
        try {
            SoldListingNotification newSoldListingNotification = new SoldListingNotification(soldListing.getBusiness().getId() + 1, soldListing, "Purchased listing");
            Assertions.fail("IllegalSoldListingNotificationArgumentException was expected to be thrown.");
        } catch (IllegalSoldListingNotificationArgumentException e) {
            Assertions.assertEquals("Invalid business ID", e.getMessage());
        }
    }

    /**
     * Test to see whether SoldListingNotification is correctly converted to a SoldListingNotificationPayload
     */
    @Test
    void isSoldListingNotificationConvertedToPayloadCorrectlyTest() throws Exception {
        SoldListingNotificationPayload soldListingNotificationPayload  = soldListingNotification.toSoldListingNotificationPayload();
        SoldListingPayload soldListingPayload = soldListingNotification.getSoldListing().toSoldListingPayload();

        Assertions.assertEquals(soldListingNotification.getId(), soldListingNotificationPayload.getId());
        Assertions.assertEquals(soldListingNotification.getDescription(), soldListingNotificationPayload.getDescription());
        Assertions.assertEquals(soldListingNotification.getCreated().toString(), soldListingNotificationPayload.getCreated());
        Assertions.assertEquals(NotificationType.SOLD_LISTING.toString(), soldListingNotificationPayload.getNotificationType());

        Assertions.assertEquals(soldListingPayload.getId(), soldListingNotificationPayload.getSoldListing().getId());
        Assertions.assertEquals(soldListingPayload.getListingDate(), soldListingNotificationPayload.getSoldListing().getListingDate());
        Assertions.assertEquals(soldListingPayload.getSaleDate(), soldListingNotificationPayload.getSoldListing().getSaleDate());
        Assertions.assertEquals(soldListingPayload.getQuantity(), soldListingNotificationPayload.getSoldListing().getQuantity());
        Assertions.assertEquals(soldListingPayload.getPrice(), soldListingNotificationPayload.getSoldListing().getPrice());
        Assertions.assertEquals(soldListingPayload.getBookmarks(), soldListingNotificationPayload.getSoldListing().getBookmarks());
        Assertions.assertEquals(soldListingPayload.getProductId(), soldListingNotificationPayload.getSoldListing().getProductId());

        Assertions.assertEquals(soldListingPayload.getCustomer().getId(), soldListingNotificationPayload.getSoldListing().getCustomer().getId());
        Assertions.assertEquals(soldListingPayload.getCustomer().getEmail(), soldListingNotificationPayload.getSoldListing().getCustomer().getEmail());
    }

}
