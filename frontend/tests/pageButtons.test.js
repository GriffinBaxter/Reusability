/**
 * Jest tests for PageButtons.vue.
 * @jest-environment jsdom
 */

import {test, expect, describe} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import PageButtons from "../src/components/PageButtons";

describe("Testing the page button functionality", () => {

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

    test('Testing that when there are three or more pages and you are on page 1 there are three numbered page buttons', () => {
        const currentPage = 0;
        const totalPages = 4;
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
        expect(pageButtonWrapper.find(buttonSelector).exists()).toBeFalsy();
    })

    test('Testing that when there are three or more pages and you are on page 3 there are three numbered page buttons', () => {
        const currentPage = 3;
        const totalPages = 4;
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

        buttonSelector = '#pageButton1';
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

        pageButtonWrapper.vm.$emit('updatePage', testInputVal);
        pageButtonWrapper.vm.$nextTick(() => {
            expect(pageButtonWrapper.emitted()).toEqual(expectedOutput);
            expect(pageButtonWrapper.emitted().updatePage.length).toBe(1);
        })

        pageButtonWrapper.vm.updatePage(testInputVal);
        pageButtonWrapper.vm.$nextTick(() => {
            // Number of emits stays the same as the event is emitted to the parent
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

// pageNumber = 0
// pageNumber > 0 && pageNumber < totalPages
// pageNumber = totalPages

// check event emitted

// need to shallow mount with props