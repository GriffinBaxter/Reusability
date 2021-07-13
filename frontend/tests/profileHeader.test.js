/**
 * Jest tests for ProfileHeader.vue.
 * @jest-environment jsdom
 */

import {describe, expect, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import ProfileHeader from "../src/components/ProfileHeader";

describe("Testing the search type radio button functionality", () => {

    describe("Testing the changeSearchType", () => {

        test('Testing changeSearchType sets the search type to the input', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'User';

            profileHeaderWrapper.vm.changeSearchType('Business');
            expect(profileHeaderWrapper.vm.searchType).toEqual('Business');

            profileHeaderWrapper.vm.changeSearchType('User');
            expect(profileHeaderWrapper.vm.searchType).toEqual('User');
        });

        test('Testing changeSearchType is called with User when the User radio button is clicked', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';
            expect(profileHeaderWrapper.vm.searchType).toEqual('Business');

            let radioButton = profileHeaderWrapper.find('#user-radio-button');
            radioButton.trigger('click');

            expect(profileHeaderWrapper.vm.searchType).toEqual('User');
        });

        test('Testing changeSearchType is called with Business when the Business radio button is clicked', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'User';
            expect(profileHeaderWrapper.vm.searchType).toEqual('User');

            let radioButton = profileHeaderWrapper.find('#business-radio-button');
            radioButton.trigger('click');

            expect(profileHeaderWrapper.vm.searchType).toEqual('Business');
        });

    });

    describe("Testing the placeholder value", () => {

        test('Testing placeholder returns the correct value when the search type is User', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'User';
            expect(profileHeaderWrapper.vm.placeholder).toEqual('Search all users');
        });

        test('Testing placeholder returns the correct value when the search type is Business', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';
            expect(profileHeaderWrapper.vm.placeholder).toEqual('Search all businesses');
        });

    });

    describe("Testing the business type dropdown", () => {

        test('Testing business type dropdown is not visible when the search type is User', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'User';

            profileHeaderWrapper.vm.$nextTick().then(() => {
                expect(profileHeaderWrapper.find('#business-type-combo-box-container').exists()).toBe(false);
            });
        });

        test('Testing business type dropdown is visible when the search type is Business', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';

            profileHeaderWrapper.vm.$nextTick().then(() => {
                expect(profileHeaderWrapper.find('#business-type-combo-box').exists()).toBe(true);
            });
        });

        test('Testing that when Any is selected in the dropdown yet, selectedBusinessType is not populated', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';

            profileHeaderWrapper.vm.$nextTick().then(() => {
                const dropdownOption = profileHeaderWrapper.find("#default-option");
                dropdownOption.setSelected();

                expect(profileHeaderWrapper.vm.selectedBusinessType).toEqual('');
            });
        });

        test('Testing that when Accommodation and Food Services is selected in the dropdown, selectedBusinessType is set to it', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';

            profileHeaderWrapper.vm.$nextTick().then(() => {
                const dropdownOption = profileHeaderWrapper.find("#accommodation-and-food-services");
                dropdownOption.setSelected();

                expect(profileHeaderWrapper.vm.selectedBusinessType).toEqual('Accommodation and Food Services');
            });
        });

        test('Testing that when Retail Trade is selected in the dropdown, selectedBusinessType is set to it', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';

            profileHeaderWrapper.vm.$nextTick().then(() => {
                const dropdownOption = profileHeaderWrapper.find("#retail-trade");
                dropdownOption.setSelected();

                expect(profileHeaderWrapper.vm.selectedBusinessType).toEqual('Retail Trade');
            });
        });

        test('Testing that when Charitable Organisation is selected in the dropdown, selectedBusinessType is set to it', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';

            profileHeaderWrapper.vm.$nextTick().then(() => {
                const dropdownOption = profileHeaderWrapper.find("#charitable-organisation");
                dropdownOption.setSelected();

                expect(profileHeaderWrapper.vm.selectedBusinessType).toEqual('Charitable Organisation');
            });
        });

        test('Testing that when Non Profit Organisation is selected in the dropdown, selectedBusinessType is set to it', () => {
            const profileHeaderWrapper = shallowMount(ProfileHeader);
            profileHeaderWrapper.vm.searchType = 'Business';

            profileHeaderWrapper.vm.$nextTick().then(() => {
                const dropdownOption = profileHeaderWrapper.find("#non-profit-organisation");
                dropdownOption.setSelected();

                expect(profileHeaderWrapper.vm.selectedBusinessType).toEqual('Non Profit Organisation');
            });
        });

    });

});