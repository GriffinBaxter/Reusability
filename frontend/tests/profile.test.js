import {test, expect, describe, jest} from "@jest/globals";
import Profile from '../src/views/Profile'
import {UserRole} from "../src/configs/User";
import Api from "../src/Api";
import {shallowMount} from "@vue/test-utils";
import Cookies from "js-cookie";

jest.mock("../src/Api");
jest.mock("js-cookie");

/**
  * Jest tests for Profile.vue.
  */

// ***************************************** isValidRole() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('isValidRole: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(Profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('isValidRole: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(Profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('isValidRole: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(Profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a User role.
 * @result a true value.
 */
test('isValidRole: Testing for User is a valid role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = true;

    expect(Profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Default Global Admin role.
 * @result a true value.
 */
test('isValidRole: Testing for if Default Global Admin is a valid role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(Profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Global Application Admin role.
 * @result a true value.
 */
test('isValidRole: Testing for if lobal Application Admin is a valid role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(Profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

// ***************************************** hasAdminRights() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('hasAdminRights: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(Profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('hasAdminRights: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(Profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('hasAdminRights: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(Profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a User role.
 * @result a false value.
 */
test('hasAdminRights: Testing for User is not a valid role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = false;

    expect(Profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Default Global Admin role.
 * @result a true value.
 */
test('hasAdminRights: Testing for if Default Global Admin is a valid role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(Profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Global Application Admin role.
 * @result a true value.
 */
test('hasAdminRights: Testing for if lobal Application Admin is a valid role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(Profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

// ***************************************** isDGAA() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('isDGAA: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(Profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('isDGAA: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(Profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('isDGAA: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(Profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a User role.
 * @result a false value.
 */
test('isDGAA: Testing for User is not a DGAA role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = false;

    expect(Profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Default Global Admin role.
 * @result a true value.
 */
test('isDGAA: Testing for if DGAA is a DGAA role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(Profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a Global Application Admin role.
 * @result a false value.
 */
test('isDGAA: Testing for if GAA is not a DGAA role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = false;

    expect(Profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

// ***************************************** isGAA() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('isGAA: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(Profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('isGAA: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(Profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('isGAA: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(Profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a User role.
 * @result a false value.
 */
test('isGAA: Testing for User is not a GAA role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = false;

    expect(Profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a Default Global Admin role.
 * @result a false value.
 */
test('isGAA: Testing for if DGAA is not a GAA role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = false;

    expect(Profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Global Application Admin role.
 * @result a true value.
 */
test('isGAA: Testing for if GAA is a GAA role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(Profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

// ***************************************** User's Cards Section Tests ************************************************

describe("Testing the retrieval of a user's cards", () => {

    test("Test that a 200 status code is returned with a user's cards when a user exists and has cards", async () => {
        const userId = 1;

        const data = {
            status: 200,
            data: {
                cards: [
                    {created: "2021-07-15T15:37:39.753837",
                    creator: {
                        bio: "Biography",
                        businessesAdministered: [null],
                        created: "2021-03-14T00:00",
                        email: "chad.taylor@example.com",
                        firstName: "Chad",
                        homeAddress: {
                            city: "Shire of Cocos Islands",
                            country: "Cocos (Keeling) Islands",
                            region: "West Island",
                            suburb: null
                        },
                        id: 1,
                        lastName: "Taylor",
                        middleName: "S",
                        nickname: "Chaddy",
                        role: "USER"
                    },
                    keywords: [],
                    section: "FORSALE",
                    title: "PS5"},
                    {created: "2021-07-15T15:37:39.753837",
                        creator: {
                            bio: "Biography",
                            businessesAdministered: [null],
                            created: "2021-03-14T00:00",
                            email: "chad.taylor@example.com",
                            firstName: "Chad",
                            homeAddress: {
                                city: "Shire of Cocos Islands",
                                country: "Cocos (Keeling) Islands",
                                region: "West Island",
                                suburb: null
                            },
                            id: 1,
                            lastName: "Taylor",
                            middleName: "S",
                            nickname: "Chaddy",
                            role: "USER"
                        },
                        keywords: [],
                        section: "FORSALE",
                        title: "PS4"}
                ]
            }
        }

        Api.getUsersCards.mockImplementation(() => Promise.resolve(data));

        const returnData = await Api.getUsersCards(userId);

        expect(returnData).toBe(data);
    })

    test("Test that a 200 status code is returned with an empty list when a user exists but has no cards", async () => {
        const userId = 1;

        const data = {
            status: 200,
            data: {
                cards:[]
            }
        }

        Api.getUsersCards.mockImplementation(() => Promise.resolve(data));

        const returnData = await Api.getUsersCards(userId);

        expect(returnData).toBe(data);
    })

    test("Test that when a 401 error is returned when retrieving cards a non-logged in user is redirected to /invalidtoken", async () => {
        const $router = {
            push: jest.fn()
        };
        const profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });

        const userId = 1;

        const data = {
            response: {
                status: 401
            }
        }

        Api.getUsersCards.mockImplementation(() => Promise.reject(data));

        await profileWrapper.vm.retrieveUsersCards(userId);
        await profileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({ path: '/invalidtoken'});
    });

    test("Test that when a 406 error is returned when retrieving cards for a user who does not exist then the current user is redirected to /noUser", async () => {
        const $router = {
            push: jest.fn()
        };
        const profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });

        const userId = 1;

        const data = {
            response: {
                status: 406
            }
        }

        Api.getUsersCards.mockImplementation(() => Promise.reject(data));

        await profileWrapper.vm.retrieveUsersCards(userId);
        await profileWrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({ path: '/noUser'});
    });

})

// ********************************************** getImageSrc() Tests **************************************************

describe("Testing the getImageSrc()", () => {

    test("Testing the getImageSrc() will return default image when file name is ''", async () => {

        const profileWrapper = await shallowMount(Profile);

        expect(await profileWrapper.vm.getImageSrc('')).toBe("test-file-stub")
    })

    test("Testing the getImageSrc() will return image src when file name is given", async () => {

        const profileWrapper = await shallowMount(Profile);

        expect(await profileWrapper.vm.getImageSrc('userImage.jpg')).toBe("undefined/userImage.jpg")
    })

})

describe("Testing the getCreatedDate method", () => {

    test("Testing that months is reduced by 1 if currentDate is less than dateJoined", async () => {
        jest.spyOn(Date, 'now').mockReturnValue(Date.parse('2021-09-22T00:00'));

        const profileWrapper = await shallowMount(Profile);

        profileWrapper.vm.getCreatedDate('2020-09-23T00:00');

        expect(profileWrapper.vm.joined).toBe("Sep 23 2020 (11 months ago)");
    });

})

describe("Testing the grantUserGAA method", () => {

    let profileWrapper;
    let $router;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });
    });

    test("Testing that when loadingAction is true, it remains true and actionErrorMessage remains empty", async () => {
        profileWrapper.vm.loadingAction = true;

        await profileWrapper.vm.grantUserGAA();

        expect(profileWrapper.vm.loadingAction).toBeTruthy();
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
    });

    test("Testing that when urlID is null, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        profileWrapper.vm.urlID = null;

        await profileWrapper.vm.grantUserGAA();

        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

    test("Testing that when a 200 status is received, the role becomes Global Application Admin and loadingAction is false", async () => {
        const data = {
            status: 200
        }
        Api.makeAdmin.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.role = UserRole.USER;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.grantUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when a status other than 200 is received, actionErrorMessage becomes 'Sorry, but something went wrong...' and loadingAction is false", async () => {
        const data = {
            status: 300
        }
        Api.makeAdmin.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.role = UserRole.USER;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.grantUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.USER);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when an error response is received with a 401 status, there is a router push to /invalidtoken", async () => {
        const data = {
            response: {
                status: 401
            }
        }
        Api.makeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.USER;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.grantUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.USER);
        expect($router.push).toHaveBeenCalledWith({"path": '/invalidtoken'});
    });

    test("Testing that when an error response is received with a 403 status, actionErrorMessage becomes 'Sorry, but you lack permissions to perform this action.'", async () => {
        const data = {
            response: {
                status: 403
            }
        }
        Api.makeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.USER;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.grantUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.USER);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but you lack permissions to perform this action.");
    });

    test("Testing that when an error response is received with a 406 status, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        const data = {
            response: {
                status: 406
            }
        }
        Api.makeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.USER;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.grantUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.USER);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

    test("Testing that when an error request is received, there is a router push to /timeout", async () => {
        const data = {
            request: {}
        }
        Api.makeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.USER;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.grantUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.USER);
        expect($router.push).toHaveBeenCalledWith({"path": '/timeout'});
    });

    test("Testing that when a different error is received, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        const data = {}
        Api.makeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.USER;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.grantUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.USER);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

})

describe("Testing the revokeUserGAA method", () => {

    let profileWrapper;
    let $router;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });
    });

    test("Testing that when loadingAction is true, it remains true and actionErrorMessage remains empty", async () => {
        profileWrapper.vm.loadingAction = true;

        await profileWrapper.vm.revokeUserGAA();

        expect(profileWrapper.vm.loadingAction).toBeTruthy();
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
    });

    test("Testing that when urlID is null, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        profileWrapper.vm.urlID = null;

        await profileWrapper.vm.revokeUserGAA();

        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

    test("Testing that when a 200 status is received, the role becomes User and loadingAction is false", async () => {
        const data = {
            status: 200
        }
        Api.revokeAdmin.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.USER);
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when a status other than 200 is received, actionErrorMessage becomes 'Sorry, but something went wrong...' and loadingAction is false", async () => {
        const data = {
            status: 300
        }
        Api.revokeAdmin.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when an error response is received with a 401 status, there is a router push to /invalidtoken", async () => {
        const data = {
            response: {
                status: 401
            }
        }
        Api.revokeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect($router.push).toHaveBeenCalledWith({"path": '/invalidtoken'});
    });

    test("Testing that when an error response is received with a 403 status, actionErrorMessage becomes 'Sorry, but you lack permissions to perform this action.'", async () => {
        const data = {
            response: {
                status: 403
            }
        }
        Api.revokeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but you lack permissions to perform this action.");
    });

    test("Testing that when an error response is received with a 406 status, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        const data = {
            response: {
                status: 406
            }
        }
        Api.revokeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

    test("Testing that when an error response is received with a 409 status, actionErrorMessage becomes 'Sorry, but as DGAA you cannot remove your admin status.'", async () => {
        const data = {
            response: {
                status: 409
            }
        }
        Api.revokeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but as DGAA you cannot remove your admin status.");
    });

    test("Testing that when an error request is received, there is a router push to /timeout", async () => {
        const data = {
            request: {}
        }
        Api.revokeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect($router.push).toHaveBeenCalledWith({"path": '/timeout'});
    });

    test("Testing that when a different error is received, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        const data = {}
        Api.revokeAdmin.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.role = UserRole.GLOBALAPPLICATIONADMIN;
        profileWrapper.vm.urlID = 1;

        await profileWrapper.vm.revokeUserGAA();
        await Promise.resolve();

        expect(profileWrapper.vm.role).toBe(UserRole.GLOBALAPPLICATIONADMIN);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

})

describe("Testing the retrieveUser method", () => {

    let profileWrapper;
    let $router;
    let populatePageSpy;
    let processUserInfoErrorSpy;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        populatePageSpy = jest.spyOn(Profile.methods, 'populatePage');
        processUserInfoErrorSpy = jest.spyOn(Profile.methods, 'processUserInfoError');

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });

        await Promise.resolve();
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test("Testing that when a non-error response is received, populatePage is called", async () => {
        const data = {
            data: {

            }
        }
        Api.getUser.mockImplementation(() => Promise.resolve(data));

        await profileWrapper.vm.retrieveUser(1);
        await Promise.resolve();

        expect(populatePageSpy).toHaveBeenCalled();
    });

    test("Testing that when an error is received, processUserInfoError is called", async () => {
        const data = {
            data: {

            }
        }
        Api.getUser.mockImplementation(() => Promise.reject(data));

        await profileWrapper.vm.retrieveUser(1);
        await Promise.resolve();

        expect(processUserInfoErrorSpy).toHaveBeenCalled();
    });

})

describe("Testing the formatAge method", () => {

    test("Testing that the age output is formatted correctly", async () => {
        const profileWrapper = await shallowMount(Profile);

        let returnedString = profileWrapper.vm.formatAge("2000-10-09");

        expect(returnedString).toBe("Oct 09 2000");
    });

})

describe("Testing the populatePage method", () => {

    let profileWrapper;

    beforeEach(async () => {
        profileWrapper = await shallowMount(Profile);
    });

    test("Testing that all fields are set correctly with the data received", async () => {
        const userPayload = {
            "id": 100,
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
            "role": null,
            "businessesAdministered": []
        }

        await profileWrapper.vm.populatePage(userPayload);

        expect(profileWrapper.vm.userId).toBe(100);
        expect(profileWrapper.vm.dateOfBirth).toBe("Apr 27 1999");
        expect(profileWrapper.vm.phoneNumber).toBe("+64 3 555 0129");
        expect(profileWrapper.vm.streetNumber).toBe("3/24");
        expect(profileWrapper.vm.streetName).toBe("Ilam Road");
        expect(profileWrapper.vm.suburb).toBe("Upper Riccarton");
        expect(profileWrapper.vm.city).toBe("Christchurch");
        expect(profileWrapper.vm.region).toBe("Canterbury");
        expect(profileWrapper.vm.postcode).toBe("90210");
        expect(profileWrapper.vm.country).toBe("New Zealand");
        expect(profileWrapper.vm.address).toStrictEqual([{"line": "3/24 Ilam Road"}, {"line": "Upper Riccarton"}, {"line": "Christchurch, 90210"}, {"line": "Canterbury, New Zealand"}]);
        expect(profileWrapper.vm.firstName).toBe("John");
        expect(profileWrapper.vm.middleName).toBe("Hector");
        expect(profileWrapper.vm.lastName).toBe("Smith");
        expect(profileWrapper.vm.nickname).toBe("Jonny");
        expect(profileWrapper.vm.bio).toBe("Likes long walks on the beach");
        expect(profileWrapper.vm.email).toBe("johnsmith99@gmail.com");
        expect(profileWrapper.vm.user).toStrictEqual({"data": {
            "firstName": "John",
                "id": 100,
                "images": []
        }});
    });

    test("Testing that all fields are set correctly with the data received when the user role is returned", async () => {
        const userPayload = {
            "id": 100,
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
        }

        await profileWrapper.vm.populatePage(userPayload);

        expect(profileWrapper.vm.userId).toBe(100);
        expect(profileWrapper.vm.dateOfBirth).toBe("Apr 27 1999");
        expect(profileWrapper.vm.phoneNumber).toBe("+64 3 555 0129");
        expect(profileWrapper.vm.streetNumber).toBe("3/24");
        expect(profileWrapper.vm.streetName).toBe("Ilam Road");
        expect(profileWrapper.vm.suburb).toBe("Upper Riccarton");
        expect(profileWrapper.vm.city).toBe("Christchurch");
        expect(profileWrapper.vm.region).toBe("Canterbury");
        expect(profileWrapper.vm.postcode).toBe("90210");
        expect(profileWrapper.vm.country).toBe("New Zealand");
        expect(profileWrapper.vm.address).toStrictEqual([{"line": "3/24 Ilam Road"}, {"line": "Upper Riccarton"}, {"line": "Christchurch, 90210"}, {"line": "Canterbury, New Zealand"}]);
        expect(profileWrapper.vm.firstName).toBe("John");
        expect(profileWrapper.vm.middleName).toBe("Hector");
        expect(profileWrapper.vm.lastName).toBe("Smith");
        expect(profileWrapper.vm.nickname).toBe("Jonny");
        expect(profileWrapper.vm.bio).toBe("Likes long walks on the beach");
        expect(profileWrapper.vm.email).toBe("johnsmith99@gmail.com");
        expect(profileWrapper.vm.role).toBe("user");
        expect(profileWrapper.vm.user).toStrictEqual({"data": {
                "firstName": "John",
                "id": 100,
                "images": []
            }});
    });

    test("Testing that all fields are set correctly with the data received when user images are returned", async () => {
        const userPayload = {
            "id": 100,
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
            "role": null,
            "businessesAdministered": [],
            "images": ["test"]
        }

        await profileWrapper.vm.populatePage(userPayload);

        expect(profileWrapper.vm.userId).toBe(100);
        expect(profileWrapper.vm.dateOfBirth).toBe("Apr 27 1999");
        expect(profileWrapper.vm.phoneNumber).toBe("+64 3 555 0129");
        expect(profileWrapper.vm.streetNumber).toBe("3/24");
        expect(profileWrapper.vm.streetName).toBe("Ilam Road");
        expect(profileWrapper.vm.suburb).toBe("Upper Riccarton");
        expect(profileWrapper.vm.city).toBe("Christchurch");
        expect(profileWrapper.vm.region).toBe("Canterbury");
        expect(profileWrapper.vm.postcode).toBe("90210");
        expect(profileWrapper.vm.country).toBe("New Zealand");
        expect(profileWrapper.vm.address).toStrictEqual([{"line": "3/24 Ilam Road"}, {"line": "Upper Riccarton"}, {"line": "Christchurch, 90210"}, {"line": "Canterbury, New Zealand"}]);
        expect(profileWrapper.vm.firstName).toBe("John");
        expect(profileWrapper.vm.middleName).toBe("Hector");
        expect(profileWrapper.vm.lastName).toBe("Smith");
        expect(profileWrapper.vm.nickname).toBe("Jonny");
        expect(profileWrapper.vm.bio).toBe("Likes long walks on the beach");
        expect(profileWrapper.vm.email).toBe("johnsmith99@gmail.com");
        expect(profileWrapper.vm.images).toStrictEqual(["test"]);
        expect(profileWrapper.vm.user).toStrictEqual({"data": {
                "firstName": "John",
                "id": 100,
                "images": ["test"]
            }});
    });

    test("Testing that when business isn't null and the business ID matches actingBusinessId then isBusinessAdministrator is true and businessesAdministered contains the business", async () => {
        const userPayload = {
            "id": 100,
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
            "role": null,
            "businessesAdministered": [
                {
                    "id": 100,
                    "administrators": [
                        "string"
                    ],
                    "primaryAdministratorId": 20,
                    "name": "Lumbridge General Store",
                    "description": "A one-stop shop for all your adventuring needs",
                    "address": {
                        "streetNumber": "3/24",
                        "streetName": "Ilam Road",
                        "suburb": "Upper Riccarton",
                        "city": "Christchurch",
                        "region": "Canterbury",
                        "country": "New Zealand",
                        "postcode": "90210"
                    },
                    "businessType": "Accommodation and Food Services",
                    "created": "2020-07-14T14:52:00Z"
                }
            ]
        }

        Cookies.get = jest.fn().mockImplementation(() => "100");

        await profileWrapper.vm.populatePage(userPayload);

        expect(profileWrapper.vm.isBusinessAdministrator).toBeTruthy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([
            {
                "id": 100,
                "name": "Lumbridge General Store"
            }
        ]);
    });

    test("Testing that when business isn't null and the business ID doesn't match actingBusinessId then isBusinessAdministrator is false and businessesAdministered contains the business", async () => {
        const userPayload = {
            "id": 100,
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
            "role": null,
            "businessesAdministered": [
                {
                    "id": 100,
                    "administrators": [
                        "string"
                    ],
                    "primaryAdministratorId": 20,
                    "name": "Lumbridge General Store",
                    "description": "A one-stop shop for all your adventuring needs",
                    "address": {
                        "streetNumber": "3/24",
                        "streetName": "Ilam Road",
                        "suburb": "Upper Riccarton",
                        "city": "Christchurch",
                        "region": "Canterbury",
                        "country": "New Zealand",
                        "postcode": "90210"
                    },
                    "businessType": "Accommodation and Food Services",
                    "created": "2020-07-14T14:52:00Z"
                }
            ]
        }

        Cookies.get = jest.fn().mockImplementation(() => "1");

        await profileWrapper.vm.populatePage(userPayload);

        expect(profileWrapper.vm.isBusinessAdministrator).toBeFalsy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([
            {
                "id": 100,
                "name": "Lumbridge General Store"
            }
        ]);
    });

})

describe("Testing the redirectToBusiness method", () => {

    test("Testing that there is a router push to Business Profile with the ID", async () => {
        const $router = {
            push: jest.fn()
        };

        const profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });

        const id = 1;

        await profileWrapper.vm.redirectToBusiness(id);

        expect($router.push).toHaveBeenCalledWith({"name": 'BusinessProfile', "params": {id}});
    });

})

describe("Testing the getLoginRole method", () => {

    test("Testing that the loginRole is set correctly to the received role", async () => {
        const profileWrapper = await shallowMount(Profile);

        const data = {
            data: {
                role: "Test"
            }
        }
        Api.getUser.mockImplementation(() => Promise.resolve(data));

        await profileWrapper.vm.getLoginRole(1);
        await Promise.resolve();

        expect(profileWrapper.vm.loginRole).toBe("Test");
    });

})

describe("Testing the logout method", () => {

    test("Testing that there is a router push to Login", async () => {
        const $router = {
            push: jest.fn()
        };

        const profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });

        Api.signOut.mockImplementation(() => Promise.resolve());

        await profileWrapper.vm.logout();

        expect($router.push).toHaveBeenCalledWith({"name": "Login"});
    });

})

describe("Testing the activeAsAdministrator method", () => {

    let profileWrapper;
    let $router;
    let processUpdateAdministratorErrorSpy;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        processUpdateAdministratorErrorSpy = jest.spyOn(Profile.methods, "processUpdateAdministratorError");

        Api.getUser.mockImplementation(() => Promise.resolve({data: {status: 200, role: "User"}, response: {status: 200}, status: 200}));
        Api.getUsersCards.mockImplementation(() => Promise.resolve({data: [], status: 200, response: {status: 200}}));
        Cookies.get = jest.fn().mockImplementation(() => "1");

        const data = {
            data: {
                name: "Business",
                id: 1
            }
        }
        Api.getBusiness.mockImplementation(() => Promise.resolve(data));

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });

        await Promise.resolve();
    });

    test("Testing that when loadingAction is true, it remains true and actionErrorMessage remains empty", async () => {
        profileWrapper.vm.loadingAction = true;

        await profileWrapper.vm.activeAsAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.loadingAction).toBeTruthy();
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
    });

    test("Testing that when urlID is null, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        profileWrapper.vm.urlID = null;

        await profileWrapper.vm.activeAsAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

    test("Testing that when a 200 status is received, businessesAdministered contains the business, actionErrorMessage is empty, isBusinessAdministrator is true, and loadingAction is false", async () => {
        const data = {
            status: 200
        }
        Api.makeAdministrator.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.isBusinessAdministrator = false;
        profileWrapper.vm.businessesAdministered = [];
        profileWrapper.vm.urlID = 1;
        profileWrapper.vm.otherUser = true;

        await profileWrapper.vm.activeAsAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.isBusinessAdministrator).toBeTruthy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([{"name": "Business", "id": 1}]);
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when a status other than 200 is received, actionErrorMessage becomes 'Sorry, but something went wrong...' and loadingAction is false", async () => {
        const data = {
            status: 300
        }
        Api.makeAdministrator.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.isBusinessAdministrator = false;
        profileWrapper.vm.businessesAdministered = [];
        profileWrapper.vm.urlID = 1;
        profileWrapper.vm.otherUser = true;

        await profileWrapper.vm.activeAsAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.isBusinessAdministrator).toBeFalsy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([]);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when an error is received, processUpdateAdministratorError is called", async () => {
        const data = {}
        Api.makeAdministrator.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.isBusinessAdministrator = false;
        profileWrapper.vm.businessesAdministered = [];
        profileWrapper.vm.urlID = 1;
        profileWrapper.vm.otherUser = true;

        await profileWrapper.vm.activeAsAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.isBusinessAdministrator).toBeFalsy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([]);
        expect(processUpdateAdministratorErrorSpy).toHaveBeenCalled();
    });

})

describe("Testing the removeActiveAdministrator method", () => {

    let profileWrapper;
    let $router;
    let processUpdateAdministratorErrorSpy;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        processUpdateAdministratorErrorSpy = jest.spyOn(Profile.methods, "processUpdateAdministratorError");

        Api.getUser.mockImplementation(() => Promise.resolve({data: {status: 200}}));
        Api.getUsersCards.mockImplementation(() => Promise.resolve({data: []}));

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });

        Cookies.get = jest.fn().mockImplementation(() => "1");

        const data = {
            data: {
                name: "Business",
                id: 1
            }
        }
        Api.getBusiness.mockImplementation(() => Promise.resolve(data));
    });

    test("Testing that when loadingAction is true, it remains true and actionErrorMessage remains empty", async () => {
        profileWrapper.vm.loadingAction = true;

        await profileWrapper.vm.removeActiveAdministrator();

        expect(profileWrapper.vm.loadingAction).toBeTruthy();
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
    });

    test("Testing that when urlID is null, actionErrorMessage becomes 'Sorry, but something went wrong...'", async () => {
        profileWrapper.vm.urlID = null;

        await profileWrapper.vm.removeActiveAdministrator();

        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

    test("Testing that when a 200 status is received, businessesAdministered does not contain the business, actionErrorMessage is empty, isBusinessAdministrator is false, and loadingAction is false", async () => {
        const data = {
            status: 200,

        }
        Api.removeAdministrator.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.isBusinessAdministrator = true;
        profileWrapper.vm.businessesAdministered = [{"name": "Business", "id": 1}];
        profileWrapper.vm.urlID = 1;
        profileWrapper.vm.otherUser = true;
        profileWrapper.vm.actingBusinessId = "1";

        await profileWrapper.vm.removeActiveAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.isBusinessAdministrator).toBeFalsy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([]);
        expect(profileWrapper.vm.actionErrorMessage).toBe("");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when a status other than 200 is received, actionErrorMessage becomes 'Sorry, but something went wrong...', loadingAction is false, isBusinessAdministrator is still true, and businessesAdministered still contains the business", async () => {
        const data = {
            status: 300
        }
        Api.removeAdministrator.mockImplementation(() => Promise.resolve(data));
        profileWrapper.vm.isBusinessAdministrator = true;
        profileWrapper.vm.businessesAdministered = [{"name": "Business", "id": 1}];
        profileWrapper.vm.urlID = 1;
        profileWrapper.vm.otherUser = true;

        await profileWrapper.vm.removeActiveAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.isBusinessAdministrator).toBeTruthy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([{"name": "Business", "id": 1}]);
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
        expect(profileWrapper.vm.loadingAction).toBeFalsy();
    });

    test("Testing that when an error is received, processUpdateAdministratorError is called", async () => {
        const data = {}
        Api.removeAdministrator.mockImplementation(() => Promise.reject(data));
        profileWrapper.vm.isBusinessAdministrator = true;
        profileWrapper.vm.businessesAdministered = [{"name": "Business", "id": 1}];
        profileWrapper.vm.urlID = 1;
        profileWrapper.vm.otherUser = true;

        await profileWrapper.vm.removeActiveAdministrator();
        await Promise.resolve();

        expect(profileWrapper.vm.isBusinessAdministrator).toBeTruthy();
        expect(profileWrapper.vm.businessesAdministered).toStrictEqual([{"name": "Business", "id": 1}]);
        expect(processUpdateAdministratorErrorSpy).toHaveBeenCalled();
    });

})

describe("Testing the processUpdateAdministratorError method", () => {

    let profileWrapper;
    let $router;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });
    });

    test("Testing that when the error has a response status 401, there is a router push to /invalidtoken and false is returned", async () => {
        const data = {
            response: {
                status: 401
            }
        }

        let result = profileWrapper.vm.processUpdateAdministratorError(data);

        expect(result).toBeFalsy();
        expect($router.push).toHaveBeenCalledWith({"path": '/invalidtoken'});
    });

    test("Testing that when the error has a response status 403, actionErrorMessage becomes 'Sorry, but you lack permissions to perform this action.' and false is returned", async () => {
        const data = {
            response: {
                status: 403
            }
        }

        let result = profileWrapper.vm.processUpdateAdministratorError(data);

        expect(result).toBeFalsy();
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but you lack permissions to perform this action.");
    });

    test("Testing that when the error has a response status 406, actionErrorMessage becomes 'Sorry, but something went wrong...' and false is returned", async () => {
        const data = {
            response: {
                status: 406
            }
        }

        let result = profileWrapper.vm.processUpdateAdministratorError(data);

        expect(result).toBeFalsy();
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

    test("Testing that when the error has a request, there is a router push to /timeout and false is returned", async () => {
        const data = {
            request: {}
        }

        let result = profileWrapper.vm.processUpdateAdministratorError(data);

        expect(result).toBeFalsy();
        expect($router.push).toHaveBeenCalledWith({"path": '/timeout'});
    });

    test("Testing that when the error doesn't have a request or response, actionErrorMessage becomes 'Sorry, but something went wrong...' and false is returned", async () => {
        const data = {}

        let result = profileWrapper.vm.processUpdateAdministratorError(data);

        expect(result).toBeFalsy();
        expect(profileWrapper.vm.actionErrorMessage).toBe("Sorry, but something went wrong...");
    });

})

describe("Testing the retrieveUsersCards method", () => {

    test("Testing that when a response is received, usersCards contains the sorted data", async () => {
        const profileWrapper = await shallowMount(Profile);

        const data = {
            status: 200,
            data: [
                    {created: "2021-07-15T15:37:39.753837",
                        creator: {
                            bio: "Biography",
                            businessesAdministered: [null],
                            created: "2021-03-14T00:00",
                            email: "chad.taylor@example.com",
                            firstName: "Chad",
                            homeAddress: {
                                city: "Shire of Cocos Islands",
                                country: "Cocos (Keeling) Islands",
                                region: "West Island",
                                suburb: null
                            },
                            id: 1,
                            lastName: "Taylor",
                            middleName: "S",
                            nickname: "Chaddy",
                            role: "USER"
                        },
                        keywords: [],
                        section: "FORSALE",
                        title: "PS4"},
                    {created: "2021-07-15T15:37:39.753837",
                        creator: {
                            bio: "Biography",
                            businessesAdministered: [null],
                            created: "2021-03-14T00:00",
                            email: "chad.taylor@example.com",
                            firstName: "Chad",
                            homeAddress: {
                                city: "Shire of Cocos Islands",
                                country: "Cocos (Keeling) Islands",
                                region: "West Island",
                                suburb: null
                            },
                            id: 1,
                            lastName: "Taylor",
                            middleName: "S",
                            nickname: "Chaddy",
                            role: "USER"
                        },
                        keywords: [],
                        section: "EXCHANGE",
                        title: "PS5"}
                ]
        }

        Api.getUsersCards.mockImplementation(() => Promise.resolve(data));

        const expected = [
                {created: "2021-07-15T15:37:39.753837",
                    creator: {
                        bio: "Biography",
                        businessesAdministered: [null],
                        created: "2021-03-14T00:00",
                        email: "chad.taylor@example.com",
                        firstName: "Chad",
                        homeAddress: {
                            city: "Shire of Cocos Islands",
                            country: "Cocos (Keeling) Islands",
                            region: "West Island",
                            suburb: null
                        },
                        id: 1,
                        lastName: "Taylor",
                        middleName: "S",
                        nickname: "Chaddy",
                        role: "USER"
                    },
                    keywords: [],
                    section: "EXCHANGE",
                    title: "PS5"},
                {created: "2021-07-15T15:37:39.753837",
                        creator: {
                            bio: "Biography",
                            businessesAdministered: [null],
                            created: "2021-03-14T00:00",
                            email: "chad.taylor@example.com",
                            firstName: "Chad",
                            homeAddress: {
                                city: "Shire of Cocos Islands",
                                country: "Cocos (Keeling) Islands",
                                region: "West Island",
                                suburb: null
                            },
                            id: 1,
                            lastName: "Taylor",
                            middleName: "S",
                            nickname: "Chaddy",
                            role: "USER"
                        },
                        keywords: [],
                        section: "FORSALE",
                        title: "PS4"}
                ]

        await profileWrapper.vm.retrieveUsersCards(1);
        await Promise.resolve();

        expect(profileWrapper.vm.usersCards).toStrictEqual(expected);
    });

})

describe("Testing the compareCards method", () => {

    let profileWrapper;

    beforeEach(async () => {
        profileWrapper = await shallowMount(Profile);
    });

    test("Testing that when the section of card 1 is less than the section of card 2, -1 is returned", async () => {
        const card1 = {
            section: "For Sale"
        }
        const card2 = {
            section: "Wanted"
        }

        expect(profileWrapper.vm.compareCards(card1, card2)).toBe(-1);
    });

    test("Testing that when the section of card 1 is greater than the section of card 2, 1 is returned", async () => {
        const card1 = {
            section: "For Sale"
        }
        const card2 = {
            section: "Exchange"
        }

        expect(profileWrapper.vm.compareCards(card1, card2)).toBe(1);
    });

    test("Testing that when the section of card 1 is equal to the section of card 2, 0 is returned", async () => {
        const card1 = {
            section: "For Sale"
        }
        const card2 = {
            section: "For Sale"
        }

        expect(profileWrapper.vm.compareCards(card1, card2)).toBe(0);
    });

})

describe("Testing the processUserInfoError method", () => {

    let profileWrapper;
    let $router;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });
    });

    test("Testing that when an error request and no error response is received, there is a router push to /timeout", async () => {
        const data = {
            request: {}
        }

        await profileWrapper.vm.processUserInfoError(data);

        expect($router.push).toHaveBeenCalledWith({"path": '/timeout'});
    });

    test("Testing that when an error response status of 406 is received, there is a router push to /noUser", async () => {
        const data = {
            response: {
                status: 406
            }
        }

        await profileWrapper.vm.processUserInfoError(data);

        expect($router.push).toHaveBeenCalledWith({"path": '/noUser'});
    });

    test("Testing that when an error response status of 401 is received, there is a router push to /invalidtoken", async () => {
        const data = {
            response: {
                status: 401
            }
        }

        await profileWrapper.vm.processUserInfoError(data);

        expect($router.push).toHaveBeenCalledWith({"path": '/invalidtoken'});
    });

    test("Testing that when a different error response status is received, there is a router push to /noUser", async () => {
        const data = {
            response: {
                status: 500
            }
        }

        await profileWrapper.vm.processUserInfoError(data);

        expect($router.push).toHaveBeenCalledWith({"path": '/noUser'});
    });

})

describe("Testing the goToEdit method", () => {

    let profileWrapper;
    let $router;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };

        profileWrapper = await shallowMount(Profile, {
            mocks: {
                $router
            }
        });
    });

    test("Testing that when urlID matches 'profile', there is a router push to EditProfile with currentID", async () => {
        profileWrapper.vm.urlID = "profile";
        profileWrapper.vm.currentID = 1;

        await profileWrapper.vm.goToEdit();

        expect($router.push).toHaveBeenCalledWith({"name": 'EditProfile', "params": {"id": 1}});
    });

    test("Testing that when urlID does not match 'profile', there is a router push to EditProfile with urlID", async () => {
        profileWrapper.vm.urlID = 2;
        profileWrapper.vm.currentID = 1;

        await profileWrapper.vm.goToEdit();

        expect($router.push).toHaveBeenCalledWith({"name": 'EditProfile', "params": {"id": 2}});
    });

})