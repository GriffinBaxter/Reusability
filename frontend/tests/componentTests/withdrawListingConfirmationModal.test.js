/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import withdrawListingConfirmationModal from "../../src/components/listing/WithdrawListingConfirmationModal";
import {describe, expect, test} from "@jest/globals";

const wrapper = shallowMount(withdrawListingConfirmationModal, {
    propsData: {
        businessName: "My Business",
        productName: "Epic Product",
        quantity: "69",
        price: "420.69",
        currencySymbol: "$",
        currencyCode: "NZD",
    },
})

describe("Testing the withdraw listing modal's functionality", () => {

    test('Testing the buttons exist for withdrawing listings and cancelling the operation.', () => {
        expect(wrapper.find('#withdraw-listing').exists()).toBeTruthy();
        expect(wrapper.find('#cancel-button').exists()).toBeTruthy();
    })

    test('Testing that nothing is emitted upon no press of the withdraw listing button.', async () => {
        expect(wrapper.emitted().deleteListing).toBeFalsy();
    })

    test('Testing that deleteListing is emitted upon pressing the withdraw listing button.', async () => {
        const withdrawButton = wrapper.find('#withdraw-listing');
        await withdrawButton.trigger("click");
        await wrapper.vm.$nextTick();
        expect(wrapper.emitted().deleteListing).toBeTruthy();
    })
})
