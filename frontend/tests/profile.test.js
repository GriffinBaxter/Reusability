import {test, expect} from "@jest/globals";
import profile from '../src/views/Profile'
import {UserRole} from "../src/configs/User";


/**
  * Jest tests for profile.vue .
  */

// ***************************************** isValidRole() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('isValidRole: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('isValidRole: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('isValidRole: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a User role.
 * @result a true value.
 */
test('isValidRole: Testing for User is a valid role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = true;

    expect(profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Default Global Admin role.
 * @result a true value.
 */
test('isValidRole: Testing for if Default Global Admin is a valid role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Global Application Admin role.
 * @result a true value.
 */
test('isValidRole: Testing for if lobal Application Admin is a valid role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(profile.methods.isValidRole(testInput)).toBe(expectedOutput);
})

// ***************************************** hasAdminRights() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('hasAdminRights: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('hasAdminRights: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('hasAdminRights: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a User role.
 * @result a false value.
 */
test('hasAdminRights: Testing for User is not a valid role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = false;

    expect(profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Default Global Admin role.
 * @result a true value.
 */
test('hasAdminRights: Testing for if Default Global Admin is a valid role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Global Application Admin role.
 * @result a true value.
 */
test('hasAdminRights: Testing for if lobal Application Admin is a valid role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(profile.methods.hasAdminRights(testInput)).toBe(expectedOutput);
})

// ***************************************** isDGAA() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('isDGAA: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('isDGAA: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('isDGAA: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a User role.
 * @result a false value.
 */
test('isDGAA: Testing for User is not a DGAA role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = false;

    expect(profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Default Global Admin role.
 * @result a true value.
 */
test('isDGAA: Testing for if DGAA is a DGAA role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a Global Application Admin role.
 * @result a false value.
 */
test('isDGAA: Testing for if GAA is not a DGAA role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = false;

    expect(profile.methods.isDGAA(testInput)).toBe(expectedOutput);
})

// ***************************************** isGAA() Tests ***************************************************

/**
 * Test for ensuring you get false for a null role.
 * @result a false value.
 */
test('isGAA: Testing for that null role gives false', () => {
    const testInput = null;
    const expectedOutput = false;

    expect(profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for an arbitary string.
 * @result a false value.
 */
test('isGAA: Testing for that an arbitary string returns false.', () => {
    const testInput = "asdasdasASDASD12312321S!@#!@#!@";
    const expectedOutput = false;

    expect(profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a arbitary number.
 * @result a false value.
 */
test('isGAA: Testing for that 0 returns false.', () => {
    const testInput = 0;
    const expectedOutput = false;

    expect(profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a User role.
 * @result a false value.
 */
test('isGAA: Testing for User is not a GAA role', () => {
    const testInput = UserRole.USER;
    const expectedOutput = false;

    expect(profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get false for a Default Global Admin role.
 * @result a false value.
 */
test('isGAA: Testing for if DGAA is not a GAA role', () => {
    const testInput = UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    const expectedOutput = false;

    expect(profile.methods.isGAA(testInput)).toBe(expectedOutput);
})

/**
 * Test for ensuring you get true for a Global Application Admin role.
 * @result a true value.
 */
test('isGAA: Testing for if GAA is a GAA role', () => {
    const testInput = UserRole.GLOBALAPPLICATIONADMIN;
    const expectedOutput = true;

    expect(profile.methods.isGAA(testInput)).toBe(expectedOutput);
})