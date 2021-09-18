import {createLocalVue, shallowMount} from '@vue/test-utils'
import BusinessProfile from "../src/views/BusinessProfile";
import {afterEach, beforeEach, describe, expect, jest, test} from "@jest/globals";

import Cookies from "js-cookie"
import Api from "../src/Api";

import VueLogger from "vuejs-logger";
const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false});


let wrapper;

jest.mock("../src/Api");
jest.mock("js-cookie");

const setupCookies = async (userId, actAsId) => {

    await Cookies.get.mockImplementation((cookie) => {
        if (cookie === "actAs") return actAsId;
        else if (cookie === "userID") return userId;
        return undefined
    })

    await Api.getUser.mockImplementation(() => Promise.resolve({
        status: 200,
        data: {
            "id": 21,
            "firstName": "John",
            "lastName": "Doe",
            "middleName": "S",
            "nickname": "Johnny",
            "bio": "Biography",
            "email": "email@email.com",
            "created": "2021-02-02T00:00",
            "role": "DEFAULTGLOBALAPPLICATIONADMIN",
            "businessesAdministered": [
                {"id":4,"administrators":[{"id":21,"firstName":"John","lastName":"Doe","middleName":"S","nickname":"Johnny","bio":"Biography","email":"email@email.com","created":"2021-02-02T00:00","role":"DEFAULTGLOBALAPPLICATIONADMIN","businessesAdministered":[],"images":[],"dateOfBirth":"2000-02-02","phoneNumber":"0271316","homeAddress":{"streetNumber":"3/24","streetName":"Ilam Road","suburb":"Ilam","city":"Christchurch","region":"Canterbury","country":"New Zealand","postcode":"90210"}}],"primaryAdministratorId":21,"name":"Kartofell","description":null,"address":{"streetNumber":null,"streetName":null,"suburb":null,"city":null,"region":null,"country":"Deutschland","postcode":null},"businessType":"ACCOMMODATION_AND_FOOD_SERVICES","created":"2021-09-18T16:37:51.290710","currencySymbol":"€","currencyCode":"EUR"}
            ],
            "images": [],
            "dateOfBirth": "2000-02-02",
            "phoneNumber": "0271316",
            "homeAddress": {
                "streetNumber": "3/24",
                "streetName": "Ilam Road",
                "suburb": "Ilam",
                "city": "Christchurch",
                "region": "Canterbury",
                "country": "New Zealand",
                "postcode": "90210"
            }
        },
    }))

    await Api.getBusiness.mockImplementation(() => Promise.resolve({
        status: 200,
        data: {"id":4,"administrators":[{"id":21,"firstName":"John","lastName":"Doe","middleName":"S","nickname":"Johnny","bio":"Biography","email":"email@email.com","created":"2021-02-02T00:00","role":"DEFAULTGLOBALAPPLICATIONADMIN","businessesAdministered":[],"images":[],"dateOfBirth":"2000-02-02","phoneNumber":"0271316","homeAddress":{"streetNumber":"3/24","streetName":"Ilam Road","suburb":"Ilam","city":"Christchurch","region":"Canterbury","country":"New Zealand","postcode":"90210"}}],"primaryAdministratorId":21,"name":"Kartofell","description":null,"address":{"streetNumber":null,"streetName":null,"suburb":null,"city":null,"region":null,"country":"Deutschland","postcode":null},"businessType":"ACCOMMODATION_AND_FOOD_SERVICES","created":"2021-09-18T16:37:51.290710","currencySymbol":"€","currencyCode":"EUR"}
    }))

    await Api.getBusinessNotifications.mockImplementation(() => Promise.resolve({
        status: 200,
        data: []
    }))

    await Api.getNotifications.mockImplementation( () => Promise.resolve({
        data:[],
        status:200
    }));

    await Api.signOut.mockImplementation( () => Promise.resolve(
        {status: 200}
    ));
}

let $route;
let $router;

beforeEach(() => {
    $route = {
        path: '/businessProfile/4',
        name: 'BusinessProfile',
        params: {
            id: 4
        }
    }

    $router = {
        push: jest.fn().mockImplementation((content) => {console.log(content)})
    }
})

afterEach(() => {
    jest.clearAllMocks();
});

describe("Testing that the change profile button appears only when allowed to.", () => {


    test("Testing that if you are not acting as a business then the button does not appear.", async () => {
        await setupCookies(21, undefined);

        wrapper = shallowMount(BusinessProfile, {
            localVue,
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });
        await wrapper.vm.$nextTick();
        const button = await wrapper.find("#update-business-image-button");

        expect(button.exists()).toBeFalsy();
    });

    test("Testing that if you are acting as a business then the button does appear.", async () => {
        await setupCookies(21, 4);


        wrapper = shallowMount(BusinessProfile, {
            localVue,
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });
        await wrapper.vm.$nextTick();
        const button = await wrapper.find("#update-business-image-button");

        expect(button.exists()).toBeTruthy();
    });
});

describe("Testing getImageSrc()", () => {

    test("Testing the getImageSrc() will return default image when file name is ''", async () => {

        const wrapper = await shallowMount(BusinessProfile);

        expect(await wrapper.vm.getImageSrc('')).toBe("test-file-stub")
    })

    test("Testing the getImageSrc() will return default image when file name is not provided", async () => {

        const wrapper = await shallowMount(BusinessProfile);

        expect(await wrapper.vm.getImageSrc()).toBe("test-file-stub")
    })

    test("Testing the getImageSrc() will return image src when file name is given", async () => {

        const wrapper = await shallowMount(BusinessProfile);

        expect(await wrapper.vm.getImageSrc('userImage.jpg')).toBe("undefined/userImage.jpg")
    })
})

describe("Testing formatAge function", () => {

    test("Testing with a expected date string and output Sep 18 2021", async () => {
        const result = BusinessProfile.methods.formatDate("2021-09-18T16:37:51.290710");

        expect(result).toStrictEqual("Sep 18 2021");
    })

    test("Testing that formating a 1234 date then it will return a weird correct result.", async () => {
        expect(BusinessProfile.methods.formatDate(1234)).toStrictEqual("Jan 01 1970");
    })

    test("Testing that formating a 1234 date object then it will return a weird correct result.", async () => {
        const testDate = new Date(1234)
        expect(BusinessProfile.methods.formatDate(testDate)).toStrictEqual("Jan 01 1970");
    })
})

describe("Testing the getCreatedDate function", () => {

    Date.now = jest.fn(() => new Date(Date.UTC(2007, 1, 14)).valueOf())

    test("Testing that getCreadtedDate behaves with a date string", async () => {
        wrapper = await shallowMount(BusinessProfile, {
            data() {return {
                created: ""
            }}
        });

        await wrapper.vm.getCreatedDate("2021-09-18T16:37:51.290710");
        expect(wrapper.vm.$data.created).toStrictEqual("Sep 18 2021 (-176 months ago)");
    })

    test("Testing that formating a 1234 date then it will return a weird correct result.", async () => {
        wrapper = await shallowMount(BusinessProfile, {
            data() {return {
                created: ""
            }}
        });

        await wrapper.vm.getCreatedDate(1234);
        expect(wrapper.vm.$data.created).toStrictEqual("Jan 01 1970 (445 months ago)");
    })

    test("Testing that formating a 1234 date object then it will return a weird correct result.", async () => {
        wrapper = await shallowMount(BusinessProfile, {
            data() {return {
                created: ""
            }}
        });

        await wrapper.vm.getCreatedDate(new Date(1234));
        expect(wrapper.vm.$data.created).toStrictEqual("Jan 01 1970 (445 months ago)");
    })
})


describe("Testing the retieveBusiness function", () => {

    const createWrapper = async () => {
        wrapper = await shallowMount(BusinessProfile, {
            localVue,
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']});
        await wrapper.vm.$nextTick();
    }


})