/**
 * This file contains Jest tests for the validationUtils file.
 */

import {expect, test} from "@jest/globals";
import {toggleInvalidClass, toggleInvalidSelectClass} from "../../src/validationUtils";

// ***************************************** toggleInvalidClass() Tests ***************************************************

/**
 * Test for ensuring that when an error message is passed in, the is-invalid class is added to the messages.
 * @result class list contains 'form-control' and 'is-invalid'
 */
test('Toggle invalid class with error message', () => {
    const testInputVal = "Error message";
    const expectedOutput = ['form-control', 'is-invalid'];

    expect(toggleInvalidClass(testInputVal)).toEqual(expectedOutput);
})

/**
 * Test for ensuring that when no error message is passed in, the is-invalid class is not added to the messages.
 * @result class list contains 'form-control'
 */
test('Toggle invalid class with no error message', () => {
    const testInputVal = "";
    const expectedOutput = ['form-control'];

    expect(toggleInvalidClass(testInputVal)).toEqual(expectedOutput);
})

// ***************************************** toggleInvalidSelectClass() Tests ******************************************

/**
 * Test for ensuring that when an error message is passed in, the is-invalid class is added to the messages.
 * @result class list contains 'form-select' and 'is-invalid'
 */
test('Toggle invalid select class with error message', () => {
    const testInputVal = "Error message";
    const expectedOutput = ['form-select', 'is-invalid'];

    expect(toggleInvalidSelectClass(testInputVal)).toEqual(expectedOutput);
})

/**
 * Test for ensuring that when no error message is passed in, the is-invalid class is not added to the messages.
 * @result class list contains 'form-select'
 */
test('Toggle invalid select class with no error message', () => {
    const testInputVal = "";
    const expectedOutput = ['form-select'];

    expect(toggleInvalidSelectClass(testInputVal)).toEqual(expectedOutput);
})
