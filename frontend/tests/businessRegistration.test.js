import {test, expect} from "@jest/globals"
import reg from '../src/views/BusinessRegistration'
import {Business, User} from '../src/Api'

/**
 * Jest tests for registration.vue.
 */

// ***************************************** getErrorMessage() Tests ***************************************************

// --------------------------------------------- Business Name Tests -----------------------------------------------------

/**
 * Test for ensuring an error message is raised when no input is entered into the business name field.
 * @result message raised is "Please enter input".
 */
test('Business name with no input', () => {
    const testInputVal = "";
    const expectedMessage = "Please enter input";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input entered into the business name field is more than 100
 * characters in length.
 * @result message raised is "Input must be between 1 and 100 characters long.".
 */
test('Business name with more than max length', () => {
    const testInputVal = "Joe" * 34;
    const expectedMessage = `Input must be between 1 and 100 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the business name field has the
 * correct syntax and leading spaces.
 * @result message raised is the empty string.
 */
test('Business name with correct name, syntax and leading spaces', () => {
    const testInputVal = "     General Store";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the business name field has the
 * correct syntax and trailing spaces.
 * @result message raised is the empty string.
 */
test('Business name with correct name syntax and trailing spaces', () => {
    const testInputVal = "General Store       ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the business name field has the
 * correct syntax and includes hyphens.
 * @result message raised is the empty string.
 */
test('Business name with correct name syntax, including hyphen', () => {
    const testInputVal = "General-Store";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the business name field has the
 * correct syntax and includes spaces.
 * @result message raised is the empty string.
 */
test('Business name with correct name syntax, including space', () => {
    const testInputVal = "General Store";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the business name field has numbers
 * present.
 * @result message raised is the empty string.
 */
test('Business name with correct name syntax with numbers', () => {
    const testInputVal = "3";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the business name field has symbols
 * that are supported
 * @result message raised is the empty string.
 */
test('Business name with correct name syntax with supported symbols', () => {
    const testInputVal = "Dan & Co. '#,.&()-";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when the input entered into the business name field has symbols
 * not supported.
 * @result message "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$".
 */
test('Business name with incorrect name syntax with symbols not supported', () => {
    const testInputVal = "Dan !!!!!!!";
    const expectedMessage = "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when the input entered into the business name field has the
 * correct syntax and includes apostrophes.
 * @result message raised is the empty string.
 */
test('Business name with correct name syntax with apostrophes', () => {
    const testInputVal = "Peps'";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessName.name,
            testInputVal,
            Business.config.businessName.minLength,
            Business.config.businessName.maxLength,
            Business.config.businessName.regexMessage,
            Business.config.businessName.regex,
        )
    ).toBe(expectedMessage);
})

// --------------------------------------------------- Description Tests -------------------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the description field.
 * @result message raised is the empty string.
 */
test('Description with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.description.name,
            testInputVal,
            Business.config.description.minLength,
            Business.config.description.maxLength,
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
            Business.config.description.name,
            testInputVal,
            Business.config.description.minLength,
            Business.config.description.maxLength,
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
            Business.config.description.name,
            testInputVal,
            Business.config.description.minLength,
            Business.config.description.maxLength,
        )
    ).toBe(expectedMessage);
})

// ------------------------------------------------ Business Address Tests -------------------------------------------------

/**
 * Test for ensuring no error message is raised when no input is entered into the business address field.
 * @result No message is raised".
 */
test('Business address with no input', () => {
    const testInputVal = "";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessAddress.name,
            testInputVal,
            Business.config.businessAddress.minLength,
            Business.config.businessAddress.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the business address field and it is more than
 * the expected length of 255 characters.
 * @result message raised is "Input must be between 0 and 255 characters long.".
 */
test('Business address with input that is too long', () => {
    const testInputVal = "123456789 Pellentesque rhoncus massa eget tempor laoreet. Vivamus aliquet gravida nisi eu ultrices. " +
        "Curabitur dignissim bibendum porttitor. Fusce enim lacus, imperdiet non eros scelerisque, porttitor imperdiet " +
        "ipsum. Nunc nulla eros, dignissim non malesuada vitae, feugiat dapibus metus. Nunc scelerisque nunc nulla. " +
        "Quisque elementum a mauris et aliquet. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices " +
        "posuere cubilia curae; Aenean orci enim, fermentum sit amet fermentum sit amet, tincidunt a nisi. ";
    const expectedMessage = `Input must be between 0 and 255 characters long.`;

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessAddress.name,
            testInputVal,
            Business.config.businessAddress.minLength,
            Business.config.businessAddress.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input is entered into the business address field and contains allowed
 * symbols ( '#,.&-).
 * @result No message is raised.
 */
test('Business address with input that contains allowed symbols', () => {
    const testInputVal = "Pellentesque rhoncus massa eget tempor laoreet. '#,&()- ";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessAddress.name,
            testInputVal,
            Business.config.businessAddress.minLength,
            Business.config.businessAddress.maxLength,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring an error message is raised when input is entered into the business address field and contains invalid
 * symbols.
 * @result message raised is "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$".
 */
test('Business address with input that contains invalid symbols', () => {
    const testInputVal = "Pellentesque rhoncus massa eget tempor laoreet. ~!@$%^*()_+=";
    const expectedMessage = "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessAddress.name,
            testInputVal,
            Business.config.businessAddress.minLength,
            Business.config.businessAddress.maxLength,
            Business.config.businessAddress.regexMessage,
            Business.config.businessAddress.regex,
        )
    ).toBe(expectedMessage);
})

/**
 * Test for ensuring no error message is raised when input is entered into the business address field and contains the
 * sample data's business address.
 * @result message raised is the empty string.
 */
test('Business address with input that contains the sample data\'s business address', () => {
    const testInputVal = "4 Rountree Street, Upper Riccarton";
    const expectedMessage = "";

    expect(
        reg.methods.getErrorMessage(
            Business.config.businessAddress.name,
            testInputVal,
            Business.config.businessAddress.minLength,
            Business.config.businessAddress.maxLength,
            Business.config.businessAddress.regexMessage,
            Business.config.businessAddress.regex,
        )
    ).toBe(expectedMessage);
})

// ************************************************ Street Address Tests ***********************************************

/**
 * Testing that an error message is returned when the inputted street address is greater than the maximum length
 * of 255 characters.
 *  @result message raised is "Input must be between 0 and 255 characters long.".
 */
test( 'Test for the street address for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        Business.config.streetAddress.name,
        inputValue,
        Business.config.streetAddress.minLength,
        Business.config.streetAddress.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string into the street address.
 * @result No messages should be raised.
 */
test( 'Test for the street address for empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        Business.config.streetAddress.name,
        inputValue,
        Business.config.streetAddress.minLength,
        Business.config.streetAddress.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of a street address.
 * @result No messages should be raised.
 */
test( 'Testing for a valid street address', () => {
    const inputValue = "Some Street address"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.streetAddress.name,
        inputValue,
        Business.config.streetAddress.minLength,
        Business.config.streetAddress.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for street address with numbers
 * @result No messages should be raised
 */
test( 'Testing for street address with numbers', () => {
    const inputValue = "6 Seven Kings"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.streetAddress.name,
        inputValue,
        Business.config.streetAddress.minLength,
        Business.config.streetAddress.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for street address with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the street address with symbols', () => {
    const inputValue = "!@#!@ 2 Street"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.streetAddress.name,
        inputValue,
        Business.config.streetAddress.minLength,
        Business.config.streetAddress.maxLength
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
        Business.config.city.name,
        inputValue,
        Business.config.city.minLength,
        Business.config.city.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string.
 * @result No messages should be raised.
 */
test( 'Test for the city for empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        Business.config.city.name,
        inputValue,
        Business.config.city.minLength,
        Business.config.city.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of city.
 * @result No messages should be raised.
 */
test( 'Testing for a valid city', () => {
    const inputValue = "Christchurch"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.city.name,
        inputValue,
        Business.config.city.minLength,
        Business.config.city.maxLength
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
        Business.config.city.name,
        inputValue,
        Business.config.city.minLength,
        Business.config.city.maxLength
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
        Business.config.city.name,
        inputValue,
        Business.config.city.minLength,
        Business.config.city.maxLength
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
        Business.config.region.name,
        inputValue,
        Business.config.region.minLength,
        Business.config.region.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that no error message is returned when we submit the empty string.
 * @result No messages should be raised.
 */
test( 'Test for the region for empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        Business.config.region.name,
        inputValue,
        Business.config.region.minLength,
        Business.config.region.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of region.
 * @result No messages should be raised.
 */
test( 'Testing for a valid region', () => {
    const inputValue = "Central Otago"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.region.name,
        inputValue,
        Business.config.region.minLength,
        Business.config.region.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for region with numbers.
 * @result No messages should be raised.
 */
test( 'Testing for region with numbers', () => {
    const inputValue = "Region 12"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.region.name,
        inputValue,
        Business.config.region.minLength,
        Business.config.region.maxLength
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
        Business.config.region.name,
        inputValue,
        Business.config.region.minLength,
        Business.config.region.maxLength
    )).toBe(expectedValue)
})

// ************************************************ Country Tests *****************************************************

/**
 * Testing that an error message is returned when the country is greater than the maximum length of 255 characters.
 *  @result message raised is Input must be between 1 and 255.
 */
test( 'Test for the country for the max length', () => {
    const inputValue = "Abc"*255
    const expectedValue = `Input must be between 1 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        Business.config.country.name,
        inputValue,
        Business.config.country.minLength,
        Business.config.country.maxLength,
        Business.config.country.regexMessage,
        Business.config.country.regex
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
        Business.config.country.name,
        inputValue,
        Business.config.country.minLength,
        Business.config.country.maxLength,
        Business.config.country.regexMessage,
        Business.config.country.regex
    )).toBe(expectedValue)
})

/**
 * Testing for valid input for country.
 * @result No messages should be raised.
 */
test( 'Testing for a valid country', () => {
    const inputValue = "Belgium"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.country.name,
        inputValue,
        Business.config.country.minLength,
        Business.config.country.maxLength,
        Business.config.country.regexMessage,
        Business.config.country.regex
    )).toBe(expectedValue)
})

/**
 * Testing for country with numbers.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */
test( 'Testing for country with numbers', () => {
    const inputValue = "Country 13"
    const expectedValue = "Must be alphanumeric (spaces, -, ' optional)"

    expect(reg.methods.getErrorMessage(
        Business.config.country.name,
        inputValue,
        Business.config.country.minLength,
        Business.config.country.maxLength,
        Business.config.country.regexMessage,
        Business.config.country.regex
    )).toBe(expectedValue)
})

/**
 * Testing for country with invalid symbols.
 * @result message raised is "Must be alphanumeric (spaces, -, ' optional)".
 */
test( 'Testing for the country with symbols', () => {
    const inputValue = "!@#!@ country"
    const expectedValue = "Must be alphanumeric (spaces, -, ' optional)"

    expect(reg.methods.getErrorMessage(
        Business.config.country.name,
        inputValue,
        Business.config.country.minLength,
        Business.config.country.maxLength,
        Business.config.country.regexMessage,
        Business.config.country.regex
    )).toBe(expectedValue)
})

/**
 * Testing for country with valid symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the country with symbols', () => {
    const inputValue = "'- country"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.country.name,
        inputValue,
        Business.config.country.minLength,
        Business.config.country.maxLength,
        Business.config.country.regexMessage,
        Business.config.country.regex
    )).toBe(expectedValue)
})

// ************************************************ Suburb Tests *******************************************************

/**
 * Testing that an error message is returned when the suburb has a length greater than the maximum length of 255
 * characters.
 *  @result message raised is Input must be between 0 and 255.
 */
test( 'Test for the suburb for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        Business.config.suburb.name,
        inputValue,
        Business.config.suburb.minLength,
        Business.config.suburb.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that an error message is returned when we submit the empty string.
 * @result No messages should be raised.
 */
test( 'Test for the suburb for empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        Business.config.suburb.name,
        inputValue,
        Business.config.suburb.minLength,
        Business.config.suburb.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of suburb.
 * @result No messages should be raised.
 */
test( 'Testing for a valid suburb', () => {
    const inputValue = "Ilam"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.suburb.name,
        inputValue,
        Business.config.suburb.minLength,
        Business.config.suburb.maxLength
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
        Business.config.suburb.name,
        inputValue,
        Business.config.suburb.minLength,
        Business.config.suburb.maxLength
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
        Business.config.suburb.name,
        inputValue,
        Business.config.suburb.minLength,
        Business.config.suburb.maxLength
    )).toBe(expectedValue)
})

// *********************************************** Postcode Tests ******************************************************

/**
 * Testing that an error message is returned when the postcode has a length greater than the maximum length of 255
 * characters.
 *  @result message raised is Input must be between 0 and 255.
 */
test( 'Test for the postcode for the max length', () => {
    const inputValue = "2555"*255
    const expectedValue = `Input must be between 0 and 255 characters long.`;

    expect(reg.methods.getErrorMessage(
        Business.config.postcode.name,
        inputValue,
        Business.config.postcode.minLength,
        Business.config.postcode.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing that an error message is returned when we submit the empty string.
 * @result No messages should be raised.
 */
test( 'Test for the postcode for empty string', () => {
    const inputValue = ""
    const expectedValue = "";

    expect(reg.methods.getErrorMessage(
        Business.config.postcode.name,
        inputValue,
        Business.config.postcode.minLength,
        Business.config.postcode.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for valid input of postcode.
 * @result No messages should be raised.
 */
test( 'Testing for a valid postcode', () => {
    const inputValue = "8014"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.postcode.name,
        inputValue,
        Business.config.postcode.minLength,
        Business.config.postcode.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for postcode with numbers.
 * @result No messages should be raised.
 */
test( 'Testing for postcode with numbers', () => {
    const inputValue = "1111"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.postcode.name,
        inputValue,
        Business.config.postcode.minLength,
        Business.config.postcode.maxLength
    )).toBe(expectedValue)
})

/**
 * Testing for postcode with symbols.
 * @result No messages should be raised.
 */
test( 'Testing for the postcode with symbols', () => {
    const inputValue = "!@#!@ suburb"
    const expectedValue = ""

    expect(reg.methods.getErrorMessage(
        Business.config.postcode.name,
        inputValue,
        Business.config.postcode.minLength,
        Business.config.postcode.maxLength
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
