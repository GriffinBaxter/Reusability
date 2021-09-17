import { shallowMount } from '@vue/test-utils'
import BusinessProfile from "../src/views/BusinessProfile";
import {describe, jest, test} from "@jest/globals";

import Cookies from "js-cookie"
import Api from "../src/Api";


let wrapper;

jest.mock("../src/Api");
jest.mock("js-cookie");


describe("Testing that the change profile button appears only when allowed to.", () => {

    test("Testing that if you are not acting as a business then the button does not appear.", async () => {

    });


});
