import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import {shallowMount} from "@vue/test-utils";
import Api from "../src/Api";
import Inventory from "../src/views/Inventory";

/**
 * Jest tests for Inventory.vue.
 */



describe("Test methods in the inventory component", () => {

    let wrapper;
    let $router;
    let $route;

    beforeEach(async () => {
        $router = {
            push: jest.fn()
        };
        $route = {
            params: {
                businessId: 2
            }
        };
        wrapper = shallowMount(
            Inventory,
            {
                mocks: {
                    $router,
                    $route
                }
            });
        await wrapper.vm.$nextTick();

        // const response = {
        //     status: 200,
        //     data: []
        // }

        // Api.getSalesReport.mockImplementation(() => Promise.resolve(response));
    });

    describe("Test setLinkBusinessAccount method", () => {

        test("Testing setLinkBusinessAccount method sets linkBusinessAccount", async () => {
            const data = [
                    {
                        "id": 4,
                        "administrators": [
                            null
                        ],
                        "primaryAdministratorId": 21,
                        "name": "MyBusiness",
                        "description": null,
                        "address": {
                            "streetNumber": null,
                            "streetName": null,
                            "suburb": null,
                            "city": null,
                            "region": null,
                            "country": "New Zealand",
                            "postcode": null
                        },
                        "businessType": "CHARITABLE_ORGANISATION",
                        "created": "2021-09-26T14:53:14.082085",
                        "currencySymbol": "",
                        "currencyCode": "",
                        "businessImages": []
                    }]

            await wrapper.vm.setLinkBusinessAccount(data);
            expect(wrapper.vm.$data.linkBusinessAccount).toEqual(data);
        })

    });


});
