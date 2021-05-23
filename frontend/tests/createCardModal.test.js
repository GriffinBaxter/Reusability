import {expect, test, describe} from "@jest/globals";
import Api from "../src/Api"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger"
import VueRouter from 'vue-router'
import router from '../src/router/index'
import CreateCardModal from "@/components/CreateCardModal";

jest.mock("../src/Api");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled : false});
localVue.use(VueRouter);

//

test("Ensure the modal appears when the Create Card is clicked", async () => {

    const createCardWrapper = await shallowMount(CreateCardModal, {localVue});
    const createCardOpenModalButton = createCardWrapper.get("#open-create-card-modal-button");
    const createCardModal = createCardWrapper.get("#create-card-modal");

    await createCardOpenModalButton.trigger("click");

    expect()
})


// describe("", () => {
//
// })