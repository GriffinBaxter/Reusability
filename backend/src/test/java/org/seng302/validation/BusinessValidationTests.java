package org.seng302.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

class BusinessValidationTests {

    private List<Business> businesses;
    private Business business;

    @BeforeEach
    void setup() throws Exception {
        businesses = new ArrayList<>();
        Address address = new Address (
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        User user = new User (
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
                LocalDateTime.now().minusMonths(1).minusDays(10),
                Role.USER
        );
        business = new Business (
                1,
                "Countdown",
                "Main competitor of New World.",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user
        );
    }
    // ******************************** BUSINESS NAME ************************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of business name
     * is less than the minimum length.
     */
    @Test
    void isValidNameLessThanMinLength() {
        String businessName = ""; //minLength = 1
        assertFalse(BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of business name
     * is greater than the max length.
     */
    @Test
    void isValidNameGreaterThanMaxLength() {
        String string = "A";
        String businessName = string.repeat(101); //maxLength = 100
        assertFalse(BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when business name
     * is of the correct length but contains invalid symbols.
     */
    @Test
    void isValidNameInvalidSymbols() {
        String businessName = "New %@!";
        assertFalse(BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * is of the correct length and contains valid symbols, spaces etc.
     */
    @Test
    void isValidNameValidSymbolsAndSpaces() {
        String businessName = "New '#,.&()-";
        assertTrue(BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * is of the correct length and contains numbers.
     */
    @Test
    void isValidNameContainsNumbers() {
        String businessName = "New123";
        assertTrue(BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * has the same length as the min length.
     */
    @Test
    void isValidNameEqualToMinLength() {
        String businessName = "3"; // minLength = 1
        assertTrue(BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * has the same length as the max length.
     */
    @Test
    void isValidNameEqualToMaxLength() {
        String string = "A"; // maxLength = 100
        String businessName = string.repeat(100);
        assertTrue(BusinessValidation.isValidName(businessName));
    }

    // ******************************** DESCRIPTION **************************************

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has the same length as the min length.
     */
    @Test
    void isValidDescriptionEqualMinLength() {
        String description = ""; // minLength = 0
        assertTrue(BusinessValidation.isValidDescription(description));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when description
     * has a length greater than the max length.
     */
    @Test
    void isValidDescriptionGreaterThanMaxLength() {
        String string = "Z";
        String description = string.repeat(601); //maxLength = 600
        assertFalse(BusinessValidation.isValidDescription(description));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has a length equal to the max length.
     */
    @Test
    void isValidDescriptionEqualToMaxLength() {
        String string = "Z";
        String description = string.repeat(600); //maxLength = 600
        assertTrue(BusinessValidation.isValidDescription(description));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has correct length and contains symbols, numbers etc.
     */
    @Test
    void isValidDescriptionCorrectLengthContainsSymbolsAndNumbers() {
        String description = "Hello welcome to New World12345!!!!!!!!. Contact us" +
                " at top@bga.com. Now %$&#$*#(#";
        assertTrue(BusinessValidation.isValidDescription(description));
    }


    // ******************************** NEW BUSINESS *************************************

    /**
     * Test to see whether true (i.e valid) is returned when business
     * doesn't exist.
     */
    @Test
    void isValidBusinessNotExist() {
        businesses.add(business);
        assertTrue(BusinessValidation.isNewBusiness(businesses, "New World"));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when business
     * already exists
     */
    @Test
    void isValidBusinessExists() {
        businesses.add(business);
        assertFalse(BusinessValidation.isNewBusiness(businesses, "Countdown"));
    }

    /**
     * Test to see whether true (i.e valid) is returned when there are no
     * businesses.
     */
    @Test
    void isValidBusinessNoBusinesses() {
        assertTrue(BusinessValidation.isNewBusiness(businesses, "Countdown"));
    }

}
