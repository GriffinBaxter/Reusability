/**
 * Jest tests for CreateNewInventoryItem.vue.
 */

import {test, expect} from "@jest/globals"
import reg from '../src/components/CreateNewInventoryItem'
// import {InventoryItem} from '../Api'

// ***************************************** getErrorMessage() Tests ***************************************************
// /**
//  * Test for ensuring an error message is raised when no input is entered into the product id field.
//  * Also a test for min length.
//  * @result message raised is "Please enter input".
//  */
// test('Product id with no input', () => {
//     const testInputVal = "";
//     const expectedMessage = "Please enter input";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring an error message is raised when input greater then the max length
//  * is entered into the product id field.
//  * @result message raised is "Input must be between 3 and 15 characters long.".
//  */
// test('Product id with length greater than max length', () => {
//     const testInputVal = "1".repeat(20);
//     const expectedMessage = "Input must be between 3 and 15 characters long.";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring no error message is raised when input length is equal to min length of product id.
//  * @result no error message is raised.
//  */
// test('Product id with length equal to min length', () => {
//     const testInputVal = "123" ; //minLength = 3
//     const expectedMessage = "";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring no error message is raised when input length is equal to max length of product id.
//  * @result no error message is raised.
//  */
// test('Product id with length equal to max length', () => {
//     const testInputVal = "1".repeat(15); //maxLength = 15
//     const expectedMessage = "";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring an error message is raised when length of product id is within range but contains lowercase
//  * characters.
//  * @result error message is raised "Must only contain uppercase alphanumeric characters, numbers, or -"
//  */
// test('Product id with valid length but contains lowercase letters', () => {
//     const testInputVal = "aBCa";
//     const expectedMessage = "Must only contain uppercase alphanumeric characters, numbers, or -";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring no error message is raised when length of product id is within range and contains a hyphen.
//  * @result no error message is raised.
//  */
// test('Product id with valid length and contains hyphen', () => {
//     const testInputVal = "A-B-C";
//     const expectedMessage = "";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring no error message is raised when length of product id is within range and contains numbers.
//  * @result no error message is raised.
//  */
// test('Product id with valid length and contains numbers', () => {
//     const testInputVal = "12445";
//     const expectedMessage = "";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring no error message is raised when sample data (WATT-420-BEANS) is entered into product id field.
//  * @result no error message is raised.
//  */
// test('Product id is sample data.', () => {
//     const testInputVal = "WATT-420-BEANS";
//     const expectedMessage = "";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })
//
// /**
//  * Test for ensuring an error message is raised when product id field contains invalid symbols.
//  * @result error message is raised "Must only contain uppercase alphanumeric characters, numbers, or -"
//  */
// test('Product id contains invalid symbols', () => {
//     const testInputVal = "WATT !#%*";
//     const expectedMessage = "Must only contain uppercase alphanumeric characters, numbers, or -";
//
//     expect(
//         reg.methods.getErrorMessage(
//             InventoryItem.config.productID.name,
//             testInputVal,
//             InventoryItem.config.productID.minLength,
//             InventoryItem.config.productID.maxLength,
//             InventoryItem.config.productID.regexMessage,
//             InventoryItem.config.productID.regex,
//         )
//     ).toBe(expectedMessage);
// })

//------------------------------------------------------------------------------------------------------'


/**
 * This method checks that the given date will be parsed as expect i.e. is in the correct format
 * year-month-day e.g. 2000-20-12 and a object containing the year, month and day will be returned
 */
test('parseSelectedDate_GivenValidDateString_ReturnYearMonthAndDay', () => {
    expect(
        reg.methods.parseSelectedDate('2021-01-01')).toStrictEqual({
        year: 2021,
        month: 1,
        day: 1
    }
    );
})

/**
 * This method checks that the given date will be parsed as expect i.e. is in the incorrect format
 * e.g. 20-12-1912 and null will be returned
 */
test('parseSelectedDate_GivenValidDateString_ReturnYearMonthAndDay', () => {
    expect(
        reg.methods.parseSelectedDate('21-01-2001')).toStrictEqual(
            null
    );
})


/**
 * Test for the date being after the current date.
 * @result
 */
test('isADateBeforeToday_DateIsPriorToday_ReturnTrue', () => {
    // const dateToday = new Date(Date.now());
    // const dateTodayYear = dateToday.getFullYear();
    // const dateTodayMonth = dateToday.getMonth();
    // const dateTodayDay = dateToday.getDay()
    // const pastDate = new Date('01-01-2021');
    // const pastDateYear = pastDate.getFullYear();
    // const pastDateMonth = pastDate.getMonth();
    // const pastDateDay = pastDate.getDay();

    expect(
        reg.methods.isADateBeforeToday('2000-01-02')).toBe(true);

})