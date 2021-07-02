import { shallowMount } from '@vue/test-utils'
import Listings from '../src/views/Listings.vue'

const factory = (values = {}) => {
    return shallowMount(Listings, {
        data () {
            return {
                ...values
            }
        }
    })
}

describe('Test No Listings Found message is displayed correctly.', () => {
    test('No Listings Found message is rendered when no listings are retrieved.', () => {
        const wrapper = factory({
            listings: [],
            notInitialLoad: true,
        })
        expect(wrapper.find('.noListings').exists()).toBeTruthy()
    })

    test('No Listings Found message is not rendered when listings are retrieved.', () => {
        const wrapper = factory({
            listings: [
                {
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
})


