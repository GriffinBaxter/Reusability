import {createLocalVue, mount} from '@vue/test-utils'
import BusinessProfile from "../src/views/BusinessProfile";
import {beforeEach, describe, expect, jest, test} from "@jest/globals";

import Cookies from "js-cookie"
import Api from "../src/Api";

import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false});
localVue.use(VueRouter);

let wrapper;

jest.mock("../src/Api");
jest.mock("js-cookie");

const setupCookies = (userId, actAsId) => {

    Cookies.get.mockReturnValueOnce(userId).mockReturnValueOnce(actAsId).mockReturnValueOnce(actAsId)
        .mockReturnValueOnce(actAsId).mockReturnValueOnce(userId).mockReturnValueOnce(actAsId);
}

describe("Testing that the change profile button appears only when allowed to.", () => {
    let $route;
    let $router;

    beforeEach(() => {
        $route = {
            path: '/profile',
            name: 'Profile',
        }

        $router = {
            push: jest.fn()
        }
    })

    test("Testing that if you are not acting as a business then the button does not appear.", async () => {
        setupCookies(1, undefined);

        wrapper = mount(BusinessProfile, {
            localVue,
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });
        await wrapper.vm.$nextTick();
        const button = await wrapper.find("#change-profile-picture-button");

        expect(button.exists()).toBeFalsy();
    });



});
