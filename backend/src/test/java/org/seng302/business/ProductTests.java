package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

    private Business business;

    @BeforeEach
    public void setup() throws Exception {
        business = new Business(
                "name",
                "description",
                "92 River Lum Road, Lumbridge, Misthalin",
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now()
        );
        business.setId(1);
    }

    /**
     * Tests that a product can be created given valid parameters.
     *
     * @throws Exception Exception error
     */
    @Test
    public void TestValidProduct() throws Exception {
        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                20.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                LocalTime.of(0, 0))
        );

        assertEquals("PROD", product.getProductId());
        assertEquals(1, product.getBusinessId());
        assertEquals("Beans", product.getName());
        assertEquals("Description", product.getDescription());
        assertEquals(20.00, product.getRecommendedRetailPrice());
        assertEquals(LocalDateTime.of(LocalDate.of(2021, 1, 1),
                    LocalTime.of(0, 0)), product.getCreated());
    }

    /**
     * Tests that the optional fields (description and recommendedRetailPrice) are set to null when empty,
     * and that this doesn't prevent a product from being created.
     *
     * @throws Exception Exception error
     */
    @Test
    public void TestOptionalFields() throws Exception {
        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "",
                null,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                LocalTime.of(0, 0))
        );

        assertNull(product.getDescription());
        assertNull(product.getRecommendedRetailPrice());
    }

    /**
     * Tests that an invalid product code throws an error.
     */
    @Test
    public void TestInvalidProductCode() {
        try {
            Product product = new Product(
                    "",
                    business,
                    "Beans",
                    "Description",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid product id", e.getMessage());
        }
    }

    /**
     * Tests that an invalid (null) business object throws an error.
     */
    @Test
    public void TestInvalidBusiness() {
        try {
            Product product = new Product(
                    "PROD",
                    null,
                    "Beans",
                    "Description",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid business", e.getMessage());
        }
    }

    /**
     * Tests that an invalid name throws an error.
     */
    @Test
    public void TestInvalidName() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "",
                    "Description",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                    LocalTime.of(0, 0))
            );
        } catch (Exception e) {
            assertEquals("Invalid product name", e.getMessage());
        }
    }

    /**
     * Tests that an invalid creation date throws an error.
     */
    @Test
    public void TestInvalidCreatedDate() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    20.00,
                    null
            );
        } catch (Exception e) {
            assertEquals("Invalid date", e.getMessage());
        }
    }
}
