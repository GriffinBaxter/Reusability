/**
 * @jest-environment jsdom
 */

import {createLocalVue, shallowMount} from '@vue/test-utils';
import EditBusinessProfile from "../src/views/EditBusinessProfile";
import AddressAPI from "../src/addressInstance";
import Api from "../src/Api";
import {beforeEach, describe, expect, jest, test} from "@jest/globals";
import Cookies from "js-cookie";
import VueLogger from "vuejs-logger";

jest.mock("../src/addressInstance");
jest.mock("../src/Api");
jest.mock("js-cookie");

const localVue = createLocalVue();
localVue.use(VueLogger, {isEnabled: false})

describe("Testing methods in EditBusinessProfile", () => {

    let wrapper;
    let $router;
    let $route;
    let editBusinessSpy;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };

        $route = {
            params: {
                id: "1"
            }
        };

        const userResponse = {
            "data": {
                "id": 1,
                "firstName": "Evelia",
                "lastName": "Blanxart",
                "middleName": "Robert",
                "nickname": "Robby",
                "bio": "I like art!",
                "email": "everblanxart@gmail.com",
                "created": "2019-05-20T00:00",
                "role": "USER",
                "businessesAdministered": [
                    {
                        "id": 1,
                        "administrators": [
                            null
                        ],
                        "primaryAdministratorId": 10,
                        "name": "Sunburst Waste",
                        "description": "Description",
                        "address": {
                            "streetNumber": "1849",
                            "streetName": "C Street Northwest",
                            "suburb": null,
                            "city": "Washington",
                            "region": "District of Columbia",
                            "country": "United States",
                            "postcode": "20240"
                        },
                        "businessType": "CHARITABLE_ORGANISATION",
                        "created": "2021-02-14T00:00",
                        "currencySymbol": "$",
                        "currencyCode": "USD"
                    }
                ],
                "images": [],
                "dateOfBirth": "2007-04-13",
                "phoneNumber": "0272331323",
                "homeAddress": {
                    "streetNumber": "190",
                    "streetName": "Fort Washington Avenue",
                    "suburb": null,
                    "city": "New York",
                    "region": "New York",
                    "country": "United States",
                    "postcode": "10040"
                }
            },
            "status": 200,
        }
        Api.getUser.mockImplementation( () => Promise.resolve(userResponse));

        const businessResponse = {
            data: {
                "id": 1,
                "administrators": [
                    {
                        "id": 1,
                        "firstName": "Evelia",
                        "lastName": "Blanxart",
                        "middleName": "Robert",
                        "nickname": "Robby",
                        "bio": "I like art!",
                        "email": "everblanxart@gmail.com",
                        "created": "2019-05-20T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "images": [],
                        "dateOfBirth": "2007-04-13",
                        "phoneNumber": "0272331323",
                        "homeAddress": {
                            "streetNumber": "190",
                            "streetName": "Fort Washington Avenue",
                            "suburb": null,
                            "city": "New York",
                            "region": "New York",
                            "country": "United States",
                            "postcode": "10040"
                        }
                    },
                    {
                        "id": 11,
                        "firstName": "Mirta",
                        "lastName": "Lovel",
                        "middleName": "Juan",
                        "nickname": "Love",
                        "bio": "Pancakes",
                        "email": "mjl25@uclive.ac.nz",
                        "created": "2021-01-20T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "images": [],
                        "dateOfBirth": "1999-02-22",
                        "phoneNumber": "0273321116",
                        "homeAddress": {
                            "streetNumber": "32",
                            "streetName": "Hunter Avenue",
                            "suburb": null,
                            "city": "Fairview Shores",
                            "region": "Florida",
                            "country": "United States",
                            "postcode": "32804"
                        }
                    },
                    {
                        "id": 13,
                        "firstName": "Ife",
                        "lastName": "Weston",
                        "middleName": "Missie",
                        "nickname": "Missie",
                        "bio": "Miss me.",
                        "email": "missie@gmail.com",
                        "created": "2014-02-14T00:00",
                        "role": "USER",
                        "businessesAdministered": [
                            null
                        ],
                        "images": [],
                        "dateOfBirth": "2004-05-17",
                        "phoneNumber": "0271316323",
                        "homeAddress": {
                            "streetNumber": "3434",
                            "streetName": "Russell Street",
                            "suburb": null,
                            "city": "Detroit",
                            "region": "Michigan",
                            "country": "United States",
                            "postcode": "48207"
                        }
                    }
                ],
                "primaryAdministratorId": 10,
                "name": "Sunburst Waste",
                "description": "Description",
                "address": {
                    "streetNumber": "1849",
                    "streetName": "C Street Northwest",
                    "suburb": null,
                    "city": "Washington",
                    "region": "District of Columbia",
                    "country": "United States",
                    "postcode": "20240"
                },
                "businessType": "CHARITABLE_ORGANISATION",
                "created": "2021-02-14T00:00",
                "currencySymbol": null,
                "currencyCode": null
            },
            status: 200
        };
        Api.getBusiness.mockImplementation(() => Promise.resolve(businessResponse));

        const response = {
            "data": {
                "features": [
                    {
                        "geometry": {
                            "coordinates": [
                                172.5846033,
                                -43.5280654
                            ],
                            "type": "Point"
                        },
                        "type": "Feature",
                        "properties": {
                            "osm_id": 5264937246,
                            "country": "New Zealand",
                            "city": "Christchurch",
                            "countrycode": "NZ",
                            "postcode": "8041",
                            "locality": "Upper Riccarton",
                            "county": "Christchurch City",
                            "type": "house",
                            "osm_type": "N",
                            "osm_key": "place",
                            "housenumber": "20",
                            "street": "Kirkwood Avenue",
                            "district": "Upper Riccarton",
                            "osm_value": "house",
                            "state": "Canterbury"
                        }
                    },
                    {
                        "geometry": {
                            "coordinates": [
                                172.5858826,
                                -43.525779
                            ],
                            "type": "Point"
                        },
                        "type": "Feature",
                        "properties": {
                            "osm_id": 7611966930,
                            "country": "New Zealand",
                            "countrycode": "NZ",
                            "postcode": "8041",
                            "locality": "Upper Riccarton",
                            "county": "Christchurch City",
                            "type": "house",
                            "osm_type": "N",
                            "osm_key": "amenity",
                            "housenumber": "20",
                            "street": "Kirkwood Avenue",
                            "district": "Halswell-Hornby-Riccarton Community",
                            "osm_value": "charging_station",
                            "state": "Canterbury"
                        }
                    }
                ],
                "type": "FeatureCollection"
            },
            "status": 200
        }
        AddressAPI.addressQuery.mockImplementation(() => Promise.resolve(response));

        editBusinessSpy = jest.spyOn(EditBusinessProfile.methods, 'editBusiness');

        Cookies.get = jest.fn().mockImplementation(() => "1"); // mock all cookies

        wrapper = shallowMount(
            EditBusinessProfile,
            {
                mocks: {
                    $router,
                    $route
                }
            });
    });

    afterEach( () => {
        jest.clearAllMocks();
    })

    test('Test request returns the correct list of addresses when requesting data from the Komoot Photon API ',  async () => {
        jest.spyOn(document, 'getElementById').mockImplementation(() => {
            return { value: "20 Kirkwood Avenue, Upper"};
        });

        await wrapper.vm.request();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.addresses).toEqual([
            "20 Kirkwood Avenue,  Upper Riccarton, Christchurch, 8041, Canterbury, New Zealand",
            "20 Kirkwood Avenue,  Halswell-Hornby-Riccarton Community, 8041, Canterbury, New Zealand"
        ])
    })

    test('Test request returns an empty list of addresses when user has only autofilled two characters',  async () => {
        jest.spyOn(document, 'getElementById').mockImplementation(() => {
            return { value: "20"};
        });

        await wrapper.vm.request();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.addresses).toEqual([]);
    })

    test('Test user is pushed to NoBusiness page when retrieving a business results in a 406.',  async () => {
        const error = {
            response: {
                status: 406
            }
        }
        Api.getBusiness.mockImplementation(() => Promise.reject(error));

        await wrapper.vm.retrieveBusiness(1);
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({name: "NoBusiness"});
    })

    test('Test user is pushed to InvalidToken page when retrieving a business results in a 401.',  async () => {
        const error = {
            response: {
                status: 401
            }
        }
        Api.getBusiness.mockImplementation(() => Promise.reject(error));

        await wrapper.vm.retrieveBusiness(1);
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({name: "InvalidToken"});
    })

    test('Test the trimTextInputFields method correctly trims various fields.', async () => {
        const name = "New World";
        const type = "Retail Trade";
        const description = "This is a description.";
        const country = "New     Zealand";
        const city = "Christchurch";
        const postcode = "8 0 4 1";
        const region = "";
        const streetNumber = " ";
        const streetName = "   ";
        const suburb = "Upper Riccarton";

        wrapper.vm.$data.businessName = "  " + name + "  "; // leading and trailing whitespace
        wrapper.vm.$data.businessType = "   " + type; // leading whitespace
        wrapper.vm.$data.description = description + "     "; // trailing whitespace
        wrapper.vm.$refs.country.value = country; // whitespace in the middle should not be trimmed
        wrapper.vm.$refs.city.value = city; // no whitespace
        wrapper.vm.$refs.postcode.value = postcode; // whitespace in the middle should not be trimmed
        wrapper.vm.$refs.region.value = region; // empty string
        wrapper.vm.$refs.streetNumber.value = streetNumber; // all whitespace
        wrapper.vm.$refs.streetName.value = streetName; // all whitespace
        wrapper.vm.$refs.suburb.value = suburb; // whitespace in the middle should not be trimmed

        wrapper.vm.trimTextInputFields();

        expect(wrapper.vm.$data.businessName).toEqual(name);
        expect(wrapper.vm.$data.businessType).toEqual(type);
        expect(wrapper.vm.$data.description).toEqual(description);
        expect(wrapper.vm.$refs.country.value).toEqual(country);
        expect(wrapper.vm.$refs.city.value).toEqual(city);
        expect(wrapper.vm.$refs.postcode.value).toEqual(postcode);
        expect(wrapper.vm.$refs.region.value).toEqual(region);
        expect(wrapper.vm.$refs.streetNumber.value).toEqual("");
        expect(wrapper.vm.$refs.streetName.value).toEqual("");
        expect(wrapper.vm.$refs.suburb.value).toEqual(suburb);
    })

    test('Test the getErrorMessages method when business type is a valid business type.', async () => {
        wrapper.vm.$data.businessType = "Retail Trade"; // valid business type

        wrapper.vm.getErrorMessages();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.$data.businessTypeErrorMsg).toEqual(""); // empty string since no error.
    })

    test('Test the getErrorMessages method when business type is a invalid business type.', async () => {
        wrapper.vm.$data.businessType = "Not Valid"; // invalid business type

        wrapper.vm.getErrorMessages();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.$data.businessTypeErrorMsg).toEqual("This field is required!");
    })

    test('Test the processEdit method does not call editBusiness method when an input field is invalid.', async () => {
        wrapper.vm.$data.businessType = "Not Valid"; // invalid business type

        const event = { preventDefault: jest.fn() };
        wrapper.vm.processEdit(event);

        expect(editBusinessSpy).toHaveBeenCalledTimes(0); // should not have been called
    })

    test('Test the processEdit method does not call editBusiness method when country has been change because' +
        'the user needs to be notified about currency changes', async () => {
        wrapper.vm.$data.orginalCountry = "United States";
        wrapper.vm.$refs.country.value = "New Zealand"; // country has been changed
        wrapper.vm.$refs.currencyChangeModal.showModal = jest.fn(); // mock opening modal

        const event = { preventDefault: jest.fn() };
        wrapper.vm.processEdit(event);

        expect(editBusinessSpy).toHaveBeenCalledTimes(0); // should not have been called
    })

    test('Test the processEdit method calls editBusiness method when country has not been changed, and there are ' +
        'no input fields that are invalid.', async () => {
        wrapper.vm.$data.orginalCountry = "United States";
        wrapper.vm.$refs.country.value = "United States"; // has not been changed

        await wrapper.vm.$nextTick();
        const event = { preventDefault: jest.fn() };
        wrapper.vm.processEdit(event);
        await wrapper.vm.$nextTick();

        expect(editBusinessSpy).toHaveBeenCalledTimes(1); // should have been called
    })

    test('Test the currencyChange method sets currency code and symbol when not null.', async () => {
        const code = "NZD";
        const symbol = "$";

        wrapper.vm.currencyChange(code, symbol);

        expect(wrapper.vm.$data.currencyCode).toEqual(code);
        expect(wrapper.vm.$data.currencySymbol).toEqual(symbol);
        expect(editBusinessSpy).toHaveBeenCalledTimes(1); // should have been called (editBusiness method)
    })

    test('Test the currencyChange method does not set currency code and symbol when they are null.', async () => {
        const originalCode = "USD";
        const originalSymbol = "$";

        wrapper.vm.$data.currencyCode = originalCode;
        wrapper.vm.$data.currencySymbol = originalSymbol;

        const code = null;
        const symbol = null;

        wrapper.vm.currencyChange(code, symbol);

        expect(wrapper.vm.$data.currencyCode).toEqual(originalCode);
        expect(wrapper.vm.$data.currencySymbol).toEqual(originalSymbol);
        expect(editBusinessSpy).toHaveBeenCalledTimes(1); // should have been called (editBusiness method)
    })

    test('Test checkIsAdmin method takes user to timeout page when no response is received', async () => {
        const error = {
            request: "Hello"
        }

        Api.getUser.mockImplementation(() => Promise.reject(error));

        wrapper.vm.checkIsAdmin(1, 1);
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: "/timeout"});
    })

    test('Test checkIsAdmin method takes user to noUser page when 406 is received', async () => {
        const error = {
            response: {
                status: 406
            }
        }

        Api.getUser.mockImplementation(() => Promise.reject(error));

        wrapper.vm.checkIsAdmin(1, 1);
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: "/noUser"});
    })

    test('Test checkIsAdmin method takes user to invalidtoken page when 401 is received', async () => {
        const error = {
            response: {
                status: 401
            }
        }

        Api.getUser.mockImplementation(() => Promise.reject(error));

        wrapper.vm.checkIsAdmin(1, 1);
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: "/invalidtoken"});
    })

    test('Test checkIsAdmin method takes user to noUser page when random error (500) is received', async () => {
        const error = {
            response: {
                status: 500
            }
        }

        Api.getUser.mockImplementation(() => Promise.reject(error));

        wrapper.vm.checkIsAdmin(1, 1);
        await wrapper.vm.$nextTick();

        expect($router.push).toHaveBeenCalledWith({path: "/noUser"});
    })

    //TODO: finish this
    describe("Testing the response on save attempt", () => {

        const mockFullApiResponse = {
            status: 200,
            data: {
                firstName: "first",
                middleName: "second",
                lastName: "third",
                nickname: "nick",
                homeAddress: {
                    streetNumber: "123",
                    streetName: "Road Street",
                    suburb: "Suburb",
                    city: "City",
                    postcode: "1234",
                    region: "Region",
                    country: "Country"
                },
                email: "email@example.com",
                bio: "Biography",
                phoneNumber: "0210210210",
                dateOfBirth: "1999-2-5"
            }
        }

        const userResponse = {
            "data": {
                "id": 1,
                "firstName": "Evelia",
                "lastName": "Blanxart",
                "middleName": "Robert",
                "nickname": "Robby",
                "bio": "I like art!",
                "email": "everblanxart@gmail.com",
                "created": "2019-05-20T00:00",
                "role": "USER",
                "businessesAdministered": [
                    {
                        "id": 1,
                        "administrators": [
                            null
                        ],
                        "primaryAdministratorId": 10,
                        "name": "Sunburst Waste",
                        "description": "Description",
                        "address": {
                            "streetNumber": "1849",
                            "streetName": "C Street Northwest",
                            "suburb": null,
                            "city": "Washington",
                            "region": "District of Columbia",
                            "country": "United States",
                            "postcode": "20240"
                        },
                        "businessType": "CHARITABLE_ORGANISATION",
                        "created": "2021-02-14T00:00",
                        "currencySymbol": "$",
                        "currencyCode": "USD"
                    }
                ],
                "images": [],
                "dateOfBirth": "2007-04-13",
                "phoneNumber": "0272331323",
                "homeAddress": {
                    "streetNumber": "190",
                    "streetName": "Fort Washington Avenue",
                    "suburb": null,
                    "city": "New York",
                    "region": "New York",
                    "country": "United States",
                    "postcode": "10040"
                }
            },
            "status": 200,
        }

        let editBusinessProfileWrapper;
        let mockEditResponse;
        let id;
        let $router;

        beforeEach(async () => {

            id = 1;

            let $route = {
                params: {
                    id: id
                }
            }

            $router = {
                push: jest.fn()
            }

            Api.getUser.mockImplementation(() => Promise.resolve(mockFullApiResponse));
            Cookies.get.mockReturnValue(id);

            editBusinessProfileWrapper = shallowMount(EditBusinessProfile, {
                localVue,
                mocks: {
                    $route,
                    $router
                }
            });
            await editBusinessProfileWrapper.vm.$nextTick();
        })

        test("Application routes to business profile on 200 response", async () => {
            mockEditResponse = {
                status: 200
            }

            Api.getUser.mockImplementation(() => Promise.resolve(userResponse))

            editBusinessProfileWrapper.vm.$data.description = "New description"

            Api.editBusiness.mockImplementation(() => Promise.resolve(mockEditResponse))

            await editBusinessProfileWrapper.vm.editBusiness({preventDefault: jest.fn()});

            await editBusinessProfileWrapper.vm.$nextTick();

            expect($router.push).toHaveBeenCalledWith(`/businessProfile/${1}`)
        })
    })
})