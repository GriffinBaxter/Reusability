/**
 * Jest tests for Navbar.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe, jest, beforeAll} from "@jest/globals"
import {UserRole} from "../src/configs/User";
import {shallowMount, RouterLinkStub } from "@vue/test-utils";
import Navbar from "../src/components/main/Navbar";

jest.mock("../src/Api");

describe("Testing the navbar methods", () => {

    let navbarWrapper;
    let $route;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/home',
        }

        navbarWrapper = shallowMount(
            Navbar,
            {
                mocks: {
                    $router,
                    $route
                },
                // stubs: {
                //     RouterLink: RouterLinkStub
                // }
            });

        Promise.resolve();
    });

    test("Testing that the DGAA label appears when the logged in user is the DGAA", () => {

        navbarWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;

        expect(navbarWrapper.find("#admin-label").exists()).toBe(true)
        expect(navbarWrapper.find("#admin-label").text()).toBe("Admin (DGAA)")
        expect(navbarWrapper.find("#admin-label").backgroundColor()).toBe("#fd5050")
        expect(navbarWrapper.find("#admin-label").color()).toBe("white")

    })

    test("Testing that the GAA label appears when the logged in user is a GAA", () => {

        navbarWrapper.vm.role = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;

        expect(navbarWrapper.find("#admin-label").exists()).toBe(true)
        expect(navbarWrapper.find("#admin-label").text()).toBe("Admin (GAA)")
        expect(navbarWrapper.find("#admin-label").backgroundColor()).toBe("#fd5050")
        expect(navbarWrapper.find("#admin-label").color()).toBe("white")

    })

    test("Testing that no admin label appears when the logged in user is not the DGAA nor a GAA", () => {

        navbarWrapper.vm.role = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;

        expect(navbarWrapper.find("#admin-label").exists()).toBe(false)

    })
})