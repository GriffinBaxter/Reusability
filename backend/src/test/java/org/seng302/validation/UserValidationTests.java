package org.seng302.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

class UserValidationTests {

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

}
