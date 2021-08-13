/**
 * Jest tests for Marketplace.vue.
 * @jest-environment jsdom
 */

import { shallowMount } from '@vue/test-utils'
import {afterEach, beforeAll, describe, expect, jest} from "@jest/globals";

import BrowseListingsSearch from "../src/components/listing/BrowseListingsSearch";
import Api from "../src/Api"

jest.mock("../src/Api");

describe("Testing the BrowseListingsSearch methods", () => {

    let browseListingsSearchWrapper;
    let data;
    let $route;
    let browseListingsSpy;
    let browseListingApiResponse;
    let generalMockApiResponse;
    let emptyMockApiResponse;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/browseListings',
            query: {}
        }
        data = {
            status: 200,
            data: [
                {

                }],
            headers: {"total-pages": 5}

        }

        Api.searchListings.mockImplementation(() => Promise.resolve(data));

        generalMockApiResponse = {
            status: 200,
            response: {
                status: 400
            },
            data: [],
            message: "",
            headers: {
                "total-rows": 0,
                "total-pages": 0
            }
        }

        browseListingApiResponse = {
            status: 200,
            headers: {
                "total-rows": 1,
                "total-pages": 1
            },
            data: [{
                "closes": "2021-06-10T00:00",
                "created": "2021-05-12T00:00",
                "id": 23,
                "inventoryItem": {
                    "bestBefore": "2021-06-12",
                    "expires": "2021-06-12",
                    "id": 24,
                    "manufactured": "2021-05-12",
                    "pricePerItem": 2.99,
                    "product": {
                        "barcode": null,
                        "business": {
                            "address": {
                                "city": "Chaoyang District",
                                "country": "China",
                                "postcode": "100102",
                                "region": "Beijing",
                                "streetName": "Wangjing Zhonghuan Nanlu",
                                "streetNumber": "7",
                                "suburb": null
                            },
                            "administrators": {
                                "bio": "Biography",
                                "businessesAdministered": [null],
                                "created": "2021-01-08T00:00",
                                "dateOfBirth": "2008-09-10",
                                "email": "francisca.benitez@example.com",
                                "firstName": "Francisca",
                                "homeAddress": {
                                    "city": "Bururi",
                                    "country": "Africa",
                                    "postcode": "1000",
                                    "region": "Bigomogomo",
                                    "streetName": "Monique Vista",
                                    "streetNumber": "9205",
                                    "suburb": null
                                },
                                "id": 6,
                                "lastName": "Benitez",
                                "middleName": "Tina",
                                "nickname": "Fran",
                                "phoneNumber": "12334456",
                                "role": "USER"
                            },
                            "businessType": "RETAIL_TRADE",
                            "created": "2021-02-01T00:00",
                            "description": "Description",
                            "id": 3,
                            "name": "Fringe Wasteless",
                            "primaryAdministratorId": 6
                        },
                        "created": "2021-05-12T00:00",
                        "description": "Crunchy biscuits made with rolled oats golden syrup and coconut with a coating of delicious milk chocolate.",
                        "id": "ARNOTTS-CSNAP",
                        "images": [],
                        "manufacturer": "Arnotts",
                        "name": "Arnotts Chocolate Biscuits Butternut Snap",
                        "recommendedRetailPrice": 2.99
                    },
                    "quantity": 44,
                    "sellBy": "2021-06-10",
                },
                "isBookmarked": false,
                "moreInfo": "Selling quick.",
                "price": 11.96,
                "quantity": 4,
                "totalBookmarks": 0,
            }]
        }

        emptyMockApiResponse = {
            status: 200,
            data: []
        }

        browseListingsSearchWrapper = shallowMount(
            BrowseListingsSearch,
            {
                mocks: {
                    $router,
                    $route
                }
            });

        Promise.resolve();

        browseListingsSpy = jest.spyOn(Api, 'searchListings');

    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    describe('Tests the enterPressed method.', () => {

        test("Testing that when the enter button is pressed that the search is executed", () => {

            let searchBar = browseListingsSearchWrapper.find('#search-bar');
            searchBar.trigger('keydown.enter');

            const event = new Event("keydown.enter", undefined)
            browseListingsSearchWrapper.vm.enterPressed(event)

            Api.searchUsers.mockImplementation( () => Promise.resolve(browseListingApiResponse) );

        })


    }),

    describe('Tests the getSelectedRadio method.', () => {

        test("Testing that when the Product Name radio button is checked that it is correctly selected", async () => {

            await browseListingsSearchWrapper.vm.$nextTick();
            const type = 'match';
            browseListingsSearchWrapper.vm.searchType = 'Business Name';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('Business Name');

            let radioButton = browseListingsSearchWrapper.find('#radio-product-name');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            await browseListingsSearchWrapper.vm.$nextTick();


            expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('Product Name');

        })


    }),

    describe('Tests the searchClicked method.', () => {

    }),

    describe('Tests the clearRadios method.', () => {

    }),

    describe('Tests the setOrderByOption method.', () => {

    }),

    describe('Tests the validatePriceInput method.', () => {

    }),

    describe('Tests the validateDateInput method.', () => {

    }),

    describe('Tests the clearFilters method.', () => {

    })

})
