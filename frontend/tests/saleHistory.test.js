/**
 * This file contains Jest tests for the SaleHistory component.
 * @jest-environment jsdom
 */
import {shallowMount} from '@vue/test-utils';
import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import Api from "../src/Api";
import SaleHistory from "../src/views/SaleHistory";
import CurrencyAPI from "../src/currencyInstance";
import Cookies from 'js-cookie';

jest.mock("../src/Api");
jest.mock("../src/currencyInstance");


describe('Tests methods in the SaleHistory component.', () => {

    let wrapper;
    let $router;
    let $route;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };
        $route = {
            params: {
                businessId: 2
            }
        };
        wrapper = shallowMount(
            SaleHistory,
            {
                mocks: {
                    $router,
                    $route
                }
            });
        Cookies.get = jest.fn().mockImplementation(() => 1);
    });

    describe("Test the formatPrice method", () => {

        test("Test when currency symbol and code are both empty", () => {
            const price = 2.99;
            wrapper.vm.$data.currencySymbol = "";
            wrapper.vm.$data.currencyCode = "";
            expect(wrapper.vm.formatPrice(price)).toEqual(price);
        })

        test("Test when currency symbol is not empty and code is empty", () => {
            const price = 2.99;
            wrapper.vm.$data.currencySymbol = "$";
            wrapper.vm.$data.currencyCode = "";
            expect(wrapper.vm.formatPrice(price)).toEqual(price);
        })

        test("Test when currency symbol is empty and code is not empty", () => {
            const price = 2.99;
            wrapper.vm.$data.currencySymbol = "";
            wrapper.vm.$data.currencyCode = "NZD";
            expect(wrapper.vm.formatPrice(price)).toEqual(price);
        })

        test("Test when currency symbol is not empty and code is not empty", () => {
            const price = 2.99;
            const formattedPrice = "$" + price + " NZD";
            wrapper.vm.$data.currencySymbol = "$";
            wrapper.vm.$data.currencyCode = "NZD";
            expect(wrapper.vm.formatPrice(price)).toEqual(formattedPrice);
        })

    })

    describe("Test the manageError method", () => {

        test("Test the user is pushed to /timeout when a timeout occurs", async () => {
            const error = {
                request: "A Request Was Made"
            }
            await wrapper.vm.manageError(error);
            expect($router.push).toHaveBeenCalledWith({ path: `/timeout`});
        })

        test("Test the user is pushed to /invalidtoken when a 401 error is received", async () => {
            const error = {
                response: {
                    status: 401
                }
            }
            await wrapper.vm.manageError(error);
            expect($router.push).toHaveBeenCalledWith({ path: `/invalidtoken`});
        })

        test("Test the user is pushed to /forbidden when a 403 error is received", async () => {
            const error = {
                response: {
                    status: 403
                }
            }
            await wrapper.vm.manageError(error);
            expect($router.push).toHaveBeenCalledWith({ path: `/forbidden`});
        })

        test("Test the user is pushed to /noBusiness when a 406 error is received", async () => {
            const error = {
                response: {
                    status: 406
                }
            }
            await wrapper.vm.manageError(error);
            expect($router.push).toHaveBeenCalledWith({ path: `/noBusiness`});
        })

        test("Test the user is pushed to /noBusiness when a random error is received", async () => {
            const error = {
                response: {
                    status: 500
                },
                message: "500 Internal Server Error"
            }
            await wrapper.vm.manageError(error);
            expect($router.push).toHaveBeenCalledWith({ path: `/noBusiness`});
        })

    })

    describe("Test retrieveBusinessInfo method", () => {

        test("Test retrieveBusinessInfo method correctly sets data when a 200 response with data is returned.", async () => {
            const response = {
                status: 200,
                data: {
                    "id": 100,
                    "administrators": [
                        {
                            "id": 100,
                            "firstName": "John",
                            "lastName": "Smith",
                            "middleName": "Hector",
                            "nickname": "Jonny",
                            "bio": "Likes long walks on the beach",
                            "email": "johnsmith99@gmail.com",
                            "dateOfBirth": "1999-04-27",
                            "phoneNumber": "+64 3 555 0129",
                            "homeAddress": {
                                "streetNumber": "3/24",
                                "streetName": "Ilam Road",
                                "suburb": "Upper Riccarton",
                                "city": "Christchurch",
                                "region": "Canterbury",
                                "country": "New Zealand",
                                "postcode": "90210"
                            },
                            "created": "2020-07-14T14:32:00Z",
                            "role": "user",
                            "businessesAdministered": [
                                "string"
                            ]
                        }
                    ],
                    "primaryAdministratorId": 20,
                    "name": "Lumbridge General Store",
                    "description": "A one-stop shop for all your adventuring needs",
                    "address": {
                        "streetNumber": "3/24",
                        "streetName": "Ilam Road",
                        "suburb": "Upper Riccarton",
                        "city": "Christchurch",
                        "region": "Canterbury",
                        "country": "New Zealand",
                        "postcode": "90210"
                    },
                    "businessType": "Accommodation and Food Services",
                    "created": "2020-07-14T14:52:00Z"
                }
            };

            Api.getBusiness.mockImplementation(() => Promise.resolve(response));

            await wrapper.vm.retrieveBusinessInfo();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.businessName).toEqual(response.data.name);
            expect(wrapper.vm.$data.businessCountry).toEqual(response.data.address.country);
        })

    })

    describe("Test retrieveCurrencyInfo method", () => {

        test("Test retrieveCurrencyInfo method correctly sets data when a 200 response with data is returned.", async () => {
            const response = {
                status: 200,
                data: [{
                        currencies: [{
                            code: "NZD",
                            symbol: "$"
                        }]
                }]

            };

            CurrencyAPI.currencyQuery.mockImplementation(() => Promise.resolve(response));

            await wrapper.vm.retrieveCurrencyInfo();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.currencyCode).toEqual(response.data[0].currencies[0].code);
            expect(wrapper.vm.$data.currencySymbol).toEqual(response.data[0].currencies[0].symbol);
        })

    })


})


// 200 retrieve sold listings
// 200 empty sold listings
// 200 < max
// 200 = max
