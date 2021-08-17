/**
 * Jest tests for Marketplace.vue.
 * @jest-environment jsdom
 */

import {createLocalVue, shallowMount} from '@vue/test-utils'
import {afterEach, beforeAll, beforeEach, describe, expect, jest, test} from "@jest/globals";

import BrowseListingsSearch from "../src/components/listing/BrowseListingsSearch";
import Api from "../src/Api"
import VueRouter from "vue-router";

jest.mock("../src/Api");

describe("Testing the BrowseListingsSearch methods", () => {

    let browseListingsSearchWrapper;
    let data;
    let $route;
    let browseListingApiResponse;
    let searchClickedSpy;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/browseListings',
            name: 'BrowseListing',
            query: {
                searchQuery: null, searchType: null,
                orderBy: null, page: null, businessTypes: [],
                minimumPrice: null, maximumPrice: null,
                fromDate: null, toDate: null
            }
        }
        data = {
            status: 200,
            data: [
                {

                }],
            headers: {"total-pages": 5}

        }

        Api.searchListings.mockImplementation(() => Promise.resolve(data));

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

        browseListingsSearchWrapper = shallowMount(
            BrowseListingsSearch,
            {
                mocks: {
                    $router,
                    $route
                }
            });

        Promise.resolve();

        searchClickedSpy = jest.spyOn(BrowseListingsSearch.methods, 'searchClicked')

    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    describe('Tests the enterPressed method.', () => {

        test("Testing that when a button other than the enter button is pressed the search is not executed", async () => {

            Api.searchUsers.mockImplementation( () => Promise.resolve(browseListingApiResponse) ); // here since async

            let searchBar = browseListingsSearchWrapper.find('#search-bar');
            searchBar.trigger('keydown.space');

            const event = new Event("keydown.space", undefined)
            browseListingsSearchWrapper.vm.enterPressed(event)

            await Promise.resolve();

            expect(searchClickedSpy).toHaveBeenCalledTimes(0);

        })

    })

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

        test('Testing that pressing enter populates the URL correctly', async() => {
            let inputQuery = 'Browse Listings Search Enter Test';
            let expectedQuery = 'Browse%20Listings%20Search%20Enter%20Test';
            browseListingsSearchWrapper.vm.$refs.searchInput.value = inputQuery;

            let searchBar = browseListingsSearchWrapper.find('#search-bar');
            searchBar.trigger('keydown.enter');

            await browseListingsSearchWrapper.vm.$nextTick();

            expect(router.currentRoute.name).toBe('BrowseListings')
            expect(router.currentRoute.fullPath).toBe(`/browseListings?searchQuery=${expectedQuery}&searchType&orderBy=priceASC&page=1&minimumPrice&maximumPrice&fromDate&toDate`)
        });

        test('Testing that clicking the search button populates the URL correctly', () => {
            let inputQuery = 'User Search Click Test';
            let expectedQuery = 'User%20Search%20Click%20Test';
            browseListingsSearchWrapper.vm.$refs.searchInput.value = inputQuery;

            browseListingsSearchWrapper.vm.$nextTick().then(() => {
                let searchButton = browseListingsSearchWrapper.find('#search-button');
                searchButton.trigger('click');

                expect(router.currentRoute.name).toBe('BrowseListings')
                expect(router.currentRoute.fullPath).toBe(`/browseListings?searchQuery=${expectedQuery}&searchType&orderBy=priceASC&page=1&minimumPrice&maximumPrice&fromDate&toDate`)
            });
        });
    })

    describe('Tests the clearRadios method.', () => {

        test('Testing that when type is business, all radio buttons have checked set to false', () => {
            let nonProfitRadio = browseListingsSearchWrapper.find('#radio-non-profit');
            nonProfitRadio.trigger('click');
            let charitableRadio = browseListingsSearchWrapper.find('#radio-charitable');
            charitableRadio.trigger('click');
            let retailRadio = browseListingsSearchWrapper.find('#radio-retail');
            retailRadio.trigger('click');
            let accommodationRadio = browseListingsSearchWrapper.find('#radio-accommodation');
            accommodationRadio.trigger('click');

            jest.spyOn(document, 'querySelector').mockImplementation(() => {
                return [nonProfitRadio, charitableRadio, retailRadio, accommodationRadio];
            });

            browseListingsSearchWrapper.vm.$nextTick();

            browseListingsSearchWrapper.vm.clearRadios('business');

            browseListingsSearchWrapper.vm.$nextTick();

            expect(nonProfitRadio.checked).toBeFalsy();
            expect(charitableRadio.checked).toBeFalsy();
            expect(retailRadio.checked).toBeFalsy();
            expect(accommodationRadio.checked).toBeFalsy();
        });
    })

    describe('Tests the setOrderByOption method.', () => {

        test('Testing that when orderBy is priceASC, orderByOptionText is Price Low', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("price");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("price");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Price");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Low Price");
        });

        test('Testing that when orderBy is productNameASC, orderByOptionText is Product Name', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("productName");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("productName");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Product Name");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });

        test('Testing that when orderBy is countryASC, orderByOptionText is Country', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("country");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("country");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Country");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });

        test('Testing that when orderBy is cityASC, orderByOptionText is City', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("city");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("city");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("City");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });

        test('Testing that when orderBy is expiryDateASC, orderByOptionText is Expiry Date Earliest', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("expiryDate");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("expiryDate");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Expiry Date");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Earliest");
        });

        test('Testing that when orderBy is sellerNameASC, orderByOptionText is Expiry Date Latest', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("sellerName");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("sellerName");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Business Name");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });
    })

    describe('Tests the setOrderBySequence method.', () => {

        test('Testing that when orderBy is price ASC, orderBySequenceText is From Low Price', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("price");
            browseListingsSearchWrapper.vm.setOrderBySequence("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Low Price");
        });

        test('Testing that when orderBy is price DESC, orderBySequenceText is From Height Price', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("price");
            browseListingsSearchWrapper.vm.setOrderBySequence("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Height Price");
        });

        test('Testing that when orderBy is productName ASC, orderBySequenceText is From A To Z', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("productName");
            browseListingsSearchWrapper.vm.setOrderBySequence("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });

        test('Testing that when orderBy is productName DESC, orderBySequenceText is From Z To A', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("productName");
            browseListingsSearchWrapper.vm.setOrderBySequence("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Z To A");
        });

        test('Testing that when orderBy is country ASC, orderBySequenceText is From A To Z', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("country");
            browseListingsSearchWrapper.vm.setOrderBySequence("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });

        test('Testing that when orderBy is country DESC, orderBySequenceText is From Z To A', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("country");
            browseListingsSearchWrapper.vm.setOrderBySequence("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Z To A");
        });

        test('Testing that when orderBy is city ASC, orderBySequenceText is From A To Z', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("city");
            browseListingsSearchWrapper.vm.setOrderBySequence("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });

        test('Testing that when orderBy is city DESC, orderBySequenceText is From Z To A', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("city");
            browseListingsSearchWrapper.vm.setOrderBySequence("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Z To A");
        });

        test('Testing that when orderBy is expiryDate ASC, orderBySequenceText is From Earliest', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("expiryDate");
            browseListingsSearchWrapper.vm.setOrderBySequence("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Earliest");
        });

        test('Testing that when orderBy is expiryDate DESC, orderBySequenceText is From Latest', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("expiryDate");
            browseListingsSearchWrapper.vm.setOrderBySequence("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Latest");
        });

        test('Testing that when orderBy is sellerName ASC, orderBySequenceText is From A To Z', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("sellerName");
            browseListingsSearchWrapper.vm.setOrderBySequence("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("ASC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From A To Z");
        });

        test('Testing that when orderBy is sellerName DESC, orderBySequenceText is From Z To A', () => {
            browseListingsSearchWrapper.vm.orderBySequence = "None";
            browseListingsSearchWrapper.vm.orderBySequenceText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("sellerName");
            browseListingsSearchWrapper.vm.setOrderBySequence("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequence).toBe("DESC");
            expect(browseListingsSearchWrapper.vm.orderBySequenceText).toBe("From Z To A");
        });
    })

    describe('Tests the validatePriceInput method.', () => {

        test('Testing when one of the input prices is null', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput(null, "20")).toStrictEqual(["0", "20"]);
        });

        test('Testing when neither of the input prices are null and firstPrice is less than secondPrice', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput("10", "20")).toStrictEqual(["10", "20"]);
        });

        test('Testing when neither of the input prices are null and firstPrice is equal to secondPrice', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput("20", "20")).toStrictEqual(["20", "20"]);
        });

        test('Testing when neither of the input prices are null and firstPrice is greater than secondPrice', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput("20", "10")).toStrictEqual(["10", "20"]);
        });

        test('Testing when the lowest price is negative', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput("-20", "10")).toStrictEqual(["0", "10"]);
        });

        test('Testing when the highest price is negative', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput("20", "-10")).toStrictEqual(["0", "20"]);
        });
    })

    describe('Tests the validateDateInput method.', () => {

        test('Testing that when one of the input dates is null, then true is returned', () => {
            expect(browseListingsSearchWrapper.vm.validateDateInput(null, '2020-01-25')).toBeTruthy();
        });

        test('Testing that when neither of the input dates are null and firstDate is earlier than secondDate then true is returned', () => {
            expect(browseListingsSearchWrapper.vm.validateDateInput('2020-01-24', '2020-01-25')).toBeTruthy();
        });

        test('Testing that when neither of the input dates are null and firstDate is the same as secondDate then false is returned', () => {
            expect(browseListingsSearchWrapper.vm.validateDateInput('2020-01-25', '2020-01-25')).toBeFalsy();
        });

        test('Testing that when neither of the input dates are null and firstDate is later than secondDate then false is returned', () => {
            expect(browseListingsSearchWrapper.vm.validateDateInput('2020-01-28', '2020-01-25')).toBeFalsy();
        });
    })

    describe('Tests the clearFilters method.', () => {

        test('Testing that clearFilters clears the filters', () => {
            browseListingsSearchWrapper.vm.lowestPrice = "price";
            browseListingsSearchWrapper.vm.highestPrice = "price";
            browseListingsSearchWrapper.vm.startDate = "date";
            browseListingsSearchWrapper.vm.endDate = "date";

            expect(browseListingsSearchWrapper.vm.lowestPrice).toBe("price");
            expect(browseListingsSearchWrapper.vm.highestPrice).toBe("price");
            expect(browseListingsSearchWrapper.vm.startDate).toBe("date");
            expect(browseListingsSearchWrapper.vm.endDate).toBe("date");

            browseListingsSearchWrapper.vm.clearFilters();

            expect(browseListingsSearchWrapper.vm.lowestPrice).toBeNull();
            expect(browseListingsSearchWrapper.vm.highestPrice).toBeNull();
            expect(browseListingsSearchWrapper.vm.startDate).toBeNull();
            expect(browseListingsSearchWrapper.vm.endDate).toBeNull();
        });
    })

    describe('Tests the searchClicked method.', () => {

        let browseListingsSearchWrapper;
        let $router;
        let $route;

        beforeEach(() => {
            $router = {
                push: jest.fn()
            };
            $route = {
                query: {
                    orderBy: "priceASC"
                }
            };
            browseListingsSearchWrapper = shallowMount(BrowseListingsSearch, {
                mocks: {
                    $router,
                    $route
                }
            });
        });

        test('Testing that the grid data is populated correctly when data is returned', async () => {
            browseListingsSearchWrapper.vm.$refs.searchInput.value = "test";
            browseListingsSearchWrapper.vm.$data.orderBy ="priceASC";
            browseListingsSearchWrapper.vm.$data.page = 1;
            browseListingsSearchWrapper.vm.$data.lowestPrice = 10;
            browseListingsSearchWrapper.vm.$data.highestPrice = 100;
            browseListingsSearchWrapper.vm.$data.startDate = "2020-01-24";
            browseListingsSearchWrapper.vm.$data.endDate = "2020-05-28";

            const searchQuery = "test";
            const orderBy ="priceASC";
            const page = 1;
            const minimumPrice = 10;
            const maximumPrice = 100;
            const fromDate = "2020-01-24T00:00";
            const toDate = "2020-05-28T00:00";

            Api.searchListings.mockImplementation( () => Promise.resolve(browseListingApiResponse) );

            browseListingsSearchWrapper.vm.searchClicked();

            await browseListingsSearchWrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({ path: `/browseListings`,
                query: { searchQuery: searchQuery, searchType: null,
                orderBy: orderBy, page: page, businessTypes: [],
                minimumPrice: minimumPrice, maximumPrice: maximumPrice,
                fromDate: fromDate, toDate: toDate }})
        });

        test('Testing that the grid data is populated correctly when data is returned and the prices are swapped around ' +
            'when they are out of order', async () => {
            browseListingsSearchWrapper.vm.$refs.searchInput.value = "test";
            browseListingsSearchWrapper.vm.$data.orderBy ="priceASC";
            browseListingsSearchWrapper.vm.$data.page = 1;
            browseListingsSearchWrapper.vm.$data.lowestPrice = 100;
            browseListingsSearchWrapper.vm.$data.highestPrice = 10;
            browseListingsSearchWrapper.vm.$data.startDate = "2020-01-24";
            browseListingsSearchWrapper.vm.$data.endDate = "2020-05-28";

            const searchQuery = "test";
            const orderBy ="priceASC";
            const page = 1;
            const minimumPrice = 10;
            const maximumPrice = 100;
            const fromDate = "2020-01-24T00:00";
            const toDate = "2020-05-28T00:00";

            Api.searchListings.mockImplementation( () => Promise.resolve(browseListingApiResponse) );

            browseListingsSearchWrapper.vm.searchClicked();

            await browseListingsSearchWrapper.vm.$nextTick()

            expect($router.push).toHaveBeenCalledWith({ path: `/browseListings`,
                query: { searchQuery: searchQuery, searchType: null,
                    orderBy: orderBy, page: page, businessTypes: [],
                    minimumPrice: minimumPrice, maximumPrice: maximumPrice,
                    fromDate: fromDate, toDate: toDate }})
        })


        test('Testing that the grid data is populated correctly when data is returned and the dates are swapped around ' +
            'when they are out of order', async () => {
            browseListingsSearchWrapper.vm.$refs.searchInput.value = "test";
            browseListingsSearchWrapper.vm.$data.orderBy ="priceASC";
            browseListingsSearchWrapper.vm.$data.page = 1;
            browseListingsSearchWrapper.vm.$data.lowestPrice = 10;
            browseListingsSearchWrapper.vm.$data.highestPrice = 100;
            browseListingsSearchWrapper.vm.$data.startDate = "2020-05-28";
            browseListingsSearchWrapper.vm.$data.endDate = "2020-01-24";

            const searchQuery = "test";
            const orderBy ="priceASC";
            const page = 1;
            const minimumPrice = 10;
            const maximumPrice = 100;
            const fromDate = "2020-01-24T00:00";
            const toDate = "2020-05-28T00:00";

            Api.searchListings.mockImplementation( () => Promise.resolve(browseListingApiResponse) );

            browseListingsSearchWrapper.vm.searchClicked();

            await browseListingsSearchWrapper.vm.$nextTick()

            expect($router.push).toHaveBeenCalledWith({ path: `/browseListings`,
                query: { searchQuery: searchQuery, searchType: null,
                    orderBy: orderBy, page: page, businessTypes: [],
                    minimumPrice: minimumPrice, maximumPrice: maximumPrice,
                    fromDate: fromDate, toDate: toDate }})
        })
    })
})

describe('Tests the getSelectedRadio method.', () => {

    let browseListingsSearchWrapper;
    let $route;

    let radioProductNameButton;
    let radioSellerLocationButton;
    let radioSellerNameButton;
    let radioNonProfitButton;
    let radioCharitableButton;
    let radioRetailTradeButton;
    let radioAccommodationButton;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/browseListings',
            name: 'BrowseListing',
            query: {
                searchQuery: null, searchType: null,
                orderBy: null, page: null, businessTypes: [],
                minimumPrice: null, maximumPrice: null,
                fromDate: null, toDate: null
            }
        }

        browseListingsSearchWrapper = shallowMount(
            BrowseListingsSearch,
            {
                mocks: {
                    $router,
                    $route
                }
            });

        radioProductNameButton = browseListingsSearchWrapper.find('#radio-product-name');
        radioSellerLocationButton = browseListingsSearchWrapper.find('#radio-seller-location');
        radioSellerNameButton = browseListingsSearchWrapper.find('#radio-seller-name');

        radioNonProfitButton = browseListingsSearchWrapper.find('#radio-non-profit');
        radioCharitableButton = browseListingsSearchWrapper.find('#radio-charitable');
        radioRetailTradeButton = browseListingsSearchWrapper.find('#radio-retail');
        radioAccommodationButton = browseListingsSearchWrapper.find('#radio-accommodation');

        expect(radioProductNameButton.exists()).toBeTruthy();
        expect(radioSellerLocationButton.exists()).toBeTruthy();
        expect(radioSellerNameButton.exists()).toBeTruthy();
        expect(radioNonProfitButton.exists()).toBeTruthy();
        expect(radioCharitableButton.exists()).toBeTruthy();
        expect(radioRetailTradeButton.exists()).toBeTruthy();
        expect(radioAccommodationButton.exists()).toBeTruthy();

        // mock the call which gets the radio buttons
        jest.spyOn(document, 'querySelectorAll').mockImplementation((selector) => {
            switch (selector) {
                case "input[name='match-radios']":
                    return [radioProductNameButton.element, radioSellerLocationButton.element, radioSellerNameButton.element];
                case "input[name='business-type-radios']":
                    return [radioNonProfitButton.element, radioCharitableButton.element, radioRetailTradeButton.element, radioAccommodationButton.element];
                default:
                    return null;
            }
        });
    });

    afterEach(() => {
        let nonProfitRadio = browseListingsSearchWrapper.find('#radio-non-profit');
        let charitableRadio = browseListingsSearchWrapper.find('#radio-charitable');
        let retailRadio = browseListingsSearchWrapper.find('#radio-retail');
        let accommodationRadio = browseListingsSearchWrapper.find('#radio-accommodation');

        jest.spyOn(document, 'querySelector').mockImplementation(() => {
            return [nonProfitRadio, charitableRadio, retailRadio, accommodationRadio];
        });

        browseListingsSearchWrapper.vm.$nextTick();

        browseListingsSearchWrapper.vm.clearRadios('business');

        browseListingsSearchWrapper.vm.$nextTick();

        expect(nonProfitRadio.checked).toBeFalsy();
        expect(charitableRadio.checked).toBeFalsy();
        expect(retailRadio.checked).toBeFalsy();
        expect(accommodationRadio.checked).toBeFalsy();
    });

    test("Testing that when the Product Name radio button is checked that it is correctly selected", async () => {
        const type = 'match';
        browseListingsSearchWrapper.vm.searchType = 'businessName';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('businessName');

        // trigger the button click which selects the radio button
        radioProductNameButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('listingName');
    })

    test("Testing that when the Business Name radio button is checked that it is correctly selected", async () => {
        const type = 'match';
        browseListingsSearchWrapper.vm.searchType = 'listingName';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('listingName');

        // trigger the button click which selects the radio button
        radioSellerNameButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('businessName');
    })

    test("Testing that when the Business Location radio button is checked that it is correctly selected", async () => {
        const type = 'match';
        browseListingsSearchWrapper.vm.searchType = 'businessName';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('businessName');

        // trigger the button click which selects the radio button
        radioSellerLocationButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('location');
    })

    test("Testing that when the NON_PROFIT_ORGANISATION radio button is checked that it is correctly selected", async () => {
        const type = 'business';
        browseListingsSearchWrapper.vm.searchType = 'CHARITABLE_ORGANISATION"';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('CHARITABLE_ORGANISATION"');

        // trigger the button click which selects the radio button
        radioNonProfitButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual(['NON_PROFIT_ORGANISATION']);
    })

    test("Testing that when the CHARITABLE_ORGANISATION radio button is checked that it is correctly selected", async () => {
        const type = 'business';
        browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

        // trigger the button click which selects the radio button
        radioCharitableButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual(['CHARITABLE_ORGANISATION']);
    })

    test("Testing that when the RETAIL_TRADE radio button is checked that it is correctly selected", async () => {
        const type = 'business';
        browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

        // trigger the button click which selects the radio button
        radioRetailTradeButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual(['RETAIL_TRADE']);
    })

    test("Testing that when the ACCOMMODATION_AND_FOOD_SERVICES radio button is checked that it is correctly selected", async () => {
        const type = 'business';
        browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

        // trigger the button click which selects the radio button
        radioAccommodationButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual(['ACCOMMODATION_AND_FOOD_SERVICES']);
    })

    test("Testing that when two business type radio buttons are checked that they are correctly selected", async () => {
        const type = 'business';
        browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

        // trigger the button click which selects the radio button
        radioRetailTradeButton.trigger('click');
        radioAccommodationButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual(['RETAIL_TRADE', 'ACCOMMODATION_AND_FOOD_SERVICES']);    })

    test("Testing that when three business type radio buttons are checked that they are correctly selected", async () => {
        const type = 'business';
        browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

        // trigger the button click which selects the radio button
        radioRetailTradeButton.trigger('click');
        radioAccommodationButton.trigger('click');
        radioCharitableButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual(['CHARITABLE_ORGANISATION', 'RETAIL_TRADE', 'ACCOMMODATION_AND_FOOD_SERVICES']);
    })

    test("Testing that when four business type radio buttons are checked that they are correctly selected", async () => {
        const type = 'business';
        browseListingsSearchWrapper.vm.searchType = 'NON_PROFIT_ORGANISATION"';
        expect(browseListingsSearchWrapper.vm.searchType).toEqual('NON_PROFIT_ORGANISATION"');

        // trigger the button click which selects the radio button
        radioRetailTradeButton.trigger('click');
        radioAccommodationButton.trigger('click');
        radioCharitableButton.trigger('click');
        radioNonProfitButton.trigger('click');

        await browseListingsSearchWrapper.vm.$nextTick();

        expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual(['NON_PROFIT_ORGANISATION', 'CHARITABLE_ORGANISATION', 'RETAIL_TRADE', 'ACCOMMODATION_AND_FOOD_SERVICES']);
    })
})
