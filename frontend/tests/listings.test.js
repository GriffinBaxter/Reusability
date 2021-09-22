import {createLocalVue, shallowMount} from '@vue/test-utils'
import Listings from '../src/views/Listings.vue'
import VueLogger from "vuejs-logger";
import {jest} from "@jest/globals";
import Api from "./../src/Api";
import Cookies from "js-cookie";

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false})

jest.mock("../src/Api");
jest.mock("js-cookie");

const factory = (values = {}) => {
    return shallowMount(Listings, {
        data () {
            return {
                ...values
            }
        }
    })
}

describe('Test No Listings Found message is displayed correctly.',  () => {
    test('No Listings Found message component is rendered when no listings are retrieved.', async () => {
        const wrapper = factory({
            listings: [],
            notInitialLoad: true,
        });
        expect(wrapper.find('.noListings').exists()).toBeTruthy()
    })

    test('No Listings Found message component is not rendered when listings are retrieved.', async () => {
        const wrapper = factory({
            listings: [
                {
                    id: 1,
                    closeDate: "21st Jul 2021",
                    description: null,
                    expires: "21st Jul 2021",
                    listDate: "2nd Jul 2021",
                    moreInfo: "",
                    price: 21,
                    productId: "EGG",
                    productName: "Eggs",
                    quantity: 10,
                }
                ],
            notInitialLoad: true,
            })
        expect(wrapper.find('.noListings').exists()).toBeFalsy()
    })

    test('No Listings Found message component is not rendered when listings have not yet been retrieved.', async () => {
        const wrapper = factory({
            listings: [],
            notInitialLoad: false,
        });
        expect(wrapper.find('.noListings').exists()).toBeFalsy()
    })

})

describe("Testing the Listing pages methods", () => {
    let listingWrapper;
    let $route = {
        params: {
            id: 1,
        },
        query: {
            orderBy: "closesASC",
            page: 1,
            barcode: null
        }
    }
    let $router = {
        push: jest.fn()
    }
    let roleResponse = {
        status: 200,
        data: {
            role: "USER"
        }
    }
    let businessResponse = {
        status: 200,
        data: {
            name: "",
            currencySymbol: "",
            currencyCode: "",
            address: {
                country: ""
            },
            id: 5,
            administrators: [
                {
                    id: 10
                }
            ]
        }
    }
    let listingsResponse = {
        status: 200,
        data: [],
        headers: {
            "total-rows": 1,
            "total-pages": 1
        }
    }

    describe("Test the deleteListing function", () => {

        beforeEach(async () => {

            Cookies.get.mockReturnValueOnce(1);
            Api.getUser.mockImplementation(() => Promise.resolve(roleResponse))
            Api.getBusiness.mockImplementation(() => Promise.resolve(businessResponse))
            Api.sortListings.mockImplementation(() => Promise.resolve(listingsResponse))

            listingWrapper = shallowMount(Listings, {
                localVue,
                mocks: {
                    $route,
                    $router
                }
            });
            await listingWrapper.vm.$nextTick()
        })

        afterEach(() => {
            jest.clearAllMocks();
        });

        test("Test successful deleteListing function", async () => {
            let deleteResponse = {
                status: 200
            }
            Api.deleteListing.mockImplementation(() => Promise.resolve(deleteResponse))

            listingWrapper.vm.deleteListing(25)
            await listingWrapper.vm.$nextTick()

            expect(Api.sortListings).toHaveBeenCalled()
            expect(Api.sortListings).toHaveBeenCalledTimes(2)
        })

        test("Test 406 response on attempted deletion of listing", async () => {
            let deleteResponse = {
                response: {
                    status: 406
                }
            }
            Api.deleteListing.mockImplementation(() => Promise.reject(deleteResponse))

            listingWrapper.vm.deleteListing(25)
            await listingWrapper.vm.$nextTick()

            expect(Api.sortListings).toHaveBeenCalled()
            expect(Api.sortListings).toHaveBeenCalledTimes(2)
        })

        test("Test 403 response on attempted deletion of listing", async () => {
            let deleteResponse = {
                response: {
                    status: 403
                }
            }
            Api.deleteListing.mockImplementation(() => Promise.reject(deleteResponse))

            listingWrapper.vm.$data.businessAdmin = true

            await listingWrapper.vm.deleteListing(25)
            await listingWrapper.vm.$nextTick()

            expect(listingWrapper.vm.$data.businessAdmin).toBeFalsy()
        })

        test("Test 401 response on attempted deletion of listing", async () => {
            let deleteResponse = {
                response: {
                    status: 401
                }
            }
            Api.deleteListing.mockImplementation(() => Promise.reject(deleteResponse))

            await listingWrapper.vm.deleteListing(25)
            await listingWrapper.vm.$nextTick()

            expect($router.push).toHaveBeenCalled()
            expect($router.push).toHaveBeenCalledWith({name: "InvalidToken"})
        })
    })
})

