import {test, expect, describe, jest} from "@jest/globals";
import Api from "../src/Api";
import {createLocalVue, shallowMount} from "@vue/test-utils";
import Cookies from "js-cookie";
import EditProfile from "../src/views/EditProfile";
import VueLogger from "vuejs-logger";

jest.mock("../src/Api");
jest.mock("js-cookie");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false})

const mockFullApiResponse = {
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

        Api.getUser.mockImplementation( () => Promise.resolve(mockFullApiResponse) );
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

describe("Testing validation when no input is received", () => {
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
                firstName: null,
                middleName: null,
                lastName: null,
                nickname: null,
                homeAddress: {
                    streetNumber: null,
                    streetName: null,
                    suburb: null,
                    city: null,
                    postcode: null,
                    region: null,
                    country: null
                },
                email: null,
                bio: null,
                phoneNumber: null,
                dateOfBirth: null
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

describe("Testing password error messages", () => {
    let editProfileWrapper;

    beforeEach(async () => {
        let $route = {
            params: {
                id: 1
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockFullApiResponse) );
        Cookies.get.mockReturnValue(1);

        editProfileWrapper = shallowMount(EditProfile, {
            mocks: {
                $route
            }
        });

        await editProfileWrapper.vm.$nextTick();
    })

    test("Test error message when confirm password is not equal to password", async () => {
        editProfileWrapper.vm.$data.currentPassword = "CurrentPass123!"
        editProfileWrapper.vm.$data.password = "NewPass123!"
        editProfileWrapper.vm.$data.confirmPassword = "FakePass123!"

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});
        await editProfileWrapper.vm.$nextTick();

        expect(editProfileWrapper.vm.$data.confirmPasswordErrorMsg).toBe("Confirmation password does not equal password field.")
    })

    test("Test error message when an invalid password has been entered", async () => {
        editProfileWrapper.vm.$data.currentPassword = "CurrentPass"
        editProfileWrapper.vm.$data.password = "NewPass"
        editProfileWrapper.vm.$data.confirmPassword = "NewPass"

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});
        await editProfileWrapper.vm.$nextTick();

        expect(editProfileWrapper.vm.$data.currentPasswordErrorMsg).toBe("Invalid password format")
        expect(editProfileWrapper.vm.$data.passwordErrorMsg).toBe("Invalid password format")
        expect(editProfileWrapper.vm.$data.confirmPasswordErrorMsg).toBe("")
    })
})

describe("Testing the response on save attempt", () => {

    let editProfileWrapper;
    let mockEditResponse;
    let id;
    let $router;

    beforeEach(async () => {

        id = 1;

        let $route = {
            params: {
                id: id
            }
        }

        $router = {
            push: jest.fn()
        }

        Api.getUser.mockImplementation(() => Promise.resolve(mockFullApiResponse));
        Cookies.get.mockReturnValue(id);

        editProfileWrapper = shallowMount(EditProfile, {
            localVue,
            mocks: {
                $route,
                $router
            }
        });
        await editProfileWrapper.vm.$nextTick();
    })

    test("Application displays error message on 400 response", async () => {
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

    test("Application routes to Invalid Token on 401 response", async () => {
        mockEditResponse = {
            response: {
                status: 401
            }
        }

        Api.editUser.mockImplementation(() => Promise.reject(mockEditResponse))

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});

        await editProfileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({name: "InvalidToken"})
    })

    test("Application displays error message and status code on other response error", async () => {
        let expectedMessage = "406 Unexpected error occurred!"
        mockEditResponse = {
            response: {
                status: 406
            }
        }

        Api.editUser.mockImplementation(() => Promise.reject(mockEditResponse))

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});

        await editProfileWrapper.vm.$nextTick();

        expect(editProfileWrapper.vm.$data.errorMessageBubble).toBe(expectedMessage)
    })

    test("Application displays timeout message on request error", async () => {
        let expectedMessage = "Timeout occurred"
        mockEditResponse = {
            request: {}
        }

        Api.editUser.mockImplementation(() => Promise.reject(mockEditResponse))

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});

        await editProfileWrapper.vm.$nextTick();

        expect(editProfileWrapper.vm.$data.errorMessageBubble).toBe(expectedMessage)
    })

    test("Application displays error messages for unexpected errors", async () => {
        let expectedMessage = "Unexpected error occurred!"
        mockEditResponse = {}

        Api.editUser.mockImplementation(() => Promise.reject(mockEditResponse))

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});

        await editProfileWrapper.vm.$nextTick();

        expect(editProfileWrapper.vm.$data.errorMessageBubble).toBe(expectedMessage)
    })


    test("Application routes to Profile on 200 response", async () => {
        mockEditResponse = {
            status: 200
        }

        Api.editUser.mockImplementation(() => Promise.resolve(mockEditResponse))

        await editProfileWrapper.vm.editUser({preventDefault: jest.fn()});

        await editProfileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({name: "Profile", params: {id: id}})
    })
})

describe("Tests for other users accessing your Edit Profile page", () => {

    let id;
    let $route;
    let $router

    beforeEach(async () => {
        id = 1;
        $route = {
            params: {
                id: id
            }
        }

        Cookies.get.mockReturnValue(2);
        $router = {
            push: jest.fn()
        }
    })

    test("Test that a DGAA can access Edit Profile for another user", async () => {
        const mockCurrentUserApiResponse= {
            status: 200,
            data: {
                role: "DEFAULTGLOBALAPPLICATIONADMIN"
            }
        };

        Api.getUser.mockImplementationOnce(() => Promise.resolve(mockCurrentUserApiResponse));
        Api.getUser.mockImplementationOnce(() => Promise.resolve(mockFullApiResponse));

        let editProfileWrapper = shallowMount(EditProfile, {
            localVue,
            mocks: {
                $route,
                $router
            }
        });
        await editProfileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledTimes(0)
    })

    test("Test that a GAA can access Edit Profile for another user", async () => {
        const mockCurrentUserApiResponse= {
            status: 200,
            data: {
                role: "GLOBALAPPLICATIONADMIN"
            }
        };

        Api.getUser.mockImplementationOnce(() => Promise.resolve(mockCurrentUserApiResponse));
        Api.getUser.mockImplementationOnce(() => Promise.resolve(mockFullApiResponse));

        let editProfileWrapper = shallowMount(EditProfile, {
            localVue,
            mocks: {
                $route,
                $router
            }
        });
        await editProfileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledTimes(0)
    })

    test("Test that a normal user can not access Edit Profile for another user", async () => {
        const mockCurrentUserApiResponse= {
            status: 200,
            data: {
                role: "USER"
            }
        };

        Api.getUser.mockImplementationOnce(() => Promise.resolve(mockCurrentUserApiResponse));
        Api.getUser.mockImplementationOnce(() => Promise.resolve(mockFullApiResponse));

        let editProfileWrapper = shallowMount(EditProfile, {
            localVue,
            mocks: {
                $route,
                $router
            }
        });
        await editProfileWrapper.vm.$nextTick();
        await editProfileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledTimes(1)
        expect($router.push).toHaveBeenCalledWith({name: "Profile", params: {id: id}})

    })

    test("Test that a logged out user can not access Edit Profile for another user", async () => {
        const mockCurrentUserApiResponse= {
            response: {
                status: 401
            }
        };

        Api.getUser.mockImplementationOnce(() => Promise.reject(mockCurrentUserApiResponse));

        let editProfileWrapper = shallowMount(EditProfile, {
            localVue,
            mocks: {
                $route,
                $router
            }
        });
        await editProfileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledTimes(1)
        expect($router.push).toHaveBeenCalledWith({name: "InvalidToken"})
    })

    test("Test that a GAA can not access Edit Profile for an invalid user", async () => {
        const mockCurrentUserApiResponse= {
            status: 200,
            data: {
                role: "GLOBALAPPLICATIONADMIN"
            }
        };

        const mockApiResponse = {
            response: {
                status: 406
            }
        }

        Api.getUser.mockImplementationOnce(() => Promise.resolve(mockCurrentUserApiResponse));
        Api.getUser.mockImplementationOnce(() => Promise.reject(mockApiResponse));

        let editProfileWrapper = shallowMount(EditProfile, {
            localVue,
            mocks: {
                $route,
                $router
            }
        });
        await editProfileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledTimes(1)
        expect($router.push).toHaveBeenCalledWith({name: "NoUser"})
    })
})