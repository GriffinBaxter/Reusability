/**
 * @jest-environment jsdom
 */

import {test, expect, describe, jest, beforeAll} from "@jest/globals"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import ProductCatalogue from "../src/views/ProductCatalogue";
import VueRouter from "vue-router";

jest.mock("../src/Api");
jest.mock("../src/currencyInstance");
jest.mock("../src/openFoodFactsInstance");
jest.mock("js-cookie");

// ---------------------------------------------- Search Bar Tests -----------------------------------------------------

describe('Tests the URL populates correctly when searching for all relevant products.', () => {

    let productCatalogueSearchWrapper;
    let router;

    beforeAll(() => {
        const localVue = createLocalVue();
        localVue.use(VueRouter)

        const routes = [{path: '/productCatalogue', component: ProductCatalogue, name: 'ProductCatalogue'}]
        router = new VueRouter({
            routes
        })
        router.push({
            name: 'ProductCatalogue',
        })
        productCatalogueSearchWrapper = shallowMount(ProductCatalogue, {
            localVue,
            router
        });
    });

    test('Testing that pressing enter populates the URL correctly', async() => {
        let inputQuery = 'Product Catalogue Search Enter Test';
        let expectedQuery = 'Product%Catalogue%20Search%20Enter%20Test';
        productCatalogueSearchWrapper.vm.$refs.searchInput.value = inputQuery;

        let searchBar = productCatalogueSearchWrapper.find('#search-bar');
        searchBar.trigger('keydown.enter');

        await productCatalogueSearchWrapper.vm.$nextTick();

        expect(router.currentRoute.name).toBe('ProductCatalogue')
        expect(router.currentRoute.fullPath).toBe(`/productCatalogue?searchQuery=${expectedQuery}&barcode`)
    });

    test('Testing that clicking the search button populates the URL correctly', () => {
        let inputQuery = 'Product Catalogue Search Click Test';
        let expectedQuery = 'User%20Search%20Click%20Test';
        productCatalogueSearchWrapper.vm.$refs.searchInput.value = inputQuery;

        productCatalogueSearchWrapper.vm.$nextTick().then(() => {
            let searchButton = productCatalogueSearchWrapper.find('#search-button');
            searchButton.trigger('click');

            expect(router.currentRoute.name).toBe('ProductCatalogue')
            expect(router.currentRoute.fullPath).toBe(`/productCatalogue?searchQuery=${expectedQuery}&barcode`)
        });
    });
})

// --------------------------------------- Barcode Search Requirement Tests --------------------------------------------

describe('Tests the barcode search elements.', () => {

    let productCatalogueSearchWrapper;
    let router;

    beforeAll(() => {
        const localVue = createLocalVue();
        localVue.use(VueRouter)

        const routes = [{path: '/productCatalogue', component: ProductCatalogue, name: 'ProductCatalogue'}]
        router = new VueRouter({
            routes
        })
        router.push({
            name: 'BrowseListings',
        })
        productCatalogueSearchWrapper = shallowMount(ProductCatalogue, {
            localVue,
            router
        });
    });


    test('Testing that pressing the camera button opens the barcode scanning model', () => {
        // TODO

    });

    test('Testing that the barcode input box is filled with the barcode from the barcode scanner', () => {
        // TODO

    });

    test('Testing that pressing the clear button for the barcode input box clears the barcode when it is present', () => {
        // TODO
    });

    test('Testing that pressing the clear button for the barcode input box has no impact when there is not barcode present', () => {
        // TODO
    });

})