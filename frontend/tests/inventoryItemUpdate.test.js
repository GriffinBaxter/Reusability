/**
 * Jest tests for UpdateInventoryItemModal.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe, jest} from "@jest/globals"
const endOfToday = require('date-fns/endOfToday');
const format = require('date-fns/format');
const add = require('date-fns/add');
const sub = require('date-fns/sub');
import Api from "../src/Api"
import {shallowMount} from "@vue/test-utils";
import InventoryItem from "../src/configs/InventoryItem";
import UpdateInventoryItemModal from "../src/components/inventory/UpdateInventoryItemModal.vue";

jest.mock("../src/Api");


describe("Testing the update inventory item functionality", () => {

    test('Test the update inventory item API', async () => {
        const todayDate = endOfToday();

        const id = 1;
        const businessId = 1;
        const inventoryItem = {
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: sub(todayDate, {months: 1}),
            sellBy: add(todayDate, {months: 1}),
            bestBefore: add(todayDate, {months: 2}),
            expires: add(todayDate, {months: 3})
        }

        const data = {
            status: 200
        }

        Api.modifyInventoryItem.mockImplementation(() => Promise.resolve(data));

        const returnData = await Api.modifyInventoryItem(id, businessId, inventoryItem);

        expect(returnData).toBe(data);
    })

    test("Fields are set correctly when showing modal", async () => {
        const todayDate = endOfToday();
        const inventoryItem = {
            productId: "PROD",
            quantity: 10,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: format(sub(todayDate, {months: 1}), 'yyyy-MM-dd'),
            manufacturedUnformatted: (new Date()),
            sellBy: format(add(todayDate, {months: 1}), 'yyyy-MM-dd'),
            sellByUnformatted: (new Date()),
            bestBefore: format(add(todayDate, {months: 2}), 'yyyy-MM-dd'),
            bestBeforeUnformatted: (new Date()),
            expires: format(add(todayDate, {months: 3}), 'yyyy-MM-dd'),
            expiresUnformatted: (new Date())
        }

        const businessId = 1;
        const currencyCode = "NZD";
        const currencySymbol = "$";
        const value = {
            index: 1,
            productName: "NAME",
            productId: "PROD",
            quantity: 10,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: inventoryItem.manufactured,
            sellBy: inventoryItem.sellBy,
            bestBefore: inventoryItem.bestBefore,
            expires: inventoryItem.expires,
        }

        const $router = {
            push: jest.fn()
        }
        const updateInventoryItemWrapper = await shallowMount(
            UpdateInventoryItemModal,
            {
                mocks: {
                    $router,
                },
                propsData: {
                    value,
                    businessId,
                    currencyCode,
                    currencySymbol
                },
            });

        updateInventoryItemWrapper.vm.newInventoryItem = new InventoryItem(inventoryItem);
        await updateInventoryItemWrapper.vm.showModal();
        await Promise.resolve();

        expect(updateInventoryItemWrapper.vm.newInventoryItem).toEqual(new InventoryItem(inventoryItem));
        expect(updateInventoryItemWrapper.vm.productIdErrorMsg).toEqual("");
        expect(updateInventoryItemWrapper.vm.quantityErrorMsg).toEqual("");
        expect(updateInventoryItemWrapper.vm.pricePerItemErrorMsg).toEqual("");
        expect(updateInventoryItemWrapper.vm.totalPriceErrorMsg).toEqual("");
        expect(updateInventoryItemWrapper.vm.manufacturedErrorMsg).toEqual("");
        expect(updateInventoryItemWrapper.vm.sellByErrorMsg).toEqual("");
        expect(updateInventoryItemWrapper.vm.bestBeforeErrorMsg).toEqual("");
        expect(updateInventoryItemWrapper.vm.expiresErrorMsg).toEqual("");
    })

    test("Validate fields returns false when given valid data", async () => {
        const todayDate = endOfToday();
        const inventoryItem = {
            productId: "PROD",
            quantity: 10,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: format(sub(todayDate, {months: 1}), 'yyyy-MM-dd'),
            sellBy: format(add(todayDate, {months: 1}), 'yyyy-MM-dd'),
            bestBefore: format(add(todayDate, {months: 2}), 'yyyy-MM-dd'),
            expires: format(add(todayDate, {months: 3}), 'yyyy-MM-dd')
        }

        const data = false;

        const businessId = 1;
        const currencyCode = "NZD";
        const currencySymbol = "$";
        const value = {
            index: 1,
            productName: "NAME",
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: inventoryItem.manufactured,
            sellBy: inventoryItem.sellBy,
            bestBefore: inventoryItem.bestBefore,
            expires: inventoryItem.expires,
        }
        const $router = {
            push: jest.fn()
        }
        const updateInventoryItemWrapper = await shallowMount(
            UpdateInventoryItemModal,
            {
                mocks: {
                    $router,
                },
                propsData: {
                    value,
                    businessId,
                    currencyCode,
                    currencySymbol
                },
            });

        updateInventoryItemWrapper.vm.newInventoryItem = new InventoryItem(inventoryItem);
        const returnData = await updateInventoryItemWrapper.vm.validateFields();

        expect(returnData).toEqual(data);
    })

    test("Validate fields returns true when given invalid data", async () => {
        const todayDate = endOfToday();
        const inventoryItem = {
            productId: "",
            quantity: 0,
            pricePerItem: 100000000000000000000,
            totalPrice: 100000000000000000000,
            manufactured: format(add(todayDate, {months: 1}), 'yyyy-MM-dd'),
            sellBy: format(sub(todayDate, {months: 1}), 'yyyy-MM-dd'),
            bestBefore: format(sub(todayDate, {months: 2}), 'yyyy-MM-dd'),
            expires: format(sub(todayDate, {months: 3}), 'yyyy-MM-dd')
        }
        const data = true;

        const businessId = 1;
        const currencyCode = "NZD";
        const currencySymbol = "$";
        const value = {
            index: 1,
            productName: "NAME",
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: sub(todayDate, {months: 1}),
            sellBy: add(todayDate, {months: 1}),
            bestBefore: add(todayDate, {months: 2}),
            expires: add(todayDate, {months: 3}),
        }
        const $router = {
            push: jest.fn()
        }
        const updateInventoryItemWrapper = await shallowMount(
            UpdateInventoryItemModal,
            {
                mocks: {
                    $router,
                },
                propsData: {
                    value,
                    businessId,
                    currencyCode,
                    currencySymbol
                },
            });

        updateInventoryItemWrapper.vm.newInventoryItem = new InventoryItem(inventoryItem);
        const returnData = await updateInventoryItemWrapper.vm.validateFields(inventoryItem);

        expect(returnData).toEqual(data);
    })

    test('Test that when a 400 is received, the formErrorModalMessage is updated', async () => {
        const todayDate = endOfToday();
        const inventoryItem = {
            productId: "PROD",
            quantity: 10,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: format(sub(todayDate, {months: 1}), 'yyyy-MM-dd'),
            sellBy: format(add(todayDate, {months: 1}), 'yyyy-MM-dd'),
            bestBefore: format(add(todayDate, {months: 2}), 'yyyy-MM-dd'),
            expires: format(add(todayDate, {months: 3}), 'yyyy-MM-dd')
        }

        const businessId = 1;
        const currencyCode = "NZD";
        const currencySymbol = "$";
        const value = {
            index: 1,
            id: 1,
            productName: "NAME",
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: inventoryItem.manufactured,
            sellBy: inventoryItem.sellBy,
            bestBefore: inventoryItem.bestBefore,
            expires: inventoryItem.expires,
        }
        const $router = {
            push: jest.fn()
        }
        const updateInventoryItemWrapper = await shallowMount(
            UpdateInventoryItemModal,
            {
                mocks: {
                    $router,
                },
                propsData: {
                    value,
                    businessId,
                    currencyCode,
                    currencySymbol
                },
            });

        updateInventoryItemWrapper.vm.newInventoryItem = new InventoryItem(inventoryItem);

        const data = {
            response: {
                status: 400,
                data: {
                    message: ""
                }
            }
        }

        Api.modifyInventoryItem.mockImplementation(() => Promise.reject(data));

        await updateInventoryItemWrapper.vm.updateInventoryItem(new Event('click'));
        await Promise.resolve();
        expect(updateInventoryItemWrapper.vm.formErrorModalMessage).toEqual("Some of the information you have entered is invalid.");
    })

    test('Test that when a 403 is received, the formErrorModalMessage is updated', async () => {
        const todayDate = endOfToday();
        const inventoryItem = {
            productId: "PROD",
            quantity: 10,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: format(sub(todayDate, {months: 1}), 'yyyy-MM-dd'),
            sellBy: format(add(todayDate, {months: 1}), 'yyyy-MM-dd'),
            bestBefore: format(add(todayDate, {months: 2}), 'yyyy-MM-dd'),
            expires: format(add(todayDate, {months: 3}), 'yyyy-MM-dd')
        }

        const businessId = 1;
        const currencyCode = "NZD";
        const currencySymbol = "$";
        const value = {
            index: 1,
            id: 1,
            productName: "NAME",
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: inventoryItem.manufactured,
            sellBy: inventoryItem.sellBy,
            bestBefore: inventoryItem.bestBefore,
            expires: inventoryItem.expires,
        }
        const $router = {
            push: jest.fn()
        }
        const updateInventoryItemWrapper = await shallowMount(
            UpdateInventoryItemModal,
            {
                mocks: {
                    $router,
                },
                propsData: {
                    value,
                    businessId,
                    currencyCode,
                    currencySymbol
                },
            });

        updateInventoryItemWrapper.vm.newInventoryItem = new InventoryItem(inventoryItem);

        const data = {
            response: {
                status: 403
            }
        }

        Api.modifyInventoryItem.mockImplementation(() => Promise.reject(data));

        await updateInventoryItemWrapper.vm.updateInventoryItem(new Event('click'));
        await Promise.resolve();
        expect(updateInventoryItemWrapper.vm.formErrorModalMessage).toEqual("You do not have permission to perform this action!");
    })

    test('Test that when a 401 is received, the formErrorModalMessage is updated', async () => {
        const todayDate = endOfToday();
        const inventoryItem = {
            productId: "PROD",
            quantity: 10,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: format(sub(todayDate, {months: 1}), 'yyyy-MM-dd'),
            sellBy: format(add(todayDate, {months: 1}), 'yyyy-MM-dd'),
            bestBefore: format(add(todayDate, {months: 2}), 'yyyy-MM-dd'),
            expires: format(add(todayDate, {months: 3}), 'yyyy-MM-dd')
        }

        const businessId = 1;
        const currencyCode = "NZD";
        const currencySymbol = "$";
        const value = {
            index: 1,
            id: 1,
            productName: "NAME",
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: inventoryItem.manufactured,
            sellBy: inventoryItem.sellBy,
            bestBefore: inventoryItem.bestBefore,
            expires: inventoryItem.expires,
        }
        const $router = {
            push: jest.fn()
        }
        const updateInventoryItemWrapper = await shallowMount(
            UpdateInventoryItemModal,
            {
                mocks: {
                    $router,
                },
                propsData: {
                    value,
                    businessId,
                    currencyCode,
                    currencySymbol
                },
            });

        updateInventoryItemWrapper.vm.newInventoryItem = new InventoryItem(inventoryItem);

        const data = {
            response: {
                status: 401
            }
        }

        Api.modifyInventoryItem.mockImplementation(() => Promise.reject(data));

        await updateInventoryItemWrapper.vm.updateInventoryItem(new Event('click'));
        await Promise.resolve();
        expect(updateInventoryItemWrapper.vm.formErrorModalMessage).toEqual("You must be logged in to perform this action.");
    })

    test('Test that when a 406 is received, the formErrorModalMessage is updated', async () => {
        const todayDate = endOfToday();
        const inventoryItem = {
            productId: "PROD",
            quantity: 10,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: format(sub(todayDate, {months: 1}), 'yyyy-MM-dd'),
            sellBy: format(add(todayDate, {months: 1}), 'yyyy-MM-dd'),
            bestBefore: format(add(todayDate, {months: 2}), 'yyyy-MM-dd'),
            expires: format(add(todayDate, {months: 3}), 'yyyy-MM-dd')
        }

        const businessId = 1;
        const currencyCode = "NZD";
        const currencySymbol = "$";
        const value = {
            index: 1,
            id: 1,
            productName: "NAME",
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: inventoryItem.manufactured,
            sellBy: inventoryItem.sellBy,
            bestBefore: inventoryItem.bestBefore,
            expires: inventoryItem.expires,
        }
        const $router = {
            push: jest.fn()
        }
        const updateInventoryItemWrapper = await shallowMount(
            UpdateInventoryItemModal,
            {
                mocks: {
                    $router,
                },
                propsData: {
                    value,
                    businessId,
                    currencyCode,
                    currencySymbol
                },
            });

        updateInventoryItemWrapper.vm.newInventoryItem = new InventoryItem(inventoryItem);

        const data = {
            response: {
                status: 406
            }
        }

        Api.modifyInventoryItem.mockImplementation(() => Promise.reject(data));

        await updateInventoryItemWrapper.vm.updateInventoryItem(new Event('click'));
        await Promise.resolve();
        expect(updateInventoryItemWrapper.vm.formErrorModalMessage).toEqual("Sorry, something went wrong...");
    })

})
