import {test, expect, describe, jest} from "@jest/globals";
import Api from "../src/Api";
import {shallowMount} from "@vue/test-utils";
import Cookies from "js-cookie";
import EditProfile from "../src/views/EditProfile";

jest.mock("../src/Api");
jest.mock("js-cookie");


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

describe("Testing validation when no input is recieved", () => {
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
                homeAddress: {
                }
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );
        Cookies.get.mockReturnValue(1);

        editProfileWrapper = shallowMount(EditProfile, {
            mocks: {
                $route
            }
        });

        await editProfileWrapper.vm.$nextTick();

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});

        await editProfileWrapper.vm.$nextTick();
    })

    test("Testing name error messages with no input", () => {
        expect(editProfileWrapper.vm.$data.firstNameErrorMsg).toBe("Please enter input")

        expect(editProfileWrapper.vm.$data.middleNameErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.lastNameErrorMsg).toBe("Please enter input")

        expect(editProfileWrapper.vm.$data.nicknameErrorMsg).toBe("")
    })

    test("Testing address error messages with no input", () => {
        expect(editProfileWrapper.vm.$data.streetNumberErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.streetNameErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.suburbErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.cityErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.regionErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.postcodeErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.countryErrorMsg).toBe("Please enter input")
    })

    test("Testing other error messages with no input", () => {
        expect(editProfileWrapper.vm.$data.emailErrorMsg).toBe("Please enter input")

        expect(editProfileWrapper.vm.$data.phoneNumberErrorMsg).toBe("")

        expect(editProfileWrapper.vm.$data.bioErrorMsg).toBe("")
    })
})

describe("Testing the response on save attempt", () => {

    let editProfileWrapper;
    let mockEditResponse;
    let id;

    beforeEach(async () => {

        id = 1;

        let $route = {
            params: {
                id: id
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
                dateOfBirth: "1999-11-05"
            }
        }

        Api.getUser.mockImplementation(() => Promise.resolve(mockApiResponse));
        Cookies.get.mockReturnValue(id);

        editProfileWrapper = shallowMount(EditProfile, {
            mocks: {
                $route
            }
        });
        await editProfileWrapper.vm.$nextTick();
    })

    test("System displays error message on 400 response", async () => {
        let message = "Email in use"
        mockEditResponse = {
            response: {
                status: 400,
                data: {
                    message: message
                }
            }
        }

        Api.editUser.mockImplementation(() => Promise.reject(mockEditResponse))

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});

        await editProfileWrapper.vm.$nextTick();

        expect(editProfileWrapper.vm.$data.errorMessageBubble).toBe(message)
    })
})