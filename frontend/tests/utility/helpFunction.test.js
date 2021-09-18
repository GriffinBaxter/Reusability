/**
 * This file contains Jest tests for the helpFunction file.
 */

import {expect, test, describe, beforeEach} from "@jest/globals";
import {checkAccessPermission, checkNullity, getFormattedAddress, manageError} from "../../src/views/helpFunction";

describe("Test the checkAccessPermission method", () => {

    test("Test when actAs and id are both undefined, method returns false", () => {
        expect(checkAccessPermission(undefined, undefined)).toBeFalsy();
    })
    test("Test when actAs is undefined and id is an int, method returns false", () => {
        expect(checkAccessPermission(1, undefined)).toBeFalsy();
    })
    test("Test when actAs is an int and id is undefined, method returns true", () => {
        expect(checkAccessPermission(undefined, 1)).toBeTruthy();
    })
    test("Test when actAs and id are both an int and equal, method returns false", () => {
        expect(checkAccessPermission(1, 1)).toBeFalsy();
    })
    test("Test when actAs is 1 and id is 2, method returns true", () => {
        expect(checkAccessPermission(2, 1)).toBeTruthy();
    })

})

describe("Test checkNullity method", () => {

    test("Test when item is not null, checkNullity returns the original item", () => {
        const item = "Christchurch";
        expect(checkNullity(item)).toBe(item);
    })
    test("Test when item is null, checkNullity returns an empty string", () => {
        const item = null;
        expect(checkNullity(item)).toBe("");
    })

})

describe("Test getFormattedAddress method", () => {

    let number;
    let streetName;
    let suburb;
    let city;
    let postcode;
    let region;
    let country;

    beforeEach(() => {
        number = "";
        streetName = "";
        suburb = "";
        city = "";
        postcode = "";
        region = "";
        country = "";
    })

    test("Tests that when all fields are empty strings getFormattedAddress returns a list containing empty lines", () => {
        const address = [
            {line: ""},
            {line: ""},
            {line: ""}
        ];
        expect(getFormattedAddress(number, streetName, suburb, city, postcode, region, country)).toStrictEqual(address);
    })

    test("Test that when the second item of each line is an empty string then a list containing lines which" +
        " only contain the first item of each line is returned", () => {
        number = 1;
        suburb = "Ilam";
        city = "Christchurch";
        region = "Canterbury";
        const address = [
            {line: "1"},
            {line: suburb},
            {line: city},
            {line: region}
        ];
        expect(getFormattedAddress(number, streetName, suburb, city, postcode, region, country)).toStrictEqual(address);
    })

    test("Test that when the first item of each line is an empty string then a list containing lines which" +
        " only contain the second item of each line is returned", () => {
        streetName = "Waimairi Road";
        suburb = "Ilam";
        postcode = "8041";
        country = "New Zealand";
        const address = [
            {line: streetName},
            {line: suburb},
            {line: postcode},
            {line: country}
        ];
        expect(getFormattedAddress(number, streetName, suburb, city, postcode, region, country)).toStrictEqual(address);
    })

    test("Test that when every item is not an empty string then the lines returned contain all data.", () => {
        number = 1;
        streetName = "Waimairi Road";
        suburb = "Ilam";
        city = "Christchurch";
        postcode = "8041";
        region = "Canterbury";
        country = "New Zealand";
        const address = [
            {line: number + " " + streetName},
            {line: suburb},
            {line: city + ", " + postcode},
            {line: region + ", " + country}
        ];
        expect(getFormattedAddress(number, streetName, suburb, city, postcode, region, country)).toStrictEqual(address);
    })

})

describe("Test manageError method", () => {
    test("Tests that manageError will return '/timeout' when response undefined and request exist", () => {
        const error = {
            request: "1",
            response: undefined
        }

        expect(manageError(error)).toStrictEqual({path: '/timeout'});
    })

    test("Tests that manageError will return '/invalidtoken' when error status is 401", () => {
        const error = {
            response: {
                status: 401
            }
        }
        expect(manageError(error)).toStrictEqual({path: '/invalidtoken'});
    })

    test("Tests that manageError will return '/forbidden' when error status is 403", () => {
        const error = {
            response: {
                status: 403
            }
        }
        expect(manageError(error)).toStrictEqual({path: '/forbidden'});
    })

    test("Tests that manageError will return '/noBusiness' when error status is 406", () => {
        const error = {
            response: {
                status: 406
            }
        }
        expect(manageError(error)).toStrictEqual({path: '/noBusiness'});
    })

    test("Tests that manageError will return '/noBusiness' when error status is other than 401, 403 and 406", () => {
        const error = {
            response: {
                status: 400
            }
        }
        expect(manageError(error)).toStrictEqual({path: '/noBusiness'});
    })
})

