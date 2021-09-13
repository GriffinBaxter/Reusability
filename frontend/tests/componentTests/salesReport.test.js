/**
 * This file contains Jest tests for the SalesReport component.
 * @jest-environment jsdom
 */
import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import Cookies from "js-cookie";
import SalesReport from "../../src/components/saleInsights/SalesReport";

describe('Tests methods in the SaleReport component.', () => {

    let wrapper;
    let $router;
    let $route;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };
        $route = {
            params: {
                businessId: 2
            }
        };
        wrapper = shallowMount(
            SalesReport,
            {
                mocks: {
                    $router,
                    $route
                },
                propsData: {
                    businessName: "Lumbridge General Store",
                }
            });
        Cookies.get = jest.fn().mockImplementation(() => 1);
    });

    describe("Test the applyDate method", () => {

        test("Test the applyDate method sets invalidDateMsg when the start date is after the end date", () => {
            wrapper.vm.$data.startDate = '2021-03-20';
            wrapper.vm.$data.endDate = '2020-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Start date must be before end date");
        })

        test("Test the applyDate method sets invalidDateMsg when the start date is in the future", () => {
            wrapper.vm.$data.startDate = '2031-03-20';
            wrapper.vm.$data.endDate = '2032-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Start date cannot be in the future");
        })

        test("Test the applyDate method sets invalidDateMsg to an empty string when the dates are valid", () => {
            wrapper.vm.$data.startDate = '2019-03-20';
            wrapper.vm.$data.endDate = '2020-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("");
        })

        test("Test the applyDate method sets invalidDateMsg to an empty string when the dates are one day apart", () => {
            wrapper.vm.$data.startDate = '2020-03-19';
            wrapper.vm.$data.endDate = '2020-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("");
        })

        test("Test the applyDate method sets invalidDateMsg when the start date is the same as the end date", () => {
            wrapper.vm.$data.startDate = '2020-03-20';
            wrapper.vm.$data.endDate = '2020-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Start date must be before end date");
        })

    });

    describe("Test the setGranularityOption method", () => {

        test("Test the setGranularityOption method granularityText sets granularityText to the sentence case of " +
            "the given granularity.", () => {
            wrapper.vm.$data.granularityText = "";
            const granularity = "Total";
            wrapper.vm.setGranularityOption(granularity)
            expect(wrapper.vm.$data.granularityText).toBe("Total");
        })

    });

})
