/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import CurrencyChangeModal from "../../src/components/business/CurrencyChangeModal";
import CurrencyAPI from "../../src/currencyInstance";
import {beforeEach, describe, expect, jest, test} from "@jest/globals";

jest.mock("../../src/currencyInstance");

describe("Testing the currency methods in CurrencyChangeModal", () => {

    let wrapper;
    let $router;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };

        wrapper = shallowMount(
            CurrencyChangeModal,
            {
                mocks: {
                    $router
                },
                propsData: {
                    businessName: "New World"
                }
            });
    });

    test('Testing the updateCurrency method emits a custom event when currency code and symbol are ' +
        'successfully received from REST Countries.', async () => {
        const response = {
            data: [
                {
                    currencies: {
                        "NZD": {
                            symbol: "$"
                        }
                    }
                }
            ]
        }

        wrapper.vm.$parent.$refs = {
            country: {
                value: "New Zealand"
            }
        };

        CurrencyAPI.currencyQuery.mockImplementation(() => Promise.resolve(response));

        await wrapper.vm.updateCurrency();
        await wrapper.vm.$nextTick();

        expect(wrapper.emitted().currencyChange).toBeTruthy();
        expect(wrapper.emitted().currencyChange[0]).toEqual(["NZD", "$"]);
    })

    test('Testing the updateCurrency method does not emit a custom event when ' +
        'unsuccessfully retrieving data from REST Countries.', async () => {
        const response = {};

        wrapper.vm.$parent.$refs = {
            country: {
                value: "New Zealand"
            }
        };

        CurrencyAPI.currencyQuery.mockImplementation(() => Promise.reject(response));

        await wrapper.vm.updateCurrency();
        await wrapper.vm.$nextTick();

        expect(wrapper.emitted().currencyChange).toBeFalsy();
    })

    test('Testing the keepCurrency method emits a custom event with currency code and symbol being null', async () => {
        await wrapper.vm.keepCurrency();
        await wrapper.vm.$nextTick();

        expect(wrapper.emitted().currencyChange).toBeTruthy();
        expect(wrapper.emitted().currencyChange[0]).toEqual([null, null]);
    })

})