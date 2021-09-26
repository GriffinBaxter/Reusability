/**
 * Jest tests for Messages.vue, MessageOption.vue, SendMessages.vue, MessageTitle.vue .
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
import SendMessage from "../../src/components/messages/SendMessage";
import MessageTitle from "../../src/components/messages/MessageTitle";


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
            },
            data() {
                return {
                    conversations: [
                        {
                            images: [
                                {
                                    "id": 1,
                                    "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                                    "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                                    "isPrimary": true
                                }
                            ],
                            userName: "Test",
                            id: 1,
                            newMessage: false,
                            cardName: "Test"
                        }
                    ]
                }
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
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:22:03.213546",
                    "receiverImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }],
                    "instigatorImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }]
                },
                {
                    "id": 3,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "receiverId": 5,
                    "receiverName": "Minttu Anna Wainio",
                    "marketplaceCardId": 7,
                    "marketplaceCardTitle": "Sealord Fish Fillets Hoki Classic Crumbed",
                    "created": "2021-09-05T17:20:57.783525",
                    "receiverImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }],
                    "instigatorImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }]
                },
                {
                    "id": 2,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "receiverId": 9,
                    "receiverName": "Lina Jose Mari Patterson",
                    "marketplaceCardId": 10,
                    "marketplaceCardTitle": "Beef Mince Premium",
                    "created": "2021-09-05T17:09:58.893898",
                    "receiverImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }],
                    "instigatorImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }]
                },
                {
                    "id": 1,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:09:38.111619",
                    "receiverImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }],
                    "instigatorImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }]
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
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:22:03.213546",
                    "receiverImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }],
                    "instigatorImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }]
                },
                {
                    "id": 1,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:09:38.111619",
                    "receiverImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }],
                    "instigatorImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }]
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

    test("Testing the emitDeleteConversation method emits an event.", async () => {
        const id = 1;
        const userName = "Tom Edwards";
        Cookies.get = jest.fn().mockImplementation(() => 8);
        Api.getConversations.mockImplementation(() => Promise.resolve());
        const wrapper = await factory();
        wrapper.vm.emitDeleteConversation(id, userName);
        expect(wrapper.emitted().emittedDeleteConversation).toBeTruthy();
        expect(wrapper.emitted().emittedDeleteConversation[0]).toEqual([id, userName]);
    })


    describe("Testing the openConversation method", () => {
        let wrapper;

        beforeEach(async () => {
            const response = {
                response: {
                    status: 200,
                },
                data: [
                    {
                        "id": 4,
                        "instigatorId": 21,
                        "instigatorName": "John S Doe",
                        "receiverId": 8,
                        "receiverName": "Frank J Smith",
                        "marketplaceCardId": 9,
                        "marketplaceCardTitle": "Beef Frying",
                        "created": "2021-09-05T17:22:03.213546",
                        "receiverImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }],
                        "instigatorImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }]
                    },
                    {
                        "id": 3,
                        "instigatorId": 21,
                        "instigatorName": "John S Doe",
                        "receiverId": 5,
                        "receiverName": "Minttu Anna Wainio",
                        "marketplaceCardId": 7,
                        "marketplaceCardTitle": "Sealord Fish Fillets Hoki Classic Crumbed",
                        "created": "2021-09-05T17:20:57.783525",
                        "receiverImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }],
                        "instigatorImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }]
                    },
                    {
                        "id": 2,
                        "instigatorId": 21,
                        "instigatorName": "John S Doe",
                        "receiverId": 9,
                        "receiverName": "Lina Jose Mari Patterson",
                        "marketplaceCardId": 10,
                        "marketplaceCardTitle": "Beef Mince Premium",
                        "created": "2021-09-05T17:09:58.893898",
                        "receiverImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }],
                        "instigatorImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }]
                    },
                    {
                        "id": 1,
                        "instigatorId": 21,
                        "instigatorName": "John S Doe",
                        "receiverId": 8,
                        "receiverName": "Frank J Smith",
                        "marketplaceCardId": 9,
                        "marketplaceCardTitle": "Beef Frying",
                        "created": "2021-09-05T17:09:38.111619",
                        "receiverImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }],
                        "instigatorImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }]
                    }
                ]
            }
            Cookies.get = jest.fn().mockImplementation(() => 21);
            Api.getConversations.mockImplementation(() => Promise.resolve(response));
            wrapper = await factory();
            await wrapper.vm.$nextTick();
        })

        test("Test open conversation", async () => {
            const response = {
                status: 200,
                data: [{
                    senderId: 21,
                    marketplaceCardId: 0,
                    content: "Hello"
                },{
                    senderId: 8,
                    marketplaceCardId: 0,
                    content: "Hey"
                }]
            }
            Api.getConversation.mockImplementation(() => Promise.resolve(response))

            await wrapper.vm.openConversation({id: 1})
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.conversationIsOpen).toBeTruthy()
            expect(wrapper.find("#message-title").exists()).toBeTruthy()
            expect(wrapper.find("#message-conversation").exists()).toBeTruthy()
            expect(wrapper.find("#send-message").exists()).toBeTruthy()
        })

        test("Test error response on open conversation", async () => {
            const response = {
                status: 400
            }
            Api.getConversation.mockImplementation(() => Promise.reject(response))
            await wrapper.vm.openConversation({id: 1})
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.conversationIsOpen).toBeFalsy()
            expect(wrapper.vm.$data.errorMessage).toBe("Something went wrong")
            expect(wrapper.find("#message-title").exists()).toBeFalsy()
            expect(wrapper.find("#message-conversation").exists()).toBeFalsy()
            expect(wrapper.find("#send-message").exists()).toBeFalsy()
        })
    })

    describe("Testing the sendMessage method", () => {
        let wrapper;

        beforeEach(async () => {
            const response = {
                response: {
                    status: 200,
                },
                data: [
                    {
                        "id": 1,
                        "instigatorId": 21,
                        "instigatorName": "John S Doe",
                        "receiverId": 8,
                        "receiverName": "Frank J Smith",
                        "marketplaceCardId": 9,
                        "marketplaceCardTitle": "Beef Frying",
                        "created": "2021-09-05T17:09:38.111619",
                        "receiverImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }],
                        "instigatorImages":  [{
                            "id": 1,
                            "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                            "isPrimary": true
                        }]
                    }
                ]
            }

            Cookies.get = jest.fn().mockImplementation(() => 21);
            Api.getConversations.mockImplementation(() => Promise.resolve(response));
            wrapper = await factory();
            await wrapper.vm.$nextTick();

            wrapper.vm.$data.conversationIsOpen = true;
            wrapper.vm.$data.conversationData.userId = 1
            wrapper.vm.$data.conversationData.marketplaceCardId = 2
            await wrapper.vm.$nextTick();
        })

        test("Testing a successful reply", async () => {
            const response = {
                status: 200
            }

            Api.sendReply.mockImplementation(() => Promise.resolve(response))

            const messageInput = "Hello"
            wrapper.vm.sendMessage(messageInput)
            await wrapper.vm.$nextTick()

            expect(wrapper.vm.$data.messages).toStrictEqual([{content: "Hello", marketplaceCardId: 2, receiverId: 1, senderId: 21}])
        })

        test("Testing a unsuccessful reply if not logged in", async () => {
            const response = {
                response: {
                    status: 401
                }
            }
            Api.sendReply.mockImplementation(() => Promise.reject(response))

            const messageInput = "Hello"
            wrapper.vm.sendMessage(messageInput)
            await wrapper.vm.$nextTick()

            expect($router.push).toHaveBeenCalledTimes(1)
            expect($router.push).toHaveBeenCalledWith({name: "InvalidToken"})
        })

        test("Testing a unsuccessful reply when bad data is sent", async () => {
            const response = {
                response: {
                    status: 400,
                    data: {
                        message: "Invalid data"
                    }
                }
            }

            Api.sendReply.mockImplementation(() => Promise.reject(response))
            await wrapper.vm.$nextTick()

            const messageInput = "Hello"
            wrapper.vm.sendMessage(messageInput)
            await wrapper.vm.$nextTick()

            expect(wrapper.vm.$data.errorMessage).toBe("400 - Invalid data")
        })

        test("Testing a timeout", async () => {
            const response = {
                request: {}
            }

            Api.sendReply.mockImplementation(() => Promise.reject(response))

            const messageInput = "Hello"
            wrapper.vm.sendMessage(messageInput)
            await wrapper.vm.$nextTick()

            expect(wrapper.vm.$data.errorMessage).toBe("Timeout")
        })
        test("Testing a unexpected error", async () => {
            const response = {}

            Api.sendReply.mockImplementation(() => Promise.reject(response))

            const messageInput = "Hello"
            wrapper.vm.sendMessage(messageInput)
            await wrapper.vm.$nextTick()

            expect(wrapper.vm.$data.errorMessage).toBe("Something went wrong")
        })
    })

    test("Testing the closeConversation method", async () => {
        const response = {
            response: {
                status: 200,
            },
            data: [
                {
                    "id": 1,
                    "instigatorId": 21,
                    "instigatorName": "John S Doe",
                    "receiverId": 8,
                    "receiverName": "Frank J Smith",
                    "marketplaceCardId": 9,
                    "marketplaceCardTitle": "Beef Frying",
                    "created": "2021-09-05T17:09:38.111619",
                    "receiverImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }],
                    "instigatorImages":  [{
                        "id": 1,
                        "filename": "23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "thumbnailFilename": "thumbnail23275f7e-b8af-4da3-a63f-ba850ab3d189.png",
                        "isPrimary": true
                    }]
                }
            ]
        }

        Cookies.get = jest.fn().mockImplementation(() => 21);
        Api.getConversations.mockImplementation(() => Promise.resolve(response));
        const wrapper = await factory();
        await wrapper.vm.$nextTick()
        wrapper.vm.$data.conversationIsOpen = true
        await wrapper.vm.$nextTick()

        expect(wrapper.find("#message-title").exists()).toBeTruthy()
        expect(wrapper.find("#message-conversation").exists()).toBeTruthy()
        expect(wrapper.find("#send-message").exists()).toBeTruthy()

        wrapper.vm.closeConversation()
        await wrapper.vm.$nextTick()

        expect(wrapper.vm.$data.conversationIsOpen).toBeFalsy()
        expect(wrapper.find("#message-title").exists()).toBeFalsy()
        expect(wrapper.find("#message-conversation").exists()).toBeFalsy()
        expect(wrapper.find("#send-message").exists()).toBeFalsy()
    })
})

describe("Testing MessageOption.vue", () => {

    const factory = async (username, cardName) => {
        const wrapper = await shallowMount(MessageOption, {
            localVue,
            propsData: {
                userName: username,
                images: [
                    {id: 1,
                    filename: ProfileImage,
                    thumbnailFilename: ProfileImage,
                    isPrimary: true}
                ],
                newMessage: true,
                cardName: cardName,
                conversationId: 1
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

describe("Testing SendMessage component", () => {

    let sendMessageWrapper;

    beforeEach( async () => {
        sendMessageWrapper = await shallowMount(SendMessage, {
            localVue
        });

        sendMessageWrapper.vm.$parent.sendMessage = jest.fn()
        await sendMessageWrapper.vm.$nextTick();
    })

    test("Test with no input parent method not called", async () => {
        sendMessageWrapper.vm.$data.messageInput = " "
        await sendMessageWrapper.vm.sendMessage({preventDefault: jest.fn()})

        await sendMessageWrapper.vm.$nextTick()

        expect(sendMessageWrapper.vm.$parent.sendMessage).toHaveBeenCalledTimes(0);
    })

    test("Test with input parent method is called", async () => {
        sendMessageWrapper.vm.$data.messageInput = "Hello"
        await sendMessageWrapper.vm.sendMessage({preventDefault: jest.fn()})

        await sendMessageWrapper.vm.$nextTick()

        expect(sendMessageWrapper.vm.$parent.sendMessage).toHaveBeenCalledTimes(1);
        expect(sendMessageWrapper.vm.$parent.sendMessage).toHaveBeenCalledWith("Hello");
    })
})

describe("Testing MessageTitle component", () => {

    let messageTitleWrapper;

    beforeEach( async () => {
        messageTitleWrapper = await shallowMount(MessageTitle, {
            localVue,
            propsData: {
                conversationData: {
                    id: 1,
                    images: [
                        {id: 1,
                            filename: ProfileImage,
                            thumbnailFilename: ProfileImage,
                            isPrimary: true}
                    ],
                    userName: "Joe"
                }
            }
        });

        await messageTitleWrapper.vm.$nextTick();


        messageTitleWrapper.vm.$parent.closeConversation = jest.fn()
        await messageTitleWrapper.vm.$nextTick();
    })

    test("Test that parent method is called when closing", async () => {
        await messageTitleWrapper.vm.closeConversation()

        await messageTitleWrapper.vm.$nextTick()

        expect(messageTitleWrapper.vm.$parent.closeConversation).toHaveBeenCalledTimes(1);
    })

    test("Test that the getThumbnailImageSrc method in messageHelper returns the default image when there are no images given", async () => {
        const serverURL = 'http://localhost:9499';
        Api.getServerURL.mockImplementation(() => serverURL);

        const images = [];
        expect(await messageTitleWrapper.vm.getThumbnailImageSrc(images)).toBe("test-file-stub");
    })

    test("Test that the getThumbnailImageSrc method in messageHelper returns the image src when there is one image", async () => {

        const serverURL = 'http://localhost:9499';
        Api.getServerURL.mockImplementation(() => serverURL);

        const images = [
                {
                    filename:"image777",
                    thumbnailFilename:"imageThumb888",
                    isPrimary: true
                }
            ];

        expect(await messageTitleWrapper.vm.getThumbnailImageSrc(images)).toBe(serverURL + "/imageThumb888");
    })

    test("Test that the getThumbnailImageSrc method in messageHelper returns the image src when there are multiple images (there is only ever one primary image)", async () => {

        const serverURL = 'http://localhost:9499';
        Api.getServerURL.mockImplementation(() => serverURL);

        const images = [
            {
                filename:"image111",
                thumbnailFilename:"imageThumb111",
                isPrimary: false
            },
            {
                filename:"image222",
                thumbnailFilename:"imageThumb222",
                isPrimary: false
            },
            {
                filename:"image333",
                thumbnailFilename:"imageThumb333",
                isPrimary: false
            },
            {
                filename:"image444",
                thumbnailFilename:"imageThumb444",
                isPrimary: true
            }
        ];

        expect(await messageTitleWrapper.vm.getThumbnailImageSrc(images)).toBe(serverURL + "/imageThumb444");
    })



})
