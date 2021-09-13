/**
 * This file contains Jest tests for the SaleHistory component.
 * @jest-environment jsdom
 */
import {shallowMount} from '@vue/test-utils';
import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import Api from "../../src/Api";
import SaleHistory from "../../src/components/saleInsights/SaleHistory";
import CurrencyAPI from "../../src/currencyInstance";
import Cookies from 'js-cookie';

jest.mock("../../src/Api");
jest.mock("../../src/currencyInstance");


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
                },
                propsData: {
                    businessName: "Lumbridge General Store",
                    businessCountry: "New Zealand",
                    businessId: 1
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

            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$props.businessName).toEqual(response.data.name);
            expect(wrapper.vm.$props.businessCountry).toEqual(response.data.address.country);
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

    describe("Test retrieveSoldListings method", () => {

        test("Test that the table rows (soldListings) are filled when zero soldListings are received from the backend", async ()=> {
            const response = {
                status: 200,
                data: [],
                headers: {
                    "total-rows": 0,
                    "total-pages": 1
                }
            };
            Api.getSoldListings.mockImplementation(() => Promise.resolve(response));

            await wrapper.vm.retrieveSoldListings();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.noResults).toBeTruthy(); // there is no results
            expect(wrapper.vm.$data.soldListings.length).toEqual(10); // the max number of sold listings
        })

        test("Test that the data is correctly formatted when a 200 response is received from the backend with data", async ()=> {
            const response = {
                status: 200,
                data: [
                    {
                        "id": 5,
                        "saleDate": "2021-08-15 23:37:39",
                        "listingDate": "2020-09-18 12:58:47",
                        "productId": "BLACK-FOREST",
                        "quantity": 96,
                        "price": 52.46,
                        "bookmarks": 48,
                        "customer": {
                            "id": 7,
                            "firstName": "Francisca",
                            "lastName": "Bznitez",
                            "middleName": "Tessa",
                            "nickname": "Fran",
                            "bio": "Biography",
                            "email": "francisca.bznitez@example.com",
                            "created": "2021-01-01T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Bernhard Run",
                                "region": "Southland",
                                "country": "New Zealand"
                            }
                        }
                    },
                    {
                        "id": 119,
                        "saleDate": "2021-08-15 23:30:09",
                        "listingDate": "2021-04-16 08:29:00",
                        "productId": "BLACK-FOREST",
                        "quantity": 80,
                        "price": 2.97,
                        "bookmarks": 95,
                        "customer": {
                            "id": 9,
                            "firstName": "Lina",
                            "lastName": "Patterson",
                            "middleName": "Jose Mari",
                            "nickname": "Lina",
                            "bio": "Da",
                            "email": "linap@email.com",
                            "created": "2010-05-14T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Funes",
                                "region": "Santa Fe",
                                "country": "Argentina"
                            }
                        }
                    },
                    {
                        "id": 713,
                        "saleDate": "2021-08-15 23:25:42",
                        "listingDate": "2021-05-13 09:41:09",
                        "productId": "BLACK-FOREST",
                        "quantity": 39,
                        "price": 15.93,
                        "bookmarks": 59,
                        "customer": {
                            "id": 14,
                            "firstName": "Pia",
                            "lastName": "Kemp",
                            "middleName": "Alex",
                            "nickname": "Hemp",
                            "bio": "My cool bio.",
                            "email": "piakemp13@email.com",
                            "created": "2020-01-12T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Scarborough",
                                "region": "England",
                                "country": "United Kingdom"
                            }
                        }
                    },
                    {
                        "id": 770,
                        "saleDate": "2021-08-15 23:22:05",
                        "listingDate": "2020-11-25 11:21:00",
                        "productId": "BLACK-FOREST",
                        "quantity": 71,
                        "price": 12.49,
                        "bookmarks": 56,
                        "customer": {
                            "id": 13,
                            "firstName": "Ife",
                            "lastName": "Weston",
                            "middleName": "Missie",
                            "nickname": "Missie",
                            "bio": "Miss me.",
                            "email": "missie@gmail.com",
                            "created": "2014-02-14T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                {
                                    "id": 2,
                                    "administrators": [
                                        null
                                    ],
                                    "primaryAdministratorId": 10,
                                    "name": "Sunburst Waste",
                                    "description": "Description",
                                    "address": {
                                        "streetNumber": "1849",
                                        "streetName": "C Street Northwest",
                                        "suburb": null,
                                        "city": "Washington",
                                        "region": "District of Columbia",
                                        "country": "United States",
                                        "postcode": "20240"
                                    },
                                    "businessType": "CHARITABLE_ORGANISATION",
                                    "created": "2021-02-14T00:00"
                                }
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Detroit",
                                "region": "Michigan",
                                "country": "United States"
                            }
                        }
                    },
                    {
                        "id": 710,
                        "saleDate": "2021-08-15 22:59:32",
                        "listingDate": "2021-03-14 19:56:26",
                        "productId": "BLACK-FOREST",
                        "quantity": 95,
                        "price": 5.04,
                        "bookmarks": 79,
                        "customer": {
                            "id": 7,
                            "firstName": "Francisca",
                            "lastName": "Bznitez",
                            "middleName": "Tessa",
                            "nickname": "Fran",
                            "bio": "Biography",
                            "email": "francisca.bznitez@example.com",
                            "created": "2021-01-01T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Bernhard Run",
                                "region": "Southland",
                                "country": "New Zealand"
                            }
                        }
                    },
                    {
                        "id": 753,
                        "saleDate": "2021-08-15 22:51:42",
                        "listingDate": "2020-09-30 04:07:59",
                        "productId": "BLACK-FOREST",
                        "quantity": 90,
                        "price": 8.87,
                        "bookmarks": 91,
                        "customer": {
                            "id": 14,
                            "firstName": "Pia",
                            "lastName": "Kemp",
                            "middleName": "Alex",
                            "nickname": "Hemp",
                            "bio": "My cool bio.",
                            "email": "piakemp13@email.com",
                            "created": "2020-01-12T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Scarborough",
                                "region": "England",
                                "country": "United Kingdom"
                            }
                        }
                    },
                    {
                        "id": 463,
                        "saleDate": "2021-08-15 22:39:22",
                        "listingDate": "2021-07-21 19:44:03",
                        "productId": "BLACK-FOREST",
                        "quantity": 24,
                        "price": 57.57,
                        "bookmarks": 80,
                        "customer": {
                            "id": 9,
                            "firstName": "Lina",
                            "lastName": "Patterson",
                            "middleName": "Jose Mari",
                            "nickname": "Lina",
                            "bio": "Da",
                            "email": "linap@email.com",
                            "created": "2010-05-14T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Funes",
                                "region": "Santa Fe",
                                "country": "Argentina"
                            }
                        }
                    },
                    {
                        "id": 469,
                        "saleDate": "2021-08-15 22:35:38",
                        "listingDate": "2021-05-31 19:35:20",
                        "productId": "BLACK-FOREST",
                        "quantity": 25,
                        "price": 8.67,
                        "bookmarks": 75,
                        "customer": {
                            "id": 7,
                            "firstName": "Francisca",
                            "lastName": "Bznitez",
                            "middleName": "Tessa",
                            "nickname": "Fran",
                            "bio": "Biography",
                            "email": "francisca.bznitez@example.com",
                            "created": "2021-01-01T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Bernhard Run",
                                "region": "Southland",
                                "country": "New Zealand"
                            }
                        }
                    },
                    {
                        "id": 147,
                        "saleDate": "2021-08-15 21:40:32",
                        "listingDate": "2020-08-29 22:43:37",
                        "productId": "BLACK-FOREST",
                        "quantity": 91,
                        "price": 98.47,
                        "bookmarks": 73,
                        "customer": {
                            "id": 15,
                            "firstName": "Alyce",
                            "lastName": "Gibbs",
                            "middleName": "Teddie",
                            "nickname": "Teddie",
                            "bio": "Looking for cheap teddies.",
                            "email": "alycegibbs@gmail.com",
                            "created": "2019-03-28T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Dar es Salaam",
                                "region": "Coastal Zone",
                                "country": "Tanzania"
                            }
                        }
                    },
                    {
                        "id": 131,
                        "saleDate": "2021-08-15 21:00:26",
                        "listingDate": "2021-06-09 20:21:05",
                        "productId": "BLACK-FOREST",
                        "quantity": 56,
                        "price": 90.8,
                        "bookmarks": 15,
                        "customer": {
                            "id": 14,
                            "firstName": "Pia",
                            "lastName": "Kemp",
                            "middleName": "Alex",
                            "nickname": "Hemp",
                            "bio": "My cool bio.",
                            "email": "piakemp13@email.com",
                            "created": "2020-01-12T00:00",
                            "role": "USER",
                            "businessesAdministered": [
                                null
                            ],
                            "homeAddress": {
                                "suburb": null,
                                "city": "Scarborough",
                                "region": "England",
                                "country": "United Kingdom"
                            }
                        }
                    }
                ],
                headers: {
                    "total-rows": 10,
                    "total-pages": 1
                }
            };
            const formattedListings = [
                {
                    "id": 5,
                    "saleDate": "15th Aug 2021 11:37 pm",
                    "listingDate": "18th Sep 2020 12:58 pm",
                    "productId": "BLACK-FOREST",
                    "quantity": 96,
                    "price": "$52.46 USD",
                    "bookmarks": 48,
                    "customer": {
                        "id": 7,
                        "firstName": "Francisca",
                        "lastName": "Bznitez",
                        "middleName": "Tessa",
                        "nickname": "Fran",
                        "bio": "Biography",
                        "email": "francisca.bznitez@example.com",
                        "created": "2021-01-01T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Bernhard Run",
                            "region": "Southland",
                            "country": "New Zealand"
                        }
                    }
                },
                {
                    "id": 119,
                    "saleDate": "15th Aug 2021 11:30 pm",
                    "listingDate": "16th Apr 2021 8:29 am",
                    "productId": "BLACK-FOREST",
                    "quantity": 80,
                    "price": "$2.97 USD",
                    "bookmarks": 95,
                    "customer": {
                        "id": 9,
                        "firstName": "Lina",
                        "lastName": "Patterson",
                        "middleName": "Jose Mari",
                        "nickname": "Lina",
                        "bio": "Da",
                        "email": "linap@email.com",
                        "created": "2010-05-14T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Funes",
                            "region": "Santa Fe",
                            "country": "Argentina"
                        }
                    }
                },
                {
                    "id": 713,
                    "saleDate": "15th Aug 2021 11:25 pm",
                    "listingDate": "13th May 2021 9:41 am",
                    "productId": "BLACK-FOREST",
                    "quantity": 39,
                    "price": "$15.93 USD",
                    "bookmarks": 59,
                    "customer": {
                        "id": 14,
                        "firstName": "Pia",
                        "lastName": "Kemp",
                        "middleName": "Alex",
                        "nickname": "Hemp",
                        "bio": "My cool bio.",
                        "email": "piakemp13@email.com",
                        "created": "2020-01-12T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Scarborough",
                            "region": "England",
                            "country": "United Kingdom"
                        }
                    }
                },
                {
                    "id": 770,
                    "saleDate": "15th Aug 2021 11:22 pm",
                    "listingDate": "25th Nov 2020 11:21 am",
                    "productId": "BLACK-FOREST",
                    "quantity": 71,
                    "price": "$12.49 USD",
                    "bookmarks": 56,
                    "customer": {
                        "id": 13,
                        "firstName": "Ife",
                        "lastName": "Weston",
                        "middleName": "Missie",
                        "nickname": "Missie",
                        "bio": "Miss me.",
                        "email": "missie@gmail.com",
                        "created": "2014-02-14T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            {
                                "id": 2,
                                "administrators": [
                                    null
                                ],
                                "primaryAdministratorId": 10,
                                "name": "Sunburst Waste",
                                "description": "Description",
                                "address": {
                                    "streetNumber": "1849",
                                    "streetName": "C Street Northwest",
                                    "suburb": null,
                                    "city": "Washington",
                                    "region": "District of Columbia",
                                    "country": "United States",
                                    "postcode": "20240"
                                },
                                "businessType": "CHARITABLE_ORGANISATION",
                                "created": "2021-02-14T00:00"
                            }
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Detroit",
                            "region": "Michigan",
                            "country": "United States"
                        }
                    }
                },
                {
                    "id": 710,
                    "saleDate": "15th Aug 2021 10:59 pm",
                    "listingDate": "14th Mar 2021 7:56 pm",
                    "productId": "BLACK-FOREST",
                    "quantity": 95,
                    "price": "$5.04 USD",
                    "bookmarks": 79,
                    "customer": {
                        "id": 7,
                        "firstName": "Francisca",
                        "lastName": "Bznitez",
                        "middleName": "Tessa",
                        "nickname": "Fran",
                        "bio": "Biography",
                        "email": "francisca.bznitez@example.com",
                        "created": "2021-01-01T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Bernhard Run",
                            "region": "Southland",
                            "country": "New Zealand"
                        }
                    }
                },
                {
                    "id": 753,
                    "saleDate": "15th Aug 2021 10:51 pm",
                    "listingDate": "30th Sep 2020 4:07 am",
                    "productId": "BLACK-FOREST",
                    "quantity": 90,
                    "price": "$8.87 USD",
                    "bookmarks": 91,
                    "customer": {
                        "id": 14,
                        "firstName": "Pia",
                        "lastName": "Kemp",
                        "middleName": "Alex",
                        "nickname": "Hemp",
                        "bio": "My cool bio.",
                        "email": "piakemp13@email.com",
                        "created": "2020-01-12T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Scarborough",
                            "region": "England",
                            "country": "United Kingdom"
                        }
                    }
                },
                {
                    "id": 463,
                    "saleDate": "15th Aug 2021 10:39 pm",
                    "listingDate": "21st Jul 2021 7:44 pm",
                    "productId": "BLACK-FOREST",
                    "quantity": 24,
                    "price": "$57.57 USD",
                    "bookmarks": 80,
                    "customer": {
                        "id": 9,
                        "firstName": "Lina",
                        "lastName": "Patterson",
                        "middleName": "Jose Mari",
                        "nickname": "Lina",
                        "bio": "Da",
                        "email": "linap@email.com",
                        "created": "2010-05-14T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Funes",
                            "region": "Santa Fe",
                            "country": "Argentina"
                        }
                    }
                },
                {
                    "id": 469,
                    "saleDate": "15th Aug 2021 10:35 pm",
                    "listingDate": "31st May 2021 7:35 pm",
                    "productId": "BLACK-FOREST",
                    "quantity": 25,
                    "price": "$8.67 USD",
                    "bookmarks": 75,
                    "customer": {
                        "id": 7,
                        "firstName": "Francisca",
                        "lastName": "Bznitez",
                        "middleName": "Tessa",
                        "nickname": "Fran",
                        "bio": "Biography",
                        "email": "francisca.bznitez@example.com",
                        "created": "2021-01-01T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Bernhard Run",
                            "region": "Southland",
                            "country": "New Zealand"
                        }
                    }
                },
                {
                    "id": 147,
                    "saleDate": "15th Aug 2021 9:40 pm",
                    "listingDate": "29th Aug 2020 10:43 pm",
                    "productId": "BLACK-FOREST",
                    "quantity": 91,
                    "price": "$98.47 USD",
                    "bookmarks": 73,
                    "customer": {
                        "id": 15,
                        "firstName": "Alyce",
                        "lastName": "Gibbs",
                        "middleName": "Teddie",
                        "nickname": "Teddie",
                        "bio": "Looking for cheap teddies.",
                        "email": "alycegibbs@gmail.com",
                        "created": "2019-03-28T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Dar es Salaam",
                            "region": "Coastal Zone",
                            "country": "Tanzania"
                        }
                    }
                },
                {
                    "id": 131,
                    "saleDate": "15th Aug 2021 9:00 pm",
                    "listingDate": "9th Jun 2021 8:21 pm",
                    "productId": "BLACK-FOREST",
                    "quantity": 56,
                    "price": "$90.8 USD",
                    "bookmarks": 15,
                    "customer": {
                        "id": 14,
                        "firstName": "Pia",
                        "lastName": "Kemp",
                        "middleName": "Alex",
                        "nickname": "Hemp",
                        "bio": "My cool bio.",
                        "email": "piakemp13@email.com",
                        "created": "2020-01-12T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "homeAddress": {
                            "suburb": null,
                            "city": "Scarborough",
                            "region": "England",
                            "country": "United Kingdom"
                        }
                    }
                }
            ];
            Api.getSoldListings.mockImplementation(() => Promise.resolve(response));

            // "mock" the currency request
            wrapper.vm.$data.currencyCode = "USD";
            wrapper.vm.$data.currencySymbol = "$";

            await wrapper.vm.retrieveSoldListings();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.noResults).toBeFalsy(); // there were results
            expect(wrapper.vm.$data.soldListings.length).toEqual(response.data.length); // the max number of sold listings
            expect(wrapper.vm.$data.soldListings).toEqual(formattedListings); // with the prices and dates formatted
        })
    })

    describe("Test goToCustomerProfile method", () => {

        test("Test the goToCustomerProfile method.", async () => {
            const userId = 2;

            wrapper.vm.goToCustomerProfile(userId);

            expect($router.push).toHaveBeenCalledWith({ path: `/profile/${userId}`});
        })

    })
})
