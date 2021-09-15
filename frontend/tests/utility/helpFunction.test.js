import {expect, test, describe, beforeEach} from "@jest/globals";
import {
    checkAccessPermission,
    checkNullity,
    getAddressConcatenation,
    getFormattedAddress
} from "../../src/views/helpFunction";

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

    beforeEach( () => {
        number = "";
        streetName = "";
        suburb = "";
        city = "";
        postcode = "";
        region = "";
        country = "";
    })

    test ("Tests that when all fields are empty strings getFormattedAddress returns a list containing empty lines", () => {
        const address = [
            {line: ""},
            {line: ""},
            {line: ""}
        ];
        expect(getFormattedAddress(number, streetName, suburb, city, postcode, region, country)).toStrictEqual(address);
    })

    test ("Test that when the second item of each line is an empty string then a list containing lines which" +
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

    test ("Test that when the first item of each line is an empty string then a list containing lines which" +
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

    test ("Test that when every item is not an empty string then the lines returned contain all data.", () => {
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

describe("Test getAddressConcatenation method", () => {

    test("Test when properties contains valid strings for each address component.", () => {
        const country = "New Zealand";
        const city = "Christchurch";
        const postcode = "8041";
        const state = "Canterbury";
        const street = "Kirkwood Avenue";
        const housenumber = 20;
        const name = "University of Canterbury" ;
        const district = "Upper Riccarton";
        const properties = {country, city, postcode, state, street, housenumber, name, district};
        const expectedAddressString = name + ", " + housenumber + " " + street + ", " + " " +  district + ", " + city + ", " +
            postcode + ", " + state +  ", " + country;
        expect(getAddressConcatenation(properties)).toEqual(expectedAddressString);
    })


})