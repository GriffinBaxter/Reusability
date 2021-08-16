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