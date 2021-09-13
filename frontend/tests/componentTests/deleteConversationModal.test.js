/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import DeleteConversationModal from "../../src/components/messages/DeleteConversationModal";
import Api from "../../src/Api";
import {beforeEach, describe, expect, jest, test} from "@jest/globals";

jest.mock("../../src/Api");

describe("Testing the deleteConversation method.", () => {

    let wrapper;
    let $router;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };

        wrapper = shallowMount(
            DeleteConversationModal,
            {
                mocks: {
                    $router
                },
                propsData: {
                    userName: "Rene Rider",
                    id: 1
                }
            });
    });

    test('Testing an error message is set when a user without permission to delete a conversation' +
        'tries to delete a conversation and receives a 403 error.', async () => {
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

    test('Testing user is redirected to timeout page when no response is received', async () => {
        const data = {
            request:true
        }

        Api.deleteConversation.mockImplementation(() => Promise.reject(data));

        await wrapper.vm.deleteConversation();
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: '/timeout'});
    })

})
