package org.seng302.validation;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Product validation test class
 */
public class ProductValidationTests {

    // ******************************** PRODUCT ID ***********************************

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product ID is equal to the minimum length.
     */
    @Test
    public void isValidProductIdMinimumLength() {
        String productId = "PRO";
        assertTrue(ProductValidation.isValidProductId(productId));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product ID is equal to the maximum length.
     */
    @Test
    public void isValidProductIdMaximumLength() {
        String productId = "PRODUCTID-12345";
        assertTrue(ProductValidation.isValidProductId(productId));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product ID is between the maximum and minimum length and contains valid symbols.
     */
    @Test
    public void isValidProductIdValidSymbols() {
        String productId = "PRODUCT-ID-123";
        assertTrue(ProductValidation.isValidProductId(productId));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * product ID is less than the minimum length.
     */
    @Test
    public void isValidProductIdLessThanMinimumLength() {
        String productId = "PR";
        assertFalse(ProductValidation.isValidProductId(productId));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * product ID is greater than the maximum length.
     */
    @Test
    public void isValidProductIdGreaterThanMaximumLength() {
        String productId = "PRODUCTID-123456";
        assertFalse(ProductValidation.isValidProductId(productId));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * product ID is between the maximum and minimum length but contains invalid symbols.
     */
    @Test
    public void isValidProductIdInvalidSymbols() {
        String productId = "PRODUCT- 123";
        assertFalse(ProductValidation.isValidProductId(productId));

        productId = "PRODUCT!1?#23";
        assertFalse(ProductValidation.isValidProductId(productId));
    }

    // ******************************** NAME ***********************************

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product name is equal to the minimum length.
     */
    @Test
    public void isValidNameMinimumLength() {
        String name = "A";
        assertTrue(ProductValidation.isValidName(name));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product name is equal to the maximum length.
     */
    @Test
    public void isValidNameMaximumLength() {
        String string = "A";
        String name = string.repeat(100);
        assertTrue(ProductValidation.isValidName(name));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product name is between the maximum and minimum length and contains valid symbols.
     */
    @Test
    public void isValidNameValidSymbols() {
        String name = "New '#,.&()- 123g";
        assertTrue(ProductValidation.isValidName(name));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * product name is less than the minimum length.
     */
    @Test
    public void isValidNameLessThanMinimumLength() {
        String name = "";
        assertFalse(ProductValidation.isValidName(name));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * product name is greater than the maximum length.
     */
    @Test
    public void isValidNameGreaterThanMaximumLength() {
        String string = "A";
        String name = string.repeat(101);
        assertFalse(ProductValidation.isValidName(name));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * product name is between the maximum and minimum length but contains invalid symbols.
     */
    @Test
    public void isValidNameInvalidSymbols() {
        String name = "New %@!";
        assertFalse(ProductValidation.isValidName(name));
    }

    // ******************************** DESCRIPTION ***********************************

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product description is equal to the minimum length.
     */
    @Test
    public void isValidDescriptionMinimumLength() {
        String description = "";
        assertTrue(ProductValidation.isValidDescription(description));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product description is equal to the maximum length.
     */
    @Test
    public void isValidDescriptionMaximumLength() {
        String string = "A";
        String description = string.repeat(600);
        assertTrue(ProductValidation.isValidDescription(description));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * product description is between the maximum and minimum length.
     */
    @Test
    public void isValidDescriptionValidLength() {
        String description = "Product description.";
        assertTrue(ProductValidation.isValidDescription(description));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * product description is greater than the maximum length.
     */
    @Test
    public void isValidDescriptionGreaterThanMaximumLength() {
        String string = "A";
        String description = string.repeat(601);
        assertFalse(ProductValidation.isValidDescription(description));
    }

    // ******************************** MANUFACTURER ***********************************

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * manufacturer name is equal to the minimum length.
     */
    @Test
    public void isValidManufacturerMinimumLength() {
        String manufacturer = "";
        assertTrue(ProductValidation.isValidManufacturer(manufacturer));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * manufacturer name is equal to the maximum length.
     */
    @Test
    public void isValidManufacturerMaximumLength() {
        String string = "A";
        String manufacturer = string.repeat(100);
        assertTrue(ProductValidation.isValidManufacturer(manufacturer));
    }

    /**
     * Test to see whether true (i.e. valid) is returned when the length of the
     * manufacturer name is between the maximum and minimum length and contains valid symbols.
     */
    @Test
    public void isValidManufacturerValidSymbols() {
        String manufacturer = "New '#,.&()- 123g";
        assertTrue(ProductValidation.isValidManufacturer(manufacturer));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * manufacturer name is greater than the maximum length.
     */
    @Test
    public void isValidManufacturerGreaterThanMaximumLength() {
        String string = "A";
        String manufacturer = string.repeat(101);
        assertFalse(ProductValidation.isValidManufacturer(manufacturer));
    }

    /**
     * Test to see whether false (i.e. invalid) is returned when the length of the
     * manufacturer name is between the maximum and minimum length but contains invalid symbols.
     */
    @Test
    public void isValidManufacturerInvalidSymbols() {
        String manufacturer = "New %@!";
        assertFalse(ProductValidation.isValidManufacturer(manufacturer));
    }
}
