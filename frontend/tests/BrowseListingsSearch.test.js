/**
 * Jest tests for Marketplace.vue.
 * @jest-environment jsdom
 */

import { shallowMount } from '@vue/test-utils'
import {beforeAll, describe, expect, jest} from "@jest/globals";

import BrowseListingsSearch from "../src/components/listing/BrowseListingsSearch";
import Api from "../src/Api"

describe("Testing the BrowseListingsSearch methods", () => {

    let browseListingsSearchWrapper;
    let data;
    let $route;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/browseListings',
            query: {'orderBy': "createdASC", 'page': "1"} // TODO!
        }
        data = {
            status: 200,
            data: [
                {

                }]
        }

        // Api.getAllCards().mockImplementation(() => Promise.resolve(data));

        browseListingsSearchWrapper = shallowMount(
            BrowseListingsSearch,
            {
                mocks: {
                    $router,
                    $route
                }
            });

        Promise.resolve();


    });

    describe('Tests the enterPressed method.', () => {

        test("Testing that when the enter button is pressed that the search is executed", () => {

        })


    }),

    describe('Tests the getSelectedRadio method.', () => {

        test("Testing that when the Product Name radio button is checked that it is correctly selected", () => {

            const type = 'match';
            browseListingsSearchWrapper.vm.searchType = 'Business Name';
            expect(browseListingsSearchWrapper.vm.searchType).toEqual('Business Name');

            let radioButton = browseListingsSearchWrapper.find('#radio-product-name');
            radioButton.trigger('click');


            expect(browseListingsSearchWrapper.vm.getSelectedRadio(type)).toEqual('Product Name');

        })


    }),

    describe('Tests the searchClicked method.', () => {

    }),

    describe('Tests the clearRadios method.', () => {

    }),

    describe('Tests the setOrderByOption method.', () => {

    }),

    describe('Tests the validatePriceInput method.', () => {

    }),

    describe('Tests the validateDateInput method.', () => {

    }),

    describe('Tests the clearFilters method.', () => {

    })

})
