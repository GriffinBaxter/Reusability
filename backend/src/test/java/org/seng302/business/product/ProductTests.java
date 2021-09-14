package org.seng302.business.product;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalProductArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.Product;
import org.seng302.model.enums.Role;
import org.seng302.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Product test class
 * This class includes tests to see whether IllegalProductArgumentException's are thrown when invalid data
 * is supplied when creating a new Product.
 */
class ProductTests {

    private Address address;
    private User user;
    private Business business;
    private Product setProduct;

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
                "business name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user,
                "$",
                "NZD"
        );
        business.setId(1);
        setProduct = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

    }

    /**
     * Tests that a product can be created given valid parameters.
     *
     * @throws IllegalProductArgumentException Exception error
     */
    @Test
    void testValidProduct() throws IllegalProductArgumentException {
        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

        assertEquals("PROD", product.getProductId());
        assertEquals(1, product.getBusinessId());
        assertEquals("Beans", product.getName());
        assertEquals("Description", product.getDescription());
        assertEquals("Manufacturer", product.getManufacturer());
        assertEquals(20.00, product.getRecommendedRetailPrice());
        assertEquals("9400547002634", product.getBarcode());
    }

    /**
     * Tests that the optional fields (description, manufacturer, recommendedRetailPrice and barcode)
     * are set to null when empty, and that this doesn't prevent a product from being created.
     *
     * @throws IllegalProductArgumentException Exception error
     */
    @Test
    void testProductOptionalFields() throws IllegalProductArgumentException {
        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "",
                "",
                null,
                ""
        );

        assertNull(product.getDescription());
        assertNull(product.getManufacturer());
        assertNull(product.getRecommendedRetailPrice());
        assertNull(product.getBarcode());
    }

    /**
     * Tests that an invalid product code throws an error.
     */
    @Test
    void testInvalidProductCode() {
        try {
            Product product = new Product(
                    "",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    ""
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid product ID", e.getMessage());
        }
    }

    /**
     * Tests that an invalid (null) business object throws an error.
     */
    @Test
    void testInvalidBusiness() {
        try {
            Product product = new Product(
                    "PROD",
                    null,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    ""
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid business", e.getMessage());
        }
    }

    /**
     * Tests that an invalid name throws an error.
     */
    @Test
    void testInvalidName() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "",
                    "Description",
                    "Manufacturer",
                    20.00,
                    ""
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid product name", e.getMessage());
        }
    }

    /**
     * Tests that an invalid description throws an error.
     */
    @Test
    void testInvalidDescription() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Name",
                    "A".repeat(601),
                    "Manufacturer",
                    20.00,
                    ""
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid product description", e.getMessage());
        }
    }

    /**
     * Tests that an invalid manufacturer throws an error.
     */
    @Test
    void testInvalidManufacturer() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Name",
                    "Description",
                    "Manufacturer!23",
                    20.00,
                    ""
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid manufacturer", e.getMessage());
        }
    }


    /**
     * Tests that an invalid RRP throws an error.
     */
    @Test
    void testInvalidRRP() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    -1.00,
                    ""
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid recommended retail price", e.getMessage());
        }
    }

    /**
     * Tests that a product can be created given valid parameters and name and manufacturer contain diacritics.
     *
     * @throws IllegalProductArgumentException Exception error
     */
    @Test
    void testValidProductNameAndManufacturerIncludeDiacritics() throws IllegalProductArgumentException {
        Product product = new Product(
                "PROD",
                business,
                "L'Oréal Shampoo",
                "Description",
                "L'Oréal",
                20.00,
                "9400547002634"
        );

        assertEquals("PROD", product.getProductId());
        assertEquals(1, product.getBusinessId());
        assertEquals("L'Oréal Shampoo", product.getName());
        assertEquals("Description", product.getDescription());
        assertEquals("L'Oréal", product.getManufacturer());
        assertEquals(20.00, product.getRecommendedRetailPrice());
        assertEquals("9400547002634", product.getBarcode());
    }

    /* ------------------------------------------------- isValidBarcodeTests -----------------------------------------*/

    /**
     * A barcode must be a string representation of only numbers. Therefore, if the barcode string contains
     * letters an exception must be thrown. This method tests whether this is the case.
     */
    @Test
    void isInvalidBarcodeExceptionThrownWhenBarcodeIsAStringNotContainingOnlyNumbersTest() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    "INVALID123456"
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid barcode checksum", e.getMessage());
        }
    }

    /**
     * A barcode must be a string representation of only numbers. Therefore, if the barcode string contains
     * letters an exception must be thrown. This method tests whether this is the case.
     */
    @Test
    void isInvalidBarcodeExceptionThrownWhenBarcodeIsAStringContainingOnlyLettersTest() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    "AFAKEBARCODE"
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid barcode checksum", e.getMessage());
        }
    }

    /**
     * This method tests whether a valid UPC-A barcode (with correct check digit) is added to
     * a product when it is created (i.e. no exception is thrown).
     * @throws IllegalProductArgumentException thrown if a product contains invalid data.
     */
    @Test
    void isProductCreatedWhenProductHasUPCABarcodeTest() throws IllegalProductArgumentException {
        String upcBarcode = "036000291452";
        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                upcBarcode
        );
        assertEquals(upcBarcode, product.getBarcode());
    }

    /**
     * This method tests whether a valid EAN-13 barcode (with correct check digit) is added to
     * a product when it is created (i.e. an exception is not thrown).
     * @throws IllegalProductArgumentException thrown if a product contains invalid data.
     */
    @Test
    void isProductCreatedWhenProductHasEANBarcodeTest() throws IllegalProductArgumentException {
        String eanBarcode = "9400547002634";
        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                eanBarcode
        );
        assertEquals(eanBarcode, product.getBarcode());
    }

    /**
     * This method tests whether an exception is thrown when a barcode that is too short is added to
     * a product. The minimum length of a barcode is 12 (for UPC-A).
     */
    @Test
    void isInvalidBarcodeExceptionThrownWhenBarcodeIsTooShortTest() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    "94005470026"
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid barcode checksum", e.getMessage());
        }
    }

    /**
     * This method tests whether an exception is thrown when a barcode that is too long is added to
     * a product. The maximum length of a barcode is 13 (for EAN-13).
     */
    @Test
    void isInvalidBarcodeExceptionThrownWhenBarcodeIsTooLongTest() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    "94005470026345"
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid barcode checksum", e.getMessage());
        }
    }

    /**
     * This method checks whether an exception is thrown when the check digit of a barcode is incorrect.
     * This method specifically tests for transposition. Transposition is when a user manually enters
     * a barcode and accidentally switches two numbers around i.e. the barcode should be 9400547002634
     * instead of 9400574002634 (they have switched the 7 and 4).
     */
    @Test
    void isInvalidBarcodeExceptionThrownWhenBarcodeHasIncorrectCheckDigitViaTranspositionTest() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    "9400574002634"
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid barcode checksum", e.getMessage());
        }
    }

    /**
     * This method checks whether an exception is thrown when the check digit of a barcode is incorrect.
     * This method specifically tests for twinning. Twinning is when a user manually enters
     * a barcode and accidentally duplicates two numbers i.e. the barcode should be 9400547002634
     * instead of 9400557002634 (they have duplicated 5).
     */
    @Test
    void isInvalidBarcodeExceptionThrownWhenBarcodeHasIncorrectCheckDigitViaTwinningTest() {
        try {
            Product product = new Product(
                    "PROD",
                    business,
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    "9400557002634"
            );
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid barcode checksum", e.getMessage());
        }
    }

    /* ------------------------------------------------- SetterTests -------------------------------------------------*/

    /**
     * This method tests whether an exception is thrown when trying to change a product name to something invalid
     * (e.g. an empty string when name is a required field).
     */
    @Test
    void isIllegalProductArgumentExceptionThrownWhenChangingProductNameToInvalidName() {
        try {
            setProduct.setName("");
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid product name", e.getMessage());
        }
    }

    /**
     * This method tests whether an exception is thrown when trying to change a product description to something invalid
     * (e.g. a string with a length greater than the max length).
     */
    @Test
    void isIllegalProductArgumentExceptionThrownWhenChangingProductDescriptionToInvalidDescription() {
        try {
            setProduct.setDescription("A".repeat(601));
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid product description", e.getMessage());
        }
    }

    /**
     * This method tests whether an exception is thrown when trying to change a product manufacturer to something invalid
     * (e.g. a string with a length greater than the max length).
     */
    @Test
    void isIllegalProductArgumentExceptionThrownWhenChangingProductManufacturerToInvalidManufacturer() {
        try {
            setProduct.setManufacturer("A".repeat(101));
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid manufacturer", e.getMessage());
        }
    }

    /**
     * This method tests whether an exception is thrown when trying to change a product RRP to something invalid
     * (e.g. a RRP which is negative).
     */
    @Test
    void isIllegalProductArgumentExceptionThrownWhenChangingProductRRPToInvalidRRP() {
        try {
            setProduct.setRecommendedRetailPrice(-1.0);
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid recommended retail price", e.getMessage());
        }
    }

    /**
     * This method tests whether an exception is thrown when trying to change a product barcode to something invalid
     * (e.g. a barcode which contains letters).
     */
    @Test
    void isIllegalProductArgumentExceptionThrownWhenChangingProductBarcodeToInvalidBarcode() {
        try {
            setProduct.setBarcode("INVALIDBARCODE");
            Assertions.fail("IllegalProductArgumentException was expected to be thrown.");
        } catch (IllegalProductArgumentException e) {
            assertEquals("Invalid barcode checksum", e.getMessage());
        }
    }

}
