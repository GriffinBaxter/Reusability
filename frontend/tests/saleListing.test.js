import {test, expect, describe, jest, beforeEach} from "@jest/globals"
import listing from "../src/views/SaleListing"
import defaultImage from "./../public/default-product.jpg"
import {createLocalVue, shallowMount} from "@vue/test-utils";
import VueLogger from "vuejs-logger";
import Api from "./../src/Api";

const localVue = createLocalVue();
const resourcePath = "some.test.url.com/";
let wrapper;
localVue.use(VueLogger, {isEnabled: false})

jest.mock("../src/Api");
jest.mock("js-cookie");

beforeEach(() => {
    Api.getServerResourcePath.mockImplementation( (stringPart) =>  resourcePath + stringPart);
})

describe("Tests for getMainImage function.", () => {

    test("Testing that getMainImage returns default image if the index is below zero", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                mainImageIndex: -1,
                saleImages: [
                    {filename: "testing.png"}
                ]
            }},
        });

        expect(wrapper.vm.getMainImage()).toBe(defaultImage);
    });

    test("Testing that getMainImage returns the zero index image, when index is zero", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                mainImageIndex: 0,
                saleImages: [
                    {filename: "testing0.png"},
                    {filename: "testing1.png"}
                ]
            }},
        });

        expect(wrapper.vm.getMainImage()).toBe(resourcePath+"testing0.png");
    });

    test("Testing that getMainImage returns the 1st index image, when given index 1.", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                mainImageIndex: 1,
                saleImages: [
                    {filename: "testing0.png"},
                    {filename: "testing1.png"}
                ]
            }},
        });

        expect(wrapper.vm.getMainImage()).toBe(resourcePath+"testing1.png");
    });

    test("Testing that getMainImage returns the default image, when the index is equal to the length of saleImages", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                mainImageIndex: 4,
                saleImages: [
                    {filename: "testing0.png"},
                    {filename: "testing1.png"},
                    {filename: "testing2.png"},
                    {filename: "testing3.png"}
                ]
            }},
        });

        expect(wrapper.vm.getMainImage()).toBe(defaultImage);
    });

    test("Checking that it returns the default image, when the filename is not specified.", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                mainImageIndex: 0,
                saleImages: [
                    {},
                    {}
                ]
            }},
        });

        expect(wrapper.vm.getMainImage()).toBe(defaultImage);
    });

})

describe("Tests for getCarouselImage function", () => {

    test("Testing that getCarouselImage returns default image if the index is below zero", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                saleImages: [
                    {thumbnailFilename: "testing.png"}
                ]
            }},
        });

        expect(wrapper.vm.getCarouselImage(-2)).toBe(defaultImage);
    });

    test("Testing that getCarouselImage returns the zero index image, when index is zero", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                saleImages: [
                    {thumbnailFilename: "testing0.png"},
                    {thumbnailFilename: "testing1.png"}
                ]
            }},
        });

        expect(wrapper.vm.getCarouselImage(0)).toBe(resourcePath+"testing0.png");
    });

    test("Testing that getCarouselImage returns the 1st index image, when given index 1.", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                saleImages: [
                    {thumbnailFilename: "testing0.png"},
                    {thumbnailFilename: "testing1.png"}
                ]
            }},
        });

        expect(wrapper.vm.getCarouselImage(1)).toBe(resourcePath+"testing1.png");
    });

    test("Testing that getCarouselImage returns the default image, when the index is equal to the length of saleImages", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                saleImages: [
                    {thumbnailFilename: "testing0.png"},
                    {thumbnailFilename: "testing1.png"},
                    {thumbnailFilename: "testing2.png"},
                    {thumbnailFilename: "testing3.png"}
                ]
            }},
        });

        expect(wrapper.vm.getCarouselImage(4)).toBe(defaultImage);
    });

    test("Checking that it returns the default image, when the thumbnailFilename is not specified.", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() { return  {
                saleImages: [
                    {},
                    {}
                ]
            }},
        });

        expect(wrapper.vm.getCarouselImage(0)).toBe(defaultImage);
    });

})

describe("Tests for getVisibleImages function", () => {

    test("Testing that for index -3, and 3 images we get the correct values", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: -3,
                    carouselNumImages: 3,
                    saleImages: [
                        {},
                        {},
                        {}
                    ]
                }
            }
        });

        expect(wrapper.vm.getVisibleImages()).toStrictEqual([0, 1, 2])
    })

    test("Testing that for index 0, and 3 images we get the correct values", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 0,
                    carouselNumImages: 3,
                    saleImages: [
                        {},
                        {},
                        {}
                    ]
                }
            }
        });

        expect(wrapper.vm.getVisibleImages()).toStrictEqual([0, 1, 2])
    })

    test("Testing that for index 1, and 3 images we get the correct values", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 1,
                    carouselNumImages: 3,
                    saleImages: [
                        {},
                        {},
                        {}
                    ]
                }
            }
        });

        expect(wrapper.vm.getVisibleImages()).toStrictEqual([1, 2, 0])
    })

    test("Testing that for index larger then number of images we get the correct values", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 7,
                    carouselNumImages: 3,
                    saleImages: [
                        {},
                        {},
                        {}
                    ]
                }
            }
        });

        expect(wrapper.vm.getVisibleImages()).toStrictEqual([1, 2, 0])
    })

    test("Testing that for an equal number of images to the index we get the correct values", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 5,
                    carouselNumImages: 5,
                    saleImages: [
                        {},
                        {},
                        {}
                    ]
                }
            }
        });

        expect(wrapper.vm.getVisibleImages()).toStrictEqual([2, 0, 1, 2, 0])
    })

    test("Testing for a zero number of carousel images we get an empty array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 3,
                    carouselNumImages: 0,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {}
                    ]
                }
            }
        });

        expect(wrapper.vm.getVisibleImages()).toStrictEqual([])
    })
})

describe("Tests for boundIndex function", () => {

    test("Testing boundIndex(), with negative random index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(-5, 5)).toStrictEqual(0);
    });

    test("Testing boundIndex(), with -1 index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(-1, 4)).toStrictEqual(3);
    });

    test("Testing boundIndex(), with 0 index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(0, 5)).toStrictEqual(0);
    });

    test("Testing boundIndex(), with 1 index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(1, 7)).toStrictEqual(1);
    });

    test("Testing boundIndex(), with n-1 index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(17, 18)).toStrictEqual(17);
    });

    test("Testing boundIndex(), with n index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(30, 30)).toStrictEqual(0);
    });

    test("Testing boundIndex(), with n+1 index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(6, 5)).toStrictEqual(1);
    });

    test("Testing boundIndex(), with n+13 index", async () => {
        wrapper = shallowMount(listing, {localVue});
        expect(wrapper.vm.boundIndex(18, 5)).toStrictEqual(3);
    });
})

describe("Tests for nextImage function", () => {

    test("Testing that we get the next image at 0 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 0,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.nextImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(1);
    })

    test("Testing that we get the next image at 4 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 4,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.nextImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(0);
    })

    test("Testing that we get the next image at 5 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 5,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.nextImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(1);
    })

    test("Testing that we get the next image at 6 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 6,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.nextImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(2);
    })
})

describe("Tests for previousImage function", () => {

    test("Testing that we get the previous image at 0 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 0,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.previousImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(4);
    })

    test("Testing that we get the previous image at 4 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 4,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.previousImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(3);
    })

    test("Testing that we get the previous image at 5 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 5,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.previousImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(4);
    })

    test("Testing that we get the previous image at 6 index in 5 n array", async () => {
        wrapper = shallowMount(listing, {
            localVue,
            data() {
                return {
                    carouselStartIndex: 6,
                    saleImages: [
                        {},
                        {},
                        {},
                        {},
                        {},
                    ]
                }
            }
        })
        wrapper.vm.previousImage();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.$data.carouselStartIndex).toStrictEqual(0);
    })
})
