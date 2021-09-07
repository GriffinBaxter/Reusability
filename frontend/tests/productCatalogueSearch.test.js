/**
 * @jest-environment jsdom
 */

import {test, expect, describe, jest, beforeAll, afterEach} from "@jest/globals"
import {createLocalVue, shallowMount, mount} from "@vue/test-utils";
import ProductCatalogue from "../src/views/ProductCatalogue";
import VueRouter from "vue-router";
import Api from "../src/Api"
import ProductSearchBar from "../src/components/productCatalogue/ProductSearchBar";
import BarcodeScannerModal from "../src/components/BarcodeScannerModal";

jest.mock("../src/Api");

// ---------------------------------------------- Search Bar Tests -----------------------------------------------------
describe("Testing the ProductSearchBar methods", () => {

    let productCatalogueSearchWrapper;
    let data;
    let $route;
    let productCatalogueApiResponse;
    let searchClickedSpy;
    let productSearchBarWrapper;
    let checkboxProductNameButton;

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

        productSearchBarWrapper = shallowMount(ProductSearchBar,
            {
                mocks: {
                    $router,
                    $route
                }
            }
        );

        Promise.resolve();

        searchClickedSpy = jest.spyOn(ProductSearchBar.methods, 'searchClicked');

        checkboxProductNameButton = productSearchBarWrapper.find('#checkbox-product-name');

        jest.spyOn(document, 'getElementById').mockImplementation(() => {
            return checkboxProductNameButton.element;
        })

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
            const inputQuery = 'Product Catalogue Search Enter Test';
            const barcode = 123457891234;
            productCatalogueSearchWrapper.vm.$refs.searchInput.value = inputQuery;
            productCatalogueSearchWrapper.vm.barcode = barcode;

            let nameCheckbox = productCatalogueSearchWrapper.find('#checkbox-product-name');
            nameCheckbox.trigger('click');
            await productCatalogueSearchWrapper.vm.$nextTick();

            let searchBar = productCatalogueSearchWrapper.find('#product-search-bar');
            searchBar.trigger('keydown.enter');
            await productCatalogueSearchWrapper.vm.$nextTick();

            expect(router.currentRoute.name).toBe('ProductCatalogue')
            expect(productCatalogueSearchWrapper.emitted().search[0]).toEqual([["name"], inputQuery, barcode]);
        });

        test('Testing that clicking the search button populates the URL correctly', async () => {
            const inputQuery = 'Product Catalogue Search Click Test';
            const barcode = 123457891234;

            productCatalogueSearchWrapper.vm.$refs.searchInput.value = inputQuery;
            productCatalogueSearchWrapper.vm.barcode = barcode;

            await productCatalogueSearchWrapper.vm.$nextTick().then(() => {
                let searchButton = productCatalogueSearchWrapper.find('#product-search-button');
                searchButton.trigger('click');

                expect(router.currentRoute.name).toBe('ProductCatalogue')
                expect(productCatalogueSearchWrapper.emitted().search[1]).toEqual([["name"], inputQuery, barcode]);


            });
        });
    })

    // --------------------------------------- Barcode Search Requirement Tests --------------------------------------------

    describe('Tests the barcode search elements.', () => {

        let productCatalogueSearchWrapper;
        let router;

        beforeEach(() => {
            const localVue = createLocalVue();
            localVue.use(VueRouter)

            const routes = [{path: '/productCatalogue', component: ProductCatalogue, name: 'ProductCatalogue'}]
            router = new VueRouter({
                routes
            })
            router.push({
                name: 'ProductCatalogue',
            })
            productCatalogueSearchWrapper = mount(ProductSearchBar, {
                localVue,
                router
            });
        });

        test('Testing that pressing the camera button opens the barcode scanning model', async () => {

            let scannerModalBtn;

            scannerModalBtn = productCatalogueSearchWrapper.find('#scanner-modal-btn');
            await scannerModalBtn.trigger('click');

            await productCatalogueSearchWrapper.vm.$nextTick();

            const modal = productCatalogueSearchWrapper.findComponent(BarcodeScannerModal)

            expect(scannerModalBtn.exists()).toBeTruthy();
            expect(modal.vm.hasBeenShown).toBe(true);

        });

        test('Testing that the barcode scanning model is not present by default', async () => {

            let scannerModalBtn;

            scannerModalBtn = productCatalogueSearchWrapper.find('#scanner-modal-btn');

            await productCatalogueSearchWrapper.vm.$nextTick();

            const modal = productCatalogueSearchWrapper.findComponent(BarcodeScannerModal)

            expect(scannerModalBtn.exists()).toBeTruthy();
            expect(modal.vm.hasBeenShown).toBe(false);

        });

        test('Testing that the barcode input box is filled with the barcode from the barcode scanner', () => {
            // TODO

        });

        test('Testing that pressing the clear button for the barcode input box clears the barcode when it is present', () => {
            let barcodeClearBtn;
            productCatalogueSearchWrapper.vm.barcode = "1234567891234";

            barcodeClearBtn = productCatalogueSearchWrapper.find('#barcode-clear-btn');
            barcodeClearBtn.trigger('click');

            productCatalogueSearchWrapper.vm.$nextTick();

            expect(productCatalogueSearchWrapper.vm.barcode).toBe("");
        });

        test('Testing that pressing the clear button for the barcode input box has no impact when there is not barcode present', () => {
            let barcodeClearBtn;
            productCatalogueSearchWrapper.vm.barcode = "";

            barcodeClearBtn = productCatalogueSearchWrapper.find('#barcode-clear-btn');
            barcodeClearBtn.trigger('click');

            productCatalogueSearchWrapper.vm.$nextTick();

            expect(productCatalogueSearchWrapper.vm.barcode).toBe("");
        });
    })
})