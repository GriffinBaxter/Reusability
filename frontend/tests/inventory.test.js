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

        beforeEach(() => {

            const sortInventoryItemsResponse = {
                status: 200,
                data: [],
                headers: {
                    "total-pages": "0",
                    "total-rows": "0"
                }
            }

            Api.sortInventoryItems.mockImplementation(() => Promise.resolve(sortInventoryItemsResponse));
        })

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
            expect(retrieveInventoryItemsSpyOn).toHaveBeenCalledTimes(2);
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

        test("Test retrieveBusinessInfo pushes to the timeout page when there is no response received", async () => {

            Api.getBusiness.mockImplementation(() => Promise.reject({request: true}))

            await wrapper.vm.retrieveBusinessInfo();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/timeout"});

        });

        test("Test retrieveBusinessInfo pushes to the invalid token page when a 401 status is returned", async () => {

            const data = {
                response: {
                    status: 401
                }
            }
            Api.getBusiness.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveBusinessInfo();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/invalidtoken"});

        });

        test("Test retrieveBusinessInfo pushes to the no business page when a 406 status is returned", async () => {

            const data = {
                response: {
                    status: 406
                }
            }
            Api.getBusiness.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveBusinessInfo();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/noBusiness"});

        });

        test("Test retrieveBusinessInfo pushes to the no business page by default when a 500 status is returned", async () => {

            const data = {
                response: {
                    status: 500
                }
            }
            Api.getBusiness.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveBusinessInfo();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/noBusiness"});

        });

    });

    describe("Test orderInventory method", () => {

        test("Test orderInventory sets product ID ascending values and adds the fas chevron classes to the correct icon when ordering by product ID", async () => {
            let productIdIcon = wrapper.find('#productIdIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return productIdIcon.element
            });

            wrapper.vm.$data.productIdAscending = false;
            wrapper.vm.orderInventory(true, false, false, false, false, false, false, false);

            expect(wrapper.vm.$data.productIdAscending).toEqual(true);
            expect(productIdIcon.classes()).toContain('fas');
            expect(productIdIcon.classes()).toContain('fa-chevron-down');
            expect(productIdIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "productIdDESC", page: "1"}});
        });

        test("Test orderInventory sets quantity ascending value and adds the fas chevron classes to the correct icon when ordering by quantity", async () => {
            let quantityIcon = wrapper.find('#quantityIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return quantityIcon.element
            });

            wrapper.vm.$data.quantityAscending = true;
            wrapper.vm.orderInventory(false, true, false, false, false, false, false, false);


            expect(wrapper.vm.$data.quantityAscending).toEqual(false);
            expect(quantityIcon.classes()).toContain('fas');
            expect(quantityIcon.classes()).toContain('fa-chevron-up');
            expect(quantityIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "quantityASC", page: "1"}});
        });

        test("Test orderInventory sets price per item ascending value and adds the fas chevron classes to the correct icon when ordering by price per item", async () => {
            let pricePerItemIcon = wrapper.find('#pricePerItemIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return pricePerItemIcon.element
            });

            wrapper.vm.$data.quantityAscending = false;
            wrapper.vm.orderInventory(false, false, true, false, false, false, false, false);


            expect(wrapper.vm.$data.pricePerItemAscending).toEqual(true);
            expect(pricePerItemIcon.classes()).toContain('fas');
            expect(pricePerItemIcon.classes()).toContain('fa-chevron-down');
            expect(pricePerItemIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "pricePerItemDESC", page: "1"}});
        });

        test("Test orderInventory sets total price ascending value and adds the fas chevron classes to the correct icon when ordering by total price", async () => {
            let totalPriceIcon = wrapper.find('#totalPriceIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return totalPriceIcon.element
            });

            wrapper.vm.$data.totalPriceAscending = true;
            wrapper.vm.orderInventory(false, false, false, true, false, false, false, false);


            expect(wrapper.vm.$data.totalPriceAscending).toEqual(false);
            expect(totalPriceIcon.classes()).toContain('fas');
            expect(totalPriceIcon.classes()).toContain('fa-chevron-up');
            expect(totalPriceIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "totalPriceASC", page: "1"}});
        });

        test("Test orderInventory sets manufactured ascending value and adds the fas chevron classes to the correct icon when ordering by manufactured date", async () => {
            let manufacturedIcon = wrapper.find('#manufacturedIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return manufacturedIcon.element
            });

            wrapper.vm.$data.manufacturedAscending = false;
            wrapper.vm.orderInventory(false, false, false, false, true, false, false, false);


            expect(wrapper.vm.$data.manufacturedAscending).toEqual(true);
            expect(manufacturedIcon.classes()).toContain('fas');
            expect(manufacturedIcon.classes()).toContain('fa-chevron-down');
            expect(manufacturedIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "manufacturedDESC", page: "1"}});
        });

        test("Test orderInventory sets sell by ascending value and adds the fas chevron classes to the correct icon when ordering by sell by date", async () => {
            let sellByIcon = wrapper.find('#sellByIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return sellByIcon.element
            });

            wrapper.vm.$data.sellByAscending = true;
            wrapper.vm.orderInventory(false, false, false, false, false, true, false, false);


            expect(wrapper.vm.$data.sellByAscending).toEqual(false);
            expect(sellByIcon.classes()).toContain('fas');
            expect(sellByIcon.classes()).toContain('fa-chevron-up');
            expect(sellByIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "sellByASC", page: "1"}});
        });

        test("Test orderInventory sets best before ascending value and adds the fas chevron classes to the correct icon when ordering by best before date", async () => {
            let bestBeforeIcon = wrapper.find('#sellByIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return bestBeforeIcon.element
            });

            wrapper.vm.$data.bestBeforeAscending = false;
            wrapper.vm.orderInventory(false, false, false, false, false, false, true, false);


            expect(wrapper.vm.$data.bestBeforeAscending).toEqual(true);
            expect(bestBeforeIcon.classes()).toContain('fas');
            expect(bestBeforeIcon.classes()).toContain('fa-chevron-down');
            expect(bestBeforeIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "bestBeforeDESC", page: "1"}});
        });

        test("Test orderInventory sets expires ascending value and adds the fas chevron classes to the correct icon when ordering by expiry date", async () => {
            let expiresIcon = wrapper.find('#expiresIcon');

            jest.spyOn(document, 'getElementById').mockImplementation(() => {
                return expiresIcon.element
            });

            wrapper.vm.$data.expiresAscending = true;
            wrapper.vm.orderInventory(false, false, false, false, false, false, false, true);


            expect(wrapper.vm.$data.expiresAscending).toEqual(false);
            expect(expiresIcon.classes()).toContain('fas');
            expect(expiresIcon.classes()).toContain('fa-chevron-up');
            expect(expiresIcon.classes()).toContain('float-end');
            expect($router.push).toHaveBeenCalledWith({path: "/businessProfile/0/inventory", query: {barcode: "", orderBy: "expiresASC", page: "1"}});
        });

    });


    describe("Test retrieveInventoryItems method", () => {

        test("Test retrieveInventoryItems clears inventory item values if a 200 response is returned but there is no data for the given query", async () => {

            const sortInventoryItemsResponse = {
                status: 200,
                data: [],
                headers: {
                    "total-pages": "0",
                    "total-rows": "0"
                }
            }
            Api.sortInventoryItems.mockImplementation(() => Promise.resolve(sortInventoryItemsResponse));

            await wrapper.vm.retrieveInventoryItems();

            expect(wrapper.vm.$data.inventories).toEqual([]);
            expect(wrapper.vm.$data.currentPage).toEqual(0);
            expect(wrapper.vm.$data.totalRows).toEqual(0);
            expect(wrapper.vm.$data.totalPages).toEqual(0);

        });

        test("Test retrieveInventoryItems correctly stores the inventory data if a 200 response containing inventory items is returned for the given query", async () => {

            const sortInventoryItemsResponse = {
                status: 200,
                data: [
                    {
                        "id": 12,
                        "product": {
                            "id": "BLACK-FOREST",
                            "name": "Cadbury Chocolate Block Dairy Milk Black Forest 180g",
                            "description": "Made in Australia from imported and local ingredients.",
                            "manufacturer": "Cadbury",
                            "recommendedRetailPrice": 3.49,
                            "created": "2021-05-12T00:00",
                            "images": [],
                            "business": null,
                            "barcode": null
                        },
                        "quantity": 68,
                        "pricePerItem": 4,
                        "totalPrice": 250,
                        "manufactured": "2021-05-10",
                        "sellBy": "2023-05-14",
                        "bestBefore": "2023-05-16",
                        "expires": "2023-05-30"
                    },
                    {
                        "id": 16,
                        "product": {
                            "id": "CARAMELLO",
                            "name": "Cadbury Chocolate Block Dairy Milk Caramello",
                            "description": "Made in Australia from imported and local ingredients.",
                            "manufacturer": "Cadbury",
                            "recommendedRetailPrice": 3.49,
                            "created": "2021-05-12T00:00",
                            "images": [],
                            "business": null,
                            "barcode": null
                        },
                        "quantity": 100,
                        "pricePerItem": 3.49,
                        "totalPrice": 349,
                        "manufactured": "2021-05-12",
                        "sellBy": "2021-09-10",
                        "bestBefore": "2021-09-12",
                        "expires": "2023-11-12"
                    }
                ],
                headers: {
                    "total-pages": "20",
                    "total-rows": "40"
                }
            }
            Api.sortInventoryItems.mockImplementation(() => Promise.resolve(sortInventoryItemsResponse));

            await wrapper.vm.retrieveInventoryItems();

            expect(wrapper.vm.$data.inventories.length).toEqual(2);
            expect(wrapper.vm.$data.currentPage).toEqual(0);
            expect(wrapper.vm.$data.totalRows).toEqual(40);
            expect(wrapper.vm.$data.totalPages).toEqual(20);

        });


        test("Test retrieveInventoryItems pushes to the timeout page when there is no response received", async () => {

            Api.sortInventoryItems.mockImplementation(() => Promise.reject({request: true}))

            await wrapper.vm.retrieveInventoryItems();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/timeout"});

        });

        test("Test retrieveInventoryItems pushes to the invalid token page when a 401 status is returned", async () => {

            const data = {
                response: {
                    status: 401
                }
            }
            Api.sortInventoryItems.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveInventoryItems();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/invalidtoken"});

        });

        test("Test retrieveInventoryItems pushes to the no business page when a 406 status is returned", async () => {

            const data = {
                response: {
                    status: 406
                }
            }
            Api.sortInventoryItems.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveInventoryItems();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/noBusiness"});

        });

        test("Test retrieveInventoryItems pushes to the timeout page by default when a 500 status is returned", async () => {

            const data = {
                response: {
                    status: 500
                }
            }
            Api.sortInventoryItems.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveInventoryItems();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/timeout"});

        });

        test("Test retrieveInventoryItems pushes to the does not exist page when a 400 status is returned", async () => {

            const data = {
                response: {
                    status: 400
                }
            }
            Api.sortInventoryItems.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveInventoryItems();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/pageDoesNotExist"});

        });

        test("Test retrieveInventoryItems pushes to the forbidden page when a 403 status is returned", async () => {

            const data = {
                response: {
                    status: 403
                }
            }
            Api.sortInventoryItems.mockImplementation(() => Promise.reject(data))

            await wrapper.vm.retrieveInventoryItems();
            await wrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith({path: "/forbidden"});

        });


    });






});
