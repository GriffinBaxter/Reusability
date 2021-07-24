/**
 * @jest-environment jsdom
 */

import {shallowMount, createLocalVue} from '@vue/test-utils';
import updateProductImagesModal from "../src/components/productCatalogue/UpdateProductImagesModal.vue";
import Api from "../src/Api";
import {describe, expect, jest, test} from "@jest/globals";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
import Cookies from "js-cookie"

describe("Testing the delete button functionality", () => {

    test('Testing the delete button is not "visible" when no image is selected', () => {

        const wrapper = shallowMount(
            updateProductImagesModal,
            {
                propsData: {
                    selectedImage: null // no image is selected
                },
            });

        expect(wrapper.find('.noListings').exists()).toBeTruthy()
    })

    test('Testing the delete button is "visible" when image is selected', () => {

    })

    test('Testing the selected image is removed from the list of images (deleted) ' +
        'when delete button is clicked and a 200 is received from the backend', () => {

    })

    test('Testing an error message is displayed when a user without permission to delete an image' +
        'clicks the delete button and the frontend receives a 403 error.', () => {

    })

    test('Testing an error message is displayed when a user tries to delete an image' +
        'and clicks the delete button and the frontend receives a 406 error.', () => {

    })


})