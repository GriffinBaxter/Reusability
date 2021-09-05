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
import ProfileImage from "../../public/sample_profile_image.jpg"
import Cookies from "js-cookie";


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
                {
                    "id": 4,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "instigatorImage": null,
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "receiverImage": null,
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:22:03.213546"
                },
                {
                    "id": 3,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "instigatorImage": null,
                    "receiverId": 5,
                    "receiverName": "Minttu Anna Wainio",
                    "receiverImage": null,
                    "marketplaceCardId": 7,
                    "marketplaceCardTitle": "Sealord Fish Fillets Hoki Classic Crumbed",
                    "created": "2021-09-05T17:20:57.783525"
                },
                {
                    "id": 2,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "instigatorImage": null,
                    "receiverId": 9,
                    "receiverName": "Lina Jose Mari Patterson",
                    "receiverImage": null,
                    "marketplaceCardId": 10,
                    "marketplaceCardTitle": "Beef Mince Premium",
                    "created": "2021-09-05T17:09:58.893898"
                },
                {
                    "id": 1,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "instigatorImage": null,
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "receiverImage": null,
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:09:38.111619"
                }
            ]
        }
        Cookies.get = jest.fn().mockImplementation(() => 21);
        Api.getConversations.mockImplementation(() => Promise.resolve(response));
        const wrapper = await factory();
        expect(wrapper.find("#conversation-1").exists()).toBeTruthy();
        expect(wrapper.find("#conversation-2").exists()).toBeTruthy();
        expect(wrapper.find("#conversation-3").exists()).toBeTruthy();
        expect(wrapper.find("#conversation-4").exists()).toBeTruthy();
        expect(wrapper.vm.$data.errorMessage).toStrictEqual("");
        expect(wrapper.find(".error-message").exists()).toBeFalsy();
    })
    test("Testing a OK with conversations returned", async () => {
        const response = {
            response: {
                status: 200,
            },
            data: [
                {
                    "id": 4,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "instigatorImage": null,
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "receiverImage": null,
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:22:03.213546"
                },
                {
                    "id": 1,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "instigatorImage": null,
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "receiverImage": null,
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:09:38.111619"
                }
            ]
        }
        Cookies.get = jest.fn().mockImplementation(() => 8);
        Api.getConversations.mockImplementation(() => Promise.resolve(response));
        const wrapper = await factory();
        // ids (e.g 1) are based on conversation id.
        expect(wrapper.find("#conversation-1").exists()).toBeTruthy();
        expect(wrapper.find("#conversation-2").exists()).toBeFalsy();
        expect(wrapper.find("#conversation-3").exists()).toBeFalsy();
        expect(wrapper.find("#conversation-4").exists()).toBeTruthy();
        expect(wrapper.vm.$data.errorMessage).toStrictEqual("");
        expect(wrapper.find(".error-message").exists()).toBeFalsy();
    })
})

describe("Testing MessageOption.vue", () => {

    const factory = async (username, cardName) => {
        const wrapper = await shallowMount(MessageOption, {
            localVue,
            propsData: {
                userName: username,
                image: ProfileImage,
                newMessage: true,
                cardName: cardName
            }
        });
        await wrapper.vm.$nextTick();

        return wrapper;
    }

    test("Testing the length of the card name under the character limit", async () => {
        const wrapper = await factory("username", "0 123 456 789");
        expect(wrapper.find(".card-name").text()).toStrictEqual("0 123 456 789");
    })

    test("Testing the length of the card name on the character limit", async () => {
        const wrapper = await factory("username", "0 123 456 7893");
        expect(wrapper.find(".card-name").text()).toStrictEqual("0 123 456 7...");
    })

    test("Testing the length of the card name over the character limit", async () => {
        const wrapper = await factory("username", "0 123 456 78933");
        expect(wrapper.find(".card-name").text()).toStrictEqual("0 123 456 7...");
    })

    test("Testing the length of the user name under the character limit", async () => {
        const wrapper = await factory( "123450 123 456 789", "cardname");
        expect(wrapper.find(".user-name").text()).toStrictEqual("123450 123 456 789");
    })

    test("Testing the length of the user name over the character limit", async () => {
        const wrapper = await factory( "123450 123 456 7893", "cardname");
        expect(wrapper.find(".user-name").text()).toStrictEqual("123450 123 456 7...");
    })

    test("Testing the length of the user name over the character limit", async () => {
        const wrapper = await factory( "123450 123 456 78933", "cardname");
        expect(wrapper.find(".user-name").text()).toStrictEqual("123450 123 456 7...");
    })
})