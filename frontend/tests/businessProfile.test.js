import {shallowMount} from '@vue/test-utils'
import BusinessProfile from "../src/views/BusinessProfile";
import {afterEach, beforeEach, describe, expect, jest, test} from "@jest/globals";

import Cookies from "js-cookie"
import Api from "../src/Api";

import {UserRole} from "../src/configs/User";

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
        data: {
            "id": 4,
            "administrators": [
                {
                    "id": 21,
                    "firstName": "John",
                    "lastName": "Doe",
                    "middleName": "S",
                    "nickname": "Johnny",
                    "bio": "Biography",
                    "email": "email@email.com",
                    "created": "2021-02-02T00:00",
                    "role": "DEFAULTGLOBALAPPLICATIONADMIN",
                    "businessesAdministered": [],
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
                }
            ],
            "primaryAdministratorId": 21,
            "name": "Kartofell",
            "description": null,
            "address": {
                "streetNumber": null,
                "streetName": null,
                "suburb": null,
                "city": null,
                "region": null,
                "country": "Deutschland",
                "postcode": null
            },
            "businessType": "ACCOMMODATION_AND_FOOD_SERVICES",
            "created": "2021-09-18T16:37:51.290710",
            "currencySymbol": "€",
            "currencyCode": "EUR",
            "businessImages": []
        }
    }))
}

let wrapper;
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
        push: jest.fn()
    }
})

afterEach(() => {
    jest.clearAllMocks();
});

const createWrapper = async () => {
    wrapper = await shallowMount(BusinessProfile, {
        mocks: {
            $router,
            $route
        },
    });
}

describe("Testing that the change profile button appears only when allowed to.", () => {


    test("Testing that if you are not acting as a business then the button does not appear.", async () => {
        await setupCookies(21, undefined);

        await  createWrapper();

        await wrapper.vm.$nextTick();
        const button = await wrapper.find("#update-business-image-button");

        expect(button.exists()).toBeFalsy();
    });

    test("Testing that if you are acting as a business then the button does appear.", async () => {
        await setupCookies(21, 4);

        await createWrapper();

        await wrapper.vm.$nextTick();
        const button = await wrapper.find("#update-business-image-button");

        expect(button.exists()).toBeTruthy();
    });
});

describe("Testing getImageSrc()", () => {

    test("Testing the getImageSrc() will return default image when file name is ''", async () => {
        await createWrapper();

        expect(await wrapper.vm.getImageSrc('')).toBe("test-file-stub")
    })

    test("Testing the getImageSrc() will return default image when file name is not provided", async () => {
        await createWrapper();

        expect(await wrapper.vm.getImageSrc()).toBe("test-file-stub")
    })

    test("Testing the getImageSrc() will return image src when file name is given", async () => {
        await createWrapper();

        expect(await wrapper.vm.getImageSrc('userImage.jpg')).toBe("undefined/userImage.jpg")
    })
})

describe("Testing formatAge function", () => {

    test("Testing with a expected date string and output Sep 18 2021", async () => {
        const result = BusinessProfile.methods.formatDate("2021-09-18T16:37:51.290710");

        expect(result).toStrictEqual("Sep 18 2021");
    })

    test("Testing that formatting a 1234 date then it will return a weird correct result.", async () => {
        expect(BusinessProfile.methods.formatDate(1234)).toStrictEqual("Jan 01 1970");
    })

    test("Testing that formatting a 1234 date object then it will return a weird correct result.", async () => {
        const testDate = new Date(1234)
        expect(BusinessProfile.methods.formatDate(testDate)).toStrictEqual("Jan 01 1970");
    })
})

describe("Testing the getCreatedDate function", () => {

    Date.now = jest.fn(() => new Date(Date.UTC(2007, 1, 14)).valueOf())

    test("Testing that getCreatedDate behaves with a date string", async () => {
        await createWrapper();

        await wrapper.vm.getCreatedDate("2021-09-18T16:37:51.290710");
        expect(wrapper.vm.$data.created).toStrictEqual("Sep 18 2021 (-176 months ago)");
    })

    test("Testing that formatting a 1234 date then it will return a weird correct result.", async () => {
        await createWrapper();

        await wrapper.vm.getCreatedDate(1234);
        expect(wrapper.vm.$data.created).toStrictEqual("Jan 01 1970 (445 months ago)");
    })

    test("Testing that formatting a 1234 date object then it will return a weird correct result.", async () => {
        await createWrapper();

        await wrapper.vm.getCreatedDate(new Date(1234));
        expect(wrapper.vm.$data.created).toStrictEqual("Jan 01 1970 (445 months ago)");
    })
})

describe("Testing the retrieveBusiness function", () => {

    test("Testing that a timeout is thrown when the getBusiness api call has a timeout error", async () => {
        await setupCookies(21, 4);
        Api.getBusiness.mockImplementation(() => Promise.reject({
            request: true
        }));
        await createWrapper();
        expect($router.push).toHaveBeenCalledWith({path: '/timeout'});
    })

    test("Testing that a 401 status error routes you to the /invalidtoken", async () => {
        await setupCookies(21, 4);
        Api.getBusiness.mockImplementation(() => Promise.reject({
            response: {
                status: 401
            }
        }));
        await createWrapper();
        expect($router.push).toHaveBeenCalledWith({path: '/invalidtoken'});
    })

    test("Testing that a 406 status error routes you to the /noBusiness route", async () => {
        await setupCookies(21, 4);
        Api.getBusiness.mockImplementation(() => Promise.reject({
            response: {
                status: 406
            }
        }));
        await createWrapper();
        expect($router.push).toHaveBeenCalledWith({path: '/noBusiness'});
    })

    test("Testing that if the response has a 500 error code then we are taken to /noBusiness by default", async () => {
        await setupCookies(21, 4);
        Api.getBusiness.mockImplementation(() => Promise.reject({
            response: {
                status: 500
            }
        }));
        await createWrapper();
        expect($router.push).toHaveBeenCalledWith({path: '/noBusiness'});
    })

    test("Testing that has no response then we are taken to /noBusiness by default", async () => {
        await setupCookies(21, 4);
        Api.getBusiness.mockImplementation(() => Promise.reject({
            request: true,
            response: true
        }));
        await createWrapper();
        expect($router.push).toHaveBeenCalledWith({path: '/noBusiness'});
    })
})


describe("Testing that data variables get populated correctly", () => {

    const response = { data: {
        id: 4,
        administrators: [
            {
                id: 21,
                firstName: "John",
                lastName: "Doe",
                middleName: "S",
                nickname: "Johnny",
                bio: "Biography",
                email: "email@email.com",
                created: "2021-02-02T00:00",
                role: "DEFAULTGLOBALAPPLICATIONADMIN",
                businessesAdministered: [],
                images:[],
                dateOfBirth: "2000-02-02",
                phoneNumber: "0271316",
                homeAddress: {
                    streetNumber: "3/24",
                    streetName:"Ilam Road",
                    suburb:"Ilam",
                    city:"Christchurch",
                    region:"Canterbury",
                    country:"New Zealand",
                    postcode:"90210"}
            }
        ],
        primaryAdministratorId:21,
        name:"kartofell",
        description:"asd",
        address:{
            streetNumber:"asd",
            streetName:"asd",
            suburb:"asd",
            city:"asd",
            region:"asd",
            country:"Deautschland",
            postcode:"123"},
        businessType:"ACCOMMODATION_AND_FOOD_SERVICES",
        created:"2021-09-19T15:36:42.328003",
        currencySymbol:"#",
        currencyCode:"123"
    }}

    test("Testing that with a full response all the values are correctly assigned.", async () => {
        await setupCookies(21, 4);
        Api.getBusiness.mockImplementation(() => Promise.resolve(response));
        await createWrapper();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        const expectEquality = (vmValue, responseValue) => {
            expect(vmValue).toStrictEqual(responseValue);
        }

        expectEquality(wrapper.vm.$data.name, response.data.name);
        expectEquality(wrapper.vm.$data.business.data.name, response.data.name);
        expectEquality(wrapper.vm.$data.description, response.data.description);
        expect(wrapper.vm.$data.businessType).toStrictEqual("Accommodation And Food Services");
        expectEquality(wrapper.vm.$data.streetNumber, response.data.address.streetNumber);
        expectEquality(wrapper.vm.$data.streetName, response.data.address.streetName);
        expectEquality(wrapper.vm.$data.region, response.data.address.region);
        expectEquality(wrapper.vm.$data.country, response.data.address.country)
        expectEquality(wrapper.vm.$data.city, response.data.address.city);
        expectEquality(wrapper.vm.$data.postcode, response.data.address.postcode);
        expectEquality(wrapper.vm.$data.address, [{"line": "asd asd"}, {"line": "asd"}, {"line": "asd, 123"}, {"line": "asd, Deautschland"}]);
        expectEquality(wrapper.vm.$data.primaryAdministratorId, response.data.primaryAdministratorId);
        expectEquality(wrapper.vm.$data.primaryAdministrator, "John S Doe")
        expectEquality(wrapper.vm.$data.nameOfAdministrators, [{"id": 21, "name": "John S Doe"}])
    })
})


describe("Testing pushToUser function", () => {
    test("Testing the pushToUser pushes the route with the param id 123 ", async () => {
        await setupCookies(21, 4);
        await createWrapper();
        await wrapper.vm.pushToUser(123);
        expect($router.push).toHaveBeenCalledWith({name: "Profile", params: {id: 123}})
    })

    test("Testing the pushToUser pushes the route with the param id '123' ", async () => {
        await setupCookies(21, 4);
        await createWrapper();
        await wrapper.vm.pushToUser('123');
        expect($router.push).toHaveBeenCalledWith({name: "Profile", params: {id: '123'}})
    })
})

describe("Testing the isGaaOrDgaa function", () => {

    test("Testing that DGAA returns true.", async () => {
        expect(BusinessProfile.methods.isGaaOrDgaa(UserRole.DEFAULTGLOBALAPPLICATIONADMIN)).toBeTruthy();
    })

    test("Testing that GAA returns true.", async () => {
        expect(BusinessProfile.methods.isGaaOrDgaa(UserRole.GLOBALAPPLICATIONADMIN)).toBeTruthy();
    })

    test("Testing that USER returns false.", async () => {
        expect(BusinessProfile.methods.isGaaOrDgaa(UserRole.USER)).toBeFalsy();
    })

    test("Testing that '1231231' returns false.", async () => {
        expect(BusinessProfile.methods.isGaaOrDgaa("1231231")).toBeFalsy();
    })
})