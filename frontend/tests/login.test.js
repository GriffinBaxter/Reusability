import {expect, test, jest, describe} from "@jest/globals";
import Login from "../src/views/Login"
import Api from "../src/Api"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger"
import VueRouter from 'vue-router'
import router from '../src/router/index'

jest.mock("../src/Api");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled : false});
localVue.use(VueRouter);

// ************************************************ Testing API call ***************************************************

describe("Testing the login", () => {

    test('Test the login API', async () => {
        const email = "johnsmith99@gmail.com"
        const password = "1337-H%nt3r2"

        const data = {
            status: 200,
            data: {
                userId: 1
            }
        }

        Api.signIn.mockImplementation(() => Promise.resolve(data));

        const returnData = await Api.signIn(email, password)

        expect(returnData).toBe(data)
    })

    test("Successful login after clicking login button", async () => {
        const loginWrapper = await shallowMount(Login, {localVue, router});
        const signInButton = loginWrapper.get("#loginButton")

        await signInButton.trigger("click");

        expect(loginWrapper.vm.$route.name).toStrictEqual("Login");
    })

    test('Test the login API gives a 400 when the password is incorrect and the user is informed via an error' +
        ' message', async () => {
        const loginWrapper = await shallowMount(Login, {localVue, router});
        const data = {
            response: {
                status: 400
            }
        }

        Api.signIn.mockImplementation(() => Promise.reject(data));

        await loginWrapper.vm.login();
        await loginWrapper.vm.$nextTick();

        expect(loginWrapper.vm.$refs.errorLbl.innerText).toBe('Failed login attempt, email or password incorrect.');
    })

    test('Test the login API gives a 403 when the user unsuccessfully logs in more than 3 times and the user is informed via an error' +
        ' message', async () => {
        const loginWrapper = await shallowMount(Login, {localVue, router});
        const data = {
            response: {
                status: 403
            }
        }

        Api.signIn.mockImplementation(() => Promise.reject(data));

        await loginWrapper.vm.login();
        await loginWrapper.vm.$nextTick();

        expect(loginWrapper.vm.$refs.errorLbl.innerText).toBe(
            'Exceeded login attempts. Please wait 1 hour for your account to be unlocked, or use the ' +
            'forgotten password functionality above.'
        );
    })

    test('Test error message is visible when the login API gives a 400', async () => {
        const loginWrapper = await shallowMount(Login, {localVue, router});
        const data = {
            response: {
                status: 400
            }
        }

        Api.signIn.mockImplementation(() => Promise.reject(data));

        await loginWrapper.vm.login();
        await loginWrapper.vm.$nextTick();

        expect(loginWrapper.find('#error-label').exists()).toBe(true);
    })

    test('Test error message is visible when the login API gives a 403', async () => {
        const loginWrapper = await shallowMount(Login, {localVue, router});
        const data = {
            response: {
                status: 403
            }
        }

        Api.signIn.mockImplementation(() => Promise.reject(data));

        await loginWrapper.vm.login();
        await loginWrapper.vm.$nextTick();

        expect(loginWrapper.find('#error-label').exists()).toBe(true);
    })
})
