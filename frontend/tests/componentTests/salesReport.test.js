/**
 * This file contains Jest tests for the SalesReport component.
 * @jest-environment jsdom
 */
import {beforeAll, beforeEach, describe, expect, jest, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import SalesReport from "../../src/components/saleInsights/SalesReport";
import Api from "../../src/Api";
import Cookies from "js-cookie";

jest.mock("../../src/Api");
jest.mock("../../src/currencyInstance");

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
                    businessCountry: "New Zealand",
                    businessId: 1,
                    currencyCode: "NZD",
                    currencySymbol: "$"
                }
            });

        const response = {
            status: 200,
            data: []
        }

        Api.getSalesReport.mockImplementation(() => Promise.resolve(response));
    });

    describe("Test the applyDate method", () => {

        test("Test the applyDate method sets invalidDateMsg when the start date is after the end date", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2021-03-20';
            wrapper.vm.$data.endDate = '2021-01-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Start date must be before end date");
        })

        test("Test the applyDate method sets invalidDateMsg when the start date is in the future", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2031-03-20';
            wrapper.vm.$data.endDate = '2032-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Start date cannot be in the future");
        })

        test("Test the applyDate method sets invalidDateMsg to an empty string when the dates are valid", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2021-04-20';
            wrapper.vm.$data.endDate = '2021-05-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("");
        })

        test("Test the applyDate method sets invalidDateMsg to an empty string when the dates are one day apart", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2021-03-19';
            wrapper.vm.$data.endDate = '2021-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("");
        })

        test("Test the applyDate method sets invalidDateMsg when the start date is the same as the end date", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2021-03-20';
            wrapper.vm.$data.endDate = '2021-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Start date must be before end date");
        })

        test("Test the applyDate method sets invalidDateMsg when the start date is before 2021", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2020-12-31';
            wrapper.vm.$data.endDate = '2021-03-20';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Dates must be after 2020");
        })

        test("Test the applyDate method sets invalidDateMsg when the end date is before 2021", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2021-12-31';
            wrapper.vm.$data.endDate = '2020-12-31';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Dates must be after 2020");
        })

        test("Test the applyDate method sets invalidDateMsg when the both the start and end dates are before 2021", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2020-12-31';
            wrapper.vm.$data.endDate = '2020-12-31';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Dates must be after 2020");
        })

        test("Test the applyDate method sets invalidDateMsg when the start date has not been entered", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.endDate = '2021-12-31';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Please enter two dates");
        })

        test("Test the applyDate method sets invalidDateMsg when the end date has not been entered", () => {
            wrapper.vm.period = 'Custom';
            wrapper.vm.$data.startDate = '2021-12-31';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Please enter two dates");
        })

        test("Test the applyDate method sets invalidDateMsg when both the start and end dates have not been entered", () => {
            wrapper.vm.period = 'Custom';
            let event = {
                preventDefault: jest.fn()
            }
            wrapper.vm.applyDate(event)
            expect(wrapper.vm.$data.invalidDateMsg).toBe("Please enter two dates");
        })

    });

    describe("Test the setGranularityOption method", () => {

        test("Test the setGranularityOption method sets granularityText to the sentence case of " +
            "the given granularity.", () => {
            wrapper.vm.$data.granularity = "";
            let event = {
                preventDefault: jest.fn()
            }
            const granularity = "Total";
            wrapper.vm.setGranularityOption(granularity, event)
            expect(wrapper.vm.$data.granularity).toBe("Total");
        })

    });

    describe("Test the generateDatesFromYear method", () => {

        test("Test the generateDatesFromYear method returns a valid fromDate and toDate for a given year ", () => {

            const dates = wrapper.vm.generateDatesFromYear('2021');

            expect(dates.fromDate).toEqual("2021-01-01T00:00")
            expect(dates.toDate).toEqual("2021-12-31T23:59:59")

        })

    });

    describe("Test the generateDatesFromMonth method", () => {

        test("Test the generateDatesFromMonth method returns a valid fromDate and toDate for a February (fewer than 30 days) ", () => {

            const dates = wrapper.vm.generateDatesFromMonth('February');

            expect(dates.fromDate).toEqual("2021-02-01T00:00")
            expect(dates.toDate).toEqual("2021-02-28T23:59:59")

        })

        test("Test the generateDatesFromMonth method returns a valid fromDate and toDate for a month with a 30 days and a number less than 10 (a leading zero needs to be added to the month) ", () => {

            const dates = wrapper.vm.generateDatesFromMonth('April');

            expect(dates.fromDate).toEqual("2021-04-01T00:00")
            expect(dates.toDate).toEqual("2021-04-30T23:59:59")

        })

        test("Test the generateDatesFromMonth method returns a valid fromDate and toDate for a month with 31 days ", () => {

            const dates = wrapper.vm.generateDatesFromMonth('May');

            expect(dates.fromDate).toEqual("2021-05-01T00:00")
            expect(dates.toDate).toEqual("2021-05-31T23:59:59")

        })

        test("Test the generateDatesFromMonth method returns a valid fromDate and toDate for a month with a number greater than 10 (no leading zeros need to be added to the month) ", () => {

            const dates = wrapper.vm.generateDatesFromMonth('November');

            expect(dates.fromDate).toEqual("2021-11-01T00:00")
            expect(dates.toDate).toEqual("2021-11-30T23:59:59")

        })

    });


    describe("Test the generateDatesFromDay method", () => {

        test("Test the generateDatesFromDay method returns a valid fromDate and toDate for a given day ", () => {

            const dates = wrapper.vm.generateDatesFromDay('2021-02-18');

            expect(dates.fromDate).toEqual("2021-02-18T00:00")
            expect(dates.toDate).toEqual("2021-02-18T23:59:59")

        })

    });

    describe("Test the generateDates method", () => {

        test("Test the generateDates method returns a valid fromDate and toDate when the selected period is 'Custom'", async () => {

            const customDropdownOption = wrapper.find("#custom-option");
            customDropdownOption.trigger('click');
            await wrapper.vm.$nextTick();
            wrapper.vm.$data.startDate = '2021-03-12';
            wrapper.vm.$data.endDate = '2021-09-15';

            const dates = wrapper.vm.generateDates();

            expect(dates.fromDate).toEqual("2021-03-12T00:00")
            expect(dates.toDate).toEqual("2021-09-15T23:59:59")

        })

        test("Test the generateDates method returns a valid fromDate and toDate when the selected period is 'Year'", async () => {

            const yearDropdownOption = wrapper.find("#year-option");
            yearDropdownOption.trigger('click');
            await wrapper.vm.$nextTick();
            wrapper.vm.$data.selectedYear = '2021'

            const dates = wrapper.vm.generateDates();

            expect(dates.fromDate).toEqual("2021-01-01T00:00")
            expect(dates.toDate).toEqual("2021-12-31T23:59:59")

        })

        test("Test the generateDates method returns a valid fromDate and toDate when the selected period is 'Month'", async () => {

            const yearDropdownOption = wrapper.find("#month-option");
            yearDropdownOption.trigger('click');
            await wrapper.vm.$nextTick();
            wrapper.vm.$data.selectedMonth = 'May'

            const dates = wrapper.vm.generateDates();

            expect(dates.fromDate).toEqual("2021-05-01T00:00")
            expect(dates.toDate).toEqual("2021-05-31T23:59:59")

        })

        test("Test the generateDates method returns a valid fromDate and toDate when the selected period is 'Day'", async () => {

            const yearDropdownOption = wrapper.find("#day-option");
            yearDropdownOption.trigger('click');
            await wrapper.vm.$nextTick();
            wrapper.vm.$data.selectedDay = '2021-04-27'

            const dates = wrapper.vm.generateDates();

            expect(dates.fromDate).toEqual("2021-04-27T00:00")
            expect(dates.toDate).toEqual("2021-04-27T23:59:59")

        })

    });

    describe("Test the retrieveSalesReport method", () => {

        test("Test the retrieveSalesReport method correctly sets data when a 200 response with data is returned.", async () => {
            const response = {
                status: 200,
                data: [
                    { "granularityName": "1 August 2021", "totalSales": 1, "totalRevenue": 5.99 },
                    { "granularityName": "2 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "3 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "4 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "5 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "6 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "7 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "8 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "9 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "10 August 2021", "totalSales": 1, "totalRevenue": 19.99 },
                    { "granularityName": "11 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "12 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "13 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "14 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "15 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "16 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "17 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "18 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "19 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "20 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "21 August 2021", "totalSales": 5, "totalRevenue": 85.80 },
                    { "granularityName": "22 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "23 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "24 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "25 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "26 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "27 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "28 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "29 August 2021", "totalSales": 0, "totalRevenue": 0 },
                    { "granularityName": "30 August 2021", "totalSales": 390, "totalRevenue": 9980.00 },
                    { "granularityName": "31 August 2021", "totalSales": 489, "totalRevenue": 10758.59 } ]
            };

            const yearDropdownOption = wrapper.find("#month-option");
            yearDropdownOption.trigger('click');
            await wrapper.vm.$nextTick();
            wrapper.vm.$data.selectedMonth = 'August'
            await wrapper.vm.$nextTick();

            Api.getSalesReport.mockImplementation(() => Promise.resolve(response));

            await wrapper.vm.retrieveSalesReport();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.salesReportData.length).toEqual(response.data.length)
            expect(wrapper.vm.$data.salesReportData).toEqual(response.data);

        })

        test("Test the retrieveSalesReport method correctly sets the invalidDayMsg when a section of the single day selector is empty", async () => {
            wrapper.vm.$data.selectedDay = '';

            await wrapper.vm.retrieveSalesReport();
            await wrapper.vm.$nextTick();

            expect(wrapper.vm.$data.invalidDayMsg).toBe("Please enter a date");

        })

    });

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


})
