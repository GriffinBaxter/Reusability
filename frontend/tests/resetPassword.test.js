import {shallowMount} from "@vue/test-utils";
import ResetPassword from "../src/views/ResetPassword";
import {beforeEach, describe, expect, test} from "@jest/globals";

let wrapper;

beforeEach(() => {
    wrapper = shallowMount(ResetPassword);
})

describe("Teting the password field", () => {


    test("Testing that inputing a value into the input field updates the value", async () => {
        const password = await wrapper.find("#password");
        expect(password.element.value).toStrictEqual("");
        expect(wrapper.vm.$data.password).toStrictEqual("");

        await password.setValue("password");
        await password.trigger("input");
        await wrapper.vm.$nextTick();
        expect(password.element.value).toStrictEqual("password");
        expect(wrapper.vm.$data.password).toStrictEqual("password");
    })

})