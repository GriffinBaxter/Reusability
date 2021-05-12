import {createLocalVue, shallowMount} from "@vue/test-utils";
import Login from "../src/views/Login"
import Api from "../src/Api"
import VueLogger from "vuejs-logger"
import VueRouter from 'vue-router'
import router from '../src/router/index'

jest.mock("../src/Api");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled : false});
localVue.use(VueRouter);


let loginWrapper;

describe("Clicking on Sign in button", () => {

    let signInButton;

    beforeEach(async () => {

        let signInSuccess = {
            "data": {
                userID: 1
            }
        }
        Api.signIn.mockImplementation(() => Promise.resolve(signInSuccess))

        loginWrapper = await shallowMount(Login, {localVue, router});

        await loginWrapper.vm.$nextTick();

        signInButton = loginWrapper.get("#loginButton")
    })

    test("Successful login with valid credentials", async () => {

        await signInButton.trigger("click");

        expect(loginWrapper.vm.$route.name).toStrictEqual(0);
        // Expect route change

    })

})


