/**
 * @jest-environment jsdom
 */

import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
import {expect, jest, describe, afterEach, test} from "@jest/globals";
import notification from "../../src/components/main/Notification";
import Api from "../../src/Api";

jest.mock("../../src/Api");

let wrapper;

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false});
localVue.use(VueRouter);

describe("Tests for notification's accordion", () => {

    afterEach(() => {
        wrapper.destroy();
    });

    async function mountPage() {
        wrapper = shallowMount(notification, {
            localVue,
        });
        await wrapper.vm.$nextTick();
    }

    test("Test accordion for Notifications will display all notifications, when data contain multiple notifications", async function () {
        // Given
        const notificationsData = {
            status: 200,
            data: [
                {
                    id: 1,
                    description: "Your card (Nescafe Cafe Menu Coffee Mix Caramel Latte 170G) has been deleted.",
                    created: "2021-07-21T22:05:55.449072",
                    marketplaceCardPayload: null
                }, {
                    id: 2,
                    description: "Your card (Red Bull Energy Drink Sugar Free) has been deleted.",
                    created: "2021-07-21T22:05:55.449072",
                    marketplaceCardPayload: null
                },
                {
                    id: 3,
                    description: "Your card (PS5) has been deleted.",
                    created: "2021-07-21T22:05:55.449072",
                    marketplaceCardPayload: null
                }]
        };

        // When
        Api.getNotifications.mockImplementation(() => Promise.resolve(notificationsData));
        await mountPage();

        // Then
        expect(wrapper.find("#notification_box1").exists()).toBeTruthy()
        expect(wrapper.find("#notification_box2").exists()).toBeTruthy()
        expect(wrapper.find("#notification_box3").exists()).toBeTruthy()
    });

    test("Test accordion for Notifications will display a notification, when data contain one notification", async function () {
        // Given
        const notificationsData = {
            status: 200,
            data: [
                {
                    id: 1,
                    description: "Your card (Nescafe Cafe Menu Coffee Mix Caramel Latte 170G) has been deleted.",
                    created: "2021-07-21T22:05:55.449072",
                    marketplaceCardPayload: null
                }]
        };

        // When
        Api.getNotifications.mockImplementation(() => Promise.resolve(notificationsData));
        await mountPage();

        // Then
        expect(wrapper.find("#notification_box1").exists()).toBeTruthy()
    });

    test("Test accordion for Notifications will display any thing, when data contain no notification", async function () {
        // Given
        const notificationsData = {
            status: 200,
            data: []
        };

        // When
        Api.getNotifications.mockImplementation(() => Promise.resolve(notificationsData));
        await mountPage();

        // Then
        expect(wrapper.find("#notification_box1").exists()).toBeFalsy()
    });

    test("Test the emptyMessage will been display, when data contain no notification", async function () {
        // Given
        const notificationsData = {
            status: 200,
            data: []
        };

        // When
        Api.getNotifications.mockImplementation(() => Promise.resolve(notificationsData));
        await mountPage();

        // Then
        expect(wrapper.find("#emptyMessage").exists()).toBeTruthy()
    });

    test("Test the emptyMessage will will not been display, when data contain some notifications", async function () {
        // Given
        const notificationsData = {
            status: 200,
            data: [
                {
                    id: 1,
                    description: "Your card (Nescafe Cafe Menu Coffee Mix Caramel Latte 170G) has been deleted.",
                    created: "2021-07-21T22:05:55.449072",
                    marketplaceCardPayload: null
                }, {
                    id: 2,
                    description: "Your card (Red Bull Energy Drink Sugar Free) has been deleted.",
                    created: "2021-07-21T22:05:55.449072",
                }]
        };

        // When
        Api.getNotifications.mockImplementation(() => Promise.resolve(notificationsData));
        await mountPage();

        // Then
        expect(wrapper.find("#emptyMessage").exists()).toBeFalsy()
    });
})

