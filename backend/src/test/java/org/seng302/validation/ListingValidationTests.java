package org.seng302.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessType;
import org.seng302.business.inventoryItem.InventoryItem;
import org.seng302.business.listing.Listing;
import org.seng302.business.product.Product;
import org.seng302.user.Role;
import org.seng302.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

class ListingValidationTests {

    private Address address;

    private User user;

    private Business business;

    private Product product;

    private InventoryItem inventoryItem;

    private Listing listing;

    @BeforeEach
    void setUp() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User(
                "first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        business = new Business(
                user.getId(),
                "business name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );
        business.setId(1);
        product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        inventoryItem = new InventoryItem(
                product,
                product.getProductId(),
                30,
                7.00,
                210.00,
                null,
                null,
                null,
                LocalDate.of(2022, 1,1)
        );
    }

    // ********************************* MORE INFO **************************************

    /**
     * Test to see whether true (i.e valid) is returned when more info
     * has the same length as the min length.
     */
    @Test
    void isValidMoreInfoEqualMinLength() {
        String moreInfo = ""; // minLength = 0
        assertEquals(true, ListingValidation.isValidMoreInfo(moreInfo));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when more info
     * has a length greater than the max length.
     */
    @Test
    void isValidMoreInfoGreaterThanMaxLength() {
        String string = "Z";
        String moreInfo = string.repeat(601); //maxLength = 600
        assertEquals(false, ListingValidation.isValidMoreInfo(moreInfo));
    }

    /**
     * Test to see whether true (i.e valid) is returned when more info
     * has a length equal to the max length.
     */
    @Test
    void isValidMoreInfoEqualToMaxLength() {
        String string = "Z";
        String moreInfo = string.repeat(600); //maxLength = 600
        assertEquals(true, ListingValidation.isValidMoreInfo(moreInfo));
    }

    /**
     * Test to see whether true (i.e valid) is returned when more info
     * has correct length and contains symbols, numbers etc.
     */
    @Test
    void isValidMoreInfoCorrectLengthContainsSymbolsAndNumbers() {
        String moreInfo = "Willing t0 accept low3r offers !^#9p4039*$";
        assertEquals(true, ListingValidation.isValidMoreInfo(moreInfo));
    }

    // ********************************** QUANTITY **************************************

    /**
     * Test to see whether false (i.e invalid) is returned when listing quantity
     * is less the min quantity i.e quantity = 0
     */
    @Test
    void isValidQuantityLessThanMinQuantity() {
        int quantity = 0;
        assertEquals(false, ListingValidation.isValidQuantity(quantity, inventoryItem));
    }

    /**
     * Test to see whether true (i.e valid) is returned when listing quantity
     * is in range
     */
    @Test
    void isValidQuantityInRange() {
        int quantity = 10;
        assertEquals(true, ListingValidation.isValidQuantity(quantity, inventoryItem));
    }

    /**
     * Test to see whether true (i.e valid) is returned when listing quantity
     * equals the max quantity.
     */
    @Test
    void isValidQuantityEqualToMaxQuantity() {
        int quantity = 30;
        assertEquals(true, ListingValidation.isValidQuantity(quantity, inventoryItem));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when listing quantity
     * is greater than max quantity and no other listings exist.
     */
    @Test
    void isValidQuantityGreaterMaxQuantityNoOtherListings() {
        int quantity = 35;
        assertEquals(false, ListingValidation.isValidQuantity(quantity, inventoryItem));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when listing quantity
     * is greater than max quantity due to other listings existing.
     */
    @Test
    void isValidQuantityGreaterMaxQuantityOtherListings() throws Exception {
        listing = new Listing(
                inventoryItem,
                15,
                99.99,
                "More info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1,1, 0, 0)
        );
        inventoryItem.addListing(listing);
        int quantity = 20;
        assertEquals(false, ListingValidation.isValidQuantity(quantity, inventoryItem));
    }

    /**
     * Test to see whether true (i.e valid) is returned when listing quantity
     * is in range when other listings exist.
     */
    @Test
    void isValidQuantityInRangeOtherListings() throws Exception {
        listing = new Listing(
                inventoryItem,
                15,
                99.99,
                "More info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1,1, 0, 0)
        );
        inventoryItem.addListing(listing);
        int quantity = 10;
        assertEquals(true, ListingValidation.isValidQuantity(quantity, inventoryItem));
    }

}
