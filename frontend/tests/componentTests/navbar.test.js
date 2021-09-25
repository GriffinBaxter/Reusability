import {describe, jest} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import Navbar from "../../src/components/Navbar";
import Cookies from "js-cookie";
import {UserRole} from "../../src/configs/User";
import Api from "../../src/Api";

jest.mock("../../src/Api");
jest.mock("js-cookie");

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
        const response = {
            data: []
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response))
        Api.getNotifications.mockImplementation( () => Promise.resolve(response));
        Api.getBusinessNotifications.mockImplementation( () => Promise.resolve(response));
        Api.signOut.mockImplementation( () => Promise.resolve());

        wrapper = shallowMount(Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });

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
        const response = {
            data: []
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response))
        Api.getNotifications.mockImplementation( () => Promise.resolve(response));
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

    test("Test the toggleMessages method sets variables correctly.", () => {
        wrapper.vm.$data.showMessages = false;

        wrapper.vm.toggleMessages();

        expect(wrapper.vm.$data.showMessages).toBeTruthy();
        expect(wrapper.vm.$data.openNotificationBox).toBeFalsy();
        expect(wrapper.vm.$data.showInteractMenu).toBeFalsy();
    })

    test("Test the closeInteractMenu method sets variables correctly.", () => {
        wrapper.vm.$data.showMessages = true;
        wrapper.vm.$data.openNotificationBox = true;
        wrapper.vm.$data.showInteractMenu = true;

        wrapper.vm.closeInteractMenu();

        expect(wrapper.vm.$data.showMessages).toBeFalsy();
        expect(wrapper.vm.$data.openNotificationBox).toBeFalsy();
        expect(wrapper.vm.$data.showInteractMenu).toBeFalsy();
    })

    test("Test the closeNavbar method sets variables correctly.", () => {
        wrapper.vm.$data.showBusinessDropdown = true;
        wrapper.vm.$data.showMessages = true;
        wrapper.vm.$data.showNavabar = true;

        wrapper.vm.closeNavbar();

        expect(wrapper.vm.$data.showBusinessDropdown).toBeFalsy();
        expect(wrapper.vm.$data.showMessages).toBeFalsy();
        expect(wrapper.vm.$data.showNavbar).toBeFalsy();
    })

    test("Test the toggleBusinessDropdown method sets variable correctly.", () => {
        wrapper.vm.$data.showBusinessDropdown = true;

        wrapper.vm.toggleBusinessDropdown();

        expect(wrapper.vm.$data.showBusinessDropdown).toBeFalsy();

    })

    test("Test the omitName function returns the original name if its length is less than the max length.", () => {
        const name = "Zachary";
        const max = 10;

        expect(wrapper.vm.omitName(name, max)).toEqual(name);
    })

    test("Test the omitName function returns the omitted name if its length is greater than the max length.", () => {
        const name = "Zachary";
        const max = 5;

        expect(wrapper.vm.omitName(name, max)).toEqual("Zacha...");
    })

    test("Tests refreshDropdown method when nickname is null", () => {
        wrapper.vm.$data.currentUser = {
            "id": 21,
            "firstName": "John",
            "lastName": "Doe",
            "middleName": "S",
            "nickname": null,
            "bio": "Biography",
            "email": "email@email.com",
            "created": "2021-02-02T00:00",
            "role": "DEFAULTGLOBALAPPLICATIONADMIN",
            "businessesAdministered": [
                null
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
        }

        wrapper.vm.refreshDropdown();

        expect(wrapper.vm.$data.interactAs).toEqual([{id: 21, name: "John"}]);
        expect(wrapper.vm.$data.interactAsOmit).toEqual([{id: 21, name: "John"}]);
        expect(wrapper.emitted().getLinkBusinessAccount).toBeTruthy();
    })

    test("Tests refreshDropdown method when nickname is not null and user administers a business", () => {
        wrapper.vm.$data.currentUser = {
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
                {
                    "id": 4,
                    "administrators": [
                        null
                    ],
                    "primaryAdministratorId": 21,
                    "name": "Big Business",
                    "description": null,
                    "address": {
                        "streetNumber": null,
                        "streetName": null,
                        "suburb": null,
                        "city": null,
                        "region": null,
                        "country": "New Zealand",
                        "postcode": null
                    },
                    "businessType": "CHARITABLE_ORGANISATION",
                    "created": "2021-09-08T21:22:53.130650"
                }
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
        }

        // set the businesses for the user (mocking).
        wrapper.vm.$data.businesses = wrapper.vm.$data.currentUser.businessesAdministered.filter(
            (business) => business !== null
        );
        wrapper.vm.refreshDropdown();

        expect(wrapper.vm.$data.interactAs).toEqual([{id: 21, name: "Johnny"}, {id: 4, name: "Big Business"}]);
        expect(wrapper.vm.$data.interactAsOmit).toEqual([{id: 21, name: "Johnny"}, {id: 4, name: "Big Busine..."}]);
        expect(wrapper.emitted().getLinkBusinessAccount).toBeTruthy();
    })


})

describe('Tests for notifications', () => {
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

        Cookies.get.mockReturnValueOnce(21);

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

        const response = {
            data: []
        }

        Api.getNotifications.mockImplementation(() => Promise.resolve(response));
        Api.getBusinessNotifications.mockImplementation(() => Promise.resolve(response));

    });

    test("Test that newMessage is set to true when receiver", async () => {
        let response = {
            data: [
                {
                    receiverId: 21,
                    readByReceiver: false
                }
            ]
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response))

        wrapper = shallowMount(Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });
        await wrapper.vm.$nextTick()

        expect(wrapper.vm.$data.unreadMessage).toBeTruthy()
    })

    test("Test that newMessage is set to false when receiver", async () => {
        let response = {
            data: [
                {
                    receiverId: 21,
                    readByReceiver: true
                }
            ]
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response))

        wrapper = shallowMount(Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.$data.unreadMessage).toBeFalsy();
    })

    test("Test that newMessage is set to true when instigator", async () => {
        let response = {
            data: [
                {
                    instigatorId: 21,
                    readByInstigator: false
                }
            ]
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response))

        wrapper = shallowMount(Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });
        await wrapper.vm.$nextTick()

        expect(wrapper.vm.$data.unreadMessage).toBeTruthy()
    })

    test("Test that newMessage is set to false when instigator", async () => {
        let response = {
            data: [
                {
                    instigatorId: 21,
                    readByInstigator: true
                }
            ]
        }
        Api.getConversations.mockImplementation(() => Promise.resolve(response))

        wrapper = shallowMount(Navbar, {
            mocks: {
                $router,
                $route
            },
            stubs: ['router-link', 'router-view']
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.$data.unreadMessage).toBeFalsy();
    })
})