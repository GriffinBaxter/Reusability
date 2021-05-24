package org.seng302.business.inventoryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessType;
import org.seng302.business.product.Product;
import org.seng302.user.Role;
import org.seng302.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * InventoryItem test class
 */
class InventoryItemTests {

    private Address address;

    private User user;

    private Business business;

    private Product product;

    @BeforeEach
    void setup() throws Exception {
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
                "name",
                "description",
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
    }

    /**
     * Tests that an inventory item can be created given valid parameters.
     *
     * @throws Exception Exception error
     */
    @Test
    void TestValidInventoryItem() throws Exception {
        InventoryItem inventoryItem = new InventoryItem(
                product,
                "PROD",
                2,
                10.00,
                20.00,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1)
        );

        assertEquals(product, inventoryItem.getProduct());
        assertEquals(product.getProductId(), inventoryItem.getProductId());
        assertEquals(business.getId(), inventoryItem.getBusinessId());
        assertEquals(2, inventoryItem.getQuantity());
        assertEquals(10.00, inventoryItem.getPricePerItem());
        assertEquals(20.00, inventoryItem.getTotalPrice());
        assertEquals(LocalDate.now().minusDays(1), inventoryItem.getManufactured());
        assertEquals(LocalDate.now().plusDays(1), inventoryItem.getSellBy());
        assertEquals(LocalDate.now().plusDays(1), inventoryItem.getBestBefore());
        assertEquals(LocalDate.now().plusDays(1), inventoryItem.getExpires());

        InventoryItem inventoryItemExpiresToday = new InventoryItem(
                product,
                "PROD",
                2,
                10.00,
                20.00,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now()
        );
        assertEquals(LocalDate.now(), inventoryItemExpiresToday.getExpires());

        InventoryItem inventoryItemManufacturedToday = new InventoryItem(
                product,
                "PROD",
                2,
                10.00,
                20.00,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1)
        );
        assertEquals(LocalDate.now(), inventoryItemManufacturedToday.getManufactured());
    }

    /**
     * Tests that the optional fields (pricePerItem, totalPrice, manufatured, sellBy, and bestBefore) are set to null
     * when empty, and that this doesn't prevent a inventory item from being created.
     *
     * @throws Exception Exception error
     */
    @Test
    void TestInventoryItemOptionalFields() throws Exception {
        InventoryItem inventoryItem = new InventoryItem(
                product,
                "PROD",
                2,
                null,
                null,
                null,
                null,
                null,
                LocalDate.now().plusDays(1)
        );

        assertNull(inventoryItem.getPricePerItem());
        assertNull(inventoryItem.getTotalPrice());
        assertNull(inventoryItem.getManufactured());
        assertNull(inventoryItem.getSellBy());
        assertNull(inventoryItem.getBestBefore());
    }

    /**
     * Tests that an invalid (null) product object throws an error.
     */
    @Test
    void TestInvalidProduct() {
        try {
            InventoryItem inventoryItem = new InventoryItem(
                    null,
                    "PROD",
                    2,
                    10.00,
                    20.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid product", e.getMessage());
        }
    }

    /**
     * Tests that an invalid product ID (doesn't match the one on the product) throws an error.
     */
    @Test
    void TestInvalidProductId() {
        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PRO",
                    2,
                    10.00,
                    20.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid product or product ID", e.getMessage());
        }
    }

    /**
     * Tests that an invalid quantity (less than or equal to 0) throws an error.
     */
    @Test
    void TestInvalidQuantity() {
        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PROD",
                    0,
                    10.00,
                    20.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid quantity, must have at least one item", e.getMessage());
        }

        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PROD",
                    -1,
                    10.00,
                    20.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid quantity, must have at least one item", e.getMessage());
        }
    }

    /**
     * Tests that an invalid price per item (less than 0) throws an error.
     */
    @Test
    void TestInvalidPricePerItem() {
        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PROD",
                    2,
                    0.00,
                    20.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid price per item, must not be negative", e.getMessage());
        }
    }

    /**
     * Tests that an invalid total price (less than 0) throws an error.
     */
    @Test
    void TestInvalidTotalPrice() {
        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PROD",
                    2,
                    10.00,
                    0.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid total price, must not be negative", e.getMessage());
        }
    }

    /**
     * Tests that an invalid manufacture date (after current date) throws an error.
     */
    @Test
    void TestInvalidManufactured() {
        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PROD",
                    2,
                    10.00,
                    20.00,
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid manufacture date", e.getMessage());
        }
    }

    /**
     * Tests that an invalid expiration date (null or before current date) throws an error.
     */
    @Test
    void TestInvalidExpires() {
        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PROD",
                    2,
                    10.00,
                    20.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    null
            );
        } catch (Exception e) {
            assertEquals("Invalid expiration date, must have expiration date and cannot add expired item", e.getMessage());
        }

        try {
            InventoryItem inventoryItem = new InventoryItem(
                    product,
                    "PROD",
                    2,
                    10.00,
                    20.00,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().minusDays(1)
            );
        } catch (Exception e) {
            assertEquals("Invalid expiration date, must have expiration date and cannot add expired item", e.getMessage());
        }
    }
}
