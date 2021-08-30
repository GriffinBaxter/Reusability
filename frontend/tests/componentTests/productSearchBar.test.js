/**
 * @jest-environment jsdom
 */

import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
import {expect, jest, describe, afterEach, test} from "@jest/globals";
import ProductSearchBar from "../../src/components/productCatalogue/ProductSearchBar";

jest.mock("../../src/Api");

let wrapper;

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false});
localVue.use(VueRouter);

afterEach(() => {
    wrapper.destroy();
});

describe("Tests for search type radio", () => {
    let radioProductNameButton;
    let radioProductIdButton;
    let radioManufacturerButton;
    let radioDescriptionButton;
    let radioAllButton;

    beforeEach(() => {
        wrapper = shallowMount(ProductSearchBar, {
            localVue,
        });

        radioProductNameButton = wrapper.find('#radio-product-name');
        radioProductIdButton = wrapper.find('#radio-product-id');
        radioManufacturerButton = wrapper.find('#radio-manufacturer');
        radioDescriptionButton = wrapper.find('#radio-description');
        radioAllButton = wrapper.find('#radio-all');

        // mock the call which gets the radio buttons
        jest.spyOn(document, 'querySelectorAll').mockImplementation(() => {
            return [radioProductNameButton.element, radioProductIdButton.element, radioManufacturerButton.element,
                radioDescriptionButton.element, radioAllButton.element];
        });
    })

    test("Test for if do not click any radio, the value will be PRODUCT_NAME", async function () {
        // Given
        expect(radioProductNameButton.exists()).toBeTruthy();

        // When
        await radioProductNameButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedRadio()).toEqual("PRODUCT_NAME")
    });

    test("Test for if click on Product Name the value will be PRODUCT_NAME", async function () {
        // Given
        expect(radioProductNameButton.exists()).toBeTruthy();

        // When
        await radioProductNameButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedRadio()).toEqual("PRODUCT_NAME")
    });

    test("Test for if click on Product Id the value will be PRODUCT_ID", async function () {
        // Given
        expect(radioProductIdButton.exists()).toBeTruthy();

        // When
        await radioProductIdButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedRadio()).toEqual("PRODUCT_ID")
    });

    test("Test for if click on Manufacturer the value will be MANUFACTURER", async function () {
        // Given
        expect(radioManufacturerButton.exists()).toBeTruthy();

        // When
        await radioManufacturerButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedRadio()).toEqual("MANUFACTURER")
    });

    test("Test for if click on Description the value will be DESCRIPTION", async function () {
        // Given
        expect(radioDescriptionButton.exists()).toBeTruthy();

        // When
        await radioDescriptionButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedRadio()).toEqual("DESCRIPTION")
    });

    test("Test for if click on All the value will be ALL", async function () {
        // Given
        expect(radioAllButton.exists()).toBeTruthy();

        // When
        await radioAllButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedRadio()).toEqual("ALL")
    });

})

describe('Tests fot enterPressed.', () => {
    let searchClickedSpy;

    beforeEach(() => {
        wrapper = shallowMount(ProductSearchBar, {
            localVue,
        });

        searchClickedSpy = jest.spyOn(ProductSearchBar.methods, 'searchClicked')
    })

    test("Testing that when a button other than the enter button is pressed the search is not executed", async () => {
        wrapper.find('#product-search-bar').trigger('keydown.space');

        const event = new Event("keydown.space", undefined)
        wrapper.vm.enterPressed(event)

        await Promise.resolve();

        expect(searchClickedSpy).toHaveBeenCalledTimes(0);

    })

})