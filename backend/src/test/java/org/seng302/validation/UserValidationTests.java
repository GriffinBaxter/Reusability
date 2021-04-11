package org.seng302.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class UserValidationTests {

    // ******************************** FIRST NAME ***********************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of first name
     * is less than the minimum length.
     */
    @Test
    public void isValidFirstNameLessThanMinLength() {
        String firstName = ""; //minLength = 2
        assertEquals(false, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of first name
     * is greater than the max length.
     */
    @Test
    public void isValidFirstNameGreaterThanMaxLength() {
        String string = "A";
        String firstName = string.repeat(25); //maxLength = 20
        assertEquals(false, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when first name
     * is of the right length but contains invalid symbols.
     */
    @Test
    public void isValidFirstNameInvalidSymbols() {
        String firstName = "Zac!@#";
        assertEquals(false, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when first name
     * is of the right length but contains numbers.
     */
    @Test
    public void isValidFirstNameContainsNumbers() {
        String firstName = "Zac123";
        assertEquals(false, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * is of the right length and contains valid symbols.
     */
    @Test
    public void isValidFirstNameValidSymbols() {
        String firstName = "Za-c'bd";
        assertEquals(true, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * is of the right length and contains a space.
     */
    @Test
    public void isValidFirstNameContainsSpace() {
        String firstName = "Za c";
        assertEquals(true, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * is of the right length.
     */
    @Test
    public void isValidFirstNameRightLength() {
        String firstName = "Zachary";
        assertEquals(true, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * has the same length as the min length.
     */
    @Test
    public void isValidFirstNameEqualMinLength() {
        String firstName = "Za"; // minLength = 2
        assertEquals(true, UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * has the same length as the max length.
     */
    @Test
    public void isValidFirstNameEqualMaxLength() {
        String string = "Z";
        String firstName = string.repeat(20); //maxLength = 20
        assertEquals(true, UserValidation.isValidFirstName(firstName));
    }

    // ******************************** MIDDLE NAME **********************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of middle name
     * is greater than the max length.
     */
    @Test
    public void isValidMiddleNameGreaterThanMaxLength() {
        String string = "F";
        String middleName = string.repeat(25); //maxLength = 20
        assertEquals(false, UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when middle name
     * is of the right length but contains invalid symbols.
     */
    @Test
    public void isValidMiddleNameInvalidSymbols() {
        String middleName = "Finlay!@#";
        assertEquals(false, UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when middle name
     * is of the right length but contains numbers.
     */
    @Test
    public void isValidMiddleNameContainsNumbers() {
        String middleName = "Finaly123";
        assertEquals(false, UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * is of the right length and contains valid symbols.
     */
    @Test
    public void isValidMiddleNameValidSymbols() {
        String middleName = "Za-c'bd";
        assertEquals(true, UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * is of the right length and contains a space.
     */
    @Test
    public void isValidMiddleNameContainsSpace() {
        String middleName = "Za c";
        assertEquals(true, UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * is of the right length.
     */
    @Test
    public void isValidMiddleNameRightLength() {
        String middleName = "Finlay";
        assertEquals(true, UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * has the same length as the min length.
     */
    @Test
    public void isValidMiddleNameEqualMinLength() {
        String middleName = ""; // minLength = 0
        assertEquals(true, UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * has the same length as the max length.
     */
    @Test
    public void isValidMiddleNameEqualMaxLength() {
        String string = "Z";
        String middleName = string.repeat(20); //maxLength = 20
        assertEquals(true, UserValidation.isValidMiddleName(middleName));
    }

    // ********************************* LAST NAME ***********************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of last name
     * is less than the minimum length.
     */
    @Test
    public void isValidLastNameLessThanMinLength() {
        String lastName = ""; //minLength = 2
        assertEquals(false, UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of last name
     * is greater than the max length.
     */
    @Test
    public void isValidLastNameGreaterThanMaxLength() {
        String string = "A";
        String lastName = string.repeat(25); //maxLength = 20
        assertEquals(false, UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when last name
     * is of the right length but contains invalid symbols.
     */
    @Test
    public void isValidLastNameInvalidSymbols() {
        String LastName = "Jones!@#";
        assertEquals(false, UserValidation.isValidLastName(LastName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when last name
     * is of the right length but contains numbers.
     */
    @Test
    public void isValidLastNameContainsNumbers() {
        String lastName = "Jones123";
        assertEquals(false, UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * is of the right length and contains valid symbols.
     */
    @Test
    public void isValidLastNameValidSymbols() {
        String lastName = "Za-c'bd";
        assertEquals(true, UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * is of the right length and contains a space.
     */
    @Test
    public void isValidLastNameContainsSpace() {
        String lastName = "Za c";
        assertEquals(true, UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * is of the right length.
     */
    @Test
    public void isValidLastNameRightLength() {
        String lastName = "Jones";
        assertEquals(true, UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * has the same length as the min length.
     */
    @Test
    public void isValidLastNameEqualMinLength() {
        String lastName = "Jo"; // minLength = 2
        assertEquals(true, UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * has the same length as the max length.
     */
    @Test
    public void isValidLastNameEqualMaxLength() {
        String string = "J"; // minLength = 2
        String lastName = string.repeat(20); //maxLength = 20
        assertEquals(true, UserValidation.isValidLastName(lastName));
    }

    // ********************************* NICKNAME ************************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of nickname
     * is greater than the max length.
     */
    @Test
    public void isValidNicknameGreaterThanMaxLength() {
        String string = "A";
        String nickname = string.repeat(25); //maxLength = 20
        assertEquals(false, UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when nickname
     * is of the right length but contains invalid symbols.
     */
    @Test
    public void isValidNicknameInvalidSymbols() {
        String nickname = "Zac!@#";
        assertEquals(false, UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when nickname
     * is of the right length but contains numbers.
     */
    @Test
    public void isValidNicknameContainsNumbers() {
        String nickname = "Zac123";
        assertEquals(false, UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * is of the right length and contains valid symbols.
     */
    @Test
    public void isValidNicknameValidSymbols() {
        String nickname = "Za-c'bd";
        assertEquals(true, UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * is of the right length and contains a space.
     */
    @Test
    public void isValidNicknameContainsSpace() {
        String nickname = "Za c";
        assertEquals(true, UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * is of the right length.
     */
    @Test
    public void isValidNicknameRightLength() {
        String nickname = "Peps";
        assertEquals(true, UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * has the same length as the min length.
     */
    @Test
    public void isValidNicknameEqualMinLength() {
        String nickname = ""; // minLength = 0
        assertEquals(true, UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * has the same length as the max length.
     */
    @Test
    public void isValidNicknameEqualMaxLength() {
        String string = "Z";
        String nickname = string.repeat(20); //maxLength = 20
        assertEquals(true, UserValidation.isValidNickname(nickname));
    }

    // *********************************** BIO ***************************************

    /**
     * Test to see whether true (i.e valid) is returned when bio
     * has the same length as the min length.
     */
    @Test
    public void isValidBioEqualMinLength() {
        String bio = ""; // minLength = 0
        assertEquals(true, UserValidation.isValidBio(bio));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when bio
     * has a length greater than the max length
     */
    @Test
    public void isValidBioGreaterThanMaxLength() {
        String string = "Z";
        String bio = string.repeat(601); //maxLength = 600
        assertEquals(false, UserValidation.isValidBio(bio));
    }

    /**
     * Test to see whether true (i.e valid) is returned when bio
     * has a length equal to the max length
     */
    @Test
    public void isValidBioEqualToMaxLength() {
        String string = "Z";
        String bio = string.repeat(600); //maxLength = 600
        assertEquals(true, UserValidation.isValidBio(bio));
    }

    /**
     * Test to see whether true (i.e valid) is returned when bio
     * has correct length and contains symbols, numbers etc.
     */
    @Test
    public void isValidBioCorrectLengthContainsSymbolsAndNumbers() {
        String bio = "Hello my name is Euan1234. My email is top@bga.com." +
                "Hello!!!! #$%&&**";
        assertEquals(true, UserValidation.isValidBio(bio));
    }

    // ********************************** EMAIL **************************************

    /**
     * Test to see whether false (i.e invalid) is returned when email
     * has length less than min length.
     */
    @Test
    public void isValidEmailLessThanMinLength() {
        String email = "z@"; // minLength = 3
        assertEquals(false, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when email
     * has length greater than max length.
     */
    @Test
    public void isValidEmailGreaterThanMaxLength() {
        String email = "z@abbbbbbbbbbbbbbbbbbbbbbbbbbb.co.nz"; // maxLength = 30
        assertEquals(false, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether true (i.e valid) is returned when email
     * has length equal to min length
     */
    @Test
    public void isValidEmailEqualToMinLength() {
        String email = "z@c.a"; // minLength = 3
        assertEquals(true, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether true (i.e valid) is returned when email
     * has length equal to max length
     */
    @Test
    public void isValidEmailEqualToMaxLength() {
        String email = "zzzzzzzzzzzz@ccccccccccc.co.nz"; // maxLength = 30
        assertEquals(true, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when email
     * does not contain @ symbol.
     */
    @Test
    public void isValidEmailNoAtSymbol() {
        String email = "zac.gmail.com";
        assertEquals(false, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether true (i.e valid) is returned when email
     * is of correct format.
     */
    @Test
    public void isValidEmailCorrectFormat() {
        String email = "zac@gmail.com";
        assertEquals(true, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether true (i.e valid) is returned when email
     * is of correct format and contains numbers.
     */
    @Test
    public void isValidEmailCorrectFormatContainsNumbers() {
        String email = "zac123@gmail.com";
        assertEquals(true, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when email
     * contains spaces.
     */
    @Test
    public void isValidEmailContainsSpaces() {
        String email = "zac 123@gmail.com";
        assertEquals(false, UserValidation.isValidEmail(email));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when email
     * contains invalid symbols.
     */
    @Test
    public void isValidEmailInvalidSymbols() {
        String email = "zac#***123@gmail.com";
        assertEquals(false, UserValidation.isValidEmail(email));
    }

    // ******************************* DATE OF BIRTH *********************************

    /**
     * Test to see whether false (i.e invalid) is returned when date of birth
     * means user is younger than the min age of 13.
     */
    @Test
    public void isValidDOBNotOldEnough() {
        LocalDate currentDate = LocalDate.now();
        assertEquals(false, UserValidation.isValidDOB(currentDate));
    }

    /**
     * Test to see whether true (i.e valid) is returned when date of birth
     * means user is older than the min age of 13.
     */
    @Test
    public void isValidDOBOldEnough() {
        LocalDate birthDate = LocalDate.of(2000, Month.JANUARY, 1);
        assertEquals(true, UserValidation.isValidDOB(birthDate));
    }

    /**
     * Test to see whether true (i.e valid) is returned when date of birth
     * means user is the min age of 13.
     */
    @Test
    public void isValidDOBEqualToMinAge() {
        LocalDate birthDate = LocalDate.now().minusYears(13); // minAge = 13
        assertEquals(true, UserValidation.isValidDOB(birthDate));
    }

    // ******************************* PHONE NUMBER **********************************

    /**
     * Test to see whether true (i.e valid) is returned when phone number
     * has no input.
     */
    @Test
    public void isValidPhoneNumberNoInput() {
        String phoneNumber = ""; // minLength = 0
        assertEquals(true, UserValidation.isValidPhoneNumber(phoneNumber));
    }

    // euqal max length
    // greater max length
    // invalid syntax
    // valid
}
