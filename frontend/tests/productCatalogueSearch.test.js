/**
 * @jest-environment jsdom
 */

import {test, expect, describe, jest, beforeAll, afterEach} from "@jest/globals"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import ProductCatalogue from "../src/views/ProductCatalogue";
import VueRouter from "vue-router";
import Api from "../src/Api"
import ProductSearchBar from "../src/components/productCatalogue/ProductSearchBar";

jest.mock("../src/Api");

// ---------------------------------------------- Search Bar Tests -----------------------------------------------------
describe("Testing the BrowseListingsSearch methods", () => {

    let productCatalogueSearchWrapper;
    let data;
    let $route;
    let productCatalogueApiResponse;
    let searchClickedSpy;

    beforeAll(() => {
        const $router = {
            push: jest.fn()
        }
        $route = {
            path: '/productCatalogue',
            name: 'ProductCatalogue',
            query: {
                searchQuery: null, searchBy: null,
                barcode: null, orderBy: null, page: null
            }
        }
        data = {
            status: 200,
            data: [
                {

                }],
            headers: {"total-pages": 5}

        }

        Api.searchProducts.mockImplementation(() => Promise.resolve(data));

        productCatalogueApiResponse = {
            status: 200,
            headers: {
                "total-rows": 1,
                "total-pages": 1
            },
            data: [{
                "product": {
                    "barcode": null,
                    "business": {
                        "address": {
                            "city": "Chaoyang District",
                            "country": "China",
                            "postcode": "100102",
                            "region": "Beijing",
                            "streetName": "Wangjing Zhonghuan Nanlu",
                            "streetNumber": "7",
                            "suburb": null
                        },
                        "administrators": {
                            "bio": "Biography",
                            "businessesAdministered": [null],
                            "created": "2021-01-08T00:00",
                            "dateOfBirth": "2008-09-10",
                            "email": "francisca.benitez@example.com",
                            "firstName": "Francisca",
                            "homeAddress": {
                                "city": "Bururi",
                                "country": "Africa",
                                "postcode": "1000",
                                "region": "Bigomogomo",
                                "streetName": "Monique Vista",
                                "streetNumber": "9205",
                                "suburb": null
                            },
                            "id": 6,
                            "lastName": "Benitez",
                            "middleName": "Tina",
                            "nickname": "Fran",
                            "phoneNumber": "12334456",
                            "role": "USER"
                        },
                        "businessType": "RETAIL_TRADE",
                        "created": "2021-02-01T00:00",
                        "description": "Description",
                        "id": 3,
                        "name": "Fringe Wasteless",
                        "primaryAdministratorId": 6
                    },
                    "created": "2021-05-12T00:00",
                    "description": "Crunchy biscuits made with rolled oats golden syrup and coconut with a coating of delicious milk chocolate.",
                    "id": "ARNOTTS-CSNAP",
                    "images": [],
                    "manufacturer": "Arnotts",
                    "name": "Arnotts Chocolate Biscuits Butternut Snap",
                    "recommendedRetailPrice": 2.99
                },
            }]
        }

        productCatalogueSearchWrapper = shallowMount(
            ProductSearchBar,
            {
                mocks: {
                    $router,
                    $route
                }
            });

        Promise.resolve();

        searchClickedSpy = jest.spyOn(ProductSearchBar.methods, 'searchClicked')

    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    describe('Tests the enterPressed method.', () => {

        test("Testing that when a button other than the enter button is pressed the search is not executed", async () => {

            Api.searchUsers.mockImplementation( () => Promise.resolve(productCatalogueApiResponse) ); // here since async

            let searchBar = productCatalogueSearchWrapper.find('#product-search-bar');
            searchBar.trigger('keydown.space');

            const event = new Event("keydown.space", undefined)
            productCatalogueSearchWrapper.vm.enterPressed(event)

            await Promise.resolve();

            expect(searchClickedSpy).toHaveBeenCalledTimes(0);

        })

    });

    describe('Tests the URL populates correctly when searching for all relevant products.', () => {

        let productCatalogueSearchWrapper;
        let router;

        beforeAll(() => {
            const localVue = createLocalVue();
            localVue.use(VueRouter)

            const routes = [{path: '/productCatalogue', component: ProductCatalogue, name: 'ProductCatalogue'}]
            router = new VueRouter({
                routes
            })
            router.push({
                name: 'ProductCatalogue',
            })
            productCatalogueSearchWrapper = shallowMount(ProductSearchBar, {
                localVue,
                router
            });
        });

        test('Testing that pressing enter populates the URL correctly', async() => {
            let inputQuery = 'Product Catalogue Search Enter Test';
            let expectedQuery = 'Product%20Catalogue%20Search%20Enter%20Test';
            productCatalogueSearchWrapper.vm.$refs.searchInput.value = inputQuery;

            let nameCheckbox = productCatalogueSearchWrapper.find('#checkbox-product-name');
            nameCheckbox.trigger('click');

            await productCatalogueSearchWrapper.vm.$nextTick();

            let searchBar = productCatalogueSearchWrapper.find('#product-search-bar');
            searchBar.trigger('keydown.enter');

            await productCatalogueSearchWrapper.vm.$nextTick();

            expect(router.currentRoute.name).toBe('ProductCatalogue')
            expect(router.currentRoute.fullPath).toBe(`/productCatalogue?searchQuery=${expectedQuery}&barcode`)
        });

        test('Testing that clicking the search button populates the URL correctly', async () => {
            let inputQuery = 'Product Catalogue Search Click Test';
            let expectedQuery = 'Product%20Catalogue%20Search%20Click%20Test';

            productCatalogueSearchWrapper.vm.$refs.searchInput.value = inputQuery;

            await productCatalogueSearchWrapper.vm.$nextTick().then(() => {
                let searchButton = productCatalogueSearchWrapper.find('#product-search-button');
                searchButton.trigger('click');

                expect(router.currentRoute.name).toBe('ProductCatalogue')
                expect(router.currentRoute.fullPath).toBe(`/productCatalogue?searchQuery=${expectedQuery}&barcode`)
            });
        });
    })

    // --------------------------------------- Barcode Search Requirement Tests --------------------------------------------

    describe('Tests the barcode search elements.', () => {

        let productCatalogueSearchWrapper;
        let router;

        beforeAll(() => {
            const localVue = createLocalVue();
            localVue.use(VueRouter)

            const routes = [{path: '/productCatalogue', component: ProductCatalogue, name: 'ProductCatalogue'}]
            router = new VueRouter({
                routes
            })
            router.push({
                name: 'BrowseListings',
            })
            productCatalogueSearchWrapper = shallowMount(ProductSearchBar, {
                localVue,
                router
            });
        });


        test('Testing that pressing the camera button opens the barcode scanning model', () => {
            // TODO

        });

        test('Testing that the barcode input box is filled with the barcode from the barcode scanner', () => {
            // TODO

        });

        test('Testing that pressing the clear button for the barcode input box clears the barcode when it is present', () => {
            // TODO
        });

        test('Testing that pressing the clear button for the barcode input box has no impact when there is not barcode present', () => {
            // TODO
        });
    })
})