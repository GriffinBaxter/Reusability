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
