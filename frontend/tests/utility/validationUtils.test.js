/**
 * This file contains Jest tests for the validationUtils file.
 */

// ***************************************** toggleInvalidClass() Tests ***************************************************
import {expect, test} from "@jest/globals";
import {toggleInvalidClass} from "../../src/validationUtils";

/**
 * Test for ensuring that when an error message is passed in, the is-invalid class is added to the messages.
 * @result class list contains 'form-control' and 'is-invalid'
 */
test('Toggle invalid class with error message', () => {
    const testInputVal = "Error message";
    const expectedOutput = ['form-control', 'is-invalid'];

    expect(
        toggleInvalidClass(testInputVal)
    ).toEqual(expectedOutput);
})

/**
 * Test for ensuring that when no error message is passed in, the is-invalid class is not added to the messages.
 * @result class list contains 'form-control'
 */
test('Toggle invalid class with no error message', () => {
    const testInputVal = "";
    const expectedOutput = ['form-control'];

    expect(
        toggleInvalidClass(testInputVal)
    ).toEqual(expectedOutput);
})
