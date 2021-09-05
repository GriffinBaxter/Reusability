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
 * Filters the response data from the Komoot API by extracting the relevant fields and storing them
 * both as a string to be shown in the autocomplete dropdown box, and unchanged in the addressResultProperties
 * variable to allow for the individual parts of the address to be entered into the correct fields
 * when a user clicks on an autocomplete option.
 * @param data The request result from sent back by the Komoot Photon API
 * @returns {array} A list of addresses to suggest to the user
 */
export function filterResponse(data) {
    let {features} = data;
    let autoCompleteOptions = [];
    let index = 0;
    let numInList = 0;
    let fLength = features.length;
    // Display the first 8 options returned
    let maxL = 8;
    // Clear the list after each request (before filtering)
    this.addressResultProperties = [];

    while ((numInList < maxL) && (index < fLength)) {
        let address = "";
        let { properties } = features[index];
        if (properties) {

            let {country, city, postcode, state, street, housenumber, name, district} = properties;

            if (name) {
                address += name + ", ";
            }

            if (housenumber) {
                address += housenumber;
            }

            if (street) {
                address += " " + street + ", ";
            }

            if (district) {
                address += " " + district + ", ";
            }

            if (city) {
                address += city + ", ";
            }

            if (postcode) {
                address += postcode + ", ";
            }

            if (state) {
                address += state + ", ";
            }

            if (country) {
                address += country;
            }

            if (!autoCompleteOptions.includes(address.trim())) {
                // Add to both the string to display and the variable for later use.
                autoCompleteOptions.push(address.trim());
                this.addressResultProperties.push(properties);
                numInList++;
            }
        }
        index++;
    }
    return autoCompleteOptions;
}