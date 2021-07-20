/**
 * Jest tests for Navbar.vue & SideNavVar.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import Navbar from "../src/components/main/Navbar";
import SideNavBar from "../src/components/main/SideNavBar";
import Home from "../src/views/Home"

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

    const navbarWrapper = shallowMount(
        Navbar);
    const sideNavBarWrapper = shallowMount(
        SideNavBar);
    const homeWrapper = shallowMount(Home);

    //TODO: add in mocking of isActivePath(path)

    test('Testing the button on the top navigation bar opens the side navigation bar when the side navigation ' +
        'bar is closed', () => {

        const sideNavToggleButton = navbarWrapper.find("#side-nav-bar-open-btn");
        const sideNavBar = sideNavBarWrapper.find("#side-nav-bar");
        const homePage = homeWrapper.find("main")

        // Mock clicking the open button
        sideNavToggleButton.trigger("click");
        navbarWrapper.vm.$nextTick();

        sideNavToggleButton.toggleSideNav();

        expect(sideNavBar.style.width).toBe("250px");
        expect(homePage.style.marginLeft).toBe("250px");

    })

    test('Testing the button on the top navigation bar closes the side  navigation bar when the side navigation ' +
        'bar is open', () => {

    })

    test('Testing the x button on the side navigation bar closes the side navigation bar when the side navigation ' +
        'bar is open', () => {

    })


})