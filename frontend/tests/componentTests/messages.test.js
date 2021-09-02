/**
 * Jest tests for Messages.vue, MessageOption.vue .
 * @jest-environment jsdom
 */

import {test, expect, describe, beforeEach, afterEach, jest} from "@jest/globals"
import {shallowMount} from "@vue/test-utils";
import Messages from "../../src/components/messages/Messages";
import MessageOption from "../../src/components/messages/MessageOption";
import Api from "../../src/Api";


describe("Testing the Messages.vue component", () => {

    test("Testing a 401 response sends you to the invalid token route", async () => {})

    test("Testing a timeout error displays an error message", async () => {})

    test("Testing a random error returns a error message", async () => {})

    test("Testing a setup error returns a error message", async () => {})

    test("Testing a setup error returns a error message", async () => {})

    test("Testing a OK with no conversations returned", async () => {})

    test("Testing a OK with conversations returned", async () => {})
})

describe("Testing MessageOption.vue", () => {

    test("Testing the length of the card name under the character limit", async () => {})

    test("Testing the length of the card name over the character limit", async () => {})

    test("Testing the length of the user name under the character limit", async () => {})

    test("Testing the length of the user name over the character limit", async () => {})
})