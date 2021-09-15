/**
 * This file contains functions that are used in multiple Vue components, to
 * reduce code duplication.
 */

/**
 * Check current user's permission
 * @returns {boolean} permission
 */
export function checkAccessPermission(id, actAs) {
    return actAs !== undefined && id !== actAs;
}

/**
 * This function takes the components of an address and returns a list of formatted lines
 * that make up the address and do not contain null.
 * @returns {*[]} a list of lines which make up the address.
 */
export function getFormattedAddress(number, streetName, suburb, city, postcode, region, country) {
    let address = [];
    // line one which contains street number and name
    if (number !== "" && streetName !== "") {
        address.push({line: number + " " + streetName});
    } else {
        address.push({line: number + streetName});
    }
    // line two which contains suburb
    if (suburb !== "") {
        address.push({line: suburb});
    }
    // line three which contains city and postcode
    if (city !== "" && postcode !== "") {
        address.push({line: city + ", " + postcode});
    } else {
        address.push({line: city + postcode});
    }
    // line four which contains region and country
    if (region !== "" && country !== "") {
        address.push({line: region + ", " + country});
    } else {
        address.push({line: region + country});
    }
    return address;
}

/**
 * If an item is null then this function converts the item to an empty string so that null is not shown in the
 * frontend.
 * @param item potentially null item to be checked.
 * @return item if not null or an empty string.
 */
export function checkNullity(item) {
    return (item || "");
}

/**
 * This method validates the date of birth field input and creates a Date which represents the new user's
 * date of birth.
 *
 * @param selectedDate, string, the date of birth of the user.
 * @returns {Boolean|null}, returns true is the date is valid i.e. in the past and meets the expected format, else
 *                          null or false.
 */
export function isValidDateOfBirth(selectedDate) {
    const todayDate = new Date();
    const year_13_ms = 1000 * 60 * 60 * 24 * 365 * 13;
    const data = parseSelectedDate(selectedDate);

    if (data) {
        const {year, month, day} = data;
        if (year && month && day) {
            const chosenDate = new Date(year, month, day);
            return todayDate - chosenDate >= year_13_ms;
        }
    }
    return null;
}

/**
 * This method parses the given date of birth input and separates it into a year, month and day, provided it meets
 * the expected format.
 *
 * @param dateString, string, the date to validate and separate.
 * @returns {{month: number, year: number, day: number}|null}, {year, month, day}, if the date meets the expected
 * format, else null.
 *
 */
function parseSelectedDate(dateString) {
    const verifyRegex = /^[0-9]{1,5}-[0-9]{1,2}-[0-9]{1,2}$/

    if (verifyRegex.test(dateString)) {
        const dateParts = dateString.split("-", 3);
        return {
            year: Number(dateParts[0]),
            month: Number(dateParts[1]),
            day: Number(dateParts[2])
        }
    } else {
        return null
    }
}

/**
 * This method converts the components of the address received from the Komoot Photon API
 * to a single line string.
 *
 * @param properties the components of address received from the Komoot Photon API.
 * @return address a string representation of the address returned by the Komoot Photon API
 */
export function getAddressConcatenation(properties) {
    let address = "";

    let {country, city, postcode, state, street, housenumber, name, district} = properties;

    if (name) { address += name + ", "; }
    if (housenumber) { address += housenumber; }
    if (street) { address += " " + street + ", "; }
    if (district) { address += " " + district + ", "; }
    if (city) { address += city + ", "; }
    if (postcode) { address += postcode + ", "; }
    if (state) { address += state + ", "; }
    if (country) { address += country; }

    return address;
}