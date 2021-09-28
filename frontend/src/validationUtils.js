/**
 * This method toggles the appearance of the error message, where the is-invalid class is added to the messages
 * if an error message needs to be presented to the user.
 *
 * @param errorMessage, string, the error message relating to invalid input of a field.
 * @returns {[string]}, classList, a list containing the classes for an invalid message.
 */
export function toggleInvalidClass(errorMessage) {
    let classList = ['form-control']
    if (errorMessage) {
        classList.push('is-invalid')
    }
    return classList
}

/**
 * This method toggles the appearance of the error message for select boxes, where the is-invalid
 * class is added to the messages if an error message needs to be presented to the user.
 *
 * @param errorMessage, string, the error message relating to invalid input of a field.
 * @returns {[string]}, classList, a list containing the classes for an invalid message.
 */
export function toggleInvalidSelectClass(errorMessage) {
    let classList = ['form-select']
    if (errorMessage) {
        classList.push('is-invalid')
    }
    return classList
}

/**
 * This method determines the error message to be generated for a given input value based on the field type and
 * its associated validity (determined by a regex).
 *
 * @param name, string, name of the input field.
 * @param inputVal, string, the value entered in the stated field.
 * @param minLength, number, the minimum allowed length of the inputVal.
 * @param maxLength, number, the maximum allowed length of the inputVal.
 * @param regexMessage, string, the tailored message about the expected syntax for the inputVal if it does not
 *                              meet the regex given.
 * @param regex, string, the allowed format for the given input field.
 * @returns {string}, errorMessage, the message that needs to be raised if the inputVal does not meet the regex.
 */
export function getErrorMessage(name, inputVal, minLength, maxLength, regexMessage = "", regex = /^[\s\S]*$/) {
    let errorMessage = "";
    if (inputVal === "" && minLength >= 1) {
        errorMessage = "Please enter input";
    }
    else if (!regex.test(inputVal)) {
        errorMessage = regexMessage;
    } else if (!between(inputVal.length, minLength, maxLength)) {
        errorMessage = `Input must be between ${minLength} and ${maxLength} characters long.`
    }
    return errorMessage;
}

/**
 * This method checks whether the given value, val, is within the given lower and upper bounds, inclusive.
 *
 * @param val, int, the value to be tested for being within the range.
 * @param min, int, the minimum value in the range.
 * @param max, int, the maximum value in the range.
 * @returns Boolean, true if within range, false is not within range.
 */
export function between(val, min, max) {
    return min <= val && val <= max;
}
