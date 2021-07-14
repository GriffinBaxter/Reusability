/**
 * Jest tests for ProfileHeader.vue.
 * @jest-environment jsdom
 */

import {jest, describe, expect, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import ProfileHeader from "../src/components/ProfileHeader";

describe("Testing the search type functionality", () => {

    describe("Testing the changeSearchType method", () => {

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

    describe("Testing the placeholder value setting", () => {

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

                expect(profileHeaderWrapper.vm.selectedBusinessType).toEqual('Any');
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

    describe("Testing the URL populates correctly when searching for users", () => {

        test('Testing that pressing enter when the search type is User populates the URL correctly', () => {
            const $router = {
                push: jest.fn()
            };
            const profileHeaderWrapper = shallowMount(ProfileHeader, {
                mocks: {
                    $router
                }
            });

            profileHeaderWrapper.vm.searchType = 'User';

            let inputQuery = 'User Search Enter Test';
            profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

            profileHeaderWrapper.vm.$nextTick().then(() => {
                let searchBar = profileHeaderWrapper.find('#search-bar');
                searchBar.trigger('keydown.enter');

                expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `User`, searchQuery: `${inputQuery}`, orderBy: `fullNameASC`, page: "1"}});
            });
        });

        test('Testing that clicking the search button when the search type is User populates the URL correctly', () => {
            const $router = {
                push: jest.fn()
            };
            const profileHeaderWrapper = shallowMount(ProfileHeader, {
                mocks: {
                    $router
                }
            });

            profileHeaderWrapper.vm.searchType = 'User';

            let inputQuery = 'User Search Click Test';
            profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

            profileHeaderWrapper.vm.$nextTick().then(() => {
                let searchButton = profileHeaderWrapper.find('#search-button');
                searchButton.trigger('click');

                expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `User`, searchQuery: `${inputQuery}`, orderBy: `fullNameASC`, page: "1"}});
            });
        });

    });

    describe("Testing the URL populates correctly when searching for businesses", () => {

        describe("Testing URL population when business type is default", () => {

            test('Testing that pressing enter when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Enter Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let searchBar = profileHeaderWrapper.find('#search-bar');
                    searchBar.trigger('keydown.enter');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });

            test('Testing that clicking the search button when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Click Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let searchButton = profileHeaderWrapper.find('#search-button');
                    searchButton.trigger('click');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });
        });

        describe("Testing URL population when business type is Accommodation and Food Services", () => {

            test('Testing that pressing enter when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Enter Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#accommodation-and-food-services");
                    dropdownOption.setSelected();

                    let searchBar = profileHeaderWrapper.find('#search-bar');
                    searchBar.trigger('keydown.enter');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });

            test('Testing that clicking the search button when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Click Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#accommodation-and-food-services");
                    dropdownOption.setSelected();

                    let searchButton = profileHeaderWrapper.find('#search-button');
                    searchButton.trigger('click');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });
        });

        describe("Testing URL population when business type is Retail Trade", () => {

            test('Testing that pressing enter when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Enter Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#retail-trade");
                    dropdownOption.setSelected();

                    let searchBar = profileHeaderWrapper.find('#search-bar');
                    searchBar.trigger('keydown.enter');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });

            test('Testing that clicking the search button when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Click Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#retail-trade");
                    dropdownOption.setSelected();

                    let searchButton = profileHeaderWrapper.find('#search-button');
                    searchButton.trigger('click');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });
        });

        describe("Testing URL population when business type is Charitable Organisation", () => {

            test('Testing that pressing enter when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Enter Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#charitable-organisation");
                    dropdownOption.setSelected();

                    let searchBar = profileHeaderWrapper.find('#search-bar');
                    searchBar.trigger('keydown.enter');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });

            test('Testing that clicking the search button when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Click Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#charitable-organisation");
                    dropdownOption.setSelected();

                    let searchButton = profileHeaderWrapper.find('#search-button');
                    searchButton.trigger('click');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });
        });

        describe("Testing URL population when business type is Non Profit Organisation", () => {

            test('Testing that pressing enter when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Enter Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#non-profit-organisation");
                    dropdownOption.setSelected();

                    let searchBar = profileHeaderWrapper.find('#search-bar');
                    searchBar.trigger('keydown.enter');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });

            test('Testing that clicking the search button when the search type is Business populates the URL correctly', () => {
                const $router = {
                    push: jest.fn()
                };
                const profileHeaderWrapper = shallowMount(ProfileHeader, {
                    mocks: {
                        $router
                    }
                });

                profileHeaderWrapper.vm.searchType = 'Business';

                let inputQuery = 'Business Search Click Test';
                profileHeaderWrapper.vm.$refs.searchInput.value = inputQuery;

                profileHeaderWrapper.vm.$nextTick().then(() => {
                    let dropdownOption = profileHeaderWrapper.find("#non-profit-organisation");
                    dropdownOption.setSelected();

                    let searchButton = profileHeaderWrapper.find('#search-button');
                    searchButton.trigger('click');

                    let businessType = profileHeaderWrapper.vm.selectedBusinessType;

                    expect($router.push).toHaveBeenCalledWith({ path: '/search', query: { type: `Business`, businessType: `${businessType}`, searchQuery: `${inputQuery}`, orderBy: `nameASC`, page: "1"}});
                });
            });
        });
    });

});