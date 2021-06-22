/**
 * Jest tests for ProfileHeader.vue.
 * @jest-environment jsdom
 */

import {describe, expect, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import ProfileHeader from "../src/components/ProfileHeader";

describe("Testing the search type radio button functionality", () => {

    test('Testing changeSearchType sets the search type to the input', () => {
        const profileHeaderWrapper = shallowMount(ProfileHeader);
        profileHeaderWrapper.vm.searchType = 'User'

        profileHeaderWrapper.vm.changeSearchType('Business');
        profileHeaderWrapper.vm.$nextTick();
        expect(profileHeaderWrapper.vm.searchType).toEqual('Business');

        profileHeaderWrapper.vm.changeSearchType('User');
        profileHeaderWrapper.vm.$nextTick();
        expect(profileHeaderWrapper.vm.searchType).toEqual('User');
    });

    test('Testing placeholder returns the correct value when the search type is User', () => {
        const profileHeaderWrapper = shallowMount(ProfileHeader);
        profileHeaderWrapper.vm.searchType = 'User'
        expect(profileHeaderWrapper.vm.placeholder).toEqual('Search all users');
    });

    test('Testing placeholder returns the correct value when the search type is Business', () => {
        const profileHeaderWrapper = shallowMount(ProfileHeader);
        profileHeaderWrapper.vm.searchType = 'Business'
        expect(profileHeaderWrapper.vm.placeholder).toEqual('Search all businesses');
    });

});