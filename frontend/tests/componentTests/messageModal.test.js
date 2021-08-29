/**
 * Jest tests for MessageModal.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe, beforeEach, afterEach, jest} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import MessageModal from "../../src/components/marketplace/MessageModal";
import Api from "../../src/Api";

describe("Testing the message modal functionality", () => {

    let messageModalWrapper;
    let sendMessageSpy;
    let $router;

    beforeEach(() => {
        const creator = "Name";
        const creatorId = 1;
        const card = "Card";
        const cardId = 2;

        $router = {
            push: jest.fn()
        };

        messageModalWrapper = shallowMount(
            MessageModal,
            {
                mocks: {
                    $router
                },
                propsData: {
                    creator,
                    creatorId,
                    card,
                    cardId
                }
            });

        sendMessageSpy = jest.spyOn(Api, 'sendMessage');

        Api.sendMessage.mockImplementation(() => Promise.resolve());
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test('Testing that when message length is less than 1 then the modal error is set and the API call is not made', () => {
        messageModalWrapper.vm.message = "";

        messageModalWrapper.vm.sendMessage();

        expect(messageModalWrapper.vm.modalError).toBe("Message length must be between 1 and 300 characters long");
        expect(sendMessageSpy).toHaveBeenCalledTimes(0);
    });

    test('Testing that when message length is greater than 300 then the modal error is set and the API call is not made', () => {
        messageModalWrapper.vm.message = "a".repeat(301);

        messageModalWrapper.vm.sendMessage();

        expect(messageModalWrapper.vm.modalError).toBe("Message length must be between 1 and 300 characters long");
        expect(sendMessageSpy).toHaveBeenCalledTimes(0);
    });

    test('Testing that when message length is 1 then the modal error is not set and the API call is made', async () => {
        messageModalWrapper.vm.message = "a";

        await messageModalWrapper.vm.sendMessage();
        await Promise.resolve();

        expect(messageModalWrapper.vm.modalError).toBe("");
        expect(messageModalWrapper.vm.message).toBe("");
        expect(sendMessageSpy).toHaveBeenCalledTimes(1);
    });

    test('Testing that when message length is 300 then the modal error is not set and the API call is made', async () => {
        messageModalWrapper.vm.message = "a".repeat(300);

        await messageModalWrapper.vm.sendMessage();
        await Promise.resolve();

        expect(messageModalWrapper.vm.modalError).toBe("");
        expect(messageModalWrapper.vm.message).toBe("");
        expect(sendMessageSpy).toHaveBeenCalledTimes(1);
    });

    test('Testing that when a 400 error is received then the modal error is set to the error message', async () => {
        let response = {
            response: {
                status: 400,
                data: "Error Message"
            }
        }
        Api.sendMessage.mockImplementation(() => Promise.reject(response));

        messageModalWrapper.vm.message = "a";

        await messageModalWrapper.vm.sendMessage();
        await Promise.resolve();

        expect(messageModalWrapper.vm.modalError).toBe("Error Message");
        expect(messageModalWrapper.vm.message).toBe("a");
    });

    test('Testing that when a 401 error is received then the page routes to /invalidtoken', async () => {
        let response = {
            response: {
                status: 401,
                data: "Error Message"
            }
        }
        Api.sendMessage.mockImplementation(() => Promise.reject(response));

        messageModalWrapper.vm.message = "a";

        await messageModalWrapper.vm.sendMessage();
        await Promise.resolve();

        expect(messageModalWrapper.vm.modalError).toBe("");
        expect(messageModalWrapper.vm.message).toBe("a");
        expect($router.push).toHaveBeenCalledWith({ path: `/invalidtoken`});
    });

    test('Testing that when a 406 error is received then the page routes to /noCard', async () => {
        let response = {
            response: {
                status: 406,
                data: "Error Message"
            }
        }
        Api.sendMessage.mockImplementation(() => Promise.reject(response));

        messageModalWrapper.vm.message = "a";

        await messageModalWrapper.vm.sendMessage();
        await Promise.resolve();

        expect(messageModalWrapper.vm.modalError).toBe("");
        expect(messageModalWrapper.vm.message).toBe("a");
        expect($router.push).toHaveBeenCalledWith({ path: `/noCard`});
    });

    test('Testing that when an error is received with no response and a require then the page routes to /timeout', async () => {
        let response = {
            require: {

            }
        }
        Api.sendMessage.mockImplementation(() => Promise.reject(response));

        messageModalWrapper.vm.message = "a";

        await messageModalWrapper.vm.sendMessage();
        await Promise.resolve();

        expect(messageModalWrapper.vm.modalError).toBe("");
        expect(messageModalWrapper.vm.message).toBe("a");
        expect($router.push).toHaveBeenCalledWith({ path: `/timeout`});
    });

    test('Testing that when a different error is received then the page routes to /timeout', async () => {
        let response = {
            response: {
                status: 500,
                data: "Error Message"
            }
        }
        Api.sendMessage.mockImplementation(() => Promise.reject(response));

        messageModalWrapper.vm.message = "a";

        await messageModalWrapper.vm.sendMessage();
        await Promise.resolve();

        expect(messageModalWrapper.vm.modalError).toBe("");
        expect(messageModalWrapper.vm.message).toBe("a");
        expect($router.push).toHaveBeenCalledWith({ path: `/timeout`});
    });

    test('Testing that the reset method resets the modal error and message', () => {
        messageModalWrapper.vm.message = "a";
        messageModalWrapper.vm.modalError = "error"

        expect(messageModalWrapper.vm.modalError).toBe("error");
        expect(messageModalWrapper.vm.message).toBe("a");

        messageModalWrapper.vm.reset();

        expect(messageModalWrapper.vm.modalError).toBe("");
        expect(messageModalWrapper.vm.message).toBe("");
    });

})