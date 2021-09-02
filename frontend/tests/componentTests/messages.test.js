/**
 * Jest tests for Messages.vue, MessageOption.vue .
 * @jest-environment jsdom
 */

import {test, expect, describe, beforeEach, afterEach, jest} from "@jest/globals"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import Messages from "../../src/components/messages/Messages";
import MessageOption from "../../src/components/messages/MessageOption";
import Api from "../../src/Api";
import VueLogger from "vuejs-logger";


jest.mock("../../src/Api");
jest.mock("js-cookie")
const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false})
let $router;

describe("Testing the Messages.vue component", () => {

    beforeEach( () => {
        $router = {
            push: jest.fn(),
        }
    })

    afterEach( () => {
        jest.clearAllMocks();
    })

    const factory = async () => {
        const wrapper = await shallowMount(Messages, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        return wrapper;
    }

    const testErrorMessageDisappear = async (wrapper) => {
        setTimeout(async () => {
            await wrapper.vm.$nextTick();
            expect(wrapper.find(".error-message").exists()).toBeFalsy();
        }, 300)
    }

    test("Testing a 401 response sends you to the invalid token route", async () => {
        const response = {
            response: {
                status: 401
            }
        }

        Api.getConversations.mockImplementation(() => Promise.reject(response));
        await factory();
        expect($router.push).toHaveBeenCalledWith({path: '/invalidtoken'});
    })

    test("Testing a random error displays an error message", async () => {
        const response = {
            response: {
                status: 500
            }
        }

        Api.getConversations.mockImplementation(() => Promise.reject(response));
        const wrapper = await factory();
        expect(wrapper.vm.$data.errorMessage).toStrictEqual("Something went wrong sorry.")
        expect(wrapper.find(".error-message").exists()).toBeTruthy();
        expect(wrapper.find(".error-message").text()).toStrictEqual("Something went wrong sorry.");
        await testErrorMessageDisappear(wrapper);
    })

    test("Testing a timeout error returns a error message", async () => {
        const response = {
            request: true
        }

        Api.getConversations.mockImplementation(() => Promise.reject(response));
        const wrapper = await factory();
        expect(wrapper.vm.$data.errorMessage).toStrictEqual("Timed out getting conversations.")
        expect(wrapper.find(".error-message").exists()).toBeTruthy();
        expect(wrapper.find(".error-message").text()).toStrictEqual("Timed out getting conversations.");
        await testErrorMessageDisappear(wrapper);
    })

    test("Testing a setup error returns a error message", async () => {
        const response = {};

        Api.getConversations.mockImplementation(() => Promise.reject(response));
        const wrapper = await factory();
        expect(wrapper.vm.$data.errorMessage).toStrictEqual("Something went wrong sorry.")
        expect(wrapper.find(".error-message").exists()).toBeTruthy();
        expect(wrapper.find(".error-message").text()).toStrictEqual("Something went wrong sorry.");
        await testErrorMessageDisappear(wrapper);

    })

    test("Testing a OK with no conversations returned", async () => {
        const response = {
            response: {
                status: 200,
            },
            data: []
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response));
        const wrapper = await factory();
        expect(wrapper.vm.$data.errorMessage).toStrictEqual("No messages found.")
        expect(wrapper.find(".error-message").exists()).toBeTruthy();
        expect(wrapper.find(".error-message").text()).toStrictEqual("No messages found.");
    })

    test("Testing a OK with conversations returned", async () => {
        const response = {
            response: {
                status: 200,
            },
            data: [
                {id: 1, instigatorName: "Testy", marketplaceCardTitle: "YARRR", created: "2019-2-1"},
                {id: 2, instigatorName: "Testy", marketplaceCardTitle: "YARRR", created: "2019-2-1"},
                {id: 3, instigatorName: "Testy", marketplaceCardTitle: "YARRR", created: "2019-2-1"}
            ]
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response));
        const wrapper = await factory();
        expect(wrapper.find("#conversation-1").exists()).toBeTruthy();
        expect(wrapper.find("#conversation-2").exists()).toBeTruthy();
        expect(wrapper.find("#conversation-3").exists()).toBeTruthy();
        expect(wrapper.vm.$data.errorMessage).toStrictEqual("");
        expect(wrapper.find(".error-message").exists()).toBeFalsy();
    })
})

describe("Testing MessageOption.vue", () => {

    test("Testing the length of the card name under the character limit", async () => {})

    test("Testing the length of the card name over the character limit", async () => {})

    test("Testing the length of the user name under the character limit", async () => {})

    test("Testing the length of the user name over the character limit", async () => {})
})