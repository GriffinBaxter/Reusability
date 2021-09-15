import { shallowMount } from '@vue/test-utils'
import BrowseListings from '../src/views/BrowseListings.vue'
import {beforeAll, jest} from "@jest/globals";
import Api from "../src/Api";

jest.mock("../src/Api")

let factory;

describe('Tests for BrowseListings',  () => {

    let data;
    let $route;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/browseListings',
            query: {}
        }
        data = {
            status: 200,
            data: [
                {}],
            headers: {"total-pages": 5}
        }

        Api.searchListings.mockImplementation(() => Promise.resolve(data));

        factory = (values = {}) => {
            return shallowMount(BrowseListings, {
                data () {
                    return {
                        ...values
                    }
                },
                mocks: {
                    $router,
                    $route
                },

            })
        }

        Promise.resolve();

    });

    describe('Test No Listings Found message is displayed correctly in the Browse Listings page.', () => {

        test('No Listings Found message component is rendered when no listings are retrieved in the Browse Listings page.', async () => {
            const wrapper = factory({
                listings: [],
                notInitialLoad: true,
            });
            expect(wrapper.find('.noListings').exists()).toBeTruthy()
        })

        test('No Listings Found message component is not rendered when listings are retrieved in the Browse Listings page.', async () => {
            const wrapper = factory({
                listings: [
                    {
                        id: 23,
                        closes: "1st Aug 2021",
                        created: "1st Jul 2021",
                        expires: "1st Aug 2021",
                        isBookmarked: false,
                        moreInfo: "Selling quick.",
                        price: 11.96,
                        quantity: 4,
                        totalBookmarks: 0
                    }
                ],
                notInitialLoad: true,

            })
            await wrapper.vm.$nextTick();

            expect(wrapper.find('.noListings').exists()).toBeFalsy();
        })

        test('No Listings Found message component is not rendered when listings have not yet been retrieved in the Browse Listings page.', async () => {
            const wrapper = factory({
                listings: [],
                notInitialLoad: false,
            });
            expect(wrapper.find('.noListings').exists()).toBeFalsy()
        })

    })

})


