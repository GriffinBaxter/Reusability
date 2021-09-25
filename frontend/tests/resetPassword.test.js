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

    const getDynamicCriteria = async (wrapper) => {
        return {
            lowerCase: await wrapper.find("#lower-case-criteria"),
            upperCase: await wrapper.find("#upper-case-criteria"),
            number: await wrapper.find("#number-criteria"),
            symbol: await wrapper.find("#symbol-criteria"),
            length: await wrapper.find("#length-criteria")
        }
    }

    const redClassList = ["small", "text-red"];
    const normalClassList = ["small"]

    const expectCriteriaClassList = async (criteria, classList) => {
        expect(criteria.lowerCase.classes()).toEqual(classList)
        expect(criteria.upperCase.classes()).toEqual(classList)
        expect(criteria.number.classes()).toEqual(classList)
        expect(criteria.symbol.classes()).toEqual(classList)
        expect(criteria.length.classes()).toEqual(classList)
    }

    const setup = async (criteria) => {
        await expectCriteriaClassList(criteria, normalClassList);
        await wrapper.find("#password").trigger("focus")
        await wrapper.vm.$nextTick();
    }

    test("Testing that on focus all fields turn red", async () => {
        const criteria = await getDynamicCriteria(wrapper);
        await setup(criteria);
        await expectCriteriaClassList(criteria, redClassList);
    })

    const expectChangeInput = async (criteria, classList, input) => {
        await setup(criteria);
        await wrapper.find("#password").setValue(input);
        await wrapper.vm.$nextTick();
        expect(classList).toEqual(normalClassList)
    }

    test("Testing that when we input a lower case character the lower case criteria is no longer red", async () => {
        const criteria = await getDynamicCriteria(wrapper);
        await expectChangeInput(criteria, criteria.lowerCase.classes(), "a")
    })

    test("Testing that when we input a upper case character the upper case criteria is no longer red", async () => {
        const criteria = await getDynamicCriteria(wrapper);
        await expectChangeInput(criteria, criteria.upperCase.classes(), "A")
    })

    test("Testing that when we input a number the number criteria is no longer red", async () => {
        const criteria = await getDynamicCriteria(wrapper);
        await expectChangeInput(criteria, criteria.number.classes(), "1")
    })

    test("Testing that when we input a symbol the symbol criteria is no longer red", async () => {
        const criteria = await getDynamicCriteria(wrapper);
        await expectChangeInput(criteria, criteria.symbol.classes(), "@")
    })

    test("Testing that when we input 8 numbers the length criteria is no longer red", async () => {
        const criteria = await getDynamicCriteria(wrapper);
        await expectChangeInput(criteria, criteria.length.classes(), "12345678")
    })

})