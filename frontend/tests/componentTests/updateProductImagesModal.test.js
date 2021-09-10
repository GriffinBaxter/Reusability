/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import updateImagesModal from "../../src/components/UpdateImagesModal";
import Api from "../../src/Api";
import {describe, expect, jest, test} from "@jest/globals";
import Product from "../../src/configs/Product";
const endOfToday = require('date-fns/endOfToday');

jest.mock("../../src/Api");

const factory = (values = {}) => {
    return shallowMount(updateImagesModal, {
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
            businessId: 1,
            location: "Product"
        }
    })
}

describe("Testing the set primary image, delete and upload image functionality", () => {

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

    test('Testing an error message is sent when a user without permission tries to ' +
        'set a primary image and the frontend receives a 403 error.', async () => {
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

        Api.setPrimaryImage.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.setPrimarySelectedImage();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.formErrorModalMessage).toBe(
            "Sorry, you do not have permission to change the primary image."
        );

    })

    test('Testing an error message is sent when a user tries to ' +
        'set a primary image and the frontend receives a 406 error.', async () => {
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

        Api.setPrimaryImage.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.setPrimarySelectedImage();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.formErrorModalMessage).toBe("Sorry, something went wrong...");
    })

    test('Testing an error message is sent when a user tries to upload an invalid file (not an image).',
        async () => {
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
                status: 400
            }
        }

        Api.uploadImage.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.getImage();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.formErrorModalMessage).toBe(
            "Sorry, the file you uploaded is not a valid image."
        );

    })
})
