package org.seng302.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.*;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

/**
 * User test class.
 */
class UserTests {

    private static Address address;

    private static User user;

    private static Business business;

    private static Product product;

    private static InventoryItem inventoryItem;

    private static Listing listing;

    @BeforeAll
    static void before() throws Exception {
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
        listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
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
        Assertions.assertEquals(business.getAdministrators(), List.of(user, newUser));
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
     * Test to see whether a user is successfully created when the password contains all required fields, spaces,
     * and password length is within the accepted range.
     */
    @Test
    void isUserSuccessfullyCreatedWhenPasswordContainsRequiredFieldsAndCorrectLengthTest() throws IllegalUserArgumentException {
        String password = "123ASD!@#as  d";

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
        Assertions.assertNotNull(user);
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * the password of valid length but not containing an uppercase letter.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPasswordHasValidLengthButContainsNoUppercaseLetterTest() {
        String password  = "123!@#asd";

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
     * the password of valid length but not containing a number.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPasswordHasValidLengthButContainsNoNumberTest() {
        String password  = "ASD!@#asd";

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
     * the password of valid length but not containing a symbol.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPasswordHasValidLengthButContainsNoSymbolTest() {
        String password  = "ASD124asd";

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
     * the password of valid length but not containing a lower case letter.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPasswordHasValidLengthButContainsNoLowercaseLetterTest() {
        String password  = "ASD124#!";

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
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid password", e.getMessage());
        }
    }

    // ******************************** FIRST NAME ***********************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * first name (a required field) empty.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenFirstNameEmptyTest() {
        String firstName = "";

        try {
            User user = new User(
                    firstName,
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid first name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * first name greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenFirstNameLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String firstName = string.repeat(256); // max length = 255;

        try {
            User user = new User(
                    firstName,
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid first name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * first name valid, but first name contains invalid symbols.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenFirstNameHasValidLengthButContainsInvalidSymbolsTest() {
        String firstName = "Zac!@#";

        try {
            User user = new User(
                    firstName,
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid first name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * first name valid, but first name contains numbers.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenFirstNameHasValidLengthButContainsNumbersTest() {
        String firstName = "Zac123";

        try {
            User user = new User(
                    firstName,
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid first name", e.getMessage());
        }
    }

    /**
     * Test to see whether a user is successfully created when first name contains valid symbols, diacritics and spaces.
     */
    @Test
    void isUserSuccessfullyCreatedWhenFirstNameContainsValidSymbolsDiacriticsAndSpacesTest() throws IllegalUserArgumentException {
        String firstName = "Za-c'bd āâĕ";
        User user = new User(
                firstName,
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
            );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(firstName, user.getFirstName());
    }

    // ******************************* PHONE NUMBER **********************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * phone number greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPhoneNumberLengthGreaterThanMaxLengthTest() {
        String phoneNumber = "123 456 789 102 345"; // max length = 15

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    phoneNumber,
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid phone number", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with phone number
     * having invalid syntax.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenPhoneNumberHasInvalidSyntax() {
        String phoneNumber = "111-222-333%!@#";

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    phoneNumber,
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid phone number", e.getMessage());
        }
    }

    /**
     * Test to see whether a user is successfully created when phone number has valid syntax and length equals max
     * length.
     */
    @Test
    void isUserSuccessfullyCreatedWhenPhoneNumberHasValidSyntaxAndLengthEqualsMaxLengthTest() throws IllegalUserArgumentException {
        String phoneNumber = "+64 32 555 0129"; // maxLength = 15

        User user = new User(
                "first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                phoneNumber,
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(phoneNumber, user.getPhoneNumber());
    }

    // ******************************* DATE OF BIRTH *********************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user when date of birth
     * means user is younger than the min age of 13.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenUserIsYoungerThanThirteenTest() {
        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.now(),
                    "+64 32 555 0129",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid date of birth", e.getMessage());
        }
    }

    /**
     * Test to see whether a user is successfully created when date of birth means that user is older than the min age
     * of 13.
     */
    @Test
    void isUserSuccessfullyCreatedWhenUserIsOlderThanThirteenTest() throws IllegalUserArgumentException {
        LocalDate birthDate = LocalDate.of(2000, Month.JANUARY, 1);

        User user = new User(
                "first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                birthDate,
                "+64 32 555 0129",
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(birthDate, user.getDateOfBirth());
    }

    /**
     * Test to see whether a user is successfully created when date of birth means that user is the min age
     * of 13.
     */
    @Test
    void isUserSuccessfullyCreatedWhenUserAgeIsMinAgeOfThirteen() throws IllegalUserArgumentException {
        LocalDate birthDate = LocalDate.now().minusYears(13); // min age = 13

        User user = new User(
                "first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                birthDate,
                "+64 32 555 0129",
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(birthDate, user.getDateOfBirth());
    }

    // ********************************* LAST NAME ***********************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with
     * last name (a required field) empty.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenLastNameEmptyTest() {
        String lastName = "";

        try {
            User user = new User(
                    "first",
                    lastName,
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid last name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * last name greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenLastNameLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String lastName = string.repeat(256); // max length = 255;

        try {
            User user = new User(
                    "first",
                    lastName,
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid last name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * last name valid, but last name contains invalid symbols.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenLastNameHasValidLengthButContainsInvalidSymbolsTest() {
        String lastName = "Kay!@#";

        try {
            User user = new User(
                    "first",
                    lastName,
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid last name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * last name valid, but last name contains numbers.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenLastNameHasValidLengthButContainsNumbersTest() {
        String lastName = "Kay123";

        try {
            User user = new User(
                    "first",
                    lastName,
                    "middle",
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid last name", e.getMessage());
        }
    }

    /**
     * Test to see whether a user is successfully created when last name contains valid symbols, diacritics and spaces.
     */
    @Test
    void isUserSuccessfullyCreatedWhenLastNameContainsValidSymbolsDiacriticsAndSpacesTest() throws IllegalUserArgumentException {
        String lastName = "Ka-y'bd āâĕ";
        User user = new User(
                "first",
                lastName,
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(lastName, user.getLastName());
    }

    // *********************************** BIO ***************************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * bio greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenBioLengthGreaterThanMaxLengthTest() {
        String string = "Z";
        String bio = string.repeat(601); // max length = 600

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    "nick",
                    bio,
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid bio", e.getMessage());
        }
    }

    /**
     * Test to see whether a user is successfully created when bio has valid length and contains symbols, numbers etc.
     */
    @Test
    void isUserSuccessfullyCreatedWhenBioContainsSymbolsAndNumbersTest() throws IllegalUserArgumentException {
        String bio = "Hello my name is Euan1234. My email is top@bga.com." +
                "Hello!!!! #$%&&**";
        User user = new User(
                "first",
                "last",
                "middle",
                "nick",
                bio,
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(bio, user.getBio());
    }

    // ******************************** MIDDLE NAME **********************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * middle name greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenMiddleNameLengthGreaterThanMaxLengthTest() {
        String string = "F";
        String middleName = string.repeat(256); // max length = 255;

        try {
            User user = new User(
                    "first",
                    "last",
                    middleName,
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid middle name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * middle name valid, but middle name contains invalid symbols.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenMiddleNameHasValidLengthButContainsInvalidSymbolsTest() {
        String middleName = "Cam!@#";

        try {
            User user = new User(
                    "first",
                    "last",
                    middleName,
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid middle name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * middle name valid, but middle name contains numbers.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenMiddleNameHasValidLengthButContainsNumbersTest() {
        String middleName = "Cam123";

        try {
            User user = new User(
                    "first",
                    "last",
                    middleName,
                    "nick",
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid middle name", e.getMessage());
        }
    }

    /**
     * Test to see whether a user is successfully created when middle name contains valid symbols, diacritics and spaces.
     */
    @Test
    void isUserSuccessfullyCreatedWhenMiddleNameContainsValidSymbolsDiacriticsAndSpacesTest() throws IllegalUserArgumentException {
        String middleName = "Ca-m'bd āâĕ";
        User user = new User(
                "first",
                "last",
                middleName,
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(middleName, user.getMiddleName());
    }

    // ********************************* NICKNAME ************************************

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * nick name greater than the max length.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenNickNameLengthGreaterThanMaxLengthTest() {
        String string = "P";
        String nickName = string.repeat(256); // max length = 255;

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    nickName,
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid nickname", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * nick name valid, but nick name contains invalid symbols.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenNickNameHasValidLengthButContainsInvalidSymbolsTest() {
        String nickName = "Peps!@#";

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    nickName,
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid nickname", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalUserArgumentException is thrown when trying to create a user with the length of
     * nick name valid, but nick name contains numbers.
     */
    @Test
    void isIllegalUserArgumentExceptionThrownWhenNickNameHasValidLengthButContainsNumbersTest() {
        String nickName = "Peps123";

        try {
            User user = new User(
                    "first",
                    "last",
                    "middle",
                    nickName,
                    "bio",
                    "test@example.com",
                    LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                    "123456789",
                    address,
                    "Qwerty123!",
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    Role.USER
            );
        } catch (IllegalUserArgumentException e) {
            Assertions.assertEquals("Invalid nickname", e.getMessage());
        }
    }

    /**
     * Test to see whether a user is successfully created when nick name contains valid symbols, diacritics and spaces.
     */
    @Test
    void isUserSuccessfullyCreatedWhenNickNameContainsValidSymbolsDiacriticsAndSpacesTest() throws IllegalUserArgumentException {
        String nickName = "Pep-s'bd āâĕ";
        User user = new User(
                "first",
                "last",
                "middle",
                nickName,
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNotNull(user);
        Assertions.assertEquals(nickName, user.getNickname());
    }

    // ******************************* ************ **********************************

    /**
     * Tests that the optional fields (middle name, nickname, bio and phone number) are set to null when empty.
     */
    @Test
    void TestOptionalFields() throws IllegalUserArgumentException {
        User user = new User(
                "first",
                "last",
                "",
                "",
                "",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        Assertions.assertNull(user.getNickname());
        Assertions.assertNull(user.getBio());
        Assertions.assertNull(user.getMiddleName());
        Assertions.assertNull(user.getPhoneNumber());
    }

    /**
     * Checks to see if the message only contains months when
     * the user has been registered for less than a year.
     */
    @Test
    void testMonthsSinceRegistration() throws IllegalUserArgumentException {
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
        Assertions.assertEquals("2 Month(s)\n", user.getTimeSinceRegistration());
    }

    /**
     * Checks to see if the message contains both years and months when
     * the user has been registered for more than a year.
     */
    @Test
    void testYearsSinceRegistration() throws IllegalUserArgumentException {
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
        Assertions.assertEquals("1 Year(s) and 2 Month(s)\n", user.getTimeSinceRegistration());
    }

    /**
     * Checks to see if the months are rounded down to the nearest month.
     */
    @Test
    void testMonthsRounded() throws IllegalUserArgumentException {
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
        Assertions.assertEquals("1 Month(s)\n", user.getTimeSinceRegistration());
    }

    /**
     * Checks to see the password has been hashed when create new User object
     */
    @Test
    void testEncode() throws IllegalUserArgumentException {
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
        Assertions.assertEquals("UGFzc3dvcmQxMjMh", user.getPassword());
    }

    /**
     * Checks to see the password has been hashed when create new User object
     */
    @Test
    void testVerifyPassword() throws IllegalUserArgumentException {
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
        Assertions.assertTrue(user.verifyPassword("Password123!"));
    }

    /**
     * Test to see whether the getBusinessesAdministered method returns a list of
     * ids' (integers) of the businesses administered by that user.
     * @throws IllegalBusinessArgumentException validation exception for a business.
     */
    @Test
    void testGetBusinessesAdministered() throws IllegalBusinessArgumentException {
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

        Assertions.assertEquals(user, business.getAdministrators().get(0));
    }

    /**
     * Test to see whether the addAListingToBookMark function can successfully add new listing to bookmark
     */
    @Test
    void testANewListingSuccessfullyBeenAddToBookMark() {
        // Given
        Assertions.assertTrue(user.getBookMarkedListing().isEmpty());

        // When
        user.addAListingToBookMark(listing);

        // Then
        Assertions.assertEquals(1, user.getBookMarkedListing().size());
        Assertions.assertEquals(listing, user.getBookMarkedListing().get(0));
    }

    /**
     * Test to see whether the addAListingToBookMark function will not add exist listing again to bookmark
     */
    @Test
    void testAExistListingNotBeenAddToBookMarkAgain() {
        // Given
        user.addAListingToBookMark(listing);
        Assertions.assertEquals(1, user.getBookMarkedListing().size());
        Assertions.assertEquals(listing, user.getBookMarkedListing().get(0));

        // When
        user.addAListingToBookMark(listing);

        // Then
        Assertions.assertEquals(1, user.getBookMarkedListing().size());
    }

    /**
     * Test to see whether the removeAListingToBookMark function will successfully remove given listing if its exist.
     */
    @Test
    void testAExistListingSuccessfullyBeenRemoveFromBookMark() {
        // Given
        user.addAListingToBookMark(listing);
        Assertions.assertEquals(1, user.getBookMarkedListing().size());
        Assertions.assertEquals(listing, user.getBookMarkedListing().get(0));

        // When
        user.removeAListingToBookMark(listing);

        // Then
        Assertions.assertTrue(user.getBookMarkedListing().isEmpty());
    }

}
