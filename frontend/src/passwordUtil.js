/**
 * Used in the registration page and reset password page.
 * Contains helper functions for checking password criteria.
 */

/**
 * This method toggles the appearance of the password field, where the password will be shown if showPassword is
 * true, else it is hidden.
 * @param showPassword, whether the password should be displayed.
 * @returns {string}, String, the visibility of the password.
 */
export const togglePasswordInputType = (showPassword) => {
    if (showPassword) {
        return 'text'
    } else {
        return 'password'
    }
}
/**
 * This method checks the password against the given criteria and determines whether it meets the criteria.
 * If it does, the colour is changed from black to red.
 *
 * @param password, string, the current input of the password field.
 * @param regex, string, the password criteria that the password is checked against.
 * @param passwordWasTyped Determines if the password was typed.
 * @returns {[string]}, classList, a List containing a String of classes for the password criteria to used.
 */
export const checkPasswordCriteria = (password, regex, passwordWasTyped) => {
    let classList = ['small']
    if (!regex.test(password) && passwordWasTyped) {
        classList.push('text-red');
    }
    return classList;
}