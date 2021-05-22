package org.seng302.business.listing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessType;
import org.seng302.business.inventoryItem.InventoryItem;
import org.seng302.business.product.Product;
import org.seng302.user.Role;
import org.seng302.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListingTests {

    private Address address;

    private User user;

    private Business business;

    private Product product;

    private InventoryItem inventoryItem;

    @BeforeEach
    public void setUp() throws Exception {
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
                4,
                6.5,
                21.99,
                null,
                null,
                null,
                LocalDate.of(2022, 1,1)
        );
    }

    /**
     * when price not exist and quantity less then inventory quantity
     * @throws Exception
     */
    @Test
    public void testCalculatePriceWhenPriceNotExistAndQuantityLessThenInventoryQuantity() throws Exception {
        Listing listing = new Listing(
                inventoryItem,
                3,
                null,
                "more info",
                LocalDateTime.now(),
                null
        );
        assertTrue(listing.getPrice() == 19.50);
    }

    /**
     * when price not exist and quantity equal to inventory quantity
     * @throws Exception
     */
    @Test
    public void testCalculatePriceWhenPriceNotExistAndQuantityEqualToInventoryQuantity() throws Exception {
        Listing listing = new Listing(
                inventoryItem,
                4,
                null,
                "more info",
                LocalDateTime.now(),
                null
        );
        assertTrue(listing.getPrice() == 21.99);
    }

    /**
     * when price exist and quantity equal to inventory quantity
     * @throws Exception
     */
    @Test
    public void testCalculatePriceWillBeOverriddenWhenPriceExist() throws Exception {
        Listing listing = new Listing(
                inventoryItem,
                4,
                30.00,
                "more info",
                LocalDateTime.now(),
                null
        );
        assertTrue(listing.getPrice() == 30.00);
    }

    /**
     * when price exist and quantity less then inventory quantity
     * @throws Exception
     */
    @Test
    public void testCalculatePriceWillBeOverriddenWhenPriceExistEvenQuantityNotEqualToInventory() throws Exception {
        Listing listing = new Listing(
                inventoryItem,
                3,
                30.00,
                "more info",
                LocalDateTime.now(),
                null
        );
        assertTrue(listing.getPrice() == 30.00);
    }

    /**
     * Test an exception is thrown when no inventoryItem exists (null).
     * @throws Exception 'Invalid inventory item'
     */
    @Test
    public void TestInvalidInventoryItem() throws Exception {
        try {
            Listing listing = new Listing(
                    null,
                    3,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    null
            );
        } catch (Exception e) {
            Assertions.assertEquals("Invalid inventory item", e.getMessage());
        }
    }

    /**
     * Test an exception is thrown when created doesn't exist (null).
     * @throws Exception 'Invalid creation date'
     */
    @Test
    public void TestInvalidCreated() throws Exception {
        try {
            Listing listing = new Listing(
                    inventoryItem,
                    3,
                    30.00,
                    "more info",
                    null,
                    null
            );
        } catch (Exception e) {
            Assertions.assertEquals("Invalid creation date", e.getMessage());
        }
    }

    /**
     * Test an exception is thrown when closes date occurs before current date.
     * @throws Exception 'Invalid closing date'
     */
    @Test
    public void TestInvalidClosingDate() throws Exception {
        try {
            Listing listing = new Listing(
                    inventoryItem,
                    3,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    LocalDateTime.of(2000, 10, 11, 0,0)
            );
        } catch (Exception e) {
            Assertions.assertEquals("Invalid closing date.", e.getMessage());
        }
    }

    /**
     * Test closes date is set to expiry date of inventory time when not set (null)
     * @throws Exception if field of Listing is not valid.
     */
    @Test
    public void TestNoClosingDateSet() throws Exception {
        Listing listing = new Listing(
                    inventoryItem,
                    3,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    null
            );
        Assertions.assertEquals(listing.getCloses(), LocalDateTime.of(inventoryItem.getExpires(), LocalTime.of(0, 0)));
    }

    /**
     * Test closes date is not overridden to expiry date of inventory item when supplied to constructor.
     * @throws Exception if field of Listing is not valid.
     */
    @Test
    public void TestClosingDateSupplied() throws Exception {
        Listing listing = new Listing(
                inventoryItem,
                3,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertEquals(listing.getCloses(), LocalDateTime.of(2022, 1, 1, 0, 0));
    }
}
