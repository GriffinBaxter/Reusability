import {mount} from "@vue/test-utils";
import ForgotPassword from "../src/views/ForgotPassword";
import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import Api from '../src/Api';

let wrapper;

jest.mock("../src/Api");

beforeEach(() => {
    wrapper = mount(ForgotPassword, {
        stubs: ['router-link']
    });
})

describe("Testing the Forgot Password page.", () => {

    test("Testing that the default values are set upon loading the page.", async () => {
        expect(wrapper.vm.errorMessage).toEqual("");
        expect(wrapper.vm.successMessage).toEqual("");
        expect(wrapper.vm.canSendEmail).toBeTruthy();
        expect(wrapper.vm.loading).toBeFalsy();
    });

    test("Testing that the correct values are set upon successful password reset email sending.", async () => {
        wrapper.vm.$refs.email.value = "email@email.com";
        const response = {
            response: {
                status: 201
            }
        }
        Api.forgotPasswordSendEmail.mockImplementation(() => Promise.resolve(response));

        wrapper.vm.sendEmail();

        await wrapper.vm.$nextTick();

        expect(wrapper.vm.errorMessage).toEqual("");
        expect(wrapper.vm.successMessage).toEqual(
            "A password reset link from reusability.help@gmail.com has successfully been sent to your email. " +
            "Make sure to check your spam folder if it's not in your inbox."
        );
        expect(wrapper.vm.canSendEmail).toBeFalsy();
        expect(wrapper.vm.loading).toBeFalsy();
    });

    test("Testing that the correct values are set when the email doesn't belong to a registered user.", async () => {
        wrapper.vm.$refs.email.value = "otheremail@email.com";
        const response = {
            response: {
                status: 406
            }
        }
        Api.forgotPasswordSendEmail.mockImplementation(() => Promise.reject(response));

        wrapper.vm.sendEmail();

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.errorMessage).toEqual("Email does not belong to a registered user.");
        expect(wrapper.vm.successMessage).toEqual("");
        expect(wrapper.vm.canSendEmail).toBeTruthy();
        expect(wrapper.vm.loading).toBeFalsy();
    });

    test("Testing that the correct values are set when the email couldn't be sent (may not exist).", async () => {
        wrapper.vm.$refs.email.value = "email@invalidemail.com";
        const response = {
            response: {
                status: 400
            }
        }
        Api.forgotPasswordSendEmail.mockImplementation(() => Promise.reject(response));

        wrapper.vm.sendEmail();

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.errorMessage).toEqual(
            "Email was unable to be sent (the email address may not exist)."
        );
        expect(wrapper.vm.successMessage).toEqual("");
        expect(wrapper.vm.canSendEmail).toBeTruthy();
        expect(wrapper.vm.loading).toBeFalsy();
    });
})
