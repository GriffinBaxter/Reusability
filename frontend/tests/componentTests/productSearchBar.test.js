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

describe("Tests for search type checkbox", () => {
    let checkboxProductNameButton;
    let checkboxProductIdButton;
    let checkboxManufacturerButton;
    let checkboxDescriptionButton;

    beforeEach(() => {
        wrapper = shallowMount(ProductSearchBar, {
            localVue,
        });

        checkboxProductNameButton = wrapper.find('#checkbox-product-name');
        checkboxProductIdButton = wrapper.find('#checkbox-product-id');
        checkboxManufacturerButton = wrapper.find('#checkbox-manufacturer');
        checkboxDescriptionButton = wrapper.find('#checkbox-description');

        expect(checkboxProductNameButton.exists()).toBeTruthy();
        expect(checkboxProductIdButton.exists()).toBeTruthy();
        expect(checkboxManufacturerButton.exists()).toBeTruthy();
        expect(checkboxDescriptionButton.exists()).toBeTruthy();

        // mock the call which gets the checkbox buttons
        jest.spyOn(document, 'querySelectorAll').mockImplementation(() => {
            return [checkboxProductNameButton.element, checkboxProductIdButton.element,
                checkboxManufacturerButton.element, checkboxDescriptionButton.element];
        });

        jest.spyOn(document, 'getElementById').mockImplementation(() => {
            return checkboxProductNameButton.element;
        })
    })

    test("Test for if do not click any checkbox, the value will be productName", async function () {
        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["productName"])
    });

    test("Test for if click on Product Name (unselected) the value will be productName", async function () {
        // When
        await checkboxProductNameButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["productName"])
    });

    test("Test for if click on Product Id the value will be productId", async function () {
        // Given
        await checkboxProductNameButton.trigger('click');

        // When
        await checkboxProductIdButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["productId"])
    });

    test("Test for if click on Manufacturer the value will be manufacturer", async function () {
        // Given
        await checkboxProductNameButton.trigger('click');

        // When
        await checkboxManufacturerButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["manufacturer"])
    });

    test("Test for if click on Description the value will be description", async function () {
        // Given
        await checkboxProductNameButton.trigger('click');

        // When
        await checkboxDescriptionButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["description"])
    });

    test("Test for if click on all the value will be all of them", async function () {
        // Given
        await checkboxProductNameButton.trigger('click');

        // When
        await checkboxProductNameButton.trigger('click');
        await checkboxProductIdButton.trigger('click');
        await checkboxManufacturerButton.trigger('click');
        await checkboxDescriptionButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["productName", "productId", "manufacturer", "description"])
    });

})

describe('Tests for enterPressed.', () => {
    let searchClickedSpy;
    let checkboxProductNameButton;
    let checkboxProductIdButton;
    let checkboxManufacturerButton;
    let checkboxDescriptionButton;

    beforeEach(() => {
        searchClickedSpy = jest.spyOn(ProductSearchBar.methods, 'searchClicked');

        wrapper = shallowMount(ProductSearchBar, {
            localVue,
        });

        checkboxProductNameButton = wrapper.find('#checkbox-product-name');
        checkboxProductIdButton = wrapper.find('#checkbox-product-id');
        checkboxManufacturerButton = wrapper.find('#checkbox-manufacturer');
        checkboxDescriptionButton = wrapper.find('#checkbox-description');

        expect(checkboxProductNameButton.exists()).toBeTruthy();
        expect(checkboxProductIdButton.exists()).toBeTruthy();
        expect(checkboxManufacturerButton.exists()).toBeTruthy();
        expect(checkboxDescriptionButton.exists()).toBeTruthy();

        // mock the call which gets the checkbox buttons
        jest.spyOn(document, 'querySelectorAll').mockImplementation(() => {
            return [checkboxProductNameButton.element, checkboxProductIdButton.element,
                checkboxManufacturerButton.element, checkboxDescriptionButton.element];
        });

        jest.spyOn(document, 'getElementById').mockImplementation(() => {
            return checkboxProductNameButton.element;
        })
    })

    test("Testing that when a button other than the enter button is pressed the search is not executed", async () => {
        wrapper.find('#product-search-bar').trigger('keydown.space');
        expect(searchClickedSpy).toHaveBeenCalledTimes(0);
    })

    test("Testing that when the enter button is pressed the search is executed", async () => {
        wrapper.find('#product-search-bar').trigger('keydown.enter');
        expect(searchClickedSpy).toHaveBeenCalledTimes(1);
    })

    test("Testing that the searchClicked method emits an event", async () => {
        wrapper.vm.$refs.searchInput.value = "Hello";
        wrapper.vm.searchClicked();
        expect(wrapper.emitted().search).toBeTruthy();
        expect(wrapper.emitted().search[0]).toEqual([["productName"], "Hello"]);
    })

})
