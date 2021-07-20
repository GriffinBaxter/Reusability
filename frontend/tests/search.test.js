/**
 * Jest tests for Search.vue.
 * @jest-environment jsdom
 */

import {jest, describe, expect, test, beforeEach, afterEach} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import Search from "../src/views/Search";
import Api from "../src/Api";

jest.mock("../src/Api");

describe("Testing the Search page functionality", () => {

    let searchWrapper;
    let $router;
    let $route;
    let searchUsersSpy;
    let searchBusinessesSpy;
    let requestUsersSpy;
    let requestBusinessesSpy;
    let userMockApiResponse;
    let businessMockApiResponse;
    let emptyMockApiResponse;
    let generalMockApiResponse;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };
        $route = {
            query: {
                type: 'User',
                searchQuery: 'Search'
            }
        };

        searchUsersSpy = jest.spyOn(Api, 'searchUsers');
        searchBusinessesSpy = jest.spyOn(Api, 'searchBusinesses');

        requestUsersSpy = jest.spyOn(Search.methods, 'requestUsers');
        requestBusinessesSpy = jest.spyOn(Search.methods, 'requestBusinesses');

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

        Api.searchBusinesses.mockImplementation( () => Promise.resolve(generalMockApiResponse) );
        Api.searchUsers.mockImplementation( () => Promise.resolve(generalMockApiResponse) );

        searchWrapper = shallowMount(Search, {
            mocks: {
                $router,
                $route
            }
        });

        userMockApiResponse = {
            status: 200,
            headers: {
                "total-rows": 1,
                "total-pages": 1
            },
            data: [{
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
                        {
                            "id": 100,
                            "administrators": [
                                "string"
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
                            "businessType": "ACCOMMODATION_AND_FOOD_SERVICES",
                            "created": "2020-07-14T14:52:00Z"
                        }
                    ]}]
        }

        businessMockApiResponse = {
            status: 200,
            headers: {
                "total-rows": 1,
                "total-pages": 1
            },
            data: [{
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
                    "businessType": "ACCOMMODATION_AND_FOOD_SERVICES",
                    "created": "2020-07-14T14:52:00Z"
                }]
        }

        emptyMockApiResponse = {
            status: 200,
            data: []
        }
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    describe("Testing the updatePage method", () => {

        test("Testing that the router push is called correctly when the search type is User", () => {
            searchWrapper.vm.searchType = 'User';
            searchWrapper.vm.query = 'Search';
            searchWrapper.vm.orderByString = 'fullNameDESC';

            let event = {
                newPageNumber: 3
            };

            searchWrapper.vm.updatePage(event);

            expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `User`, searchQuery: 'Search', orderBy: 'fullNameDESC', page: (event.newPageNumber + 1).toString()}});
            // 2 as it was called once during the shallowMount, and once during updatePage
            expect(requestUsersSpy).toHaveBeenCalledTimes(2);
        });

        test("Testing that the router push is called correctly when the search type is Business", () => {
            searchWrapper.vm.searchType = 'Business';
            searchWrapper.vm.query = 'Search';
            searchWrapper.vm.orderByString = 'nameDESC';

            let event = {
                newPageNumber: 2
            };

            searchWrapper.vm.updatePage(event);

            expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, searchQuery: 'Search', businessType: 'Any', orderBy: 'nameDESC', page: (event.newPageNumber + 1).toString()}});
            expect(requestBusinessesSpy).toHaveBeenCalledTimes(1);
        });

        test("Testing that the router push is called with /pageDoesNotExist when the search type isn't User or Business", () => {
            searchWrapper.vm.searchType = 'Other';

            let event = {
                newPageNumber: 2
            };

            searchWrapper.vm.updatePage(event);

            expect($router.push).toHaveBeenCalledWith('/pageDoesNotExist');
            // 1 as it was called once during shallowMount
            expect(requestUsersSpy).toHaveBeenCalledTimes(1);
            expect(requestBusinessesSpy).toHaveBeenCalledTimes(0);
        });
    });

    describe("Testing the parseOrderBy method", () => {

        test("Testing that when the orderByString ends in ASC that isAscending is returned as true", () => {
            searchWrapper.vm.orderByString = searchWrapper.vm.$data.orderByUserHeaders[0] + 'ASC';
            searchWrapper.vm.$data.tableOrderByHeaders = searchWrapper.vm.$data.orderByUserHeaders;

            let orderBy = searchWrapper.vm.parseOrderBy();

            expect(orderBy.orderBy).toBe(0);
            expect(orderBy.isAscending).toBeTruthy();
        });

        test("Testing that when the orderByString ends in DESC that isAscending is returned as false", () => {
            searchWrapper.vm.orderByString = searchWrapper.vm.$data.orderByUserHeaders[1] + 'DESC';
            searchWrapper.vm.$data.tableOrderByHeaders = searchWrapper.vm.$data.orderByUserHeaders;

            let orderBy = searchWrapper.vm.parseOrderBy();

            expect(orderBy.orderBy).toBe(1);
            expect(orderBy.isAscending).toBeFalsy();
        });

        test("Testing that when the orderByString doesn't end in ASC or DESC that isAscending is returned as true and orderBy is returned as null", () => {
            searchWrapper.vm.orderByString = searchWrapper.vm.$data.orderByUserHeaders[2];
            searchWrapper.vm.$data.tableOrderByHeaders = searchWrapper.vm.$data.orderByUserHeaders;

            let orderBy = searchWrapper.vm.parseOrderBy();

            expect(orderBy.orderBy).toBe(null);
            expect(orderBy.isAscending).toBeTruthy();
        });

        test("Testing that when orderBy doesn't match an entry in tableHeaders then it is returned as null", () => {
            searchWrapper.vm.orderByString = 'noMatchDESC';
            searchWrapper.vm.$data.tableOrderByHeaders = searchWrapper.vm.$data.orderByUserHeaders;

            let orderBy = searchWrapper.vm.parseOrderBy();

            expect(orderBy.orderBy).toBe(null);
            expect(orderBy.isAscending).toBeFalsy();
        });
    });

    describe("Testing the requestBusinesses method", () => {

        test("Testing that when the inputQuery is null then Api searchBusinesses is not called", () => {
            searchWrapper.vm.requestBusinesses(null, "");

            expect(searchBusinessesSpy).toHaveBeenCalledTimes(0);
        });

        test("Testing that the table data is populated correctly when data is returned", async () => {
            let expectedTableData = ["Lumbridge General Store", "Christchurch, Canterbury, New Zealand", "Accommodation and Food Services"]

            Api.searchBusinesses.mockImplementation( () => Promise.resolve(businessMockApiResponse) );

            await searchWrapper.vm.requestBusinesses("", "");

            expect(searchWrapper.vm.currentPage).toBe(0);
            expect(searchWrapper.vm.maxPage).toBe(0);
            expect(searchWrapper.vm.totalRows).toBe(1);
            expect(searchWrapper.vm.totalPages).toBe(1);
            expect(searchWrapper.vm.tableData).toStrictEqual(expectedTableData);
        });

        test("Testing that page-related variables are set correctly when no data is returned", async () => {
            Api.searchBusinesses.mockImplementation( () => Promise.resolve(emptyMockApiResponse) );

            await searchWrapper.vm.requestBusinesses("", "");

            expect(searchWrapper.vm.currentPage).toBe(0);
            expect(searchWrapper.vm.maxPage).toBe(0);
            expect(searchWrapper.vm.totalRows).toBe(0);
            expect(searchWrapper.vm.totalPages).toBe(0);
        });
    });

    describe("Testing the requestUsers method", () => {

        test("Testing that when the inputQuery is null then Api searchUsers is not called", () => {
            searchWrapper.vm.requestUsers(null);

            // 1 because it will be called once when the component is mounted
            expect(searchUsersSpy).toHaveBeenCalledTimes(1);
        });

        test("Testing that the table data is populated correctly when data is returned", async () => {
            let expectedTableData = ["Jonny","John Hector Smith", "johnsmith99@gmail.com", "Christchurch, Canterbury, New Zealand"]

            Api.searchUsers.mockImplementation( () => Promise.resolve(userMockApiResponse) );

            await searchWrapper.vm.requestUsers("");

            expect(searchWrapper.vm.currentPage).toBe(0);
            expect(searchWrapper.vm.maxPage).toBe(0);
            expect(searchWrapper.vm.totalRows).toBe(1);
            expect(searchWrapper.vm.totalPages).toBe(1);
            expect(searchWrapper.vm.tableData).toStrictEqual(expectedTableData);
        });

        test("Testing that page-related variables are set correctly when no data is returned", async () => {
            Api.searchUsers.mockImplementation( () => Promise.resolve(emptyMockApiResponse) );

            await searchWrapper.vm.requestUsers("");

            expect(searchWrapper.vm.currentPage).toBe(0);
            expect(searchWrapper.vm.maxPage).toBe(0);
            expect(searchWrapper.vm.totalRows).toBe(0);
            expect(searchWrapper.vm.totalPages).toBe(0);
        });
    });

    describe("Testing the convertBusinessType method", () => {

        test("Testing that when the provided string is Any then an empty string is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("Any")).toBe("");
        });

        test("Testing that when the provided string is Accommodation and Food Services then ACCOMMODATION_AND_FOOD_SERVICES is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("Accommodation and Food Services")).toBe("ACCOMMODATION_AND_FOOD_SERVICES");
        });

        test("Testing that when the provided string is ACCOMMODATION_AND_FOOD_SERVICES then Accommodation and Food Services is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("ACCOMMODATION_AND_FOOD_SERVICES")).toBe("Accommodation and Food Services");
        });

        test("Testing that when the provided string is Retail Trade then RETAIL_TRADE is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("Retail Trade")).toBe("RETAIL_TRADE");
        });

        test("Testing that when the provided string is RETAIL_TRADE then Retail Trade is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("RETAIL_TRADE")).toBe("Retail Trade");
        });

        test("Testing that when the provided string is Charitable Organisation then CHARITABLE_ORGANISATION is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("Charitable Organisation")).toBe("CHARITABLE_ORGANISATION");
        });

        test("Testing that when the provided string is CHARITABLE_ORGANISATION then Charitable Organisation is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("CHARITABLE_ORGANISATION")).toBe("Charitable Organisation");
        });

        test("Testing that when the provided string is Non Profit Organisation then NON_PROFIT_ORGANISATION is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("Non Profit Organisation")).toBe("NON_PROFIT_ORGANISATION");
        });

        test("Testing that when the provided string is NON_PROFIT_ORGANISATION then Non Profit Organisation is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("NON_PROFIT_ORGANISATION")).toBe("Non Profit Organisation");
        });

        test("Testing that when the provided string doesn't match any of the options then an empty string is returned", () => {
            expect(searchWrapper.vm.convertBusinessType("no match")).toBe("");
        });
    });

    describe("Testing the getAddress method", () => {

        test("Testing that when homeAddress is defined and all the fields are present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                    city: "City",
                    region: "Region",
                    country: "Country"
                }
            }
            let expectedAddress = "City, Region, Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is defined and city is not present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                    region: "Region",
                    country: "Country"
                }
            }
            let expectedAddress = "Region, Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is defined and region is not present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                    city: "City",
                    country: "Country"
                }
            }
            let expectedAddress = "City, Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is defined and country is not present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                    city: "City",
                    region: "Region"
                }
            }
            let expectedAddress = "City, Region"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is defined and city and region not present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                    country: "Country"
                }
            }
            let expectedAddress = "Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is defined and city and country not present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                    region: "Region"
                }
            }
            let expectedAddress = "Region"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is defined and region and country not present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                    city: "City"
                }
            }
            let expectedAddress = "City"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is defined and no fields are present then it concatenates correctly", () => {
            let entity = {
                homeAddress: {
                }
            }
            let expectedAddress = ""

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and all the fields are present then it concatenates correctly", () => {
            let entity = {
                address: {
                    city: "City",
                    region: "Region",
                    country: "Country"
                }
            }
            let expectedAddress = "City, Region, Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and city is not present then it concatenates correctly", () => {
            let entity = {
                address: {
                    region: "Region",
                    country: "Country"
                }
            }
            let expectedAddress = "Region, Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and region is not present then it concatenates correctly", () => {
            let entity = {
                address: {
                    city: "City",
                    country: "Country"
                }
            }
            let expectedAddress = "City, Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and country is not present then it concatenates correctly", () => {
            let entity = {
                address: {
                    city: "City",
                    region: "Region"
                }
            }
            let expectedAddress = "City, Region"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and city and region not present then it concatenates correctly", () => {
            let entity = {
                address: {
                    country: "Country"
                }
            }
            let expectedAddress = "Country"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and city and country not present then it concatenates correctly", () => {
            let entity = {
                address: {
                    region: "Region"
                }
            }
            let expectedAddress = "Region"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and region and country not present then it concatenates correctly", () => {
            let entity = {
                address: {
                    city: "City"
                }
            }
            let expectedAddress = "City"

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });

        test("Testing that when homeAddress is undefined and no fields are present then it concatenates correctly", () => {
            let entity = {
                address: {
                }
            }
            let expectedAddress = ""

            expect(searchWrapper.vm.getAddress(entity)).toBe(expectedAddress);
        });
    });

    describe("Testing the getFullName method", () => {

        test("Testing that when the middle name is defined then it concatenates correctly", () => {
            let entity = {
                firstName: "first",
                middleName: "middle",
                lastName: "last"
            }
            let expectedName = "first middle last"

            expect(searchWrapper.vm.getFullName(entity)).toBe(expectedName);
        });

        test("Testing that when the middle name is undefined then it concatenates correctly", () => {
            let entity = {
                firstName: "first",
                lastName: "last"
            }
            let expectedName = "first last"

            expect(searchWrapper.vm.getFullName(entity)).toBe(expectedName);
        });
    });

    describe("Testing the orderData method", () => {

        test("Testing that when the search type is User that router push and requestUsers are called", () => {
            searchWrapper.vm.searchType = 'User';
            searchWrapper.vm.query = 'Search';
            searchWrapper.vm.tableOrderByHeaders = searchWrapper.vm.orderByUserHeaders;

            let event = {
                orderBy: 0,
                isAscending: false
            };

            searchWrapper.vm.orderData(event);

            expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `User`, searchQuery: 'Search', orderBy: searchWrapper.vm.orderByUserHeaders[0] + 'DESC', page: (searchWrapper.vm.currentPage + 1).toString()}});
            expect(requestUsersSpy).toHaveBeenCalledTimes(2);
        });

        test("Testing that when the search type is Business that router push and requestBusinesses are called", () => {
            searchWrapper.vm.searchType = 'Business';
            searchWrapper.vm.query = 'Search';
            searchWrapper.vm.tableOrderByHeaders = searchWrapper.vm.orderByBusinessHeaders;

            let event = {
                orderBy: 0,
                isAscending: false
            };

            searchWrapper.vm.orderData(event);

            expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, searchQuery: 'Search', businessType: 'Any', orderBy: searchWrapper.vm.orderByBusinessHeaders[0] + 'DESC', page: (searchWrapper.vm.currentPage + 1).toString()}});
            expect(requestBusinessesSpy).toHaveBeenCalledTimes(1);
        });

        test("Testing that when the search type is not User or Business that router push is called with /pageDoesNotExist", () => {
            searchWrapper.vm.searchType = 'Other';
            searchWrapper.vm.query = 'Search';
            searchWrapper.vm.tableOrderByHeaders = searchWrapper.vm.orderByBusinessHeaders;

            let event = {
                orderBy: 0,
                isAscending: false
            };

            searchWrapper.vm.orderData(event);

            expect($router.push).toHaveBeenCalledWith('/pageDoesNotExist');
            expect(requestBusinessesSpy).toHaveBeenCalledTimes(0);
            expect(requestBusinessesSpy).toHaveBeenCalledTimes(0);
        });
    });

    describe("Testing the routeToProfile method", () => {

        test("Testing that when the search type is User then router push is called", () => {
            searchWrapper.vm.searchType = 'User';
            searchWrapper.vm.rowsPerPage = 3;
            searchWrapper.vm.userList = [{
                id: 5
            }, {
                id: 8
            }, {
                id: 16
            }];

            let expectedUserId = 16;

            Promise.resolve();

            searchWrapper.vm.routeToProfile(2);

            expect($router.push).toHaveBeenCalledWith({ path: `/profile/${expectedUserId}`});
        });

        test("Testing that when the search type is Business then router push is called", () => {
            searchWrapper.vm.searchType = 'Business';
            searchWrapper.vm.rowsPerPage = 2;
            searchWrapper.vm.businessList = [{
                id: 5
            }, {
                id: 8
            }, {
                id: 16
            }];

            let expectedBusinessId = 5;

            Promise.resolve();

            searchWrapper.vm.routeToProfile(2);

            expect($router.push).toHaveBeenCalledWith({ path: `/businessProfile/${expectedBusinessId}`});
        });
    });
});