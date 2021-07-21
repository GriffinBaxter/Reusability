import {expect, test, describe, beforeEach, jest} from "@jest/globals";
import Api from "../src/Api"
import Cookies from "js-cookie"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger"
import VueRouter from 'vue-router'
import {UserRole} from "../src/configs/User";
import EditCreateCardModal from "../src/components/marketplace/EditCreateCards";

jest.mock("../src/Api");
jest.mock("js-cookie");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled : false});
localVue.use(VueRouter);

/** White box testing ... */

describe("Testing the selection behaviour of the select section.", () => {

    let createCardModalWrapper;

    beforeEach(async () => {
        const mockApiResponse = {
            status: 200,
            data: {
                homeAddress: {
                    city: "CITY"
                },
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );
        Cookies.get.mockReturnValue(36);

        createCardModalWrapper = await shallowMount(EditCreateCardModal, {localVue});
        await createCardModalWrapper.vm.$nextTick();

    })

    test("Test that when you select ForSale it is stored in data", async () => {

        expect(createCardModalWrapper.find("#section-selection").exists()).toBe(true);
        expect(createCardModalWrapper.find("#for-sale-option").exists()).toBe(true);
        const forSaleOption = createCardModalWrapper.find("#for-sale-option");

        forSaleOption.setSelected();
        expect(createCardModalWrapper.vm.$data.sectionSelected).toBe("ForSale");
    })

    test("Test that when you select Exchange it is stored in data", async () => {

        expect(createCardModalWrapper.find("#section-selection").exists()).toBe(true);
        expect(createCardModalWrapper.find("#exchange-option").exists()).toBe(true);
        const exchangeOption = createCardModalWrapper.find("#exchange-option");

        exchangeOption.setSelected();
        expect(createCardModalWrapper.vm.$data.sectionSelected).toBe("Exchange");
    })

    test("Test that when you select Wanted it is stored in data", async () => {

        expect(createCardModalWrapper.find("#section-selection").exists()).toBe(true);
        expect(createCardModalWrapper.find("#wanted-option").exists()).toBe(true);
        const wantedOption = createCardModalWrapper.find("#wanted-option");

        wantedOption.setSelected();
        expect(createCardModalWrapper.vm.$data.sectionSelected).toBe("Wanted");
    })

    test("Test that when the form is submitted with no selection invalid feedback is provided.", async () => {

        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(false);

        Api.addNewCard.mockImplementation( Promise.resolve( () => {} ) )
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        expect(Api.addNewCard).toBeCalledTimes(0);
        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(true);
    })

    test("Test that when the form is submitted with for sale selected, no invalid feedback is returned", async () => {

        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(false);

        expect(createCardModalWrapper.find("#for-sale-option").exists()).toBe(true);
        const forSaleOption = createCardModalWrapper.find("#for-sale-option");
        forSaleOption.setSelected();

        Api.addNewCard.mockImplementation( Promise.resolve( () => {} ) )
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(false);
    })

    test("Test that when the form is submitted with for sale selected, no invalid feedback is returned", async () => {

        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(false);

        expect(createCardModalWrapper.find("#exchange-option").exists()).toBe(true);
        const exchangeOption = createCardModalWrapper.find("#exchange-option");
        exchangeOption.setSelected();

        Api.addNewCard.mockImplementation( Promise.resolve( () => {} ) )
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(false);
    })

    test("Test that when the form is submitted with for sale selected, no invalid feedback is returned", async () => {

        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(false);

        expect(createCardModalWrapper.find("#wanted-option").exists()).toBe(true);
        const wantedOption = createCardModalWrapper.find("#wanted-option");
        wantedOption.setSelected();

        Api.addNewCard.mockImplementation( Promise.resolve( () => {} ) )
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        expect(createCardModalWrapper.find("#section-selection-invalid-feedback").exists()).toBe(false);
    })

} )

describe("Testing the behaviour of prefilled input fields", () => {

    let createCardModal;

    beforeEach(async () => {
        // Mocking the get from the js-cookie library
        const mockApiResponse = {
            status: 200,
            data: {
                firstName: "FIRST_NAME",
                lastName: "LAST_NAME",
                role: UserRole.DEFAULTGLOBALAPPLICATIONADMIN
            }
        }
        // Mock the API Calls
        Cookies.get.mockReturnValue(36)
        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );

        createCardModal = await shallowMount(EditCreateCardModal, {localVue})
        await createCardModal.vm.$nextTick();

    })

    test("Test that the id returned from the Api is stored in the input by default.", async () => {

        // Checking that the creator id has the correct value
        expect(createCardModal.vm.$data.creatorId).toBe(36)

        // Checking the form has been updated correctly.
        expect(createCardModal.find("#card-creator-id").exists()).toBe(true);
        expect(createCardModal.find("#card-creator-id").element.value).toBe("36")

    })

    test("Test that the name field is automatically displaying the correct full name", async () => {

        expect(createCardModal.vm.$data.userFullName).toBe("FIRST_NAME LAST_NAME")
        expect(createCardModal.find("#user-full-name").exists()).toBe(true);
        expect(createCardModal.find("#user-full-name").text()).toBe("FIRST_NAME LAST_NAME");

    })

    test("Test that the location field is correctly generated with only a city", async () => {

        const mockApiResponse = {
            status: 200,
            data: {
                homeAddress: {
                    city: "CITY"
                },
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );
        createCardModal = await shallowMount(EditCreateCardModal, {localVue})
        await createCardModal.vm.$nextTick();


        expect(createCardModal.vm.$data.userLocation).toBe("CITY");
        expect(createCardModal.find("#user-location").exists()).toBe(true)
        expect(createCardModal.find("#user-location").text()).toBe("CITY");
    })

    test("Test that the location is correctly generated with only a suburb", async () => {
        const mockApiResponse = {
            status: 200,
            data: {
                homeAddress: {
                    suburb: "SUBURB"
                },
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );
        createCardModal = await shallowMount(EditCreateCardModal, {localVue})
        await createCardModal.vm.$nextTick();

        expect(createCardModal.vm.$data.userLocation).toBe("SUBURB");
        expect(createCardModal.find("#user-location").exists()).toBe(true)
        expect(createCardModal.find("#user-location").text()).toBe("SUBURB");
    })

    test("Test that the location is correctly generated with no home address data.", async () => {
        const mockApiResponse = {
            status: 200,
            data: {
                homeAddress: {
                },
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );
        createCardModal = await shallowMount(EditCreateCardModal, {localVue})
        await createCardModal.vm.$nextTick();

        expect(createCardModal.vm.$data.userLocation).toBe("N/A");
        expect(createCardModal.find("#user-location").exists()).toBe(true)
        expect(createCardModal.find("#user-location").text()).toBe("N/A");
    })

    test("Test that the location is correctly generated with both city and suburb", async () => {
        const mockApiResponse = {
            status: 200,
            data: {
                homeAddress: {
                    city: "CITY",
                    suburb: "SUBURB"
                },
            }
        }

        Api.getUser.mockImplementation( () => Promise.resolve(mockApiResponse) );
        createCardModal = await shallowMount(EditCreateCardModal, {localVue})
        await createCardModal.vm.$nextTick();

        expect(createCardModal.vm.$data.userLocation).toBe("SUBURB, CITY");
        expect(createCardModal.find("#user-location").exists()).toBe(true)
        expect(createCardModal.find("#user-location").text()).toBe("SUBURB, CITY");
    })

})

describe( "Testing the title input field", () => {

    let createCardModalWrapper;

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

        Api.getUser.mockImplementation( () => Promise.resolve(mockGetUserApiResponse) );
        Api.addNewCard.mockImplementation( () => {} );
        Cookies.get.mockReturnValue(36);

        createCardModalWrapper = await shallowMount(EditCreateCardModal, {localVue});
        await createCardModalWrapper.vm.$nextTick();

    })

    test("Testing an empty title input field", async () => {

        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-title").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is provided.
        expect(Api.addNewCard).toBeCalledTimes(0);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").text()).toBe("The title must be between 1 and 51 in length.");
        expect(createCardModalWrapper.vm.$data.formError.titleError).toBe("The title must be between 1 and 51 in length.")
        expect(createCardModalWrapper.vm.$data.formErrorClasses.titleError).toBe("is-invalid")
    })

    test("Testing a single character string (A)", async () => {

        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-title").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-title").setValue("A");

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.title).toBe("A");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is provided.
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.titleError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.titleError).toBe("")
    })

    test("Testing with 50 characters string (12345678912345678912345678912345678912345678912345)", async () => {

        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-title").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-title").setValue("12345678912345678912345678912345678912345678912345");

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.title).toBe("12345678912345678912345678912345678912345678912345");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is provided.
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.titleError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.titleError).toBe("")
    })

    test("Testing with 51 characters string (123456789123456789123456789123456789123456789123456)", async () => {

        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-title").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-title").setValue("123456789123456789123456789123456789123456789123456");

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.title).toBe("123456789123456789123456789123456789123456789123456");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is provided.
        expect(Api.addNewCard).toBeCalledTimes(0);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").text()).toBe("The title must be between 1 and 51 in length.");
        expect(createCardModalWrapper.vm.$data.formError.titleError).toBe("The title must be between 1 and 51 in length.")
        expect(createCardModalWrapper.vm.$data.formErrorClasses.titleError).toBe("is-invalid")
    })

    test("Testing the title can handle emojis 😋", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-title").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-title").setValue("😋");

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.title).toBe("😋");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is provided.
        expect(createCardModalWrapper.find("#card-title-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.titleError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.titleError).toBe("")
    })
})

describe("Testing the description field", () => {

    let createCardModalWrapper;

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

        Api.getUser.mockImplementation( () => Promise.resolve(mockGetUserApiResponse) );
        Api.addNewCard.mockImplementation( () => {} );
        Cookies.get.mockReturnValue(36);

        createCardModalWrapper = await shallowMount(EditCreateCardModal, {localVue});
        await createCardModalWrapper.vm.$nextTick();

    })

    test("Testing an empty title input field", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-description").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.descriptionError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.descriptionError).toBe("")
    })

    test("Testing a single character string (A)", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-description").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);

        // Set the description input value.
        createCardModalWrapper.find("#card-title").setValue("A");

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.title).toBe("A");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.descriptionError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.descriptionError).toBe("")
    })

    test("Testing with 300 characters string", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-description").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);

        // Set the description input value.
        createCardModalWrapper.find("#card-description").setValue("A".repeat(300));

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.description).toBe("A".repeat(300));

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.descriptionError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.descriptionError).toBe("")
    })

    test("Testing with 301 characters string", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-description").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);

        // Set the description input value.
        createCardModalWrapper.find("#card-description").setValue("A".repeat(301));

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.description).toBe("A".repeat(301));

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is provided.
        expect(Api.addNewCard).toBeCalledTimes(0);
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").text()).toBe("The description length must be between 0 and 301 in length.");
        expect(createCardModalWrapper.vm.$data.formError.descriptionError).toBe("The description length must be between 0 and 301 in length.")
        expect(createCardModalWrapper.vm.$data.formErrorClasses.descriptionError).toBe("is-invalid")
    })

    test("Testing the title can handle emojis 😋", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-description").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);

        // Set the description input value.
        createCardModalWrapper.find("#card-description").setValue("😋");

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.description).toBe("😋");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is provided.
        expect(createCardModalWrapper.find("#card-description-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.descriptionError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.descriptionError).toBe("")
    })

})

describe("Testing the keywords field", () => {

    let createCardModalWrapper;

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

        Api.getUser.mockImplementation( () => Promise.resolve(mockGetUserApiResponse) );
        Api.addNewCard.mockImplementation( () => {} );
        Cookies.get.mockReturnValue(36);

        createCardModalWrapper = await shallowMount(EditCreateCardModal, {localVue});
        await createCardModalWrapper.vm.$nextTick();

    })

    test("Testing an empty keywords input field", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("")
    })

    test("Testing a single character string (A)", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("A");
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#A");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(true);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("All keywords need to be between 2 and 20 in length.");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("is-invalid")
    })

    test("Testing a single character string (#)", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("#");
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(true);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("All keywords need to be between 2 and 20 in length.");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("is-invalid")
    })

    test("Testing with 3 character string (123)", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("123");
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#"+"123");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("")
    })

    test("Testing with 19 characters string", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("A".repeat(19));
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#"+"A".repeat(19));

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("")
    })

    test("Testing with 20 characters string", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("A".repeat(20));
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated. and the extra characters are removed!
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#"+"A".repeat(19));

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("")
    })

    test("Testing the title can handle emojis 😋😋😋", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("😋😋😋");
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#😋😋😋");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("")
    })

    test("Testing the title can handle emojis 😋", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("😋");
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#😋");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("")
    })

    test("Testing the title can handle emojis 😋😋", async () => {
        // Checking all necessary elements are exist and do not.
        expect(createCardModalWrapper.find("#card-keywords").exists()).toBe(true);
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);

        // Set the title input value.
        createCardModalWrapper.find("#card-keywords").setValue("😋😋");
        await createCardModalWrapper.vm.$nextTick();

        // Ensure that the data was updated.
        expect(createCardModalWrapper.vm.$data.keywordsInput).toBe("#😋😋");

        // Perform a submission
        await createCardModalWrapper.vm.createNewCard();
        await createCardModalWrapper.vm.$nextTick();

        // Ensure invalid feedback is not provided.
        expect(createCardModalWrapper.find("#card-keywords-invalid-feedback").exists()).toBe(false);
        expect(createCardModalWrapper.vm.$data.formError.keywordsError).toBe("");
        expect(createCardModalWrapper.vm.$data.formErrorClasses.keywordsError).toBe("")
    })

})

describe("Testing required fields", () => {

    let createCardWrapper;

    beforeEach( async () => {
        // Mock all the API calls needed.
        const getUserMockResponse = {
            status: 200,
            data: {
                role: UserRole.DEFAULTGLOBALAPPLICATIONADMIN,
                firstName: "FIRST",
                lastName: "LAST",
                homeAddress: {
                    city: "CITY",
                    suburb: "SUBURB"
                }
            }
        }
        Api.getUser.mockImplementation( () => Promise.resolve(getUserMockResponse))
        Cookies.get.mockReturnValue(36);
        Api.addNewCard.mockImplementation( () => Promise.resolve( {status: 201} ) )

        // Shallow mount the component
        createCardWrapper = await shallowMount(EditCreateCardModal, {localVue});
        await createCardWrapper.vm.$nextTick();

    } )

    test("Submitting the create new card form with minimum required fields", async () => {
        // Set the section & verify it is there.
        expect(createCardWrapper.find("#section-selection").exists()).toBe(true)
        expect(createCardWrapper.find("#for-sale-option").exists()).toBe(true);
        createCardWrapper.find("#for-sale-option").setSelected();
        expect(createCardWrapper.vm.$data.sectionSelected).toBe("ForSale");

        // Verify that the creator Id is set.
        expect(createCardWrapper.find("#card-creator-id").exists()).toBe(true);
        expect(createCardWrapper.vm.$data.creatorId).toBe(36);
        expect(createCardWrapper.find("#card-creator-id").element.value).toBe("36");

        // Set and verify the title.
        expect(createCardWrapper.find("#card-title").exists()).toBe(true);
        createCardWrapper.find("#card-title").setValue("A Title");
        expect(createCardWrapper.find("#card-title").element.value).toBe("A Title");
        expect(createCardWrapper.vm.$data.title).toBe("A Title");

        // Attempt to create the card.
        await createCardWrapper.vm.createNewCard();
        expect(Api.addNewCard).toBeCalledTimes(1);
    })

})