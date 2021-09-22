/**
 * This file contains Jest tests for the SalesReport component.
 * @jest-environment jsdom
 */
import {shallowMount} from '@vue/test-utils';
import {beforeAll, describe, expect, jest, test} from "@jest/globals";
import SalesReport from "../src/components/saleInsights/SalesReport";
import Cookies from 'js-cookie';

jest.mock("../src/Api");
jest.mock("../src/currencyInstance");


describe('Testing the period selection of the sales report page', function () {

    let wrapper;
    let $router;
    let $route;

    beforeAll(() => {
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
                    businessCountry: "New Zealand",
                    businessId: 1,
                    currencyCode: "NZD",
                    currencySymbol: "$"
                }
            });
        Cookies.get = jest.fn().mockImplementation(() => 1);
    });

    test('Testing that the required options are given for selecting a year period.', () => {
        wrapper.vm.period = "Year";

        wrapper.vm.$nextTick().then(() => {
            // Year Required
            expect(wrapper.find('#sales-period-select-year').exists()).toBeTruthy();
            expect(wrapper.find('#sales-period-select-month').exists()).toBeFalsy();
            expect(wrapper.find('#sales-period-select-day').exists()).toBeFalsy();
        });
    });

    test('Testing that the required options are given for selecting a month period.', () => {
        wrapper.vm.period = "Month";

        wrapper.vm.$nextTick().then(() => {
            // Month + Year Required
            expect(wrapper.find('#sales-period-select-year').exists()).toBeTruthy();
            expect(wrapper.find('#sales-period-select-month').exists()).toBeTruthy();
            expect(wrapper.find('#sales-period-select-day').exists()).toBeFalsy();
        });
    });

    test('Testing that the required options are given for selecting a day period.', () => {
        wrapper.vm.period = "Day";

        wrapper.vm.$nextTick().then(() => {
            // Day Required (as it uses its own date input)
            expect(wrapper.find('#sales-period-select-year').exists()).toBeFalsy();
            expect(wrapper.find('#sales-period-select-month').exists()).toBeFalsy();
            expect(wrapper.find('#sales-period-select-day').exists()).toBeTruthy();
        });
    });

    test('Testing that a detected current date sets the current/selected year, month and day fields ' +
        'correctly.', () => {
        const year = 2021;
        const month = 6;
        const day = 11;
        const dateString = year + "-" + "0" + month + "-" + day;

        wrapper.vm.setDates(new Date(dateString))

        wrapper.vm.$nextTick().then(() => {
            expect(wrapper.vm.currentYear).toEqual(year);
            expect(wrapper.vm.selectedYear).toEqual(year);
            expect(wrapper.vm.currentMonth).toEqual(month - 1); // -1 as months start from index 0
            expect(wrapper.vm.selectedMonth).toEqual(wrapper.vm.months[month - 1]); // -1 as months start from index 0
            expect(wrapper.vm.currentDay).toEqual(dateString);
            expect(wrapper.vm.selectedDay).toEqual(dateString);
        });
    });

    test('Testing that the valid years are increased after years have passed.', () => {
        const year = 2028;
        const month = 4;
        const day = 20;
        const dateString = year + "-" + "0" + month + "-" + day;

        wrapper.vm.setDates(new Date(dateString))

        wrapper.vm.$nextTick().then(() => {
            expect(wrapper.vm.validYears).toEqual([2021, 2022, 2023, 2024, 2025, 2026, 2027, year]);
        });
    });

    test('Testing that a year in the future cannot be selected.', () => {
        const year = 2021;
        const month = 4;
        const day = 20;
        const dateString = year + "-" + "0" + month + "-" + day;

        wrapper.vm.setDates(new Date(dateString))

        wrapper.vm.$nextTick().then(() => {
            expect(wrapper.vm.validYears).toEqual([year]);
        });
    });

    test('Testing that a month (of the current year) in the future cannot be selected.', () => {
        const year = 2021;
        const month = 6;
        const day = 9;
        const dateString = year + "-" + "0" + month + "-" + "0" + day;

        wrapper.vm.setDates(new Date(dateString))

        wrapper.vm.$nextTick().then(() => {
            expect(wrapper.vm.selectedMonth).toEqual(wrapper.vm.months[month - 1]); // -1 as months start from index 0
        });
    });
});
