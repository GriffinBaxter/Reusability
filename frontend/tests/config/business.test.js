/**
 * This file tests the function exported in the Business.js config file.
 */

import {describe, expect, test} from "@jest/globals";
import {convertToFrontendBusinessType} from "../../src/configs/Business";

describe ('Testing the convertToFrontendBusinessType method', () =>  {

    test('Test ACCOMMODATION_AND_FOOD_SERVICES is converted to Accommodation and Food Services', () => {
        expect(convertToFrontendBusinessType("ACCOMMODATION_AND_FOOD_SERVICES")).toBe("Accommodation and Food Services");
    })

    test('Test RETAIL_TRADE is converted to Retail Trade', () => {
        expect(convertToFrontendBusinessType("RETAIL_TRADE")).toBe("Retail Trade");
    })

    test('Test CHARITABLE_ORGANISATION is converted to Charitable Organisation and Food Services', () => {
        expect(convertToFrontendBusinessType("CHARITABLE_ORGANISATION")).toBe("Charitable Organisation");
    })

    test('Test NON_PROFIT_ORGANISATION is converted to Non Profit Organisation', () => {
        expect(convertToFrontendBusinessType("NON_PROFIT_ORGANISATION")).toBe("Non Profit Organisation");
    })

})
