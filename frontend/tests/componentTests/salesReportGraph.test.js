/**
 * This file contains Jest tests for the SalesReportGraph component.
 * @jest-environment jsdom
 */
import {describe, expect, jest, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import SalesReportGraph from "../../src/components/saleInsights/SalesReportGraph";

jest.mock("../../src/Api");
jest.mock("../../src/currencyInstance");

describe('Tests the SalesReportGraph component.', () => {

    let wrapper;
    let props;

    test("Tests that the correct data is populated for a sales graph.", () => {
        props = {
            mock: true,  // Custom mock passed through as a prop if active
            labelList: [
                'January 2021', 'February 2021', 'March 2021', 'April 2021', 'May 2021',
                'June 2021', 'July 2021', 'August 2021', 'September 2021'
            ],
            dataList: [0, 0, 0, 2, 0, 1, 0, 0, 1],
            sales: true,
        }

        wrapper = shallowMount(SalesReportGraph, {
            propsData: props
        });

        expect(wrapper.vm.$props.labelList).toBe(props.labelList);
        expect(wrapper.vm.$props.dataList).toBe(props.dataList);
    });

    test("Tests that the correct data is populated for a revenue graph.", () => {
        props = {
            mock: true,  // Custom mock passed through as a prop if active
            labelList: [
                'January 2021', 'February 2021', 'March 2021', 'April 2021', 'May 2021',
                'June 2021', 'July 2021', 'August 2021', 'September 2021'
            ],
            dataList: [0, 0, 0, 42, 0, 35, 0, 0, 111],
            sales: false,
            currencySymbol: "$"
        }

        wrapper = shallowMount(SalesReportGraph, {
            propsData: props
        });

        expect(wrapper.vm.$props.labelList).toBe(props.labelList);
        expect(wrapper.vm.$props.dataList).toBe(props.dataList);
        expect(wrapper.vm.$props.currencySymbol).toBe(props.currencySymbol);
    });

    test("Tests that the sales label is returned correctly from the label computed property.", () => {
        const salesProp = {
            sales: true
        }

        const label = SalesReportGraph.computed.label.call(salesProp);

        expect(label).toBe("Sales");
    });

    test("Tests that the revenue label is returned correctly from the label computed property.", () => {
        const salesProp = {
            sales: false
        }

        const label = SalesReportGraph.computed.label.call(salesProp);

        expect(label).toBe("Revenue");
    });

    test("Tests that the currencyRevenue method returns the correct symbol for a given value.", () => {
        props = {
            mock: true,  // Custom mock passed through as a prop if active
            labelList: [
                'January 2021', 'February 2021', 'March 2021', 'April 2021', 'May 2021',
                'June 2021', 'July 2021', 'August 2021', 'September 2021'
            ],
            dataList: [0, 0, 0, 42, 0, 35, 0, 0, 111],
            sales: false,
            currencySymbol: "$"
        }

        wrapper = shallowMount(SalesReportGraph, {
            propsData: props
        });

        const currencyRevenue = wrapper.vm.currencyRevenue(props.dataList[5]);

        expect(currencyRevenue).toBe(props.currencySymbol + props.dataList[5]);
    });
})
