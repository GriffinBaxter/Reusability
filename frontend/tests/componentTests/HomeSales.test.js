/**
 * Jest tests for HomeSales.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe, beforeAll} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import HomeSales from "../../src/components/saleInsights/HomeSales";

describe("Testing the loading component", () => {

    test("Testing that the loading component appears if loading ", async () => {})

    test("Testing that the loading component is not visible when not loading", async () => {})

})

describe("Testing the hide graph part.", () => {

    test("Testing that the no data to show message appears if not loading and hiding the graph", async () => {})

    test("Testing that the no data to show message does not appear when loading", async () => {})

    test("Testing that the no dat to show message does not appear when not hiding the graph", async () => {})
});

describe("Testing the graph", () => {

    test("Testing that the total sales is shown on screen", async () => {})

    test("Testing that the total revenue appears with the correct currency code and symbol", async () => {})

    test("Testing that the Fulle Sales Report button takes you to the sales page", async () => {})

})

// Mock the Date.now()
describe("Testing generate dates", () => {

    test("Testing that the generated dates are the current week when today is the middle of the week", async () => {})

    test("Testing that the generated dates are the current week when today is the start of the week", async () => {})

    test("Testing that the generated dates are the current week when today is the end of the week", async () => {})
})

describe("Testing the build graph function",() => {

    test("Testing that if the user is acting as a user. The hide graph message appears", async () => {})

    test("Testing that the correct total are calculated and currency is set correctly.", async () => {})

    test("Testing that if an error is thrown from getBusiness handleGraphError is called", async () => {})

    test("Testing that if an error is thrown from getSalesReport handleGraphError is called", async () => {})

    test("Testing a 401 from getBusiness", async () => {})

    test("Testing a 403 from getBusiness", async () => {})

    test("Testing a 500 from getBusiness", async () => {})

    test("Testing a timeout from getBusiness", async () => {})

    test("Testing a 401 from getSalesReport", async () => {})

    test("Testing a 403 from getSalesReport", async () => {})

    test("Testing a 500 from getSalesReport", async () => {})

    test("Testing a timeout from getSalesReport", async () => {})

})

describe("Testing the handleGraphError function", () => {

    test("Testing error with timeout", async () => {})

    test("Testing error with 401", async () => {})

    test("Testing error with 403", async () => {})

    test("Testing error with 500", async () => {})

})

describe("Testing isActingAsUser function", () => {

    test("Testing with a number for actAs", async () => {})

    test("Testing with a string for actAs", async () => {})

    test("Testing with a undefined for actAs", async () => {})

    test("Testing with a null for actAs", async () => {})
})

describe("Testing goToSales", async () => {

    test("Testing with a number for actAs, that the router.push is called", async () => {})

    test("Testing with a number for actAs, that the router.push is called", async () => {})

    test("Testing with a string for actAs, that the router.push is called", async () => {})

    test("Testing with a undefined for actAs, that the router.push is called", async () => {})

    test("Testing with a null for actAs, that the router.push is called", async () => {})
})