import {test, expect, describe, jest} from "@jest/globals";
import Api from "../src/Api";
import {createLocalVue, shallowMount} from "@vue/test-utils";
import Cookies from "js-cookie";
import EditProfile from "../src/views/EditProfile";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";

jest.mock("../src/Api");
jest.mock("js-cookie");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled : false});
localVue.use(VueRouter);

/**
 * Jest tests for editProfile.vue
 */

describe("Testing the autofill of data", () => {

    let editProfileWrapper;

    beforeEach(async () => {
        let $route = {
            params: {
                id: 1
            }
        }
        const mockApiResponse = {
            status: 200,
            data: {
                firstName: "first",
                middleName: "second",
                lastName: "third",
                nickname: "nick",
                homeAddress: {
                    streetNumber: "123",
                    streetName: "Road Street",
                    suburb: "Suburb",
                    city: "City",
                    postcode: "1234",
                    region: "Region",
                    country: "Country"
                },
                email: "email@example.com",
                bio: "Biography",
                phoneNumber: "0210210210",
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );
        Cookies.get.mockReturnValue(1);

        editProfileWrapper = shallowMount(EditProfile, {
            mocks: {
                $route
            }
        });
    })

    test("Testing name values are set", () => {
        expect(editProfileWrapper.find("#first-name").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#first-name").element.value).toBe("first")

        expect(editProfileWrapper.find("#middle-name").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#middle-name").element.value).toBe("second")

        expect(editProfileWrapper.find("#last-name").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#last-name").element.value).toBe("third")

        expect(editProfileWrapper.find("#nickname").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#nickname").element.value).toBe("nick")
    })

    test("Testing address values are set", () => {
        expect(editProfileWrapper.find("#streetNumber").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#streetNumber").element.value).toBe("123")

        expect(editProfileWrapper.find("#streetName").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#streetName").element.value).toBe("Road Street")

        expect(editProfileWrapper.find("#suburb").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#suburb").element.value).toBe("Suburb")

        expect(editProfileWrapper.find("#city").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#city").element.value).toBe("City")

        expect(editProfileWrapper.find("#region").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#region").element.value).toBe("Region")

        expect(editProfileWrapper.find("#postcode").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#postcode").element.value).toBe("1234")

        expect(editProfileWrapper.find("#country").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#country").element.value).toBe("Country")
    })

    test("Testing other values are set", () => {
        expect(editProfileWrapper.find("#email").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#email").element.value).toBe("email@example.com")

        expect(editProfileWrapper.find("#phone-number").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#phone-number").element.value).toBe("0210210210")

        expect(editProfileWrapper.find("#bio").exists()).toBeTruthy()
        expect(editProfileWrapper.find("#bio").element.value).toBe("Biography")
    })
})