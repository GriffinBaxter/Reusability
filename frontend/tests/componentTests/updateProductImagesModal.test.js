/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import updateProductImagesModal from "../../src/components/productCatalogue/UpdateProductImagesModal";
import Api from "../../src/Api";
import {describe, expect, test} from "@jest/globals";
import Product from "../../src/configs/Product";
const endOfToday = require('date-fns/endOfToday');

const factory = (values = {}) => {
    return shallowMount(updateProductImagesModal, {
        data () {
            return {
                ...values
            }
        },
        propsData: {
            value: new Product(
                "VEGE",
                "Lettuce",
                "Green and fresh",
                "Pams",
                1.99,
                endOfToday(),
                [
                    {filename:"/fakeImage1.jpg", id: 1, isPrimary: true},
                    {filename:"/fakeImage2.png", id: 2, isPrimary: false}
                ]
            ),
            businessId: 1
        }
    })
}

describe("Testing the delete button functionality", () => {

    test('Testing the component containing the delete button is not "visible" when no image is selected', () => {
        const wrapper = factory({
            selectedImage: null // no image is selected
        });
        expect(wrapper.find('.actionButtons').exists()).toBeFalsy();
    })

    test('Testing the component containing the delete button is "visible" when image is selected', () => {
        const wrapper = factory({
            selectedImage: 1 // id of selected image
        });
        expect(wrapper.find('.actionButtons').exists()).toBeTruthy();
    })

    test('Testing the selected image is removed from the list of images (deleted) ' +
        'when delete button is clicked and a 200 is received from the backend', async () => {
        const initialImages = [
            {filename:"/fakeImage1.jpg", id: 1, isPrimary: true},
            {filename:"/fakeImage2.png", id: 2, isPrimary: false}
        ];

        const wrapper = factory({
            selectedImage: 1, // id of selected image
            images: initialImages
        });

        const data = {
            response: {
                status: 200
            }
        }

        Api.deleteProductImage.mockImplementation(() => Promise.resolve(data));

        expect(wrapper.vm.images).toBe(initialImages);

        const button = wrapper.find('button')
        button.trigger('click')

        await wrapper.vm.$nextTick();

        // the primary image has been removed from list
        expect(wrapper.vm.images).toBe([{filename:"/fakeImage2.png", id: 2, isPrimary: false}]);
    })

    test('Testing an error message is displayed when a user without permission to delete an image' +
        'clicks the delete button and the frontend receives a 403 error.', () => {

    })

    test('Testing an error message is displayed when a user tries to delete an image' +
        'and clicks the delete button and the frontend receives a 406 error.', () => {

    })


})