import {shallowMount} from "@vue/test-utils";
import PrimaryAdminModification from "../../src/components/business/PrimaryAdminModification"
import Api from "../../src/Api";
import {jest} from "@jest/globals";

jest.mock("../../src/Api");
jest.mock("js-cookie");

let wrapper;
let $route;
let $router;
let result;

let adminList = []

const createWrapper = (() => {
    wrapper = shallowMount(PrimaryAdminModification, {
        propsData: {
            businessInfo: {
                "primaryAdministratorId": null,
                "name": "Brink Food",
                "description": "Description",
                "address": {
                    "streetNumber": "86",
                    "streetName": "High Street",
                    "suburb": "",
                    "city": "Picton",
                    "region": "Marlborough",
                    "country": "New Zealand",
                    "postcode": "7220"
                },
                "businessType": "Retail Trade",
                "currencySymbol": "$",
                "currencyCode": "NZD"
            },
            adminList: adminList
        },
        mocks: {
            $router,
            $route
        },
    });
})


describe("Testing getPrimaryImageSrc function", () => {

    test("Testing when images is empty, it will return a default image", () => {
        let images = []

        createWrapper();
        result = wrapper.vm.getPrimaryImageSrc(images);

        expect(result).toStrictEqual("test-file-stub");
    })

    test("Testing when images is one image, the function will return its thumbnailFilename address", () => {
        let images = [
            {
                filename: "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                id: 1,
                isPrimary: true,
                thumbnailFilename: "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png"
            },
            {
                filename: "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                id: 2,
                isPrimary: false,
                thumbnailFilename: "storage/images/thumbnail13c86224-aa7b-4860-8617-b42a"
            }]
        createWrapper();
        result = wrapper.vm.getPrimaryImageSrc(images);

        expect(result).toStrictEqual("undefined/storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png");
    })

    test("Testing when images is more then one image, the function will return primary image's thumbnailFilename address", () => {
        let images = [
            {
                filename: "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                id: 1,
                isPrimary: false,
                thumbnailFilename: "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png"
            },
            {
                filename: "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                id: 2,
                isPrimary: true,
                thumbnailFilename: "storage/images/thumbnail13c86224-aa7b-4860-8617-b42a"
            }]
        createWrapper();
        result = wrapper.vm.getPrimaryImageSrc(images);

        expect(result).toStrictEqual("undefined/storage/images/thumbnail13c86224-aa7b-4860-8617-b42a");
    })

})

describe("Testing primaryAdminCandidate button", () => {

    test("Testing when adminList is empty, no primaryAdminCandidate will be display", () => {
        adminList = [];
        createWrapper();

        expect(wrapper.find("#primaryAdminCandidate_15").exists()).toBeFalsy();
        expect(wrapper.find("#primaryAdminCandidate_16").exists()).toBeFalsy();
        expect(wrapper.find("#primaryAdminCandidate_17").exists()).toBeFalsy();
    })

    test("Testing when adminList is empty, there is a message will shown", () => {
        adminList = [];
        createWrapper();

        expect(wrapper.find("#noAdminInListMessage").exists()).toBeTruthy();
    })

    test("Testing when adminList have a admin, one primaryAdminCandidate will be display", () => {
        adminList = [{
            "id": 15,
            "firstName": "Alyce",
            "lastName": "Gibbs",
            "middleName": "Teddie",
            "nickname": "Teddie",
            "bio": "Looking for cheap teddies.",
            "email": "alycegibbs@gmail.com",
            "created": "2019-03-28T00:00",
            "role": "USER",
            "businessesAdministered": [],
            "images": [{
                "id": 1,
                "filename": "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "thumbnailFilename": "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "isPrimary": true
            }, {
                "id": 2,
                "filename": "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "thumbnailFilename": "storage/images/thumbnail13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "isPrimary": false
            }],
            "dateOfBirth": "1965-02-19",
            "phoneNumber": "0271316943",
            "homeAddress": {
                "streetNumber": "199",
                "streetName": "Kwa Jongo Street",
                "suburb": null,
                "city": "Dar es Salaam",
                "region": "Coastal Zone",
                "country": "Tanzania",
                "postcode": "78570"
            }
        }];
        createWrapper();

        expect(wrapper.find("#primaryAdminCandidate_15").exists()).toBeTruthy();
        expect(wrapper.find("#primaryAdminCandidate_16").exists()).toBeFalsy();
        expect(wrapper.find("#primaryAdminCandidate_17").exists()).toBeFalsy();
    })

    test("Testing when adminList three admin, three primaryAdminCandidate will be display", () => {
        adminList = [
            {
                "id": 15,
                "firstName": "Alyce",
                "lastName": "Gibbs",
                "middleName": "Teddie",
                "nickname": "Teddie",
                "bio": "Looking for cheap teddies.",
                "email": "alycegibbs@gmail.com",
                "created": "2019-03-28T00:00",
                "role": "USER",
                "businessesAdministered": [],
                "images": [{
                    "id": 1,
                    "filename": "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                    "thumbnailFilename": "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png",
                    "isPrimary": true
                }, {
                    "id": 2,
                    "filename": "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                    "thumbnailFilename": "storage/images/thumbnail13c86224-aa7b-4860-8617-b42ad21125f3.png",
                    "isPrimary": false
                }],
                "dateOfBirth": "1965-02-19",
                "phoneNumber": "0271316943",
                "homeAddress": {
                    "streetNumber": "199",
                    "streetName": "Kwa Jongo Street",
                    "suburb": null,
                    "city": "Dar es Salaam",
                    "region": "Coastal Zone",
                    "country": "Tanzania",
                    "postcode": "78570"
                }
            },
            {
                "id": 17,
                "firstName": "Bennett",
                "lastName": "Garner",
                "middleName": "Finola",
                "nickname": "Garnish",
                "bio": "I like apples.",
                "email": "garnish@yahoo.com",
                "created": "2021-02-02T00:00",
                "role": "USER",
                "businessesAdministered": [],
                "images": [],
                "dateOfBirth": "1989-09-12",
                "phoneNumber": "0271316096",
                "homeAddress": {
                    "streetNumber": "87",
                    "streetName": "Ansancheonnam-ro",
                    "suburb": null,
                    "city": "Ansan-si",
                    "region": "Gyeonggi-do",
                    "country": "South Korea",
                    "postcode": "15483"
                }
            },
            {
                "id": 16,
                "firstName": "Casandra",
                "lastName": "Dane",
                "middleName": "Fen",
                "nickname": "Cassie",
                "bio": "I am not from Denmark!",
                "email": "cassie@dane.com",
                "created": "2018-12-13T00:00",
                "role": "USER",
                "businessesAdministered": [],
                "images": [],
                "dateOfBirth": "1982-01-17",
                "phoneNumber": "0271316226",
                "homeAddress": {
                    "streetNumber": "76",
                    "streetName": "Milltown Road",
                    "suburb": null,
                    "city": "Strabane",
                    "region": "Northern Ireland",
                    "country": "United Kingdom",
                    "postcode": "BT82 8AS"
                }
            }];
        createWrapper();

        expect(wrapper.find("#primaryAdminCandidate_15").exists()).toBeTruthy();
        expect(wrapper.find("#primaryAdminCandidate_16").exists()).toBeTruthy();
        expect(wrapper.find("#primaryAdminCandidate_17").exists()).toBeTruthy();
    })
})

describe("Test setPrimaryAdmin function", () => {
    test("Test an error will be display when 400 be receive", async () => {
        adminList = [{
            "id": 15,
            "firstName": "Alyce",
            "lastName": "Gibbs",
            "middleName": "Teddie",
            "nickname": "Teddie",
            "bio": "Looking for cheap teddies.",
            "email": "alycegibbs@gmail.com",
            "created": "2019-03-28T00:00",
            "role": "USER",
            "businessesAdministered": [],
            "images": [{
                "id": 1,
                "filename": "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "thumbnailFilename": "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "isPrimary": true
            }, {
                "id": 2,
                "filename": "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "thumbnailFilename": "storage/images/thumbnail13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "isPrimary": false
            }],
            "dateOfBirth": "1965-02-19",
            "phoneNumber": "0271316943",
            "homeAddress": {
                "streetNumber": "199",
                "streetName": "Kwa Jongo Street",
                "suburb": null,
                "city": "Dar es Salaam",
                "region": "Coastal Zone",
                "country": "Tanzania",
                "postcode": "78570"
            }
        }];

        await Api.editBusiness.mockImplementation(() => Promise.reject(
            {
                response: {
                    status: 400
                }
            }));
        await createWrapper();

        await wrapper.vm.setPrimaryAdmin();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.toastErrorMessage).toBe('400 Bad request; invalid business data');
    })

    test("Test an error will be display when 403 be receive", async () => {
        adminList = [{
            "id": 15,
            "firstName": "Alyce",
            "lastName": "Gibbs",
            "middleName": "Teddie",
            "nickname": "Teddie",
            "bio": "Looking for cheap teddies.",
            "email": "alycegibbs@gmail.com",
            "created": "2019-03-28T00:00",
            "role": "USER",
            "businessesAdministered": [],
            "images": [{
                "id": 1,
                "filename": "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "thumbnailFilename": "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "isPrimary": true
            }, {
                "id": 2,
                "filename": "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "thumbnailFilename": "storage/images/thumbnail13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "isPrimary": false
            }],
            "dateOfBirth": "1965-02-19",
            "phoneNumber": "0271316943",
            "homeAddress": {
                "streetNumber": "199",
                "streetName": "Kwa Jongo Street",
                "suburb": null,
                "city": "Dar es Salaam",
                "region": "Coastal Zone",
                "country": "Tanzania",
                "postcode": "78570"
            }
        }];

        await Api.editBusiness.mockImplementation(() => Promise.reject(
            {
                response: {
                    status: 403
                }
            }));
        await createWrapper();

        await wrapper.vm.setPrimaryAdmin();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.toastErrorMessage).toBe('403 Unexpected error occurred!');
    })

    test("Test an error will be display when 406 be receive", async () => {
        adminList = [{
            "id": 15,
            "firstName": "Alyce",
            "lastName": "Gibbs",
            "middleName": "Teddie",
            "nickname": "Teddie",
            "bio": "Looking for cheap teddies.",
            "email": "alycegibbs@gmail.com",
            "created": "2019-03-28T00:00",
            "role": "USER",
            "businessesAdministered": [],
            "images": [{
                "id": 1,
                "filename": "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "thumbnailFilename": "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "isPrimary": true
            }, {
                "id": 2,
                "filename": "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "thumbnailFilename": "storage/images/thumbnail13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "isPrimary": false
            }],
            "dateOfBirth": "1965-02-19",
            "phoneNumber": "0271316943",
            "homeAddress": {
                "streetNumber": "199",
                "streetName": "Kwa Jongo Street",
                "suburb": null,
                "city": "Dar es Salaam",
                "region": "Coastal Zone",
                "country": "Tanzania",
                "postcode": "78570"
            }
        }];

        await Api.editBusiness.mockImplementation(() => Promise.reject(
            {
                response: {
                    status: 406
                }
            }));
        await createWrapper();

        await wrapper.vm.setPrimaryAdmin();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.toastErrorMessage).toBe('406 Unexpected error occurred!');
    })

    test("Test an error will be display when 409 be receive", async () => {
        adminList = [{
            "id": 15,
            "firstName": "Alyce",
            "lastName": "Gibbs",
            "middleName": "Teddie",
            "nickname": "Teddie",
            "bio": "Looking for cheap teddies.",
            "email": "alycegibbs@gmail.com",
            "created": "2019-03-28T00:00",
            "role": "USER",
            "businessesAdministered": [],
            "images": [{
                "id": 1,
                "filename": "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "thumbnailFilename": "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "isPrimary": true
            }, {
                "id": 2,
                "filename": "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "thumbnailFilename": "storage/images/thumbnail13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "isPrimary": false
            }],
            "dateOfBirth": "1965-02-19",
            "phoneNumber": "0271316943",
            "homeAddress": {
                "streetNumber": "199",
                "streetName": "Kwa Jongo Street",
                "suburb": null,
                "city": "Dar es Salaam",
                "region": "Coastal Zone",
                "country": "Tanzania",
                "postcode": "78570"
            }
        }];

        await Api.editBusiness.mockImplementation(() => Promise.reject(
            {
                response: {
                    status: 409
                }
            }));
        await createWrapper();

        await wrapper.vm.setPrimaryAdmin();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.businessNameErrorMsg).toBe("Business with name already exists");
    })

    test("Test an error will be display when error response not exist", async () => {
        adminList = [{
            "id": 15,
            "firstName": "Alyce",
            "lastName": "Gibbs",
            "middleName": "Teddie",
            "nickname": "Teddie",
            "bio": "Looking for cheap teddies.",
            "email": "alycegibbs@gmail.com",
            "created": "2019-03-28T00:00",
            "role": "USER",
            "businessesAdministered": [],
            "images": [{
                "id": 1,
                "filename": "storage/images/0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "thumbnailFilename": "storage/images/thumbnail0df06370-3cd5-4be0-8239-716b77003ac0.png",
                "isPrimary": true
            }, {
                "id": 2,
                "filename": "storage/images/13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "thumbnailFilename": "storage/images/thumbnail13c86224-aa7b-4860-8617-b42ad21125f3.png",
                "isPrimary": false
            }],
            "dateOfBirth": "1965-02-19",
            "phoneNumber": "0271316943",
            "homeAddress": {
                "streetNumber": "199",
                "streetName": "Kwa Jongo Street",
                "suburb": null,
                "city": "Dar es Salaam",
                "region": "Coastal Zone",
                "country": "Tanzania",
                "postcode": "78570"
            }
        }];

        await Api.editBusiness.mockImplementation(() => Promise.reject({}));
        await createWrapper();

        await wrapper.vm.setPrimaryAdmin();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.toastErrorMessage).toBe("Unexpected error occurred!");
    })
})
