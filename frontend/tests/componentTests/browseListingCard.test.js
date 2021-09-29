/**
 * @jest-environment jsdom
 */

import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
import {expect, jest, describe, afterEach, test} from "@jest/globals";
import BrowseListingCard from "../../src/components/listing/BrowseListingCard";
import Api from "../../src/Api";

jest.mock("../../src/Api");

let wrapper;

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false});
localVue.use(VueRouter);

let data;

const propDefault = {
    closes: "2021-06-10T00:00",
    created: "2021-05-12T00:00",
    id: 1,
    inventoryItem: {
        expire: "2021-05-12T00:00",
        product: {
            name: "Apple",
            images: [],
            business: {
                address: {
                    city: "Chaoyang District",
                    country: "China",
                    postcode: "100102",
                    region: "Beijing",
                    streetName: "Wangjing Zhonghuan Nanlu",
                    streetNumber: "7",
                    suburb: null
                },
                currencyCode: "NZD",
                currencySymbol: "$"
            }
        }
    },
    isBookmarked: true,
    moreInfo: "stop",
    price: 10,
    quantity: 1,
    totalBookmarks: 1
};

afterEach(() => {
    wrapper.destroy();
});

describe("Tests for bookmark component existing", () => {

    beforeEach(() => {
        // Given
        data = {
            status: 200,
        };

        Api.changeStatusOfAListing.mockImplementation(() => Promise.resolve(data))
    })

    test("Test for if bookmark false, the heart not exist", async function () {
        // When
        wrapper = shallowMount(BrowseListingCard, {
            localVue,
            propsData: {...propDefault, isBookmarked: false},
            data() {
                return {
                    tooltipList: [],
                    isMarked: this.isBookmarked,
                }
            }
        });

        // Then
        expect(wrapper.find("#bookmark_1").exists()).toBeFalsy();
    });

    test("Test for if bookmark true, the heart exist", async function () {
        // When
        wrapper = shallowMount(BrowseListingCard, {
            localVue,
            propsData: {...propDefault, isBookmarked: true},
            data() {
                return {
                    tooltipList: [],
                    isMarked: this.isBookmarked,
                }
            }
        });

        // Then
        expect(wrapper.find("#bookmark_1").exists()).toBeTruthy();
    });

    test("Test heart disappeared after click", async function () {
        // Given
        wrapper = shallowMount(BrowseListingCard, {
            localVue,
            propsData: {...propDefault, isBookmarked: true},
            data() {
                return {
                    tooltipList: [],
                    isMarked: this.isBookmarked,
                }
            }
        });
        expect(wrapper.find("#bookmark_1").exists()).toBeTruthy();

        // When
        await wrapper.find("#bookmarkButton_1").trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.find("#bookmark_1").exists()).toBeFalsy();
    })

    test("Test heart appeared after click", async function () {
        // Given
        wrapper = shallowMount(BrowseListingCard, {
            localVue,
            propsData: {...propDefault, isBookmarked: false},
            data() {
                return {
                    tooltipList: [],
                    isMarked: this.isBookmarked,
                }
            }
        });
        expect(wrapper.find("#bookmark_1").exists()).toBeFalsy();

        // When
        await wrapper.find("#bookmarkButton_1").trigger('click');
        await wrapper.vm.$nextTick();

        // Then
        expect(wrapper.find("#bookmark_1").exists()).toBeTruthy();
    })
})
