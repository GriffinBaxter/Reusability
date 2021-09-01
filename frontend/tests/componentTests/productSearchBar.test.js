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
        console.log(wrapper.html())

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

    test("Test for if do not click any checkbox, the value will be PRODUCT_NAME", async function () {
        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["PRODUCT_NAME"])
    });

    test("Test for if click on Product Name (unselected) the value will be PRODUCT_NAME", async function () {
        // When
        await checkboxProductNameButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["PRODUCT_NAME"])
    });

    test("Test for if click on Product Id the value will be PRODUCT_ID", async function () {
        // Given
        await checkboxProductNameButton.trigger('click');

        // When
        await checkboxProductIdButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["PRODUCT_ID"])
    });

    test("Test for if click on Manufacturer the value will be MANUFACTURER", async function () {
        // Given
        await checkboxProductNameButton.trigger('click');

        // When
        await checkboxManufacturerButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["MANUFACTURER"])
    });

    test("Test for if click on Description the value will be DESCRIPTION", async function () {
        // Given
        await checkboxProductNameButton.trigger('click');

        // When
        await checkboxDescriptionButton.trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["DESCRIPTION"])
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
        expect(wrapper.vm.getSelectedCheckbox()).toEqual(["PRODUCT_NAME", "PRODUCT_ID", "MANUFACTURER", "DESCRIPTION"])
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