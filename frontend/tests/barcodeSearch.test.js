/**
 * Jest tests for BarcodeSearchBar.vue as well as the barcodeSearch methods in Inventory.vue and Listings.vue.
 * @jest-environment jsdom
 */



import {describe, expect, jest, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import BarcodeSearchBar from "../src/components/BarcodeSearchBar";
import Inventory from "../src/views/Inventory";
import Api from "../src/Api";
import Cookies from "js-cookie";
import CurrencyApi from "../src/currencyInstance";
import Listings from "../src/views/Listings";

jest.mock("../src/Api");
jest.mock("../src/currencyInstance");
jest.mock("js-cookie");

describe("Testing the barcode search bar functionality", () => {

    let barcodeSearchBarWrapper;
    let searchClickedSpyOn;

    beforeEach(() => {
        searchClickedSpyOn = jest.spyOn(BarcodeSearchBar.methods, 'searchClicked');
        barcodeSearchBarWrapper = shallowMount(BarcodeSearchBar);
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test('Testing that updateBarcode sets the barcode to the input value', () => {
        barcodeSearchBarWrapper.vm.barcode = "";
        expect(barcodeSearchBarWrapper.vm.barcode).toBe("");

        barcodeSearchBarWrapper.vm.updateBarcode("123456");
        expect(barcodeSearchBarWrapper.vm.barcode).toBe("123456");
    });

    test('Testing that searchClicked triggers an emit with the barcode', () => {
        barcodeSearchBarWrapper.vm.barcode = "123";
        expect(barcodeSearchBarWrapper.vm.barcode).toBe("123");

        barcodeSearchBarWrapper.vm.searchClicked();

        expect(barcodeSearchBarWrapper.emitted('barcodeSearch')).toBeTruthy();
        expect(barcodeSearchBarWrapper.emitted('barcodeSearch')[0][0]).toBe("123");
    });

    test('Testing that enterPressed triggers searchClicked when the keycode is 13', () => {
        let event = {
            keyCode: 13
        }
        expect(event.keyCode).toEqual(13);

        barcodeSearchBarWrapper.vm.enterPressed(event);

        expect(searchClickedSpyOn).toHaveBeenCalledTimes(1);
    });

    test('Testing that enterPressed does not trigger searchClicked when the keycode is not 13', () => {
        let event = {
            keyCode: 14
        }
        expect(event.keyCode === 13).toBeFalsy();

        barcodeSearchBarWrapper.vm.enterPressed(event);

        expect(searchClickedSpyOn).toHaveBeenCalledTimes(0);
    });

});

test("Testing the barcodeSearch method in Inventory triggers a router push with the received event as the barcode and then triggers retrieveInventoryItems method", async () => {
    const $router = {
        push: jest.fn()
    };

    const $route = {
        params: {
            id: 1
        },
        path: `/businessProfile/1/inventory`,
        query: {'barcode': "123", 'orderBy': "createdASC", 'page': "1"}
    };

    const response = {
        response: {
            status: 200
        },
        status: 200,
        data: {
            role: "DEFAULTGLOBALAPPLICATIONADMIN",
            name: "name",
            id: 1,
            address: {
                country: "country"
            },
            administrators: [],
            length: 0
        }
    }

    const sortInventoryItemsResponse = {
        data: [],
        headers: {
            "total-rows": 0,
            "total-pages": 0
        }
    }

    const currencyResponse = {
        data: [{"currencies":[{"code":"NZD","name":"New Zealand dollar","symbol":"$"}]}]
    }

    Api.sortInventoryItems.mockImplementation(() => Promise.resolve(sortInventoryItemsResponse));
    Api.getUser.mockImplementation(() => Promise.resolve(response));
    Api.getBusiness.mockImplementation(() => Promise.resolve(response));
    CurrencyApi.currencyQuery.mockImplementation(() => Promise.resolve(currencyResponse));
    Cookies.get = jest.fn().mockImplementation(() => 1);

    const retrieveInventoryItemsSpy = jest.spyOn(Inventory.methods, 'retrieveInventoryItems');

    const inventoryWrapper = shallowMount(Inventory, {
        mocks: {
            $router,
            $route
        },
        data () {
            return {
                inventories: [],
                notInitialLoad: true
            }
        }
    });

    await Promise.resolve();

    inventoryWrapper.vm.businessId = 1;
    inventoryWrapper.vm.orderByString = "createdASC"
    inventoryWrapper.vm.currentPage = 0;

    await inventoryWrapper.vm.barcodeSearch('12345');

    await Promise.resolve();

    expect($router.push).toHaveBeenLastCalledWith({path: `/businessProfile/1/inventory`, query: {'barcode': "12345", 'orderBy': "createdASC", 'page': "1"}});
    expect(retrieveInventoryItemsSpy).toHaveBeenCalledTimes(1);
});

test("Testing the barcodeSearch method in Listings triggers a router push with the received event as the barcode and then triggers getListings method", async () => {
    const $router = {
        push: jest.fn()
    };

    const $route = {
        params: {
            id: 1
        },
        path: `/businessProfile/1/listings`,
        query: {'barcode': "123", 'orderBy': "createdASC", 'page': "1"}
    };

    const response = {
        response: {
            status: 200
        },
        status: 200,
        data: {
            role: "DEFAULTGLOBALAPPLICATIONADMIN",
            name: "name",
            id: 1,
            address: {
                country: "country"
            },
            administrators: [],
            length: 0
        },
        headers: {
            "total-rows": 1,
            "total-pages": 1
        }
    }

    const currencyResponse = {
        data: [{"currencies":[{"code":"NZD","name":"New Zealand dollar","symbol":"$"}]}]
    }

    Api.sortListings.mockImplementation(() => Promise.resolve(response));
    Api.getUser.mockImplementation(() => Promise.resolve(response));
    Api.getBusiness.mockImplementation(() => Promise.resolve(response));
    CurrencyApi.currencyQuery.mockImplementation(() => Promise.resolve(currencyResponse));
    Cookies.get = jest.fn().mockImplementation(() => 1);

    const getListingsSpy = jest.spyOn(Listings.methods, 'getListings');

    const listingsWrapper = shallowMount(Listings, {
        mocks: {
            $router,
            $route
        }
    });

    await Promise.resolve();

    listingsWrapper.vm.businessId = 1;
    listingsWrapper.vm.orderBy = "createdASC"
    listingsWrapper.vm.currentPage = 0;

    await listingsWrapper.vm.barcodeSearch('12345');

    await Promise.resolve();

    expect($router.push).toHaveBeenLastCalledWith({path: `/businessProfile/1/listings`, query: {'barcode': "12345", 'orderBy': "createdASC", 'page': "1"}});
    expect(getListingsSpy).toHaveBeenCalledTimes(1);
});