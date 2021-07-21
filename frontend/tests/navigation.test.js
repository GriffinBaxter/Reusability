/**
 * Jest tests for Navbar.vue & SideNavVar.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe} from "@jest/globals"
import {mount} from "@vue/test-utils";
import Navbar from "../src/components/main/Navbar";
import SideNavBar from "../src/components/main/SideNavBar";
import Home from "../src/views/Home"

import { RouterLinkStub } from '@vue/test-utils';

let navbarWrapper;
let sideNavBarWrapper;
let homeWrapper;
let sideNavToggleButton;
let sideNavBar;
let homePage;
let sideNavCloseButton;

beforeAll(() => {
    const $router = {
        push: jest.fn()
    }
    const $route = {
        path: '/home',
    }

    navbarWrapper = mount(
        Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: {RouterLink: RouterLinkStub}
        });
    sideNavBarWrapper = mount(
        SideNavBar,
        {
            mocks: {
                $router,
                $route
            },
            stubs: {RouterLink: RouterLinkStub}
        });
    homeWrapper = mount(
        Home,
        {
            mocks: {
                $router,
                $route
            }
        });

    sideNavToggleButton = navbarWrapper.find("#side-nav-bar-open-btn");
    sideNavBar = sideNavBarWrapper.find("#side-nav-bar");
    homePage = homeWrapper.find("main")
    sideNavCloseButton = sideNavBarWrapper.find("#closebtn");

})

describe("Testing the side navigation buttons' functionality", () => {

    test('Testing the home page link takes the user from the current page (not the home page) to the home page ' +
        'when the user clicks the home link', () => {

    })

    test('Testing that the user remains on the home page when they are on the home page and click the home link',
        () => {

    })

    test('Testing the profile page link takes the user from the current page (not the user profile page) to the ' +
        'user profile page when the user clicks the profile link and they are acting as the user', () => {

    })

    test('Testing that the user remains on the user profile page when they are on the profile page and click the' +
        'profile link in the side navigation bar and they are acting as the user', () => {

    })

    test('Testing the profile page link takes the user from the current page (not the business profile page) to' +
        ' the user profile page when the user clicks the profile link in the side navigation bar and they are acting as ' +
        'the business', () => {

    })

    test('Testing that the user remains on the business profile page when they are on the business profile page' +
        ' and click the profile link in the side navigation bar and they are acting as the business', () => {

    })

    test('Testing that the user remains on the business profile page when they are on the business profile page' +
        ' and click the profile link in the side navigation bar and they are acting as the business', () => {

    })

    test('Testing the marketplace page link takes the user from the current page (not the marketplace page) to' +
        ' the marketplace page when the user clicks the marketplace link in the side navigation bar', () => {

    })

    test('Testing that the user remains on the marketplace page when they are on the marketplace page and click' +
        ' the marketplace link in the side navigation bar', () => {

    })

    test('Testing that the user is logged out (taken to the login page) when the click the logout link in the' +
        ' side navigation bar and are currently logged in', () => {

    })
})

describe("Testing the side navigation bar opens and closes", () => {


    test('Testing the button on the top navigation bar opens the side navigation bar when the side navigation ' +
        'bar is closed', () => {

        // Mock clicking the open button
        sideNavToggleButton.trigger("click");
        navbarWrapper.vm.$nextTick();

        // sideNavToggleButton.toggleSideNav();
        sideNavBarWrapper.vm.toggleSideNav();

        expect(sideNavBar.style.width).toBe("250px");
        expect(homePage.style.marginLeft).toBe("250px");

    })

    test('Testing the button on the top navigation bar closes the side navigation bar when the side navigation ' +
        'bar is open', () => {

        // Mock clicking the open button
        sideNavToggleButton.trigger("click");
        navbarWrapper.vm.$nextTick();

        // sideNavToggleButton.toggleSideNav();
        sideNavBarWrapper.vm.toggleSideNav();

        expect(sideNavBar.style.width).toBe("0px");
        expect(homePage.style.marginLeft).toBe("0px");

    })

    test('Testing the x button on the side navigation bar closes the side navigation bar when the side navigation ' +
        'bar is open', () => {

        // Mock clicking the open button
        sideNavCloseButton.trigger("click");
        navbarWrapper.vm.$nextTick();

        // sideNavToggleButton.toggleSideNav();
        sideNavBarWrapper.vm.closeSideNav();

        expect(sideNavBar.style.width).toBe("0px");
        expect(homePage.style.marginLeft).toBe("0px");

    })

})

describe("closeSideNav() tests", () => {

    test('Testing closeSideNav() closes the side nav bar in terms of SideNavBar when it is open', () => {

        sideNavBarWrapper.closeSideNav();
        expect(sideNavBar.style.width).toBe("0px");
        expect(homePage.style.marginLeft).toBe("0px");
    })

    test('Testing closeSideNav() closes the side nav bar in terms of Navbar when it is open', () => {

        navbarWrapper.closeSideNav();
        expect(sideNavBar.style.width).toBe("0px");
        expect(homePage.style.marginLeft).toBe("0px");
    })

})

describe("toggleSideNav() tests", () => {

    test('Testing toggleSideNav() closes the side nav bar in terms of SideNavBar when it is open', () => {

        sideNavBar.style.width = "250px";
        homePage.style.marginLeft = "250px";

        sideNavBarWrapper.toggleSideNav();

        expect(sideNavBar.style.width).toBe("0px");
        expect(homePage.style.marginLeft).toBe("0px");
    })

    test('Testing toggleSideNav() closes the side nav bar in terms of Navbar when it is open', () => {

        sideNavBar.style.width = "250px";
        homePage.style.marginLeft = "250px";

        navbarWrapper.toggleSideNav();

        expect(sideNavBar.style.width).toBe("0px");
        expect(homePage.style.marginLeft).toBe("0px");
    })

    test('Testing toggleSideNav() opens the side nav bar in terms of SideNavBar when it is closed', () => {

        sideNavBar.style.width = "0px";
        homePage.style.marginLeft = "0px";

        sideNavBarWrapper.toggleSideNav();

        expect(sideNavBar.style.width).toBe("250px");
        expect(homePage.style.marginLeft).toBe("250px");
    })

    test('Testing toggleSideNav() opens the side nav bar in terms of Navbar when it is closed', () => {

        sideNavBar.style.width = "0px";
        homePage.style.marginLeft = "0px";

        navbarWrapper.toggleSideNav();

        expect(sideNavBar.style.width).toBe("250px");
        expect(homePage.style.marginLeft).toBe("250px");
    })

})

describe("toggleInteractAs() tests in terms of Navbar", () => {

    test('Testing toggleInteractAs() sets the showInteractMenu to false when the showInteractMenu dropdown is true.', () => {
        navbarWrapper.vm.showInteractMenu = true;
        navbarWrapper.vm.toggleInteractAs()
        expect(navbarWrapper.vm.showInteractMenu).toBe(false)
    })

    test('Testing toggleInteractAs() sets the showInteractMenu to true when the showInteractMenu dropdown is false.', () => {
        navbarWrapper.vm.showInteractMenu = false;
        navbarWrapper.vm.toggleInteractAs()
        expect(navbarWrapper.vm.showInteractMenu).toBe(true)
    })

})

describe("toggleBusinessDropdown() tests in terms of Navbar", () => {

    test('Testing toggleBusinessDropdown() sets the showBusinessDropdown to false when the showBusiness is true.', () => {
        navbarWrapper.vm.showBusinessDropdown = true;
        navbarWrapper.vm.toggleBusinessDropdown()
        expect(navbarWrapper.vm.showBusinessDropdown).toBe(false)
    })

    test('Testing toggleBusinessDropdown() sets the showBusinessDropdown to true when the showBusiness is false.', () => {
        navbarWrapper.vm.showBusinessDropdown = false;
        navbarWrapper.vm.toggleBusinessDropdown()
        expect(navbarWrapper.vm.showBusinessDropdown).toBe(true)
    })

})

describe("toggleInteractAs() tests in terms of SideNavBar", () => {

    test('Testing toggleInteractAs() sets the showInteractMenu to false when the showInteractMenu dropdown is true.', () => {
        navbarWrapper.vm.showInteractMenu = true;
        navbarWrapper.vm.toggleInteractAs()
        expect(navbarWrapper.vm.showInteractMenu).toBe(false)
    })

    test('Testing toggleInteractAs() sets the showInteractMenu to true when the showInteractMenu dropdown is false.', () => {
        navbarWrapper.vm.showInteractMenu = false;
        navbarWrapper.vm.toggleInteractAs()
        expect(navbarWrapper.vm.showInteractMenu).toBe(true)
    })

})

describe("toggleBusinessDropdown() tests in terms of SideNavBar", () => {

    test('Testing toggleBusinessDropdown() sets the showBusinessDropdown to false when the showBusiness is true.', () => {
        navbarWrapper.vm.showBusinessDropdown = true;
        navbarWrapper.vm.toggleBusinessDropdown()
        expect(navbarWrapper.vm.showBusinessDropdown).toBe(false)
    })

    test('Testing toggleBusinessDropdown() sets the showBusinessDropdown to true when the showBusiness is false.', () => {
        navbarWrapper.vm.showBusinessDropdown = false;
        navbarWrapper.vm.toggleBusinessDropdown()
        expect(navbarWrapper.vm.showBusinessDropdown).toBe(true)
    })

})