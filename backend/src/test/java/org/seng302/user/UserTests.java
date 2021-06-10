package org.seng302.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalBusinessArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * User test class.
 */
class UserTests {

    private static Address address;

    private static User user;

    @BeforeAll
    static void before() throws IllegalAddressArgumentException, IllegalUserArgumentException {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
    }

    /**
     * Tests that an administrator can be added to a business.
     * @throws IllegalBusinessArgumentException validation exception
     * @throws IllegalUserArgumentException validation exception
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
                user
        );
        User newUser = new User("NEWUSER",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "newUser@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0999999",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        business.addAdministrators(newUser);
        assertEquals(business.getAdministrators(), List.of(user, newUser));
    }


    // ********************************** EMAIL **************************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the length of email address less than the min length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenEmailAddressLengthLessThanMinLengthTest() {
        String email = "z@c"; // min length = 5

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    email,
                    LocalDate.of(2021, Month.JANUARY, 1),
                    "123456789",
                    address,
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid email address", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the length of email address greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenEmailAddressLengthGreaterThanMaxLengthTest() {
        String email = "z@abbbbbbbbbbbbbbbbbbbbbbbbbbb.co.nz"; // max length = 30

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    email,
                    LocalDate.of(2021, Month.JANUARY, 1),
                    "123456789",
                    address,
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid email address", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the email address not containing the @ symbol.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenEmailAddressDoesNotContainAtSymbolTest() {
        String email = "zac.gmail.com";

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    email,
                    LocalDate.of(2021, Month.JANUARY, 1),
                    "123456789",
                    address,
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid email address", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the email address containing spaces
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenEmailAddressContainsSpacesTest() {
        String email = "zac 123@gmail.com";

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    email,
                    LocalDate.of(2021, Month.JANUARY, 1),
                    "123456789",
                    address,
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid email address", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the email address containing invalid symbols.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenEmailAddressContainsInvalidSymbolsTest() {
        String email = "zac#***123@gmail.com";

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    email,
                    LocalDate.of(2021, Month.JANUARY, 1),
                    "123456789",
                    address,
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid email address", e.getMessage());
        }
    }

    // ********************************* PASSWORD ************************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the length of password less than the min length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPasswordLengthLessThanMinLengthTest() {
        String password = "1234567"; // min length = 8

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    password,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid password", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the length of password greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPasswordLengthGreaterThanMaxLengthTest() {
        String string = "1234567";
        String password = string.repeat(5); // maxLength = 30

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    password,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid password", e.getMessage());
        }
    }

    /**
     * Tests that an invalid password length < 8 throws an error.
     */
    @Test
    void TestInvalidPasswordLength() {
        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (Exception e) {
            assertEquals("Invalid password", e.getMessage());
        }
    }

    /**
     * Tests that an invalid password with space throws an error.
     */
    @Test
    void TestInvalidPasswordSpace() {
        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "   ",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (Exception e) {
            assertEquals("Invalid password", e.getMessage());
        }
    }

    /**
     * Tests that an invalid first name throws an error.
     */
    @Test
    void TestInvalidFirstName() {
        try {
            User user = new User(
                    "",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1),
                    "123456789",
                    address,
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (Exception e) {
            assertEquals("Invalid first name", e.getMessage());
        }
    }

    /**
     * Tests that an invalid phone number throws an error.
     */
    @Test
    void TestInvalidPhoneNumber() {
        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789abc",
                    address,
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (Exception e) {
            assertEquals("Invalid phone number", e.getMessage());
        }
    }

    /**
     * Tests that the optional fields (nickname and bio) are set to null when empty.
     */
    @Test
    void TestOptionalFields() throws Exception {
        User user = new User(
                "first",
                "last",
                "middle",
                "",
                "",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        assertNull(user.getNickname());
        assertNull(user.getBio());
    }

    /**
     * Checks to see if the message only contains months when
     * the user has been registered for less than a year.
     */
    @Test
    void testMonthsSinceRegistration() throws Exception {
        User user = new User(
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
                LocalDateTime.now().minusMonths(2),
                Role.USER
        );
        assertEquals("2 Month(s)\n", user.getTimeSinceRegistration());
    }

    /**
     * Checks to see if the message contains both years and months when
     * the user has been registered for more than a year.
     */
    @Test
    void testYearsSinceRegistration() throws Exception {
        User user = new User(
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
                LocalDateTime.now().minusYears(1).minusMonths(2),
                Role.USER
        );
        assertEquals("1 Year(s) and 2 Month(s)\n", user.getTimeSinceRegistration());
    }

    /**
     * Checks to see if the months are rounded down to the nearest month.
     */
    @Test
    void testMonthsRounded() throws Exception {
        User user = new User(
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
        assertEquals("1 Month(s)\n", user.getTimeSinceRegistration());
    }

    /**
     * Checks to see the password has been hashed when create new User object
     */
    @Test
    void testEncode() throws Exception{
        User user = new User(
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
        assertEquals("UGFzc3dvcmQxMjMh", user.getPassword());
    }

    /**
     * Checks to see the password has been hashed when create new User object
     */
    @Test
    void testVerifyPassword() throws Exception{
        User user = new User(
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
        assertTrue(user.verifyPassword("Password123!"));
    }

    /**
     * Test to see whether the getBusinessesAdministered method returns a list of
     * ids' (integers) of the businesses administered by that user.
     * @throws Exception
     */
    @Test
    void testGetBusinessesAdministered() throws Exception {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user
        );

        business.addAdministrators(user);

        assertEquals(user, business.getAdministrators().get(0));
    }


}
