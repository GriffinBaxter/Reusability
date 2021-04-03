package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Product test class
 */
public class ProductTests {

    private static Business business;

    @BeforeAll
    public static void setup() throws Exception {
        business = new Business(
                "name",
                "description",
                "92 River Lum Road, Lumbridge, Misthalin",
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now()
        );
    }

    @Test
    public void TestValidProduct() throws Exception {
        Product product = new Product(
                "PROD",
                1,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                LocalTime.of(0, 0))
        );

        assertEquals("PROD", product.getProductCode());
        assertEquals(1, product.getBusinessId());
        assertEquals("Beans", product.getName());
        assertEquals("Description", product.getDescription());
        assertEquals("Manufacturer", product.getManufacturer());
        assertEquals(20.00, product.getRecommendedRetailPrice());
        assertEquals(LocalDateTime.of(LocalDate.of(2021, 1, 1),
                    LocalTime.of(0, 0)), product.getCreated());
    }

    @Test
    public void TestOptionalFields() throws Exception {
        Product product = new Product(
                "PROD",
                1,
                "Beans",
                "",
                "",
                null,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                LocalTime.of(0, 0))
        );

        assertNull(product.getDescription());
        assertNull(product.getManufacturer());
        assertNull(product.getRecommendedRetailPrice());
    }

    @Test
    public void TestInvalidProductCode() {
        try {
            Product product = new Product(
                    "",
                    1,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid product code", e.getMessage());
        }
    }

    @Test
    public void TestInvalidBusinessIdZero() {
        try {
            Product product = new Product(
                    "PROD",
                    0,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid business ID", e.getMessage());
        }
    }

    @Test
    public void TestInvalidBusinessIdNegative() {
        try {
            Product product = new Product(
                    "PROD",
                    -1,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid business ID", e.getMessage());
        }
    }

    @Test
    public void TestInvalidName() {
        try {
            Product product = new Product(
                    "PROD",
                    1,
                    "",
                    "Description",
                    "Manufacturer",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid product name", e.getMessage());
        }
    }

    @Test
    public void TestInvalidManufacturer() {
        try {
            Product product = new Product(
                    "PROD",
                    1,
                    "Beans",
                    "Description",
                    "",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid manufacturer", e.getMessage());
        }
    }

    @Test
    public void TestInvalidCreatedDate() {
        try {
            Product product = new Product(
                    "PROD",
                    1,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    null
            );
        } catch (Exception e) {
            assertEquals("Invalid date", e.getMessage());
        }
    }
}
