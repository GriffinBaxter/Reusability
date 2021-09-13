/**
 * Jest tests for ListingItem.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe, beforeAll} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import ListingItem from "../../src/components/listing/ListingItem";

describe("Testing the listing item functionality", () => {

    let listingItemWrapper;

    beforeAll(() => {
        listingItemWrapper = shallowMount(ListingItem, {
            propsData: {
                productName: "Product",
                productId: "PROD",
                quantity: 10,
                price: 20.00,
                listDate: "10th September 2021",
                closeDate: "20th October 2022",
                fullCloseDate: "2022-10-20T00:00",
                expires: "20th October 2022",
                images: [],
                isAdmin: true
            }
        });
    });

    describe("Testing the checkClosed method", () => {

        test("Testing that closed is set to true when fullCloseDate is equal to the current date and time", () => {
            let date = "2022-10-20T00:00";

            jest.spyOn(Date, 'now').mockReturnValue(Date.parse(date));

            listingItemWrapper.vm.checkClosed();

            expect(listingItemWrapper.vm.closed).toBe(true);
        });

        test("Testing that closed is set to true when fullCloseDate is before the current date and time", () => {
            let date = "2022-10-21T00:00";

            jest.spyOn(Date, 'now').mockReturnValue(Date.parse(date));

            listingItemWrapper.vm.checkClosed();

            expect(listingItemWrapper.vm.closed).toBe(true);
        });

        test("Testing that closed is set to false when fullCloseDate is after the current date and time", () => {
            let date = "2020-10-21T00:00";

            jest.spyOn(Date, 'now').mockReturnValue(Date.parse(date));

            listingItemWrapper.vm.checkClosed();

            expect(listingItemWrapper.vm.closed).toBe(false);
        });

    });

    describe("Testing the visibility of the withdraw button and closed label", () => {

        test("Testing that when closed is true and isAdmin is true then the closed label is visible and the withdraw button is not", () => {
            listingItemWrapper.vm.closed = true;
            listingItemWrapper.setProps({ isAdmin: true });

            listingItemWrapper.vm.$nextTick(() => {
                expect(listingItemWrapper.find("#closed-label").exists()).toBeTruthy();
                expect(listingItemWrapper.find("#withdraw-button").exists()).toBeFalsy();
            });
        });

        test("Testing that when closed is true and isAdmin is false then the closed label is visible and the withdraw button is not", () => {
            listingItemWrapper.vm.closed = true;
            listingItemWrapper.setProps({ isAdmin: false });

            listingItemWrapper.vm.$nextTick(() => {
                expect(listingItemWrapper.find("#closed-label").exists()).toBeTruthy();
                expect(listingItemWrapper.find("#withdraw-button").exists()).toBeFalsy();
            });
        });

    });

})
