import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import Api from "../src/Api";
import Inventory from "../src/views/Inventory";

/**
 * Jest tests for Inventory.vue.
 */

jest.mock("../src/Api");

describe("Test methods in the inventory component", () => {

    let wrapper;
    let $router;
    let $route;
    let retrieveInventoryItemsSpyOn;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };
        $route = {
            params: {
                businessId: 2
            },
            query: {
                page: 1
            }
        };

        retrieveInventoryItemsSpyOn = jest.spyOn(Inventory.methods, 'retrieveInventoryItems');

        wrapper = shallowMount(
            Inventory,
            {
                mocks: {
                    $router,
                    $route
                }
            });
        await wrapper.vm.$nextTick();

        const sortInventoryItemsResponse = {
            status: 200,
            data: [],
            headers: {
                "total-pages": "0",
                "total-rows": "0"
            }
        }

        Api.sortInventoryItems.mockImplementation(() => Promise.resolve(sortInventoryItemsResponse));
    });

    describe("Test setLinkBusinessAccount method", () => {

        test("Testing setLinkBusinessAccount method sets linkBusinessAccount", async () => {
            const data = [
                    {
                        "id": 4,
                        "administrators": [
                            null
                        ],
                        "primaryAdministratorId": 21,
                        "name": "MyBusiness",
                        "description": null,
                        "address": {
                            "streetNumber": null,
                            "streetName": null,
                            "suburb": null,
                            "city": null,
                            "region": null,
                            "country": "New Zealand",
                            "postcode": null
                        },
                        "businessType": "CHARITABLE_ORGANISATION",
                        "created": "2021-09-26T14:53:14.082085",
                        "currencySymbol": "",
                        "currencyCode": "",
                        "businessImages": []
                    }]

            await wrapper.vm.setLinkBusinessAccount(data);
            expect(wrapper.vm.$data.linkBusinessAccount).toEqual(data);
        })

    });

    describe("Test convertToString method", () => {

        test.each([
            {input: "productIdASC", expected: "Product ID Ascending"},
            {input: "productIdDESC", expected: "Product ID Descending"},
            {input: "quantityASC", expected: "Quantity Ascending"},
            {input: "quantityDESC", expected: "Quantity Descending"},
            {input: "pricePerItemASC", expected: "Price Per Item Ascending"},
            {input: "pricePerItemDESC", expected: "Price Per Item Descending"},
            {input: "totalPriceASC", expected: "Total Price Ascending"},
            {input: "totalPriceDESC", expected: "Total Price Descending"},
            {input: "manufacturedASC", expected: "Manufactured Ascending"},
            {input: "manufacturedDESC", expected: "Manufactured Descending"},
            {input: "sellByASC", expected: "Sell By Ascending"},
            {input: "sellByDESC", expected: "Sell By Descending"},
            {input: "bestBeforeASC", expected: "Best Before Ascending"},
            {input: "bestBeforeDESC", expected: "Best Before Descending"},
            {input: "expiresASC", expected: "Expires Ascending"},
            {input: "expiresDESC", expected: "Expires Descending"}
        ])('returns $expected formatted string from input', ({input, expected}) => {
            wrapper.vm.$data.orderByString = input;
            expect(wrapper.vm.convertToString()).toBe(expected);
        });

    })

    describe("Test closeMessage method", () => {

        test("Test closeMessage sets creationSuccess when called", () => {
            wrapper.vm.$data.creationSuccess = true;

            wrapper.vm.closeMessage();

            expect(wrapper.vm.$data.creationSuccess).toEqual(false);

        });

    });

    describe("Test updatePage method", () => {

        test("Test updatePage sets the current page when called with a given page number", () => {

            wrapper.vm.updatePage(89);

            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "", page: "90"}});

        });

        test("Test updatePage pushes to the correct page with query values and URL params set and retrieves inventory items", async () => {

            wrapper.vm.$data.businessId = 8
            wrapper.vm.$data.barcode = "9400547002634"
            wrapper.vm.$data.orderByString = "expiresASC"
            wrapper.vm.updatePage(1);

            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/8/inventory", query: {barcode: "9400547002634", orderBy: "expiresASC", page: "2"}});
            expect(retrieveInventoryItemsSpyOn).toHaveBeenCalledTimes(1);
        });

    });


    describe("Test isValidPageNumber method", () => {

        test("Test isValidPageNumber returns true for a number between zero and the total pages number", () => {

            wrapper.vm.$data.totalPages = 10;

            expect(wrapper.vm.isValidPageNumber(2)).toEqual(true);

        });

        test("Test isValidPageNumber returns false for a number below zero", () => {

            wrapper.vm.$data.totalPages = 10;

            expect(wrapper.vm.isValidPageNumber(-1)).toEqual(false);

        });

        test("Test isValidPageNumber returns false for a number below above the total page number", () => {

            wrapper.vm.$data.totalPages = 10;

            expect(wrapper.vm.isValidPageNumber(11)).toEqual(false);

        });

        test("Test isValidPageNumber returns false for a number equal to the total page number", () => {

            wrapper.vm.$data.totalPages = 10;

            expect(wrapper.vm.isValidPageNumber(10)).toEqual(false);

        });

    });

    describe("Test retrieveBusinessInfo method", () => {

        test("Test retrieveBusinessInfo sets business and currency values when a 200 response is received.", async () => {

            const businessName = "MyBiz";
            const businessDescription = "Hello, this is a cool biz";
            const businessCountry = "New Zealand";
            const currencySymbol = "$";
            const currencyCode = "NZD";

            const getBusinessResponse = {
                status: 200,
                data: {
                    name: businessName,
                    description: businessDescription,
                    address: {
                        country: businessCountry
                    },
                    currencySymbol: currencySymbol,
                    currencyCode: currencyCode
                }
            }

            Api.getBusiness.mockImplementation(() => Promise.resolve(getBusinessResponse))

            await wrapper.vm.retrieveBusinessInfo();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.businessName).toEqual(businessName);
            expect(wrapper.vm.businessDescription).toEqual(businessDescription);
            expect(wrapper.vm.businessCountry).toEqual(businessCountry);
            expect(wrapper.vm.currencySymbol).toEqual(currencySymbol);
            expect(wrapper.vm.currencyCode).toEqual(currencyCode);

        });


    });




});
