/**
 * Jest tests for PageButtons.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import PageButtons from "../src/components/PageButtons";

describe("Testing the page button functionality", () => {

    test('Testing the first page button is disabled when on the first page', () => {
        const buttonSelector = '#firstButton';

        const currentPage = 0;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes()).toContain('disabled');
    })

    test('Testing the previous page button is disabled when on the first page', () => {
        const buttonSelector = '#previousButton';

        const currentPage = 0;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes()).toContain('disabled');
    })

    test('Testing the next page button is disabled when on the last page', () => {
        const buttonSelector = '#nextButton';

        const currentPage = 0;
        const totalPages = 1;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes()).toContain('disabled');
    })

    test('Testing the last page button is disabled when on the last page', () => {
        const buttonSelector = '#lastButton';

        const currentPage = 0;
        const totalPages = 1;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes()).toContain('disabled');
    })

    test('Testing the first page button is enabled when on the last page', () => {
        const buttonSelector = '#firstButton';

        const currentPage = 1;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes().includes('disabled')).toBeFalsy();
    })

    test('Testing the previous page button is enabled when on the last page', () => {
        const buttonSelector = '#previousButton';

        const currentPage = 1;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes().includes('disabled')).toBeFalsy();
    })

    test('Testing the last page button is enabled when on the first page', () => {
        const buttonSelector = '#lastButton';

        const currentPage = 0;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes().includes('disabled')).toBeFalsy();
    })

    test('Testing the next page button is enabled when on the first page', () => {
        const buttonSelector = '#nextButton';

        const currentPage = 0;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        const button = pageButtonWrapper.find(buttonSelector);
        expect(button.classes().includes('disabled')).toBeFalsy();
    })

    test('Testing that when there are five or more pages and you are on page 1 there are five numbered page buttons', () => {
        const currentPage = 0;
        const totalPages = 6;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        let buttonSelector = '#pageButton1';
        let button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton2';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton3';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton4';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton5';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton6';
        expect(pageButtonWrapper.find(buttonSelector).exists()).toBeFalsy();
    })

    test('Testing that when there are five or more pages and you are on page 4 there are five numbered page buttons', () => {
        const currentPage = 3;
        const totalPages = 7;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        let buttonSelector = '#pageButton2';
        let button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton3';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton4';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton5';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton6';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton1';
        expect(pageButtonWrapper.find(buttonSelector).exists()).toBeFalsy();

        buttonSelector = '#pageButton7';
        expect(pageButtonWrapper.find(buttonSelector).exists()).toBeFalsy();
    })

    test('Testing that when there are two pages and you are on page 1 there are two numbered page buttons', () => {
        const currentPage = 0;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        let buttonSelector = '#pageButton1';
        let button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton2';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton3';
        expect(pageButtonWrapper.find(buttonSelector).exists()).toBeFalsy();
    })

    test('Testing that when there are two pages and you are on page 2 there are two numbered page buttons', () => {
        const currentPage = 1;
        const totalPages = 2;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        let buttonSelector = '#pageButton1';
        let button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton2';
        button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton3';
        expect(pageButtonWrapper.find(buttonSelector).exists()).toBeFalsy();
    })

    test('Testing that when there is one page there is one numbered page button', () => {
        const currentPage = 0;
        const totalPages = 1;
        const pageButtonWrapper = shallowMount(
            PageButtons,
            {
                propsData: {
                    currentPage,
                    totalPages
                },
            });

        let buttonSelector = '#pageButton1';
        let button = pageButtonWrapper.find(buttonSelector);
        expect(button.isVisible()).toBeTruthy();

        buttonSelector = '#pageButton2';
        expect(pageButtonWrapper.find(buttonSelector).exists()).toBeFalsy();
    })
})

describe("Testing the page buttons component methods", () => {
    const currentPage = 0;
    const totalPages = 2;
    const pageButtonWrapper = shallowMount(
        PageButtons,
        {
            propsData: {
                currentPage,
                totalPages
            },
        });

    test('Testing the updatePage method emits an updatePage event with the new page number', () => {
        const testInputVal = 1;
        const expectedOutput = {'updatePage': [[1]]};

        pageButtonWrapper.vm.updatePage(testInputVal);
        pageButtonWrapper.vm.$nextTick(() => {
            expect(pageButtonWrapper.emitted()).toEqual(expectedOutput);
            expect(pageButtonWrapper.emitted().updatePage.length).toBe(1);
        })
    })

    describe("Testing the isValidPageNumber method", () => {

        test('Testing that page number is 0 returns true', () => {
            const testInputVal = 0;
            const expectedOutput = true;

            expect(
                pageButtonWrapper.vm.isValidPageNumber(testInputVal)
            ).toEqual(expectedOutput);

        })

        test('Testing that page number greater than 0 and less than totalPages returns true', () => {
            const testInputVal = 1;
            const expectedOutput = true;

            expect(
                pageButtonWrapper.vm.isValidPageNumber(testInputVal)
            ).toEqual(expectedOutput);
        })

        test('Testing that page number equals the total pages returns false', () => {
            const testInputVal = 2;
            const expectedOutput = false;

            expect(
                pageButtonWrapper.vm.isValidPageNumber(testInputVal)
            ).toEqual(expectedOutput);
        })

    })

})