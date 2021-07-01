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

})
