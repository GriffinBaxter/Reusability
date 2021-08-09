/**
 * Jest tests for SaleListing.vue.
 * @jest-environment jsdom
 */

import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import SaleListing from "../src/views/SaleListing";
import {shallowMount} from "@vue/test-utils";

describe("Testing the 'Go to Business Profile' button", () => {

    let saleListingWrapper;
    let $router;
    let $route;
    let goToButton;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };
        $route = {
            params: {
                businessId: 2,
                listingId: 11
            }
        };
        saleListingWrapper = shallowMount(SaleListing, {
            mocks: {
                $router,
                $route
            }
        });
        goToButton = saleListingWrapper.find("#go-to-button");
    });

    test("Test that user is redirected to the business profile page when the 'Go to Business Profile' button is clicked.", async () => {
        const businessId = 2;

        await goToButton.trigger("click");

        expect($router.push).toHaveBeenCalledWith({ path: `/businessProfile/${businessId}`});
    });
});