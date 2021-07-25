/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import updateProductImagesModal from "../../src/components/productCatalogue/UpdateProductImagesModal";
import Api from "../../src/Api";
import {describe, expect, jest, test} from "@jest/globals";
import Product from "../../src/configs/Product";
const endOfToday = require('date-fns/endOfToday');

jest.mock("../../src/Api");

const factory = (values = {}) => {
    return shallowMount(updateProductImagesModal, {
        data () {
            return {
                ...values
            }
        },
        propsData: {
            value: new Product({
                id: "VEGE",
                name:"Lettuce",
                description: "Green and fresh",
                manufacturer: "Pams",
                recommendedRetailPrice: 1.99,
                created: endOfToday(),
                images: [
                    {filename: "/fakeImage1.jpg", id: 1, isPrimary: true},
                    {filename: "/fakeImage2.png", id: 2, isPrimary: false}
                ]
            }),
            businessId: 1
        }
    })
}

describe("Testing the delete image functionality", () => {

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

    test('Testing an error message is set when a user without permission to delete an image' +
        'tries to delete an image and the frontend receives a 403 error.', async () => {
        const $router = {
            push: jest.fn()
        };
        const wrapper = await factory({
            mocks: {
                $router
            }
        });

        const data = {
            response: {
                status: 403
            }
        }

        Api.deleteProductImage.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.deleteSelectedImage();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.formErrorModalMessage).toBe("Sorry, you do not have permission to delete this image.");

    })

    test('Testing an error message is set when a user tries to delete an image' +
        'and the frontend receives a 406 error.', async () => {
        const $router = {
            push: jest.fn()
        };
        const wrapper = await factory({
            mocks: {
                $router
            }
        });

        const data = {
            response: {
                status: 406
            }
        }

        Api.deleteProductImage.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.deleteSelectedImage();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.formErrorModalMessage).toBe("Sorry, something went wrong...");
    })


})