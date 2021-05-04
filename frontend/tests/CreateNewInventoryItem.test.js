/**
 * Jest tests for CreateNewInventoryItem.vue.
 * @jest-environment jsdom
 */

import {test, expect} from "@jest/globals"
import reg from '../src/components/CreateNewInventoryItem'
import {InventoryItem} from '../src/Api'

const endOfToday = require('date-fns/endOfToday');
const format = require('date-fns/format');

// ***************************************** getErrorMessage() Tests ***************************************************
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
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
            InventoryItem.config.productId.name,
            testInputVal,
            InventoryItem.config.productId.minLength,
            InventoryItem.config.productId.maxLength,
            InventoryItem.config.productId.regexMessage,
            InventoryItem.config.productId.regex,
        )
    ).toBe(expectedMessage);
})

//------------------------------------------------------------------------------------------------------'


/**
 * This method checks that the given date will be parsed as expect i.e. is in the correct format
 * year-month-day e.g. 2000-20-12 and a object containing the year, month and day will be returned
 */
test('parseSelectedDate_GivenValidDateString_ReturnYearMonthAndDay', () => {
    expect(
        reg.methods.parseSelectedDate('2021-01-01')).toStrictEqual({
        year: '2021',
        month: '01',
        day: '01'
    }
    );
})

/**
 * This method checks that the given date will be parsed as expected i.e. if is in the incorrect format
 * e.g. 20-12-1912, null will be returned
 */
test('parseSelectedDate_GivenInvalidDateString_ReturnYearMonthAndDay', () => {
    expect(
        reg.methods.parseSelectedDate('21-01-2001')).toStrictEqual(
            null
    );
})

//------------------------------ date validation tests for isValidManufactureDate --------------------------------------

/**
 * Test for the validation of the manufacture date. Specifically, it tests that the date is valid if it is a date
 * prior to today's date.
 * @result true is returned
 */
test('isValidManufactureDate_DateIsPriorToday_ReturnTrue', () => {
    expect(
        reg.methods.isValidManufactureDate('2000-30-10')).toBe(true);

})

/**
 * Test for the validation of the manufacture date. Specifically, it tests that the date is valid if it is today's date.
 * @result true is returned
 */
test('isValidManufactureDate_DateIsToday_ReturnTrue', () => {

    const todayDateYear = format(endOfToday(new Date()), 'yyyy');
    const todayDateMonth = format(endOfToday(new Date()), 'MM');
    const todayDateDay = format(endOfToday(new Date()), 'dd');

    const todayDate = `${todayDateYear}-${todayDateMonth}-${todayDateDay}`;

    expect(
        reg.methods.isValidManufactureDate(todayDate)).toBe(true);

})

/**
 * Test for the validation of the manufacture date. Specifically, it tests that the date is invalid if it is a date
 * after today's date.
 * @result true is returned
 */
test('isValidManufactureDate_DateIsAfterToday_ReturnFalse', () => {
    expect(
        reg.methods.isValidManufactureDate('2029-04-30')).toBe(false);

})

/**
 * Test for the validation of the manufacture date. Specifically, it tests that a valid date that contains a single digit day
 * is valid 9. This checks that it becomes 09.
 * prior to today's date.
 * @result true is returned
 */
test('isValidManufactureDate_DateIsPriorToday_ReturnTrue', () => {
    expect(
        reg.methods.isValidManufactureDate('2000-09-02')).toBe(true);

})

/**
 * Test for the validation of the manufacture date. Specifically, it tests that a valid date that contains a single digit month
 * is valid i.e. 4 for april. This checks that it becomes 04.
 * prior to today's date.
 * @result true is returned
 */
test('isValidManufactureDate_DateIsPriorToday_ReturnTrue', () => {
    expect(
        reg.methods.isValidManufactureDate('2000-01-04')).toBe(true);

})

/**
 * Test for the validation of the manufacture date. Specifically, it tests that a date in the wrong format returns false.
 * @result true is returned
 */
test('isValidBestBeforeDate_DateIsAfterTodayAndAfterManufactureDateAndBeforeExpiryDateBadFormat_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2029-21-10";
    const testSelectedManufactureDate = "16-10-2021";

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedManufactureDate, selectedManufacturedDate, selectedExpiryDate)).toBe(true);
})

//------------------------------ date validation tests for isValidSellByDate -------------------------------------------

/**
 * Test for the validation of the sell by date. Specifically, it tests that the date is valid if it is a date
 * after today's date, after the manufacture date and before the expiry date.
 * @result true is returned
 */
test('isValidSellByDate_DateIsAfterTodayAndAfterManufactureDateAndBeforeExpiryDate_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2029-21-10";
    const testSelectedSellByDate = "2022-11-10";

    expect(
        reg.methods.isValidSellByDate(testSelectedSellByDate, selectedManufacturedDate, selectedExpiryDate)).toBe(true);
})

/**
 * Test for the validation of the sell by date. Specifically, it tests that the date is invalid if it is today's date
 * (and after the manufacture date and before the expiry date).
 * @result false is returned
 */
test('isValidSellByDate_DateIsTodayAndAfterManufactureDateAndBeforeExpiryDate_ReturnFalse', () => {

    const todayDateYear = format(endOfToday(new Date()), 'yyyy');
    const todayDateMonth = format(endOfToday(new Date()), 'MM');
    const todayDateDay = format(endOfToday(new Date()), 'dd');

    const todayDate = `${todayDateYear}-${todayDateDay}-${todayDateMonth}`; // selected sell by date

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";

    expect(
        reg.methods.isValidSellByDate(todayDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the sell by date. Specifically, it tests that the date is invalid if it is prior to today's date
 * (and after the manufacture date and before the expiry date).
 * @result false is returned
 */
test('isValidSellByDate_DateIsBeforeTodayAndAfterManufactureDateAndBeforeExpiryDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";
    const testSelectedSellByDate = '2020-11-10';

    expect(
        reg.methods.isValidSellByDate(testSelectedSellByDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the sell by date. Specifically, it tests that the date is invalid if it is after the
 * manufacture date and after today but is after the expiry date.
 * @result false is returned
 */
test('isValidSellByDate_DateIsAfterTodayAndAfterManufactureDateAndAfterExpiryDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";
    const testSelectedSellByDate = '2024-11-10';

    expect(
        reg.methods.isValidSellByDate(testSelectedSellByDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the sell by date. Specifically, it tests that the date is invalid if it is after the
 * manufacture date and after today but is the expiry date.
 * @result false is returned
 */
test('isValidSellByDate_DateIsNotTodayAndAfterManufactureDateAndIsExpiryDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";
    const testSelectedSellByDate = "2022-21-10";

    expect(
        reg.methods.isValidSellByDate(testSelectedSellByDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the sell by date. Specifically, it tests that a date in the wrong format returns false.
 * @result true is returned
 */
test('isValidBestBeforeDate_DateIsAfterTodayAndAfterManufactureDateAndBeforeExpiryDateBadFormat_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2029-21-10";
    const testSelectedSellByDate = "16-10-2021";

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedSellByDate, selectedManufacturedDate, selectedExpiryDate)).toBe(true);
})


//------------------------------ date validation tests for isValidBestBeforeDate -------------------------------------------

/**
 * Test for the validation of the best before date. Specifically, it tests that the date is valid if it is a date
 * after today's date, after the manufacture date and before the expiry date.
 * @result true is returned
 */
test('isValidBestBeforeDate_DateIsAfterTodayAndAfterManufactureDateAndBeforeExpiryDate_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2029-21-10";
    const testSelectedBestBeforeDate = "2022-11-10";

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedBestBeforeDate, selectedManufacturedDate, selectedExpiryDate)).toBe(true);
})

/**
 * Test for the validation of the best before date. Specifically, it tests that the date is invalid if it is today's date
 * (and after the manufacture date and before the expiry date).
 * @result false is returned
 */
test('isValidBestBeforeDate_DateIsTodayAndAfterManufactureDateAndBeforeExpiryDate_ReturnFalse', () => {

    const todayDateYear = format(endOfToday(new Date()), 'yyyy');
    const todayDateMonth = format(endOfToday(new Date()), 'MM');
    const todayDateDay = format(endOfToday(new Date()), 'dd');

    const todayDate = `${todayDateYear}-${todayDateDay}-${todayDateMonth}`; // selected best before date

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";

    expect(
        reg.methods.isValidBestBeforeDate(todayDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the best before date. Specifically, it tests that the date is invalid if it is prior to today's date
 * (and after the manufacture date and before the expiry date).
 * @result false is returned
 */
test('isValidBestBeforeDate_DateIsBeforeTodayAndAfterManufactureDateAndBeforeExpiryDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";
    const testSelectedBestBeforeDate = '2020-11-10';

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedBestBeforeDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the best before date. Specifically, it tests that the date is invalid if it is after the
 * manufacture date and after today but is after the expiry date.
 * @result false is returned
 */
test('isValidBestBeforeDate_DateIsAfterTodayAndAfterManufactureDateAndAfterExpiryDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";
    const testSelectedBestBeforeDate = '2024-11-10';

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedBestBeforeDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the best before date. Specifically, it tests that the date is invalid if it is after the
 * manufacture date and after today but is the expiry date.
 * @result false is returned
 */
test('isValidBestBeforeDate_DateIsNotTodayAndAfterManufactureDateAndIsExpiryDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2022-21-10";
    const testSelectedBestBeforeDate = "2022-21-10";

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedBestBeforeDate, selectedManufacturedDate, selectedExpiryDate)).toBe(false);

})

/**
 * Test for the validation of the best before date. Specifically, it tests that a date in the wrong format returns false.
 * @result true is returned
 */
test('isValidBestBeforeDate_DateIsAfterTodayAndAfterManufactureDateAndBeforeExpiryDateBadFormat_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedExpiryDate = "2029-21-10";
    const testSelectedBestBeforeDate = "10-11-2022";

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedBestBeforeDate, selectedManufacturedDate, selectedExpiryDate)).toBe(true);
})


//------------------------------ date validation tests for isValidExpiryDate -------------------------------------------

/**
 * Test for the validation of the expires date. Specifically, it tests that that the expiry date
 * of the inventory item is after today's date, after the manufacture date, and after or equal to the best before
 * date. In this case, the expiry date is equal to the best before date.
 * @result true is returned
 */
test('isValidExpiryDate_DateIsAfterTodayAndAfterManufactureDateAndEqualToBestBeforeDate_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedBestBeforeDate = "2029-21-10";
    const testSelectedExpiryDate = "2029-21-10";

    expect(
        reg.methods.isValidExpiryDate(testSelectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate)).toBe(true);
})

/**
 * Test for the validation of the expires date. Specifically, it tests that that the expiry date
 * of the inventory item is after today's date, after the manufacture date, and after or equal to the best before
 * date. In this case, the expiry date is after the best before date.
 * @result true is returned
 */
test('isValidExpiryDate_DateIsAfterTodayAndAfterManufactureDateAndAfterBestBeforeDate_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedBestBeforeDate = "2029-21-10";
    const testSelectedExpiryDate = "2030-21-10";

    expect(
        reg.methods.isValidExpiryDate(testSelectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate)).toBe(true);
})


/**
 * Test for the validation of the expires date. Specifically, it tests that the date is invalid if it is today's date
 * (and after the manufacture date and before or is the expiry date).
 * @result false is returned
 */
test('isValidExpiryDate_DateIsTodayAndAfterManufactureDateAndAfterBestBeforeDate_ReturnFalse', () => {

    const todayDateYear = format(endOfToday(new Date()), 'yyyy');
    const todayDateMonth = format(endOfToday(new Date()), 'MM');
    const todayDateDay = format(endOfToday(new Date()), 'dd');

    const todayDate = `${todayDateYear}-${todayDateDay}-${todayDateMonth}`; // selected expiry date

    const selectedManufacturedDate = "2019-21-10";
    const selectedBestBeforeDate = "2020-21-10";

    expect(
        reg.methods.isValidExpiryDate(todayDate, selectedBestBeforeDate, selectedManufacturedDate)).toBe(false);

})

/**
 * Test for the validation of the expires date. Specifically, it tests that the date is invalid if it is prior to today's date
 * (and after the manufacture date and before the expiry date).
 * @result false is returned
 */
test('isValidExpiryDate_DateIsBeforeTodayAndAfterManufactureDateAndAfterBestBeforeDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedBestBeforeDate = "2021-01-02";
    const testSelectedExpiryDate = "2021-02-02";

    expect(
        reg.methods.isValidExpiryDate(testSelectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate)).toBe(false);
})


/**
 * Test for the validation of the expires date. Specifically, it tests that the date is invalid if it is before manufactured date
 * @result false is returned
 */
test('isValidExpiryDate_DateIsAfterTodayAndBeforeManufactureDateAndAfterBestBeforeDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedBestBeforeDate = "2017-21-10";
    const testSelectedExpiryDate = "2018-21-10";

    expect(
        reg.methods.isValidExpiryDate(testSelectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate)).toBe(false);
})

/**
 * Test for the validation of the best before date. Specifically, it tests that the date is invalid if it is after the expiry date
 * @result false is returned
 */
test('isValidBestBeforeDate_DateIsAfterTodayAndAfterManufactureDateAndBeforeBestBeforeDate_ReturnFalse', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedBestBeforeDate = "2021-21-10";
    const testSelectedExpiryDate = "2020-21-10";

    expect(
        reg.methods.isValidBestBeforeDate(testSelectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate)).toBe(false);
})


/**
 * Test for the validation of the expires date. Specifically, it tests that a date in the wrong format returns false.
 * @result true is returned
 */
test('isValidExpiryDate_DateIsAfterTodayAndAfterManufactureDateAndBeforeExpiryDateBadFormat_ReturnTrue', () => {

    const selectedManufacturedDate = "2019-21-10";
    const selectedBestBeforeDate = "2029-21-10";
    const testSelectedExpiryDate = "10-21-2029";

    expect(
        reg.methods.isValidExpiryDate(testSelectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate)).toBe(false);
})
