import {test, expect, describe, jest, beforeEach} from "@jest/globals";
import Home from '../src/views/Home'
import Api from "../src/Api";
import Cookies from "js-cookie"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";

const localVue = createLocalVue();
jest.mock("../src/Api");
jest.mock("js-cookie")
localVue.use(VueLogger, {isEnabled: false})
let wrapper;

const $router = {
    push: jest.fn()
};

const card1 = {"id":34,"creator":{"id":21,"firstName":"John","lastName":"Doe","middleName":"S","nickname":"Johnny","bio":"Biography","email":"email@email.com","created":"2021-02-02T00:00","role":"DEFAULTGLOBALAPPLICATIONADMIN","businessesAdministered":[null],"homeAddress":{"suburb":"Ilam","city":"Christchurch","region":"Canterbury","country":"New Zealand"}},"section":"EXCHANGE","created":"2021-08-22T14:37:25.767435","displayPeriodEnd":"2021-09-05T14:37:25.767435","title":"asdasd","description":"asd","keywords":[{"id":19,"name":"asd","created":"2021-08-22T14:37:25.745069"}]};

describe("Tests for bookmark message display", () => {
    test("Test 'No bookmarked messages to show' displays when no bookmark messages.", async () => {
        const response = {
            data: []
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeFalsy();
        expect(wrapper.find('#no-bookmarks-message').exists()).toBeTruthy();
    });

    test("Test 'No bookmarked messages to show' will not display when there is a bookmark message.", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeTruthy();
        expect(wrapper.find('#no-bookmarks-message').exists()).toBeFalsy();
    });

    test("Test the bookmark message will display when there is only one message", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeTruthy();
    });

    test("Test the bookmark messages will display when there is more than one message", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                },
                {
                    id: 2,
                    description: "Bookmark for product listing 'Eta Ripple Cut Potato Chips Chicken' has been removed.",
                    created: "2021-08-17T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                },
                {
                    id: 3,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-18T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#bookmark-message-container-1').exists()).toBeTruthy();
        expect(wrapper.find('#bookmark-message-container-2').exists()).toBeTruthy();
        expect(wrapper.find('#bookmark-message-container-3').exists()).toBeTruthy();
    });
})

describe("Tests for toListing function", () => {
    test("Test the page is pushed to business profile listing page, when the message is clicked", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();
        await wrapper.find('#bookmark-message-link-1').trigger("click");
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: `/businessProfile/1/listings/1`});
    });
})

describe("Tests for bookmark message deletion", () => {
    test("Test the bookmark message will been delete when user click on the delete button", async () => {
        const response = {
            data: [
                {
                    id: 1,
                    description: "Product listing 'Mandarins' has been bookmarked.",
                    created: "2021-08-16T11:34:13.889807",
                    listingId: 1,
                    businessId: 1,
                    closes: "2021-09-12T00:00"
                }
            ]
        }

        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve(response));
        Api.deleteBookmarkMessage.mockResolvedValue();

        wrapper = await shallowMount(Home, {
            localVue,
            mocks: {
                $router
            }
        });
        await wrapper.vm.$nextTick();
        expect(wrapper.find('#delete-bookmark-message-button-1').exists()).toBeTruthy();

        await wrapper.find('#delete-bookmark-message-button-1').trigger("click");
        await wrapper.vm.$nextTick();

        expect(wrapper.find('#delete-bookmark-message-button-1').exists()).toBeFalsy();
    });
})


describe("Tests for my cards section", () => {

    beforeEach( () => {
        Cookies.get.mockImplementation(
            (name) => {
                if (name === "actAs") {
                    return undefined;
                } else {
                    return 15;
                }
            }
        );
    })

    const createWrapperAndClick = async (response, success) => {
        if (success) {
            Api.getUsersCards.mockImplementation(() => Promise.resolve(response));
        } else {
            Api.getUsersCards.mockImplementation(() => Promise.reject(response));
        }
        wrapper = shallowMount(
            Home,
            {
                localVue,
                mocks: {
                    $router
                }
            }
        )

        await wrapper.vm.$nextTick();
        await wrapper.find("#my-cards-tab").trigger("click");
        await wrapper.vm.$nextTick();
    }

    test("Testing that no cards to show message appears when the user has no cards", async () => {
        const response = {
            status: 200,
            data: []
        }

        await createWrapperAndClick(response, true);

        expect(wrapper.find("#no-cards-message").exists()).toBeTruthy();
        expect(wrapper.find("#cards-container").exists()).toBeFalsy();
        expect(wrapper.find("#cards-error-message").exists()).toBeFalsy();
        expect(wrapper.find("#loading-cards-dots").exists()).toBeFalsy();
    })

    test("Testing that the cards appear on the page when user has cards", async () => {
        const response = {
        status: 200,
        data: [card1]
        }

        await createWrapperAndClick(response, true);

        expect(wrapper.find("#no-cards-message").exists()).toBeFalsy();
        expect(wrapper.find("#cards-container").exists()).toBeTruthy();
        expect(wrapper.find("#cards-error-message").exists()).toBeFalsy();
        expect(wrapper.find("#loading-cards-dots").exists()).toBeFalsy();
    })

    test("Testing that an error message appears for a 406", async () => {
        const response = {
            response: {
                status: 406,
            }
        }

        await createWrapperAndClick(response, false);

        expect(wrapper.find("#no-cards-message").exists()).toBeFalsy();
        expect(wrapper.find("#cards-container").exists()).toBeFalsy();
        expect(wrapper.find("#cards-error-message").exists()).toBeTruthy();
        expect(wrapper.find("#cards-error-message").text()).toStrictEqual("No user id found.")
    })

    test("Testing that an error message appears for a unknown code", async () => {
        const response = {
            response: {
                status: 500,
            }
        }

        await createWrapperAndClick(response, false);

        expect(wrapper.find("#no-cards-message").exists()).toBeFalsy();
        expect(wrapper.find("#cards-container").exists()).toBeFalsy();
        expect(wrapper.find("#cards-error-message").exists()).toBeTruthy();
        expect(wrapper.find("#cards-error-message").text()).toStrictEqual("Something went wrong...")
    })

    test("Testing that user is redirected on a 401", async () => {
        const response = {
            response: {
                status: 401,
            }
        }

        await createWrapperAndClick(response, false);

        expect(wrapper.find("#no-cards-message").exists()).toBeFalsy();
        expect(wrapper.find("#cards-container").exists()).toBeFalsy();
        expect(wrapper.find("#cards-error-message").exists()).toBeTruthy();
        expect(wrapper.find("#cards-error-message").text()).toStrictEqual("Unauthorized to see the cards.")
        expect($router.push).toHaveBeenCalledWith({path: '/invalidtoken'})
    })

})

describe("Tests for tab being available depending ", () => {

    const createWrapperAndMockApi = async () => {
        const response = {
            status: 200,
            data: [card1]
        }
        Api.getUsersCards.mockImplementation(() => Promise.resolve(response));
        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve({
            status: 200,
            data: []
        }));

        wrapper = shallowMount(
            Home,
            {
                localVue,
                mocks: {
                    $router
                }
            }
        )
        await wrapper.vm.$nextTick();
    }

    test("Testing for actAs returning a value", async () => {
        Cookies.get.mockImplementation(
            (name) => {
                if (name === "actAs") {
                    return 15;
                } else {
                    return 15;
                }
            }
        );
        await createWrapperAndMockApi();

        expect(wrapper.find("#my-cards-tab").exists()).toBeFalsy();
        expect(wrapper.find("#my-cards").exists()).toBeFalsy();
    })

    test("Testing the actAs value being equal to undefined", async () => {
        Cookies.get.mockImplementation(
            (name) => {
                if (name === "actAs") {
                    return undefined;
                } else {
                    return 15;
                }
            }
        );
        await createWrapperAndMockApi();

        expect(wrapper.find("#my-cards-tab").exists()).toBeTruthy();
        expect(wrapper.find("#my-cards").exists()).toBeTruthy();
    })

    test("Testing the actAs value being equal to null", async () => {
        Cookies.get.mockImplementation(
            (name) => {
                if (name === "actAs") {
                    return null;
                } else {
                    return 15;
                }
            }
        );
        await createWrapperAndMockApi();

        expect(wrapper.find("#my-cards-tab").exists()).toBeTruthy();
        expect(wrapper.find("#my-cards").exists()).toBeTruthy();
    })
})

describe("Tests the compareCards method", () => {

    const createWrapperAndMockApi = async () => {
        const response = {
            status: 200,
            data: [card1]
        }
        Api.getUsersCards.mockImplementation(() => Promise.resolve(response));
        Api.getBookmarkedMessage.mockImplementation(() => Promise.resolve({
            status: 200,
            data: []
        }));

        wrapper = shallowMount(
            Home,
            {
                localVue,
                mocks: {
                    $router
                }
            }
        )
        await wrapper.vm.$nextTick();
    }

    test("Test the method returns -1 when section of card 1 has a smaller alphabetic order than the section of card 2", async () => {
        const card1 = { section: "ForSale" };
        const card2 = { section: "Wanted" };

        await createWrapperAndMockApi();

        expect(wrapper.vm.compareCards(card1, card2)).toEqual(-1);
    });

    test("Test the method returns 1 when section of card 1 has a higher alphabetic order than the section of card 2", async () => {
        const card1 = { section: "Wanted" };
        const card2 = { section: "ForSale" };

        await createWrapperAndMockApi();

        expect(wrapper.vm.compareCards(card1, card2)).toEqual(1);
    });

    test("Test the method returns 0 when section of card 1 is equal to the section of card 2", async () => {
        const card = { section: "Wanted" };

        await createWrapperAndMockApi();

        expect(wrapper.vm.compareCards(card, card)).toEqual(0);
    });
})