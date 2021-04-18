/**
 * @jest-environment jsdom
 */

import {test, expect} from "@jest/globals"
import reg from '../src/views/Products'
import {Product} from '../src/Api'

// ***************************************** getErrorMessage() Tests ***************************************************

// --------------------------------------------- Product ID Tests ------------------------------------------------------

// ProductID
// Less min
// Greater max
// equal min
// equal max
// range correct symbols
// range numbers
// range invalid symbols
// range sample

// Recommended Retail Price

// Manufacturer

// --------------------------------------------- Product Name Tests ----------------------------------------------------

/**
 * Test for ensuring an error message is raised when no input is entered into the product name field.
 * @result message raised is "Please enter input".
 */
test('Product name with no input', () => {
    const testInputVal = "";
    const expectedMessage = "Please enter input";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the product name field is more than 100
 * characters in length.
 * @result message raised is "Input must be between 1 and 100 characters long.".
 */
test('Product name with more than max length', () => {
    const testInputVal = "Eggs" * 34;
    const expectedMessage = `Input must be between 1 and 100 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the product name field has the
 * correct syntax and leading spaces.
 * @result message raised is the empty string.
 */
test('Product name with correct name, syntax and leading spaces', () => {
    const testInputVal = "     Eggs";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the product name field has the
 * correct syntax and trailing spaces.
 * @result message raised is the empty string.
 */
test('Product name with correct name syntax and trailing spaces', () => {
    const testInputVal = "Eggs       ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the product name field has the
 * correct syntax and includes hyphens.
 * @result message raised is the empty string.
 */
test('Product name with correct name syntax, including hyphen', () => {
    const testInputVal = "Eggs-6pk";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the product name field has the
 * correct syntax and includes spaces.
 * @result message raised is the empty string.
 */
test('Product name with correct name syntax, including space', () => {
    const testInputVal = "Eggs 6pk";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the product name field has numbers
 * present.
 * @result message raised is the empty string.
 */
test('Product name with correct name syntax with numbers', () => {
    const testInputVal = "Eggs 6pk";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the product name field has symbols
 * that are supported
 * @result message raised is the empty string.
 */
test('Product name with correct name syntax with supported symbols', () => {
    const testInputVal = "Eggs '#,.&()-";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the product name field has symbols
 * not supported.
 * @result message "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$".
 */
test('Product name with incorrect name syntax with symbols not supported', () => {
    const testInputVal = "Eggs !!!!!!!";
    const expectedMessage = "Must only contain alphanumeric characters, numbers, spaces or '#,.&()-";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the product name field has the
 * correct syntax and includes apostrophes.
 * @result message raised is the empty string.
 */
test('Product name with correct name syntax with apostrophes', () => {
    const testInputVal = "Eggs'";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productName.name,
            testInputVal,
            Product.config.productName.minLength,
            Product.config.productName.maxLength,
            Product.config.productName.regexMessage,
            Product.config.productName.regex,
        )
    ).toBe(expectedMessage);
})

// --------------------------------------------------- Description Tests -----------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the description field.
 * @result message raised is the empty string.
 */
test('Description with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.description.name,
            testInputVal,
            Product.config.description.minLength,
            Product.config.description.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when any input is entered into the description field and is less than 600
 * characters in length.
 * @result message raised is the empty string.
 */
test('Description with any input within length', () => {
    const testInputVal = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam iaculis leo et enim iaculis, " +
        "eget aliquet neque suscipit. In lectus libero, suscipit et tincidunt facilisis, cursus quis dui. Curabitur at " +
        "convallis lacus. Nullam nec leo pellentesque, mollis augue in, venenatis metus. Aliquam convallis id sem " +
        "dignissim ornare. Proin lacinia nisl vitae erat hendrerit, vel convallis nisi euismod. Vestibulum et lacus sed " +
        "justo venenatis auctor. Nunc vitae nisl sed lectus cursus interdum commodo sed velit.";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.description.name,
            testInputVal,
            Product.config.description.minLength,
            Product.config.description.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when any input is entered into the description field and is greater than 600
 * characters in length.
 * @result message raised is `Input must be between 0 and 600 characters long.`.
 */
test('Description with any input greater than allowed length', () => {
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
            Product.config.description.name,
            testInputVal,
            Product.config.description.minLength,
            Product.config.description.maxLength,
        )
    ).toBe(expectedMessage);
})
