/**
 * @jest-environment jsdom
 */

import {test, expect} from "@jest/globals"
import reg from '../src/views/ProductCatalogue'
// noinspection DuplicatedCode
import Product from '../src/configs/Product'

// ***************************************** getErrorMessage() Tests ***************************************************

// --------------------------------------------- Product ID Tests ------------------------------------------------------

/**
 * Test for ensuring an error message is raised when no input is entered into the product id field.
 * Also a test for min length.
 * @result message raised is "Please enter input".
 */
test('Product id with no input', () => {
    const testInputVal = "";
    const expectedMessage = "Please enter input";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input greater then the max length
 * is entered into the product id field.
 * @result message raised is "Input must be between 3 and 15 characters long.".
 */
test('Product id with length greater than max length', () => {
    const testInputVal = "1".repeat(20);
    const expectedMessage = "Input must be between 3 and 15 characters long.";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input length is equal to min length of product id.
 * @result no error message is raised.
 */
test('Product id with length equal to min length', () => {
    const testInputVal = "123" ; //minLength = 3
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input length is equal to max length of product id.
 * @result no error message is raised.
 */
test('Product id with length equal to max length', () => {
    const testInputVal = "1".repeat(15); //maxLength = 15
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when length of product id is within range but contains lowercase
 * characters.
 * @result error message is raised "Must only contain uppercase alphanumeric characters, numbers, or -"
 */
test('Product id with valid length but contains lowercase letters', () => {
    const testInputVal = "aBCa";
    const expectedMessage = "Must only contain uppercase alphanumeric characters, numbers, or -";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when length of product id is within range and contains a hyphen.
 * @result no error message is raised.
 */
test('Product id with valid length and contains hyphen', () => {
    const testInputVal = "A-B-C";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when length of product id is within range and contains numbers.
 * @result no error message is raised.
 */
test('Product id with valid length and contains numbers', () => {
    const testInputVal = "12445";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when sample data (WATT-420-BEANS) is entered into product id field.
 * @result no error message is raised.
 */
test('Product id is sample data.', () => {
    const testInputVal = "WATT-420-BEANS";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when product id field contains invalid symbols.
 * @result error message is raised "Must only contain uppercase alphanumeric characters, numbers, or -"
 */
test('Product id contains invalid symbols', () => {
    const testInputVal = "WATT !#%*";
    const expectedMessage = "Must only contain uppercase alphanumeric characters, numbers, or -";

    expect(
        reg.methods.getErrorMessage(
            Product.config.productID.name,
            testInputVal,
            Product.config.productID.minLength,
            Product.config.productID.maxLength,
            Product.config.productID.regexMessage,
            Product.config.productID.regex,
        )
    ).toBe(expectedMessage);
})

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

// --------------------------------------------- Manufacturer Tests ----------------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the manufacturer field.
 * @result no message raised is.
 */
test('Manufacturer with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the manufacturer field is more than 100
 * characters in length.
 * @result message raised is "Input must be between 0 and 100 characters long.".
 */
test('Manufacturer with more than max length', () => {
    const testInputVal = "Wattie's" * 34;
    const expectedMessage = `Input must be between 0 and 100 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the manufacturer field has the
 * correct syntax and leading spaces.
 * @result message raised is the empty string.
 */
test('Manufacturer with correct syntax and leading spaces', () => {
    const testInputVal = "     Wattie's";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the manufacturer field has the
 * correct syntax and trailing spaces.
 * @result message raised is the empty string.
 */
test('Manufacturer correct syntax and trailing spaces', () => {
    const testInputVal = "Wattie's      ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the manufacturer field has the
 * correct syntax and includes hyphens.
 * @result message raised is the empty string.
 */
test('Manufacturer with correct name syntax, including hyphen', () => {
    const testInputVal = "Wattie's-NZ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the manufacturer field has the
 * correct syntax and includes spaces.
 * @result message raised is the empty string.
 */
test('Manufacturer correct syntax, including space', () => {
    const testInputVal = "Wattie's NZ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the manufacturer field has numbers
 * present.
 * @result message raised is the empty string.
 */
test('Manufacturer with correct syntax with numbers', () => {
    const testInputVal = "Manufacturer 62";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the manufacturer field has symbols
 * that are supported
 * @result message raised is the empty string.
 */
test('Manufacturer with correct syntax with supported symbols', () => {
    const testInputVal = "Manufacturer '#,.&()-";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the manufacturer field has symbols
 * not supported.
 * @result message "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()-".
 */
test('Manufacturer with incorrect syntax with symbols not supported', () => {
    const testInputVal = "Manufacturer !!!!!!!";
    const expectedMessage = "Must only contain alphanumeric characters, numbers, spaces or '#,.&()-";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the manufacturer field has the
 * correct syntax and includes apostrophes.
 * @result message raised is the empty string.
 */
test('Manufacturer with correct syntax with apostrophes', () => {
    const testInputVal = "Wattie's";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.manufacturer.name,
            testInputVal,
            Product.config.manufacturer.minLength,
            Product.config.manufacturer.maxLength,
            Product.config.manufacturer.regexMessage,
            Product.config.manufacturer.regex,
        )
    ).toBe(expectedMessage);
})

// ---------------------------------------- Recommended Retail Price Tests ---------------------------------------------

/**
 * Test for ensuring no error message is raised when the input entered into the RRP field is a positive int.
 * @result no error message raised.
 */
test('RRP is positive int.', () => {
    const testInputVal = "10";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the RRP field is a positive decimal.
 * @result no error message raised.
 */
test('RRP is positive decimal.', () => {
    const testInputVal = "0.1";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the RRP field is a negative int.
 * @result error message raised "Must be a positive double precision floating point number e.g 1.00"
 */
test('RRP is negative int.', () => {
    const testInputVal = "-12";
    const expectedMessage = "Must be a positive double precision floating point number e.g 1.00";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the RRP field is a negative decimal.
 * @result error message raised "Must be a positive double precision floating point number e.g 1.00"
 */
test('RRP is negative decimal.', () => {
    const testInputVal = "-1.01";
    const expectedMessage = "Must be a positive double precision floating point number e.g 1.00";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the RRP field contains the plus symbol.
 * @result error message raised "Must be a positive double precision floating point number e.g 1.00"
 */
test('RRP contains plus symbol.', () => {
    const testInputVal = "+2.3";
    const expectedMessage = "Must be a positive double precision floating point number e.g 1.00";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the RRP field contains characters.
 * @result error message raised "Must be a positive double precision floating point number e.g 1.00"
 */
test('RRP contains characters.', () => {
    const testInputVal = "abc";
    const expectedMessage = "Must be a positive double precision floating point number e.g 1.00";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the RRP field is the empty string.
 * Also test for min length.
 * @result no error message is raised.
 */
test('RRP is empty string', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the RRP field is greater than the max length.
 * @result an error message is raised "Input must be between 0 and 16 characters."
 */
test('RRP length is greater than max length', () => {
    const testInputVal = "1".repeat(20);
    const expectedMessage = "Input must be between 0 and 16 characters long.";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the RRP field is equal to the max length.
 * @result no error message is raised
 */
test('RRP length is equal to max length', () => {
    const testInputVal = "1".repeat(16);
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the RRP field contains invalid symbols,
 * e.g commas.
 * @result error message is raised "Must be a positive double precision floating point number e.g 1.00".
 */
test('RRP contains invalid symbols.', () => {
    const testInputVal = "1-,#";
    const expectedMessage = "Must be a positive double precision floating point number e.g 1.00";

    expect(
        reg.methods.getErrorMessage(
            Product.config.recommendedRetailPrice.name,
            testInputVal,
            Product.config.recommendedRetailPrice.minLength,
            Product.config.recommendedRetailPrice.maxLength,
            Product.config.recommendedRetailPrice.regexMessage,
            Product.config.recommendedRetailPrice.regex,
        )
    ).toBe(expectedMessage);
})