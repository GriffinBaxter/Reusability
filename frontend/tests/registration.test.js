import {test, expect} from "@jest/globals"
import reg from '../src/views/Registration'
import User from '../src/configs/User'
import Api from '../src/Api';
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
import router from '../src/router/index'

jest.mock("../src/Api.js");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled : false});
localVue.use(VueRouter);

/**
 * Jest tests for registration.vue.
 */

// ***************************************** getErrorMessage() Tests ***************************************************

// --------------------------------------------- First Name Tests -----------------------------------------------------

/**
 * Test for ensuring an error message is raised when no input is entered into the first name field.
 * @result message raised is "Please enter input".
 */
test('First name with no input', () => {
    const testInputVal = "";
    const expectedMessage = "Please enter input";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the first name field is less than 2
 * characters in length.
 * @result message raised is "Input must be between 2 and 255 characters long.".
 */
test('First name with less than min length', () => {
    const testInputVal = 'a';
    const expectedMessage = `Input must be between 2 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})


/**
 * Test for ensuring an error message is raised when input entered into the first name field is more than 255
 * characters in length.
 * @result message raised is "Input must be between 2 and 255 characters long.".
 */
test('First name with more than max length', () => {
    const testInputVal = "a" * 260;
    const expectedMessage = `Input must be between 2 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the first name field has the
 * correct syntax and leading spaces.
 * @result message raised is the empty string.
 */
test('First name with correct name syntax and leading spaces', () => {
    const testInputVal = "             Hayley";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the first name field has the
 * correct syntax and trailing spaces.
 * @result message raised is the empty string.
 */
test('First name with correct name syntax and trailing spaces', () => {
    const testInputVal = "Hayley             ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the first name field has the
 * correct syntax and includes hyphens.
 * @result message raised is the empty string.
 */
test('First name with correct name syntax, including hyphen', () => {
    const testInputVal = "Bree-Anne";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the first name field has the
 * correct syntax and includes spaces.
 * @result message raised is the empty string.
 */
test('First name with correct name syntax, including space', () => {
    const testInputVal = "Mary Jane";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the first name field has numbers
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */
test('First name with incorrect name syntax with numbers', () => {
    const testInputVal = "Johny123";
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the first name field has symbols
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */
test('First name with incorrect name syntax with symbols', () => {
    const testInputVal = "Dan !!!!!!!";
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the first name field has the
 * correct syntax and includes apostrophes.
 * @result message raised is the empty string.
 */
test('First name with correct name syntax with apostrophes', () => {
    const testInputVal = "Ra'ro";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.firstName.name,
            testInputVal,
            User.config.firstName.minLength,
            User.config.firstName.maxLength,
            User.config.firstName.regexMessage,
            User.config.firstName.regex,
        )
    ).toBe(expectedMessage);
})


// ------------------------------------------------ Middle Name Tests --------------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the middle name field.
 * @result message raised is the empty string.
 */
test('Middle name with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})


/**
 * Test for ensuring no error message is raised when the input entered into the middle name field is less
 * than 2 characters in length.
 * @result message raised is the empty string.
 */
test('Middle name with less than min length', () => {
    const testInputVal = "p";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the middle name field is more than 255
 * characters in length.
 * @result message raised is "Input must be between 0 and 255 characters long.".
 */
test('Middle name with more than max length', () => {
    const testInputVal = "k" * 260;
    const expectedMessage = `Input must be between 0 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the first name field has the
 * correct syntax and leading spaces.
 * @result message raised is the empty string.
 */
test('Middle name with correct name syntax and leading spaces', () => {
    const testInputVal = "             Bobby";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the first name field has the
 * correct syntax and trailing spaces.
 * @result message raised is the empty string.
 */
test('Middle name with correct name syntax and trailing spaces', () => {
    const testInputVal = "Pieter             ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the middle name field has the
 * correct syntax and includes hyphens.
 * @result message raised is the empty string.
 */

test('Middle name with correct name syntax, including hyphen', () => {
    const testInputVal = "Keith-Rodger";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage)
})

/**
 * Test for ensuring no error message is raised when the input entered into the middle name field has the
 * correct syntax and includes spaces.
 * @result message raised is the empty string.
 */

test('Middle name with correct name syntax, including space', () => {
    const testInputVal = "Jane Lee";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the middle name field has numbers
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */

test('Middle name with incorrect name syntax with numbers', () => {
    const testInputVal = "Zack7899";
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the middle name field has symbols
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */

test('Middle name with incorrect name syntax with symbols', () => {
    const testInputVal = "$$$$$"
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the middle name field has the
 * correct syntax and includes apostrophes.
 * @result message raised is the empty string.
 */
test('Middle name with correct name syntax with apostrophes', () => {
    const testInputVal = "Le'roy";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.middleName.name,
            testInputVal,
            User.config.middleName.minLength,
            User.config.middleName.maxLength,
            User.config.middleName.regexMessage,
            User.config.middleName.regex,
        )
    ).toBe(expectedMessage);
})

// ------------------------------------------------- Last Name Tests ---------------------------------------------------

/**
 * Test for ensuring an error message is raised when no input is entered into the last name field.
 * @result message raised is "Please enter input".
 */
test('Last name with no input', () => {
    const testInputVal = "";
    const expectedMessage = "Please enter input";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the last name field is less than 2
 * characters in length.
 * @result message raised is "Input must be between 0 and 255 characters long.".
 */
test('Last name with less than min length', () => {
    const testInputVal = "J";
    const expectedMessage = `Input must be between 2 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the last name field is more than 255
 * characters in length.
 * @result message raised is "Input must be between 0 and 255 characters long.".
 */
test('Last name with more than max length', () => {
    const testInputVal = "Alessandro-Richardson" * 20;
    const expectedMessage = `Input must be between 2 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the last name field has the
 * correct syntax and leading spaces.
 * @result message raised is the empty string.
 */
test('Last name with correct name syntax and leading spaces', () => {
    const testInputVal = "             Smith";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the last name field has the
 * correct syntax and trailing spaces.
 * @result message raised is the empty string.
 */
test('Last name with correct name syntax and trailing spaces', () => {
    const testInputVal = "Yang             ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the last name field has the
 * correct syntax and includes hyphens.
 * @result message raised is the empty string.
 */
test('Last name with correct name syntax, including hyphen', () => {
    const testInputVal = "Miller-Green";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the last name field has the
 * correct syntax and includes spaces.
 * @result message raised is the empty string.
 */
test('Last name with correct name syntax, including space', () => {
    const testInputVal = "Yu Lin";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the last name field has numbers
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */
test('Last name with incorrect name syntax with numbers', () => {
    const testInputVal = "Clark47";
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the last name field has symbols
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */
test('Last name with incorrect name syntax with symbols', () => {
    const testInputVal = "Ryans&*%";
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the last name field has the
 * correct syntax and includes apostrophes.
 * @result message raised is the empty string.
 */
test('Last name with correct name syntax with apostrophes', () => {
    const testInputVal = "A'Worsh";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.lastName.name,
            testInputVal,
            User.config.lastName.minLength,
            User.config.lastName.maxLength,
            User.config.lastName.regexMessage,
            User.config.lastName.regex,
        )
    ).toBe(expectedMessage);
})

// ------------------------------------------------- Nickname Tests ----------------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the nickname field.
 * @result message raised is the empty string.
 */
test('Nickname with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the middle name field is less
 * than 2 characters in length.
 * @result message raised is the empty string.
 */
test('Nickname with less than min length', () => {
    const testInputVal = 'M';
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the nickname field is more than 255
 * characters in length.
 * @result message raised is "Input must be between 0 and 255 characters long.".
 */

test('Nickname with more than max length', () => {
    const testInputVal = "TheSupercalafragalisticexpialadoshusJohnny" * 10;
    const expectedMessage = `Input must be between 0 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the nickname field has the
 * correct syntax and leading spaces.
 * @result message raised is the empty string.
*/
test('Nickname with correct name syntax and leading spaces', () => {
    const testInputVal = '             Libby'
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the nickname field has the
 * correct syntax and trailing spaces.
 * @result message raised is the empty string.
 */
test('Nickname with correct name syntax and trailing spaces', () => {
    const testInputVal = "Bob             ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the nickname field has the
 * correct syntax and includes hyphens.
 * @result message raised is the empty string.
 */
test('Nickname with correct name syntax, including hyphen', () => {
    const testInputVal = "Anna-Bell";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the nickname field has the
 * correct syntax and includes spaces.
 * @result message raised is the empty string.
 */
test('Nickname with correct name syntax, including space', () => {
    const testInputVal = "Su Tsai";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the nickname field has numbers
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */

test('Nickname with incorrect name syntax with numbers', () => {
    const testInputVal = "Ben2000";
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the nickname field has symbols
 * present.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */

test('Nick name with incorrect name syntax with symbols', () => {
    const testInputVal = 'Chad@#!'
    const expectedMessage = "Must be alphanumeric (spaces, -, ' optional)";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the nickname field has the
 * correct syntax and includes apostrophes.
 * @result message raised is the empty string.
 */
test('Nickname with correct name syntax with apostrophes', () => {
    const testInputVal = "Y'orke";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.nickname.name,
            testInputVal,
            User.config.nickname.minLength,
            User.config.nickname.maxLength,
            User.config.nickname.regexMessage,
            User.config.nickname.regex,
        )
    ).toBe(expectedMessage);
})


// --------------------------------------------------- Bio Tests -------------------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the bio field.
 * @result message raised is the empty string.
 */
test('Bio with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.bio.name,
            testInputVal,
            User.config.bio.minLength,
            User.config.bio.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when any input is entered into the bio field and is less than 600
 * characters in length.
 * @result message raised is the empty string.
 */
test('Bio with any input within length', () => {
    const testInputVal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam iaculis leo et enim iaculis, " +
        "eget aliquet neque suscipit. In lectus libero, suscipit et tincidunt facilisis, cursus quis dui. Curabitur at " +
        "convallis lacus. Nullam nec leo pellentesque, mollis augue in, venenatis metus. Aliquam convallis id sem " +
        "dignissim ornare. Proin lacinia nisl vitae erat hendrerit, vel convallis nisi euismod. Vestibulum et lacus sed " +
        "justo venenatis auctor. Nunc vitae nisl sed lectus cursus interdum commodo sed velit.";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.bio.name,
            testInputVal,
            User.config.bio.minLength,
            User.config.bio.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when any input is entered into the bio field and is greater than 600
 * characters in length.
 * @result message raised is `Input must be between 0 and 600 characters long.`.
 */
test('Bio with any input greater than allowed length', () => {
    const testInputVal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam iaculis leo et enim iaculis, " +
        "eget aliquet neque suscipit. In lectus libero, suscipit et tincidunt facilisis, cursus quis dui. Curabitur at " +
        "convallis lacus. Nullam nec leo pellentesque, mollis augue in, venenatis metus. Aliquam convallis id sem " +
        "dignissim ornare. Proin lacinia nisl vitae erat hendrerit, vel convallis nisi euismod. Vestibulum et lacus sed " +
        "justo venenatis auctor. Nunc vitae nisl sed lectus cursus interdum commodo sed velit.Phasellus et vestibulum " +
        "felis, sed eleifend massa. Nulla facilisi. Quisque ut lorem et mauris gravida tristique vitae eget massa. " +
        "Aliquam quis dolor condimentum, semper leo at, porta mi. Fusce pellentesque nisl nulla, a rhoncus massa " +
        "hendrerit eu. Donec viverra commodo elementum. Aliquam et vehicula sapien, id hendrerit ante. Proin pretium " +
        "aliquam lorem, sed fringilla risus porta eget. Mauris quis tortor gravida, efficitur urna ut, tempor orci.";
    const expectedMessage = `Input must be between 0 and 600 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.bio.name,
            testInputVal,
            User.config.bio.minLength,
            User.config.bio.maxLength,
        )
    ).toBe(expectedMessage);
})

// --------------------------------------------------- Email Tests -----------------------------------------------------


/**
 * Test for ensuring an error message is raised when no input is entered into the email field.
 * @result message raised is "Please enter input".
 */
test('Email with no input', () => {
    const testInputVal = "";
    const expectedMessage = "Please enter input";

    expect(
        reg.methods.getErrorMessage(
            User.config.email.name,
            testInputVal,
            User.config.email.minLength,
            User.config.email.maxLength,
            User.config.email.regexMessage,
            User.config.email.regex,

        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the email field and it is less than
 * the expected length of 3 characters. But since it is a invalid format the regex message gets returned.
 * @result message raised is ""Invalid email. Expected format is example123@gmail.com."".
 */
test('Email with input that is too short', () => {
    const testInputVal = "AB";
    const expectedMessage = "Invalid email. Expected format is example123@gmail.com.";

    expect(
        reg.methods.getErrorMessage(
            User.config.email.name,
            testInputVal,
            User.config.email.minLength,
            User.config.email.maxLength,
            User.config.email.regexMessage,
            User.config.email.regex,

        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the home address field and it is more than
 * the expected length of 30 characters.
 * @result message raised is "Input must be between 5 and 30 characters long.".
 */
test('Email with input that is too long', () => {
    const testInputVal = "thisisasuperlongemail@example.com";
    const expectedMessage = `Input must be between 5 and 30 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.email.name,
            testInputVal,
            User.config.email.minLength,
            User.config.email.maxLength,
            User.config.email.regexMessage,
            User.config.email.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the home address field and does not contain
 * the @ symbol.
 * @result message raised is "Invalid email. Expected format is example123@gmail.com.".
 */
test('Email with input that is missing @', () => {
    const testInputVal = "Pellentesque rhoncus massa eget tempor laoreet. '#,&()- ";
    const expectedMessage = "Invalid email. Expected format is example123@gmail.com.";

    expect(
        reg.methods.getErrorMessage(
            User.config.email.name,
            testInputVal,
            User.config.email.minLength,
            User.config.email.maxLength,
            User.config.email.regexMessage,
            User.config.email.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the home address field and contains the
 * sample data's email with additional symbols.
 * @result message raised is "Invalid email. Expected format is example123@gmail.com.".
 */
test('Email with input that contains invalid symbols', () => {
    const testInputVal = "johnsmith99@gmail.com *****";
    const expectedMessage = "Invalid email. Expected format is example123@gmail.com.";

    expect(
        reg.methods.getErrorMessage(
            User.config.email.name,
            testInputVal,
            User.config.email.minLength,
            User.config.email.maxLength,
            User.config.email.regexMessage,
            User.config.email.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the home address field and contains a
 * syntactically valid email but it includes a space.
 * @result message raised is "Invalid email. Expected format is example123@gmail.com.".
 */
test('Email with input that space', () => {
    const testInputVal = "johnsmith99 @gmail.com";
    const expectedMessage = "Invalid email. Expected format is example123@gmail.com.";

    expect(
        reg.methods.getErrorMessage(
            User.config.email.name,
            testInputVal,
            User.config.email.minLength,
            User.config.email.maxLength,
            User.config.email.regexMessage,
            User.config.email.regex,
        )
    ).toBe(expectedMessage);
})

// ------------------------------------------------ Home Address Tests -------------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the home address field.
 * @result No message is raised".
 */
test('Home address with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.homeAddress.name,
            testInputVal,
            User.config.homeAddress.minLength,
            User.config.homeAddress.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input is entered into the home address field and it is less than
 * the expected length of 10 characters.
 * @result No message is raised.
 */
test('Home address with no input', () => {
    const testInputVal = "Ilam";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.homeAddress.name,
            testInputVal,
            User.config.homeAddress.minLength,
            User.config.homeAddress.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the home address field and it is more than
 * the expected length of 255 characters.
 * @result message raised is "Input must be between 0 and 255 characters long.".
 */
test('Home address with input that is too long', () => {
    const testInputVal = "123456789 Pellentesque rhoncus massa eget tempor laoreet. Vivamus aliquet gravida nisi eu ultrices. " +
        "Curabitur dignissim bibendum porttitor. Fusce enim lacus, imperdiet non eros scelerisque, porttitor imperdiet " +
        "ipsum. Nunc nulla eros, dignissim non malesuada vitae, feugiat dapibus metus. Nunc scelerisque nunc nulla. " +
        "Quisque elementum a mauris et aliquet. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices " +
        "posuere cubilia curae; Aenean orci enim, fermentum sit amet fermentum sit amet, tincidunt a nisi. ";
    const expectedMessage = `Input must be between 0 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            User.config.homeAddress.name,
            testInputVal,
            User.config.homeAddress.minLength,
            User.config.homeAddress.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input is entered into the home address field and contains allowed
 * symbols ( '#,.&-).
 * @result No message is raised.
 */
test('Home address with input that contains allowed symbols', () => {
    const testInputVal = "Pellentesque rhoncus massa eget tempor laoreet. '#,&()- ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.homeAddress.name,
            testInputVal,
            User.config.homeAddress.minLength,
            User.config.homeAddress.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the home address field and contains invalid
 * symbols.
 * @result message raised is "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$".
 */
test('Home address with input that contains invalid symbols', () => {
    const testInputVal = "Pellentesque rhoncus massa eget tempor laoreet. ~!@$%^*()_+=";
    const expectedMessage = "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$";

    expect(
        reg.methods.getErrorMessage(
            User.config.homeAddress.name,
            testInputVal,
            User.config.homeAddress.minLength,
            User.config.homeAddress.maxLength,
            User.config.homeAddress.regexMessage,
            User.config.homeAddress.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input is entered into the home address field and contains the
 * sample data's home address.
 * @result message raised is the empty string.
 */
test('Home address with input that contains the sample data\'s home address', () => {
    const testInputVal = "4 Rountree Street, Upper Riccarton";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.homeAddress.name,
            testInputVal,
            User.config.homeAddress.minLength,
            User.config.homeAddress.maxLength,
            User.config.homeAddress.regexMessage,
            User.config.homeAddress.regex,
        )
    ).toBe(expectedMessage);
})

// ------------------------------------------------- Password Tests ----------------------------------------------------

/**
 * Testing the pass for an error is raised when the input doesn't meet the minimum length.
 * @result Message raised is "Invalid password format".
 */
test('Testing the password that the error message is thrown when not meeting the minimum length', () => {
    const testInputVal = '1234567';
    const expectMessage = "Invalid password format";

    expect(
        reg.methods.getErrorMessage(
            User.config.password.name,
            testInputVal,
            User.config.password.minLength,
            User.config.password.maxLength,
            User.config.password.regexStrongMessage,
            User.config.password.regexStrong
        )
    ).toBe(expectMessage)
})

/**
 * Testing the password that an error message is thrown when the maximum length is broken.
 * @result Message raised is "Invalid password format".
 */
test('Testing the password that an error message is thrown when the maximum length is broken', () => {
    const testInputVal = '1'*60; // 60 ones.
    const expectMessage = "Invalid password format";

    expect(
        reg.methods.getErrorMessage(
            User.config.password.name,
            testInputVal,
            User.config.password.minLength,
            User.config.password.maxLength,
            User.config.password.regexStrongMessage,
            User.config.password.regexStrong
        )
    ).toBe(expectMessage)
})

/**
 * Testing that no error message is thrown when given a valid password.
 * @result No message is raised.
 */
test('Testing the password, such that no error messages are thrown given a valid input', () => {
    const testInputVal = '123ASD!@#asd';
    const expectMessage = '';

    expect(
        reg.methods.getErrorMessage(
            User.config.password.name,
            testInputVal,
            User.config.password.minLength,
            User.config.password.maxLength,
            User.config.password.regexStrongMessage,
            User.config.password.regexStrong
        )
    ).toBe(expectMessage)
})

/**
 * Testing that no error is thrown when using numbers in a password.
 * @result No message is raised.
 */
test('Testing the password that no error is thrown when using numbers', () => {
    const testInputVal = 'Sa@132456978';
    const expectMessage = '';

    expect(
        reg.methods.getErrorMessage(
            User.config.password.name,
            testInputVal,
            User.config.password.minLength,
            User.config.password.maxLength,
            User.config.password.regexStrongMessage,
            User.config.password.regexStrong
        )
    ).toBe(expectMessage)
})

/**
 * Testing that no error is thrown when using upper case letters in a password.
 * @result No message is raised.
 */
test('Testing the password that no error is thrown when using upper case letters', () => {
    const testInputVal = 'a1!ASDASDASDASD';
    const expectMessage = '';

    expect(
        reg.methods.getErrorMessage(
            User.config.password.name,
            testInputVal,
            User.config.password.minLength,
            User.config.password.maxLength,
            User.config.password.regexStrongMessage,
            User.config.password.regexStrong
        )
    ).toBe(expectMessage)
})

/**
 * Testing that no error is thrown when using lower case letters in a password.
 * @result No message is raised.
 */
test('Testing the password that no error is thrown when using lower case letters', () => {
    const testInputVal = 'A1@aaaaaaaaaaaaa';
    const expectMessage = '';

    expect(
        reg.methods.getErrorMessage(
            User.config.password.name,
            testInputVal,
            User.config.password.minLength,
            User.config.password.maxLength,
            User.config.password.regexStrongMessage,
            User.config.password.regexStrong
        )
    ).toBe(expectMessage)
})

/**
 * Testing that no error is thrown when using symbols !@#$%^&* in a password.
 * @result No message is raised.
 */
test('Testing the password that no error is thrown when using symbols !@#$%^&*', () => {
    const testInputVal = 'Aa1!@#$%^&*!@#$%^&*!@#$%^&*';
    const expectMessage = '';

    expect(
        reg.methods.getErrorMessage(
            User.config.password.name,
            testInputVal,
            User.config.password.minLength,
            User.config.password.maxLength,
            User.config.password.regexStrongMessage,
            User.config.password.regexStrong
        )
    ).toBe(expectMessage)
})

// ------------------------------------------------ Phone Number Tests -------------------------------------------------
/**
 * Test for ensuring an error message is raised when no input is entered into the phone number field.
 * @result message raised is "Please enter input".
 */
test('Phone number with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.phoneNumber.name,
            testInputVal,
            User.config.phoneNumber.minLength,
            User.config.phoneNumber.maxLength,
            User.config.phoneNumber.regexMessage,
            User.config.phoneNumber.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the phone number field and it contains
 * invalid characters.
 * @result message raised is "Invalid phone number. Must only contain numbers, +, and spaces.".
 */
test('Phone number with invalid syntax', () => {
    const testInputVal = "111-222-333%!@#";
    const expectedMessage = "Invalid phone number. Must only contain numbers, +, and spaces.";

    expect(
        reg.methods.getErrorMessage(
            User.config.phoneNumber.name,
            testInputVal,
            User.config.phoneNumber.minLength,
            User.config.phoneNumber.maxLength,
            User.config.phoneNumber.regexMessage,
            User.config.phoneNumber.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the phone number field and it contains
 * valid characters but is greater than 15 characters in length.
 * @result message raised is "Input must be between 0 and 15 characters long.".
 */
test('Phone number that is too long', () => {
    const testInputVal = "123 456 789 102 345";
    const expectedMessage = "Input must be between 0 and 15 characters long.";

    expect(
        reg.methods.getErrorMessage(
            User.config.phoneNumber.name,
            testInputVal,
            User.config.phoneNumber.minLength,
            User.config.phoneNumber.maxLength,
            User.config.phoneNumber.regexMessage,
            User.config.phoneNumber.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input is entered into the phone number field and contains the
 * sample data's phone number.
 * @result message raised is the empty string.
 */
test('Phone number with input that contains the sample data\'s phone number', () => {
    const testInputVal = "+64 3 555 0129";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            User.config.phoneNumber.name,
            testInputVal,
            User.config.phoneNumber.minLength,
            User.config.phoneNumber.maxLength,
            User.config.phoneNumber.regexMessage,
            User.config.phoneNumber.regex,
        )
    ).toBe(expectedMessage);
})


// ---------------------------------------------- Date of Birth Tests --------------------------------------------------

/**
 * Test for functionality of the getMaxDateOfBirth(), to ensure that we get a date that is valid, such that you have to
 * be at least 13 years of age.
 * @result new Date() - 13 * 365 * 24 * 60 * 60 * 1000.
 * */
test('Test that the value returned is indeed the current time minus 13 years.', () => {
    const THIRTEEN_YEARS_MS = 13 * 365 * 24 * 60 * 60 * 1000
    const expectedValue = (() => {
        let month, day;
        let temp = new Date((new Date() - THIRTEEN_YEARS_MS));

        if (temp.getMonth() < 10) {
            month = `0${temp.getMonth().toString()}`
        } else {
            month = temp.getMonth().toString()
        }

        if (temp.getDate() < 10) {
            day = `0${temp.getDate().toString()}`
        } else {
            day = temp.getDate().toString()
        }

        return `${temp.getFullYear().toString()}-${month}-${day}`
    })()
    expect(reg.methods.getMaxDateOfBirth()).toBe(expectedValue);
})

/**
 * Test for checking the functionality of isValidDateOfBirth when the input is 1234--65.
 * @result null.
 */
test ("Testing isValidDateOfBirth with invalid input 1234--65 gives null.", () => {
    const testInput = '1234--65';
    const expectedValue = null

    expect(reg.methods.isValidDateOfBirth(testInput)).toBe(expectedValue);
})

/**
 * Test for checking the functionality of isValidDateOfBirth when the input is 321E321-546a456-6a54-9874.
 * @result null.
 */
test ("Testing isValidDateOfBirth with invalid input 321E321-546a456-6a54-9874 gives null.", () => {
    const testInput = '321E321-546a456-6a54-9874';
    const expectedValue = null

    expect(reg.methods.isValidDateOfBirth(testInput)).toBe(expectedValue);
})

/**
 * Test for checking the functionality of isValidDateOfBirth when the input is !@#)-!@#-!@.
 * @result null.
 */
test ("Testing isValidDateOfBirth with invalid input !@#)-!@#-!@ gives null.", () => {
    const testInput = '!@#)-!@#-!@';
    const expectedValue = null

    expect(reg.methods.isValidDateOfBirth(testInput)).toBe(expectedValue);
})

/**
 * Test for checking the functionality of isValidDateOfBirth when the input is 123AASD-.
 * @result null.
 */
test ("Testing isValidDateOfBirth with invalid input 123AASD- gives null.", () => {
    const testInput = '123AASD-';
    const expectedValue = null

    expect(reg.methods.isValidDateOfBirth(testInput)).toBe(expectedValue);
})

/**
 * Test for checking the functionality of isValidDateOfBirth when the empty string gives null.
 * @result null.
 */
test ("Testing isValidDateOfBirth with empty string gives null.", () => {
    const testInput = '';
    const expectedValue = null

    expect(reg.methods.isValidDateOfBirth(testInput)).toBe(expectedValue);
})

/**
 * Test for checking the functionality of isValidDateOfBirth() when the input is invalid.
 * @result null.
 * */
test('Testing isValidDateOfBirth with invalid input gives null.', () => {
    const testValue5 = ''
    const expectedValue = null

    expect(reg.methods.isValidDateOfBirth(testValue5)).toBe(expectedValue)
})


/**
 * Testing that that the boundary allowable date is allowed by isValidDateOfBirth.
 * @result true.
 */
test('Test for boundary date', () => {
    const testInput = reg.methods.getMaxDateOfBirth()
    const expectedValue = true

    expect(reg.methods.isValidDateOfBirth(testInput)).toBe(expectedValue)
})

/**
 * Testing that that a arbitrary date before the maximum date is allowed by isValidDateOfBirth.
 * @result true.
 */
test('Test for random early allowable date', () => {
    const testInput = reg.methods.getMaxDateOfBirth().substring(1)
    const expectedValue = true

    expect(reg.methods.isValidDateOfBirth(testInput)).toBe(expectedValue)
})


/**
 * Testing isValidDateOfBirth doesn't allow people below the age of 13.
 * @result false.
 */
test ('Test the children under 13 aren\'t allowed by usValidDateOfBirth', () => {
    // aged 0
    const todayDate = (new Date()).toISOString().split('T')[0];
    const expectedValue = false

    expect(reg.methods.isValidDateOfBirth(todayDate)).toBe(expectedValue)
})

/**
 * Testing isValidDateOfBirth for dates that are in the future are disallowed.
 * @result false.
 *
 */
test('Future birthday is disallowed', () => {
    const todayDate = (new Date()).toISOString().split('T')[0];
    //future date
    const futureDate = '9' + todayDate
    const expectedValue = false

    expect(reg.methods.isValidDateOfBirth(futureDate)).toBe(expectedValue)
})

// ************************************************ Street Number Tests ************************************************

/**
 * Testing that an error message is returned when the inputted street number is greater than the maximum length length
 * of 255 characters in length.
 *  @result message raised is "Input must be between 0 and 255 characters long.".
 */
test( 'Test for the street number for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        User.config.streetNumber.name,
        inputValue,
        User.config.streetNumber.minLength,
        User.config.streetNumber.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string into the street number. This is also
 * a test for the minimum length.
 * @result no error message is raised
 */
test( 'Test for the street number for the minimum length and empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        User.config.streetNumber.name,
        inputValue,
        User.config.streetNumber.minLength,
        User.config.streetNumber.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of a street number.
 * @result No messages should be raised.
 */
test( 'Testing for a valid street number', () => {
    const inputValue = "23"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.streetNumber.name,
        inputValue,
        User.config.streetNumber.minLength,
        User.config.streetNumber.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for street number with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the street number with symbols', () => {
    const inputValue = "!@#!@ 2"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.streetNumber.name,
        inputValue,
        User.config.streetNumber.minLength,
        User.config.streetNumber.maxLength
    )).toBe(expectedValue)
})

// ************************************************** Street Name Tests ************************************************

/**
 * Testing that an error message is returned when the inputted street name is greater than the maximum length
 * of 255 characters in length.
 *  @result message raised is "Input must be between 0 and 255 characters long.".
 */
test( 'Test for the street name for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        User.config.streetName.name,
        inputValue,
        User.config.streetName.minLength,
        User.config.streetName.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string into the street name. This is also
 * a test for the minimum length.
 * @result no error message is raised
 */
test( 'Test for the street name for the minimum length and empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        User.config.streetName.name,
        inputValue,
        User.config.streetName.minLength,
        User.config.streetName.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of a street name.
 * @result No messages should be raised.
 */
test( 'Testing for a valid street name', () => {
    const inputValue = "West Street"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.streetName.name,
        inputValue,
        User.config.streetName.minLength,
        User.config.streetName.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for street name with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the street name with symbols', () => {
    const inputValue = "!@#!@ Street"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.streetName.name,
        inputValue,
        User.config.streetName.minLength,
        User.config.streetName.maxLength
    )).toBe(expectedValue)
})

// ************************************************ City Tests *********************************************************

/**
 * Testing that an error message is returned when the city is greater than the allowed maximum length of 255 characters.
 *  @result message raised is 'Input must be between 0 and 255 characters long'.
 */
test( 'Test for the city for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        User.config.city.name,
        inputValue,
        User.config.city.minLength,
        User.config.city.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string. This is also a test for the minimum length
 * of the city, where no input is allowed i.e. length of 0.
 * @result no message is raised.
 */
test( 'Test for the city for the minimum length and empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        User.config.city.name,
        inputValue,
        User.config.city.minLength,
        User.config.city.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of city.
 * @result No messages should be raised.
 */
test( 'Testing for a valid city', () => {
    const inputValue = "Some city"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.city.name,
        inputValue,
        User.config.city.minLength,
        User.config.city.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for city with numbers.
 * @result No messages should be raised.
 */
test( 'Testing for city with numbers', () => {
    const inputValue = "District 12"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.city.name,
        inputValue,
        User.config.city.minLength,
        User.config.city.maxLength
    )).toBe(expectedValue)
})


/**
 * Testing for city with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the city with symbols', () => {
    const inputValue = "!@#!@ city"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.city.name,
        inputValue,
        User.config.city.minLength,
        User.config.city.maxLength
    )).toBe(expectedValue)
})

// ************************************************ Region Tests *******************************************************

/**
 * Testing that an error message is returned when the region is greater than the max length of 255 characters.
 *  @result message raised is "Input must be between 0 and 255 characters long".
 */
test( 'Test for the region for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        User.config.region.name,
        inputValue,
        User.config.region.minLength,
        User.config.region.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string. This is also a test for the minimum length
 * where an empty string is allowed, i.e. length of 0.
 * @result no message is raised
 */
test( 'Test for the region for the minimum length and empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        User.config.region.name,
        inputValue,
        User.config.region.minLength,
        User.config.region.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of region.
 * @result No messages should be raised.
 */
test( 'Testing for a valid region', () => {
    const inputValue = "Some region"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.region.name,
        inputValue,
        User.config.region.minLength,
        User.config.region.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for region with numbers.
 * @result No messages should be raised.
 */
test( 'Testing for region with numbers', () => {
    const inputValue = "region 12"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.region.name,
        inputValue,
        User.config.region.minLength,
        User.config.region.maxLength
    )).toBe(expectedValue)
})


/**
 * Testing for region with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the region with symbols', () => {
    const inputValue = "!@#!@ region"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.region.name,
        inputValue,
        User.config.region.minLength,
        User.config.region.maxLength
    )).toBe(expectedValue)
})

// ************************************************ Country Tests *****************************************************

/**
 * Testing that an error message is returned when the country is greater than the maximum length of 255 characters.
 *  @result message raised is Input must be between 1 and 255.
 */
test( 'Test for the country for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 1 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        User.config.country.name,
        inputValue,
        User.config.country.minLength,
        User.config.country.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that an error message is returned when we submit the empty string for the country. This is also a test for
 * the minimum length of the country, where an empty string is not allowed i.e. 0 length.
 * @result messaged raised is Please enter input.
 */
test( 'Test for the country for the minimum length and empty string', () => {
    const inputValue = ""
    const expectedValue = `Please enter input`;

    expect(reg.methods.getErrorMessage(
        User.config.country.name,
        inputValue,
        User.config.country.minLength,
        User.config.country.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input for country.
 * @result No messages should be raised.
 */
test( 'Testing for a valid country', () => {
    const inputValue = "Some country"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.country.name,
        inputValue,
        User.config.country.minLength,
        User.config.country.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for country with numbers.
 * @result No messages should be raised.
 */
test( 'Testing for country with numbers', () => {
    const inputValue = "country 13"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.country.name,
        inputValue,
        User.config.country.minLength,
        User.config.country.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for country with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the country with symbols', () => {
    const inputValue = "!@#!@ country"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.country.name,
        inputValue,
        User.config.country.minLength,
        User.config.country.maxLength
    )).toBe(expectedValue)
})

// ********************************************** Postcode Tests *******************************************************

/**
 * Testing that an error message is returned when the postcode has a length greater than the maximum length of 255
 * characters.
 *  @result message raised is Input must be between 0 and 255.
 */
test( 'Test for the postcode for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        User.config.postcode.name,
        inputValue,
        User.config.postcode.minLength,
        User.config.postcode.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string. This is also a test for the minimum length
 * which allows for the empty string i.e. length of 0.
 * @result messaged raised is "Please enter input".
 */
test( 'Test for the postcode for the minimum length and empty string', () => {
    const inputValue = ""
    const expectedValue = '';

    expect(reg.methods.getErrorMessage(
        User.config.postcode.name,
        inputValue,
        User.config.postcode.minLength,
        User.config.postcode.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of postcode.
 * @result No messages should be raised.
 */
test( 'Testing for a valid suburb', () => {
    const inputValue = "19042"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.postcode.name,
        inputValue,
        User.config.postcode.minLength,
        User.config.postcode.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for postcode with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the postcode with symbols', () => {
    const inputValue = "!@#!@ postcode"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.postcode.name,
        inputValue,
        User.config.postcode.minLength,
        User.config.postcode.maxLength
    )).toBe(expectedValue)
})

// ************************************************ Suburb Tests *******************************************************
// Waiting for confirmation from PO

/**
 * Testing that an error message is returned when the suburb has a length greater than the maximum length of 255
 * characters.
 *  @result message raised is Input must be between 1 and 255.
 */
test( 'Test for the suburb for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        User.config.suburb.name,
        inputValue,
        User.config.suburb.minLength,
        User.config.suburb.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that an error message is returned when we submit the empty string. This is also a test for the minimum length
 * which allows for the empty string i.e. length of 0.
 * @result messaged raised is "Please enter input".
 */
test( 'Test for the suburb for the minimum length and empty string', () => {
    const inputValue = ""
    const expectedValue = '';

    expect(reg.methods.getErrorMessage(
        User.config.suburb.name,
        inputValue,
        User.config.suburb.minLength,
        User.config.suburb.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of suburb.
 * @result No messages should be raised.
 */
test( 'Testing for a valid suburb', () => {
    const inputValue = "Some suburb"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.suburb.name,
        inputValue,
        User.config.suburb.minLength,
        User.config.suburb.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for suburb with numbers.
 * @result No messages should be raised.
 */
test( 'Testing for suburb with numbers', () => {
    const inputValue = "suburb 13"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.suburb.name,
        inputValue,
        User.config.suburb.minLength,
        User.config.suburb.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for suburb with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the suburb with symbols', () => {
    const inputValue = "!@#!@ suburb"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        User.config.suburb.name,
        inputValue,
        User.config.suburb.minLength,
        User.config.suburb.maxLength
    )).toBe(expectedValue)
})

// ************************************************ between() Tests ****************************************************

/**
 * Test for checking the functionality of between() when value 1 < value 2 < value 3.
 * @result true.
 */
test('Test if value 1 < value 2 < 3 gives true', () => {
    const testVal = 2;
    const testMin = 1;
    const testMax = 3;
    const expectedMessage = true;

    expect(reg.methods.between(testVal, testMin, testMax)
    ).toBe(expectedMessage);
})

/**
 * Test for checking the functionality of between() when value 1 > value 2 > value 3.
 * @result false.
 */
test('Test if value 1 > value 2 > 3 gives false', () => {
    const testVal = 8;
    const testMin = 7;
    const testMax = 6;
    const expectedMessage = false;

    expect(reg.methods.between(testVal, testMin, testMax)
    ).toBe(expectedMessage);
})

/**
 * Test for checking the functionality of between() when value 1 = value 2 = value 3.
 * @result true.
 */
test('Test if value 1 = value 2 = value 3 gives true', () => {
    const testVal = 11;
    const testMin = 11;
    const testMax = 11;
    const expectedMessage = true;

    expect(reg.methods.between(testVal, testMin, testMax)
    ).toBe(expectedMessage);

})

/**
 * Test for checking the functionality of between() when value 1 > value 2 = value 3.
 * @result true.
 */
test('Test if value 1 > value 2 = value 3 gives false', () => {
    const testVal = 8;
    const testMin = 11;
    const testMax = 20;
    const expectedMessage = false;

    expect(reg.methods.between(testVal, testMin, testMax)
    ).toBe(expectedMessage);
})

// ************************************************ Testing API call ***************************************************

describe('Testing the registration',  () => {

    test('Test the registration API', async () => {
        const validRegistrationPayload = {
            "firstName": "John",
            "lastName": "Smith",
            "middleName": "Hector",
            "nickname": "Jonny",
            "bio": "Likes long walks on the beach",
            "email": "johnsmith99@gmail.com",
            "dateOfBirth": "1999-04-27",
            "phoneNumber": "+64 3 555 0129",
            "homeAddress": {
                "streetNumber": "3/24",
                "streetName": "Ilam Road",
                "city": "Christchurch",
                "region": "Canterbury",
                "country": "New Zealand",
                "postcode": "90210"
            },
            "password": "1337-H%nt3r2"
        };

        const data = {
            status: 201,
            data: {
                userId: 1
            }
        }

        Api.addNewUser.mockImplementation(() => Promise.resolve(data));

        const returnData = await Api.addNewUser(validRegistrationPayload)

        expect(returnData).toBe(data)
    })

    test("Successful login and registration after clicking registration button", async () => {
        const wrapper = await shallowMount(reg, {localVue, router});
        const registerButton = wrapper.find("#register-button");

        await registerButton.trigger("click");

        expect(wrapper.vm.$route.name).toStrictEqual("Login");
    })
})
