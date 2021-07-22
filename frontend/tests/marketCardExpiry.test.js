/**
 * @jest-environment jsdom
 */

import {shallowMount, createLocalVue} from '@vue/test-utils';
import cardDetailPopup from "../src/components/marketplace/CardDetailPopup";
import Api from "../src/Api";
import {jest} from "@jest/globals";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
import Cookies from "js-cookie"



let wrapper;

jest.mock("../src/Api");
jest.mock("js-cookie");


const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false});
localVue.use(VueRouter);

const cardResponse = {
    status : 200,
    data: {
        "id": 1,
        "creator": {
            "id": 1,
            "firstName": "John",
            "lastName": "Smith",
            "middleName": "Hector",
            "nickname": "Jonny",
            "bio": "Likes long walks on the beach",
            "email": "johnsmith99@gmail.com",
            "dateOfBirth": "1999-04-27",
            "phoneNumber": "+64 3 555 0129",
            "homeAddress": {
                "streetNumber": "3/24",
                "streetName": "Ilam Road",
                "suburb": "Upper Riccarton",
                "city": "Christchurch",
                "region": "Canterbury",
                "country": "New Zealand",
                "postcode": "90210"
            },
            "created": "2020-07-14T14:32:00Z",
            "role": "user",
            "businessesAdministered": []
        },
        "section": "ForSale",
        "created": "2021-07-15T05:10:00Z",
        "displayPeriodEnd": "2021-07-29T05:10:00Z",
        "title": "1982 Lada Samara",
        "description": "Beige, suitable for a hen house. Fair condition. Some rust. As is, where is. Will swap for budgerigar.",
        "keywords": [
            {
                "id": 600,
                "name": "Vehicle",
                "created": "2021-07-15T05:10:00Z"
            }
        ]
    }
};

beforeEach(() => {
    Api.getDetailForACard.mockImplementation(() => Promise.resolve(cardResponse));
});

afterEach(() => {
    wrapper.destroy();
});


describe("Test the remove button will only display when current user have permission to delete the card.", () => {
    /**
     * a function to mount and update the page. (reduce code duplication)
     * @returns {Promise<void>}
     */
    async function mountPage() {
        wrapper = shallowMount(cardDetailPopup, {
            localVue,
            propsData: {id: 1}
        });
        await wrapper.vm.$nextTick();

        await wrapper.vm.retrieveCardDetail(1);
        await wrapper.vm.$nextTick();
    }

    test('Test the button will display when current USER is the creator', async function () {
        Cookies.get.mockReturnValue(1)
        Api.getUser.mockImplementation(() => Promise.resolve({
            status : 200,
            data : {role: "USER"}
        }));

        await mountPage();
        expect(wrapper.find("#remove-card-button").exists()).toBeTruthy();
    });

    test('Test the button will not display when current USER is not the creator', async function () {
        Cookies.get.mockReturnValue(2)
        Api.getUser.mockImplementation(() => Promise.resolve({
            status : 200,
            data : {role: "USER"}
        }));

        await mountPage();
        expect(wrapper.find("#remove-card-button").exists()).toBeFalsy();
    });

    test('Test the button will display when current user is not the creator, but is a GAA', async function () {
        Cookies.get.mockReturnValue(2)
        Api.getUser.mockImplementation(() => Promise.resolve({
            status : 200,
            data : {role: "GLOBALAPPLICATIONADMIN"}
        }));

        await mountPage();
        expect(wrapper.find("#remove-card-button").exists()).toBeTruthy();
    });

    test('Test the button will display when current user is not the creator, but is a DGAA', async function () {
        Cookies.get.mockReturnValue(2)
        Api.getUser.mockImplementation(() => Promise.resolve({
            status : 200,
            data : {role: "DEFAULTGLOBALAPPLICATIONADMIN"}
        }));

        await mountPage();
        expect(wrapper.find("#remove-card-button").exists()).toBeTruthy();
    });
})


