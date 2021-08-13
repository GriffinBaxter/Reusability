/**
 * Jest tests for Marketplace.vue.
 * @jest-environment jsdom
 */

import {createLocalVue, shallowMount} from '@vue/test-utils'
import {afterEach, beforeAll, describe, expect, jest, test} from "@jest/globals";

import BrowseListingsSearch from "../src/components/listing/BrowseListingsSearch";
import Api from "../src/Api"
import VueRouter from "vue-router";

jest.mock("../src/Api");

describe("Testing the BrowseListingsSearch methods", () => {

    let browseListingsSearchWrapper;
    let data;
    let $route;
    let browseListingsSpy;
    let browseListingApiResponse;
    let generalMockApiResponse;
    let emptyMockApiResponse;
    let searchClickedSpy;

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

        searchClickedSpy = jest.spyOn(BrowseListingsSearch.methods, 'searchClicked')

    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    describe('Tests the enterPressed method.', () => {

        test("Testing that when the enter button is pressed that the search is executed", async () => {

            Api.searchUsers.mockImplementation( () => Promise.resolve(browseListingApiResponse) ); // here since async

            let searchBar = browseListingsSearchWrapper.find('#search-bar');
            await searchBar.trigger('keydown.enter');

            await Promise.resolve();

            const event = new Event("keydown.enter", undefined)
            await browseListingsSearchWrapper.vm.enterPressed(event)

            await Promise.resolve();

            await browseListingsSearchWrapper.vm.$nextTick();

            const searchClicked = jest.fn(() => browseListingsSearchWrapper.vm.searchClicked())
            expect(searchClicked.mock.calls.length).toBe(1)

            // expect(searchClickedSpy).toHaveBeenCalled();

        })

        test("Testing that when a button other than the enter button is pressed the search is not executed", async () => {

            Api.searchUsers.mockImplementation( () => Promise.resolve(browseListingApiResponse) ); // here since async

            let searchBar = browseListingsSearchWrapper.find('#search-bar');
            searchBar.trigger('keydown.space');

            const event = new Event("keydown.space", undefined)
            browseListingsSearchWrapper.vm.enterPressed(event)

            await Promise.resolve();

            expect(searchClickedSpy).toHaveBeenCalledTimes(0);

        })

    }),

    describe('Tests the getSelectedRadio method.', () => {

        test("Testing that when the Product Name radio button is checked that it is correctly selected", () => {

            const type = 'match';
            browseListingsSearchWrapper.vm.searchType = 'Business Name';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('Business Name');

            let radioButton = browseListingsSearchWrapper.find('#radio-product-name');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('Product Name');
            })
        })

        test("Testing that when the Business Name radio button is checked that it is correctly selected", () => {

            const type = 'match';
            browseListingsSearchWrapper.vm.searchType = 'Product Name';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('Product Name');

            let radioButton = browseListingsSearchWrapper.find('#radio-seller-name');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('Business Name');
            })
        })

        test("Testing that when the Business Location radio button is checked that it is correctly selected", () => {

            const type = 'match';
            browseListingsSearchWrapper.vm.searchType = 'Business Name';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('Business Name');

            let radioButton = browseListingsSearchWrapper.find('#radio-seller-location');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('Business Location');
            })
        })

        test("Testing that when the NON_PROFIT_ORGANISATION radio button is checked that it is correctly selected", () => {

            const type = 'business';
            browseListingsSearchWrapper.vm.searchType = 'CHARITABLE_ORGANISATION"';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('CHARITABLE_ORGANISATION"');

            let radioButton = browseListingsSearchWrapper.find('#radio-non-profit');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('NON_PROFIT_ORGANISATION');
            })
        })

        test("Testing that when the CHARITABLE_ORGANISATION radio button is checked that it is correctly selected", () => {

            const type = 'business';
            browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

            let radioButton = browseListingsSearchWrapper.find('#radio-non-profit');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('CHARITABLE_ORGANISATION');
            })
        })

        test("Testing that when the RETAIL_TRADE radio button is checked that it is correctly selected", () => {

            const type = 'business';
            browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

            let radioButton = browseListingsSearchWrapper.find('#radio-non-profit');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('RETAIL_TRADE');
            })
        })

        test("Testing that when the ACCOMMODATION_AND_FOOD_SERVICES radio button is checked that it is correctly selected", () => {

            const type = 'business';
            browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

            let radioButton = browseListingsSearchWrapper.find('#radio-non-profit');
            expect(radioButton.exists()).toBeTruthy();
            radioButton.trigger('click');

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('ACCOMMODATION_AND_FOOD_SERVICES');
            })
        })

    }),

    describe('Tests the URL populates correctly when searching for all relevant listings.', () => {

        let browseListingsSearchWrapper;
        let router;

        beforeAll(() => {
            const localVue = createLocalVue();
            localVue.use(VueRouter)

            const routes = [{path: '/browseListings', component: BrowseListingsSearch, name: 'BrowseListings'}]
            router = new VueRouter({
                routes
            })
            router.push({
                name: 'BrowseListings',
            })
            browseListingsSearchWrapper = shallowMount(BrowseListingsSearch, {
                localVue,
                router
            });
        });

        test('Testing that pressing enter populates the URL correctly', () => {

            browseListingsSearchWrapper.vm.searchType = "Product Name";

            let inputQuery = 'Browse Listings Search Enter Test';
            let expectedQuery = 'Browse%20Listings%20Search%20Enter%20Test';
            browseListingsSearchWrapper.vm.$refs.searchInput.value = inputQuery;

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                let searchBar = browseListingsSearchWrapper.find('#search-bar');
                searchBar.trigger('keydown.enter');

                expect(router.currentRoute.name).toBe('BrowseListings')
                expect(router.currentRoute.fullPath).toBe(`/browseListings?searchQuery=${expectedQuery}&searchType=listingName&orderBy=priceASC&page=0&businessType&minimumPrice&maximumPrice&fromDate&toDate`)

            });
        });


        test('Testing that clicking the search button populates the URL correctly', () => {

            browseListingsSearchWrapper.vm.searchType = 'User';

            let inputQuery = 'User Search Click Test';
            let expectedQuery = 'User%20Search%20Click%20Test';
            browseListingsSearchWrapper.vm.$refs.searchInput.value = inputQuery;

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                let searchButton = browseListingsSearchWrapper.find('#search-button');
                searchButton.trigger('click');

                expect(router.currentRoute.name).toBe('BrowseListings')
                expect(router.currentRoute.fullPath).toBe(`/browseListings?searchQuery=${expectedQuery}&searchType=listingName&orderBy=priceASC&page=0&businessType&minimumPrice&maximumPrice&fromDate&toDate`)
            });
        });

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
