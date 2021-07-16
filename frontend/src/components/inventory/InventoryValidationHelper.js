import {compareAsc, endOfToday, format} from "date-fns";

/**
 * This method toggles the appearance of the error message, where the is-invalid class is added to the messages
 * if an error message needs to be presented to the user.
 *
 * @param errorMessage, string, the error message relating to invalid input of a field.
 * @returns {[string]}, classList, a list containing the classes for an invalid message.
 */
export function toggleInvalidClass(errorMessage) {
    let classList = ['form-control']
    if (errorMessage !== "") {
        classList.push('is-invalid')
    }
    return classList
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
    } else if (!regex.test(inputVal)) {
        errorMessage = regexMessage;
    } else if (!between(inputVal.length, minLength, maxLength)) {
        errorMessage = `Input must be between ${minLength} and ${maxLength} characters long.`
    }
    return errorMessage;
}

/**
 * This method parses the given date and separates it into a year, month and day, provided it meets
 * the expected format.
 *
 * Note that the date format is yyyy-MM-dd (e.g. '2029-12-30') to use the compareAsc() in the date validation methods.
 * So, this must be consistent!
 *
 * @param dateString, string, the date to validate and separate.
 * @returns {{year: string, month: string, day: string}|null}, {year, month, day}, if the date meets the expected
 * format, else null.
 */
export function parseSelectedDate(dateString) {

    const verifyRegex = /^[0-9]{1,5}-[0-9]{1,3}-[0-9]{1,3}$/;

    if (verifyRegex.test(dateString)) {
        const dateParts = dateString.split("-", 3);

        const year = dateParts[0];
        let month = dateParts[1];
        let day = dateParts[2];

        month = (month.length === 1) ? `0${month}` : month;
        day = (day.length === 1) ? `0${day}` : day;

        return {
            year: year,
            month: month,
            day: day,
        };
    } else {
        return null;
    }
}

/**
 * This function will check the validity of the manufactured date of an inventory item i.e. that the manufactured
 * date of the inventory item is prior to today's date
 * @return true if the date is before today's date or today's date, otherwise false.
 */
export function isValidManufactureDate(selectedManufacturedDate) {

    const selectedDate = this.parseSelectedDate(selectedManufacturedDate);

    if (selectedDate === null) {
        return true;
    } else {

        const givenDateYear = selectedDate.year;
        const givenDateMonth = selectedDate.month;
        const givenDateDay = selectedDate.day;
        const todayDateYear = format(endOfToday(), 'yyyy');
        const todayDateMonth = format(endOfToday(), 'MM');
        const todayDateDay = format(endOfToday(), 'dd');

        // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
        // second or 0 if dates are equal.
        const comparisonValue = compareAsc(new Date(givenDateYear, givenDateMonth, givenDateDay), new Date(parseInt(todayDateYear), parseInt(todayDateMonth), parseInt(todayDateDay)));

        return ((comparisonValue === -1) || (comparisonValue === 0));

    }

}

/**
 * This function will check the validity of the sell by date of an inventory item i.e. that the sell by date of the
 * inventory item is after to today's date but not today's date, and after the manufacture date and before the expiry date (not including).
 *
 * @return true if the date meets the above conditions, otherwise false
 */
export function isValidSellByDate(selectedSellByDate, selectedManufacturedDate, selectedExpiryDate) {

    let isValid = false;
    const sellByDate = this.parseSelectedDate(selectedSellByDate);
    const manufacturedDate = this.parseSelectedDate(selectedManufacturedDate);
    const expiryDate = this.parseSelectedDate(selectedExpiryDate);

    if (sellByDate === null) {
        return true;
    }
    const sellByDateYear = sellByDate.year;
    const sellByDateMonth = sellByDate.month;
    const sellByDateDay = sellByDate.day;

    if (manufacturedDate !== null) {
        const manufacturedDateYear = manufacturedDate.year;
        const manufacturedDateMonth = manufacturedDate.month;
        const manufacturedDateDay = manufacturedDate.day;

        const comparisonWithManufacturedValue = compareAsc(new Date(sellByDateYear, sellByDateMonth, sellByDateDay), new Date(manufacturedDateYear, manufacturedDateMonth, manufacturedDateDay));
        const isAfterManufactureDateAndNotManufactureDate = (comparisonWithManufacturedValue === 1);

        if (!isAfterManufactureDateAndNotManufactureDate) {
            return isValid;
        }
    }
    if (expiryDate !== null) {
        const expiredDateYear = expiryDate.year;
        const expiredDateMonth = expiryDate.month;
        const expiredDateDay = expiryDate.day;

        const comparisonWithExpiryValue = compareAsc(new Date(sellByDateYear, sellByDateMonth, sellByDateDay), new Date(expiredDateYear, expiredDateMonth, expiredDateDay));
        const isBeforeExpiryAndNotExpiryDate = (comparisonWithExpiryValue === -1);

        if (!isBeforeExpiryAndNotExpiryDate) {
            return isValid;
        }
    }

    const todayDateYear = format(endOfToday(), 'yyyy');
    const todayDateMonth = format(endOfToday(), 'MM');
    const todayDateDay = format(endOfToday(), 'dd');

    // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
    // second or 0 if dates are equal.

    const comparisonWithTodayValue = compareAsc(new Date(sellByDateYear, sellByDateMonth, sellByDateDay), new Date(parseInt(todayDateYear), parseInt(todayDateMonth), parseInt(todayDateDay)));
    const isAfterTodayAndNotToday = (comparisonWithTodayValue === 1);

    if (isAfterTodayAndNotToday) {
        isValid = true;
    }

    return isValid;

}

/**
 * This function will check the validity of the best before date of an inventory item i.e. that the best before date
 * of the inventory item is after today's date but not today's date (and implicitly after the manufacture date)
 * and before expiry date.
 *
 * @return true if the date meets the above conditions, otherwise false
 */
export function isValidBestBeforeDate(selectedBestBeforeDate, selectedManufacturedDate, selectedExpiryDate) {

    let isValid = false;
    const bestBeforeDate = this.parseSelectedDate(selectedBestBeforeDate);
    const manufacturedDate = this.parseSelectedDate(selectedManufacturedDate);
    const expiryDate = this.parseSelectedDate(selectedExpiryDate);

    if (bestBeforeDate === null) {
        return true;
    }

    const bestBeforeDateYear = bestBeforeDate.year;
    const bestBeforeDateMonth = bestBeforeDate.month;
    const bestBeforeDateDay = bestBeforeDate.day;

    if (manufacturedDate !== null) {
        const manufacturedDateYear = manufacturedDate.year;
        const manufacturedDateMonth = manufacturedDate.month;
        const manufacturedDateDay = manufacturedDate.day;

        const comparisonWithManufacturedValue = compareAsc(new Date(bestBeforeDateYear, bestBeforeDateMonth, bestBeforeDateDay), new Date(manufacturedDateYear, manufacturedDateMonth, manufacturedDateDay));
        const isAfterManufactureDateAndNotManufactureDate = (comparisonWithManufacturedValue === 1);

        if (!isAfterManufactureDateAndNotManufactureDate) {
            return isValid;
        }
    }
    if (expiryDate !== null) {
        const expiredDateYear = expiryDate.year;
        const expiredDateMonth = expiryDate.month;
        const expiredDateDay = expiryDate.day;

        const comparisonWithExpiryValue = compareAsc(new Date(bestBeforeDateYear, bestBeforeDateMonth, bestBeforeDateDay), new Date(expiredDateYear, expiredDateMonth, expiredDateDay));
        const isBeforeExpiryAndNotExpiryDate = (comparisonWithExpiryValue === -1);

        if (!isBeforeExpiryAndNotExpiryDate) {
            return isValid;
        }
    }

    const todayDateYear = format(endOfToday(), 'yyyy');
    const todayDateMonth = format(endOfToday(), 'MM');
    const todayDateDay = format(endOfToday(), 'dd');

    // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
    // second or 0 if dates are equal.

    const comparisonWithTodayValue = compareAsc(new Date(bestBeforeDateYear, bestBeforeDateMonth, bestBeforeDateDay), new Date(parseInt(todayDateYear), parseInt(todayDateMonth), parseInt(todayDateDay)));
    const isAfterTodayAndNotToday = (comparisonWithTodayValue === 1);

    if (isAfterTodayAndNotToday) {
        isValid = true;
    }

    return isValid;

}

/**
 * This function will check the validity of the expires date of an inventory item i.e. that the expiry date
 * of the inventory item is after today's date, after the manufacture date, and after or equal to the best before
 * date.
 *
 * @return true if the date meets the above conditions, otherwise false
 */
export function isValidExpiryDate(selectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate) {

    let isValid = false;
    const manufacturedDate = this.parseSelectedDate(selectedManufacturedDate);
    const expiryDate = this.parseSelectedDate(selectedExpiryDate);

    if (expiryDate === null) {
        return isValid;
    }

    const expiredDateYear = expiryDate.year;
    const expiredDateMonth = expiryDate.month;
    const expiredDateDay = expiryDate.day;

    if (manufacturedDate !== null) {
        const manufacturedDateYear = manufacturedDate.year;
        const manufacturedDateMonth = manufacturedDate.month;
        const manufacturedDateDay = manufacturedDate.day;

        const comparisonWithManufacturedValue = compareAsc(new Date(expiredDateYear, expiredDateMonth, expiredDateDay), new Date(manufacturedDateYear, manufacturedDateMonth, manufacturedDateDay));
        const isAfterManufactureDateAndNotManufactureDate = (comparisonWithManufacturedValue === 1);

        if (!isAfterManufactureDateAndNotManufactureDate) {
            return isValid;
        }
    }


    const todayDateYear = format(endOfToday(), 'yyyy');
    const todayDateMonth = format(endOfToday(), 'MM');
    const todayDateDay = format(endOfToday(), 'dd');

    // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
    // second or 0 if dates are equal.

    const comparisonWithTodayValue = compareAsc(new Date(expiredDateYear, expiredDateMonth, expiredDateDay), new Date(parseInt(todayDateYear), parseInt(todayDateMonth), parseInt(todayDateDay)));
    const isAfterTodayAndNotToday = (comparisonWithTodayValue === 1);

    if (isAfterTodayAndNotToday) {
        isValid = true;
    }

    return isValid;

}