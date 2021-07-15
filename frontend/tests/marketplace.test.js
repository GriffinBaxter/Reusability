/**
 * Jest tests for Marketplace.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe, jest, beforeAll} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import Marketplace from "../src/views/Marketplace";
import Api from "../src/Api"

jest.mock("../src/Api");

describe("Testing the marketplace methods", () => {

    let marketplaceWrapper;
    let data;
    let $route;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/marketplace',
            query: {'orderBy': "createdASC", 'page': "1"}
        }
        data = {
            status: 200,
            data: [
                {
                    "id": 500,
                    "creator": {
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
                                "businessType": "Accommodation and Food Services",
                                "created": "2020-07-14T14:52:00Z"
                            }
                        ]
                    },
                    "section": "ForSale",
                    "created": "2021-07-15T05:10:00Z",
                    "displayPeriodEnd": "2021-07-29T05:10:00Z",
                    "title": "1982 Lada Samara",
                    "description": "Beige, suitable for a hen house. Fair condition. Some rust. As is, where is. Will swap for budgerigar.",
                    "keywords": [
                        {
                            "id": 600,
                            "name": "Vehicle",
                            "created": "2021-07-15T05:10:00Z"
                        }
                    ]
                }
            ],
            headers: {'total-pages': "2"}
        }

        Api.getAllCards.mockImplementation(() => Promise.resolve(data));

        marketplaceWrapper = shallowMount(
            Marketplace,
            {
                mocks: {
                    $router,
                    $route
                }
            });

        Promise.resolve();
    });

    describe("Testing the changeSection method", () => {

        test("Testing that input of For Sale sets sortBy and page to the For Sale values", () => {
            marketplaceWrapper.vm.sortBy = "createdDESC";

            marketplaceWrapper.vm.forSaleSortBy = "locationASC";
            marketplaceWrapper.vm.forSalePage = 0;

            marketplaceWrapper.vm.wantedSortBy = "locationDESC";
            marketplaceWrapper.vm.wantedPage = 1;

            marketplaceWrapper.vm.exchangeSortBy = "createdASC";
            marketplaceWrapper.vm.exchangePage = 2;

            const inputVal = "For Sale";

            marketplaceWrapper.vm.changeSection(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.sortBy).toEqual(marketplaceWrapper.vm.forSaleSortBy);
            expect(marketplaceWrapper.vm.page).toEqual(marketplaceWrapper.vm.forSalePage);
        })

        test("Testing that input of Wanted sets sortBy and page to the Wanted values", () => {
            marketplaceWrapper.vm.sortBy = "createdDESC";

            marketplaceWrapper.vm.forSaleSortBy = "locationASC";
            marketplaceWrapper.vm.forSalePage = 0;

            marketplaceWrapper.vm.wantedSortBy = "locationDESC";
            marketplaceWrapper.vm.wantedPage = 1;

            marketplaceWrapper.vm.exchangeSortBy = "createdASC";
            marketplaceWrapper.vm.exchangePage = 2;

            const inputVal = "Wanted";

            marketplaceWrapper.vm.changeSection(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.sortBy).toEqual(marketplaceWrapper.vm.wantedSortBy);
            expect(marketplaceWrapper.vm.page).toEqual(marketplaceWrapper.vm.wantedPage);
        })

        test("Testing that input of Exchange sets sortBy and page to the Exchange values", () => {
            marketplaceWrapper.vm.sortBy = "createdDESC";

            marketplaceWrapper.vm.forSaleSortBy = "locationASC";
            marketplaceWrapper.vm.forSalePage = 0;

            marketplaceWrapper.vm.wantedSortBy = "locationDESC";
            marketplaceWrapper.vm.wantedPage = 1;

            marketplaceWrapper.vm.exchangeSortBy = "createdASC";
            marketplaceWrapper.vm.exchangePage = 2;

            const inputVal = "Exchange";

            marketplaceWrapper.vm.changeSection(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.sortBy).toEqual(marketplaceWrapper.vm.exchangeSortBy);
            expect(marketplaceWrapper.vm.page).toEqual(marketplaceWrapper.vm.exchangePage);
        })

    })

    test("Testing that openCardDetail sets selected card to the input card", async () => {
        const inputVal = 500

        marketplaceWrapper.vm.openCardDetail(inputVal);

        expect(marketplaceWrapper.vm.selectedCard).toBe(inputVal);
    })

    describe("Testing the retrieveCardsForSection method", () => {

        test("Testing the getAllCards API call", async () => {
            const returnData = await Api.getAllCards("ForSale", "createdDESC", 0);

            await Promise.resolve();

            expect(returnData).toBe(data);
        })

        test("Testing the retrieveCardsForSection method", async () => {
            const section = "ForSale";

            await marketplaceWrapper.vm.retrieveAllCardsForSection(section);

            await Promise.resolve();

            await marketplaceWrapper.vm.$nextTick();

            expect(marketplaceWrapper.vm.allCards[section]).toBe(data.data);
            expect(marketplaceWrapper.vm.totalPages).toEqual(2);
        })

    })

    describe("Testing the orderedCards method", () => {

        test("Testing that forSaleSortBy and sortBy are set to the orderBy value when the section is For Sale", () => {
            marketplaceWrapper.vm.selectSection = "For Sale";
            marketplaceWrapper.vm.sortBy = "createdDESC";
            marketplaceWrapper.vm.forSaleSortBy = "createdDESC";
            marketplaceWrapper.vm.wantedSortBy = "createdDESC";
            marketplaceWrapper.vm.exchangeSortBy = "createdDESC";

            const inputVal = "locationASC";

            $route.query.orderBy = inputVal;

            marketplaceWrapper.vm.orderedCards(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.sortBy).toEqual(marketplaceWrapper.vm.forSaleSortBy);
            expect(marketplaceWrapper.vm.forSaleSortBy).toEqual(inputVal);
            expect(marketplaceWrapper.vm.wantedSortBy).toEqual("createdDESC");
            expect(marketplaceWrapper.vm.exchangeSortBy).toEqual("createdDESC");
        })

        test("Testing that wantedSortBy and sortBy are set to the orderBy value when the section is Wanted", () => {
            marketplaceWrapper.vm.selectSection = "Wanted";
            marketplaceWrapper.vm.sortBy = "createdDESC";
            marketplaceWrapper.vm.forSaleSortBy = "createdDESC";
            marketplaceWrapper.vm.wantedSortBy = "createdDESC";
            marketplaceWrapper.vm.exchangeSortBy = "createdDESC";

            const inputVal = "locationDESC";

            $route.query.orderBy = inputVal;

            marketplaceWrapper.vm.orderedCards(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.sortBy).toEqual(marketplaceWrapper.vm.wantedSortBy);
            expect(marketplaceWrapper.vm.forSaleSortBy).toEqual("createdDESC");
            expect(marketplaceWrapper.vm.wantedSortBy).toEqual(inputVal);
            expect(marketplaceWrapper.vm.exchangeSortBy).toEqual("createdDESC");
        })

        test("Testing that exchangeSortBy and sortBy are set to the orderBy value when the section is Exchange", () => {
            marketplaceWrapper.vm.selectSection = "Exchange";
            marketplaceWrapper.vm.sortBy = "createdDESC";
            marketplaceWrapper.vm.forSaleSortBy = "createdDESC";
            marketplaceWrapper.vm.wantedSortBy = "createdDESC";
            marketplaceWrapper.vm.exchangeSortBy = "createdDESC";

            const inputVal = "titleDESC";

            $route.query.orderBy = inputVal;

            marketplaceWrapper.vm.orderedCards(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.sortBy).toEqual(marketplaceWrapper.vm.exchangeSortBy);
            expect(marketplaceWrapper.vm.forSaleSortBy).toEqual("createdDESC");
            expect(marketplaceWrapper.vm.wantedSortBy).toEqual("createdDESC");
            expect(marketplaceWrapper.vm.exchangeSortBy).toEqual(inputVal);
        })

        test("Testing that exchangeSortBy and sortBy are set to the orderBy createdASC when the section is Exchange", () => {
            marketplaceWrapper.vm.selectSection = "Exchange";
            marketplaceWrapper.vm.sortBy = "createdDESC";
            marketplaceWrapper.vm.forSaleSortBy = "createdDESC";
            marketplaceWrapper.vm.wantedSortBy = "createdDESC";
            marketplaceWrapper.vm.exchangeSortBy = "createdDESC";

            const inputVal = "createdASC";

            $route.query.orderBy = inputVal;

            marketplaceWrapper.vm.orderedCards(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.sortBy).toEqual(marketplaceWrapper.vm.exchangeSortBy);
            expect(marketplaceWrapper.vm.forSaleSortBy).toEqual("createdDESC");
            expect(marketplaceWrapper.vm.wantedSortBy).toEqual("createdDESC");
            expect(marketplaceWrapper.vm.exchangeSortBy).toEqual(inputVal);
        })


    })

    describe("Testing the updatePage method", () => {

        test("Testing that forSalePage and page are set to the new page number when the section is For Sale", () => {
            marketplaceWrapper.vm.selectSection = "For Sale";
            marketplaceWrapper.vm.totalPages = 2;
            marketplaceWrapper.vm.page = 0;
            marketplaceWrapper.vm.forSalePage = 0;
            marketplaceWrapper.vm.wantedPage = 0;
            marketplaceWrapper.vm.exchangePage = 0;

            const inputVal = 1;

            $route.query.page = inputVal + 1;

            marketplaceWrapper.vm.updatePage(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.page).toEqual(marketplaceWrapper.vm.forSalePage);
            expect(marketplaceWrapper.vm.forSalePage).toEqual(inputVal);
            expect(marketplaceWrapper.vm.wantedPage).toEqual(0);
            expect(marketplaceWrapper.vm.exchangePage).toEqual(0);
        })

        test("Testing that wantedPage and page are set to the new page number when the section is Wanted", () => {
            marketplaceWrapper.vm.selectSection = "Wanted";
            marketplaceWrapper.vm.totalPages = 2;
            marketplaceWrapper.vm.page = 0;
            marketplaceWrapper.vm.forSalePage = 0;
            marketplaceWrapper.vm.wantedPage = 0;
            marketplaceWrapper.vm.exchangePage = 0;

            const inputVal = 1;

            $route.query.page = inputVal + 1;

            marketplaceWrapper.vm.updatePage(inputVal);

            expect(marketplaceWrapper.vm.page).toEqual(marketplaceWrapper.vm.wantedPage);
            expect(marketplaceWrapper.vm.forSalePage).toEqual(0);
            expect(marketplaceWrapper.vm.wantedPage).toEqual(inputVal);
            expect(marketplaceWrapper.vm.exchangePage).toEqual(0);
        })

        test("Testing that exchangePage and page are set to the new page number when the section is Exchange", () => {
            marketplaceWrapper.vm.selectSection = "Exchange";
            marketplaceWrapper.vm.totalPages = 2;
            marketplaceWrapper.vm.page = 0;
            marketplaceWrapper.vm.forSalePage = 0;
            marketplaceWrapper.vm.wantedPage = 0;
            marketplaceWrapper.vm.exchangePage = 0;

            const inputVal = 1;

            $route.query.page = inputVal + 1;

            marketplaceWrapper.vm.updatePage(inputVal);

            Promise.resolve();

            expect(marketplaceWrapper.vm.page).toEqual(marketplaceWrapper.vm.exchangePage);
            expect(marketplaceWrapper.vm.forSalePage).toEqual(0);
            expect(marketplaceWrapper.vm.wantedPage).toEqual(0);
            expect(marketplaceWrapper.vm.exchangePage).toEqual(inputVal);
        })

    })

})