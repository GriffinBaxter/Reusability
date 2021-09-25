import {mount} from "@vue/test-utils";
import ResetPassword from "../src/views/ResetPassword";
import {beforeEach, describe, expect, test} from "@jest/globals";

let wrapper;

beforeEach(() => {
    wrapper = mount(ResetPassword, {attachTo: document.body});
})

describe("Testing the password field", () => {

    test("Testing that inputting a value into the input field updates the value", async () => {
        const password = await wrapper.find("#password");
        expect(password.element.value).toStrictEqual("");
        expect(wrapper.vm.$data.password).toStrictEqual("");

        await password.setValue("password");
        await password.trigger("input");
        await wrapper.vm.$nextTick();
        expect(password.element.value).toStrictEqual("password");
        expect(wrapper.vm.$data.password).toStrictEqual("password");
    })

    const toggleShowPassword = async (password) => {
        const showPasswordIcon = wrapper.find("#show-password");

        expect(password.element.type).toStrictEqual("password");

        showPasswordIcon.trigger("click");
        await wrapper.vm.$nextTick();
        expect(password.element.type).toStrictEqual("text");

        showPasswordIcon.trigger("click");
        await wrapper.vm.$nextTick();
        expect(password.element.type).toStrictEqual("password");

    }

    test("Testing that pressing the show password icon changes the input type for password", async () => {
        const password = await wrapper.find("#password");
        await toggleShowPassword(password);
    })

    test("Testing that pressing the show password icon changes the input type confirm password", async () => {
        const password = await wrapper.find("#confirm-password");
        await toggleShowPassword(password);
    })
})

describe("Testing the dynamic criteria", () => {

    const getDyamicCriteria = async (wrapper) => {
        return {
            lowerCase: await wrapper.find("#lower-case-criteria"),
            upperCase: await wrapper.find("#upper-case-criteria"),
            number: await wrapper.find("#number-criteria"),
            symbol: await wrapper.find("#symbol-criteria"),
            length: await wrapper.find("#length-criteria")
        }
    }

    test("Testing that on focus all fields turn red", async () => {

        const criteria = await getDyamicCriteria(wrapper);

        expect(criteria.lowerCase.classes()).toEqual(["small"])
        await wrapper.find("#password").trigger("focus")
        await wrapper.vm.$nextTick();
        expect(criteria.lowerCase.classes()).toEqual(["small", "text-red"])
    })

})