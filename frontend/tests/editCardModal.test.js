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

describe("Testing the behaviour of prefilled input fields", () => {


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

describe("Testing the creation of new keywords if they don't exist", () => {

    test("Test that the keyword's ID is added to the component's newKeywordIDs list with a successful 201 response", async () => {

        let mockResponse = {
            status: 201,
            data: {
                keywordId: 50
            }
        }

        Api.addNewKeyword.mockImplementation (() => Promise.resolve(mockResponse) );
        await editCreateCardModal.vm.createKeywordIfNotExisting("testKeyword");

        expect(editCreateCardModal.vm.$data.newKeywordIDs).toEqual([50]);

    })

    test("Test that multiple calls to the createKeywordIfNotExisting correctly add all keyword IDs to the component's newKeywordIDs list", async () => {

        let mockResponse = {
            status: 201,
            data: {
                keywordId: 50
            }
        }

        Api.addNewKeyword.mockImplementation (() => Promise.resolve(mockResponse) );
        await editCreateCardModal.vm.createKeywordIfNotExisting("testKeyword");
        await editCreateCardModal.vm.createKeywordIfNotExisting("testKeyword");
        await editCreateCardModal.vm.createKeywordIfNotExisting("testKeyword");

        expect(editCreateCardModal.vm.$data.newKeywordIDs).toEqual([50, 50, 50]);
    })

    test("Testing createKeywordIfNotExisting when a 400 response is received from Api", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            response: {
                status: 400,
                data: {
                    message: "this is a test"
                }
            }
        }

        Api.addNewKeyword.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.createKeywordIfNotExisting();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('Error: this is a test');
    });

    test("Testing createKeywordIfNotExisting when a 401 response is received from Api", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            response: {
                status: 401,
                data: {
                    message: "test error 123"
                }
            }
        };

        Api.addNewKeyword.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.createKeywordIfNotExisting();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('401: Access token missing');
    });

    test("Testing createKeywordIfNotExisting when a different error response is received from Api", async () => {
        let createCardModalWrapper = shallowMount(EditCreateCardModal, {});

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            response: {
                status: 404,
                data: {
                    message: "test1234"
                }
            }
        };

        Api.addNewKeyword.mockImplementation( () => Promise.reject(mockResponse) );

        await createCardModalWrapper.vm.createKeywordIfNotExisting();
        await createCardModalWrapper.vm.$nextTick();

        expect(createCardModalWrapper.vm.modalError).toBe('404: SOMETHING WENT WRONG');
    });

    test("Testing createKeywordIfNotExisting when an error request is received from Api", async () => {
        let createCardModalWrapper = shallowMount(EditCreateCardModal, {});

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            request: {
            }
        };

        Api.addNewKeyword.mockImplementation( () => Promise.reject(mockResponse) );

        await createCardModalWrapper.vm.createKeywordIfNotExisting();
        await createCardModalWrapper.vm.$nextTick();

        expect(createCardModalWrapper.vm.modalError).toBe('Server Timeout');
    });

    test("Testing createKeywordIfNotExisting when a different error is received from Api", async () => {
        let createCardModalWrapper = shallowMount(EditCreateCardModal, {});

        Cookies.get.mockReturnValue(36);

        let mockResponse = {};

        Api.addNewKeyword.mockImplementation( () => Promise.reject(mockResponse) );

        await createCardModalWrapper.vm.createKeywordIfNotExisting();
        await createCardModalWrapper.vm.$nextTick();

        expect(createCardModalWrapper.vm.modalError).toBe('Unexpected error occurred.');
    });

})

describe("Testing the behaviour of the save button", () => {

    test("Testing editCurrentCard when 200 response is received from Api", async () => {

        let $router = {
            go: jest.fn()
        };

        let editCardModalWrapper = shallowMount(EditCreateCardModal, {
            mocks: {
                $router
            }
        });

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'ForSale';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'My test description';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            status: 200
        }

        Api.editCard.mockImplementation( () => Promise.resolve(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();

        expect(editCardModalWrapper.emitted("new-card-created")).toBeTruthy();
    });

    test("Testing editCurrentCard when a 400 response is received from Api", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'Wanted';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'My test description';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            response: {
                status: 400,
                data: {
                    message: "Hello, this is a test"
                }
            }
        }

        Api.editCard.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('Error: Hello, this is a test');
    });

    test("Testing editCurrentCard when a 401 response is received from Api", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'Exchange';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'My test description';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            response: {
                status: 401,
                data: {
                    message: "test error"
                }
            }
        };

        Api.editCard.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('401: Access token missing');
    });

    test("Testing editCurrentCard when a 403 response is received from Api", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'Exchange';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'My new desc';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            response: {
                status: 403,
                data: {
                    message: "test"
                }
            }
        };

        Api.editCard.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('403: Cannot edit card for another user if not GAA or DGAA.');
    });

    test("Testing editCurrentCard when a different error response is received from Api", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'Wanted';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'TEST';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            response: {
                status: 404,
                data: {
                    message: "test123"
                }
            }
        };

        Api.editCard.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('404: SOMETHING WENT WRONG');
    });

    test("Testing editCurrentCard when there is an API request error", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'ForSale';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'Desc';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            request: {
            }
        };

        Api.editCard.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('Server Timeout');
    });

    test("Testing editCurrentCard when a different error is received from Api", async () => {
        let editCardModalWrapper = shallowMount(EditCreateCardModal, {});

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'ForSale';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'Desc';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {};

        Api.editCard.mockImplementation( () => Promise.reject(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();
        await editCardModalWrapper.vm.$nextTick();

        expect(editCardModalWrapper.vm.modalError).toBe('Unexpected error occurred.');
    });

    test("Testing that the newKeywordIDs list in the edit card component is cleared after successfully editing a card", async () => {

        let $router = {
            go: jest.fn()
        };

        let editCardModalWrapper = shallowMount(EditCreateCardModal, {
            mocks: {
                $router
            }
        });

        editCardModalWrapper.vm.submitAttempted = true;
        editCardModalWrapper.vm.sectionSelected = 'ForSale';
        editCardModalWrapper.vm.title = 'Card';
        editCardModalWrapper.vm.description = 'My test description';

        Cookies.get.mockReturnValue(36);

        let mockResponse = {
            status: 200
        }

        Api.editCard.mockImplementation( () => Promise.resolve(mockResponse) );

        await editCardModalWrapper.vm.editCurrentCard();

        expect(editCardModalWrapper.vm.$data.newKeywordIDs.length).toBe(0);

    });

})
