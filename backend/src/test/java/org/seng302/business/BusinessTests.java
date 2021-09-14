package org.seng302.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalBusinessArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.Validation;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * BusinessAccount test class
 */
class BusinessTests {

    private User user;
    private Address address;
    private Business business;
    private List<Business> businesses;
    private Business newBusiness;

    @BeforeEach
    void setup() throws Exception {
        address = new Address (
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User (
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
                user,
                "$",
                "NZD"
        );
        businesses = new ArrayList<>();
    }


    // ******************************** BUSINESS NAME ************************************

    /**
     * Test to see whether an IllegalBusinessArgumentException is thrown when trying to create a business with
     * name as an empty field (check for min length).
     */
    @Test
    void isIllegalBusinessArgumentExceptionThrownWhenNameIsEmptyTest() {
        try {
            newBusiness = new Business(
                    1,
                    "",
                    "Main competitor of New World.",
                    address,
                    BusinessType.RETAIL_TRADE,
                    LocalDateTime.of(LocalDate.of(2021, 2, 2),
                            LocalTime.of(0, 0)),
                    user,
                    "$",
                    "NZD"
            );
        } catch (IllegalBusinessArgumentException e) {
            Assertions.assertEquals("Invalid business name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalBusinessArgumentException is thrown when trying to create a business with
     * the length of name greater than the max length.
     */
    @Test
    void isIllegalBusinessArgumentExceptionThrownWhenNameLengthGreaterThanMaxLengthTest() {
        try {
            String string = "A";
            String name = string.repeat(101); //maxLength = 100

            newBusiness = new Business(
                    1,
                    name,
                    "Main competitor of New World.",
                    address,
                    BusinessType.RETAIL_TRADE,
                    LocalDateTime.of(LocalDate.of(2021, 2, 2),
                            LocalTime.of(0, 0)),
                    user,
                    "$",
                    "NZD"
            );
        } catch (IllegalBusinessArgumentException e) {
            Assertions.assertEquals("Invalid business name", e.getMessage());
        }
    }

    /**
     * Test to see whether a business is successfully created when the business name contains symbols, numbers, and
     * diacritics.
     */
    @Test
    void businessIsSuccessfullyCreatedWhenBusinessNameContainsSymbolsNumbersAndDiacriticsTest() throws IllegalBusinessArgumentException {
        newBusiness = new Business(
                1,
                "Business123 ÖØǰǴ '#,.&()-",
                "Main competitor of New World.",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        Assertions.assertNotNull(newBusiness);
        Assertions.assertEquals("Business123 ÖØǰǴ '#,.&()-", newBusiness.getName());
    }

    /**
     * Test to see whether an IllegalBusinessArgumentException is thrown when trying to create a business with
     * the length of description greater than the max length.
     */
    @Test
    void isIllegalBusinessArgumentExceptionThrownWhenDescriptionLengthGreaterThanMaxLengthTest() {
        try {
            String string = "A";
            String description = string.repeat(601); //maxLength = 600

            newBusiness = new Business(
                    1,
                    "Countdown",
                    description,
                    address,
                    BusinessType.RETAIL_TRADE,
                    LocalDateTime.of(LocalDate.of(2021, 2, 2),
                            LocalTime.of(0, 0)),
                    user,
                    "$",
                    "NZD"
            );
        } catch (IllegalBusinessArgumentException e) {
            Assertions.assertEquals("Invalid business description", e.getMessage());
        }
    }

    /**
     * Test to see whether exception is thrown when supplied address is null (does not exist).
     */
    @Test
    void TestInvalidAddress() {
        try {
            Business businessAccount = new Business(
                    user.getId(),
                    "name",
                    "some text",
                    null,
                    BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                    LocalDateTime.now(),
                    user,
                    "$",
                    "NZD"
            );
        } catch (Exception e) {
            Assertions.assertEquals("Invalid address", e.getMessage());
        }
    }

    @Test
    void TestOptionalFields() throws Exception {
        Business businessAccount = new Business(
                user.getId(),
                "name",
                "",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user,
                "$",
                "NZD"
        );
        Assertions.assertNull(businessAccount.getDescription());
    }

    /**
     * Test to see whether the list of administrators for a business are updated as well as the list of businesses
     * administered by a user are updated when a user becomes a new administrator for that business.
     *
     * @throws IllegalBusinessArgumentException validation exception for a business.
     * @throws IllegalUserArgumentException validation exception for a user.
     */
    @Test
    void testAddAdministrators() throws IllegalBusinessArgumentException, IllegalUserArgumentException {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, 1, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        business.addAdministrators(user);
        Assertions.assertEquals(business, user.getBusinessesAdministeredObjects().get(0));
    }

    /**
     * Test to see whether the list of administrators for a business are updated as well as the list of businesses
     * administered by a user are updated when a user is removed as an administrator for that business.
     *
     * @throws IllegalBusinessArgumentException validation exception for a business.
     * @throws IllegalUserArgumentException validation exception for a user.
     */
    @Test
    void testRemoveAdministrators() throws IllegalUserArgumentException, IllegalBusinessArgumentException {
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );

        business.removeAdministrators(user);

        assertThat(business.getAdministrators().isEmpty()).isTrue();
        assertThat(user.getBusinessesAdministeredObjects().isEmpty()).isTrue();
    }

    /**
     * Test to see when a user is one of administrator, the function return true
     * @throws Exception business or user creat fail
     */
    @Test
    void testAnUserIsAdministrator() throws Exception {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        business.addAdministrators(user);
        Assertions.assertTrue(business.isAnAdministratorOfThisBusiness(user));
    }

    /**
     * Test to see when a user is not one of administrator, the function return false
     * @throws Exception business or user creat fail
     */
    @Test
    void testAnUserIsNotAdministrator() throws Exception {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        Assertions.assertFalse(business.isAnAdministratorOfThisBusiness(user));
    }

    // ******************************** NEW BUSINESS *************************************

    /**
     * Test to see whether true (i.e valid) is returned when business
     * doesn't exist.
     */
    @Test
    void isValidBusinessNotExist() {
        businesses.add(business);
        Assertions.assertTrue(Validation.isNewBusiness(businesses, "New World"));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when business
     * already exists
     */
    @Test
    void isValidBusinessExists() {
        businesses.add(business);
        Assertions.assertFalse(Validation.isNewBusiness(businesses, "Countdown"));
    }

    /**
     * Test to see whether true (i.e valid) is returned when there are no
     * businesses.
     */
    @Test
    void isValidBusinessNoBusinesses() {
        Assertions.assertTrue(Validation.isNewBusiness(businesses, "Countdown"));
    }
}
