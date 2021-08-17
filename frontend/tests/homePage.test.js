import {test, expect, describe, jest} from "@jest/globals";
import Home from '../src/views/Home'
import Api from "../src/Api";
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";

const localVue = createLocalVue();
jest.mock("../src/Api");
localVue.use(VueLogger, {isEnabled: false})
let wrapper;

const $router = {
    push: jest.fn()
};

describe("Tests for bookmark message display", () => {
    test("Test (No Bookmarked Messages) display when no bookmark message.", async () => {
        const response = {
            data: []
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeFalsy();
        expect(wrapper.find('#no-bookmark-message').exists()).toBeTruthy();
    });

    test("Test (No Bookmarked Messages) will not display when there is bookmark message.", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeTruthy();
        expect(wrapper.find('#no-bookmark-message').exists()).toBeFalsy();
    });

    test("Test the bookmark message will display when there is only one message", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeTruthy();
    });

    test("Test the bookmark messages will display when there is more than one message", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                },
                {
                    id: 2,
                    description: "Bookmark for product listing 'Eta Ripple Cut Potato Chips Chicken' has been removed.",
                    created: "2021-08-17T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                },
                {
                    id: 3,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-18T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeTruthy();
        expect(wrapper.find('#bookmark-message-container-2').exists()).toBeTruthy();
        expect(wrapper.find('#bookmark-message-container-3').exists()).toBeTruthy();
    });
})

describe("Tests for toListing function", () => {
    test("Test the page is pushed to business profile listing page, when the message is clicked", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();
        await wrapper.find('#bookmark-message-link-1').trigger("click");
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: `/businessProfile/1/listings/1`});
    });
})

describe("Tests for bookmark message deletion", () => {
    test("Test the bookmark message will been delete when user click on the delete button", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));
        Api.deleteBookmarkMessage.mockResolvedValue();

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();
        expect(wrapper.find('#delete-bookmark-message-button-1').exists()).toBeTruthy();

        await wrapper.find('#delete-bookmark-message-button-1').trigger("click");
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#delete-bookmark-message-button-1').exists()).toBeFalsy();
    });
})
