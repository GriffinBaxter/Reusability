package org.seng302.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessType;
import org.seng302.user.Role;
import org.seng302.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BusinessValidationTests {

    private List<Business> businesses;
    private Business business;

    @BeforeEach
    public void setup() throws Exception {
        businesses = new ArrayList<>();
        Address address = new Address (
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
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
    public void isValidNameLessThanMinLength() {
        String businessName = ""; //minLength = 1
        assertEquals(false, BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of business name
     * is greater than the max length.
     */
    @Test
    public void isValidNameGreaterThanMaxLength() {
        String string = "A";
        String businessName = string.repeat(101); //maxLength = 100
        assertEquals(false, BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when business name
     * is of the correct length but contains invalid symbols.
     */
    @Test
    public void isValidNameInvalidSymbols() {
        String businessName = "New %@!";
        assertEquals(false, BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * is of the correct length and contains valid symbols, spaces etc.
     */
    @Test
    public void isValidNameValidSymbolsAndSpaces() {
        String businessName = "New '#,.&()-";
        assertEquals(true, BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * is of the correct length and contains numbers.
     */
    @Test
    public void isValidNameContainsNumbers() {
        String businessName = "New123";
        assertEquals(true, BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * has the same length as the min length.
     */
    @Test
    public void isValidNameEqualToMinLength() {
        String businessName = "3"; // minLength = 1
        assertEquals(true, BusinessValidation.isValidName(businessName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when business name
     * has the same length as the max length.
     */
    @Test
    public void isValidNameEqualToMaxLength() {
        String string = "A"; // maxLength = 100
        String businessName = string.repeat(100);
        assertEquals(true, BusinessValidation.isValidName(businessName));
    }

    // ******************************** DESCRIPTION **************************************

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has the same length as the min length.
     */
    @Test
    public void isValidDescriptionEqualMinLength() {
        String description = ""; // minLength = 0
        assertEquals(true, BusinessValidation.isValidDescription(description));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when description
     * has a length greater than the max length.
     */
    @Test
    public void isValidDescriptionGreaterThanMaxLength() {
        String string = "Z";
        String description = string.repeat(601); //maxLength = 600
        assertEquals(false, BusinessValidation.isValidDescription(description));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has a length equal to the max length.
     */
    @Test
    public void isValidDescriptionEqualToMaxLength() {
        String string = "Z";
        String description = string.repeat(600); //maxLength = 600
        assertEquals(true, BusinessValidation.isValidDescription(description));
    }

    /**
     * Test to see whether true (i.e valid) is returned when description
     * has correct length and contains symbols, numbers etc.
     */
    @Test
    public void isValidDescriptionCorrectLengthContainsSymbolsAndNumbers() {
        String description = "Hello welcome to New World12345!!!!!!!!. Contact us" +
                " at top@bga.com. Now %$&#$*#(#";
        assertEquals(true, BusinessValidation.isValidDescription(description));
    }


    // ******************************** NEW BUSINESS *************************************

    /**
     * Test to see whether true (i.e valid) is returned when business
     * doesn't exist.
     */
    @Test
    public void isValidBusinessNotExist() throws Exception {
        businesses.add(business);
        assertEquals(true, BusinessValidation.isNewBusiness(businesses, "New World"));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when business
     * already exists
     */
    @Test
    public void isValidBusinessExists() throws Exception {
        businesses.add(business);
        assertEquals(false, BusinessValidation.isNewBusiness(businesses, "Countdown"));
    }

    /**
     * Test to see whether true (i.e valid) is returned when there are no
     * businesses.
     */
    @Test
    public void isValidBusinessNoBusinesses() throws Exception {
        assertEquals(true, BusinessValidation.isNewBusiness(businesses, "Countdown"));
    }

}
