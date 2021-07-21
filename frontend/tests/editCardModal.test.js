import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import Cookies from "js-cookie";
import Api from "../src/Api";
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";
import VueRouter from "vue-router";
import { UserRole} from "../src/configs/User"
import EditCreateCardModal from "../src/components/marketplace/EditCreateCards";

jest.mock("../src/Api");
jest.mock("js-cookie");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled : false});
localVue.use(VueRouter);

describe("Testing the behaviour of prefilled input fields", () => {

    let editCreateCardModal;

    beforeEach(async () => {
        const mockGetUserApiResponse = {
            status: 200,
            data: {
                firstName: "FIRST_NAME",
                lastName: "LAST_NAME",
                role: UserRole.DEFAULTGLOBALAPPLICATIONADMIN,
                homeAddress: {
                    city: "CITY",
                    suburb: "SUBURB"
                },
            }
        }
        // Mocking the API call response
        const mockApiResponse = {
            status: 200,
            data: {
                id: 1,
                section: "FORSALE",
                title: "TestTitle",
                description: "Card for testing",
                keywords: [
                    {
                        id: 5,
                        name: "Key"
                    }
                ]
            }
        }

        // Mock the Cookie get
        Cookies.get.mockReturnValue(36)

        // Mock the API Calls
        await Api.getUser.mockImplementation(() => Promise.resolve(mockGetUserApiResponse))
        await Api.getDetailForACard.mockImplementation(() => Promise.resolve(mockApiResponse));

        editCreateCardModal = await shallowMount(EditCreateCardModal, {localVue})
        await editCreateCardModal.vm.$nextTick();

        // Mock opening the modal
        editCreateCardModal.vm.setData(1);
        await editCreateCardModal.vm.$nextTick();
    })

    test("Test that the title returned from the Api is stored in the input by default.", async () => {
        // Checking the title has been set correctly
        expect(editCreateCardModal.find("#card-title").exists()).toBe(true);
        expect(editCreateCardModal.find("#card-title").element.value).toBe("TestTitle");
    })

    test("Test that the description returned from the Api is stored in the input by default.", async () => {
        // Checking the description has been set correctly
        expect(editCreateCardModal.find("#card-description").exists()).toBe(true);
        expect(editCreateCardModal.find("#card-description").element.value).toBe("Card for testing");
    })

    test("Test that the section returned from the Api is stored in the input by default.", async () => {
        // Checking the section has been set correctly
        expect(editCreateCardModal.find("#section-selection").exists()).toBe(true);
        expect(editCreateCardModal.find("#section-selection").element.value).toBe("ForSale");
    })

    test("Test that the keywords returned from the Api is stored in the input by default.", async () => {
        // Checking the keywords has been set correctly
        expect(editCreateCardModal.find("#card-keywords").exists()).toBe(true);
        expect(editCreateCardModal.find("#card-keywords").element.value).toBe("#Key");
    })
})