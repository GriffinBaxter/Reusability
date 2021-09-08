/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import DeleteConversationModal from "../../src/components/messages/DeleteConversationModal";
import Api from "../../src/Api";
import {describe, expect, jest, test} from "@jest/globals";

jest.mock("../../src/Api");

const factory = (values = {}) => {
    return shallowMount(DeleteConversationModal, {
        data () {
            return {
                ...values
            }
        },
        propsData: {
            userName: "Rene Rider",
            id: 2
        }
    })
}

describe("Testing the deleteConversation method.", () => {

    test('Testing an error message is set when a user without permission to delete a conversation' +
        'tries to delete a conversation and receives a 403 error.', async () => {
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

        Api.deleteConversation.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.deleteConversation();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.formErrorModalMessage).toBe("Sorry, you do not have permission to delete this conversation.");
    })

    test('Testing an error message is set when a user tries to delete a conversation' +
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

        Api.deleteConversation.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.deleteConversation();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.formErrorModalMessage).toBe("Sorry, something went wrong...");
    })

    test('Testing a custom event is emitted when deletion is successful.', async () => {
        const $router = {
            push: jest.fn()
        };
        const wrapper = await factory({
            mocks: {
                $router
            }
        });

        const data = {
            status: 200
        }

        Api.deleteConversation.mockImplementation(() => Promise.resolve(data));

        await wrapper.vm.deleteConversation();
        await wrapper.vm.$nextTick();

        expect(wrapper.emitted().conversationSuccessfullyDeleted).toBeTruthy();
    })

    test('Testing an error message is set when deletion is successful but an unknown error occurs, such' +
        'as receiving a 204 (No Content) instead of a 200 (OK)', async () => {
        const $router = {
            push: jest.fn()
        };
        const wrapper = await factory({
            mocks: {
                $router
            }
        });

        const data = {
            status: 204
        }

        Api.deleteConversation.mockImplementation(() => Promise.resolve(data));

        await wrapper.vm.deleteConversation();
        await wrapper.vm.$nextTick();

        expect(wrapper.emitted().conversationSuccessfullyDeleted).toBeFalsy();
        expect(wrapper.vm.formErrorModalMessage).toBe("Sorry, something went wrong...");
    })

    test('Testing user is redirected to timeout page when a random (500) error occurs', async () => {
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
                status: 500
            }
        }

        Api.deleteConversation.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.deleteConversation();
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: '/timeout'});
    })

})
