/**
 * @jest-environment jsdom
 */

import {shallowMount} from '@vue/test-utils';
import EditBusinessProfile from "../src/views/EditBusinessProfile";
import AddressAPI from "../src/addressInstance";
import Api from "../src/Api";
import {beforeEach, describe, expect, jest, test} from "@jest/globals";

jest.mock("../src/addressInstance");
jest.mock("../src/Api");

describe("Testing methods in EditBusinessProfile", () => {

    let wrapper;
    let $router;
    let $route;

    beforeEach(() => {
        $router = {
            push: jest.fn()
        };

        $route = {
            params: {
                id: 2
            }
        };

        const businessResponse = {
            data: {
                "id": 2,
                "administrators": [
                    {
                        "id": 10,
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
})