import {describe, jest} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import Navbar from "../../src/components/Navbar";
import Cookies from "js-cookie";
import {UserRole} from "../../src/configs/User";
import Api from "../../src/Api";

jest.mock("../../src/Api");

describe('Tests for admin rights methods.', () => {
    let wrapper;
    let $route;
    let $router;

    beforeEach(() => {
        $route = {
            path: '/profile',
            name: 'Profile',
        }

        $router = {
            push: jest.fn()
        }

        wrapper = shallowMount(Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });

        Cookies.get = jest.fn().mockImplementationOnce(() => 1).mockImplementationOnce(() => 2); // mock the cookies
        Api.getUser.mockImplementation(() => Promise.resolve({
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
                    null
                ],
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
        Api.getNotifications.mockImplementation( () => Promise.resolve([]));
        const response = {
            data: []
        }
        Api.getBusinessNotifications.mockImplementation( () => Promise.resolve(response));
        Api.signOut.mockImplementation( () => Promise.resolve());

        Promise.resolve();
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test("Test the hasAdminRights method returns true when user is a DGAA", () => {
        expect(wrapper.vm.hasAdminRights(UserRole.DEFAULTGLOBALAPPLICATIONADMIN)).toBeTruthy();
    })

    test("Test the hasAdminRights method returns true when user is a GAA", () => {
        expect(wrapper.vm.hasAdminRights(UserRole.GLOBALAPPLICATIONADMIN)).toBeTruthy();
    })

    test("Test the hasAdminRights method returns false when user is not a DGAA or GAA", () => {
        expect(wrapper.vm.hasAdminRights(UserRole.USER)).toBeFalsy();
    })

    test("Test the isDGAA method returns true when user is a DGAA.", () => {
        expect(wrapper.vm.hasAdminRights(UserRole.DEFAULTGLOBALAPPLICATIONADMIN)).toBeTruthy();
    })

    test("Test the isDGAA method returns false when user is not a DGAA.", () => {
        expect(wrapper.vm.hasAdminRights(UserRole.USER)).toBeFalsy();
    })

    test("Test the isGAA method returns true when user is a GAA.", () => {
        expect(wrapper.vm.hasAdminRights(UserRole.GLOBALAPPLICATIONADMIN)).toBeTruthy();
    })

    test("Test the isGAA method returns false when user is not a GAA.", () => {
        expect(wrapper.vm.hasAdminRights(UserRole.USER)).toBeFalsy();
    })

})

describe('Tests for miscellaneous Navbar methods', () => {
    let wrapper;
    let $route;
    let $router;

    beforeEach(() => {
        $route = {
            path: '/profile',
            name: 'Profile',
        }

        $router = {
            push: jest.fn()
        }

        wrapper = shallowMount(Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });

        Cookies.get = jest.fn().mockImplementationOnce(() => 1).mockImplementationOnce(() => 2); // mock the cookies
        Api.getUser.mockImplementation(() => Promise.resolve({
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
                    null
                ],
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
        Api.getNotifications.mockImplementation( () => Promise.resolve([]));
        const response = {
            data: []
        }
        Api.getBusinessNotifications.mockImplementation( () => Promise.resolve(response));
        Api.signOut.mockImplementation( () => Promise.resolve());

        Promise.resolve();
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test("Test the switchNotificationBox method sets variables correctly.", () => {
        wrapper.vm.$data.openNotificationBox = false;

        wrapper.vm.switchNotificationBox();

        expect(wrapper.vm.$data.newNotification).toBeFalsy();
        expect(wrapper.vm.$data.openNotificationBox).toBeTruthy();
        expect(wrapper.vm.$data.showInteractMenu).toBeFalsy();
        expect(wrapper.vm.$data.showMessages).toBeFalsy();
    })


})