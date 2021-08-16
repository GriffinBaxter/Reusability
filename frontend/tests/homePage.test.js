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
        await wrapper.find('#bookmark-message-container-1').trigger("click");
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: `/businessProfile/1/listings/1`});
    });


})