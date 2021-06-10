package org.seng302.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

class UserValidationTests {

    // ******************************** FIRST NAME ***********************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of first name
     * is less than the minimum length.
     */
    @Test
    void isValidFirstNameLessThanMinLength() {
        String firstName = ""; //minLength = 2
        assertFalse(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of first name
     * is greater than the max length.
     */
    @Test
    void isValidFirstNameGreaterThanMaxLength() {
        String string = "A";
        String firstName = string.repeat(260); //maxLength = 255
        assertFalse(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when first name
     * is of the right length but contains invalid symbols.
     */
    @Test
    void isValidFirstNameInvalidSymbols() {
        String firstName = "Zac!@#";
        assertFalse(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when first name
     * is of the right length but contains numbers.
     */
    @Test
    void isValidFirstNameContainsNumbers() {
        String firstName = "Zac123";
        assertFalse(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * is of the right length and contains valid symbols.
     */
    @Test
    void isValidFirstNameValidSymbols() {
        String firstName = "Za-c'bd";
        assertTrue(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * is of the right length and contains a space.
     */
    @Test
    void isValidFirstNameContainsSpace() {
        String firstName = "Za c";
        assertTrue(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * is of the correct length.
     */
    @Test
    void isValidFirstNameCorrectLength() {
        String firstName = "Zachary";
        assertTrue(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * has the same length as the min length.
     */
    @Test
    void isValidFirstNameEqualMinLength() {
        String firstName = "Za"; // minLength = 2
        assertTrue(UserValidation.isValidFirstName(firstName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when first name
     * has the same length as the max length.
     */
    @Test
    void isValidFirstNameEqualMaxLength() {
        String string = "Z";
        String firstName = string.repeat(255); //maxLength = 255
        assertTrue(UserValidation.isValidFirstName(firstName));
    }

    // ******************************** MIDDLE NAME **********************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of middle name
     * is greater than the max length.
     */
    @Test
    void isValidMiddleNameGreaterThanMaxLength() {
        String string = "F";
        String middleName = string.repeat(260); //maxLength = 255
        assertFalse(UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when middle name
     * is of the right length but contains invalid symbols.
     */
    @Test
    void isValidMiddleNameInvalidSymbols() {
        String middleName = "Finlay!@#";
        assertFalse(UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when middle name
     * is of the right length but contains numbers.
     */
    @Test
    void isValidMiddleNameContainsNumbers() {
        String middleName = "Finaly123";
        assertFalse(UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * is of the right length and contains valid symbols.
     */
    @Test
    void isValidMiddleNameValidSymbols() {
        String middleName = "Za-c'bd";
        assertTrue(UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * is of the right length and contains a space.
     */
    @Test
    void isValidMiddleNameContainsSpace() {
        String middleName = "Za c";
        assertTrue(UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * is of the correct length.
     */
    @Test
    void isValidMiddleNameCorrectLength() {
        String middleName = "Finlay";
        assertTrue(UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * has the same length as the min length.
     */
    @Test
    void isValidMiddleNameEqualMinLength() {
        String middleName = ""; // minLength = 0
        assertTrue(UserValidation.isValidMiddleName(middleName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when middle name
     * has the same length as the max length.
     */
    @Test
    void isValidMiddleNameEqualMaxLength() {
        String string = "Z";
        String middleName = string.repeat(255); //maxLength = 255
        assertTrue(UserValidation.isValidMiddleName(middleName));
    }

    // ********************************* LAST NAME ***********************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of last name
     * is less than the minimum length.
     */
    @Test
    void isValidLastNameLessThanMinLength() {
        String lastName = ""; //minLength = 2
        assertFalse(UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when the length of last name
     * is greater than the max length.
     */
    @Test
    void isValidLastNameGreaterThanMaxLength() {
        String string = "A";
        String lastName = string.repeat(260); //maxLength = 255
        assertFalse(UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when last name
     * is of the right length but contains invalid symbols.
     */
    @Test
    void isValidLastNameInvalidSymbols() {
        String LastName = "Jones!@#";
        assertFalse(UserValidation.isValidLastName(LastName));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when last name
     * is of the right length but contains numbers.
     */
    @Test
    void isValidLastNameContainsNumbers() {
        String lastName = "Jones123";
        assertFalse(UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * is of the right length and contains valid symbols.
     */
    @Test
    void isValidLastNameValidSymbols() {
        String lastName = "Za-c'bd";
        assertTrue(UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * is of the right length and contains a space.
     */
    @Test
    void isValidLastNameContainsSpace() {
        String lastName = "Za c";
        assertTrue(UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * is of the correct length.
     */
    @Test
    void isValidLastNameCorrectLength() {
        String lastName = "Jones";
        assertTrue(UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * has the same length as the min length.
     */
    @Test
    void isValidLastNameEqualMinLength() {
        String lastName = "Jo"; // minLength = 2
        assertTrue(UserValidation.isValidLastName(lastName));
    }

    /**
     * Test to see whether true (i.e valid) is returned when last name
     * has the same length as the max length.
     */
    @Test
    void isValidLastNameEqualMaxLength() {
        String string = "J";
        String lastName = string.repeat(255); //maxLength = 255
        assertTrue(UserValidation.isValidLastName(lastName));
    }

    // ********************************* NICKNAME ************************************

    /**
     * Test to see whether false (i.e invalid) is returned when the length of nickname
     * is greater than the max length.
     */
    @Test
    void isValidNicknameGreaterThanMaxLength() {
        String string = "A";
        String nickname = string.repeat(260); //maxLength = 255
        assertFalse(UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when nickname
     * is of the right length but contains invalid symbols.
     */
    @Test
    void isValidNicknameInvalidSymbols() {
        String nickname = "Zac!@#";
        assertFalse(UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when nickname
     * is of the right length but contains numbers.
     */
    @Test
    void isValidNicknameContainsNumbers() {
        String nickname = "Zac123";
        assertFalse(UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * is of the right length and contains valid symbols.
     */
    @Test
    void isValidNicknameValidSymbols() {
        String nickname = "Za-c'bd";
        assertTrue(UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * is of the right length and contains a space.
     */
    @Test
    void isValidNicknameContainsSpace() {
        String nickname = "Za c";
        assertTrue(UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * is of the correct length.
     */
    @Test
    void isValidNicknameCorrectLength() {
        String nickname = "Peps";
        assertTrue(UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * has the same length as the min length.
     */
    @Test
    void isValidNicknameEqualMinLength() {
        String nickname = ""; // minLength = 0
        assertTrue(UserValidation.isValidNickname(nickname));
    }

    /**
     * Test to see whether true (i.e valid) is returned when nickname
     * has the same length as the max length.
     */
    @Test
    void isValidNicknameEqualMaxLength() {
        String string = "Z";
        String nickname = string.repeat(255); //maxLength = 255
        assertTrue(UserValidation.isValidNickname(nickname));
    }

    // *********************************** BIO ***************************************

    /**
     * Test to see whether true (i.e valid) is returned when bio
     * has the same length as the min length.
     */
    @Test
    void isValidBioEqualMinLength() {
        String bio = ""; // minLength = 0
        assertTrue(UserValidation.isValidBio(bio));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when bio
     * has a length greater than the max length
     */
    @Test
    void isValidBioGreaterThanMaxLength() {
        String string = "Z";
        String bio = string.repeat(601); //maxLength = 600
        assertFalse(UserValidation.isValidBio(bio));
    }

    /**
     * Test to see whether true (i.e valid) is returned when bio
     * has a length equal to the max length
     */
    @Test
    void isValidBioEqualToMaxLength() {
        String string = "Z";
        String bio = string.repeat(600); //maxLength = 600
        assertTrue(UserValidation.isValidBio(bio));
    }

    /**
     * Test to see whether true (i.e valid) is returned when bio
     * has correct length and contains symbols, numbers etc.
     */
    @Test
    void isValidBioCorrectLengthContainsSymbolsAndNumbers() {
        String bio = "Hello my name is Euan1234. My email is top@bga.com." +
                "Hello!!!! #$%&&**";
        assertTrue(UserValidation.isValidBio(bio));
    }


    // ******************************* DATE OF BIRTH *********************************

    /**
     * Test to see whether false (i.e invalid) is returned when date of birth
     * means user is younger than the min age of 13.
     */
    @Test
    void isValidDOBNotOldEnough() {
        LocalDate currentDate = LocalDate.now();
        assertFalse(UserValidation.isValidDOB(currentDate));
    }

    /**
     * Test to see whether true (i.e valid) is returned when date of birth
     * means user is older than the min age of 13.
     */
    @Test
    void isValidDOBOldEnough() {
        LocalDate birthDate = LocalDate.of(2000, Month.JANUARY, 1);
        assertTrue(UserValidation.isValidDOB(birthDate));
    }

    /**
     * Test to see whether true (i.e valid) is returned when date of birth
     * means user is the min age of 13.
     */
    @Test
    void isValidDOBEqualToMinAge() {
        LocalDate birthDate = LocalDate.now().minusYears(13); // minAge = 13
        assertTrue(UserValidation.isValidDOB(birthDate));
    }

    // ******************************* PHONE NUMBER **********************************

    /**
     * Test to see whether true (i.e valid) is returned when phone number
     * has no input.
     */
    @Test
    void isValidPhoneNumberNoInput() {
        String phoneNumber = ""; // minLength = 0
        assertTrue(UserValidation.isValidPhoneNumber(phoneNumber));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when phone number
     * has a length greater than max length.
     */
    @Test
    void isValidPhoneNumberGreaterThanMaxLength() {
        String phoneNumber = "123 456 789 102 345"; // maxLength = 15
        assertFalse(UserValidation.isValidPhoneNumber(phoneNumber));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when phone number
     * has invalid syntax
     */
    @Test
    void isValidPhoneNumberInvalidSyntax() {
        String phoneNumber = "111-222-333%!@#";
        assertFalse(UserValidation.isValidPhoneNumber(phoneNumber));
    }

    /**
     * Test to see whether true (i.e valid) is returned when phone number
     * has valid syntax
     */
    @Test
    void isValidPhoneNumberValidSyntax() {
        String phoneNumber = "+64 3 555 0129";
        assertTrue(UserValidation.isValidPhoneNumber(phoneNumber));
    }

    /**
     * Test to see whether true (i.e valid) is returned when phone number
     * has valid syntax and length is equal to max length.
     */
    @Test
    void isValidPhoneNumberValidSyntaxAndEqualToMaxLength() {
        String phoneNumber = "+64 32 555 0129"; // maxLength = 15
        assertTrue(UserValidation.isValidPhoneNumber(phoneNumber));
    }

    // ********************************* PASSWORD ************************************


    /**
     * Test to see whether true (i.e valid) is returned when password
     * length is within range and password contains all required fields.
     */
    @Test
    void isValidPasswordCorrectLengthAndAllFields() {
        String password = "123ASD!@#asd";
        assertTrue(UserValidation.isValidPassword(password));
    }

    /**
     * Test to see whether true (i.e valid) is returned when password
     * length equals min length and password contains all required fields.
     */
    @Test
    void isValidPasswordEqualToMinLengthAndAllFields() {
        String password = "1AD!@#as";
        assertTrue(UserValidation.isValidPassword(password));
    }

    /**
     * Test to see whether true (i.e valid) is returned when password
     * length equals max length and password contains all required fields.
     */
    @Test
    void isValidPasswordEqualToMaxLengthAndAllFields() {
        String password = "1AD!@#asaaaaaaaaaaaaaaaaaaaaaa"; // maxLength = 30
        assertTrue(UserValidation.isValidPassword(password));
    }

    /**
     * Test to see whether true (i.e invalid) is returned when password
     * length is within range, contains all required fields and contains
     * a space.
     */
    @Test
    void isValidPasswordContainsSpace() {
        String password = "1AD!@#asaa aaa";
        assertTrue(UserValidation.isValidPassword(password));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when password
     * length is within range, but uppercase letter missing.
     */
    @Test
    void isValidPasswordCorrectLengthNoUppercase() {
        String password = "123!@#asd";
        assertFalse(UserValidation.isValidPassword(password));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when password
     * length is within range, but number missing.
     */
    @Test
    void isValidPasswordCorrectLengthNoNumber() {
        String password = "ASD!@#asd";
        assertFalse(UserValidation.isValidPassword(password));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when password
     * length is within range, but symbol missing.
     */
    @Test
    void isValidPasswordCorrectLengthNoSymbol() {
        String password = "ASD124asd";
        assertFalse(UserValidation.isValidPassword(password));
    }

    /**
     * Test to see whether false (i.e invalid) is returned when password
     * length is within range, but lowercase letter missing.
     */
    @Test
    void isValidPasswordCorrectLengthNoLowerCase() {
        String password = "ASD124#!";
        assertFalse(UserValidation.isValidPassword(password));
    }

}
