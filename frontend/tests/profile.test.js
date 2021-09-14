import {test, expect, describe, jest} from "@jest/globals";
import Profile from '../src/views/Profile'
import {UserRole} from "../src/configs/User";
import Api from "../src/Api";
import {shallowMount} from "@vue/test-utils";

jest.mock("../src/Api");

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