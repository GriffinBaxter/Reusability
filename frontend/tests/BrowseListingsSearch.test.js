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

    })

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

    })

    describe('Tests the clearRadios method.', () => {

        test('Testing that when type is not business, the radio buttons have checked not set to false', () => {
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

            browseListingsSearchWrapper.vm.clearRadios('user');

            browseListingsSearchWrapper.vm.$nextTick();

            expect(nonProfitRadio.checked).toBeTruthy();
            expect(charitableRadio.checked).toBeTruthy();
            expect(retailRadio.checked).toBeTruthy();
            expect(accommodationRadio.checked).toBeTruthy();
        });

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

            browseListingsSearchWrapper.vm.setOrderByOption("priceASC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("priceASC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Price Low");
        });

        test('Testing that when orderBy is priceDESC, orderByOptionText is Price High', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("priceDESC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("priceDESC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Price High");
        });

        test('Testing that when orderBy is productNameASC, orderByOptionText is Product Name', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("productNameASC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("productNameASC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Product Name");
        });

        test('Testing that when orderBy is countryASC, orderByOptionText is Country', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("countryASC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("countryASC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Country");
        });

        test('Testing that when orderBy is cityASC, orderByOptionText is City', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("cityASC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("cityASC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("City");
        });

        test('Testing that when orderBy is expiryDateASC, orderByOptionText is Expiry Date Earliest', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("expiryDateASC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("expiryDateASC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Expiry Date Earliest");
        });

        test('Testing that when orderBy is expiryDateDESC, orderByOptionText is Expiry Date Latest', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("expiryDateDESC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("expiryDateDESC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Expiry Date Latest");
        });

        test('Testing that when orderBy is sellerNameASC, orderByOptionText is Expiry Date Latest', () => {
            browseListingsSearchWrapper.vm.orderByOption = "None";
            browseListingsSearchWrapper.vm.orderByOptionText = "None";

            browseListingsSearchWrapper.vm.setOrderByOption("sellerNameASC");
            expect(browseListingsSearchWrapper.vm.orderByOption).toBe("sellerNameASC");
            expect(browseListingsSearchWrapper.vm.orderByOptionText).toBe("Business Name");
        });
    })

    describe('Tests the validatePriceInput method.', () => {

        test('Testing that when one of the input prices is null, then true is returned', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput(null, 20)).toBeTruthy();
        });

        test('Testing that when neither of the input prices are null and firstPrice is less than secondPrice then true is returned', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput(10, 20)).toBeTruthy();
        });

        test('Testing that when neither of the input prices are null and firstPrice is equal to secondPrice then true is returned', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput(20, 20)).toBeTruthy();
        });

        test('Testing that when neither of the input prices are null and firstPrice is greater than secondPrice then false is returned', () => {
            expect(browseListingsSearchWrapper.vm.validatePriceInput(20, 10)).toBeFalsy();
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

    })

})
